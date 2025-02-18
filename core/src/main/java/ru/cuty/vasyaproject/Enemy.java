package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.SCR_HEIGHT;
import static ru.cuty.vasyaproject.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class Enemy extends SpaceObject{
    public int type;
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 50;

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

    private void changePhase(){
        if(TimeUtils.millis()>timeLastPhase+timePhaseInterval) {
            if (++phase == nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }
}
