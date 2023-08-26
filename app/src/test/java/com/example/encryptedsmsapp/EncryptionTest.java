package com.example.encryptedsmsapp;
import com.example.encryptedsmsapp.Helper.Encryption;

import org.junit.Test;

import static org.junit.Assert.*;
public class EncryptionTest {
    @Test
    public void DecryptionMatchesEncryption() {
        String text = "Hello";
        String encrypted = Encryption.encrypt(text, "12345678", "87654321", "1", "2");
        String[] split = encrypted.split(" ");
        String decryptedMessage = Encryption.decrypt(split[0].trim(), "87654321","12345678");
        assertEquals(text,decryptedMessage);
    }
}
