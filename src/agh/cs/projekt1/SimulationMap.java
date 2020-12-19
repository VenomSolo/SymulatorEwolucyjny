package agh.cs.projekt1;

import agh.cs.po.*;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Stream;

public class SimulationMap extends Map {
    Grid grid;
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
        this.eatingQuery = new ArrayList<>();
        this.matingQuery = new ArrayList<>();

        Vector2d tempVector;
        for(int i = jungleLBound.x; i <= jungleHBound.x; i++)
        {
            for(int j = jungleLBound.y; j < jungleHBound.y; j++)
            {
                tempVector = new Vector2d(i,j);
                AddObject(new Grass(scene, tempVector, this, -1, 1));
            }
        }
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
            int energy = Grass.energy;
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

    public synchronized void UpdateGrid()
    {
        grid.ClearGrid();
        for (Vector2d key : objects.keySet())
        {
            //if(ObjectAtTop(key) != null)
            grid.UpdateGrid(key, ObjectAtTop(key).getSpriteComponent().getColor());
        }
    }
}
