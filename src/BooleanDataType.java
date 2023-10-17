
public class BooleanDataType extends InterpreterDataType {
	
	public BooleanDataType(boolean bool) {
		this.setBool(bool);
	}
	
	private boolean bool;
	
	
	@Override
	public String ToString() {
		return bool + "";	
	}

	@Override
	public void FromString(String input) {
		bool = Boolean.parseBoolean(input);
	}

	public boolean getBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

}
