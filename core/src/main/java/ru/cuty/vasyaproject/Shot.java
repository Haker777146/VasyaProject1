package ru.cuty.vasyaproject;

public class Shot extends SpaceObject{

    public Shot(float x, float y) {
        super(x, y);
        width = 50;
        height = 150;
        vy = 20f;
    }
}
