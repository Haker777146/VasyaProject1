package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.SCR_HEIGHT;
import static ru.cuty.vasyaproject.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Shot extends SpaceObject{

    float rotation,vRotation;
    public Shot(float x, float y, float vx, float vy)
    {
        super(x, y, vx, vy);
        width = 70;
        height = 70;
        vRotation = -10;
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
