import java.util.ArrayList;
import java.util.HashMap;

public class Interpreter {
	Node program;
	public Interpreter(Node program) {
		
		this.program = program;
	}
	
	
	HashMap<String, FunctionNode> programMap = new HashMap<String, FunctionNode>();//getting the parsed program node map
	HashMap<String, InterpreterDataType> localVariables = new HashMap<String, InterpreterDataType>();
	HashMap<String, Boolean> variableDeterminingMap = new HashMap<String, Boolean>();
	public InterpreterDataType interpretFunction(FunctionNode function) throws InterpreterLogicalErrorException {//create the local variables
		programMap = ((ProgramNode)program).getHashMap();
		if(!programMap.containsKey(function.getfunctionName()))System.out.println("error !!");
		for(VariableNode varNode: function.getVariableCollection()) {//need to iterate through local variables and make IDT's
			String name = varNode.getName();
			if(varNode.getType().toLowerCase().equals("string")) {
				if(varNode.getValue()==null) {
					StringDataType variableStringDataType = new StringDataType("");
					localVariables.put(name.trim(), variableStringDataType);
					variableDeterminingMap.put(name.trim(), varNode.isChangeable());
				} else {
					StringNode variableStringNode = (StringNode)varNode.getValue();
					StringDataType stringMember = new StringDataType(variableStringNode.getStringValue());
					localVariables.put(name.trim(), stringMember);
					variableDeterminingMap.put(name.trim(), varNode.isChangeable());
				}
			}
			else if(varNode.getType().equals("integer")||varNode.getType().equals("INTEGER")) {
				IntegerNode variableIntegerNode = (IntegerNode)varNode.getValue();
				if(variableIntegerNode==null) {
					IntegerDataType integerMember = new IntegerDataType(0);
					localVariables.put(name.trim(), integerMember);
					variableDeterminingMap.put(name.trim(), varNode.isChangeable());
				} else {
					IntegerDataType integerMember = new IntegerDataType(variableIntegerNode.getInteger());
					localVariables.put(name.trim(), integerMember);
					variableDeterminingMap.put(name.trim(), varNode.isChangeable());
				}
				
			}
			else if(varNode.getType().equals("char")) {
				CharacterNode variableCharacterNode = (CharacterNode)varNode.getValue();
				CharacterDataType charMember = new CharacterDataType(variableCharacterNode.getCharacter());
				localVariables.put(name, charMember);
				variableDeterminingMap.put(name.trim(), varNode.isChangeable());
			}
			else if(varNode.getType().toLowerCase().equals("real")) {
				RealNode variableRealNode = (RealNode)varNode.getValue();
				if(variableRealNode==null) {
					RealDataType realMember = new RealDataType((float)0.0);
					localVariables.put(name.trim(), realMember);
					variableDeterminingMap.put(name.trim(), varNode.isChangeable());
				} else {
					RealDataType realMember = new RealDataType(variableRealNode.getFloatMember());
					localVariables.put(name.trim(), realMember);
					variableDeterminingMap.put(name.trim(), varNode.isChangeable());
				}
				
			}
			else if(varNode.getType().equals("bool")) {
				BooleanNode variableBooleanNode = (BooleanNode)varNode.getValue();
				BooleanDataType boolMember = new BooleanDataType(variableBooleanNode.isBool());
				localVariables.put(name.trim(), boolMember);
				variableDeterminingMap.put(name.trim(), varNode.isChangeable());
			}
			else if(varNode.getType().split(" ")[0].equals("ARRAY")) {//since the array type is "ARRAY of TYPE", need to split the string by white spaces and get the first word of it to match array
				int from = varNode.getFrom();
				int to = varNode.getTo();
				InterpreterDataType[] arrayIDT = new InterpreterDataType[Math.abs(from-to)];
				ArrayDataType variableArrayMember = new ArrayDataType(arrayIDT);
				localVariables.put(name.trim(), variableArrayMember);
				variableDeterminingMap.put(name.trim(), varNode.isChangeable());
			}
			
		}
		System.out.println("variable map: " + localVariables);
		interpretBlock(programMap.get(function.getfunctionName()).getStatements(), localVariables);
		System.out.println("finished, interpreting, here are the values that have been interpreted" + localVariables);
		return null;
	}
	
