package Engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class Parser {
     OS os;
     int ClockCycle;
     Mutex input;
     Mutex output;
     Mutex file;
     int quanta;
     Queue<Integer> ReadyQueue;
     Queue<Integer> BlockedQueue;
     ArrayList<Integer> Finished;
     boolean isRunning;
     boolean assigned;
     String readinput;
     Queue<Integer> FIFO;
     int[] processes;
     int InDisk;
     Object[] memory;
    public Parser()
    {
    	memory =new Object[40];
    	quanta=0;
    	os = new OS();
    	ClockCycle=0;
    	input = new Mutex();
    	output = new Mutex();
    	file = new Mutex();
    	ReadyQueue = new LinkedList<Integer>(); 
    	BlockedQueue = new LinkedList<Integer> (); 
    	FIFO=new LinkedList<Integer>();
         Finished = new ArrayList<Integer>();
         isRunning=false;
         assigned=false;
         readinput="";
         processes= new int[3];
    	 InDisk=-1;
    }
    public Object readMemory(int loc)
    {
    	return memory[loc];
    }
    public void writeMemory(Object var,int loc)
    {
    	memory[loc]=var;
    }
 public String[] readProgram(String Filepath) throws IOException {
	   File file = new File(System.getProperty("user.dir")+"/"+Filepath+".txt");
        BufferedReader br =new BufferedReader(new FileReader(file));
        String st;
        String x []= new String[15];
        int i=0;
        while((st=br.readLine())!=null)
        {
        	x[i]=st;
        	i++;
        }
        return x;
    }
    public void assign(String x , Object y,int id) throws Exception
    {
    	int loc=search(id);
    	
    	if(loc!=-1)
    	{	
    		if(y instanceof String)
	    	{
	    		if(y.equals("input"))
	    		{
	    			String value=os.Takeinput();
	    			Pair var= new Pair(x,value);
	    			writeMemory(var, loc);
	    			return;
	    		}
	    		else{
	    			Pair var= new Pair(x,(String)y);
					writeMemory(var, loc);
	    		}
	    		
	    	}
    		else
    		{
    			String y1= Integer.toString((int)y);
    			Pair var= new Pair(x,y1);
				writeMemory(var, loc);
    		}
    		
	    	
    	}
    	
		    	
    }
    public int search(int id)
    {
    	if((Integer)readMemory(0)==id)
    	{
    		for(int i=17;i<20;i++)
    		{
    			if(readMemory(i)==null || readMemory(i).equals("null"))
    				return i;
    		}
    		
    	}
    	else
    	{	
    		for(int i=37;i<40;i++)
    		{
    			if(readMemory(i)==null || readMemory(i).equals("null"))
    				return i;
    		}
    	}
    	return -1;
    	
    }
    public void printFromTo(String x , String y)
    
    {	
    	int x1=0;
    	int y1=0;
    	x1 = Integer.parseInt(x);
		y1 = Integer.parseInt(y);
    	for(int i=x1+1;i<y1;i++)
    	{
    		os.print(i);
    	}
    }

    public void createProcess(String FilePath,int id) throws IOException
    {
    	String[] linesOfCode=readProgram(FilePath);
    	os.processNum++;
    	if(readMemory(0)==null)
    	{
    		PCB p =new PCB(id,"NotRunning",0,0,19);
    		createProcessHelper(p,linesOfCode);
    		
    	}
    	else if(readMemory(20)==null)
    	{
    		PCB p =new PCB(id,"NotRunning",0,20,39);
    		createProcessHelper(p,linesOfCode);
    	}
    	else
    	{
    		os.createFile("Olayan");
    		String s="";
    		s += Integer.toString(id)+",";
    		s += "NotRunning" +",";
    		s += "0" + ",";
    		s += "0" + ",";
    		s += "19" + ",";
    		for(int i=0;i<linesOfCode.length;i++)
    		{
    			s+=linesOfCode[i]+",";
    		}
    		for(int i=linesOfCode.length;i<19;i++)
    		{
    			s+="null" + ",";
    		}
    		s+="null";
    		System.out.println("Process in Disk ID : " + s);
    		os.writeFile("Olayan" , s);    		
    	}
    }
    public void createProcessHelper(PCB p,String[] linesOfCode)
    {
    	writeMemory(p.processID, p.start);
    	writeMemory(p.processState, p.start+1);
    	writeMemory(p.pc, p.start+2);
    	writeMemory(p.start, p.start+3);
    	writeMemory(p.end, p.start+4);
    	int start=p.start+5;
    	int j=0;
    	for(int i=start;i<start+linesOfCode.length;i++)
    	{
    		writeMemory(linesOfCode[j], i);
    		j++;
    	}
    }

 
    public void MemoryToDisk(int id) throws IOException
    {
    	
    	String s="";
    	if((Integer)readMemory(0)==id)
    	{
		    	s +=Integer.toString((Integer)readMemory(0)) + ",";
		       	s +=readMemory(1) + ",";
		       	for(int i=2;i<=4;i++)
		       	{
		       		
		       		s +=Integer.toString((Integer)readMemory(i)) + ",";
		       	}
		       	for(int i=5;i<17;i++)
		       	{
		       		s +=readMemory(i) + ",";
		       	}
		       	for(int i=17;i<19;i++)
		       	{
		       		if(readMemory(i)==null || readMemory(i).equals("null"))
		       		{	
		       			s +=readMemory(i) + ",";
		       		}
		       		else
		       		{
		       			
		       			Pair x  = (Pair)readMemory(i);
		       			s += x.x +";";
		       			s += x.y +",";
		       		}
		       	}
		       	if(readMemory(19)==null  || readMemory(19).equals("null"))
		   		{	
		   			s +=readMemory(19);
		   		}
		   		else
		   		{
		   			Pair x  = (Pair) readMemory(19);
		   			s += x.x +";";
		   			s += x.y;
		   		}
    	}
    	else
    	{
		    	 s +=Integer.toString((Integer)readMemory(20)) + ",";
		       	 s +=readMemory(21) + ",";
		       	for(int i=22;i<=24;i++)
		       	{
		       		
		       		s +=Integer.toString((Integer)readMemory(i)) + ",";
		       	}
		       	for(int i=25;i<37;i++)
		       	{
		       		s +=readMemory(i) + ",";
		       	}
		       	for(int i=37;i<39;i++)
		       	{
		       		if(readMemory(i)==null || readMemory(i).equals("null"))
		       		{	
		       			s +=readMemory(i) + ",";
		       		}
		       		else
		       		{
		       			
		       			Pair x  = (Pair)readMemory(i);
		       			s += x.x +";";
		       			s += x.y +",";
		       		}
		       	}
		       	if(readMemory(39)==null  || readMemory(39).equals("null"))
		   		{	
		   			s +=readMemory(39);
		   		}
		   		else
		   		{
		   			Pair x  = (Pair) readMemory(39);
		   			s += x.x +";";
		   			s += x.y;
		   		}
    	}
    	
    	os.writeFile("Olayan", s);
    	
    }
    public void DiskToMemory(String x,int id)
    {
    	
    	String[] s= x.split(",");
    	if((Integer)readMemory(0)==id)
    	{
    		writeMemory(Integer.parseInt(s[0]),0);
        	writeMemory(s[1],1);
        	writeMemory(Integer.parseInt(s[2]),2);
        	writeMemory(0,3);
        	writeMemory(19,4);
        	for(int i=5;i<17;i++)
        	{
        		writeMemory(s[i],i);
        	}
        	for(int i=17;i<20;i++)
        	{
        		if(s[i].equals("null"))
        		{	
        			writeMemory(s[i],i);
        		}
        		else
        		{
        			String[] t= s[i].split(";");
        			Pair p = new Pair(t[0],t[1]);
        			writeMemory(p, i);
        			
        		}
        	}
    	}
    	else
    	{
    		writeMemory(Integer.parseInt(s[0]),20);
        	writeMemory(s[1],21);
          	writeMemory(Integer.parseInt(s[2]),22);
        	writeMemory(20,23);
        	writeMemory(39,24);
        	for(int i=25;i<37;i++)
        	{
        		writeMemory(s[i-20],i);
        	}
        	for(int i=37;i<40;i++)
        	{
        		if(s[i-20].equals("null"))
        		{	
        			writeMemory(s[i-20],i);
        		}
        		else
        		{
        			String[] t= s[i-20].split(";");
        			Pair p = new Pair(t[0],t[1]);
        			writeMemory(p, i);
        			
        		}
        	}
    	}
    	
    	
    }
    public void RoundRobin(int ts,int p1Time,int p2Time, int p3Time) throws Exception
    {
    	int RunningProcess=-1;
    	while(true)
    	{
    		
    		System.out.println("----------------------------");
    		System.out.println("ClockCycle: " + ClockCycle);
    		if(p1Time==ClockCycle)
    		{
    			createProcess("Program_1",1);
    			processes[os.processNum-1]=1;
    			ReadyQueue.add(1);
    			FIFO.add(1);
    		}
    		if (p2Time==ClockCycle)
    		{
    			createProcess("Program_2",2);
    			processes[os.processNum-1]=2;
    			ReadyQueue.add(2);
    			FIFO.add(2);
    		}
    		if (p3Time==ClockCycle)
    		{
    			createProcess("Program_3",3);
    			processes[os.processNum-1]=3;
    			ReadyQueue.add(3);
    			FIFO.add(3);
    		}
             if(os.Finished() && Finished.size()==3)
             {
            	 System.out.println("All processes Finished");
            	 break;
             }
             
             if(ReadyQueue.isEmpty())
             {
            	 
            	 if(RunningProcess!=-1 && (Finished.size()!=os.processNum))
            	 {
            		 if(!(Finished.contains(RunningProcess)))
            		 {
            			 if(quanta<=0)
            			 {
            				
            				 ReadyQueue.add(RunningProcess);
            			 }
            			 
            		 }
            	 }
            	 else
            	 {
            		 ClockCycle++;
            		 continue;
            	 }
             }
             
    			if(quanta!=-1)
    			{
    				if(quanta==ts)
    				{
    					quanta=-1;
    				}
    				else if(quanta==0)
    				{
    					
    					RunningProcess=ReadyQueue.remove();
    					isRunning=true;
    					System.out.println("RunningProcess " + RunningProcess);
    					PrintQueues();
    					checkAndSwap(RunningProcess);
    					ChangeState(RunningProcess,"Running");
    					run(RunningProcess,ts);
    					
    					
    				}
    				else if(quanta>0)
    				{
    					
    					System.out.println("RunningProcess " + RunningProcess);
    					run(RunningProcess,ts);
    					
    					
    				}
    			}
    			if(quanta==-1)
    			{
    				
    				ReadyQueue.add(RunningProcess);
    				ChangeState(RunningProcess,"NotRunning");
    				RunningProcess=ReadyQueue.remove();
					System.out.println("RunningProcess " + RunningProcess);
					PrintQueues();
					checkAndSwap(RunningProcess);
					ChangeState(RunningProcess,"Running");
					quanta++;
					run(RunningProcess,ts);
					
    			}
    			checkBlocked();
    			PrintMem();
    		}
    		
    	}
	    	
     
    
    public  void run(int id,int ts) throws Exception
    {
    	String x="";
    	
    	if((Integer)readMemory(0)==id)
		{
    		int pc = (Integer)readMemory(2);
			x=(String)readMemory(pc+5);
		}
		else
		{
			int pc = (Integer)readMemory(22);
			x=(String)readMemory(pc+25);
		}
    	
    		
    		String[] s= x.split(" ");
    		System.out.println("Current Instruction is " +x);
    		if(s[0].equals("print"))
    		{
    			String x1= Data(id,s[1]);
    			quanta++;
    			os.print(x1);
    			incrementPC(id);
		    	ClockCycle++;
    		}
    		else if (s[0].equals("assign"))
    		{
    			if(s[2].equals("readFile"))
    			{
    				if(assigned)
    				{
    					assign(s[1],readinput,id);
        		    	incrementPC(id);
        		    	ClockCycle++;
        		    	quanta++;
        		    	assigned=false;
    				}
    				else
    				{
    					String x1= Data(id,s[3]);
        				 readinput=os.readFile(x1);
        				 assigned=true;
        				ClockCycle++;
        		    	quanta++;
    				}
    				
    		    	
    			}
    			else if(s[2].equals("input"))
    			{
    				if(assigned)
    				{
    					
    					assign(s[1],readinput,id);
        		    	incrementPC(id);
        		    	ClockCycle++;
        		    	quanta++;
        		    	assigned=false;
    				}
    				else
    				{
    					
    					readinput=os.Takeinput();
    					assigned=true;
    					ClockCycle++;
        		    	quanta++;
    				}
    			}
    			else{
    				
    				assign(s[1],s[2],id);
    		    	incrementPC(id);
    		    	ClockCycle++;
    		    	quanta++;
    			}
    		}
    		else if (s[0].equals("writeFile"))
    		{
    			String x1= Data(id,s[1]);
    			String y1= Data(id,s[2]);
    			os.writeFile(x1, y1);
    			incrementPC(id);
		    	ClockCycle++;
		    	quanta++;
    			
    		}
    		else if (s[0].equals("readfile"))
    		{
    			String x1= Data(id,s[1]);
    			os.readFile(x1);
    			incrementPC(id);
		    	ClockCycle++;
		    	quanta++;
    		}
    		else if (s[0].equals("printFromTo"))
    		{
    			String x1= Data(id,s[1]);
    			String y1= Data(id,s[2]);
    			printFromTo(x1, y1);
    			incrementPC(id);
		    	ClockCycle++;
		    	quanta++;
    		}
    		else if (s[0].equals("semWait"))
    		{
    			if(s[1].equals("userInput"))
    			{
    				
    				input.semWait(id,ReadyQueue);
    			    if(input.Blockedqueue.contains(id))
    			    {
    			    	
    			    	ChangeState(id,"Blocked");
    			    	quanta=0;
    			    	ClockCycle++;
    			    	AdjustBlock();
    			    	PrintQueues();
    			    }
    			    else
    			    { 
    			    	
    			    	incrementPC(id);
    			    	ClockCycle++;
    			    	quanta++;
    			    }
    			         
    			}
    			else if(s[1].equals("userOutput"))
    			{
    				output.semWait(id,ReadyQueue);
    				if(output.Blockedqueue.contains(id))
    				{
    					ChangeState(id,"Blocked");
    					quanta=0;
    					ClockCycle++;
    					AdjustBlock();
    					PrintQueues();
    				}
    				else
    				{
    					
    					incrementPC(id);
    					ClockCycle++;
    					quanta++;
    				}
			             
			             
    			}
    			else
    			{
    				file.semWait(id,ReadyQueue);
    				if(file.Blockedqueue.contains(id))
    				{
    					ChangeState(id,"Blocked");
    					quanta=0;
    					ClockCycle++;
    					AdjustBlock();
    					PrintQueues();
    				}
    				else
    				{
    					incrementPC(id);
    					ClockCycle++;
    					quanta++;
    				}
    					
			            
			             
    			}
    			
    				
    		}
    		else if (s[0].equals("semSignal"))
    		{
    			if(s[1].equals("userInput"))
    			{
    				input.semSignal(id, ReadyQueue);
    				incrementPC(id);
			    	ClockCycle++;
			    	quanta++;
			    	checkBlocked();
			    	PrintQueues();
    			}
    			else if(s[1].equals("userOutput"))
    			{
    				output.semSignal(id,ReadyQueue);
    				incrementPC(id);
			    	ClockCycle++;
			    	quanta++;
			    	checkBlocked();
    			}
    			else
    			{
    				file.semSignal(id,ReadyQueue);
    				incrementPC(id);
			    	ClockCycle++;
			    	quanta++;
			    	checkBlocked();
    			}
    			
    		}
    		if((Integer)readMemory(0)==id)
    		{
        		int pc = (Integer)readMemory(2);
    			x=(String)readMemory(pc+5);
    		}
    		else
    		{
    			int pc = (Integer)readMemory(22);
    			x=(String)readMemory(pc+25);
    		}
        	if(x==null  || x.equals("null"))
        	{
        		os.Finished++;
        		ChangeState(id,"Finished");
        		quanta=0;
        		removeBlock(id);
        		System.out.println("Process " + id + "Finished");
        		Finished.add(id);
        		PrintQueues();
        	}
    	
    }
    public String Data(int id, String var)
    {
    	if((Integer)readMemory(0)==id)
    	{
    		for(int i=17;i<20;i++)
    		{
    			if(((Pair) readMemory(i)).x.equals(var))
    			{
    				return ((Pair) readMemory(i)).y;
    			}
    		}
    	}
    	else
    	{
    		for(int i=37;i<40;i++)
    		{
    		
    			if(((Pair) readMemory(i)).x.equals(var))
    			{
    				return ((Pair) readMemory(i)).y;
    			}
    		}
    	}
    	return null;
    }
    public void incrementPC(int id)
    {
    	if((Integer)readMemory(0)==id)
    	{
    		int pc = (Integer)readMemory(2);
    		pc++;
    		writeMemory(pc, 2);
    	}
    	else
    	{
    		int pc = (Integer)readMemory(22);
    		pc++;
    		writeMemory(pc, 22);
    	}
    }
    public void ChangeState(int id,String state)
    {
    	if((Integer)readMemory(0)==id)
    	{
    		writeMemory(state, 1);
    	}
    	else
    	{
    		writeMemory(state, 21);
    	}
    }
    public void AdjustBlock()
    {
    	if(readMemory(1).equals("Blocked"))
    	{
    		if(!BlockedQueue.contains((Integer)readMemory(0)))
    			BlockedQueue.add((Integer)readMemory(0));
    	}
    	if(readMemory(21).equals("Blocked"))
    	{
    		if(!BlockedQueue.contains((Integer)readMemory(20)))
    		     BlockedQueue.add((Integer)readMemory(20));
    	}
    	
    }
    public void checkBlocked()
    {
    	if(ReadyQueue.contains((Integer)readMemory(0)))
    	{
    		if(BlockedQueue.contains((Integer)readMemory(0)))
    		{
    			ChangeState((Integer)readMemory(0),"NotRunning");
    			remove((Integer)readMemory(0),BlockedQueue);
    		}
    		
    	}
    	if(ReadyQueue.contains((Integer)readMemory(20)))
    	{
    		if(BlockedQueue.contains((Integer)readMemory(20)))
    		{
    			ChangeState((Integer)readMemory(0),"NotRunning");
    			remove((Integer)readMemory(20),BlockedQueue);
    		}
    	}
    	if(ReadyQueue.contains(InDisk))
    	{
    		if(BlockedQueue.contains(InDisk))
    		{
    			//ChangeState((Integer)readMemory(0),"NotRunning");
    			remove(InDisk,BlockedQueue);
    		}
    	}
    	
    }
    public void checkAndSwap(int id) throws IOException{
    	if((Integer)readMemory(0)==id)
    	{
    		return;
    	}
    	else if((Integer)readMemory(20)==id)
    	{
    		return;
    	}
    	else
    	{
    		int turn=FIFO.remove();
    		FIFO.add(turn);
    		String tmp=os.readFile("Olayan");
    		System.out.println("Swapped to Disk ID :" + turn);
    		InDisk=turn;
    		MemoryToDisk(turn);
    		DiskToMemory(tmp,turn);
    		System.out.println("Swapped out of Disk ID :" + tmp.split(",")[0]);
    	}
    }
    public void PrintMem()
    {
    	System.out.print("Process ID : " + readMemory(0) + " ProcessState : " + readMemory(1) +   " PC : " + readMemory(2) + " Process Start : " + readMemory(3) + " Process End : " + readMemory(4)  );
        for(int i=5;i<17;i++)
        {
        	System.out.print(" Instruction Number " + (i-4) + " : " + readMemory(i));
        }
        for(int i=17;i<20;i++)
        {
        	if(readMemory(i)==null || readMemory(i).equals("null"))
        	{	
        		System.out.print("Variable " + " : " + "null");
        	}
        	else
        	{
        		Pair t = (Pair) readMemory(i);
            	System.out.print("Variable " + t.x + " : " + " Data : " + t.y);
        	}
        	
        }
        System.out.println();
        System.out.print("Process ID : " + readMemory(20) + " ProcessState : " + readMemory(21) +   " PC : " + readMemory(22) + " Process Start : " + readMemory(23) + " Process End : " + readMemory(24)  );
        for(int i=25;i<37;i++)
        {
        	System.out.print(" Instruction Number " + (i-24) + " : " + readMemory(i));
        }
        for(int i=37;i<40;i++)
        {
        	if(readMemory(i)==null || readMemory(i).equals("null"))
        	{	
        		System.out.print("Variable " + " : " + "null");
        	}
        	else
        	{
        		Pair t = (Pair) readMemory(i);
            	System.out.print("Variable " + t.x + " : " + " Data : " + t.y);
        	}
        	
        }
        System.out.println();
    }	
    public void removeBlock(int id)
    {
    	if(BlockedQueue.contains(id))
    	{
    		remove(id,BlockedQueue);
    	}
    	if(input.Blockedqueue.contains(id))
    	{
    		remove(id,input.Blockedqueue);
    	}
    	if(output.Blockedqueue.contains(id))
    	{
    		remove(id,output.Blockedqueue);
    	}
    	if(file.Blockedqueue.contains(id))
    	{
    		remove(id,file.Blockedqueue);
    	}
    	
    	
    }
    public void PrintQueues()
    {
    	System.out.println("Ready Queue : " +ReadyQueue.toString());
    	System.out.println("Blocked Queue : " +BlockedQueue.toString());
    	
    }
    public void remove(int t,Queue<Integer> q)
    {
     
        // Helper queue to store the elements
        // temporarily.
        Queue<Integer> ref = new LinkedList<>();
        int s = q.size();
        int cnt = 0;
     
        // Finding the value to be removed
        while (!q.isEmpty() && q.peek() != t) {
            ref.add(q.peek());
            q.remove();
            cnt++;
        }
     
        // If element is not found
        if (q.isEmpty()) {
            System.out.print("element not found!!" +"\n");
            while (!ref.isEmpty()) {
     
                // Pushing all the elements back into q
                q.add(ref.peek());
                ref.remove();
            }
        }
     
        // If element is found
        else {
            q.remove();
            while (!ref.isEmpty()) {
     
                // Pushing all the elements back into q
                q.add(ref.peek());
                ref.remove();
            }
            int k = s - cnt - 1;
            while (k-- >0) {
     
                // Pushing elements from front of q to its back
                int p = q.peek();
                q.remove();
                q.add(p);
            }
        }
    }

    public static void main (String [] args) throws Exception
    {
    	Parser p =new Parser();
    	Scanner sc=new Scanner(System.in);
        System.out.println("Enter Time Slice");
         int ts=sc.nextInt();
         sc=new Scanner(System.in);
         System.out.println("Enter P1 ArrivalTime");
          int p1=sc.nextInt();
            sc=new Scanner(System.in);
          System.out.println("Enter P2 ArrivalTime");
           int p2=sc.nextInt();
           sc=new Scanner(System.in);
           System.out.println("Enter P3 ArrivalTime");
            int p3=sc.nextInt();
         
    	p.RoundRobin(ts, p1, p2,p3);
    }
}
