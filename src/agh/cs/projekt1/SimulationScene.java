package agh.cs.projekt1;

import agh.cs.po.*;
import agh.cs.po.Map;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.transform.Rotate;
import sample.Main;

import java.util.*;

public class SimulationScene extends Scene {
    public int day = 0;
    private static int newID = 1;
    public int id;
    public static int startEnergy;
    public static int moveEnergy;
    public static int plantEnergy;
    private SimulationScene self;
    public HashMap<String, Genom> existingGenoms = new HashMap<>();

    public SimulationScene(boolean bAllowTick, Grid[] grids, Statistics[] stats, float jungleRatio) {
        super(bAllowTick);
        id = newID++;
        this.self = this;
        int j = 0;
        for(Grid grid : grids)
        {
            AddMap(new SimulationMap(new Vector2d(grid.getN()-1,grid.getN()-1), jungleRatio, true, this, grid));
            ((SimulationMap)getMaps().get(j)).setStats(stats[j]);
            j++;
        }

        Map testMap = getMaps().get(0);
        List<Vector2d> candidatesList = new ArrayList<Vector2d>();
        for(int i = testMap.lBound.x; i <= testMap.hBound.x; i++)
        {
            for(int h = testMap.lBound.y; h <= testMap.hBound.y; h++)
            {
                Vector2d tempVector = new Vector2d(i,h);
                candidatesList.add(tempVector);
            }
        }
        Collections.shuffle(candidatesList);
        ArrayDeque<Vector2d> candidates = new ArrayDeque<>(candidatesList);
        for(Map map : getMaps())
        {
            SimulationMap simMap = (SimulationMap) map;
            for(int i = 0; i < 100; i++)
            {
                Vector2d nearlyUniquePosition = candidates.poll();
                map.AddObject(new Animal(this, nearlyUniquePosition,
                        map, 1, Genom.BuildGenom(this, 32)));
                candidates.add(nearlyUniquePosition);
            }
        }
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
                        ((SimulationMap) map).UpdateGrid(self);
                    }
                }
            }
        });
    }

    public void ClearCorpses() {
        for(Map map : getMaps())
        {
            ArrayList<MapObject> dead = map.GetAllWithTag("dead");
            for (MapObject obj : dead)
            {
                obj.Destroy();
            }
        }
    }

    public void RotateAndMove() {
        ArrayList<AbstractObject> controllers = GetRegisteredObjects("Genom");
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
            simMap.SpawnGrassOutsideJungle();
            simMap.SpawnGrassInJungle();
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
