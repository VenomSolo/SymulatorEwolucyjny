package agh.cs.po;

import java.util.ArrayList;

public abstract class MapObject extends AbstractObject{
    protected SpriteComponent spriteComponent;
    protected CollisionComponent collisionComponent;
    protected Vector2d position;
    private Map map;
    private int layer;


    public MapObject(Vector2d spawnPosition, Map assignedMap, int defaultLayer)
    {
        this.map = assignedMap;
        if(map.BoundCheck(spawnPosition)) this.position = spawnPosition;
        else this.position = new Vector2d(0,0);
        this.layer = defaultLayer;
        this.spriteComponent = new SpriteComponent(null);
        this.collisionComponent = new CollisionComponent(ColliderType.NOTHING);
    }

    final public Vector2d getPosition() {
        return position;
    }

    final public int getLayer(){
        return layer;
    }

    final public Map getMap() {
        return map;
    }

    final public SpriteComponent getSpriteComponent()
    {
        return spriteComponent;
    }

    final public CollisionComponent getCollisionComponent() {
        return collisionComponent;
    }

    protected abstract void OnHit(MapObject other);
    protected abstract void OnOverlap(MapObject other);

    @Override
    public void Destroy()
    {
        map.RemoveObject(this);
    }


}