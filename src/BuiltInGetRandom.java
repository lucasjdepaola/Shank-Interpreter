import java.util.ArrayList;

public class BuiltInGetRandom extends FunctionNode {

	public BuiltInGetRandom(String functionName, ArrayList<VariableNode> parameterCollection,
			ArrayList<VariableNode> variableCollection, ArrayList<StatementNode> statements) {
		super(functionName, parameterCollection, variableCollection, statements);
	}

	public void execute(ArrayList<InterpreterDataType> dataCollection) throws IncorrectParameterException {
		if(dataCollection.size()!=1) {
			throw new IncorrectParameterException(dataCollection.size()>1?"Too many arguments, needs to be 1 value":"Not enough arguments, needs to be 1 value");
		}
		int randomInteger = (int)Math.random();//generating random integer
		IntegerDataType randomIntegerType = (IntegerDataType)dataCollection.get(0);
		randomIntegerType.setInteger(randomInteger);//setting the integer value to the mutable (first param) element
		dataCollection.set(0, randomIntegerType);
	}

}
