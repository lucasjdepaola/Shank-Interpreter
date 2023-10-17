import java.util.ArrayList;
public class BuiltInLeft extends FunctionNode {
	
	public BuiltInLeft(String functionName, ArrayList<VariableNode> parameterCollection,
			ArrayList<VariableNode> variableCollection, ArrayList<StatementNode> statements) {
		super(functionName, parameterCollection, variableCollection, statements);
	}
	
	public void execute(ArrayList<InterpreterDataType> dataCollection) throws IncorrectParameterException {
		if(dataCollection.size()!=3) {//this function requires 3 parameters
			throw new IncorrectParameterException(dataCollection.size()>3?"Too many arguments, needs to be 3 values":"Not enough arguments, needs to be 3 values");
		}
		StringDataType stringGiven = (StringDataType)dataCollection.get(0);
		IntegerDataType lengthGiven = (IntegerDataType)dataCollection.get(1);
		String answer = "";
		for(int i = 0; i < lengthGiven.getInteger(); i++) {//going from the very left (0) to the length given, so its the first {length_given} characters of the string
			answer += stringGiven.getString().charAt(i);
		}
		StringDataType mutableNode = new StringDataType(answer);
		dataCollection.set(2, mutableNode);//setting the mutated node to the mutable parameter section
	}
	
}
