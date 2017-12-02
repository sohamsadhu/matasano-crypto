import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Problem 4
public class SingleCharXor {

  public String getClosestString(String file, SingleByteXor sbxor, HexToBase64 hb64) {
    String output = "";
    double min = Double.MAX_VALUE;
    try (
      BufferedReader br = new BufferedReader(new FileReader(file));
    ) {
      String line;
      while ((line = br.readLine()) != null) {
        String s = sbxor.getHighestMatch(line);
        double score = sbxor.scoreByteToFrequency(hb64.convertHexToBytes(line));
        if (score < min) {
          min = score;
          output = s;
        }
      }
    } catch(IOException ioex) {
      ioex.printStackTrace();
    }
    return output;
  }

  public static void main(String[] args) {
    SingleCharXor scxor = new SingleCharXor();
    SingleByteXor sbxor = new SingleByteXor();
    HexToBase64 hb64 = new HexToBase64();
    String file = (args.length > 0) ? args[0] : "4.txt";
    String outputFile = scxor.getClosestString(file, sbxor, hb64);
    System.out.println("Closest matching string is: "+ outputFile);
  }
}