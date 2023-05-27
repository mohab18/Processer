package Engine;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Mutex{

//attributes
    public Queue<Integer> Blockedqueue;
    private int ownerID;
    private enum MutexState{
        ZERO,
        ONE
    }
    private MutexState state;

//constructor
    public Mutex(){
        state=MutexState.ONE;
        ownerID=-1;
        Blockedqueue=new LinkedList<Integer>();
    }

//semWait & semSignal
    public void semWait(int id,Queue<Integer> Ready) throws InterruptedException{
        if(state==MutexState.ONE){
            state=MutexState.ZERO;
            ownerID=id;
        }else{
            Blockedqueue.add(id);
        }
    }

    public void semSignal(int id,Queue<Integer> Ready){
        if (ownerID==id){
            if (Blockedqueue.isEmpty()){
                state=MutexState.ONE;
            }
            else{
            	int i=Blockedqueue.remove();  
            	Ready.add(i);
            	ownerID=i;
                state=MutexState.ONE;
             }
        }
    }
}
