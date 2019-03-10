// Problem 10
// Implement CBC mode
// CBC mode is a block cipher mode that allows us to encrypt irregularly-sized messages, despite 
// the fact that a block cipher natively only transforms individual blocks.
// In CBC mode, each ciphertext block is added to the next plaintext block before the next call to the cipher core.
// The first plaintext block, which has no associated previous ciphertext block, is added to a 
// "fake 0th ciphertext block" called the initialization vector, or IV.
// Implement CBC mode by hand by taking the ECB function you wrote earlier, making it encrypt 
// instead of decrypt (verify this by decrypting 
// whatever you encrypt to test), and using your XOR function from the previous exercise to combine them.
// The file here is intelligible (somewhat) when CBC decrypted against "YELLOW SUBMARINE" with 
// an IV of all ASCII 0 (\x00\x00\x00 &c)

package second.blockcipher;

import first.basics.AesEcbMode;
import first.basics.BreakRepeatKeyXor;
import first.basics.FixedXor;
import first.basics.HexToBase64;
import second.blockcipher.Pkcs7Padding;

import java.io.File;
import javax.crypto.Cipher;

public class CbcMode {

  public static final int ENCRYPT_MODE           = Cipher.ENCRYPT_MODE;
  public static final int DECRYPT_MODE           = Cipher.DECRYPT_MODE;
  public static final int BLOCK_SIZE             = 16;
  public static final String KEY                 = "YELLOW SUBMARINE";
  public static final String CRYPT_ALGO_MODE_PAD = "AES/CBC/PKCS5PADDING";
  public static final byte[] INITIAL_VECTOR = {
    0x00, 0x00, 0x00, 0x00, 
    0x00, 0x00, 0x00, 0x00, 
    0x00, 0x00, 0x00, 0x00, 
    0x00, 0x00, 0x00, 0x00};

  public byte[] encrypt(String plainText, AesEcbMode aes, FixedXor fxor, int blockSize, String key, 
      String cryptAlgoModePad) {
    Pkcs7Padding pkcs7Padding = new Pkcs7Padding();
    byte[] plainBytes = pkcs7Padding.padBytes(plainText.getBytes(), blockSize);
    int length        = plainBytes.length;
    byte[] result     = new byte [length];
    byte[] previous   = INITIAL_VECTOR;
    byte[] block      = new byte [blockSize];
    byte[] temp       = new byte [blockSize];
    for (int i = 0; i < (length - blockSize); i += blockSize) {
      System.arraycopy(plainBytes, i, block, 0, blockSize);
      try  {
        temp = aes.cryptBytes(fxor.xorTwoByteArrays(block, previous), key, cryptAlgoModePad, 
                  null, ENCRYPT_MODE);
      } catch(Exception e) {
        e.printStackTrace();
      }
      System.arraycopy(temp, 0, result, i, blockSize);
      System.arraycopy(temp, 0, previous, 0, blockSize);
    }
    return result;
  }

  public String decrypt(byte[] encryptedBytes, AesEcbMode aes, FixedXor fxor, int blockSize, String key) {
    StringBuilder decryptBuilder = new StringBuilder("");
    byte[] previous = INITIAL_VECTOR;
    byte[] block    = new byte [blockSize];
    for (int i = 0; i < encryptedBytes.length; i += blockSize) {
      System.arraycopy(encryptedBytes, i, block, 0, blockSize);
      try {
        decryptBuilder.append(new String(
        fxor.xorTwoByteArrays(aes.cryptBytes(block, key, null, null, DECRYPT_MODE), previous)));
      } catch(Exception e) {}
      System.arraycopy(block, 0, previous, 0, blockSize);
    }
    return decryptBuilder.toString();
  }

  public void testCbcEncryptDecrypt() {
    AesEcbMode aes = new AesEcbMode();
    BreakRepeatKeyXor brkxor = new BreakRepeatKeyXor();
    FixedXor fxor = new FixedXor();
    HexToBase64 hexToBase64 = new HexToBase64();
    File file = new File("second/blockcipher/10.txt");
    String text = brkxor.getBase64StringFromFile(file.getAbsolutePath());
    byte[] encryptedBytes = aes.getBytesFromBase64EncodedFile(file.getAbsolutePath(), brkxor);
    String decrypted = decrypt(encryptedBytes, aes, fxor, BLOCK_SIZE, KEY);
    System.out.println(decrypted);
    byte[] encrypted = encrypt(decrypted, aes, fxor, BLOCK_SIZE, KEY, CRYPT_ALGO_MODE_PAD);
    System.out.println("Encryption was success "+ text.equals(new String(encrypted)));
    System.out.println("Encryption not success. Need to let libraries do all the work.");
  }

  public static void main(String [] args) {
    CbcMode cbc = new CbcMode();
    cbc.testCbcEncryptDecrypt();
  }
}