	public void interpretBlock(ArrayList<StatementNode> statements, HashMap<String, InterpreterDataType> localVariables) throws InterpreterLogicalErrorException {//interpret the statements
		for(StatementNode statement : statements) {//need to iterate through statements
			if(statement instanceof AssignmentNode) {
				AssignmentNode assignmentStatement = (AssignmentNode)statement;
				String name = assignmentStatement.getTarget().getName();
				if(localVariables.containsKey(name.trim())==false) throw new InterpreterLogicalErrorException("error, "+ name + " is not inside of the declared variables");
				if(variableDeterminingMap.get(name.trim())==false) throw new InterpreterLogicalErrorException("error, you cannot change '" + name +"' because it is a constant value");
				InterpreterDataType assignmentNode = expression(assignmentStatement.getValue(), localVariables);
				if(localVariables.get(name.trim()) instanceof ArrayDataType) {//if its an array data type, we need to find the index expression and store the value inside of the index expression given
					ArrayDataType arrayType = (ArrayDataType)localVariables.get(name.trim());
					InterpreterDataType[] array = arrayType.getArray();
					VariableReferenceNode assignmentIdentifier = (VariableReferenceNode)assignmentStatement.getTarget();
					InterpreterDataType index = expression(assignmentIdentifier.getOptionalNode(), localVariables);
					if(index instanceof IntegerDataType) {
						array[((IntegerDataType)index).getInteger()] = assignmentNode;
						arrayType.setArray(array);
						localVariables.replace(name.trim(), arrayType);
					} else {//if the index is a string or has decimals, there's no point trying to interpret it, and an error should be thrown
						throw new InterpreterLogicalErrorException("cannot get array index of a " + IntegerDataType.class + "type");
					}
				} else {
					localVariables.replace(name.trim(), assignmentNode);
				}
					
			}
			else if(statement instanceof IfNode) {
				IfNode ifStatement = (IfNode)statement;
				boolean previousTrue = false;//boolean determines whether the previous if statements have been executed, if not then we interpret the else block
				do {
				//we need to iterate through the linked list of if nodes if a chain exists, we know for sure that the first if statement has been triggered
					if(ifStatement.getIfType()==IfNode.ifTypes.IF) {//first condition
						if(booleanCompare(ifStatement.getBoolNode(), localVariables)) {
							interpretBlock(ifStatement.getStatementNodes(), localVariables);
							previousTrue = true;
						}
					}
					else if(ifStatement.getIfType()==IfNode.ifTypes.ELSEIF) {
						if(booleanCompare(ifStatement.getBoolNode(), localVariables)) {//if the boolean condition is true, we interpret the block of statements
							interpretBlock(ifStatement.getStatementNodes(), localVariables);
							previousTrue = true;
						}
					}
					else if(previousTrue==false&& ifStatement.getIfType()==IfNode.ifTypes.ELSE){ 
						interpretBlock(ifStatement.getStatementNodes(), localVariables);
					}
					ifStatement = ifStatement.getNext();//setting the ifStatement to the next element of the linked list
				} while(ifStatement!=null);
			}
			else if(statement instanceof ForNode) {
				//setting up the 2 expressions from and to, and turning it into a proper for loop,
				ForNode forStatement = (ForNode)statement;
				InterpreterDataType from = expression(forStatement.getFrom(), localVariables);
				InterpreterDataType to = expression(forStatement.getTo(), localVariables);
				if(from instanceof IntegerDataType == false || to instanceof IntegerDataType == false) throw new InterpreterLogicalErrorException("cannot interpret for loop with non integer indexes, " + from.getClass() + to.getClass());
				String forIdentifier = forStatement.getIdentifier();
				localVariables.put(forIdentifier, from);
				for(int i = ((IntegerDataType)from).getInteger(); i < ((IntegerDataType)to).getInteger(); i++) {
					localVariables.replace(forIdentifier, expression(new IntegerNode(i), localVariables));
					interpretBlock(forStatement.getStatements(), localVariables);
				}
			}
			else if(statement instanceof RepeatNode) {
				//repeat until just means the negation of while, so when the boolean hits true, the loop stops
				RepeatNode repeatStatement = (RepeatNode)statement;
				while(true) {
					if(booleanCompare(repeatStatement.getBoolNode(), localVariables)==true)break;
					interpretBlock(repeatStatement.getStatementNodes(), localVariables);
					repeatStatement = (RepeatNode)statement;
				}
			}
			else if(statement instanceof WhileNode) {
				//interpreting the while loop to break when the condition hits false
				WhileNode whileStatement = (WhileNode)statement;
				while(true) {
					if(booleanCompare(whileStatement.getBoolNode(), localVariables)==false) break;
					interpretBlock(whileStatement.getStatementNodes(), localVariables);
					statement = (WhileNode)statement;
				}
			}
			else if(statement instanceof FunctionCallNode) {
				String call = ((FunctionCallNode)statement).getName().trim();
				interpretFunction(programMap.get(call));
			}
			
		}
	}
	
