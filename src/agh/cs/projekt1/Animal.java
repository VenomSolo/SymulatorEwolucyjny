package agh.cs.projekt1;

import agh.cs.po.*;

import java.util.ArrayList;
import java.util.Random;

public class Animal extends Pawn {
    private int energy;
    private static int id = 0;
    private SimulationScene castScene;
    public int lifetime = 0;
    public int kids = 0;

    public Animal(Scene scene, Vector2d spawnPosition, Map assignedMap, int defaultLayer, Genom newGenom)
    {
        super(scene, spawnPosition,assignedMap,defaultLayer);
        if (castScene == null) castScene = (SimulationScene) scene;
        energy = castScene.startEnergy;
        newGenom.Possess(this);
        ToggleTick();
        this.setName("Animal" + id++);
        this.AddTag("Animal");
        ChangeColor("#f54b42");
        getCollisionComponent().setColliderType(ColliderType.DYNAMIC);
        getCollisionComponent().SetCollisionWith(ColliderType.STATIC, CollisionType.OVERLAP);
        getCollisionComponent().SetCollisionWith(ColliderType.DYNAMIC, CollisionType.OVERLAP);
    }

    public Animal(Scene scene, Vector2d spawnPosition, Map assignedMap, int defaultLayer, Genom newGenom, int newEnergy)
    {
        this(scene, spawnPosition, assignedMap, defaultLayer, newGenom);
        this.energy = newEnergy;
    }

    public int getEnergy() {
        return energy;
    }

    public void Eat(int energy)
    {
        this.energy+=energy;
    }

    public void Copulate(Animal other)
    {
        this.kids++;
        other.kids++;
        this.ChangeEnergy(-this.energy/4);
        other.ChangeEnergy(-other.energy/4);
        Vector2d childPosition = new Vector2d(0,0);
        ArrayList<Vector2d> neighbours = ((SimulationMap)getMap()).GetEmptyNeighbours(this.position);
        if(neighbours.size() == 0)
        {
            childPosition = Vector2d.RandomVectorInBounds(this.position.add(new Vector2d(-1,-1)), this.position.add(new Vector2d(1,1)));
        }
        else
        {
            childPosition = neighbours.get(new Random().nextInt(neighbours.size()));
        }
        Animal child = new Animal(scene, childPosition, getMap(),
                this.energy/4+other.energy/4, ((Genom)getController()).CombineGenes((Genom)other.getController()),this.energy/4+other.energy/4);
        child.setOrientation(MapDirection.values()[new Random().nextInt(MapDirection.values().length)]);
        getMap().AddObject(child);
    }

    @Override
    protected void OnHit(CollisionInfo info) {

    }

    @Override
    protected void OnOverlap(CollisionInfo info) {
        if(info.getOther() instanceof Animal)
        {
            if(this.energy+1>castScene.startEnergy && ((Animal)info.getOther()).energy+1>castScene.startEnergy)
                ((SimulationMap)getMap()).QueryToMate(info.getPosition());
        }

    }

    private void ChangeColor(String color)
    {
        getSpriteComponent().setColor(color);
    }

    private void ChangeEnergy(int diff)
    {
        energy += diff;
        getMap().RemoveObject(this);
        layer = energy;
        getMap().AddObject(this);
    }

    @Override
    public void OnMove(Vector2d diff)
    {

        //System.out.println(energy);

        if(energy<=0)
        {
            ChangeColor("#000000");
            this.AddTag("dead");
            TurnOffTick();
        }
        else if (energy<castScene.startEnergy*0.25)
        {
            ChangeColor("#4934eb");
        }
        else if(energy<castScene.startEnergy*0.5)
        {
            ChangeColor("#e234eb");
        }
        else
        {
            ChangeColor("#f54b42");
        }
    }

    @Override
    protected void OnRotate(int degree) {

    }

    @Override
    protected void BeforeMove(Vector2d diff) {
        ChangeEnergy(-SimulationScene.moveEnergy);
    }

    @Override
    protected void BeforeRotate(int degree) {

    }

    @Override
    public void Tick() {
        if(TickAllowed())
        {
            lifetime++;
        }
    }

    @Override
    public void Destroy() {
        super.Destroy();
    }
}
