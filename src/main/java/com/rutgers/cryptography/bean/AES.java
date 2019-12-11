package com.rutgers.cryptography.bean;

import java.util.Base64;

public class AES {
    public String encrypt(String plainText, String key) {
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<plainText.length();i+=16){
            if(plainText.length()-i<16){
                StringBuilder temp=new StringBuilder();
                temp.append(plainText.substring(i));
                int count=16-plainText.length()+i;
                while(count-->0){
                    temp.append("#");
                }
                AESEncryptor e = new AESEncryptor(temp.toString(),key);
                sb.append(e.getCode());

            }else {
                AESEncryptor e = new AESEncryptor(plainText.substring(i, i + 16), key);
                sb.append(e.getCode());
            }
        }
        String code=sb.toString();
        return code;
    }

    public String decrypt(String code, String key) {
        StringBuilder text=new StringBuilder();
        //Each 128-bit text is encoded to 192-bit code
        for(int i=0;i<code.length();i+=24){
            AESDecryptor d=new AESDecryptor(code.substring(i,i+24), key);
            text.append(d.getPlainText());
        }
        String str=text.toString();
        int len=str.length()-1;
        while(len>0){
            if(str.charAt(len)=='#') len--;
            else break;
        }
        String result=str.substring(0,len+1);
        return result;
    }

    public String getText(int[][] decryptState) {
        StringBuilder builder = new StringBuilder();
        for (int[] a : decryptState) {
            for (int b : a) {
                builder.append(String.valueOf((char) b));
            }
        }
        return builder.toString();
    }

    public String encoder(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public int[] decoder(String encodedText) {
        byte[] bytes=Base64.getDecoder().decode(encodedText);
        int[] res = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            res[i] =bytes[i] & 0xff;
        }
        return res;
    }

    //Convert the encrypted state array into byte array for Base64 code
    public byte[] convertToBytes(int[][] state){
        byte[] res=new byte[state.length*4];
        for(int i=0;i<state.length;i++){
            for(int j=0;j<4;j++){
                res[i*4+j]=(byte)(state[i][j] & 0xff);
            }
        }
        return res;
    }

    //Convert 2D array into 3D array, to make it easier to get Nk*4 roundKeys
    public int[][][] convert2DTo3D(int[][] origin){
        int[][][] res=new int[origin.length/4][4][4];
        for(int i=0;i<res.length;i++){
            int[][] temp=new int[4][4];
            System.arraycopy(origin, i*4, temp, 0, 4);
            res[i]=temp;
        }
        return res;
    }

    //Convert short[] to short[][], usually return an initial state
    public int[][] convert1DTo2D(int[] origin){
        int[][] res = new int[origin.length/4][4];
        for(int i=0;i<res.length;i++){
            System.arraycopy(origin, i*4, res[i], 0, 4);
        }
        return res;
    }

    //Convert String into 1D short array
    public int[] convertToArr(String string) {
        byte[] bytes = string.getBytes();
        int length = bytes.length;
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            arr[i] = bytes[i];
        }
        return arr;
    }

    //xor operation in Galois Field
    public int[][] xor(int[][] first, int[][] second) {
        int[][] res = new int[first.length][4];
        int length = first.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                res[i][j] = first[i][j] ^ second[i][j];
            }
        }
        return res;
    }

    //xor operation, xor means Exclusive Or
    public int[] xor(int[] first, int[] second){
        int[] res=new int[4];
        for(int i=0;i<4;i++){
            res[i]=first[i]^second[i];
        }
        return res;
    }

    public int[][] substituteState(int[][] state,int[][] S_Box){
        for(int i=0;i<state.length;i++){
            for(int j=0;j<4;j++){
                int lowBits=state[i][j] & 0x000f;
                int highBits=(state[i][j]>>4) & 0x000f;
                state[i][j]=S_Box[highBits][lowBits];
            }
        }
        return state;
    }

    //Row shifting operation
    public int[][] shiftRows(int[][] state,int[][] shiftingTable){
        int[][] result=new int[state.length][4];
        for(int j=0;j<4;j++){
            for(int i=0;i<state.length;i++){
                result[i][j]=state[shiftingTable[i][j]][j];
            }
        }
        return result;
    }

    //Left shift a byte in a word
    public int[] leftShift(int[] row){
        int[] res=new int[4];
        res[0]=row[1];
        res[1]=row[2];
        res[2]=row[3];
        res[3]=row[0];

        return res;
    }

    public int multiply(int a, int b){
        int temp=0;
        while(b!=0) {
            if ((b & 0x01) == 1) {
                temp ^= a;
            }
            a <<= 1;
            if ((a & 0x100) > 0) {
                a ^= 0x1b;
            }
            b >>= 1;
        }
        return (temp & 0xff);
    }

    public int wordMultiply(int[] first, int[] second){
        int result=0;
        for(int i=0;i<4;i++){
            result^=multiply(first[i],second[i]);
        }
        return result;
    }

    public int[] matrixMultiply(int[] word,int[][] table){
        int[] result=new int[4];
        for(int i=0;i<4;i++){
            result[i]=wordMultiply(table[i],word);
        }
        return result;
    }

    public int[][] mixColumns(int[][] state, int[][] table){
        int[][] result=new int[state.length][4];
        for(int i=0;i<state.length;i++){
            result[i]=matrixMultiply(state[i],table);
        }
        return result;
    }

    public int[][] generateRoundKeys(int[][] Keys, int[][] S_Box, int[][] Constants){
        int[][] roundKeys=new int[44][4];
        int count=Keys.length;
        //Copy the original cipher words into the first four words of the roundKeys.
        System.arraycopy(Keys,0,roundKeys,0,count);
        //Extension based on previous words.
        for(int i=count;i<count*11;i++){
            int[] temp=roundKeys[i-1];
            if(i%count==0){

                temp=leftShift(temp);
                for(int j=0;j<4;j++){
                    int lowBits=temp[j] & 0x000f;
                    int highBits=(temp[j]>>4) & 0x000f;
                    temp[j]=S_Box[lowBits][highBits];

                }
                temp=xor(temp,Constants[i/count]);
            }
            roundKeys[i]=xor(roundKeys[i-count],temp);
        }
        return roundKeys;
    }
}
