package first.basics;

public class CryptResult {

  private byte singleKey;
  private byte[] extractedBytes;

  public byte getSingleKey() {
    return this.singleKey;
  }

  public void setSingleKey(byte singleKey) {
    this.singleKey = singleKey;
  }

  public byte[] getExtractedBytes() {
    return this.extractedBytes;
  }

  public void setExtractedBytes(byte[] extractedBytes) {
    this.extractedBytes = extractedBytes;
  }
}