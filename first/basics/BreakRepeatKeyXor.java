import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

// Problem 6

public class BreakRepeatKeyXor {

  public static final byte [] BYTE_MASK = {0x55, 0x33, 0x0F};
  public static final String BASE64_PATTERN = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";

  public int getHammingWeight(byte b) {
    byte temp1, temp2;
    int shift;
    for (int i = 0; i < 3; i++) {
      temp1 = (byte)(b & BYTE_MASK[i]);
      shift = (int) Math.pow(2, i);
      temp2 = (byte) ((b >>> shift) & BYTE_MASK[i]);
      b = (byte) (temp1 + temp2);
    }
    return b;
  }

  public int getHammingDistance(byte[] b1, byte[] b2, FixedXor fxor) throws Exception {
    if (b1.length != b2.length) {
      throw new Exception("Cannot get hamming distance between byte arrays of unequal length.");
    }
    byte[] temp = fxor.xorTwoByteArrays(b1, b2);
    int length = temp.length;
    int distance = 0;
    for (int i = 0; i < length; i++) {
      distance += getHammingWeight(temp[i]);
    }
    return distance;
  }

  public int getHammingDistance(String s1, String s2, RepeatKeyXor rkxor, FixedXor fxor) throws Exception {
    if (s1.length() != s2.length()) {
      throw new Exception("Cannot get hamming distance between strings of unequal length.");
    }
    return getHammingDistance(rkxor.getBytesFromString(s1), rkxor.getBytesFromString(s2), fxor);
  }

  public String getBase64StringFromFile(String file) {
    StringBuilder sb = new StringBuilder("");
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }
    } catch(IOException ioex) {
      ioex.printStackTrace();
    }
    return sb.toString();
  }

  public boolean isBase64(String base64) {
    return Pattern.matches(BASE64_PATTERN, base64);
  }

  public Map<Character, Integer> getBase64CharacterValueMap() {
    Map<Character, Integer> base64CharValueMap = new HashMap<>();
    int length = HexToBase64.BASE_64_LOOKUP.length;
    for (int i = 0; i < length; i++) {
      base64CharValueMap.put(HexToBase64.BASE_64_LOOKUP[i], i);
    }
    return base64CharValueMap;
  }

  public byte[] convertBase64StringToBytes(String base64) throws Exception {
    if (!isBase64(base64)) {
      throw new Exception("Provided string "+ base64 +" is not in proper base 64 format.");
    }
    int base64Length = base64.length() * 3 / 4;
    int byteLength = base64Length;
    if ('=' == base64.charAt(base64.length() - 1)) {
      byteLength--;
      if ('=' == base64.charAt(base64.length() - 2)) {
        byteLength--;
      }
    }
    Map<Character, Integer> base64CharValueMap = getBase64CharacterValueMap();
    byte[] result = new byte[byteLength];
    int loopLength = (byteLength == base64Length) ? byteLength : base64Length - 4;
    for (int i = 0, j = 0; i < loopLength; i += 3, j += 4) {
      result[i]     = (byte) ((base64CharValueMap.get(base64.charAt(j)) << 2) | (base64CharValueMap.get(base64.charAt(j + 1)) >>> 4));
      result[i + 1] = (byte) ((base64CharValueMap.get(base64.charAt(j + 1)) << 4) | (base64CharValueMap.get(base64.charAt(j + 2)) >>> 4));
      result[i + 2] = (byte) ((base64CharValueMap.get(base64.charAt(j + 2)) << 6) | base64CharValueMap.get(base64.charAt(j + 3)));
    }
    return null;
  }

  public static void main(String [] args) {
    BreakRepeatKeyXor brxor = new BreakRepeatKeyXor();
    RepeatKeyXor rkxor = new RepeatKeyXor();
    FixedXor fxor = new FixedXor();
    String input1 = "this is a test";
    String input2 = "wokka wokka!!!";
    try {
      System.out.println("Hamming weight "+ brxor.getHammingDistance(input1, input2, rkxor, fxor));
    } catch(Exception e) {
      e.printStackTrace();
    }
    // String s = brxor.getBase64StringFromFile("6.txt");
    // System.out.println("File read is "+ s);
    // System.out.println("File is base64 "+ brxor.isBase64(s));
  }
}
