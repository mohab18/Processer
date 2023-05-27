
public class Memory {
	int[] MainMemory;
	
	public Memory () {
		MainMemory = new int[2048];
	}
	
	public int read(int address) {
		return MainMemory[address];
	}
	
	public void write(int address, int word) {
		MainMemory[address] = word;
	}
}
