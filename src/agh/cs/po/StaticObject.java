package agh.cs.po;

public abstract  class StaticObject extends MapObject{
    private Vector2d staticPosition;

    public StaticObject(Vector2d spawnPosition, Map assignedMap, int defaultLayer)
    {
        super(spawnPosition,assignedMap,defaultLayer);
        staticPosition = spawnPosition;
    }

    final public void FixPosition()
    {
        this.position = this.staticPosition;
    }

    @Override
    public void Tick()
    {
        FixPosition();
    }
}
