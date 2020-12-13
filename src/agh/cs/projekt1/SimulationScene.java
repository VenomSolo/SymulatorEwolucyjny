package agh.cs.projekt1;

import agh.cs.po.AbstractObject;
import agh.cs.po.Map;
import agh.cs.po.MapObject;
import agh.cs.po.Scene;
import javafx.application.Platform;
import javafx.scene.control.Label;
import sample.Main;

import java.util.ArrayList;

public class SimulationScene extends Scene {
    public Statistics stats;
    public Label lab;
    public SimulationScene(Map map, boolean bAllowTick) {
        super(map, bAllowTick);
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
        stats.setI(stats.getI()+1);
        System.out.println(stats.getI() + " Tick");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lab.setText(String.valueOf(stats.getI()));
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
}
