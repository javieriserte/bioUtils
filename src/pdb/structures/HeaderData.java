package pdb.structures;

public class HeaderData extends SpacePoint {
  private String data;
  @Override
  public RecordName getType() {
    return RecordName.Header;
  }

  public void setData(String data) {
    this.data = data;
  }
  
  public String getData() {
    return this.data;
  }
}
