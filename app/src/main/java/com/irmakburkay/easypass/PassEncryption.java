package com.irmakburkay.easypass;

import android.util.Base64;
import android.util.Log;

import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class PassEncryption {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String key = "mySecretPassword";

    public static Password encrypt(Password input) {
        Password data = input.copy();
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] cipherText = cipher.doFinal(data.getPass().getBytes());
            data.setPass(Base64.encodeToString(cipherText, Base64.DEFAULT));
            return data;
        } catch (Exception e) {
            Log.e("MyLOG", e.toString(), e);
        }
        return null;
    }

    public static void decrypt(Password data) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] plainText = cipher.doFinal(Base64.decode(data.getPass(), Base64.DEFAULT));
            data.setPass(new String(plainText));
        } catch (Exception e) {
            Log.e("MyLOG", e.toString(), e);
        }
    }

}
