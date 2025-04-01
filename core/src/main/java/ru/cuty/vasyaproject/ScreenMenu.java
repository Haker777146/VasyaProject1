package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.*;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ScreenMenu implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont font;
    private Main main;
    private Music sndMenuMusic, sndPlayScreenMusic;

    Texture imgBackGround;

    SunButton btnPlay;
    SunButton btnSettings;
    SunButton btnLeaderBoard;
    SunButton btnAbout;
    SunButton btnExit;

    public ScreenMenu(Main main)
    {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.vasyaFont;
        sndMenuMusic = main.sndMenuMusic;
        sndPlayScreenMusic = main.sndPlayScreenMusic;

        sndMenuMusic.play();

        imgBackGround = new Texture("MenuBackGround.png");

        btnPlay = new SunButton("Play", font, 90, 740);
        btnSettings = new SunButton("Settings", font, 90, 615);
        btnLeaderBoard = new SunButton("Leaderboard", font, 90, 490);
        btnAbout = new SunButton("About", font, 90, 365);
        btnExit = new SunButton("Exit", font, 90, 240);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnPlay.hit(touch.x, touch.y))
            {
                sndMenuMusic.pause();
                main.setScreen(main.screenGame);
                sndPlayScreenMusic.play();
            }
            if(btnSettings.hit(touch.x, touch.y)){
                main.setScreen(main.screenSettings);
            }
            if(btnLeaderBoard.hit(touch.x, touch.y)){
                main.setScreen(main.screenLeaderBoard);
            }
            if(btnAbout.hit(touch.x, touch.y)){
                main.setScreen(main.screenAbout);
            }
            if(btnExit.hit(touch.x, touch.y)){
                Gdx.app.exit();
            }
        }
        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnPlay.font.draw(batch, btnPlay.text, btnPlay.x, btnPlay.y);
        btnSettings.font.draw(batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnLeaderBoard.font.draw(batch, btnLeaderBoard.text, btnLeaderBoard.x, btnLeaderBoard.y);
        btnAbout.font.draw(batch, btnAbout.text, btnAbout.x, btnAbout.y);
        btnExit.font.draw(batch, btnExit.text, btnExit.x, btnExit.y);
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

    }

    @Override
    public void dispose()
    {
        sndMenuMusic.dispose();
        sndPlayScreenMusic.dispose();
    }
}
