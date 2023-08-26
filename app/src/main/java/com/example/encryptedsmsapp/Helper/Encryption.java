package com.example.encryptedsmsapp.Helper;

import com.example.encryptedsmsapp.MessageActivity;
import com.example.encryptedsmsapp.Models.Account;
import com.example.encryptedsmsapp.Models.Contact;
import com.example.encryptedsmsapp.Models.Message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
        private static final String SECRET_KEY = "my_super_secret_key_ho_ho_ho";
    private static final String SALT = "ssshhhhhhhhhhh!!!!";




    public static String encrypt(String strToEncrypt, String accountNum, String contactNum, String contactId, String accountId) {
        try {




            int accountNumber = Integer.parseInt(accountNum) ;
            int contactNumber = Integer.parseInt(contactNum);
            byte[] accountByte =  new byte[8];
            byte[] contactByte = new byte[8];
            accountByte = ByteBuffer.allocate(8).putInt(accountNumber).array();
            contactByte = ByteBuffer.allocate(8).putInt(contactNumber).array();



            byte[] iv = new byte[16];
            for (int i = 0; i < 16; ++i)
            {
                iv[i] = i < 8 ? accountByte[i] : contactByte[i - 8];
            }
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            String encryptedMessage =Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
            encryptedMessage = encryptedMessage + " / " + accountId + " / " + contactId;
            return  encryptedMessage;
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
    public static String decryptRecieved(String strToDecrypt, String accountNum, String contactNum) {
        try {
            int accountNumber = Integer.parseInt(accountNum) ;
            int contactNumber = Integer.parseInt(contactNum);
            byte[] accountByte =  new byte[8];
            byte[] contactByte = new byte[8];
            accountByte = ByteBuffer.allocate(8).putInt(accountNumber).array();
            contactByte = ByteBuffer.allocate(8).putInt(contactNumber).array();
            //byte[] strToDecryptBytes = Base64.getEncoder().encode(strToDecrypt.getBytes());
            //strToDecrypt =  Base64.getDecoder().decode(strToDecryptBytes).toString();

            //change sendmessagefragment so it doesnt try to decrypt non encrypted messages by having unencrypted ids at the end and checking for them first
            System.out.println(strToDecrypt);
            byte[] iv = new byte[16];
            for (int i = 0; i < 16; ++i)
            {
                iv[i] = i < 8 ? contactByte[i] : accountByte[i - 8];
            }
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    public static String decryptSent(String strToDecrypt, String accountNum, String contactNum) {
        try {
            int accountNumber = Integer.parseInt(accountNum) ;
            int contactNumber = Integer.parseInt(contactNum);
            byte[] accountByte =  new byte[8];
            byte[] contactByte = new byte[8];
            accountByte = ByteBuffer.allocate(8).putInt(accountNumber).array();
            contactByte = ByteBuffer.allocate(8).putInt(contactNumber).array();
            //byte[] strToDecryptBytes = Base64.getEncoder().encode(strToDecrypt.getBytes());
            //strToDecrypt =  Base64.getDecoder().decode(strToDecryptBytes).toString();

            //change sendmessagefragment so it doesnt try to decrypt non encrypted messages by having unencrypted ids at the end and checking for them first
            System.out.println(strToDecrypt);
            byte[] iv = new byte[16];
            for (int i = 0; i < 16; ++i)
            {
                iv[i] = i < 8 ? accountByte[i] : contactByte[i - 8];
            }
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
