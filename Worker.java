public class Worker{
    private static final int min_cap = 1;
    private static final int exc_cap = 3;
    private static final int hau_cap = 5;
    public int uni_id = 0;
    public int capacity;

    public Worker(int uni_id, int cap){
        this.capacity = cap;
        this.uni_id = uni_id;
    }



}