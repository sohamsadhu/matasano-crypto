import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

// Problem 6

public class BreakRepeatKeyXor {

  public static final byte [] BYTE_MASK = {0x55, 0x33, 0x0F};
  // Base 64 pattern string obtained from https://stackoverflow.com/a/8571649
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
    int stringLength = base64.length();
    int base64Length = stringLength * 3 / 4;
    int byteLength = base64Length;
    if ('=' == base64.charAt(stringLength - 1)) {
      byteLength--;
      if ('=' == base64.charAt(stringLength - 2)) {
        byteLength--;
      }
    }
    Map<Character, Integer> base64CharValueMap = getBase64CharacterValueMap();
    byte[] result = new byte[byteLength];
    int loopLength = (byteLength == base64Length) ? stringLength : stringLength - 4;
    for (int i = 0, j = 0; j < loopLength; i += 3, j += 4) {
      result[i]     = (byte) ((base64CharValueMap.get(base64.charAt(j)) << 2)     | (base64CharValueMap.get(base64.charAt(j + 1)) >>> 4));
      result[i + 1] = (byte) ((base64CharValueMap.get(base64.charAt(j + 1)) << 4) | (base64CharValueMap.get(base64.charAt(j + 2)) >>> 2));
      result[i + 2] = (byte) ((base64CharValueMap.get(base64.charAt(j + 2)) << 6) | base64CharValueMap.get(base64.charAt(j + 3)));
    }
    if (base64Length > byteLength) {
      byte temp = (byte) ((base64CharValueMap.get(base64.charAt(stringLength - 4)) << 2) | 
        (base64CharValueMap.get(base64.charAt(stringLength - 3)) >>> 4));
      if ('=' != base64.charAt(stringLength - 2)) {
        result[byteLength - 2] = temp;
        result[byteLength - 1] = (byte) ((base64CharValueMap.get(base64.charAt(stringLength - 3)) << 4) | 
          (base64CharValueMap.get(base64.charAt(stringLength - 2)) >>> 2));
      } else {
        result[byteLength - 1] = temp;
      }
    }
    return result;
  }

  public void verifyBase64Decoder() {
    String base64 = "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz" +
      "IHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2Yg" +
      "dGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGlu" +
      "dWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRo" +
      "ZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=";
    String base64Decoded = "Man is distinguished, not only by his reason, but by this singular passion from" +
      " other animals, which is a lust of the mind, that by a perseverance of delight" +
      " in the continued and indefatigable generation of knowledge, exceeds the short" +
      " vehemence of any carnal pleasure.";
    String decoded2 = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
      "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,";
    String base64_2 = "TG9yZW0gSXBzdW0gaXMgc2ltcGx5IGR1bW15IHRleHQgb2YgdGhlIHByaW50aW5nIGFuZCB0eXBlc2V0dGl" + 
      "uZyBpbmR1c3RyeS4gTG9yZW0gSXBzdW0gaGFzIGJlZW4gdGhlIGluZHVzdHJ5J3Mgc3RhbmRhcmQgZHVtbXkgdGV4dCBldmVyIHNp" +
      "bmNlIHRoZSAxNTAwcyw=";
    String decoded3 = "when an unknown printer took a galley of type and scrambled it to make a type specimen " +
      "book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining " +
      "essentially unchanged. It was popularised in the 1960s with";
    String base64_3 = "d2hlbiBhbiB1bmtub3duIHByaW50ZXIgdG9vayBhIGdhbGxleSBvZiB0eXBlIGFuZCBzY3JhbWJsZWQgaXQgdG8gbWFr" +
      "ZSBhIHR5cGUgc3BlY2ltZW4gYm9vay4gSXQgaGFzIHN1cnZpdmVkIG5vdCBvbmx5IGZpdmUgY2VudHVyaWVzLCBidXQgYWxzbyB0" +
      "aGUgbGVhcCBpbnRvIGVsZWN0cm9uaWMgdHlwZXNldHRpbmcsIHJlbWFpbmluZyBlc3NlbnRpYWxseSB1bmNoYW5nZWQuIEl0IHdhc" +
      "yBwb3B1bGFyaXNlZCBpbiB0aGUgMTk2MHMgd2l0aA==";
    String decoded4 = "release of Letraset sheets containing Lorem Ipsum passage";
    String base64_4 = "cmVsZWFzZSBvZiBMZXRyYXNldCBzaGVldHMgY29udGFpbmluZyBMb3JlbSBJcHN1bSBwYXNzYWdl";
    try {
      byte[] base64Bytes = convertBase64StringToBytes(base64);
      String s = new String(base64Bytes, "UTF-8");
      System.out.println("Base 64 decoder to byte works "+ s.equals(base64Decoded));
      base64Bytes = convertBase64StringToBytes(base64_2);
      s = new String(base64Bytes, "UTF-8");
      System.out.println("Base 64 decoder to byte works for 2 "+ s.equals(decoded2));
      base64Bytes = convertBase64StringToBytes(base64_3);
      s = new String(base64Bytes, "UTF-8");
      System.out.println("Base 64 decoder to byte works for 3 "+ s.equals(decoded3));
      base64Bytes = convertBase64StringToBytes(base64_4);
      s = new String(base64Bytes, "UTF-8");
      System.out.println("Base 64 decoder to byte works for 4 "+ s.equals(decoded4));
    } catch (Exception e) {
      e.printStackTrace();
    }
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
    brxor.verifyBase64Decoder();
  }
}
