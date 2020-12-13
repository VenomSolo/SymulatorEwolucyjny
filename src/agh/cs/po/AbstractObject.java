package agh.cs.po;

import java.util.ArrayList;

public abstract class AbstractObject implements ICanTick{
    private Scene scene;
    private ArrayList<String> tags;
    private String name;
    private boolean bAllowTick = false;

    final public void AddTag(String tag)
    {
        tags.add(tag);
    }

    final public void RemoveTag(String tag)
    {
        if(tags.contains(tag)) tags.remove(tag);
    }

    final public ArrayList<String> getTags() {
        return tags;
    }

    final public boolean TickAllowed()
    {
        return bAllowTick;
    }

    final protected void ToggleTick()
    {
        bAllowTick = bAllowTick ? false : true;
    }

    final protected void setName(String name)
    {
        this.name = name;
    }

    final public String getName()
    {
        return this.name;
    }

    public abstract void Tick();

    public abstract void Destroy();
}
