public class Worker{
private static final int min_cap = 1;
private static final int exc_cap = 3;
private static final int hau_cap = 5;
public int uni_id = 0;
public int workerNum

public Worker(int workerNum,int uni_id,int workerType){
	this.workerNum = workerNum;
	this.uni_id = uni_id;
}


public void setMiner(int min_cap){
	this.min_cap = min_cap;
}

public void setExcav(int exc_cap){
	this.exc_cap = exc_cap;
}

public void  setHauler(int hau_cap){
	this.hau_cap = hau_cap;
}



}
