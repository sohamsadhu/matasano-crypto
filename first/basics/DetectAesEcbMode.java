// Problem 8: Detect AES in ECB mode
// In file "8.txt" are a bunch of hex-encoded ciphertexts. One of them has been encrypted with ECB. Detect it.
// Remember that the problem with ECB is that it is stateless and deterministic; 
// the same 16 byte plaintext block will always produce the same 16 byte ciphertext.

import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DetectAesEcbMode {

  public static final int BLOCK_LENGTH = 16;

  // Returns combination of bytes of block length. Will return null, if the
  // byte length is not a proper multiple of the block size.
  public byte[][][] getCombinationBytes(byte[] input, int blockSize) {
    if (0 != input.length % blockSize) return null;
    int numBlocks = input.length / blockSize;
    int numCombinations = (numBlocks * (numBlocks - 1)) / 2;
    if (numCombinations < 1) return null;
    byte[][][] result = new byte [numCombinations][2][blockSize];
    for (int i = 0; i < numCombinations; i++) {
      for (int j = 0; j < (numBlocks - 1); j++) {
        for (int k = j + 1; k < numBlocks; k++) {
          for (int z = 0; z < blockSize; z++) {
            result[i][0][z] = input[j * blockSize + z];
            result[i][1][z] = input[k * blockSize + z];
          }
        }
      }
    }
    return result;
  }

  public Integer getAesEcbLine(String file) {
    int lineNumber = 0;
    HexToBase64 hexToBase64 = new HexToBase64();
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      int score = 0;
      String line;
      int lineIndex = 0;
      while ((line = br.readLine()) != null) {
        int similarity = 0;
        byte[] encryptedBytes = hexToBase64.convertHexToBytes(line.trim());
        byte[][][] combinations = getCombinationBytes(encryptedBytes, BLOCK_LENGTH);
        for (int i = 0; i < combinations.length; i++) {
          if (Arrays.equals(combinations[i][0], combinations[i][1])) {
            similarity++;
          }
        }
        if (similarity >= score) {
          score = similarity;
          lineNumber = lineIndex;
        }
        lineIndex++;
      }
      return lineNumber;
    } catch(IOException ioex) {
      ioex.printStackTrace();
      return null;
    }
  }

  public static void main(String [] args) {
    DetectAesEcbMode detectAesEcbMode = new DetectAesEcbMode();
    System.out.println("Line number in file that uses AES in ECB mode is "+ detectAesEcbMode.getAesEcbLine("8.txt"));
  }
}