
public class StringDataType extends InterpreterDataType {
	
	public StringDataType(String string) {
		this.setString(string);
	}
	
	private String string;

	@Override
	public String ToString() {
		return string+"";
	}
	
	public String toString() {
		return string+"";
	}

	@Override
	public void FromString(String input) {
		string = input; //since input is already a string, no need for conversion
		
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

}
