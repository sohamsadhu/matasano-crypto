// First problem

import java.util.regex.Pattern;

public class HexToBase64 {

  public static final String HEX_PATTERN = "^[0-9a-fA-F]+$";
  public static final char[] BASE_64_LOOKUP = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 
    'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
    'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
    '9', '+', '/'};

  // https://stackoverflow.com/a/25820641
  public boolean isHex(String hex) {
    return Pattern.matches(HEX_PATTERN, hex);
  }

  // https://stackoverflow.com/a/140861
  public byte[] convertHexToBytes(String hex) {
    int length = hex.length();
    byte[] result = new byte[length / 2];
    for (int i = 0; i < length; i += 2) {
      result[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + 
          Character.digit(hex.charAt(i + 1), 16));
    }
    return result;
  }

  public String convertBytesToHex(byte[] hex) {
    StringBuilder sb = new StringBuilder("");
    int remaining = hex.length % 3;
    int processEnd = hex.length - remaining;
    for (int i = 0; i < processEnd; i += 3) {
      sb.append(BASE_64_LOOKUP[hex[i] >>> 2]);
      sb.append(BASE_64_LOOKUP[(0x30 & (hex[i] << 4)) | (hex[i + 1] >>> 4)]);
      sb.append(BASE_64_LOOKUP[((hex[i + 1] << 2) & 0x3F) | (hex[i + 2] >>> 6)]);
      sb.append(BASE_64_LOOKUP[hex[i + 2] & 0x3F]);
    }
    if (remaining > 0) {
      sb.append(BASE_64_LOOKUP[hex[processEnd] >>> 2]);
      if (1 == remaining) {
        sb.append(BASE_64_LOOKUP[(hex[processEnd] << 4) & 0x30]);
        sb.append('=');
      } else {
        sb.append(BASE_64_LOOKUP[(0x30 & (hex[processEnd] << 4)) | (hex[processEnd + 1] >>> 4)]);
        sb.append(BASE_64_LOOKUP[(hex[processEnd + 1] << 2) & 0x3F]);
      }
      sb.append('=');
    }
    return sb.toString();
  }

  public String convertHexToBase64(String hex) throws Exception {
    if (!isHex(hex)) {
      throw new Exception("Provided string is now in hexadecimal format.");
    }
    byte[] hexBytes = convertHexToBytes(hex);
    String base64 = convertBytesToHex(hexBytes);
    return base64;
  }

  public static void main(String [] args) {
    HexToBase64 hb = new HexToBase64();
    String input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
    String expectedOutput = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";
    try {
      String output = hb.convertHexToBase64(input);
      System.out.println("Result is "+ expectedOutput.equals(output));
      System.out.println(output);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}