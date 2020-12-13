package agh.cs.po;

import agh.cs.projekt1.Animal;
import agh.cs.projekt1.Grass;

import java.util.*;

public class Map {
    private final Vector2d lBound = new Vector2d(0,0);
    private Vector2d hBound;
    private Vector2d jungleLBound;
    private Vector2d jungleHBound;
    private boolean foldable;
    private HashMap<Vector2d, SortedSet<Animal>> animals;
    private HashMap<Vector2d, Grass> grass;
    private HashMap<Vector2d, TreeSet<MapObject>> objects;

    public Map(Vector2d bounds, float jungleRatio, boolean foldable)
    {
        this.hBound = bounds;
        this.jungleLBound = bounds.scale(jungleRatio);
        this.jungleHBound = bounds.scale(1.0f-jungleRatio);
        this.foldable = foldable;
    }

    public boolean AddObject(MapObject object)
    {
        return AddObjectAt(object, object.getPosition());
    }

    public boolean AddObjectAt(MapObject object, Vector2d position)
    {
        if (object == null) return false;
        if (!objects.containsKey(position)) {
            objects.put(position, new TreeSet<MapObject>(Comparator.comparingInt(MapObject::getLayer)));
        }
        objects.get(position).add(object);
        return true;
    }

    public boolean RemoveObject(MapObject object)
    {
        if (object == null) return false;
        Vector2d position = object.getPosition();
        if (!objects.containsKey(position) && object.getMap() == this) {
            objects.get(position).remove(object);
            if (objects.get(position).size() == 0){
                objects.remove(position,objects.get(position));
            }
        }
        objects.get(position).add(object);
        return true;
    }

    public boolean IsEmpty(Vector2d position)
    {
        return this.objects.get(position).isEmpty();
    }

    public TreeSet<MapObject> ObjectsAt(Vector2d position)
    {
        return this.objects.get(position);
    }

    public MapObject ObjectAtTop(Vector2d position)
    {
        return ObjectsAt(position).last();
    }

    public ArrayList<MapObject> GetAllWithTag(String searchTag)
    {
        ArrayList<MapObject> objects = new ArrayList<>();

        for(java.util.Map.Entry<Vector2d, TreeSet<MapObject>> entry : this.objects.entrySet())
        {
            for(MapObject obj : entry.getValue()) {
                if (obj.getTags().contains(searchTag)) {
                    objects.add(obj);
                }
            }
        }
        return objects;
    }

    public Vector2d UpdateObjectPosition(MapObject sender, Vector2d newPosition)
    {
        Vector2d currentPosition = sender.getPosition();
        if(foldable)
        {
            if(!BoundCheck(newPosition))
            {
                if(newPosition.x > hBound.x) newPosition = newPosition.add(new Vector2d(-hBound.x,0));
                else if(newPosition.x < lBound.x) newPosition = newPosition.add(new Vector2d(hBound.x,0));
                if(newPosition.y > hBound.y) newPosition = newPosition.add(new Vector2d(0,-hBound.y));
                else if(newPosition.y < lBound.y) newPosition = newPosition.add(new Vector2d(0,-hBound.y));
            }

        }
        else
        {
            if(!BoundCheck(newPosition))
            {
                return currentPosition;
            }
        }

        if(objects.containsKey(newPosition))
        {
            for(MapObject obj : ObjectsAt(newPosition))
            {
                switch (sender.getCollisionComponent().CheckCollisionWith(obj.getCollisionComponent()))
                {
                    case BLOCK ->
                            {
                                sender.OnHit(obj);
                                obj.OnHit(sender);
                                return currentPosition;
                            }
                    case OVERLAP ->
                            {
                                sender.OnOverlap(obj);
                                obj.OnOverlap(sender);
                            }
                }
            }
            objects.get(newPosition).add(sender);
        }
        AddObjectAt(sender, newPosition);
        RemoveObject(sender);
        return newPosition;
    }

    public boolean BoundCheck(Vector2d toCheck)
    {
        if (toCheck.upperRight(hBound) == hBound && toCheck.lowerLeft(lBound) == lBound)
        {
            return true;
        }
        return false;
    }
}
