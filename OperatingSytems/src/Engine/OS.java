package Engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class OS{
    int processNum;
    int Finished;
    
    public OS()
    {
    	
    	
    }
    public static String readFile(String FilePath) throws IOException
    {
    	BufferedReader f = new BufferedReader(new FileReader("src/Disk/"+FilePath+".txt"));
    	String line= f.readLine();
    	f.close();
    	return line;
    }
    public  void writeFile(String FilePath, String x) throws IOException
    {
    	if(FilePath.equals("Olayan"))
    	{
    		FileWriter f =new FileWriter("src/Disk/"+FilePath+".txt");
         	f.write(x);
         	f.close();
    	}
    	else
    	{
    		createFile(FilePath);
    		FileWriter f =new FileWriter("src/Disk/"+FilePath+".txt");
         	f.write(x);
         	f.close();
    	}
         	
    }
    public static void createFile(String FilePath) throws IOException
    {
    	 File file = new File("src/Disk/"+FilePath+".txt");
    	 file.createNewFile();
    }
    public void print(Object x)
    {
    	System.out.println(x.toString());
    }
    public String Takeinput()
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter value :");
        String str=sc.nextLine();
        return str;
    }

   
    public boolean Finished()
    {
    	return processNum==Finished;
    }
   
    
 
    
}
