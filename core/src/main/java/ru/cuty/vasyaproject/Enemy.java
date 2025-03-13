package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.SCR_HEIGHT;
import static ru.cuty.vasyaproject.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Enemy extends SpaceObject{
    public int type;
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 50;
    public int hp;
    public int price;

    public Enemy() {
        width = height = 200;
        type = MathUtils.random(0, 3);
        x = MathUtils.random(width/2, SCR_WIDTH-width/2);
        y = MathUtils.random(SCR_HEIGHT+height, SCR_HEIGHT*2);
        vy = MathUtils.random(-8f, -4f);
    }

    @Override
    public void move() {
        super.move();
        changePhase();
    }

    private void changePhase()
    {
        if(TimeUtils.millis()>timeLastPhase+timePhaseInterval)
        {
            if (++phase == nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }
    public boolean outOfScreen()
    {
        return y<-height/2;
    }

    private void setupByType(){
        switch (phase){
            case 0:
                width = height = 200;
                vy = MathUtils.random(-7f, -5f);
                hp = 2;
                price = 2;
                break;
            case 1:
                width = height = 300;
                vy = MathUtils.random(-5f, -4f);
                hp = 6;
                price = 5;
                break;
            case 2:
                width = height = 250;
                vy = MathUtils.random(-6f, -5f);
                hp = 4;
                price = 3;
                break;
            case 3:
                width = height = 150;
                vy = MathUtils.random(-9f, -7f);
                hp = 1;
                price = 2;
                break;
        }
    }
}
