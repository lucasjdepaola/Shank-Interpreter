import java.util.ArrayList;
public class BuiltInRight extends FunctionNode {
	
	public BuiltInRight(String functionName, ArrayList<VariableNode> parameterCollection,
			ArrayList<VariableNode> variableCollection, ArrayList<StatementNode> statements) {
		super(functionName, parameterCollection, variableCollection, statements);
	}
	
	public void execute(ArrayList<InterpreterDataType> dataCollection) throws IncorrectParameterException {
		if(dataCollection.size()!=3) {//this function requires 3 parameters
			throw new IncorrectParameterException(dataCollection.size()>3?"Too many arguments, needs to be 3 values":"Not enough arguments, needs to be 3 values");
		}
		StringDataType stringGiven = (StringDataType)dataCollection.get(0);
		IntegerDataType lengthGiven = (IntegerDataType)dataCollection.get(0);
		String answer = "";
		for(int i = stringGiven.getString().length()-lengthGiven.getInteger(); i < stringGiven.getString().length(); i++) {
			//loop is going from {stringGiven length - userGiven length} to {stringGiven length} making it iterate from the last {length} elements
			answer += stringGiven.getString().charAt(i);
		}
		StringDataType mutableNode = new StringDataType(answer);
		dataCollection.set(2, mutableNode);
	}
	
}
