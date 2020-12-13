package agh.cs.po;

import java.util.HashMap;

public class CollisionComponent extends Component{
    private HashMap<ColliderType, CollisionType> collisionMap;
    private ColliderType colliderType;

    public CollisionComponent(ColliderType collider)
    {
        super();
        this.colliderType = collider;
    }

    private void InitializeCollisionMap()
    {
        for(ColliderType type : ColliderType.values())
        {
            collisionMap.put(type, CollisionType.NONE);
        }
    }

    public void SetCollisionWith(ColliderType with, CollisionType collisionType)
    {
        if(collisionMap.containsKey(with))
        {
            collisionMap.replace(with, collisionType);
        }
        else
        {
            collisionMap.put(with, collisionType);
        }
    }

    public ColliderType getColliderType()
    {
        return colliderType;
    }

    public void setColliderType(ColliderType newCollider)
    {
        colliderType = newCollider;
    }

    public CollisionType CheckCollisionWith(CollisionComponent other)
    {
        return collisionMap.get(other.getColliderType());
    }

    @Override
    public void Tick() {

    }

    @Override
    public void Destroy() {

    }
}
