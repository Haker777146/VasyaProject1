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
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
    private BitmapFont vasyaFont, vasyaRed, vasyaWhite, vasyaOrange, vasyaRed50, vasyaRed70;
    private Main main;
    private InputKeyboard keyboard;

    Texture imgBackGround;

    SunButton btnControl;
    SunButton btnScreen;
    SunButton btnJoystick;
    SunButton btnAccelerometer;

    SunButton btnBack;
    SunButton btnSound;
    SunButton btnMusic;
    SunButton btnName;

    SunButton btnGame_difficulty;
    SunButton btnNormal;
    SunButton btnHard;
    SunButton btnExtreme;

    private Music sndMenuMusic, sndPlayScreenMusic;

    public ScreenSettings(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;

        vasyaFont = main.vasyaFont;
        vasyaWhite = main.vasyaWhite;
        vasyaRed = main.vasyaRed;
        vasyaOrange = main.vasyaOrange;
        vasyaRed50 = main.vasyaRed50;
        vasyaRed70 = main.vasyaRed70;

        sndMenuMusic = main.sndMenuMusic;
        sndPlayScreenMusic = main.sndPlayScreenMusic;

        keyboard = new InputKeyboard(vasyaWhite, SCR_WIDTH, SCR_HEIGHT/2, 7);


        imgBackGround = new Texture("MenuBackGround.png");

        loadSettings();

        btnName = new SunButton("Name: "+main.player.name, vasyaRed70, 505, 740);
        btnControl = new SunButton("Control", vasyaFont, 100, 570-65);
        btnScreen = new SunButton("Touch Screen", vasyaRed70, 200, 450-65);
        btnJoystick = new SunButton(main.joystick.getText(), vasyaWhite, 200, 350-65);
        btnAccelerometer = new SunButton("Accelerometer", vasyaWhite, 200, 250-65);
        setFontColorByControls();
        btnSound = new SunButton(isSoundOn ? "Sound ON" : "Sound OFF", isSoundOn ? vasyaRed70 : vasyaWhite, 880, 670-65);
        btnMusic = new SunButton(isMusicOn ? "Music ON" : "Music OFF", isMusicOn ? vasyaRed70 : vasyaWhite, 100, 670-65);
        setFontColorByMusicAndSound();
        btnGame_difficulty = new SunButton("Game difficulty",vasyaFont,880,570-65);
        btnNormal = new SunButton("Normal",vasyaRed70,980,450-65);
        btnHard = new SunButton("Hard",vasyaWhite,980,350-65);
        btnExtreme = new SunButton("Extreme",vasyaWhite,980,250-65);
        btnBack = new SunButton("X", vasyaRed, 1530, 870);
        setFontColorByDifficulty_game();
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        //музыка
        if(isMusicOn) sndMenuMusic.play();
        if (!isMusicOn) sndMenuMusic.pause();
        // касания
        if(Gdx.input.justTouched())
        {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(keyboard.isKeyboardShow)
            {
                if (keyboard.touch(touch))
                {
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
                if (btnSound.hit(touch))
                {
                    isSoundOn = !isSoundOn;
                    setFontColorByMusicAndSound();
                }
                if (btnMusic.hit(touch))
                {
                    isMusicOn = !isMusicOn;
                    setFontColorByMusicAndSound();
                }
                if(btnNormal.hit(touch))
                {
                    difficulty_game = Normal;
                    setFontColorByDifficulty_game();
                }
                if(btnHard.hit(touch))
                {
                    difficulty_game = Hard;
                    setFontColorByDifficulty_game();
                }
                if(btnExtreme.hit(touch))
                {
                    difficulty_game = Extreme;
                    setFontColorByDifficulty_game();
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
        vasyaFont.draw(batch, "Settings", -60, 860, SCR_WIDTH, Align.center, false);
        btnName.font.draw(batch, btnName.text, btnName.x, btnName.y);
        btnControl.font.draw(batch, btnControl.text, btnControl.x, btnControl.y);
        btnScreen.font.draw(batch, btnScreen.text, btnScreen.x, btnScreen.y);
        btnJoystick.font.draw(batch, btnJoystick.text, btnJoystick.x, btnJoystick.y);
        btnAccelerometer.font.draw(batch, btnAccelerometer.text, btnAccelerometer.x, btnAccelerometer.y);
        btnSound.font.draw(batch, btnSound.text, btnSound.x, btnSound.y);
        btnMusic.font.draw(batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnGame_difficulty.font.draw(batch, btnGame_difficulty.text, btnGame_difficulty.x, btnGame_difficulty.y);
        btnNormal.font.draw(batch, btnNormal.text, btnNormal.x, btnNormal.y);
        btnHard.font.draw(batch, btnHard.text, btnHard.x, btnHard.y);
        btnExtreme.font.draw(batch, btnExtreme.text, btnExtreme.x, btnExtreme.y);
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
        btnScreen.setFont(controls == SCREEN ? vasyaRed70 : vasyaWhite);
        btnJoystick.setFont(controls == JOYSTICK ? vasyaRed70 : vasyaWhite);
        btnAccelerometer.setFont(controls == ACCELEROMETER ? vasyaRed70 : vasyaWhite);
    }
    private void setFontColorByDifficulty_game()
    {
        btnNormal.setFont(difficulty_game == Normal ? vasyaRed70 : vasyaWhite);
        btnHard.setFont(difficulty_game == Hard ? vasyaRed70 : vasyaWhite);
        btnExtreme.setFont(difficulty_game == Extreme ? vasyaRed70 : vasyaWhite);
    }
    private void setFontColorByMusicAndSound()
    {
        btnMusic.setFont(isMusicOn ? vasyaRed70 : vasyaWhite);
        btnMusic.setText(isMusicOn ? "Music ON" : "Music OFF");
        btnSound.setFont(isSoundOn ? vasyaRed70 : vasyaWhite);
        btnSound.setText(isSoundOn ? "Sound ON" : "Sound OFF");
    }
    private void saveSettings(){
        Preferences prefs = Gdx.app.getPreferences("VasyaProjectSettings");
        prefs.putString("name", main.player.name);
        prefs.putInteger("controls", controls);
        prefs.putBoolean("joystick", main.joystick.side);
        prefs.putBoolean("sound", isSoundOn);
        prefs.putBoolean("music", isMusicOn);
        prefs.putInteger("difficulty_game", difficulty_game);
        prefs.flush();
    }

    private void loadSettings(){
        Preferences prefs = Gdx.app.getPreferences("VasyaProjectSettings");
        main.player.name = prefs.getString("name", "Noname");
        controls = prefs.getInteger("controls", SCREEN);
        main.joystick.setSide(prefs.getBoolean("joystick", RIGHT));
        isSoundOn = prefs.getBoolean("sound", true);
        isMusicOn = prefs.getBoolean("music", true);
        difficulty_game = prefs.getInteger("difficulty_game", Normal);
    }
}
