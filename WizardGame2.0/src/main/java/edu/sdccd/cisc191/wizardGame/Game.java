package edu.sdccd.cisc191.wizardGame;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import edu.sdccd.cisc191.wizardGame.events.KeyInput;
import edu.sdccd.cisc191.wizardGame.events.MouseInput;
import edu.sdccd.cisc191.wizardGame.gui.anim.Camera;
import edu.sdccd.cisc191.wizardGame.gui.screen.*;
import edu.sdccd.cisc191.wizardGame.gui.screen.Menu;
import edu.sdccd.cisc191.wizardGame.gui.screen.Window;
import edu.sdccd.cisc191.wizardGame.objects.Block;
import edu.sdccd.cisc191.wizardGame.objects.Crate;
import edu.sdccd.cisc191.wizardGame.objects.Ent;
import edu.sdccd.cisc191.wizardGame.objects.Handler;
import edu.sdccd.cisc191.wizardGame.objects.Hound;
import edu.sdccd.cisc191.wizardGame.objects.ID;
import edu.sdccd.cisc191.wizardGame.objects.Knight;
import edu.sdccd.cisc191.wizardGame.objects.Minion;
import edu.sdccd.cisc191.wizardGame.objects.Totem;
import edu.sdccd.cisc191.wizardGame.objects.Wizard;
import edu.sdccd.cisc191.wizardGame.utils.images.BufferedImageLoader;
import edu.sdccd.cisc191.wizardGame.utils.images.SpriteSheet;

