package agh.cs.po;

public abstract class Pawn extends DynamicObject{
    private Controller controller;
    public Pawn(Vector2d spawnPosition, Map assignedMap, int defaultLayer) {
        super(spawnPosition, assignedMap, defaultLayer);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController()
    {
        return this.controller;
    }

    public void Destroy()
    {
        super.Destroy();
        controller.Unpossess(this);
    }
}
