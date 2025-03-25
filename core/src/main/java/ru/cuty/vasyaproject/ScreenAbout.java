package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.SCR_HEIGHT;
import static ru.cuty.vasyaproject.Main.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


public class ScreenAbout implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont vasyaFont, vasyaRed, vasyaRed50, vasyaRed70, vasyaOrange, vasyaGreen50;
    private Main main;

    Texture imgBackGround, imgBackGroundForTextAbout;

    SunButton btnBack;

    public ScreenAbout(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        vasyaFont = main.vasyaFont;
        vasyaRed = main.vasyaRed;
        vasyaRed50 = main.vasyaRed50;
        vasyaRed70 = main.vasyaRed70;
        vasyaOrange = main.vasyaOrange;
        vasyaGreen50 = main.vasyaGreen50;

        imgBackGround = new Texture("MenuBackGround.png");
        imgBackGroundForTextAbout = new Texture("BackGroundForTextAbout.png");

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

            if(btnBack.hit(touch.x, touch.y)){
                main.setScreen(main.screenMenu);
            }
        }
        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        batch.draw(imgBackGroundForTextAbout, 135, 60, SCR_WIDTH-300, SCR_HEIGHT-100);
        vasyaFont.draw(batch, "About", 670, 825);
        vasyaGreen50.draw(batch, "Hello!\n " +
            "In this exciting game you need to score more points\n " +
            "and improve your finger reaction!\n " +
            "Wish you a great game,\n " +
            "and lots of success in scoring points!", 210, 720);
        vasyaGreen50.draw(batch, "Good luck!", SCR_WIDTH/2-150, 290);
        vasyaGreen50.draw(batch, "Creater: Primakov Vasily Evgenievich ", 220, 170);
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
    public void dispose()
    {
        imgBackGround.dispose();
        imgBackGroundForTextAbout.dispose();
    }
}
