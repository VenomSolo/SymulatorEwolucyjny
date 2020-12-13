package agh.cs.projekt1;

import agh.cs.po.*;

public class Animal extends Pawn {
    protected int energy;

    public Animal(Vector2d spawnPosition, Map assignedMap, int defaultLayer, Genom newGenom)
    {
        super(spawnPosition,assignedMap,defaultLayer);
        newGenom.Possess(this);

    }

    public void Eat()
    {

    }

    public void Copulate()
    {

    }

    @Override
    protected void OnHit(MapObject other) {

    }

    @Override
    protected void OnOverlap(MapObject other) {
        if(other instanceof Grass)
        {
            //this.energy +=
        }
    }

    @Override
    public void Tick() {
        if(TickAllowed())
        {
            energy--;
            if(energy<=0)
            {
                this.AddTag("dead");
                ToggleTick();
                this.Destroy();
            }
        }
    }

    @Override
    public void Destroy() {
        super.Destroy();
    }
}
