package ru.cuty.vasyaproject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Enemy extends SpaceObject {
    public int type;
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 50;
    public int hp;
    public int price;
    public float rotation = 0;

    public Enemy() {
        width = height = 200;
        type = MathUtils.random(0, 3);
        x = MathUtils.random(-200, 1800);
        y = MathUtils.random(-200, 1800);
        width = height = 150;
        hp = 3;
        price = 2;
    }

    public void move(Ship ship) {
        super.move();
        changePhase();


        Vector2 direction = new Vector2(ship.x - x, ship.y - y);


        float playerVelocityX = ship.x;


        if (playerVelocityX >= x)
        {
            rotation = 0;
        } else {
            rotation = 180;
        }

        float speed = 2.5f;
        vx = speed * MathUtils.cosDeg(direction.angle());
        vy = speed * MathUtils.sinDeg(direction.angle());
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
