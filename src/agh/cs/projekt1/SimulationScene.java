package agh.cs.projekt1;

import agh.cs.po.*;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.transform.Rotate;
import sample.Main;

import java.util.ArrayList;

public class SimulationScene extends SingleScene {
    public Statistics stats;
    
    public SimulationScene(boolean bAllowTick, Grid[] grids) {
        super(bAllowTick);
        setInstance(this);
        AddMap(new SimulationMap(new Vector2d(19,19), 0.2f, true, this, grids[0]));
        SimulationMap map1 = (SimulationMap) getMaps().get(0);
        Animal a1 = new Animal(this, new Vector2d(8,8),
                map1, 1, new Genom(this, new int[]{0,1,1,1,1,1,1,1,1,2,3,4,5,6,6,7}));
        map1.AddObject(a1);

        map1.AddObject(new Animal(this, new Vector2d(9,9),
                map1, 1, new Genom(this, new int[]{0,1,1,1,1,1,1,1,1,2,3,4,5,6,7,7})));


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
                this.sleep(500);
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
            ArrayList<MapObject> dead = map.GetAllWithTag("dead");
            for (MapObject obj : dead)
            {
                obj.Destroy();
            }
        }
    }

    public void RotateAndMove() {
        ArrayList<AbstractObject> controllers = GetRegisteredObjects("AnimalController");
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
}
