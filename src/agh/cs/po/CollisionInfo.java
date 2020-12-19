package agh.cs.po;

public class CollisionInfo {
    final private MapObject other;
    final private Vector2d position;

    public CollisionInfo(MapObject other, Vector2d position)
    {
        this.other = other;
        this.position = position;
    }

    public MapObject getOther(){return other;}

    public Vector2d getPosition() {
        return position;
    }
}
