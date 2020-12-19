package agh.cs.po;

public abstract class MapObject extends AbstractObject{
    protected SpriteComponent spriteComponent;
    protected CollisionComponent collisionComponent;
    protected Vector2d position;
    private Map map;
    protected int layer;


    public MapObject(Scene scene, Vector2d spawnPosition, Map assignedMap, int defaultLayer)
    {
        super(scene);
        this.map = assignedMap;
        if(map.BoundCheck(spawnPosition)) this.position = spawnPosition;
        else this.position = new Vector2d(0,0);
        this.layer = defaultLayer;
        this.spriteComponent = new SpriteComponent(scene,null);
        this.collisionComponent = new CollisionComponent(scene, ColliderType.NOTHING);
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

    protected abstract void OnHit(CollisionInfo info);
    protected abstract void OnOverlap(CollisionInfo info);

    @Override
    public void Destroy()
    {
        map.RemoveObject(this);
    }


}
