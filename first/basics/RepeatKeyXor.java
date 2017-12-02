// Problem 5

public class RepeatKeyXor {

  public byte[] getBytesFromString(String input) {
    int length = input.length();
    byte[] result = new byte[length];
    for (int i = 0; i < length; i++) {
      result[i] = (byte) (input.charAt(i) & 0x00FF);
    }
    return result;
  }

  public byte[] xorInputWithKey(byte[] input, byte[] xorKey) {
    int length = input.length;
    int keyLength = xorKey.length;
    byte[] result = new byte[length];
    for (int i = 0; i < length; i++) {
      int temp1 = (int) input[i];
      int temp2 = (int) xorKey[i % keyLength];
      int temp3 = temp1 ^ temp2;
      result[i] = (byte) (temp3 & 0xFF);
    }
    return result;
  }

  public String getHexByRepeatXor(String input, String key, FixedXor fxor) {
    byte[] inputBytes = getBytesFromString(input);
    byte[] xorKey = getBytesFromString(key);
    return fxor.convertBytesToHexString(xorInputWithKey(inputBytes, xorKey));
  }

  public static void main(String [] args) {
    RepeatKeyXor rxor = new RepeatKeyXor();
    String input = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal";
    String key = "ICE";
    String expectedOutput = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c"+
      "2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286"+
      "326302e27282f";
    FixedXor fxor = new FixedXor();
    String output = rxor.getHexByRepeatXor(input, key, fxor);
    System.out.println("Conversion done: "+ output.equals(expectedOutput));
  }
}