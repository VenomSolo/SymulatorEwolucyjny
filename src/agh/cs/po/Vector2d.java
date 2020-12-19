package agh.cs.po;

import java.util.Objects;
import java.util.Random;

import static java.lang.StrictMath.floor;

public class Vector2d{
    public final int x;
    public final int y;
    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString()
    {
        return "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
    }

    public boolean precedes(Vector2d other)
    {
        return(this.x <= other.x && this.y <= other.y);
    }

    public boolean follows(Vector2d other)
    {
        return(this.x >= other.x && this.y >= other.y);
    }

    public Vector2d upperRight(Vector2d other)
    {
        int x = Math.max(this.x, other.x);
        int y = Math.max(this.y, other.y);
        return new Vector2d(x,y);
    }

    public Vector2d lowerLeft(Vector2d other)
    {
        int x = Math.min(this.x, other.x);
        int y = Math.min(this.y, other.y);
        return new Vector2d(x,y);
    }

    public Vector2d add(Vector2d other)
    {
        return new Vector2d(this.x + other.x,this.y + other.y);
    }

    public Vector2d subtract(Vector2d other)
    {
        return new Vector2d(this.x - other.x,this.y - other.y);
    }

    public Vector2d scale(float scalar)
    {
        return new Vector2d((int)(floor(this.x*scalar)),(int)(floor(this.y*scalar)));
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector2d other = (Vector2d) obj;
        return (this.x == other.x && this.y == other.y);
    }

    public Vector2d opposite()
    {
        return new Vector2d(-this.x ,-this.y);
    }

    public static Vector2d RandomVectorInBounds(Vector2d lBound, Vector2d hBound)
    {
        Random rand = new Random();
        return new Vector2d(rand.nextInt(hBound.x-lBound.x+1)+lBound.x,
                rand.nextInt(hBound.y-lBound.y+1)+lBound.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}