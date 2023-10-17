import java.util.ArrayList;

public class BuiltInIntegerToReal extends FunctionNode {

	public BuiltInIntegerToReal(String functionName, ArrayList<VariableNode> parameterCollection,
			ArrayList<VariableNode> variableCollection, ArrayList<StatementNode> statements) {
		super(functionName, parameterCollection, variableCollection, statements);
	}

	public void execute(ArrayList<InterpreterDataType> dataCollection) throws IncorrectParameterException {
		if(dataCollection.size()!=2) {
			throw new IncorrectParameterException(dataCollection.size()>2?"Too many arguments, needs to be 2 values":"Not enough arguments, needs to be 2 values");
		}
		IntegerDataType input = (IntegerDataType)dataCollection.get(0);
		float outputFloat = (float)input.getInteger();//casting integer value to a float
		RealDataType output = (RealDataType)dataCollection.get(1);
		output.setReal(outputFloat);
		dataCollection.set(1, output);//mutating the integer output parameter into an float
	}

}
