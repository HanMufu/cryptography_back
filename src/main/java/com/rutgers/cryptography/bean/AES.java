package com.rutgers.cryptography.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Base64;

@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class AES {
    public String encrypt(String plaintext, String key) {
        // transfer plaintext and key from one-dimension matrix
        // to (data.length / 4) x 4 matrix
        System.out.println("#####################  encryption  #####################");
        printInfo("plaintext text", plaintext, false);
        printInfo("key text", key, false);
        short[][] initialPTState = convert(convertToShorts(plaintext));
        printInfo("initial plaintext state", getStateHex(initialPTState), false);
        short[][] initialKeyState = convert(convertToShorts(key));
        printInfo("initial key state", getStateHex(initialKeyState), true);

        // obtain raw round keys
        short[][] rawRoundKeys = generateRoundKeys(initialKeyState);
        System.out.println("RoundKeys");
        printRoundKeys(rawRoundKeys);

        // make it easier to obtain a whole block of round key in a round transformation
        short[][][] roundKeys = convert(rawRoundKeys);

        short[][] finalState = coreEncrypt(initialPTState, roundKeys, AESConstants.SUBSTITUTE_BOX,
                AESConstants.CX, AESConstants.SHIFTING_TABLE);
        return encode(convert2Bytes(finalState));
    }

    public String decrypt(String encryptedText, String key) {
        System.out.println("\n\n#####################  decryption  #####################");
        printInfo("encrypted text", encryptedText, false);
        printInfo("key text", key, false);

        short[][] initialTextState = convert(decodeToShorts(encryptedText));
        short[][] initialKeyState = convert(convertToShorts(key));

        printInfo("initial encrypted state", getStateHex(initialTextState), false);
        printInfo("initial key state", getStateHex(initialKeyState), true);

        short[][] decryptState = coreDecrypt(initialTextState, initialKeyState);
        String plaintext = getOrigin(decryptState);
        printInfo("plaintext", plaintext, false);
        return plaintext;
    }

    /*
     * AES core operation
     * @param initialPTState : plain or encrypted text state array
     * @param roundKeys : the key
     * @param substituteTable : the S box for encryption or decryption
     * @param mixColumnTable : array used in Mix columns operation
     * @param shiftingTable : array that defines shifting times in Row shifting
     *
     */
    private short[][] coreEncrypt(short[][] initialPTState, short[][][] roundKeys,
                                  short[][] substituteTable,short[][] mixColumnTable,
                                  short[][] shiftingTable){

        //First round of XOR.
        short[][] state = xor(roundKeys[0], initialPTState);

        //Processing the first nine round conversion
        for(int i=0;i<9;i++){
            System.out.println("Round "+(i+1));
            //Replace the bytes in state array by the corresponding bytes in  S box
            state=substituteState(state,substituteTable);
            printInfo("Substitute Bytes", getStateHex(state),false);

            //Row Shifting
            state=shiftRows(state,shiftingTable);
            printInfo("Shift Rows", getStateHex(state),false);

            //Mix Columns
            state=mixColumns(state,mixColumnTable);
            printInfo("Mix Columns",getStateHex(state),false);

            //RoundKeys conversion
            printInfo("RoundKey",getStateHex(roundKeys[i+1]),false);
            state=xor(roundKeys[i+1],state);
            printInfo("Add RoundKeys",getStateHex(state),true);
        }

        //Processing the last round
        //In the last
        System.out.println("Round 10");
        state=substituteState(state,substituteTable);
        printInfo("Substitute Bytes", getStateHex(state),false);

        state=shiftRows(state,shiftingTable);
        printInfo("Shift Rows", getStateHex(state),false);

        printInfo("RoundKey",getStateHex(roundKeys[roundKeys.length-1]),false);
        state=xor(roundKeys[roundKeys.length-1],state);
        printInfo("Add RoundKeys",getStateHex(state),false);

        return state;
    }

    private short[][] coreDecrypt(short[][] encryptedTextState, short[][] keyState) {
        // obtain raw round keys
        short[][] rawRoundKeys = generateRoundKeys(keyState);
        System.out.println("RoundKeys");
        printRoundKeys(rawRoundKeys);

        // make it easier to obtain a whole block of round key in a round transformation
        short[][][] roundKeys = convert(rawRoundKeys);

        for (int i = 1; i < roundKeys.length - 1; i++) {
            roundKeys[i] = mixColumns(roundKeys[i], AESConstants.INVERSE_CX);
        }

        short[][][] inverseRoundKeys = inverseRoundKeys(roundKeys);
        System.out.println("inverse roundKeys");
        printRoundKeys(inverseRoundKeys);
        return coreEncrypt(encryptedTextState, inverseRoundKeys, AESConstants.
                INVERSE_SUBSTITUTE_BOX, AESConstants.INVERSE_CX, AESConstants.INVERSE_SHIFTING_TABLE);
    }

    private short[][][] inverseRoundKeys(short[][][] roundKeys) {
        short[][][] result = new short[roundKeys.length][4][4];
        int length = roundKeys.length;
        for (int i = 0; i < roundKeys.length; i++) {
            result[i] = roundKeys[length - 1 - i];
        }
        return result;
    }

    private String getOrigin(short[][] decryptState) {
        StringBuilder builder = new StringBuilder();
        for (short[] shorts : decryptState) {
            for (short s : shorts) {
                builder.append(String.valueOf((char) s));
            }
        }
        return builder.toString();
    }

    private String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private short[] decodeToShorts(String encodedText) {
        return byteToShorts(Base64.getDecoder().decode(encodedText));
    }

    private short[] byteToShorts(byte[] bytes) {
        short[] result = new short[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = (short) (bytes[i] & 0xff);
        }
        return result;
    }

    //Convert the encrypted state array into byte array for Base64 code
    private byte[] convert2Bytes(short[][] finalState){
        byte[] result=new byte[finalState.length*4];
        for(int i=0;i<finalState.length;i++){
            for(int j=0;j<4;j++){
                result[i*4+j]=(byte)(finalState[i][j] & 0xff);
            }
        }
        return result;
    }

    //Convert 2D array into 3D array, to make it easier to get Nk*4 roundKeys
    private short[][][] convert(short[][] origin){
        short[][][] result=new short[origin.length/4][4][4];
        for(int i=0;i<result.length;i++){
            short[][] temp=new short[4][4];
            System.arraycopy(origin, i*4, temp, 0, 4);
            result[i]=temp;
        }
        return result;
    }

    //Convert short[] to short[][], usually return an initial state
    private short[][] convert(short[] origin){
        short[][] result = new short[origin.length/4][4];
        for(int i=0;i<result.length;i++){
            System.arraycopy(origin, i*4, result[i], 0, 4);
        }
        return result;
    }

    //Convert String into 1D short array
    private short[] convertToShorts(String string) {
        byte[] bytes = string.getBytes();
        int length = bytes.length;
        short[] shorts = new short[length];
        for (int i = 0; i < length; i++) {
            shorts[i] = bytes[i];
        }
        return shorts;
    }

    //xor operation in Galois Field
    private short[][] xor(short[][] first, short[][] second) {
        short[][] result = new short[first.length][4];
        int length = first.length;
        for (short i = 0; i < length; i++) {
            for (short j = 0; j < length; j++) {
                result[i][j] = (short) (first[i][j] ^ second[i][j]);
            }
        }
        return result;
    }

    //xor operation, xor means Exclusive Or
    private short[] xor(short[] first, short[] second){
        short[] result=new short[4];
        for(short i=0;i<4;i++){
            result[i]=(short)(first[i]^second[i]);
        }
        return result;
    }


    private short substituteByte(short originalByte, short[][] substituteTable){
        //low 4 bits
        int low4Bits=originalByte & 0x000f;
        //high 4 bits
        int high4Bits=(originalByte>>4) & 0x000f;
        //obtain value in AESConstants.SUBSTITUTE_BOX
        return substituteTable[high4Bits][low4Bits];
    }

    private short[] substituteWord(short[] word){
        for(int i=0;i<4;i++) word[i]=substituteByte(word[i],AESConstants.SUBSTITUTE_BOX);

        return word;
    }

    private short[][] substituteState(short[][] state,short[][] substituteTable){
        for(int i=0;i<state.length;i++){
            for(int j=0;j<4;j++){
                state[i][j]=substituteByte(state[i][j],substituteTable);
            }
        }
        return state;
    }

    //Row shifting operation
    private static short[][] shiftRows(short[][] state,short[][] shiftingTable){
        short[][] result=new short[state.length][4];
        for(int j=0;j<4;j++){
            for(int i=0;i<state.length;i++){
                result[i][j]=state[shiftingTable[i][j]][j];
            }
        }
        return result;
    }

    //Left shift a byte in a word
    private short[] leftShift(short[] word){
        short[] result=new short[4];
        for(int i=0;i<4;i++){
            result[i]=word[AESConstants.LEFT_SHIFT_TABLE[i]];
        }
        return result;
    }

    private short multiply(short a, short b){
        short temp=0;
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
        return (short)(temp & 0xff);
    }

    private short wordMultiply(short[] first, short[] second){
        short result=0;
        for(int i=0;i<4;i++){
            result^=multiply(first[i],second[i]);
        }
        return result;
    }

    private short[] matrixMultiply(short[] word,short[][] table){
        short[] result=new short[4];
        for(int i=0;i<4;i++){
            result[i]=wordMultiply(table[i],word);
        }
        return result;
    }

    private short[][] mixColumns(short[][] state, short[][] table){
        short[][] result=new short[state.length][4];
        for(int i=0;i<state.length;i++){
            result[i]=matrixMultiply(state[i],table);
        }
        return result;
    }

    private short[][] generateRoundKeys(short[][] originalKey){
        short[][] roundKeys=new short[44][4];
        int count=originalKey.length;
        //Copy the original cipher words into the first four words of the roundKeys.
        System.arraycopy(originalKey,0,roundKeys,0,count);
        //Extension based on previous words.
        for(int i=count;i<count*11;i++){
            short[] temp=roundKeys[i-1];
            if(i%count==0){
                temp=xor(substituteWord(leftShift(temp)),AESConstants.R_CON[i/count]);
            }
            roundKeys[i]=xor(roundKeys[i-count],temp);
        }
        return roundKeys;
    }

    /*
     *Convert a state array to its hexadecimal string
     */
    private String getStateHex(short[][] state){
        StringBuilder sb=new StringBuilder();
        for(short[] word:state){
            sb.append(toHexString(word));
        }
        return sb.toString();
    }

    private void printRoundKeys(short[][] roundKeys){
        for(int i=0,keyOrder=1;i<roundKeys.length;i+=4,keyOrder++){
            String info=getStateHex(new short[][]{
                    roundKeys[i], roundKeys[i+1], roundKeys[i+2], roundKeys[i+3]});
            printInfo("[RoundKey " + keyOrder + "]", info, false);
        }
        System.out.println();
    }

    private void printRoundKeys(short[][][] roundKeys){
        for(int i=0;i<roundKeys.length;i++){
            String info=getStateHex(roundKeys[i]);
            printInfo("[RoundKey"+(i+1)+"]",info,false);
        }
        System.out.println();
    }

    //To format a word in hexadecimal string
    private String toHexString(short[] word){
        StringBuilder sb=new StringBuilder();
        for(short aByte:word){
            sb.append(toHexString(aByte));
        }
        return sb.toString();
    }

    //Convert a byte into hexadecimal string
    private String toHexString(short value) {
        String hexString=Integer.toHexString(value);
        if(hexString.toCharArray().length==1){
            hexString="0"+hexString;
        }
        return hexString;
    }

    //Print info to trace the process
    private void printInfo(String key, String value, boolean nextRow) {
        String msg = String.format("%-30s%-70s", key, value);
        if (nextRow) {
            msg += "\n";
        }
        System.out.println(msg);
    }

}
