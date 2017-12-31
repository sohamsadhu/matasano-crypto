import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Problem 4
public class SingleCharXor {

  public CryptResult getClosestString(String file, SingleByteXor sbxor, HexToBase64 hb64) {
    CryptResult result = new CryptResult();
    double min = Double.MAX_VALUE;
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        CryptResult match = sbxor.getHighestMatch(line.trim());
        double score = sbxor.scoreByteToFrequency(hb64.convertHexToBytes(line));
        if (score < min) {
          min = score;
          result = match;
        }
      }
    } catch(IOException ioex) {
      ioex.printStackTrace();
    }
    return result;
  }

  public static void main(String[] args) {
    SingleCharXor scxor = new SingleCharXor();
    SingleByteXor sbxor = new SingleByteXor();
    HexToBase64 hb64 = new HexToBase64();
    String file = (args.length > 0) ? args[0] : "4.txt";
    CryptResult result = scxor.getClosestString(file, sbxor, hb64);
    System.out.println("Decoded string is: "+ sbxor.getAsciiStringFromBytes(result.getExtractedBytes()) 
      +", and key is "+ (char)result.getSingleKey());
  }
}