package agh.cs.po;

import java.util.Vector;

public enum MapDirection {
    NORTH, NE, EAST, SE, SOUTH, SW, WEST, NW;

    private final static MapDirection[] vals = values();
    @Override
    public String toString() {
        String s = super.toString();
        switch (this){
            case NORTH: s = "Północ"; break;
            case SOUTH: s = "Południe"; break;
            case EAST: s = "Wschód"; break;
            case WEST: s = "Zachód"; break;
            default:
                s = "inne";
        }
        return s;
    }

    public MapDirection next()
    {
        return vals[ordinal() == 3 ? 0 : (ordinal()+1)];
    }

    public MapDirection previous()
    {
        return vals[ordinal() == 0 ? 3 : (ordinal()-1)];
    }

    public Vector2d toUnitVector()
    {
        Vector2d v;
        switch (this){
            case NORTH: v = new Vector2d(0,1); break;
            case SOUTH: v = new Vector2d(0,-1); break;
            case EAST: v = new Vector2d(1,0); break;
            case WEST: v = new Vector2d(-1,0); break;
            case NE: v = new Vector2d(1,1); break;
            case NW: v = new Vector2d(-1,1); break;
            case SE: v = new Vector2d(1,-1); break;
            case SW: v = new Vector2d(-1,-1); break;
            default: v = new Vector2d(0,0);
        }
        return v;
    }
}
