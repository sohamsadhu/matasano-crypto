// Problem 3

public class SingleByteXor {

  // https://en.wikipedia.org/wiki/Letter_frequency, Robert Lewand's Cryptological Mathematics
  public static final double [] LETTER_FREQUENCY = {0.08167, 0.01492, 0.02782, 0.04253, 0.12702, 
    0.02228, 0.02015, 0.06094, 0.06966, 0.00153, 0.00772, 0.04025, 0.02406, 0.06749, 0.07507, 
    0.01929, 0.00095, 0.05987, 0.06327, 0.09056, 0.02758, 0.00978, 0.0236, 0.0015, 0.01974, 0.00074};

  public byte[][] getEveryXor(byte[] input) {
    int xoredLength = input.length;
    byte [][] result = new byte [128] [xoredLength];
    for (int i = 0; i < 128; i++) {
      byte[] xored = new byte[xoredLength];
      for (int j = 0; j < xoredLength; j++) {
        int temp = (int) input[j] ^ i;
        xored[j] = (byte) temp;
      }
      result[i] = xored;
    }
    return result;
  }

  public double scoreByteToFrequency(byte[] input) {
    double countLetter = 0.0;
    double [] count = new double[26];
    double redundantChars = 0.0;
    for (byte letter : input) {
      int i = (int) letter;
      if ((i > 64 && i < 91) || (i > 96 && i < 123)) {
        int j = (i < 91) ? (i - 65) : (i - 97);
        count[j]++;
        countLetter++;
      } else {
        redundantChars++;
      }
    }
    double result = 0.0;
    for (int i = 0; i < 26; i++) {
      if (count[i] > 0.0) {
        result += Math.pow((count[i] / countLetter) - LETTER_FREQUENCY[i], 2);
      }
    }
    return (result + redundantChars);
  }

  public String getAsciiStringFromBytes(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte character: bytes) {
      sb.append((char) character);
    }
    return sb.toString();
  }

  public String getHighestMatch(String hexInput) {
    HexToBase64 hb64 = new HexToBase64();
    byte[] hexBytes = hb64.convertHexToBytes(hexInput);
    byte[][] xoredBytes = getEveryXor(hexBytes);
    double min = Double.MAX_VALUE;
    int index = 0;
    int xoredLength = xoredBytes.length;
    for (int i = 0; i < xoredLength; i++) {
      double temp = scoreByteToFrequency(xoredBytes[i]);
      if (temp < min) {
        min = temp;
        index = i;
      }
    }
    return getAsciiStringFromBytes(xoredBytes[index]);
  }

  public static void main(String [] args) {
    SingleByteXor sxor = new SingleByteXor();
    String hexInput = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
    String output = sxor.getHighestMatch(hexInput);
    System.out.println("Decoded string is: "+ output);
  }
}