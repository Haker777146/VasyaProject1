package ru.cuty.vasyaproject;

import static com.badlogic.gdx.math.MathUtils.random;
import static ru.cuty.vasyaproject.Main.*;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import com.badlogic.gdx.graphics.Color;

public class Enemy extends SpaceObject {
    public int type;
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 50;
    public int hp;
    public int price;
    public float speed;
    private Color color;

    public Color getColor()
    {
        return color;
    }

    public Enemy(int type)
    {
        // Выбираем сторону экрана для спавна
        int side = MathUtils.random(0, 3);
        this.type = type;
        if(difficulty_game == Normal)
        {
            switch (type)
            {
                case 0:
                    hp = 1;
                    price = 2;
                    speed = 4;
                    width = height = 100;
                    color = Color.RED;
                    break;
                case 1:
                    hp = 3;
                    price = 3;
                    speed = 2.5f;
                    width = height = 150;
                    color = Color.RED;
                    break;
                case 2:
                    hp = 5;
                    price = 5;
                    speed = 1.5f;
                    width = height = 200;
                    color = Color.RED;
                    break;
                case 3:
                    hp = 10;
                    price = 10;
                    speed = 2;
                    width = height = 300;
                    color = Color.RED;
                    break;
            }
        }
        if(difficulty_game == Hard)
        {
            switch (type)
            {
                case 0:
                    hp = 2;
                    price = 3;
                    speed = 5;
                    width = height = 90;
                    color = Color.RED;
                    break;
                case 1:
                    hp = 4;
                    price = 3;
                    speed = 4;
                    width = height = 150;
                    color = Color.RED;
                    break;
                case 2:
                    hp = 6;
                    price = 5;
                    speed = 2.5f;
                    width = height = 210;
                    color = Color.RED;
                    break;
                case 3:
                    hp = 13;
                    price = 8;
                    speed = 3;
                    width = height = 300;
                    color = Color.RED;
                    break;
            }
        }
        if(difficulty_game == Extreme)
        {
            switch (type)
            {
                case 0:
                    hp = 3;
                    price = 2;
                    speed = 6;
                    width = height = 80;
                    color = Color.RED;
                    break;
                case 1:
                    hp = 5;
                    price = 2;
                    speed = 5;
                    width = height = 150;
                    color = Color.RED;
                    break;
                case 2:
                    hp = 7;
                    price = 4;
                    speed = 4;
                    width = height = 220;
                    color = Color.RED;
                    break;
                case 3:
                    hp = 17;
                    price = 5;
                    speed = 5;
                    width = height = 300;
                    color = Color.RED;
                    break;
            }
        }

        switch(side)
        {
            case 0:
                x = -width;
                y = MathUtils.random(0, 900);
                break;
            case 1:
                x = MathUtils.random(0, 1600);
                y = 900 + height;
                break;
            case 2:
                x = 1600 + width;
                y = MathUtils.random(0, 900);
                break;
            case 3:
                x = MathUtils.random(0, 1600);
                y = -height;
                break;
        }
    }

    public void move(Ship ship)
    {
        super.move();
        changePhase();

        Vector2 direction = new Vector2(ship.x - x, ship.y - y);

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
