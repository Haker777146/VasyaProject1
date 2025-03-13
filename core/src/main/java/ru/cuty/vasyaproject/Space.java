package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.*;

public class Space extends SpaceObject{
    public Space(float x, float y, float vx, float vy){
        super(x, y, vx, vy);
        width = SCR_WIDTH;
        height = SCR_HEIGHT+6;
    }

    @Override
    public void move() {
        super.move();
        outOfScreen();
    }

    private void outOfScreen(){
        if(y<-SCR_HEIGHT) y = SCR_HEIGHT;
    }
}
