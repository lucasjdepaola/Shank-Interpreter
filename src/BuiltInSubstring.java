import java.util.ArrayList;
public class BuiltInSubstring extends FunctionNode {
	
	public BuiltInSubstring(String functionName, ArrayList<VariableNode> parameterCollection,
			ArrayList<VariableNode> variableCollection, ArrayList<StatementNode> statements) {
		super(functionName, parameterCollection, variableCollection, statements);
	}
	
	public void execute(ArrayList<InterpreterDataType> dataCollection) throws IncorrectParameterException {
		if(dataCollection.size()!=4) {//this function requires 4 parameters
			throw new IncorrectParameterException(dataCollection.size()>4?"Too many arguments, needs to be 4 values":"Not enough arguments, needs to be 4 values");
		}
		StringDataType stringGiven = (StringDataType)dataCollection.get(0);
		IntegerDataType indexGiven = (IntegerDataType)dataCollection.get(1);
		IntegerDataType lengthGiven = (IntegerDataType)dataCollection.get(2);
		String answer = "";
		for(int i = indexGiven.getInteger(); i < lengthGiven.getInteger()+indexGiven.getInteger(); i++) {//loop is going from the index given, iterating "length" amount of times
			answer += stringGiven.getString().charAt(i);//appending the index of the givenstring to the answer
		}
		StringDataType answerString = new StringDataType(answer);
		dataCollection.set(3, answerString);//setting the mutable parameters to the answer
		
	}
	
}
