package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ScreenGame implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont vasyaRed;
    private Main main;
    private Music sndMenuMusic, sndPlayScreenMusic;

    Texture imgJoystick;
    Texture imgBackGround;
    Texture imgShipsAtlas;
    Texture imgShotsAtlas;
    TextureRegion[] imgShip = new TextureRegion[12];
    TextureRegion[] imgEnemy = new TextureRegion[12];
    TextureRegion[] imgShot = new TextureRegion[4];

    SunButton btnBack;

    Space[] space = new Space[2];
    Ship ship;
    List<Enemy> enemies = new ArrayList<>();
    List<Shot> shots = new ArrayList<>();

    private long timeLastSpawnEnemy, timeSpawnEnemyInterval = 2000;
    private long timeLastShoot, timeShootInterval = 500;

    public ScreenGame(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        vasyaRed = main.vasyaRed;
        sndMenuMusic = main.sndMenuMusic;
        sndPlayScreenMusic = main.sndPlayScreenMusic;

        imgJoystick = new Texture("joystick.png");
        imgBackGround = new Texture("BackGroundPlay.png");
        imgShipsAtlas = new Texture("PlayerMagAtlas.png");
        imgShotsAtlas = new Texture("shots.png");
            for (int i = 0; i < imgShip.length; i++) {
                imgShip[i] = new TextureRegion(imgShipsAtlas, (i < 6 ? i : 12 - i) * 32, 96, 32, 32);
            }
            for (int i = 0; i < imgEnemy.length; i++) {
                imgEnemy[i] = new TextureRegion(imgShipsAtlas, (i < 6 ? i : 12 - i) * 32, 96, 32, 32);
            }
        for (int i = 0; i < imgShot.length; i++) {
            imgShot[i] = new TextureRegion(imgShotsAtlas, i*100, 0, 100, 350);
        }

        btnBack = new SunButton("X", vasyaRed, 1530, 870);

        space[0] = new Space(0, 0);
        space[1] = new Space(0, SCR_HEIGHT);
        ship = new Ship(SCR_WIDTH/2, 200);
    }

    @Override
    public void show()
    {
        ship.x = SCR_WIDTH/2;
        ship.y = SCR_HEIGHT/2;
        Gdx.input.setInputProcessor(new SunInputProcessor());
    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnBack.hit(touch))
            {
                sndMenuMusic.play();
                sndPlayScreenMusic.stop();
                main.setScreen(main.screenMenu);
            }
        }
        if(controls == ACCELEROMETER){
            ship.vx = -Gdx.input.getAccelerometerX()*2;
            ship.vy = -Gdx.input.getAccelerometerY()*2;
        }

        // события
        for(Space s: space) s.move();
        ship.move();
        spawnEnemy();
        for(Enemy e: enemies) e.move();
        spawnShots();
        for(Shot s: shots) s.move();

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Space s: space) batch.draw(imgBackGround, s.x, s.y, s.width, s.height);
        if(controls == JOYSTICK){
            batch.draw(imgJoystick, main.joystick.scrX(), main.joystick.scrY(), main.joystick.width, main.joystick.height);
        }
        for(Enemy e: enemies){
            batch.draw(imgEnemy[e.phase], e.scrX(), e.scrY(), e.width, e.height);
        }
        for(Shot s: shots){
            batch.draw(imgShot[0], s.scrX(), s.scrY(), s.width, s.height);
        }
        batch.draw(imgShip[ship.phase], ship.scrX(), ship.scrY(), ship.width, ship.height);
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
        imgBackGround.dispose();
        imgShipsAtlas.dispose();
        imgJoystick.dispose();
        imgShotsAtlas.dispose();
    }

    private void spawnEnemy(){
        if(TimeUtils.millis()>timeLastSpawnEnemy+timeSpawnEnemyInterval){
            enemies.add(new Enemy());
            timeLastSpawnEnemy = TimeUtils.millis();
        }
    }
    private void spawnShots(){
        if(TimeUtils.millis()>timeLastShoot+timeShootInterval){
            shots.add(new Shot(ship.x-60, ship.y));
            shots.add(new Shot(ship.x+60, ship.y));
            timeLastShoot = TimeUtils.millis();
        }
    }

    class SunInputProcessor implements InputProcessor{

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            touch.set(screenX, screenY, 0);
            camera.unproject(touch);
            if(controls == SCREEN) {
                ship.touchScreen(touch);
            }
            if(controls == JOYSTICK) {
                if(main.joystick.isTouchInside(touch)){
                    ship.touchJoystick(touch, main.joystick);
                }
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            touch.set(screenX, screenY, 0);
            camera.unproject(touch);
            if(controls == SCREEN) {
                ship.touchScreen(touch);
            }
            if(controls == JOYSTICK) {
                if(main.joystick.isTouchInside(touch)){
                    ship.touchJoystick(touch, main.joystick);
                }
            }
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}
