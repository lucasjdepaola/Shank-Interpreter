
public class ArrayDataType extends InterpreterDataType {

	public ArrayDataType(InterpreterDataType[] array) {
		this.setArray(array);
	}
	
	private InterpreterDataType[] array;
	
	@Override
	public String ToString() {
		String returnString = "";
		for(InterpreterDataType IDT: array) {
			returnString += IDT+" ";
		}
		return returnString;
	}
	
	public String toString() {
		String returnString = "";
		for(InterpreterDataType IDT: array) {
			returnString += IDT==null?"":IDT+" ";
		}
		return returnString;
	}

	@Override
	public void FromString(String input) {
		String[] stringInput = input.split(",");//since it's an array, we're going to store comma separated components inside of the array, assuming the user separates their intended array with commas
		for(int i = 0; i < array.length; i++) {
			array[i].FromString(stringInput[i]);//taking each array element and performing a FromString(*split element*) on it
		}
	}

	public InterpreterDataType[] getArray() {
		return array;
	}

	public void setArray(InterpreterDataType[] array) {
		this.array = array;
	}
	
	public void setIndex(InterpreterDataType IDT, int index) {
		array[index] = IDT;
	}

}
