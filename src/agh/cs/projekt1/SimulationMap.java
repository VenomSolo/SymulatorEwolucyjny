package agh.cs.projekt1;

import agh.cs.po.*;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;

public class SimulationMap extends Map {
    Grid grid;
    Statistics stats;
    public Vector2d jungleLBound;
    public Vector2d jungleHBound;
    private ArrayList<Grass> eatingQuery;
    private ArrayList<Vector2d> matingQuery;


    public Grid getGrid() {
        return grid;
    }

    public SimulationMap(Vector2d bounds, float jungleRatio, boolean foldable, Scene scene, Grid grid) {
        super(bounds, foldable, scene);
        this.jungleLBound = new Vector2d((int)Math.ceil(bounds.x * ((1-jungleRatio)/2)), (int)Math.ceil(bounds.y * ((1-jungleRatio)/2)));
        this.jungleHBound = new Vector2d((int)Math.floor(bounds.x * (1 - ((1-jungleRatio)/2))), (int)Math.floor(bounds.y * (1 - ((1-jungleRatio)/2))) + 1);
        this.grid = grid;
        grid.setMap(this);
        this.eatingQuery = new ArrayList<>();
        this.matingQuery = new ArrayList<>();

        Vector2d tempVector;
        for(int i = jungleLBound.x; i <= jungleHBound.x; i++)
        {
            for(int j = jungleLBound.y; j < jungleHBound.y; j++)
            {
                tempVector = new Vector2d(i,j);
                AddObject(new Grass(scene, tempVector, this, -1));
            }
        }
    }

    public void setStats(Statistics stats)
    {
        this.stats = stats;
        stats.setMap(this);
    }

    private Vector2d RandomPosition()
    {
        return Vector2d.RandomVectorInBounds(lBound, hBound);
    }

    public void SpawnGrassInJungle()
    {
        ArrayList<Vector2d> candidates = new ArrayList<Vector2d>();
        for(int i = jungleLBound.x; i <= jungleHBound.x; i++)
        {
            for(int j = jungleLBound.y; j < jungleHBound.y; j++)
            {
                Vector2d tempVector = new Vector2d(i,j);
                if(IsEmpty(tempVector)) candidates.add(tempVector);
            }
        }
        if(candidates.size() == 0) {

            return;
        }
        else
        {
            AddObject(new Grass(getScene(), candidates.get(
                    Genom.rand.nextInt(candidates.size())), this, -1));
        }
    }

    public void SpawnGrassOutsideJungle()
    {
        ArrayList<Vector2d> candidates = new ArrayList<Vector2d>();
        for(int i = lBound.x; i <= hBound.x; i++)
        {
            for(int j = lBound.y; j <= hBound.y; j++)
            {
                Vector2d tempVector = new Vector2d(i,j);
                if(IsEmpty(tempVector) && !(tempVector.follows(jungleLBound)
                && tempVector.precedes(jungleHBound))) candidates.add(tempVector);
            }
        }
        if(candidates.size() == 0) return;
        else
        {
            AddObject(new Grass(getScene(), candidates.get
                    (Genom.rand.nextInt(candidates.size())), this, -1));
        }
    }



    public Vector2d RandomStartPosition()
    {
        Vector2d tempVector;
        do {
            tempVector = RandomPosition();
        } while (ObjectsAt(tempVector).size()>1);
        return tempVector;
    }

    public Vector2d RandomEmptyPosition()
    {
        Vector2d tempVector;
        do {
            tempVector = RandomPosition();
        } while (!IsEmpty(tempVector));
        return tempVector;
    }

    public ArrayList<Vector2d> GetEmptyNeighbours(Vector2d position)
    {
        ArrayList<Vector2d> ret = new ArrayList();
        for(Vector2d pos : GetNeighbours(position))
        {
            if(IsEmpty(pos)) ret.add(pos);
        }
        return ret;
    }

    public void QueryToEat(Grass toEat)
    {
        if(!eatingQuery.contains(toEat))eatingQuery.add(toEat);
    }

    public void QueryToMate(Vector2d toMate) {if(!matingQuery.contains(toMate)) matingQuery.add(toMate);}

    public void ExecuteEatingQuery()
    {
        for(Grass grass : eatingQuery)
        {
            Vector2d pos = grass.getPosition();
            int count = (int) ObjectsAt(pos).stream().filter(obj -> obj.getLayer() == ObjectAtTop(pos).getLayer()).count();
            int energy = grass.getEnergy();
            ObjectsAt(pos).stream().filter(obj -> obj.getLayer() == ObjectAtTop(pos).getLayer() && obj instanceof Animal)
                    .forEach(animal -> ((Animal) animal).Eat(energy));
            grass.Destroy();
        }
        eatingQuery.clear();
    }

    public void ExecuteMatingQuery()
    {
        for(Vector2d pos : matingQuery)
        {
            Animal a1, a2;
            if(ObjectsAt(pos).stream().filter(x -> x instanceof Animal && ((Animal)x).getEnergy()>
                    ((SimulationScene)x.scene).startEnergy*0.5).count() >= 2)
            {
                a1 = (Animal) ObjectAtTop(pos);
                RemoveObject(a1);
                a2 = (Animal) ObjectAtTop(pos);
                AddObject(a1);
                a1.Copulate(a2);
            }
        }
        matingQuery.clear();
    }

    public synchronized void UpdateGrid(SimulationScene scene)
    {
        synchronized (grid)
        {
            grid.ClearGrid();
            for (Vector2d key : objects.keySet())
            {
                //if(ObjectAtTop(key) != null)
                grid.UpdateGrid(key, ObjectAtTop(key).getSpriteComponent().getColor());
            }
        }
        synchronized (stats)
        {
            stats.UpdateChart(scene);
        }
    }
}
