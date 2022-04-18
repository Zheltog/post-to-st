package su.nsk.iae.post.generator.st

class DsmRequestBody {
	String id;
    String root;
    String fileName;
    String ast;
    
    new(
    	String id,
    	String root,
    	String fileName,
    	String ast
    ) {
    	this.id = id
    	this.root = root
    	this.fileName = fileName
    	this.ast = ast
    }
    
    override String toString() {
    	return "{ \"id\": " + id + ", \"root\": " + root +
    		", \"fileName\": " + fileName + ", \"ast\": " + ast + " }"
    }
}