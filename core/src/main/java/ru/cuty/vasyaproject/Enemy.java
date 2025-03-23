package ru.cuty.vasyaproject;

import static com.badlogic.gdx.math.MathUtils.random;
import static ru.cuty.vasyaproject.Main.*;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Enemy extends SpaceObject {
    public int type;
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 50;
    public int hp;
    public int price;

    public Enemy() {
        // Выбираем сторону экрана для спавна
        int side = MathUtils.random(0, 3); // 0 - лево, 1 - верх, 2 - право, 3 - низ

        switch(side) {
            case 0: // Спавним слева
                x = -width; // За левой границей
                y = MathUtils.random(0, 900); // Случайная высота
                break;
            case 1: // Спавним сверху
                x = MathUtils.random(0, 1600); // Случайная ширина
                y = 900 + height; // За верхней границей
                break;
            case 2: // Спавним справа
                x = 1600 + width; // За правой границей
                y = MathUtils.random(0, 900); // Случайная высота
                break;
            case 3: // Спавним снизу
                x = MathUtils.random(0, 1600); // Случайная ширина
                y = -height; // За нижней границей
                break;
        }

        width = height = 150;
        hp = 2;
        price = 2;
    }

    public void move(Ship ship) {
        super.move();
        changePhase();

        Vector2 direction = new Vector2(ship.x - x, ship.y - y);

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
