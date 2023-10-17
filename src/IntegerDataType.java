
public class IntegerDataType extends InterpreterDataType {
	
	public IntegerDataType(int integer) {
		this.setInteger(integer);
	}

	private int integer;
	
	@Override
	public String ToString() {
		return integer + "";
	}
	
	public String toString() {
		return integer + "";
	}

	@Override
	public void FromString(String input) {
		integer = Integer.parseInt(input);
	}

	public int getInteger() {
		return integer;
	}

	public void setInteger(int integer) {
		this.integer = integer;
	}

}
