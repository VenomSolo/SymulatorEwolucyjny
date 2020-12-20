package agh.cs.po;

import java.util.ArrayList;
import java.util.Objects;

public abstract class AbstractObject implements ICanTick{
    public Scene scene;
    private ArrayList<String> tags;
    private String name;
    private static int id = 0;
    private boolean bAllowTick = false;

    public AbstractObject(Scene scene)
    {
        this.name = "Object" + id++;
        this.tags = new ArrayList<>();
        this.scene = scene;
        scene.Register("Tickable", this);
    }

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

    final public void ToggleTick()
    {
        bAllowTick = bAllowTick ? false : true;
    }

    final public void TurnOffTick() {bAllowTick = false;}

    final public void TurnOnTick() {bAllowTick = true;}

    final protected void setName(String name)
    {
        this.name = name;
    }

    final public String getName()
    {
        return this.name;
    }

    public abstract void Tick();

    public void Destroy()
    {
        tags.clear();
    }

    @Override
    final public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractObject other = (AbstractObject) obj;
        return (this.name.equals(other.name));
    }

    @Override
    final public int hashCode()
    {
        return Objects.hash(name);
    }
}
