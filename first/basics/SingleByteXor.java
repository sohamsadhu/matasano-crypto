// Problem 3

public class SingleByteXor {

  // http://www.data-compression.com/english.html
  // The following array holds the letter frequency for each character in english alphabet. Frequency
  // of a letter can be obtained, by looking at index given by position of the letter in alphabet, in
  // the given array. For example letter 'b' will have the frequency 0.0124248, obtained by looking in
  // position LETTER_FREQUENCY[2], since 'b' is the second character in alphabet.
  // Please note that there are 27 entries in below array. The 27th entry is for space character frequency.
  public static final double [] LETTER_FREQUENCY = {0.0651738, 0.0124248, 0.0217339, 0.0349835 , 0.1041442,
    0.0197881, 0.0158610, 0.0492888, 0.0558094, 0.0009033, 0.0050529, 0.0331490, 0.0202124, 0.0564513,
    0.0596302, 0.0137645, 0.0008606, 0.0497563, 0.0515760, 0.0729357, 0.0225134, 0.0082903, 0.0171272,
    0.0013692, 0.0145984, 0.0007836, 0.1918182};

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
    double [] count = new double[27];
    double redundantChars = 0.0;
    for (byte letter : input) {
      int i = (int) letter;
      if ((i > 64 && i < 91) || (i > 96 && i < 123)) {
        int j = (i < 91) ? (i - 65) : (i - 97);
        count[j]++;
        countLetter++;
      } else if (32 == i) {
        count[26]++;
        countLetter++;
      } else {
        redundantChars++;
      }
    }
    double result = 0.0;
    for (int i = 0; i < 27; i++) {
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

  public CryptResult getLowestDeviationBytes(byte[][] xoredBytes) {
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
    CryptResult result = new CryptResult();
    result.setSingleKey((byte) index);
    result.setExtractedBytes(xoredBytes[index]);
    return result;
  }

  public CryptResult getHighestMatch(String hexInput) {
    HexToBase64 hb64 = new HexToBase64();
    byte[] hexBytes = hb64.convertHexToBytes(hexInput);
    byte[][] xoredBytes = getEveryXor(hexBytes);
    return getLowestDeviationBytes(xoredBytes);
  }

  public static void main(String [] args) {
    SingleByteXor sxor = new SingleByteXor();
    String hexInput = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
    CryptResult result = sxor.getHighestMatch(hexInput);
    String output = new String(result.getExtractedBytes());
    System.out.println("Key is: "+ result.getSingleKey() +", decoded string is: "+ output);
  }
}