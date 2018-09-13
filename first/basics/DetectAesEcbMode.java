// Problem 8: Detect AES in ECB mode
// In file "8.txt" are a bunch of hex-encoded ciphertexts. One of them has been encrypted with ECB. Detect it.
// Remember that the problem with ECB is that it is stateless and deterministic; 
// the same 16 byte plaintext block will always produce the same 16 byte ciphertext.

package first.basics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DetectAesEcbMode {

  public static final int BLOCK_LENGTH = 16;

  // You do not need to convert string to bits or bytes to find similarity.
  // Just finding the similarity in the string hex code itself would do.
  public int getSimilarBlockScore(String hexLine, int byteLength) {
    if (hexLine == null || hexLine.isEmpty()) return 0;
    int blockLength = 2 * byteLength;
    int size = hexLine.length();
    int score = 0;
    for (int i = 0; i <= size - 2 * blockLength; i += blockLength) {
      for (int j = i + blockLength; j <= size - blockLength; j += blockLength) {
        if (hexLine.substring(i, i + blockLength).equals(hexLine.substring(j, j + blockLength))) score++;
      }
    }
    return score;
  }

  public Integer getAesEcbLine(String file) {
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      int lineNumber = 1;
      String line;
      while ((line = br.readLine()) != null) {
        if (getSimilarBlockScore(line, BLOCK_LENGTH) > 0) {
          System.out.println("Line with AES in ECB mode is "+ line);
          // Any encryption would not have block repetition. So any block with
          // repetition is a candidate for ECB block use.
          return lineNumber;
        }
        lineNumber++;
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