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
import com.badlogic.gdx.utils.Align;

public class ScreenSettings implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont vasyaFont, vasyaRed, vasyaWhite;
    private Main main;
    private InputKeyboard keyboard;

    Texture imgBackGround;

    SunButton btnControl;
    SunButton btnScreen;
    SunButton btnJoystick;
    SunButton btnAccelerometer;
    SunButton btnBack;
    SunButton btnSound;
    SunButton btnName;

    public ScreenSettings(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        vasyaFont = main.vasyaFont;
        vasyaWhite = main.vasyaWhite;
        vasyaRed = main.vasyaRed;
        keyboard = new InputKeyboard(vasyaWhite, SCR_WIDTH, SCR_HEIGHT/2, 7);


        imgBackGround = new Texture("MenuBackGround.png");

        loadSettings();

        btnName = new SunButton("Name: "+main.player.name, vasyaRed, 100, 700);
        btnControl = new SunButton("Control", vasyaFont, 100, 570);
        btnScreen = new SunButton("Touch Screen", vasyaRed, 200, 450);
        btnJoystick = new SunButton(main.joystick.getText(), vasyaWhite, 200, 350);
        btnAccelerometer = new SunButton("Accelerometer", vasyaWhite, 200, 250);
        setFontColorByControls();
        btnSound = new SunButton(isSoundOn ? "Sound ON" : "Sound OFF", vasyaWhite, 200, 150);
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

            if(keyboard.isKeyboardShow) {
                if (keyboard.touch(touch)) {
                    main.player.name = keyboard.getText();
                    btnName.setText("Name: "+main.player.name);
                }
            }
            else
            {
                if (btnName.hit(touch)) {
                    keyboard.start();
                }

                if (btnScreen.hit(touch)) {
                    controls = SCREEN;
                    setFontColorByControls();
                }
                if (btnJoystick.hit(touch)) {
                    if (controls == JOYSTICK) {
                        main.joystick.setSide(!main.joystick.side);
                        btnJoystick.setText(main.joystick.getText());
                    } else {
                        controls = JOYSTICK;
                    }
                    setFontColorByControls();
                }
                if (btnAccelerometer.hit(touch)) {
                    if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
                        controls = ACCELEROMETER;
                        setFontColorByControls();
                    }
                }
                if (btnSound.hit(touch)) {
                    isSoundOn = !isSoundOn;
                    btnSound.setText(isSoundOn ? "Sound ON" : "Sound OFF");
                }
                if (btnBack.hit(touch)) {
                    main.setScreen(main.screenMenu);
                }
            }
        }
        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        vasyaFont.draw(batch, "SETTINGS", -60, 860, SCR_WIDTH, Align.center, false);
        btnName.font.draw(batch, btnName.text, btnName.x, btnName.y);
        btnControl.font.draw(batch, btnControl.text, btnControl.x, btnControl.y);
        btnScreen.font.draw(batch, btnScreen.text, btnScreen.x, btnScreen.y);
        btnJoystick.font.draw(batch, btnJoystick.text, btnJoystick.x, btnJoystick.y);
        btnAccelerometer.font.draw(batch, btnAccelerometer.text, btnAccelerometer.x, btnAccelerometer.y);
        btnSound.font.draw(batch, btnSound.text, btnSound.x, btnSound.y);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        keyboard.draw(batch);
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
    public void dispose()
    {
        keyboard.dispose();
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
