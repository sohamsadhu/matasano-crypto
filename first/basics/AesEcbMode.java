// Problem 7
// The Base64-encoded content in 7.txt has been encrypted via AES-128 in ECB mode under the key
// "YELLOW SUBMARINE".
// (case-sensitive, without the quotes; exactly 16 characters; I like "YELLOW SUBMARINE" 
// because it's exactly 16 bytes long, and now you do too).
// Decrypt it. You know the key, after all.
// Easiest way: use OpenSSL::Cipher and give it AES-128-ECB as the cipher.

// Just follow this link for implementation guidance https://www.owasp.org/index.php/Using_the_Java_Cryptographic_Extensions

package first.basics;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AesEcbMode {

  public static final String KEY                 = "YELLOW SUBMARINE";
  public static final String CRYPT_ALGO_MODE_PAD = "AES/ECB/NoPadding";
  public static final String KEY_ALGORITHM       = "AES";
  public static final int    CIPHER_MODE         = Cipher.DECRYPT_MODE;

  public byte[] getBytesFromBase64EncodedFile(String file, BreakRepeatKeyXor brkxor) {
    String encodedText = brkxor.getBase64StringFromFile(file);
    try {
      byte[] encodedBytes = brkxor.convertBase64StringToBytes(encodedText);
      return encodedBytes;
    } catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public byte[] cryptBytes(final byte[] cipherBytes, String key, String cryptAlgoModePad, String keyAlgorithm, 
                           final int cipherMode) {
    try {
      Cipher aesECBDecrypter = Cipher.getInstance(cryptAlgoModePad == null ? CRYPT_ALGO_MODE_PAD : cryptAlgoModePad);
      Key secretKey = new SecretKeySpec(key.getBytes(), keyAlgorithm == null ? KEY_ALGORITHM : keyAlgorithm);
      aesECBDecrypter.init(cipherMode, secretKey);
      return aesECBDecrypter.doFinal(cipherBytes);
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      System.out.println("No such algorithm exists "+ noSuchAlgorithmException);
    } catch (NoSuchPaddingException noSuchPaddingException) {
      System.out.println("No such padding exists "+ noSuchPaddingException);
    } catch (InvalidKeyException invalidKeyException) {
      System.out.println("The key is invalid "+ invalidKeyException);
    } catch (BadPaddingException badPaddingException) {
      System.out.println("The padding is bad "+ badPaddingException);
    } catch (IllegalBlockSizeException illegalBlockSizeException) {
      System.out.println("The block size is illegal "+ illegalBlockSizeException);
    } catch (IllegalArgumentException illegalArgumentException) {
      System.out.println("The arguments are illegal "+ illegalArgumentException);
    } catch (IllegalStateException illegalStateException) {
      System.out.println("Cipher seems to be in wrong state "+ illegalStateException);
    }
    return null;
  }

  public static void main(String [] args) {
    AesEcbMode aesEcb = new AesEcbMode();
    BreakRepeatKeyXor brkxor = new BreakRepeatKeyXor();
    byte[] encryptedBytes = aesEcb.getBytesFromBase64EncodedFile("7.txt", brkxor);
    if (encryptedBytes != null) {
      String s  = new String(aesEcb.cryptBytes(encryptedBytes, KEY, CRYPT_ALGO_MODE_PAD, KEY_ALGORITHM, CIPHER_MODE));
      System.out.println("Decrypted text is: "+ s);
    }
  }
}