package Engine;

public class PCB {
	 int processID;
	  String processState;
	  int pc;
	  int start;
	  int end;
	public PCB(int processID, String processState, int pc, int start,int end) {
		this.processID = processID;
		this.processState = processState;
		this.pc = pc;
		this.start = start;
		this.end = end;
		
	}
	  
}
