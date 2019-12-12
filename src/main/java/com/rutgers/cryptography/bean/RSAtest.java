package com.rutgers.cryptography.bean;

public class RSAtest {
    public static void main(String []args){
        /*
        test for encryption and decryption
         */

        String plaintext_s = "adsfadsfaafsdf,asdasfasdFFSFSDF1235416asdfadf";

        RSAKeyPrivate prk = new RSAKeyPrivate();
        RSAKeyPublic puk = new RSAKeyPublic();
        KeyGenerator k = new KeyGenerator(prk, puk);
        String PublicKey = puk.getPublicKey();
        String PrivateKey = prk.getPrivateKey();


        System.out.println("public key is : " + PublicKey);
        System.out.println("private key is : "+ PrivateKey);
        String cypotext = puk.encrypt(plaintext_s);
        System.out.println(cypotext);
        String decrypt_text = prk.getDecryptedText(cypotext);
        System.out.println("decrypted text is :" + decrypt_text);

        /*
        test for sign
         */
//        String plaintext_s = "adsfadsfaafsdf,asdasfasdFFSFSDF1235416asdfadf";
//        RSAKeyPrivate prk = new RSAKeyPrivate();
//        RSAKeyPublic puk = new RSAKeyPublic();
//        KeyGenerator k = new KeyGenerator(prk, puk);
//        String PublicKey = puk.getPublicKey();
//        String PrivateKey = prk.getPrivateKey();
//        System.out.println("public key is : " + PublicKey);
//        System.out.println("private key is : "+ PrivateKey);
//        String sign = prk.Sign(plaintext_s);
//        System.out.println("The sign is: " + sign);
//        String veri_text = puk.verifySign(sign);
//        System.out.println("verify sign text is :" + veri_text);
    }
}
