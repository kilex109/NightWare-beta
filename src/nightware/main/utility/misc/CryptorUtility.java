package nightware.main.utility.misc;

import ru.crashdami.internalprotection.nativeobfuscator.Native;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Native
public class CryptorUtility {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;

    public static String encrypt(String data, String secretKey) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());

            byte[] result = new byte[IV_LENGTH + encryptedBytes.length];
            System.arraycopy(iv, 0, result, 0, IV_LENGTH);
            System.arraycopy(encryptedBytes, 0, result, IV_LENGTH, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            System.err.println("Encryption error: " + e.getMessage());
            return null;
        }
    }

    public static String decrypt(String encryptedData, String secretKey) {
        try {
            byte[] encryptedBytesWithIV = Base64.getDecoder().decode(encryptedData);

            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(encryptedBytesWithIV, 0, iv, 0, IV_LENGTH);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytesWithIV, IV_LENGTH, encryptedBytesWithIV.length - IV_LENGTH);
            return new String(decryptedBytes);
        } catch (Exception e) {
            System.err.println("Decryption error: " + e.getMessage());
            return null;
        }
    }

    public static String adjustKeyLength(String key) {
        int length = 32;
        if (key.length() < length) {
            while (key.length() < length) {
                key += "0";
            }
        } else if (key.length() > length) {
            key = key.substring(0, length);
        }
        return key;
    }


}