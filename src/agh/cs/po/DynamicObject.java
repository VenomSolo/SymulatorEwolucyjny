package agh.cs.po;

public abstract class DynamicObject extends MapObject{
    protected MapDirection orientation;

    public DynamicObject(Scene scene, Vector2d spawnPosition, Map assignedMap, int defaultLayer)
    {
        super(scene, spawnPosition,assignedMap,defaultLayer);
        this.orientation = MapDirection.NORTH;
    }

    final public void setPosition(Vector2d newPosition)
    {
        if(getMap().BoundCheck(newPosition)) this.position = newPosition;
    }

    final public void setLayer(int newLayer)
    {
        this.layer = newLayer;
    }

    final public MapDirection getOrientation()
    {
        return orientation;
    }

    final public void setOrientation(MapDirection newOrientation)
    {
        this.orientation = newOrientation;
    }

    final public void Move(Vector2d diff)
    {
        this.position = getMap().UpdateObjectPosition(this, this.position.add(diff));
        this.OnMove(diff);
    }

    abstract protected void OnMove(Vector2d diff);

    abstract protected void OnRotate(int degree);

    final public void Rotate(int degree)
    {
        for (int i = 0; i < degree%(MapDirection.values().length); i++) {
            orientation = orientation.next();
        }
        this.OnRotate(degree);
    }
}
