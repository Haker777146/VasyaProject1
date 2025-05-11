package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.*;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class PlayerMag extends SpaceObject{
    public int phase, nPhases = 12;
    private long timeLastPhase, timePhaseInterval = 50;
    public float maxSpeed, accelerationFactor;

    public PlayerMag(float x, float y, float vx, float vy) {
        super(x, y, vx, vy);
        width = 150;
        height = 150;
    }
    public PlayerMag(Main main)
    {

    }
    @Override
    public void move() {
        super.move();
        changePhase();
        outOfScreen();
    }

    private void outOfScreen(){
        if(x<width/2.7f){
            vx = 0;
            x = width/2.7f;
        }
        if(x>SCR_WIDTH-width/2.7f){
            vx = 0;
            x = SCR_WIDTH-width/2.7f;
        }
        if(y<height/2){
            vy = 0;
            y = height/2;
        }
        if(y>SCR_HEIGHT-height/2.7f){
            vy = 0;
            y = SCR_HEIGHT-height/2.7f;
        }
    }

    private void changePhase(){
        if(TimeUtils.millis()>timeLastPhase+timePhaseInterval) {
            if (++phase == nPhases) phase = 0;
            timeLastPhase = TimeUtils.millis();
        }
    }

    public void touchScreen(Vector3 t)
    {
        vx = (t.x - x) / 30;
        vy = (t.y - y) / 30;
    }

    public void touchJoystick(Vector3 t, Joystick j)
    {
        vx = (t.x - j.x)/15;
        vy = (t.y - j.y)/15;
    }
    public void accelerate(float accelX, float accelY)
    {
        if (difficulty_game == Normal) {
            accelerationFactor = 1f;
            maxSpeed = 5.0f;
        }
        if (difficulty_game == Hard) {
            accelerationFactor = 2f;
            maxSpeed = 7.5f;
        }
        if (difficulty_game == Extreme) {
            accelerationFactor = 3f;
            maxSpeed = 10f;
        }
        vy -= accelY * accelerationFactor;
        vx -= accelX * accelerationFactor;

        if (vx > maxSpeed) {
            vx = maxSpeed;
        }
        else if (vx < -maxSpeed) {
            vx = -maxSpeed;
        }

        if (vy > maxSpeed) {
            vy = maxSpeed;
        }
        else if (vy < -maxSpeed) {
            vy = -maxSpeed;
        }
    }
}
