package agh.cs.projekt1;

import agh.cs.po.*;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.transform.Rotate;
import sample.Main;

import java.util.ArrayList;

public class SimulationScene extends SingleScene {
    public int day = 0;
    private static int newID = 1;
    public int id;
    public static int startEnergy;
    public static int moveEnergy;
    public static int plantEnergy;

    public SimulationScene(boolean bAllowTick, Grid[] grids, Statistics[] stats, float jungleRatio) {
        super(bAllowTick);
        id = newID++;
        setInstance(this);
        int j = 0;
        for(Grid grid : grids)
        {
            AddMap(new SimulationMap(new Vector2d(grid.getN()-1,grid.getN()-1), jungleRatio, true, this, grid));
            ((SimulationMap)getMaps().get(j)).setStats(stats[j]);
            j++;
        }
        for(Map map : getMaps())
        {
            SimulationMap simMap = (SimulationMap) map;
            for(int i = 0; i < 10; i++)
            {
                map.AddObject(new Animal(this, new Vector2d(6,6),
                        map, 1, Genom.BuildGenom(this, 10)));
            }
        }
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
            if((Main.paused1.get() && this.id == 1) || Main.paused2.get() && this.id == 2)
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
                this.sleep(100);
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
        day++;
        ClearCorpses();
        RotateAndMove();
        Feed();
        Mate();
        SpawnGrass();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                synchronized (this)
                {
                    for(Map map : getMaps())
                    {
                        ((SimulationMap) map).UpdateGrid();
                    }
                }
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
        ArrayList<AbstractObject> controllers = GetRegisteredObjects("Genom");
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

    public void Highlight()
    {
        for(Map map : getMaps())
        {
            SimulationMap simMap = ((SimulationMap)map);
            simMap.getGrid().Highlight(simMap.stats.dominatingGenotype);
        }
    }
}
