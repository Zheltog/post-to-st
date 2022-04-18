package su.nsk.iae.post.generator.st;

@SuppressWarnings("all")
public class DsmRequestBody {
  private String id;
  
  private String root;
  
  private String fileName;
  
  private String ast;
  
  public DsmRequestBody(final String id, final String root, final String fileName, final String ast) {
    this.id = id;
    this.root = root;
    this.fileName = fileName;
    this.ast = ast;
  }
  
  @Override
  public String toString() {
    return (((((((("{ \"id\": " + this.id) + ", \"root\": ") + this.root) + 
      ", \"fileName\": ") + this.fileName) + ", \"ast\": ") + this.ast) + " }");
  }
}
