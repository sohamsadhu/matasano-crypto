// Implement PKCS#7 padding
// A block cipher transforms a fixed-sized block (usually 8 or 16 bytes) of plaintext into ciphertext. 
// But we almost never want to transform a single block; we encrypt irregularly-sized messages.
// One way we account for irregularly-sized messages is by padding, creating a plaintext that is an 
// even multiple of the blocksize. The most popular padding scheme is called PKCS#7.
// So: pad any block to a specific block length, by appending the number of bytes of padding to the end of the block. For instance,
// "YELLOW SUBMARINE" ... padded to 20 bytes would be: "YELLOW SUBMARINE\x04\x04\x04\x04"

import java.util.Arrays;

public class Pkcs7Padding {

  // The padding byte value is determined by the number of bytes to be padded. So if n number of bytes
  // are to be padded. Then the value of each of those bytes would be n. A constraint of such a design
  // is that you cannot pad more than 256 bytes, or the block size is 256. If the provided number of bytes
  // is a multiple of the block size, then an entire block consisting of that block size is added.
  public byte[] padBytes(byte[] bytes, int blockSize) {
    if (bytes == null || 0 == bytes.length) return bytes;
    if (blockSize < 0 || blockSize > 256) return bytes;
    int inputLength = bytes.length;
    int length = (0 == inputLength % blockSize) ? (inputLength + blockSize) : (inputLength + blockSize - inputLength % blockSize);
    byte[] result = new byte[length];
    for (int i = 0; i < inputLength; i++) {
      result[i] = bytes[i];
    }
    byte padByte = (byte)(length - inputLength);
    for (int i = inputLength; i < length; i++) {
      result[i] = padByte;
    }
    return result;
  }

  public void testPadding() {
    String sent = "YELLOW SUBMARINE";
    int blockSize = 20;
    byte[] received = padBytes(sent.getBytes(), blockSize);
    byte[] input = sent.getBytes();
    byte[] comparedTo = new byte[blockSize];
    for (int i = 0; i < sent.length(); i++) {
      comparedTo[i] = input[i];
    }
    for (int i = sent.length(); i < blockSize; i++) {
      comparedTo[i] = (byte)4;
    }
    System.out.println("Padding was success: "+ Arrays.equals(comparedTo, received));
  }

  public static void main(String [] args) {
    Pkcs7Padding pkcs7Padding = new Pkcs7Padding();
    pkcs7Padding.testPadding();
  }
}
