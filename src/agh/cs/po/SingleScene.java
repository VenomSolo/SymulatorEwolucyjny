package agh.cs.po;

public class SingleScene extends Scene{
    private static SingleScene instance = null;

    public SingleScene(boolean bAllowTick) {
        super(bAllowTick);
    }

    public static synchronized SingleScene getInstance(){
        if(instance == null){
            instance = new SingleScene(true);
        }
        return instance;
    }

    protected static synchronized void setInstance(SingleScene newInstance)
    {
        if(instance == null)
        {
            instance = newInstance;
        }
    }
}
