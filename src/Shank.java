import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Shank extends Lexer {

	public static void main(String[] args) throws IOException,InvalidCharacterLengthException, CharacterNotFoundException, TooManyArgumentsException,InvalidCharacterException,NoEndOfLineException,MultipleAdditionException, IncorrectIndentationException, IncorrectSyntaxException, InterpreterLogicalErrorException {
		String shankLines = "";
		Path mypath = Paths.get(args[0]);
		List<String> lines = Files.readAllLines(mypath);
		for (int i = 0; i < lines.size(); i++) {
			shankLines += lines.get(i);// turning the List into a string, also adding \n to track the end of lines
			shankLines += "\n ";
		}
		Lexer Lex = new Lexer();
		Parser parser = new Parser(Lex.lex(shankLines));
		//Interpreter interpreter = new Interpreter(parser.parse());
		//Lex.lex(shankLines);// Lexer instance using the lex method	
		if (args.length == 1) {
			System.out.println(Lex);
			System.out.println("////////////////////// END OF THE LEXER, now beginning to parse");
			Interpreter interpreter = new Interpreter(parser.parse());
			System.out.println("////////////////////// END OF THE PARSER, now beginning to interpret");
			interpreter.test();//debugging function that calls function "start" which is how the program should be started
			
		} else 
			throw new TooManyArgumentsException("Invalid argument amount, make sure you have exactly 1 argument in the main method configuration");
	}

}