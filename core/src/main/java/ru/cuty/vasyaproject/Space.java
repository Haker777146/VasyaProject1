package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.*;

public class Space extends SpaceObject{
    public Space(float x, float y, float vx, float vy){
        super(x, y, vx, vy);
        width = SCR_WIDTH+5;
        height = SCR_HEIGHT+10;
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
