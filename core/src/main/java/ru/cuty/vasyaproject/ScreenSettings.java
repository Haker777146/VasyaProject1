package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.*;

import static ru.cuty.vasyaproject.Main.ACCELEROMETER;
import static ru.cuty.vasyaproject.Main.JOYSTICK;
import static ru.cuty.vasyaproject.Main.SCREEN;
import static ru.cuty.vasyaproject.Main.SCR_HEIGHT;
import static ru.cuty.vasyaproject.Main.SCR_WIDTH;
import static ru.cuty.vasyaproject.Main.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
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
    SunButton btnSound;

    public ScreenSettings(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        vasyaFont = main.vasyaFont;
        vasyaWhite = main.vasyaWhite;
        vasyaRed = main.vasyaRed;


        imgBackGround = new Texture("MenuBackGround.png");

        loadSettings();

        btnSettings = new SunButton("Settings", vasyaFont, 600, 800);
        btnControl = new SunButton("Control", vasyaFont, 100, 620);
        btnScreen = new SunButton("Touch Screen", vasyaRed, 200, 500);
        btnJoystick = new SunButton(main.joystick.getText(), vasyaWhite, 200, 400);
        btnAccelerometer = new SunButton("Accelerometer", vasyaWhite, 200, 300);
        setFontColorByControls();
        btnSound = new SunButton(isSoundOn ? "Sound ON" : "Sound OFF", vasyaWhite, 100, 750);
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
                controls = SCREEN;
                setFontColorByControls();
            }
            if(btnJoystick.hit(touch)){
                if(controls == JOYSTICK){
                    main.joystick.setSide(!main.joystick.side);
                    btnJoystick.setText(main.joystick.getText());
                }
                else
                {
                    controls = JOYSTICK;
                }
                setFontColorByControls();
            }
            if(btnAccelerometer.hit(touch)){
                if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                    controls = ACCELEROMETER;
                    setFontColorByControls();
                }
            }
            if(btnSound.hit(touch)){
                isSoundOn = !isSoundOn;
                btnSound.setText(isSoundOn ? "Sound ON" : "Sound OFF");
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
    public void hide()
    {
        saveSettings();
    }

    @Override
    public void dispose() {

    }
    private void setFontColorByControls()
    {
        btnScreen.setFont(controls == SCREEN ? vasyaRed : vasyaWhite);
        btnJoystick.setFont(controls == JOYSTICK ? vasyaRed : vasyaWhite);
        btnAccelerometer.setFont(controls == ACCELEROMETER ? vasyaRed : vasyaWhite);
    }

    private void saveSettings(){
        Preferences prefs = Gdx.app.getPreferences("VasyaProjectSettings");
        prefs.putString("name", main.player.name);
        prefs.putInteger("controls", controls);
        prefs.putBoolean("joystick", main.joystick.side);
        prefs.putBoolean("sound", isSoundOn);
        prefs.flush();
    }

    private void loadSettings(){
        Preferences prefs = Gdx.app.getPreferences("VasyaProjectSettings");
        main.player.name = prefs.getString("name", "Noname");
        controls = prefs.getInteger("controls", SCREEN);
        main.joystick.setSide(prefs.getBoolean("joystick", RIGHT));
        isSoundOn = prefs.getBoolean("sound", true);
    }
}