import javax.swing.*;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;
    protected static STATE State = STATE.MENU; // Protected static as all classes need this variable

    private final int SCREEN_WIDTH = 1920;
    private final int SCREEN_HEIGHT = 1080;

    private boolean isRunning = false;
    private Thread gameThread;

    // Camera and handler.
    private Camera camera;
    private Handler handler;

    // Objects used for menus
    private Menu menu;
    private Help help;
    private Pause pause;

    // Spritesheets
    private SpriteSheet ss;
    private SpriteSheet cs; // character sheet

    private BufferedImage sprite_sheet = null;
    private BufferedImage char_sheet = null;
    private BufferedImage floor = null;
    private BufferedImage lives_image;

    // JFrame variables
    private JFrame frame;
    private GraphicsDevice device = GraphicsEnvironment // Used for fullscreen.
            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    // Variables that are critical for display in the HUD, ammo, hp etc.
    private int ammo = 50;
    private int hp = 100; // Wizard fills the hp upon construction.
    private int lives; // Lives fed as argument in constructor.
    private AbstractLevel level; // this will be an abstract level
    private boolean totem_flag = false;

    // Modifying state debug
    public enum STATE{
        MAIN_MENU,
        MENU,
        GAME,
        HELP,
        PAUSE,
        LEVEL_1,
    };

    public Game() {

        //Window.changeLevel(this); // Level one
        frame = new JFrame("Wizard Game");
        frame.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_WIDTH));
        frame.setMaximumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        frame.setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        frame.setResizable((false));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //device.setFullScreenWindow(frame); // Careful with this

        // Don't create a new instance of this handler again, could cause problems.
        // Instead place original instance into new classes, since the game objects are inside this handler.
        // independant for each level not the controller
        level = new LevelOne(this);
        menu = new Menu();// yes every state object should be created here
        help = new Help();
        pause = new Pause();
        camera = new Camera(0, 0); // and the camera
        lives = 3; // lives and HP are independant of the level.

        BufferedImageLoader loader = new BufferedImageLoader();

        sprite_sheet = loader.loadImage("/main_sheet.png"); // load in the levels not here
        char_sheet = loader.loadImage("/wizard_sheet.png"); //char sheet

        ss = new SpriteSheet(sprite_sheet);

        cs = new SpriteSheet(char_sheet); // character sheet

        lives_image = cs.grabImage(13, 8, 32, 32); // Sprite to display lives.

        /* current_level parameter determines which level is loaded.
        switch (current_level) {
            case 1:
                level = loader.loadImage("/level_one.png"); // load level
                floor = ss.grabImage(6, 6, 32, 32); // load floor tiles
                level_numb = current_level;
                break;
            case 2:
                level = loader.loadImage("/level_two.png"); // load level 2
                floor = ss.grabImage(7, 2, 32, 32); // load different floor tiles
                level_numb = current_level;
                break;
            case 3:
                level = loader.loadImage("/level_two.png"); // load level 2
                floor = ss.grabImage(7, 2, 32, 32); // load different floor tiles
                level_numb = current_level;
                break;
            case 4:
                // If you go past level 4 will switch to white screen of death.
                level = loader.loadImage("/level_two.png"); // load level 2
                floor = ss.grabImage(7, 2, 32, 32); // load different floor tiles
                level_numb = current_level;
                break;
        }*/

        this.Update();

        //loadLevel(level);
        frame.add(this); // Add this game to the frame.
        frame.validate();
        start(); // Everything is loaded in, now begin rendering.
    }

    public synchronized void start() {
        if (!isRunning) {
            isRunning = true;
            gameThread = new Thread(this, "GameController");
            gameThread.start();
        }
    }

    public synchronized void stop() {
        if (isRunning) {
            isRunning = false;
            System.exit(0);
        }
    }

    public void run() {
        // Main game loop thread.

        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while(isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                tick();
                updates++;
                delta--;
            }
            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
                updates = 0;
            }
        }
        stop();
    }

    public void tick() {
        // Change this to a switch statement, every different case has it's own tick.
        // Moves objects to next position 60 times a second.
        if(State == STATE.LEVEL_1) {
            level.tick();
        }
    } // end tick method

    public void render() {
        // Render the particular levels render method or you're going to have a bad time...

        // Renders everything in the game.
        // Again this should just be switch statements deciding what to render.
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            // Three in the chamber preloads 3 frames for each render.
            this.createBufferStrategy(3);
            return;
        }

        /* Get the graphics2D object from the buffer strategy. */
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        if(State == STATE.LEVEL_1) {
            level.render(g);
            //////////////////////////////////
        } else if(State == STATE.MENU) {
            menu.render(g);
        } else if (State == STATE.HELP) {
            help.render(g);
        } else if(State == STATE.PAUSE) {
            pause.render(g);
        }//end if state
        g.dispose();
        bs.show();
    } // end render method

    // Loading the level.
    //private void loadLevel(BufferedImage image) {
       // This should take place outside of the Game class.
   // }

    public static void quitGame(){
        // If quit game is activated, the window will close and the program will exit.
        System.exit(0);
    }

    public void Update(){
        // Update handler, controllers etc.
        handler = getHandler();
        camera = getCamera();
        this.addMouseListener(new MouseInput(handler, camera, this, ss, cs));
        this.addKeyListener(new KeyInput(handler)); // is getting null for some reason?

    }

    /* Setters and getters for private variables. */
    public STATE getGameState() {
        return State;
    }

    public void setGameState(STATE State){
        this.State = State;
    }
    public int getAmmo() {
        return ammo;
    }

    public void setAmmo (int ammo)
    {
        this.ammo = ammo;
    }

    public void incAmmo(int inc){
        ammo += inc;
    }

    public int getHp(){
        return hp;
    }

    public void setHp(int hp){
        this.hp = hp;
    }

    public void decHp(){
        hp--;
    }

    public int getLives()
    {
        return lives;
    }

    public void decLives(){
        lives--;
    }

    public void setLives(int lives)
    {
        this.lives = lives;
    }

    public Handler getHandler(){
        // return the current handler depending on which level

        return level.getHandler();
    }

    public void setHandler(){
        if(State == STATE.LEVEL_1) {
            this.handler = level.getHandler();
        }
    }

    public Camera getCamera() {
        return level.getCamera();
    }

    public AbstractLevel getLevel(){
        return level;
    }

    public static void main(String[] args) {
        new Game(); // Represents the first level thread.
    }
}
