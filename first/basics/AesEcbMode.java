// Problem 7
// The Base64-encoded content in 7.txt has been encrypted via AES-128 in ECB mode under the key
// "YELLOW SUBMARINE".
// (case-sensitive, without the quotes; exactly 16 characters; I like "YELLOW SUBMARINE" 
// because it's exactly 16 bytes long, and now you do too).
// Decrypt it. You know the key, after all.
// Easiest way: use OpenSSL::Cipher and give it AES-128-ECB as the cipher.

public class AesEcbMode {

  public void something(String file, BreakRepeatKeyXor brkxor) {
    String encodedText = brkxor.getBase64StringFromFile(file);
    byte[] encodedBytes = brkxor.convertBase64StringToBytes(encodedText);
  }

  public static void main(String [] args) {
    AesEcbMode aesEcb = new AesEcbMode();
    BreakRepeatKeyXor brkxor = new BreakRepeatKeyXor();
  }
}