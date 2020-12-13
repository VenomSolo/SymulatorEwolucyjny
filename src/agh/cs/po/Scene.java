package agh.cs.po;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Scene extends Thread implements ICanTick{
    private ArrayList<Map> maps;
    private HashMap<String, ArrayList<AbstractObject>> register;
    private boolean bAllowTick;

    public Scene(Map map, boolean bAllowTick)
    {
        register = new HashMap<>();
        maps = new ArrayList<>();
        maps.add(map);
        this.bAllowTick = bAllowTick;
    }

    public void AddMap(Map map)
    {
        maps.add(map);
    }

    public void Register(String category, AbstractObject object)
    {
        if(!register.containsKey(category))
        {
            register.put(category, new ArrayList<>());
        }
        if(!register.get(category).contains(object))
        {
            register.get(category).add(object);
        }
    }

    public ArrayList<AbstractObject> GetRegisteredObjects(String category)
    {
        return register.get(category);
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    @Override
    public void run()
    {
        super.run();
    }

    @Override
    abstract public void Tick();
}
