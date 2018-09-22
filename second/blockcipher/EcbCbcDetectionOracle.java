// Problem 11
// An ECB/CBC detection oracle
// Now that you have ECB and CBC working:
// Write a function to generate a random AES key; that's just 16 random bytes.
// Write a function that encrypts data under an unknown key --- that is, a function that generates a random key and encrypts under it.
// The function should look like:
// encryption_oracle(your-input)
// => [MEANINGLESS JIBBER JABBER]
// Under the hood, have the function append 5-10 bytes (count chosen randomly) before the plaintext and 5-10 bytes after the plaintext.
// Now, have the function choose to encrypt under ECB 1/2 the time, and under CBC the other half (just use random IVs each time for CBC). 
// Use rand(2) to decide which to use.
// Detect the block cipher mode the function is using each time. You should end up with a piece of code that, pointed at a 
// block box that might be encrypting ECB or CBC, tells you which one is happening.

package second.blockcipher;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EcbCbcDetectionOracle {

  public static final int KEY_LENGTH = 16;

  public byte[] getRandomKey(int length) {
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

  public static void main(String [] args) {
    EcbCbcDetectionOracle ecbCbcDetectionOracle = new EcbCbcDetectionOracle();
    byte[] key = ecbCbcDetectionOracle.getRandomKey(KEY_LENGTH);
    assert(key.length == KEY_LENGTH);
    System.out.println(new String(key) +" key length is "+ key.length);
  }
}