package agh.cs.projekt1;

import agh.cs.po.*;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.transform.Rotate;
import sample.Main;

import java.util.ArrayList;

public class SimulationScene extends SingleScene {
    public Statistics stats;
    public int startEnergy = 20;
    
    public SimulationScene(boolean bAllowTick, Grid[] grids) {
        super(bAllowTick);
        setInstance(this);
        for(Grid grid : grids)
        {
            AddMap(new SimulationMap(new Vector2d(grid.getN()-1,grid.getN()-1), 0.7f, true, this, grid));
        }
        for(Map map : getMaps())
        {
            SimulationMap simMap = (SimulationMap) map;
            for(int i = 0; i < 50; i++)
            {
                map.AddObject(new Animal(this, new Vector2d(9,9),
                        map, 1, new Genom(this, 32)));
            }
        }

/*
        Animal a1 = new Animal(this, new Vector2d(8,8),
                map1, 1, new Genom(this, new int[]{0,1,1,1,1,1,1,1,1,2,3,4,5,6,6,7}));
        map1.AddObject(a1);

        map1.AddObject(new Animal(this, new Vector2d(9,9),
                map1, 1, new Genom(this, new int[]{0,1,1,1,1,1,1,1,1,2,3,4,5,6,7,7})));
*/

    }

    public static SimulationScene getInstance()
    {
        return (SimulationScene) SingleScene.getInstance();
    }

    @Override
    public void run() {
        super.run();
        while(true)
        {
            if(Main.paused.get())
            {
                synchronized(this)
                {
                    // Pause
                    try
                    {
                        this.wait();
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
            }
            try
            {
                Tick();
                this.sleep(17);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // code for stopping current task so thread stops
            }

        }
    }

    @Override
    public void Tick() {
        super.Tick();
        ClearCorpses();
        RotateAndMove();
        Feed();
        Mate();
        SpawnGrass();
        stats.setI(stats.getI()+1);
        System.out.println(stats.getI() + " Tick");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                SimulationMap map1 = (SimulationMap) getMaps().get(0);
                map1.UpdateGrid();
            }
        });
    }

    public void ClearCorpses() {
        for(Map map : getMaps())
        {
            System.out.println("Animals: " + map.GetAllWithTag("Animal").size());
            ArrayList<MapObject> dead = map.GetAllWithTag("dead");
            for (MapObject obj : dead)
            {
                obj.Destroy();
            }
        }
    }

    public void RotateAndMove() {
        ArrayList<AbstractObject> controllers = GetRegisteredObjects("AnimalController");
        System.out.println("Controllers: " + controllers.size());
        for(AbstractObject controller : controllers)
        {
            Genom animalController = (Genom)controller;
            animalController.ActionForAll();
        }
    }

    public void Feed()
    {
        for(Map map : getMaps())
        {
            ((SimulationMap)map).ExecuteEatingQuery();
        }
    }

    public void Mate()
    {
        for(Map map : getMaps())
        {
            ((SimulationMap)map).ExecuteMatingQuery();
        }
    }

    public void SpawnGrass()
    {
        for(Map map : getMaps())
        {
            SimulationMap simMap = (SimulationMap)map;
            simMap.AddObject(new Grass(this, Vector2d.RandomVectorInBounds(
                    simMap.jungleLBound, simMap.jungleHBound), simMap, -1, 1));
            simMap.AddObject(new Grass(this, Vector2d.RandomVectorInBounds(
                    simMap.lBound, simMap.hBound), simMap, -1, 1));
        }
    }
}
