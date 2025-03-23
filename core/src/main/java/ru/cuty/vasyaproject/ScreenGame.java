package ru.cuty.vasyaproject;

import static ru.cuty.vasyaproject.Main.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Align;

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
    TextureRegion[] imgEnemy = new TextureRegion[36];
    TextureRegion[] imgShot = new TextureRegion[4];
    TextureRegion[][] imgFragment = new TextureRegion[5][36];

    SunButton btnBack;
    SunButton btnRestart;

    Space[] space = new Space[2];
    Ship ship;
    List<Enemy> enemies = new ArrayList<>();
    List<Shot> shots = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    Player[] players = new Player[10];

    Sound sndBlaster;
    Sound sndExplosion;

    private long timeLastSpawnEnemy, timeSpawnEnemyInterval = 2000;
    private long timeLastShoot, timeShootInterval = 500;
    private int nFragments = 150;
    private boolean gameOver;

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

        sndBlaster = Gdx.audio.newSound(Gdx.files.internal("blaster.mp3"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
            for (int i = 0; i < imgShip.length; i++) {
                imgShip[i] = new TextureRegion(imgShipsAtlas, (i < 6 ? i : 12 - i) * 32, 96, 32, 32);
            }
            for (int i = 0; i < imgEnemy.length; i++) {
                imgEnemy[i] = new TextureRegion(imgShipsAtlas, (i < 6 ? i : 12 - i) * 32, 96, 32, 32);
            }
        for (int i = 0; i < imgShot.length; i++) {
            imgShot[i] = new TextureRegion(imgShotsAtlas, i*250, 0, 250, 250);
        }
        int k = (int) Math.sqrt(imgFragment[0].length);
        int size = 400/k;
        for(int j = 0; j<imgFragment.length; j++) {
            for (int i = 0; i < imgFragment[j].length; i++) {
                if(j==imgFragment.length-1) imgFragment[j][i] = new TextureRegion(imgShip[0], i%k*size, i/k*size, size, size);
                else imgFragment[j][i] = new TextureRegion(imgEnemy[0], i%k*size, i/k*size, size, size);
            }
        }

        btnBack = new SunButton("X", vasyaRed, 1530, 870);
        btnRestart = new SunButton("restart", vasyaRed, 300);

        space[0] = new Space(0, 0, 0, -3);
        space[1] = new Space(0, SCR_HEIGHT, 0, -3);
        for (int i = 0; i < players.length; i++)
        {
            players[i] = new Player();
        }
        loadTableOfRecords();
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(new SunInputProcessor());
        gameStart();
    }

    @Override
    public void render(float delta)
    {
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
            if(gameOver && btnRestart.hit(touch)){
                gameStart();
            }
        }
        if(controls == ACCELEROMETER){
            ship.vx = -Gdx.input.getAccelerometerX()*2;
            ship.vy = -Gdx.input.getAccelerometerY()*2;
        }

        // события
        for(Enemy e: enemies) e.move(ship);
        for(Shot s: shots) s.move();

        //for(Space s: space) s.move();
        spawnEnemy();
        for(int i=enemies.size()-1; i>=0; i--) {
            enemies.get(i).move();
            if(enemies.get(i).outOfScreen()){
                enemies.remove(i);
                if(!gameOver) gameOver();
                break;
            }
            if(enemies.get(i).overlap(ship))
            {
                enemies.remove(i);
                gameOver();
            }
        }
        if(!gameOver)
        {
            ship.move();
            spawnShots();
        }
        for(int i=shots.size()-1; i>=0; i--){
            shots.get(i).move();
            if(shots.get(i).outOfScreen()) {
                shots.remove(i);
                break;
            }
            for (int j = enemies.size()-1; j >= 0; j--) {
                if(shots.get(i).overlap(enemies.get(j))){
                    if(isSoundOn) sndExplosion.play();
                    shots.remove(i);
                    if(--enemies.get(j).hp == 0) {
                        main.player.kills++;
                        main.player.score+=enemies.get(j).price;
                        enemies.remove(j);
                    }
                    break;
                }
            }
        }

        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Space s: space) batch.draw(imgBackGround, s.x, s.y, s.width, s.height);
        if(controls == JOYSTICK){
            batch.draw(imgJoystick, main.joystick.scrX(), main.joystick.scrY(), main.joystick.width, main.joystick.height);
        }
        for(Enemy e: enemies){
            int flip = e.x>ship.x?-1:1;
            batch.draw(imgEnemy[e.phase], e.scrX(), e.scrY(), e.width/2, e.height/2, e.width, e.height, flip, 1, 0);
        }
        for(Shot s: shots){
            batch.draw(imgShot[0], s.scrX(), s.scrY(), s.width/2, s.height/2, s.width, s.height, 1, 1, s.rotation);
        }
        batch.draw(imgShip[ship.phase], ship.scrX(), ship.scrY(), ship.width, ship.height);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        vasyaRed.draw(batch, "score:"+main.player.score, 10, 880);
        if(gameOver)
        {
            vasyaRed.draw(batch, "GAME OVER", -10, 475, SCR_WIDTH, Align.center, true);
            vasyaRed.draw(batch, "score", 500, 900, 200, Align.right, false);
            vasyaRed.draw(batch, "kills", 620, 900, 200, Align.right, false);
            for (int i = 0; i < players.length; i++)
            {
                vasyaRed.draw(batch, i+1+"", 100, 900-i*70);
                vasyaRed.draw(batch, players[i].name, 200, 900-i*70);
                vasyaRed.draw(batch, players[i].score+"", 500, 900-i*70, 200, Align.right, false);
                vasyaRed.draw(batch, players[i].kills+"", 620, 900-i*70, 200, Align.right, false);
            }
            btnRestart.font.draw(batch, btnRestart.text, btnRestart.x, btnRestart.y);
        }
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
            shots.add(new Shot(ship.x+40, ship.y-30, 5, 0));
            timeLastShoot = TimeUtils.millis();
        }
    }
    private void spawnFragments(SpaceObject o){
        for (int i = 0; i < nFragments; i++) {
            fragments.add(new Fragment(o.x, o.y, o.type, imgFragment[0].length));
        }
    }

    private void gameStart(){
        gameOver = false;
        ship = new Ship(SCR_WIDTH/2, 200, 0, 0);
        enemies.clear();
        fragments.clear();
        shots.clear();
        main.player.score = 0;
        main.player.kills = 0;
    }

    private void gameOver()
    {
        if(isSoundOn) sndExplosion.play();
        spawnFragments(ship);
        ship.x = -10000;
        gameOver = true;
        players[players.length-1].clone(main.player);
        sortTableOfRecords();
        saveTableOfRecords();
    }

    private void sortTableOfRecords(){
        for (int j = 0; j < players.length; j++) {
            for (int i = 0; i < players.length-1; i++) {
                if(players[i].score < players[i+1].score){
                    Player tmp = players[i];
                    players[i] = players[i+1];
                    players[i+1] = tmp;
                }
            }
        }
    }

    public void saveTableOfRecords(){
        Preferences prefs = Gdx.app.getPreferences("VasyaProject");
        for (int i = 0; i < players.length; i++) {
            prefs.putString("name"+i, players[i].name);
            prefs.putInteger("score"+i, players[i].score);
            prefs.putInteger("kills"+i, players[i].kills);
        }
        prefs.flush();
    }

    private void loadTableOfRecords(){
        Preferences prefs = Gdx.app.getPreferences("VasyaProject");
        for (int i = 0; i < players.length; i++) {
            players[i].name = prefs.getString("name"+i, "Noname");
            players[i].score = prefs.getInteger("score"+i, 0);
            players[i].kills = prefs.getInteger("kills"+i, 0);
        }
    }

    public void clearTableOfRecords()
    {
        for (Player player : players) player.clear();
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
        //TouchDown
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
