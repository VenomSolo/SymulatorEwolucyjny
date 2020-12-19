package agh.cs.projekt1;

import agh.cs.po.*;

public class Animal extends Pawn {
    private int energy;
    private static int id = 0;
    private static SimulationScene castScene;

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

    public int getEnergy() {
        return energy;
    }

    public void Eat(int energy)
    {
        this.energy+=energy;
    }

    public void Copulate(Animal other)
    {
        this.ChangeEnergy(-this.energy/4);
        other.ChangeEnergy(-other.energy/4);
        getMap().AddObject(new Animal(scene, this.position.add(new Vector2d(0,1)), getMap(),
                this.energy/4+other.energy/4, ((Genom)getController()).CombineGenes((Genom)other.getController())));
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
        ChangeEnergy(-1);
    }

    @Override
    protected void BeforeRotate(int degree) {

    }

    @Override
    public void Tick() {
        if(TickAllowed())
        {
            if(getController() == null);
        }
    }

    @Override
    public void Destroy() {
        super.Destroy();
    }
}
