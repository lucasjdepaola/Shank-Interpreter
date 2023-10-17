import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SemanticAnalysis {
	
	public enum variableTypes {
		VARPARAM, CONSTPARAM, EXPRESSIONPARAMSTRING, EXPRESSIONPARAMINT, EXPRESSIONPARAMCHAR, EXPRESSIONPARAMBOOL, EXPRESSIONPARAMARR,  VARIDIACPARAM
	}
	
	public SemanticAnalysis(ProgramNode program) {
		this.program = program;
	}
	private ProgramNode program;
	HashMap<String, InterpreterDataType> parameters = new HashMap<String, InterpreterDataType>();
	HashMap<String, FunctionNode> programMap = program.getHashMap();
	Interpreter interpreter = new Interpreter(program);
	
	public void start() throws SemanticAnalysisException, InterpreterLogicalErrorException {
		CheckAssignments(program);
		interpreter.interpretFunction(programMap.get("start"));
	}
	
	public void CheckAssignments(ProgramNode check) throws SemanticAnalysisException, InterpreterLogicalErrorException {
		
		for(Map.Entry<String, FunctionNode> iterate: programMap.entrySet()) {
			String name = iterate.getKey();
			FunctionNode function = iterate.getValue();
			//ArrayList<variableTypes> typeList = typeList(function.getParameterCollection());
			for(StatementNode statement : function.getStatements()) {
				if(statement instanceof FunctionCallNode) {//need to iterate through the statements and find function call and assignment nodes, then make sure that they are type valid
					FunctionCallNode functionCall = (FunctionCallNode)statement;
					ParameterNode params = functionCall.getParams();
					ArrayList<variableTypes> functionCallTypes = typeList(params.getVariableCollection());
					ArrayList<variableTypes> typeList = typeList(programMap.get(functionCall.getName().trim()).getParameterCollection());
					if(functionCallTypes.size()!=typeList.size()) throw new SemanticAnalysisException("Function call error, function " + functionCall.getName() + "does not have " + functionCallTypes.size() + " parameters.");
					FunctionNode functionCalled = programMap.get(functionCall.getName().trim());
					ArrayList<InterpreterDataType> parameterIDTs = new ArrayList<InterpreterDataType>();
					for(int i = 0; i < functionCallTypes.size(); i++) {//iterate through functionCall params and compare them to the function itself
						if(functionCallTypes.get(i) != typeList.get(i))
							throw new SemanticAnalysisException("Function call parameter error, for function " + functionCall.getName() + "you called " + functionCallTypes.get(i)+ " on a "+ typeList.get(i)+" parameter.");
						else {
							InterpreterDataType paramIDT = null;
							if(functionCallTypes.get(i)==variableTypes.VARPARAM) {
								parameters.put(functionCalled.getfunctionName(), paramIDT);
								paramIDT = interpreter.expression(functionCall.getParams().getBoolCompareCollection().get(i), parameters);
								parameterIDTs.add(paramIDT);
							}
								
								
								
						}
					}
					Interpreter interpretFunction = new Interpreter(check);
					
					interpretFunction.interpretFunction(programMap.get(functionCall.getName().trim()));
				}
				else if(statement instanceof AssignmentNode) {
					
				}
			}
		}
		
	}
	
	public ArrayList<variableTypes> typeList(ArrayList<VariableNode> parameters) {
		//creating enums for the parameters of the function, will be used for function calls and assert that functions are called properly
		ArrayList<variableTypes> typeList = new ArrayList<variableTypes>();
		for(VariableNode parameter: parameters) {
			if(parameter.isChangeable()) {//var parameter
				typeList.add(variableTypes.VARPARAM);
			}
			else if(parameter.getType().toUpperCase() == "INTEGER") {
				typeList.add(variableTypes.EXPRESSIONPARAMINT);
			}
			else if(parameter.getType().toUpperCase()=="STRING") {
				typeList.add(variableTypes.EXPRESSIONPARAMSTRING);
			}
			else if(parameter.getType().toUpperCase()=="CHAR") {
				typeList.add(variableTypes.EXPRESSIONPARAMCHAR);
			}
			else if(parameter.getType().toUpperCase()=="BOOL") {
				typeList.add(variableTypes.EXPRESSIONPARAMBOOL);
			}
			else if(parameter.getType().toUpperCase().split(" ")[0]=="ARRAY") {
				typeList.add(variableTypes.EXPRESSIONPARAMARR);
			}
		}
		return typeList;
	}
	
}

class SemanticAnalysisException extends Exception {
	public SemanticAnalysisException(String message) {
		super(message);
	}
}
