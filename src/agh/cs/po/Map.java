package agh.cs.po;

import agh.cs.projekt1.Animal;
import agh.cs.projekt1.Grass;

import java.util.Collections;
import java.util.HashMap;
import java.util.*;

public class Map {
    private final Vector2d lBound = new Vector2d(0,0);
    private Vector2d hBound;
    private Scene scene;
    private boolean foldable;
    protected HashMap<Vector2d, TreeSet<MapObject>> objects;

    public Map(Vector2d bounds, boolean foldable, Scene scene)
    {
        this.hBound = bounds;
        this.foldable = foldable;
        this.scene = scene;
        this.objects = new HashMap<>();
    }

    public boolean AddObject(MapObject object)
    {
        return AddObjectAt(object, object.getPosition());
    }

    private boolean AddObjectAt(MapObject object, Vector2d position)
    {
        if (object == null) return false;
        object.scene =  this.scene;
        if (!objects.containsKey(position)) {
            objects.put(position, new TreeSet<MapObject>(
                    new Comparator<MapObject>() {
                        @Override
                        public int compare(MapObject o1, MapObject o2) {
                            if(o1.equals(o2)) return 0;
                            if(o1.getLayer() < o2.getLayer()) return -1;
                            else return 1;
                        }
                    })); //Comparator.comparingInt(MapObject::getLayer)));
        }
        objects.get(position).add(object);
        return true;
    }

    public boolean RemoveObject(MapObject object)
    {
        if (object == null) return false;
        Vector2d position = object.getPosition();
        if (objects.containsKey(position) && object.getMap() == this) {
            objects.get(position).remove(object);
            if (objects.get(position).size() == 0){
                objects.remove(position,objects.get(position));
            }
        }
        else return false;
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

    public Vector2d UpdateObjectPosition(DynamicObject sender, Vector2d newPosition)
    {
        Vector2d currentPosition = sender.getPosition();
        if(foldable)
        {
            if(!BoundCheck(newPosition))
            {
                if(newPosition.x > hBound.x) newPosition = new Vector2d(lBound.x + newPosition.x % hBound.x - 1, newPosition.y); //newPosition.add(new Vector2d(-hBound.x,0));
                else if(newPosition.x < lBound.x) newPosition = new Vector2d(hBound.x - (-newPosition.x) % hBound.x + 1, newPosition.y); //newPosition.add(new Vector2d(hBound.x,0));
                if(newPosition.y > hBound.y) newPosition = new Vector2d(newPosition.x, lBound.y + newPosition.y % hBound.y - 1); //newPosition.add(new Vector2d(0,-hBound.y));
                else if(newPosition.y < lBound.y) newPosition = new Vector2d(newPosition.x, hBound.y - (-newPosition.y) % hBound.y + 1); //newPosition.add(new Vector2d(0,-hBound.y));
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
                                if (obj.getCollisionComponent().CheckCollisionWith(sender.getCollisionComponent())
                                        == CollisionType.BLOCK) obj.OnHit(sender);
                                return currentPosition;
                            }
                    case OVERLAP ->
                            {
                                sender.OnOverlap(obj);
                                if (obj.getCollisionComponent().CheckCollisionWith(sender.getCollisionComponent())
                                        == CollisionType.OVERLAP) obj.OnOverlap(sender);
                            }
                }
            }
            //objects.get(newPosition).add(sender);
        }
        RemoveObject(sender);
        sender.setPosition(newPosition);
        AddObject(sender);
        return newPosition;
    }

    public boolean BoundCheck(Vector2d toCheck)
    {
        if (toCheck.upperRight(hBound).equals(hBound) && toCheck.lowerLeft(lBound).equals(lBound))
        {
            return true;
        }
        return false;
    }
}
