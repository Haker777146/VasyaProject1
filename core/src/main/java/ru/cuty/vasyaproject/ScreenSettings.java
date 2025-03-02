package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.ACCELEROMETER;
import static ru.cuty.vasyaproject.Main.JOYSTICK;
import static ru.cuty.vasyaproject.Main.SCREEN;
import static ru.cuty.vasyaproject.Main.SCR_HEIGHT;
import static ru.cuty.vasyaproject.Main.SCR_WIDTH;
import static ru.cuty.vasyaproject.Main.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ScreenSettings implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont vasyaFont, vasyaRed, vasyaWhite;
    private Main main;

    Texture imgBackGround;

    SunButton btnSettings;
    SunButton btnControl;
    SunButton btnScreen;
    SunButton btnJoystick;
    SunButton btnAccelerometer;
    SunButton btnBack;

    public ScreenSettings(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        vasyaFont = main.vasyaFont;
        vasyaWhite = main.vasyaWhite;
        vasyaRed = main.vasyaRed;


        imgBackGround = new Texture("MenuBackGround.png");
        btnSettings = new SunButton("Settings", vasyaFont, 800);
        btnControl = new SunButton("Control", vasyaFont, 100, 620);
        btnScreen = new SunButton("Screen", vasyaFont, 200, 500);
        btnJoystick = new SunButton(main.joystick.getText(), vasyaWhite, 200, 400);
        btnAccelerometer = new SunButton("Accelerometer", vasyaWhite, 200, 300);
        btnBack = new SunButton("X", vasyaRed, 1530, 870);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnScreen.hit(touch)){
                btnScreen.setFont(vasyaFont);
                btnJoystick.setFont(vasyaWhite);
                btnAccelerometer.setFont(vasyaWhite);
                controls = SCREEN;
            }
            if(btnJoystick.hit(touch)){
                btnScreen.setFont(vasyaWhite);
                btnJoystick.setFont(vasyaFont);
                btnAccelerometer.setFont(vasyaWhite);
                if(controls == JOYSTICK){
                    main.joystick.setSide(!main.joystick.side);
                    btnJoystick.setText(main.joystick.getText());
                }
                else {
                    controls = JOYSTICK;
                }
            }
            if(btnAccelerometer.hit(touch)){
                if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                    btnScreen.setFont(vasyaWhite);
                    btnJoystick.setFont(vasyaWhite);
                    btnAccelerometer.setFont(vasyaFont);
                    controls = ACCELEROMETER;
                }
            }
            if(btnBack.hit(touch)){
                main.setScreen(main.screenMenu);
            }
        }
        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnSettings.font.draw(batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnControl.font.draw(batch, btnControl.text, btnControl.x, btnControl.y);
        btnScreen.font.draw(batch, btnScreen.text, btnScreen.x, btnScreen.y);
        btnJoystick.font.draw(batch, btnJoystick.text, btnJoystick.x, btnJoystick.y);
        btnAccelerometer.font.draw(batch, btnAccelerometer.text, btnAccelerometer.x, btnAccelerometer.y);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
