public class MineFactory{

    int id = 0;
    int x_coord = 0;
    int y_coord = 0;
    int res = -1;
    String recTag = "";

    public MineFactory(int m, String resourceTag,int x, int y, int resources){
        y_coord = y;
        x_coord = x;
        id = m;
        recTag = resourceTag;
        res = resources;

    }

    public int getMineID(int mineID){
        return mineID;
    }

    public int getFactID(int factID){
        return factID;
    }

    public int getX(int x){
        return x;
    }

    public int getY(int y){
        return y;
    }


}