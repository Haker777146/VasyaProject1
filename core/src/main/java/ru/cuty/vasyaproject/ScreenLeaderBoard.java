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
import com.badlogic.gdx.utils.Align;

public class ScreenLeaderBoard implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont vasyaFont, vasyaOrange, vasyaRed, vasyaWhite;
    private Main main;

    Texture imgBackGround;

    SunButton btnGlobal;
    SunButton btnClear;
    SunButton btnBack;
    Player[] players;


    public ScreenLeaderBoard(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;

        vasyaFont = main.vasyaFont;
        vasyaRed = main.vasyaRed;
        vasyaWhite = main.vasyaWhite;
        vasyaOrange = main.vasyaOrange;

        players = main.screenGame.players;

        imgBackGround = new Texture("Img/MenuBackGround.png");

        btnGlobal = new SunButton("Local", vasyaRed, 1350);
        btnClear = new SunButton("Clear", vasyaRed, 725, 130);
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
            if (btnClear.hit(touch))
            {
                main.screenGame.clearTableOfRecords();
                main.screenGame.saveTableOfRecords();
            }
            if(btnBack.hit(touch.x, touch.y)){
                main.setScreen(main.screenMenu);
            }
        }
        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        vasyaFont.draw(batch, "Leaderboard", 510, 820);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        btnGlobal.font.draw(batch, btnGlobal.text, btnGlobal.x, btnGlobal.y);
        vasyaOrange.draw(batch, "score", 800, 700, 200, Align.right, false);
        vasyaOrange.draw(batch, "kills", 920, 700, 200, Align.right, false);
        for (int i = 0; i < players.length; i++) {
            vasyaOrange.draw(batch, i+1+"", 300+105, 650-i*50);
            vasyaOrange.draw(batch, players[i].name, 400+105, 650-i*50);
            vasyaOrange.draw(batch, players[i].score+"", 700+105, 650-i*50, 200, Align.right, false);
            vasyaOrange.draw(batch, players[i].kills+"", 820+105, 650-i*50, 200, Align.right, false);
        }
        btnClear.font.draw(batch, btnClear.text, btnClear.x, btnClear.y);
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
