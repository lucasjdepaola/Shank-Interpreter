import java.util.ArrayList;

public class BuiltInSquareRoot extends FunctionNode {

	public BuiltInSquareRoot(String functionName, ArrayList<VariableNode> parameterCollection,
			ArrayList<VariableNode> variableCollection, ArrayList<StatementNode> statements) {
		super(functionName, parameterCollection, variableCollection, statements);
	}

	public void execute(ArrayList<InterpreterDataType> dataCollection) throws IncorrectParameterException {
		if(dataCollection.size()!=2) {//the square root function only accepts 2 parameters
			throw new IncorrectParameterException(dataCollection.size()>2?"Too many arguments, needs to be 2 values":"Not enough arguments, needs to be 2 values");
		}
		RealDataType input = (RealDataType)dataCollection.get(0);
		float numberToSquareRoot = input.getReal();//getting the input and setting it to a float value
		numberToSquareRoot = (float)Math.sqrt(numberToSquareRoot);//performing the built in java square root method
		RealDataType outputType = (RealDataType)dataCollection.get(1);
		outputType.setReal(numberToSquareRoot);
		dataCollection.set(1, outputType);//setting the mutated output member as its mutated value
		
	}

}
