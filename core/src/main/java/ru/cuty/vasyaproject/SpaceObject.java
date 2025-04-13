package ru.cuty.vasyaproject;

public class SpaceObject {
    public float x, y;
    public float width, height;
    public float vx, vy;
    public int type;

    public SpaceObject(float x, float y, float vx, float vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public SpaceObject() {
    }

    public void move(){
        x += vx;
        y += vy;
    }

    public float scrX(){
        return x-width/2;
    }

    public float scrY(){
        return y-height/2;
    }

    public boolean overlap(SpaceObject o)
    {
        return Math.abs(x - o.x) < width/3 + o.width/3 && Math.abs(y - o.y) < height/2.5f + o.height/2.5f;
    }
}
