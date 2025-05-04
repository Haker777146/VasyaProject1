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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

public class ScreenGame implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Vector3 touch;
    private BitmapFont vasyaRed, vasyaOrange,vasyaRed50;
    private Main main;
    private Music sndMenuMusic, sndPlayScreenMusic;

    Texture imgJoystick;
    Texture imgBackGround;
    Texture imgPlayersAtlas;
    Texture imgEnemyAtlas;
    Texture imgShotsAtlas;
    TextureRegion[] imgPlayerMag = new TextureRegion[12];
    TextureRegion[] imgEnemy = new TextureRegion[36];
    TextureRegion[][] imgShot = new TextureRegion[3][3];

    SunButton btnBack;
    SunButton btnRestart;

    Space[] space = new Space[1];
    PlayerMag mag;
    List<Enemy> enemies = new ArrayList<>();
    List<Shot> shots = new ArrayList<>();
    private List<Integer> spawnedRanges = new ArrayList<>();
    Player[] players = new Player[10];

    Sound sndBlaster;
    Sound sndExplosion;

    private long timeLastSpawnEnemy, timeSpawnEnemyInterval = 2500;
    private long timeLastShoot, timeShootInterval;
    private long timeLastShootBoss, timeShootIntervalBoss;
    private boolean gameOver;
    private boolean isBossAlive = false;

    public ScreenGame(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;

        vasyaRed = main.vasyaRed;
        vasyaOrange = main.vasyaOrange;
        vasyaRed50 = main.vasyaRed50;

        sndMenuMusic = main.sndMenuMusic;
        sndPlayScreenMusic = main.sndPlayScreenMusic;

        imgJoystick = new Texture("joystick.png");
        imgBackGround = new Texture("BackGroundPlay.png");
        imgPlayersAtlas = new Texture("PlayerMagAtlas.png");
        imgEnemyAtlas = new Texture("EnemyPrizrak.png");
        imgShotsAtlas = new Texture("shots.png");

        sndBlaster = Gdx.audio.newSound(Gdx.files.internal("blaster.mp3"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
            for (int i = 0; i < imgPlayerMag.length; i++) {
                imgPlayerMag[i] = new TextureRegion(imgPlayersAtlas, (i < 6 ? i : 12 - i) * 32, 96, 32, 32);
            }
            for (int i = 0; i < imgEnemy.length; i++) {
                imgEnemy[i] = new TextureRegion(imgEnemyAtlas, 145, 75, 280, 260);
            }
        for(int j = 0; j<imgShot.length; j++) {
            for (int i = 0; i < imgShot.length; i++) {
                imgShot[i][j] = new TextureRegion(imgShotsAtlas, i * 250, j * 250, 250, 250);
            }
        }

        btnBack = new SunButton("X", vasyaRed, 1530, 870);
        btnRestart = new SunButton("restart", vasyaRed, 640, 160);

        space[0] = new Space(0, 0, 0, -3);
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
        // Проверяем, жив ли босс
        isBossAlive = false;
        for (Enemy enemy : enemies)
        {
            if (enemy.type == 3)
            {
                isBossAlive = true;
                break;
            }
        }
        //музыка
        if(isMusicOn) sndPlayScreenMusic.play();
        if (!isMusicOn) sndPlayScreenMusic.stop();
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnBack.hit(touch))
            {
                main.setScreen(main.screenMenu);
                sndPlayScreenMusic.stop();
                if(isMusicOn) sndMenuMusic.play();
            }
            if(gameOver && btnRestart.hit(touch)){
                gameStart();
            }
        }
        if(controls == ACCELEROMETER){
            mag.vx = -Gdx.input.getAccelerometerX()*2;
            mag.vy = -Gdx.input.getAccelerometerY()*2;
        }

        // события
        for(Enemy e: enemies) e.move(mag);
        for(Shot s: shots) s.move();

        //for(Space s: space) s.move();
        spawnEnemy();
        for (Enemy enemy : enemies)
        {
            if (enemy.overlap(mag))
            {
                enemies.clear();
                shots.clear();
                if (!gameOver)
                {
                    gameOver();
                }
                break;
            }
        }
        if(!gameOver)
        {
            mag.move();
            spawnShots();
        }
        for(int i=shots.size()-1; i>=0; i--)
        {
            shots.get(i).move();
            if(shots.get(i).outOfScreen()) {
                shots.remove(i);
                break;
            }
            for (int j = enemies.size()-1; j >= 0; j--)
            {
                if(shots.get(i).type == 0 && shots.get(i).overlap(enemies.get(j)))
                {
                    if(isSoundOn) sndExplosion.play();
                    shots.remove(i);
                    enemies.get(j).takeDamage();
                    if(--enemies.get(j).hp == 0)
                    {
                        main.player.kills++;
                        main.player.score+=enemies.get(j).price;
                        enemies.remove(j);
                    }
                    break;
                }
                if(shots.get(i).type == 1 && shots.get(i).overlap(mag))
                {
                    if (isSoundOn) sndExplosion.play();
                    shots.remove(i);
                    gameOver();
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
            int flip = e.x> mag.x?-1:1;
            batch.setColor(e.getColor().r, e.getColor().g, e.getColor().b, e.alpha);
            batch.draw(imgEnemy[e.phase], e.scrX(), e.scrY(), e.width/2, e.height/2, e.width, e.height, flip, 1, 0);
            batch.setColor(1, 1, 1, 1); // сбрасывание цвета
        }
        for(Shot s: shots)
        {
            if(s.type == 0) batch.draw(imgShot[1][0], s.scrX(), s.scrY(), s.width/2, s.height/2, s.width, s.height, 1, 1, s.rotation);
            if(s.type == 1) batch.draw(imgShot[0][2], s.scrX(), s.scrY(), s.width/2, s.height/2, s.width, s.height, 1, 1, s.rotation);
        }
        int flip = mag.vx>0?1:-1;
        batch.draw(imgPlayerMag[mag.phase], mag.scrX(), mag.scrY(), mag.width/2, mag.height/2, mag.width, mag.height, flip, 1, 0);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        vasyaRed50.draw(batch, "score:"+main.player.score, 10, 885);
        if(gameOver)
        {
            vasyaRed.draw(batch, "GAME OVER", -20, 830, SCR_WIDTH, Align.center, true);
            vasyaOrange.draw(batch, "score", 800, 720, 200, Align.right, false);
            vasyaOrange.draw(batch, "kills", 920, 720, 200, Align.right, false);
            for (int i = 0; i < players.length; i++)
            {
                vasyaOrange.draw(batch, i+1+"", 100+300, 900-230-i*50);
                vasyaOrange.draw(batch, players[i].name, 200+300, 900-230-i*50);
                vasyaOrange.draw(batch, players[i].score+"", 500+300, 900-230-i*50, 200, Align.right, false);
                vasyaOrange.draw(batch, players[i].kills+"", 620+300, 900-230-i*50, 200, Align.right, false);
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
        imgPlayersAtlas.dispose();
        imgJoystick.dispose();
        imgShotsAtlas.dispose();
        imgEnemyAtlas.dispose();
        sndExplosion.dispose();
        sndBlaster.dispose();
    }

    private void spawnEnemy()
    {
        if (!gameOver && TimeUtils.millis() > timeLastSpawnEnemy + timeSpawnEnemyInterval)
        {
            // Спавн обычных врагов (0,1,2), только если нет босса
            if (!isBossAlive)
            {
                enemies.add(new Enemy(MathUtils.random(0, 2)));
            }

            // Спавн босса (тип 3) в диапазоне 30±5 (25-35, 55-65...)
            if (main.player.score >= 25)
            {
                int baseRange = ((main.player.score + 5) / 30) * 30; // Округление до ближайшего 30
                int remainder = main.player.score % 30;
                boolean isInSpawnZone = (remainder >= 25 || remainder <= 5);

                if (isInSpawnZone && !spawnedRanges.contains(baseRange) && !isBossAlive)
                {
                    enemies.clear();
                    Enemy boss = new Enemy(3);
                    enemies.add(boss);
                    spawnedRanges.add(baseRange);
                    isBossAlive = true;
                }
            }

            timeLastSpawnEnemy = TimeUtils.millis();
        }
    }
    private void spawnShots()
    {
        // Снаряды игрока(Normal, Hard, Extreme)
        if ((TimeUtils.millis() > timeLastShoot + timeShootInterval) && (difficulty_game == Normal ||
        difficulty_game == Hard || difficulty_game == Extreme))
        {
            if (mag.vx != 0) {
                if (isSoundOn) sndBlaster.play();
                float offsetX = mag.vx > 0 ? 45 : -60;
                shots.add(new Shot(mag.x + offsetX, mag.y - 30, mag.vx > 0 ? 10 : -10, 0, 0));
            }
            timeLastShoot = TimeUtils.millis();
        }

        // Снаряды игрока(Normal)
        if (difficulty_game == Normal) timeShootInterval = 500;

        // Снаряды игрока(Hard)
        if (difficulty_game == Hard) timeShootInterval = 400;

        // Снаряды игрока(Extreme)
        if (difficulty_game == Extreme) timeShootInterval = 350;

        // Снаряды босса(Normal, Hard, Extreme)
        if ((TimeUtils.millis() > timeLastShootBoss + timeShootIntervalBoss) && (difficulty_game == Normal ||
        difficulty_game == Hard || difficulty_game == Extreme))
        {
            // Снаряды босса (8 направлений)
            for (Enemy enemy : enemies)
            {
                if (enemy.type == 3) {
                    float spawnOffsetX = enemy.vx > 0 ? -45 : 45;
                    float centerX = enemy.x - spawnOffsetX; // Центр по X
                    float centerY = enemy.y; // Центр по Y
                    float speed = 5f; // Скорость снарядов

                    // 8 направлений
                    for (int i = 0; i < 8; i++) {
                        float angle = i * 45f; // Угол в градусах
                        float vx = speed * MathUtils.cosDeg(angle);
                        float vy = speed * MathUtils.sinDeg(angle);
                        shots.add(new Shot(centerX, centerY, vx, vy, 1));
                    }
                    if (isSoundOn) sndBlaster.play();
                    break; // Обрабатываем только первого найденного босса
                }
            }
            timeLastShootBoss = TimeUtils.millis();
        }

        // Снаряды босса(Normal)
        if (difficulty_game == Normal) timeShootIntervalBoss = 1000;

        // Снаряды босса(Hard)
        if (difficulty_game == Hard) timeShootIntervalBoss = 950;

        // Снаряды босса(Extreme)
        if (difficulty_game == Extreme) timeShootIntervalBoss = 900;
    }


    private void gameStart(){
        gameOver = false;
        mag = new PlayerMag(SCR_WIDTH/2, 200, 0, 0);
        enemies.clear();
        shots.clear();
        spawnedRanges.clear();
        isBossAlive = false;
        main.player.score = 0;
        main.player.kills = 0;
    }

    private void gameOver()
    {
        if(isSoundOn) sndExplosion.play();
        mag.x = -10000;
        gameOver = true;
        for (int i = enemies.size()-1; i >= 0; i--) {
            if (enemies.get(i).type == 3) {
                enemies.remove(i);
                isBossAlive = false;
                break;
            }
        }
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
                mag.touchScreen(touch);
            }
            if(controls == JOYSTICK) {
                if(main.joystick.isTouchInside(touch)){
                    mag.touchJoystick(touch, main.joystick);
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
                mag.touchScreen(touch);
            }
            if(controls == JOYSTICK) {
                if(main.joystick.isTouchInside(touch)){
                    mag.touchJoystick(touch, main.joystick);
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
