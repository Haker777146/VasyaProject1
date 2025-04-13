package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.SCR_HEIGHT;
import static ru.cuty.vasyaproject.Main.SCR_WIDTH;

public class Shot extends SpaceObject{

    float rotation,vRotation;
    public Shot(float x, float y, float vx, float vy, int type)
    {
        super(x, y, vx, vy, type);
        vRotation = -10;
        switch (type)
        {
            case 0:
                width = height = 70;
                break;
            case 1:
                width = height = 80;
                break;
        }
    }
    @Override
    public void move()
    {
        super.move();

        if (vx > 0) {
            vRotation = -Math.abs(vRotation); // Отрицательное вращение (против часовой стрелки)
        } else if (vx < 0) {
            vRotation = Math.abs(vRotation); // Положительное вращение (по часовой стрелке)
        }
        rotation += vRotation; // Обновляем угол вращения
    }
    public boolean outOfScreen(){
        return y<-height/2 || y>SCR_HEIGHT+height/2 || x<-width/2 || x>SCR_WIDTH+width/2;
    }
}
