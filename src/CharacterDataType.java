
public class CharacterDataType extends InterpreterDataType {

	public CharacterDataType(char character) {
		this.setCharacter(character);
		
	}
	
	private char character;
	
	@Override
	public String ToString() {
		return character+"";
	}

	public String toString() {
		return character+"";
	}
	@Override
	public void FromString(String input) {
		character = input.length()>0?input.charAt(0):'0';
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

}
