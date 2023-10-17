import java.util.ArrayList;

public class IfNode extends StatementNode {
	
	public IfNode(BooleanCompareNode boolNode, ArrayList<StatementNode> statementNodes, ifTypes ifType) {
		this.setBoolNode(boolNode);
		this.setStatementNodes(statementNodes);
		this.setIfType(ifType);
	}
	private BooleanCompareNode boolNode;
	private IfNode next;
	private ifTypes ifType;
	private ArrayList<StatementNode> statementNodes = new ArrayList<StatementNode>();
	public BooleanCompareNode getBoolNode() {
		return boolNode;
	}
	public void setBoolNode(BooleanCompareNode boolNode) {
		this.boolNode = boolNode;
	}
	public ArrayList<StatementNode> getStatementNodes() {
		return statementNodes;
	}
	public void setStatementNodes(ArrayList<StatementNode> statementNodes) {
		this.statementNodes = statementNodes;
	}
	
	public String toString() {
		return "IfNode(condition: " + boolNode + "\nif statements: " + statementNodes + ")";
	}
	
	public IfNode getNext() {
		return next;
	}
	public void setNext(IfNode next) {
		this.next = next;
	}

	public ifTypes getIfType() {
		return ifType;
	}
	public void setIfType(ifTypes ifType) {
		this.ifType = ifType;
	}

	public enum ifTypes{
		IF, ELSEIF, ELSE
	}
	
}
