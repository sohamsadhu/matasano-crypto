// Problem 11
// An ECB/CBC detection oracle
// Now that you have ECB and CBC working:
// Write a function to generate a random AES key; that's just 16 random bytes.
// Write a function that encrypts data under an unknown key --- that is, a function that 
// generates a random key and encrypts under it.
// The function should look like:
// encryption_oracle(your-input)
// => [MEANINGLESS JIBBER JABBER]
// Under the hood, have the function append 5-10 bytes (count chosen randomly) before the 
// plaintext and 5-10 bytes after the plaintext.
// Now, have the function choose to encrypt under ECB 1/2 the time, and under CBC the 
// other half (just use random IVs each time for CBC). 
// Use rand(2) to decide which to use.
// Detect the block cipher mode the function is using each time. You should end up with a piece 
// of code that, pointed at a 
// block box that might be encrypting ECB or CBC, tells you which one is happening.

package second.blockcipher;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class EcbCbcDetectionOracle {

  public static final int KEY_LENGTH = 16;

  public byte[] getRandomBytes(int length) {
    if (length < 1) return null;
    byte[] bytes = new byte[length];
    try {
      SecureRandom.getInstanceStrong().nextBytes(bytes);
    } catch (NoSuchAlgorithmException nex) {
      nex.printStackTrace();
      return null;
    }
    return bytes;
  }

  public byte[] encryptAesEcb(byte[] plainBytes, SecretKey secretKey) {
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      return cipher.doFinal(plainBytes);
    } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException 
            | NoSuchPaddingException | BadPaddingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public byte[] decryptAesEcb() {
    return null;
  }

  public byte[] encryptAesCbc(byte[] plainBytes, SecretKey secretKey) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
      final int AES_KEYLENGTH = 128;
      byte[] iv = new byte[AES_KEYLENGTH / 8];
      SecureRandom random = new SecureRandom();
      random.nextBytes(iv);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
      return cipher.doFinal(plainBytes);
    } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException 
            | InvalidAlgorithmParameterException | BadPaddingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public byte[] decryptAesCbc() {
    return null;
  }

  public byte[] prependAppendRandomBytes(String input, int minBytes, int maxBytes) {
    byte[] inputBytes = input.getBytes();
    Random r = new Random();
    int appendByteLength = r.nextInt(maxBytes - minBytes) + minBytes;
    byte[] appendByte = getRandomBytes(appendByteLength);
    byte[] result = new byte[inputBytes.length + 2 * appendByteLength];
    for (int i = 0; i < appendByteLength; i++) {
      result[i] = appendByte[i];
    }
    int length = inputBytes.length + appendByteLength;
    for (int i = appendByteLength; i < length; i++) {
      result[i] = inputBytes[i - appendByteLength];
    }
    int appendStart = length;
    length += appendByteLength;
    for (int i = appendStart; i < length; i++) {
      result[i] = appendByte[i - appendStart];
    }
    return result;
  }

  public byte[] encryption_oracle(String input) {
    byte[] prependAppendedBytes = prependAppendRandomBytes(input, 5, 10);
    byte[] encrypted;
    Random r = new Random();
    boolean doAESEncryption = r.nextBoolean();
    try {
      KeyGenerator keyGen = KeyGenerator.getInstance("AES");
      keyGen.init(128);
      SecretKey secretKey = keyGen.generateKey();
      if (doAESEncryption) {
        encrypted = encryptAesEcb(prependAppendedBytes, secretKey);
      } else {
        encrypted = encryptAesCbc(prependAppendedBytes, secretKey);
      }
      return encrypted;
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  public String detectAesEcbOrCbc(byte[] encryptedBytes) {
    byte[] first16 = Arrays.copyOfRange(encryptedBytes, 16, 32);
    byte[] next16  = Arrays.copyOfRange(encryptedBytes, 32, 48);
    if (Arrays.equals(first16, next16)) {
      return "ECB";
    } else {
      return "CBC";
    }
  }

  public static void main(String [] args) {
    EcbCbcDetectionOracle ecbCbcDetectionOracle = new EcbCbcDetectionOracle();
    byte[] key = ecbCbcDetectionOracle.getRandomBytes(KEY_LENGTH);
    assert(key.length == KEY_LENGTH);
    System.out.println(new String(key) +" key length is "+ key.length);
  }
}
