import java.util.HashMap;

public class ProgramNode extends Node {
	//hashmap containing all of the functions, (functionName, functionNode)
	
	private HashMap<String, FunctionNode> FunctionNodes = new HashMap<String, FunctionNode>();
	public ProgramNode(HashMap<String, FunctionNode> FunctionNodes) {
		this.setHashMap(FunctionNodes);
	}
	
	public void setHashMap(HashMap<String, FunctionNode> FunctionNodes) {
		this.FunctionNodes = FunctionNodes;
	}
	
	public HashMap<String, FunctionNode> getHashMap(){
		
		return FunctionNodes;
	}
	public String toString() {
		String returnValue = "";


		return"PROGRAMTOSTRING"+ FunctionNodes+"";
	}
	
	
}