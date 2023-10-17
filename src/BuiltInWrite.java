import java.util.ArrayList;

public class BuiltInWrite extends FunctionNode {

	public BuiltInWrite(String functionName, ArrayList<VariableNode> parameterCollection,
			ArrayList<VariableNode> variableCollection, ArrayList<StatementNode> statements) {
		super(functionName, parameterCollection, variableCollection, statements);
	}

	public void execute(ArrayList<InterpreterDataType> dataCollection) {
		for(InterpreterDataType dataTypes: dataCollection) {
			System.out.println(dataTypes.ToString());//iterating through the IDT collection and printing the values, not adding white space between elements due to it being user controlled
		}
	}

}
