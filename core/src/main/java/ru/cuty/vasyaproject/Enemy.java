package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.SCR_HEIGHT;
import static ru.cuty.vasyaproject.Main.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Enemy extends SpaceObject {
    public int type;
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 50;
    public int hp;
    public int price;
    public float rotation;

    public Enemy() {
        width = height = 200;
        type = MathUtils.random(0, 3);
        x = MathUtils.random(-200, 1800);
        y = MathUtils.random(-200, 1800);
        width = height = 150;
        hp = 3;
        price = 2;
    }

    public void move(Ship ship)
    {
        super.move();
        changePhase();
        Vector2 direction = new Vector2(ship.x-x, ship.y-y);
        rotation = 0;
        if (true) {
            rotation = direction.angleDeg() + 90;
        } else {
            rotation = direction.angleDeg() - 90;
        }
        float v = 2.5f;
        vx = v*MathUtils.cos((direction.angleRad()));
        vy = v*MathUtils.sin((direction.angleRad()));
    }

    private void changePhase() {
        if (TimeUtils.millis() > timeLastPhase + timePhaseInterval) {
            if (++phase == nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }

    public boolean outOfScreen() {
        return y < -height / 2;
    }
}
