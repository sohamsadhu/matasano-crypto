// Second problem
// Fixed XOR
// Write a function that takes two equal-length buffers and produces their XOR combination.
// If your function works properly, then when you feed it the string:
// 1c0111001f010100061a024b53535009181c
// ... after hex decoding, and when XOR'd against:
// 686974207468652062756c6c277320657965
// ... should produce:
// 746865206b696420646f6e277420706c6179

package first.basics;

public class FixedXor {

  public static final char[] HEX_LOOKUP = 
      {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  public String convertBytesToHexString(byte[] toHex) {
    char[] result = new char[toHex.length * 2];
    for (int i = 0; i < result.length; i += 2) {
      result[i]     = HEX_LOOKUP[toHex[i / 2] >>> 4];
      result[i + 1] = HEX_LOOKUP[toHex[i / 2] & 0x0F];
    }
    return (new String(result));
  }

  public byte[] xorTwoByteArrays(byte[] bytes1, byte[] bytes2) throws Exception {
    int length = bytes1.length;
    if (length != bytes2.length) throw new Exception("Cannot XOR two byte arrays not of equal length.");

    byte[] bytes3 = new byte[length];
    int temp1, temp2, temp3;
    for (int i = 0; i < length; i++) {
      temp1 = (int) bytes1[i];
      temp2 = (int) bytes2[i];
      temp3 = temp1 ^ temp2;
      bytes3[i] = (byte) (temp3 & 0xFF);
    }
    return bytes3;
  }

  public String xorHexStrings(String hex1, String hex2) throws Exception {
    HexToBase64 hexToBase64 = new HexToBase64();

    if (hex1.length() != hex2.length()) throw new Exception("Given hexadecimal strings are not of same length.");
    if (!hexToBase64.isHex(hex1)) throw new Exception("String 1 is not in hexadecimal format.");
    if (!hexToBase64.isHex(hex2)) throw new Exception("String 2 is not in hexadecimal format.");

    byte[] hexBytes1 = hexToBase64.convertHexToBytes(hex1);
    byte[] hexBytes2 = hexToBase64.convertHexToBytes(hex2);
    return convertBytesToHexString(xorTwoByteArrays(hexBytes1, hexBytes2));
  }

  public static void main(String[] args) {
    FixedXor fxor = new FixedXor();
    String hex1 = "1c0111001f010100061a024b53535009181c";
    String hex2 = "686974207468652062756c6c277320657965";
    try {
      String result = fxor.xorHexStrings(hex1, hex2);
      System.out.println("Xor string is "+ result);
      System.out.println("Result is "+ result.equals("746865206b696420646f6e277420706c6179"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}