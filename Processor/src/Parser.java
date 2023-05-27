import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Spliterator;

public class Parser {
	private ArrayList<String> Instructions;
	
	private int instNum = 0;
	int numOfinst;
	public Parser() {
		numOfinst=0;
	
		this.Instructions =  new ArrayList<String>();
	}
	
	public void parse(String filepath,Memory memory) throws IOException {
		BufferedReader f = new BufferedReader(new FileReader("src/"+filepath+".txt"));
		String st="";
		int numOFinst=0;
		while((st=f.readLine())!=null) {
			numOfinst++;
			
			this.Instructions.add(st);
			instNum++;
		}
		int i=0;
		while (i!=numOfinst) {
			String getInst = Instructions.get(i);
			String split[] = getInst.split(" ");
			int opCode=0;
			switch (split[0].toUpperCase()) {
				case "ADD": opCode=0;	break;
				case "SUB": opCode=1;	break;
				case "MUL": opCode=2;	break;
				case "MOVI": opCode=3;	break;
				case "JEQ": opCode=4;	break;
				case "AND": opCode=5;	break;
				case "XORI": opCode=6;	break;
				case "JMP": opCode=7;	break;
				case "LSL": opCode=8;	break;
				case "LSR": opCode=9;	break;
				case "MOVR": opCode=10;	break;
				case "MOVM": opCode=11;	break;
				default:break;
			}
		    int arg1=0;
			int arg2=0;
			int arg3 = 0;
			int arg4=0;
			int inst=0;
			if (opCode==3 || opCode==4 || opCode==6 ||opCode==10 ||opCode==11 ){
				opCode=opCode<<28;
				
				arg1= Integer.parseInt(split[1].substring(1))<<23;
				if(opCode==3)
				{
					arg2=0;
				}
				else
				   arg2= Integer.parseInt(split[2].substring(1)) <<18;
				arg3 = Integer.parseInt(split[3]);
				
				inst=opCode + arg1 +arg2+arg3;
			
			}
			else if (opCode==7){
				opCode= opCode<<28;
				arg2= Integer.parseInt(split[1]);
				inst=opCode + arg2;
			}
			else
			{
				
				arg1= Integer.parseInt(split[1].substring(1))<<23;
				arg2= Integer.parseInt(split[2].substring(1)) <<18;
				if(opCode==9 || opCode==8)
				{
					
					arg3=0;
					arg4=Integer.parseInt(split[3]);
					
				}
				else
				{
					 arg3= Integer.parseInt(split[3].substring(1)) <<13;
				       arg4=0;
				}
				  
			    opCode= opCode<<28;
			   
				inst=opCode +arg1 +arg2+arg3 + arg4;
			}
			
			memory.write(i, inst);;
			
			i++;
		}	
	}

	

}

