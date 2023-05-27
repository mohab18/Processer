import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Cpu {
	int[] R;
	int pc;
	Memory memory;
	int currInst;
	int opcode,r1,r2,r3,shamt,imm,address,op1,op2,op3,op4,res,instNum,mem_wb;
	Queue<int[]> ID_MEM;
	Queue<int[]> EX_WB;
	Queue<int[]> ID_WB;
	Queue<Integer> IF_ID;
	Queue<Integer> ID_EX;
	boolean[] flag;
	boolean [] jump;
	int JumpStart;
	int JumpEnd;
	boolean memStart;
	boolean wbStart;
	public Cpu(Memory memory) {
		R = new int[32];
		pc = 0;
		this.memory=memory;
		r1=0;
		r2=0;
		r3=0;
		shamt=0;
		imm=0;
		address=0;
		op1=0;
		op2=0;
		op3=0;
		res=0;
		ID_WB= new LinkedList<int[]>();
		ID_MEM= new LinkedList<int[]>();
		EX_WB= new LinkedList<int[]>();
		IF_ID= new LinkedList<Integer>();
		ID_EX= new LinkedList<Integer>();
		flag = new boolean[5];
		jump =new boolean[5];
	}

	
	public void fetch() {
		if(jump[0])
		{
			
			
			if(pc==JumpStart+1)
			{
				
				System.out.println("Fetch Instruction "+(pc +1));
				System.out.println("Fetch Parameters : "  + " PC : " +pc);
			}
			else
			{
				pc=JumpEnd-1;
				System.out.println("Fetch Instruction "+(pc +1));
				currInst=memory.MainMemory[pc];		
				jump[0]=false;
				System.out.println("Fetch Parameters : "  + " PC : " +pc);
			}
		}
		else
		{
			System.out.println("Fetch Instruction "+(pc + 1));
			currInst=memory.MainMemory[pc];	
			System.out.println("Fetch Parameters : "  + " PC : " +pc);
		}
		
		
		
	}
	
	public void decode() {
	
		
		if(jump[1])
		{
			
			if(pc>=JumpEnd)
			{
				jump[1]=false;
			}
		}
		if(!jump[1] || (jump[1] && pc==JumpStart+1))
		{
			int [] MEM = new int[3];
			int [] WB = new int[4];
			int EX=pc;
			ID_EX.add(EX);
			System.out.println("Decode Instruction "  + pc);
			System.out.println("Decode Parameters : "  + " Instruction  : " +currInst);
			MEM[0]=pc;
			WB[0]=pc;
			opcode = (currInst & 0b11110000000000000000000000000000) >> (28);
			if(opcode==-5)
			{
				opcode=11;
			}
			else if(opcode==-6)
			{
				opcode=10;
			}
			else if(opcode==-7)
			{
				opcode=9;
			}
			else if(opcode==-8)
			{
				opcode=8;
			}
			
		
			MEM[1]=opcode;
			WB[1]=opcode;
			if (opcode == 7) {
				address = (currInst & 0b00001111111111111111111111111111);
			}
			else if (opcode == 3 || opcode == 4 || opcode == 6 || opcode == 10 || opcode == 11) {
				r1 = (currInst & 0b00001111100000000000000000000000) >> (23);
				r2 = (currInst & 0b00000000011111000000000000000000) >> (18);
				imm =(currInst & 0b00000000000000111111111111111111);
				
				op1 = R[r1];
				op2 = R[r2];
				if(opcode==11)
					MEM[2]=r1;
				if(opcode==10)
					MEM[2]=op1;
				if(opcode!=4)
				{
					WB[2]=r1;
				}
			}
			else {
			
				r1 = (currInst & 0b00001111100000000000000000000000) >> (23);
				r2 = (currInst & 0b00000000011111000000000000000000) >> (18);
				r3 = (currInst & 0b00000000000000111110000000000000) >> (13);
				shamt=(currInst &0b00000000000000000001111111111111);
			
				op1 = R[r1];
				op2 = R[r2];
				op3 = R[r3];
				WB[2]=r1;
			}
			ID_WB.add(WB);
			ID_MEM.add(MEM);
		}
		
		
	}
	
	public void execute() {
		if(jump[2])
		{
			if(!ID_EX.isEmpty())
			{	
				
				jump[2]=false;	
			}
			else
				return;
		}
		if(!jump[2])
		{
			int PC = ID_EX.peek();
			System.out.println("Execute Instruction"+ PC);
			int [] WB = new int[2];
			WB[0]=opcode;
			
			switch (opcode) {
			
				case 0: res = op2 + op3;WB[1]=res;System.out.println("Execute Parameters : "  + " Opcode  : " + opcode + "R2 VALUE :" + op2 + "R3 VALUE :" + op3 );break;
				case 1: res = op2 - op3;WB[1]=res;System.out.println("Execute Parameters : "  + " Opcode  : " + opcode + "R2 VALUE :" + op2 + "R3 VALUE :" + op3 ); break;
				case 2: res = op2 * op3;WB[1]=res;System.out.println("Execute Parameters : "  + " Opcode  : " + opcode + "R2 VALUE :" + op2 + "R3 VALUE :" + op3 );break;
				case 3: res = imm; WB[1]=res; System.out.println("Execute Parameters : "  + " Opcode  : " + opcode+   " IMMEDIATE :" + imm);break;
				case 4: if (op1 == op2) {res = PC+imm; setValuesBool(jump);JumpStart=PC;JumpEnd=res;} System.out.println("Execute Parameters : "  + " Opcode  : " + opcode + "R1 VALUE :" + op1 + "R3 VALUE :" + op2 + " IMMEDIATE : " + imm); break;
				case 5: res = op2 & op3;WB[1]=res;System.out.println("Execute Parameters : "  + " Opcode  : " + opcode + "R2 VALUE :" + op2 + "R3 VALUE :" + op3 ); break;
				case 6: res = op2 ^ imm;WB[1]=res; System.out.println("Execute Parameters : "  + " Opcode  : " + opcode + "R2 VALUE :" + op2 + " IMMEDIATE :" + imm  ); break;
				case 7: res = (((pc & 0b11110000000000000000000000000000) >> (28)) >> 28) | address; setValuesBool(jump);JumpStart=PC;JumpEnd=res;
				System.out.println("Execute Parameters : "  + " Opcode  : " + opcode + " ADDRESS : " + address);
				break; // not sure
				case 8: res = op2 << shamt;WB[1]=res;System.out.println("Execute Parameters : "  + " Opcode  : " + opcode + "R2 VALUE :" + op2 + " SHAMT :" + shamt);break;
				case 9: res = op2 >>> shamt;WB[1]=res; System.out.println("Execute Parameters : "  + " Opcode  : " + opcode + "R2 VALUE :" + op2 + " SHAMT :" + shamt);break;
				case 10 :res=op2+imm;WB[1]=res;System.out.println("Execute Parameters : "  + " Opcode  : " + opcode + "R2 VALUE :" + op2 + " IMMEDIATE :" + imm  ); break;
				case 11 :res=op2 + imm;WB[1]=res;System.out.println("Execute Parameters : "  + " Opcode  : " + opcode + "R2 VALUE :" + op2 + " IMMEDIATE :" + imm  ) ;break;
				default: break;
			}
			
			EX_WB.add(WB);
		}
		
	}
	public void memory(){
		if(jump[3])
		{
			if(!ID_MEM.isEmpty())
			{
				if(ID_MEM.peek()[0]==JumpStart+1)
				{
					ID_MEM.remove();
				}
				else if(ID_MEM.peek()[0]>=JumpEnd)
					  jump[3]=false;
				
			}
		}
		if(!jump[3] || (jump[3] && !ID_MEM.isEmpty() && ID_MEM.peek()[0]<=JumpStart))
		{
			if(memStart)
			{
				int[] ID=ID_MEM.remove();
				System.out.println("MEM Instruction"+ ID[0]);
				int OPCODE=ID[1];
			     System.out.println("MEMORY PARAMETERS OPCODE :" + OPCODE + " ALU RESULT : " +res + " R1 OR R1 VALUE : "+  R[ID[2]]);
				switch(OPCODE) {
					case 10: mem_wb = memory.read(res); System.out.println("Value of Memory address " + res + " was loaded in R" + mem_wb ); break;
					case 11: memory.write(res, R[ID[2]]); System.out.println(R[ID[2]]+" was Stored in Memory Address " + res ); break;
					default: break;
				}
				wbStart=true;
				memStart=false;
			}
			
			
		}
		
	}
	
	public void writeBack() {
		if(jump[4])
		{
			
			if(!ID_WB.isEmpty())
			{
				
				if(ID_WB.peek()[0]==JumpStart+1)
				{
					ID_WB.remove();
				}
				else if(ID_WB.peek()[0]>=JumpEnd)
					  jump[4]=false;
				
			}
		}
		if(!jump[4] || (jump[4] && !ID_WB.isEmpty() &&ID_WB.peek()[0]<=JumpStart ) && !EX_WB.isEmpty())
		{
			if(wbStart)
			{
				int[] ID=ID_WB.remove();
				System.out.println("WB Instruction"+ ID[0]);
				int OPCODE=ID[1];
				 wbStart=false;
				int[] EX=EX_WB.remove();
				System.out.println("WB PARAMETERS : OPCODE " + OPCODE + " DEST REG : R" + ID[2] + " VALUE TO BE WRITTEN : " + EX[1]);
			     if(OPCODE!=4 && OPCODE!=7 && OPCODE!=11)
			     {
			    	
			    	 if(ID[2]==0)
			    	 {
			    		 return;
			    	 }
			    	 else
			    	 {
			    		 if(OPCODE==10)
			    			 R[ID[2]]=mem_wb;
			    		 else
			    		   R[ID[2]]=EX[1];
			    		System.out.println("Register R" + ID[2] + " = " + EX[1]);
			    	 }
			     }
			}
			
		}
		
		
	}
	public void adjustFlags(int i)
	{
     
		if(i%2==0)
		{
			flag[1]=true;
			flag[3]=true;
			flag[2]=true;
			flag[0]=false;
			flag[4]=false;
			
		}
		else
		{
			
			flag[0]=true;
			flag[3]=false;
			flag[4]=true;
			flag[1]=false;
			flag[2]=false;
		}
	}
	public void Pipeline(int noOfins)
	{
		
		int NoOfCycles=7 + (( noOfins - 1) *2);
		for(int i=1;i<=NoOfCycles;i++)
		{
			
			System.out.println("######################");
			System.out.println("CYCLE" +i);
			adjustFlags(i);
			if(flag[4] &&  i>=7 && ID_WB.size()>0)
			{
				writeBack();
			}
			System.out.println("------------");
			if(flag[3] &&  i>=6 && ID_MEM.size()>0)
			{
				memory();
			}
			System.out.println("------------");
			if(flag[2] &&  i>=4 && ID_EX.size()>0)
			{
				execute();
			}
			else if(i>=4 && ID_EX.size()>0)
			{
				
				if(!jump[2])
				{
					int d=ID_EX.remove();
					System.out.println("Execute Instruction " + d);
					memStart=true;
				}
				else
				{
				
					if(!ID_EX.isEmpty() && ID_EX.peek()==JumpStart)
					{
							int d=ID_EX.remove();
							System.out.println("Execute Instruction " + d);
							memStart=true;
						    ID_EX.remove();
					}
					
				}	
			}
			System.out.println("------------");
			
			if(flag[1] &&  i>=2 && pc<=noOfins)
			{
				decode();
			}
			else if(i>=2 &&pc<=noOfins)
			{
				if((!jump[1]) || (jump[1] && pc==JumpStart+1))
				{
					   System.out.println("Decode Instruction " + pc);
					   
				}
				  
				if(pc==noOfins)
				{
					pc++;
				}
			}
			System.out.println("------------");
			if(flag[0] &&  i>=1 && pc<noOfins)
			{
				fetch();
			    pc++;
				
			}
		
		}
		printReg();
		printMem();
	}
	public void printMem()
	{
		System.out.println("----MEMORY---");
		for(int i=0;i<2048;i++)
		{
			System.out.print("Address " + i +": " + memory.read(i) + " - ");
		}
		System.out.println();
		     
	}
	

	public void printReg()
	{
		System.out.println("----REGISTER FILE----------");
		for(int i=0;i<R.length;i++)
		{
			System.out.print(" R" + i + " : " +R[i]);
		}
		System.out.print(" PC : " + pc);
		System.out.println();
	}
	public void setValuesBool(boolean[] x)
	{
		x[0]=true;
		x[1]=true;
		x[2]=true;
		x[3]=true;
		x[4]=true;
		
	}

	public static void main(String[] args) throws IOException {
		Memory mem=new Memory();
		Parser p = new Parser();
		p.parse("TEST1",mem);
		Cpu x = new Cpu(mem);

		x.Pipeline(p.numOfinst);
		
	}
	
}

