package agh.cs.projekt1;

import agh.cs.po.*;

public class Animal extends Pawn {
    protected int energy = 20;
    private static int id = 0;

    public Animal(Scene scene, Vector2d spawnPosition, Map assignedMap, int defaultLayer, Genom newGenom)
    {
        super(scene, spawnPosition,assignedMap,defaultLayer);
        newGenom.Possess(this);
        ToggleTick();
        this.setName("Animal" + id++);
        this.AddTag("Animal");
        getSpriteComponent().setColor("#f54b42");
        getCollisionComponent().setColliderType(ColliderType.DYNAMIC);
        getCollisionComponent().SetCollisionWith(ColliderType.STATIC, CollisionType.OVERLAP);
        //getCollisionComponent().SetCollisionWith(ColliderType.DYNAMIC, CollisionType.OVERLAP);
    }

    public void Eat(int energy)
    {
        this.energy+=energy;
    }

    public void Copulate()
    {

    }

    @Override
    protected void OnHit(MapObject other) {

    }

    @Override
    protected void OnOverlap(MapObject other) {

    }

    @Override
    public void OnMove(Vector2d diff)
    {
        energy--;
        //System.out.println(energy);
        if(energy<=0)
        {
            this.AddTag("dead");
            ToggleTick();
        }
    }

    @Override
    protected void OnRotate(int degree) {

    }

    @Override
    public void Tick() {
        if(TickAllowed())
        {
            this.setLayer(energy);
        }
    }

    @Override
    public void Destroy() {
        super.Destroy();
    }
}
