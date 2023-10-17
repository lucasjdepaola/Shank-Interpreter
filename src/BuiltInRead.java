import java.util.ArrayList;
import java.util.Scanner;
public class BuiltInRead extends FunctionNode {
	
	public BuiltInRead(String functionName, ArrayList<VariableNode> parameterCollection,
			ArrayList<VariableNode> variableCollection, ArrayList<StatementNode> statements) {
		super(functionName, parameterCollection, variableCollection, statements);
	}
	Scanner reader = new Scanner(System.in);
	public void execute(ArrayList<InterpreterDataType> dataCollection) {
		String[] read = reader.next().split(" ");//space delimited array of strings
		for(int i = 0; i < dataCollection.size();i++) {//since it is variadic, need to fill up the (potentially infinite) amount of parameters with the user input
			InterpreterDataType dataType = dataCollection.get(i);
			dataType.FromString(read[i]);//mutating the string based on the type of IDT it is, abstract method
		}
		
	}
	
}
