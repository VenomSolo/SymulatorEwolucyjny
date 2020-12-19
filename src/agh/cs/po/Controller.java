package agh.cs.po;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Controller extends EtherObject{
    private ArrayList<Pawn> possessedPawns;

    public Controller(Scene scene)
    {
        super(scene);
        scene.Register("AnimalController", this);
        possessedPawns = new ArrayList<Pawn>();
    }

    public void Possess(Pawn pawn)
    {
        possessedPawns.add(pawn);
        pawn.setController(this);
        OnPossess(pawn);
    }

    public void Unpossess(Pawn pawn)
    {
        possessedPawns.remove(pawn);
        pawn.setController(null);
        OnUnpossess(pawn);
    }

    public abstract void Action(Pawn controlledPawn);
    public void ActionForAll()
    {
        for(Pawn pawn : possessedPawns)
        {
            Action(pawn);
        }
    }
    protected abstract void OnPossess(Pawn pawn);
    protected abstract void OnUnpossess(Pawn pawn);

    final void PerformAction()
    {
        for(Pawn pawn : possessedPawns)
        {
            Action(pawn);
        }
    }
}
