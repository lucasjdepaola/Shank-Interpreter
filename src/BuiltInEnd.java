import java.util.ArrayList;
public class BuiltInEnd extends FunctionNode {
	
	public BuiltInEnd(String functionName, ArrayList<VariableNode> parameterCollection,
			ArrayList<VariableNode> variableCollection, ArrayList<StatementNode> statements) {
		super(functionName, parameterCollection, variableCollection, statements);
	}
	
	public void execute(ArrayList<InterpreterDataType> dataCollection) throws IncorrectParameterException {
		if(dataCollection.size()!=2) {
			throw new IncorrectParameterException(dataCollection.size()>2?"Too many arguments, needs to be 2 values":"Not enough arguments, needs to be 2 values");
		}
		ArrayDataType arrayType = (ArrayDataType)dataCollection.get(0);
		InterpreterDataType[] array = arrayType.getArray();
		dataCollection.set(1, array[array.length-1]);//setting the second parameter to the last array element, assuming array given in the first parameter
	}
	
}

