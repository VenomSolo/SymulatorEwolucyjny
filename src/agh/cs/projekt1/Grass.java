package agh.cs.projekt1;

import agh.cs.po.*;

public class Grass extends MapObject {
    static int energy = 5;

    public Grass(Scene scene, Vector2d spawnPosition, Map assignedMap, int defaultLayer, int energy)
    {
        super(scene, spawnPosition, assignedMap, defaultLayer);
        //this.energy = energy;
        this.getSpriteComponent().setColor("#32a836");
        getCollisionComponent().setColliderType(ColliderType.STATIC);
        getCollisionComponent().SetCollisionWith(ColliderType.DYNAMIC, CollisionType.OVERLAP);
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    protected void OnHit(CollisionInfo info) {

    }

    @Override
    protected void OnOverlap(CollisionInfo info) {
        if(info.getOther() instanceof Animal)
        {
            ((SimulationMap) getMap()).QueryToEat(this);
        }
    }

    @Override
    public void Tick() {

    }
}
