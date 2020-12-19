package agh.cs.po;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Scene extends Thread implements ICanTick{
    private ArrayList<Map> maps;
    private HashMap<String, ArrayList<AbstractObject>> register;
    private boolean bAllowTick;

    public Scene(boolean bAllowTick)
    {
        register = new HashMap<>();
        maps = new ArrayList<>();
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

    public void Unregister(String category, AbstractObject object)
    {
        if(register.containsKey(category))
        {
            register.get(category).remove(object);
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
    public void Tick()
    {
        for(AbstractObject obj : GetRegisteredObjects("Tickable"))
        {
            //System.out.println(obj.getName());
            obj.Tick();
        }
    }

    @Override
    final public void ToggleTick()
    {
        bAllowTick = bAllowTick ? false : true;
    }
}
