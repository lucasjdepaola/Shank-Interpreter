
public class RealDataType extends InterpreterDataType {
	
	public RealDataType(float real) {
		this.setReal(real);
	}

	private float real;
	@Override
	public String ToString() {
		return real+"";
	}
	
	public String toString() {
		return real+"";
	}

	@Override
	public void FromString(String input) {
		real = Float.parseFloat(input);
	}

	public float getReal() {
		return real;
	}

	public void setReal(float real) {
		this.real = real;
	}

}