	public InterpreterDataType expression(Node expressionNode, HashMap<String, InterpreterDataType> localVariables) throws InterpreterLogicalErrorException {
		//interpreting the expressions, we are given a mathOp node, which has a left, right and operator, we will use recursion in case there are nested mathOp nodes, returns an IDT to be combined with the combineNodes() method
		if(expressionNode instanceof MathOpNode) {
			MathOpNode expressionMathOp = (MathOpNode)expressionNode;
			InterpreterDataType left = expression(expressionMathOp.left, localVariables);
			InterpreterDataType right = expression(expressionMathOp.right, localVariables);
			InterpreterDataType finalValue = combineNodes(left, right, expressionMathOp);
			return finalValue;
			
		}
		else if(expressionNode instanceof VariableReferenceNode) {
			VariableReferenceNode expressionVariable = (VariableReferenceNode)expressionNode;
			if(localVariables.containsKey(expressionVariable.getName())==false) throw new InterpreterLogicalErrorException("cannot use nonexistent variable inside of an expression for variable"+ expressionVariable.getName());
			InterpreterDataType VariableType = localVariables.get(expressionVariable.getName());
			return VariableType;
		}
		else if(expressionNode instanceof IntegerNode) {
			return new IntegerDataType(((IntegerNode)expressionNode).getInteger());
		}
		else if(expressionNode instanceof RealNode) {
			return new RealDataType(((RealNode)expressionNode).getFloatMember());
		}
		else if(expressionNode instanceof StringNode) {
			return new StringDataType(((StringNode)expressionNode).getStringValue());
		}
		return null;
	}
	
	public InterpreterDataType combineNodes(InterpreterDataType left, InterpreterDataType right, MathOpNode mathOp) throws InterpreterLogicalErrorException {
		//given a left IDT, a right IDT, and an operator, this method will properly combine the 2 IDT's with proper type combinations, throws an error if type combining is not supported (like string divided by 100 or real plus char) 
		if(left instanceof IntegerDataType && right instanceof IntegerDataType) {
			if(mathOp.getOperator()==MathOpNode.operators.MOD) {
				return new IntegerDataType(((IntegerDataType)left).getInteger() % ((IntegerDataType)right).getInteger());//casting left and right to an integer, and storing the newly calculated number in the integer node;
			}
			if(mathOp.getOperator()==MathOpNode.operators.PLUS) {
				return new IntegerDataType(((IntegerDataType)left).getInteger() + ((IntegerDataType)right).getInteger());
			}
			if(mathOp.getOperator()==MathOpNode.operators.MINUS) {
				return new IntegerDataType(((IntegerDataType)left).getInteger() - ((IntegerDataType)right).getInteger());
			}
			if(mathOp.getOperator()==MathOpNode.operators.DIVIDE) {
				return new IntegerDataType(((IntegerDataType)left).getInteger() / ((IntegerDataType)right).getInteger());
			}
			if(mathOp.getOperator()==MathOpNode.operators.TIMES) {
				return new IntegerDataType(((IntegerDataType)left).getInteger() * ((IntegerDataType)right).getInteger());
			}
			
		}
		if(left instanceof RealDataType && right instanceof RealDataType) {
			if(mathOp.getOperator()==MathOpNode.operators.MOD) {
				return new RealDataType(((RealDataType)left).getReal() % ((RealDataType)right).getReal());//casting left and right to an integer, and storing the newly calculated number in the real node;
			}
			if(mathOp.getOperator()==MathOpNode.operators.PLUS) {
				return new RealDataType(((RealDataType)left).getReal() + ((RealDataType)right).getReal());
			}
			if(mathOp.getOperator()==MathOpNode.operators.MINUS) {
				return new RealDataType(((RealDataType)left).getReal() - ((RealDataType)right).getReal());
			}
			if(mathOp.getOperator()==MathOpNode.operators.DIVIDE) {
				return new RealDataType(((RealDataType)left).getReal() / ((RealDataType)right).getReal());
			}
			if(mathOp.getOperator()==MathOpNode.operators.TIMES) {
				return new RealDataType(((RealDataType)left).getReal() * ((RealDataType)right).getReal());
			}
		}
		if(left instanceof StringDataType && right instanceof StringDataType) {//strings can only be added, anything other than addition will result in an error
			if(mathOp.getOperator()==MathOpNode.operators.PLUS) {
				return new StringDataType(((StringDataType)left).getString()+((StringDataType)right).getString());
			}
			throw new InterpreterLogicalErrorException("String operator error, you can only add strings, not " + mathOp.getOperator() + " them.");
		}
		throw new InterpreterLogicalErrorException("Type error, cannot " + mathOp.getOperator() +" "+ left + " with " + right);
		
		
	}
	
	
	
