package ru.cuty.vasyaproject;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Main extends Game {
    public static final float SCR_WIDTH = 1600;
    public static final float SCR_HEIGHT = 900;
    public static final int SCREEN = 0, JOYSTICK = 1, ACCELEROMETER = 2;
    public static final boolean LEFT = false, RIGHT = true;
    public static int controls = SCREEN;
    public static boolean isSoundOn = true;
    public static final int Normal = 0, Hard = 1, Extreme = 2;
    public static int difficulty_game = Normal;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Vector3 touch;

    public BitmapFont vasyaFont, vasyaRed, vasyaWhite, vasyaOrange, vasyaRed50, vasyaRed70, vasyaGreen50;

    public Music sndMenuMusic, sndPlayScreenMusic;

    Joystick joystick;
    Player player;
    ScreenMenu screenMenu;
    ScreenGame screenGame;
    ScreenSettings screenSettings;
    ScreenLeaderBoard screenLeaderBoard;
    ScreenAbout screenAbout;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        touch = new Vector3();

        vasyaFont = new BitmapFont(Gdx.files.internal("Fonts/vasyaFont.fnt"));
        vasyaRed = new BitmapFont(Gdx.files.internal("Fonts/vasyaRed.fnt"));
        vasyaWhite = new BitmapFont(Gdx.files.internal("Fonts/vasyaWhite.fnt"));
        vasyaOrange = new BitmapFont(Gdx.files.internal("Fonts/vasyaOrange.fnt"));
        vasyaRed50 = new BitmapFont(Gdx.files.internal("Fonts/vasyaRed50.fnt"));
        vasyaRed70 = new BitmapFont(Gdx.files.internal("Fonts/vasyaRed70.fnt"));
        vasyaGreen50 = new BitmapFont(Gdx.files.internal("Fonts/vasyaGreen50.fnt"));

        sndMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("MusicMenu.mp3"));
        sndPlayScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("PlayScreenMusic.mp3"));

        joystick = new Joystick(360, RIGHT);
        player = new Player();
        screenMenu = new ScreenMenu(this);
        screenGame = new ScreenGame(this);
        screenSettings = new ScreenSettings(this);
        screenLeaderBoard = new ScreenLeaderBoard(this);
        screenAbout = new ScreenAbout(this);
        setScreen(screenMenu);
    }

    @Override
    public void dispose() {
        batch.dispose();

        vasyaFont.dispose();
        vasyaWhite.dispose();
        vasyaRed.dispose();
        vasyaRed50.dispose();
        vasyaOrange.dispose();
        vasyaRed70.dispose();
        vasyaGreen50.dispose();

        sndMenuMusic.dispose();
        sndPlayScreenMusic.dispose();
    }
}
