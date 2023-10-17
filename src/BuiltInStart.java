import java.util.ArrayList;
public class BuiltInStart extends FunctionNode {
	
	public BuiltInStart(String functionName, ArrayList<VariableNode> parameterCollection,
			ArrayList<VariableNode> variableCollection, ArrayList<StatementNode> statements) {
		super(functionName, parameterCollection, variableCollection, statements);
	}
	
	public void execute(ArrayList<InterpreterDataType> dataCollection) throws IncorrectParameterException {
		if(dataCollection.size()!=2) {//this function requires 2 parameters
			throw new IncorrectParameterException(dataCollection.size()>2?"Too many arguments, needs to be 2 values":"Not enough arguments, needs to be 2 values");
		}
		ArrayDataType arrayType = (ArrayDataType)dataCollection.get(0);
		InterpreterDataType[] array = arrayType.getArray();
		dataCollection.set(1, array[0]);//setting the mutable element to the first element of the array 
	}
	
}
