package agh.cs.po;

import agh.cs.projekt1.Animal;
import agh.cs.projekt1.Grass;

import java.util.Collections;
import java.util.HashMap;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Map {
    public final Vector2d lBound = new Vector2d(0,0);
    public final Vector2d hBound;
    private final Scene scene;
    private boolean foldable;
    protected ConcurrentHashMap<Vector2d, HashMap<Integer, ArrayList<MapObject>>> objects;

    public Map(Vector2d bounds, boolean foldable, Scene scene)
    {
        this.hBound = bounds;
        this.foldable = foldable;
        this.scene = scene;
        this.objects = new ConcurrentHashMap<Vector2d, HashMap<Integer, ArrayList<MapObject>>>();
    }

    public Scene getScene()
    {
        return scene;
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
            objects.put(position, new HashMap<>());
        }
        if(!objects.get(position).containsKey(object.getLayer()))
        {
            objects.get(position).put(object.getLayer(), new ArrayList());
        }
        objects.get(position).get(object.getLayer()).add(object);
        return true;
    }

    public boolean RemoveObject(MapObject object)
    {
        if (object == null) return false;
        Vector2d position = object.getPosition();
        if (objects.containsKey(position) && object.getMap().equals(this))
        {
            var posMap = objects.get(position);
            if(posMap.containsKey(object.layer))
            {
                var layerMap = posMap.get(object.layer);
                if(layerMap.contains(object))
                {
                    layerMap.remove(object);
                    if (layerMap.size() == 0){
                        posMap.remove(object.layer);
                        if(posMap.keySet().size() == 0)
                        {
                            objects.remove(position);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean IsEmpty(Vector2d position)
    {
        return this.objects.containsKey(position);
    }

    public ArrayList<MapObject> ObjectsAt(Vector2d position)
    {
        ArrayList ret = new ArrayList();
        for(ArrayList list : this.objects.get(position).values())
        {
            for(Object object : list)
            {
                ret.add(object);
            }
        }
        return ret;
    }

    public MapObject ObjectAtTop(Vector2d position)
    {
        ArrayList<MapObject> list = ObjectsAt(position);
        list.sort(Comparator.comparingInt(MapObject::getLayer));
        if(list.size() == 0) return null;
        return (MapObject) list.get(list.size()-1);
    }

    public ArrayList<Vector2d> GetNeighbours(Vector2d position)
    {
        ArrayList ret = new ArrayList();
        for(MapDirection dir : MapDirection.values())
        {
            Vector2d candidate = position.add(dir.toUnitVector());
            ret.add(candidate);
        }
        return ret;
    }

    public ArrayList<MapObject> GetAllWithTag(String searchTag)
    {
        ArrayList<MapObject> ret = new ArrayList<>();
        for(HashMap<Integer, ArrayList<MapObject>> map : objects.values())
        {
            for(ArrayList<MapObject> list : map.values())
            {
                for(MapObject object : list)
                {
                    if(object.getTags().contains(searchTag)) ret.add(object);
                }
            }
        }
        return ret;
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
                                sender.OnHit(new CollisionInfo(obj, newPosition));
                                if (obj.getCollisionComponent().CheckCollisionWith(sender.getCollisionComponent())
                                        == CollisionType.BLOCK) obj.OnHit(new CollisionInfo(sender, newPosition));
                                return currentPosition;
                            }
                    case OVERLAP ->
                            {
                                sender.OnOverlap(new CollisionInfo(obj, newPosition));
                                if (obj.getCollisionComponent().CheckCollisionWith(sender.getCollisionComponent())
                                        == CollisionType.OVERLAP) obj.OnOverlap(new CollisionInfo(sender, newPosition));
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
