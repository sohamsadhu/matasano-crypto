// Problem 6

public class BreakRepeatKeyXor {

  public static final byte [] BYTE_MASK = {0x55, 0x33, 0x0F};

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
  }
}
