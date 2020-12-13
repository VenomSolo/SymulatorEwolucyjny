package agh.cs.projekt1;

import agh.cs.po.Map;
import agh.cs.po.MapObject;
import agh.cs.po.Vector2d;

public class Grass extends MapObject {
    private int energy;

    public Grass(Vector2d spawnPosition, Map assignedMap, int defaultLayer, int energy)
    {
        super(spawnPosition, assignedMap, defaultLayer);
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    protected void OnHit(MapObject other) {

    }

    @Override
    protected void OnOverlap(MapObject other) {

    }

    @Override
    public void Tick() {

    }
}