	public boolean booleanCompare(Node booleanCompareNode, HashMap<String, InterpreterDataType> localVariables) throws InterpreterLogicalErrorException {
		//handling the boolean case, given a left expression and right expression, need to return a boolean case that properly evaluates the 2 expressions given a conditional sign
		InterpreterDataType leftExpression = expression(((BooleanCompareNode)booleanCompareNode).getLeft(), localVariables);
		InterpreterDataType rightExpression = expression(((BooleanCompareNode)booleanCompareNode).getRight(), localVariables);
		  if(((BooleanCompareNode)booleanCompareNode).getCompare()==BooleanCompareNode.compareType.EQUAL) {
			  if(leftExpression instanceof StringDataType &&rightExpression instanceof StringDataType) {
				  return ((StringDataType)leftExpression).getString().equals(((StringDataType)rightExpression).getString());
			  }
			  if(leftExpression instanceof IntegerDataType && rightExpression instanceof IntegerDataType) {
				  return ((IntegerDataType)leftExpression).getInteger()==((IntegerDataType)rightExpression).getInteger();
			  }
			  if(leftExpression instanceof RealDataType && rightExpression instanceof RealDataType) {
				  return ((RealDataType)leftExpression).getReal()==((RealDataType)rightExpression).getReal();
			  }
			  if(leftExpression instanceof CharacterDataType && rightExpression instanceof CharacterDataType) {
				  return ((CharacterDataType)leftExpression).getCharacter()==((CharacterDataType)rightExpression).getCharacter();
			  }
		  }
		  if(((BooleanCompareNode)booleanCompareNode).getCompare()==BooleanCompareNode.compareType.NOTEQUAL) {
			  if(leftExpression instanceof StringDataType &&rightExpression instanceof StringDataType) {
				  return ((StringDataType)leftExpression).getString().equals(((StringDataType)rightExpression).getString())==false;
			  }
			  if(leftExpression instanceof IntegerDataType && rightExpression instanceof IntegerDataType) {
				  return ((IntegerDataType)leftExpression).getInteger()!=((IntegerDataType)rightExpression).getInteger();
			  }
			  if(leftExpression instanceof RealDataType && rightExpression instanceof RealDataType) {
				  return ((RealDataType)leftExpression).getReal()!=((RealDataType)rightExpression).getReal();
			  }
			  if(leftExpression instanceof CharacterDataType && rightExpression instanceof CharacterDataType) {
				  return ((CharacterDataType)leftExpression).getCharacter()!=((CharacterDataType)rightExpression).getCharacter();
			  }
		  }
		  if(((BooleanCompareNode)booleanCompareNode).getCompare()==BooleanCompareNode.compareType.GREATHERTHANEQUAL) {
			  if(leftExpression instanceof StringDataType &&rightExpression instanceof StringDataType) {
				  return ((StringDataType)leftExpression).getString().length()>=((StringDataType)rightExpression).getString().length();
			  }
			  if(leftExpression instanceof IntegerDataType && rightExpression instanceof IntegerDataType) {
				  return ((IntegerDataType)leftExpression).getInteger()>=((IntegerDataType)rightExpression).getInteger();
			  }
			  if(leftExpression instanceof RealDataType && rightExpression instanceof RealDataType) {
				  return ((RealDataType)leftExpression).getReal()>=((RealDataType)rightExpression).getReal();
			  }
			  if(leftExpression instanceof CharacterDataType && rightExpression instanceof CharacterDataType) {
				  return ((CharacterDataType)leftExpression).getCharacter()>=((CharacterDataType)rightExpression).getCharacter();
			  }
		  }
		  if(((BooleanCompareNode)booleanCompareNode).getCompare()==BooleanCompareNode.compareType.LESSTHANEQUAL) {
			  if(leftExpression instanceof StringDataType &&rightExpression instanceof StringDataType) {
				  return ((StringDataType)leftExpression).getString().length()<=((StringDataType)rightExpression).getString().length();
			  }
			  if(leftExpression instanceof IntegerDataType && rightExpression instanceof IntegerDataType) {
				  return ((IntegerDataType)leftExpression).getInteger()<=((IntegerDataType)rightExpression).getInteger();
			  }
			  if(leftExpression instanceof RealDataType && rightExpression instanceof RealDataType) {
				  return ((RealDataType)leftExpression).getReal()<=((RealDataType)rightExpression).getReal();
			  }
			  if(leftExpression instanceof CharacterDataType && rightExpression instanceof CharacterDataType) {
				  return ((CharacterDataType)leftExpression).getCharacter()<=((CharacterDataType)rightExpression).getCharacter();
			  }
		  }
		  if(((BooleanCompareNode)booleanCompareNode).getCompare()==BooleanCompareNode.compareType.GREATHERTHAN) {
			  if(leftExpression instanceof StringDataType &&rightExpression instanceof StringDataType) {
				  return ((StringDataType)leftExpression).getString().length()>((StringDataType)rightExpression).getString().length();
			  }
			  if(leftExpression instanceof IntegerDataType && rightExpression instanceof IntegerDataType) {
				  return ((IntegerDataType)leftExpression).getInteger()>((IntegerDataType)rightExpression).getInteger();
			  }
			  if(leftExpression instanceof RealDataType && rightExpression instanceof RealDataType) {
				  return ((RealDataType)leftExpression).getReal()>((RealDataType)rightExpression).getReal();
			  }
			  if(leftExpression instanceof CharacterDataType && rightExpression instanceof CharacterDataType) {
				  return ((CharacterDataType)leftExpression).getCharacter()>((CharacterDataType)rightExpression).getCharacter();
			  }
		  }
		  if(((BooleanCompareNode)booleanCompareNode).getCompare()==BooleanCompareNode.compareType.LESSTHAN) {
			  if(leftExpression instanceof StringDataType &&rightExpression instanceof StringDataType) {
				  return ((StringDataType)leftExpression).getString().length()<((StringDataType)rightExpression).getString().length();
			  }
			  if(leftExpression instanceof IntegerDataType && rightExpression instanceof IntegerDataType) {
				  return ((IntegerDataType)leftExpression).getInteger()<((IntegerDataType)rightExpression).getInteger();
			  }
			  if(leftExpression instanceof RealDataType && rightExpression instanceof RealDataType) {
				  return ((RealDataType)leftExpression).getReal()<((RealDataType)rightExpression).getReal();
			  }
			  if(leftExpression instanceof CharacterDataType && rightExpression instanceof CharacterDataType) {
				  return ((CharacterDataType)leftExpression).getCharacter()<((CharacterDataType)rightExpression).getCharacter();
			  }
		  }
		
		return false;
	}
	
	public void test() throws InterpreterLogicalErrorException {
		//test function to see all of the variables after the interpreter has performed its task, good for debugging
		ProgramNode programs = ((ProgramNode)program);
		programMap = ((ProgramNode)program).getHashMap();
		interpretFunction(programMap.get("start"));//as a bonus it calls start which is potentially how the final interpreter should look
		
		//System.out.println(programMap);
	}

}

class InterpreterLogicalErrorException extends Exception{
	public InterpreterLogicalErrorException(String message) {
		super(message);
	}
}
