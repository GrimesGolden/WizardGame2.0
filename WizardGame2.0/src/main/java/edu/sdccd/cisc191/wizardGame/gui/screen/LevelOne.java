package edu.sdccd.cisc191.wizardGame.gui.screen;

import edu.sdccd.cisc191.wizardGame.Game;
import edu.sdccd.cisc191.wizardGame.events.KeyInput;
import edu.sdccd.cisc191.wizardGame.gui.anim.Camera;
import edu.sdccd.cisc191.wizardGame.objects.*;
import edu.sdccd.cisc191.wizardGame.utils.images.BufferedImageLoader;
import edu.sdccd.cisc191.wizardGame.utils.images.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class LevelOne {

    // Make them all private because the level is the one who will act on these variables.
    private Game game; // needs access to game controller to obtain variables such as ammo, hp etc.
    private BufferedImageLoader loader = new BufferedImageLoader();
    // Camera and handler.
    private Camera levelOneCamera;
    private Handler levelOneHandler;


    
    // The should be in an abstract/parent class. 
    private SpriteSheet ss = new SpriteSheet(loader.loadImage("/main_sheet.png")); // Who says we need to declare variables?
    private SpriteSheet cs = new SpriteSheet(loader.loadImage("/wizard_sheet.png")); // character sheet
    private BufferedImage levelOneImage = loader.loadImage("/level_one.png");

    private BufferedImage lives_image = cs.grabImage(13, 8, 32, 32); // Sprite to display lives.
    
    
    private BufferedImage floor = null;
    
    public LevelOne(Game game) {
        this.game = game;
        levelOneCamera = new Camera(0, 0); // and the camera
        this.levelOneHandler = new Handler(); // make sure the handler is started from new.

        loadLevel(levelOneImage); // now this shouldn't really load 60 times a second. So we will move this somewhere else // smart choice to put it her
    }

    private void loadLevel(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        for (int xx = 0; xx < w; xx++) {
            for (int yy = 0; yy < h; yy++) {
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                // Color map determines which sprites render to the map.
                if (red == 255 && green == 0 && blue == 0) // pure red
                    levelOneHandler.addObject(new Block(xx * 32, yy * 32, ID.Block, ss, this));

                if (red == 0 && green == 0 && blue == 255) // pure blue
                    levelOneHandler.addObject(new Wizard(xx * 32, yy * 32, ID.Player, levelOneHandler, this, cs));

                if (red == 0 && green == 255 && blue == 0) // pure green
                    levelOneHandler.addObject(new Minion(xx * 32, yy * 32, ID.Minion, levelOneHandler, cs));

                if (red == 0 && green == 255 && blue == 255) // pure cyan
                    levelOneHandler.addObject(new Crate(xx * 32, yy * 32, ID.Crate, ss));

                if (red == 255 && green == 255 && blue == 0) // pure yellow
                    levelOneHandler.addObject(new Totem(xx * 32, yy * 32, ID.Totem, ss));

                if (red == 255 && green == 0 && blue == 255) //pure magenta
                    levelOneHandler.addObject(new Knight(xx * 32, yy * 32, ID.Knight, levelOneHandler, this, cs));

                if (red == 0 && green == 153 && blue == 102) // # 009966 green
                    levelOneHandler.addObject(new Ent(xx * 32, yy * 32, ID.Ent, levelOneHandler, this, cs)); //

                if (red == 255 && green == 153 && blue == 51) // # ff9933 vivid orange
                    levelOneHandler.addObject(new Hound(xx * 32, yy * 32, ID.Hound, levelOneHandler, cs));
            }
        }
    }

    public void tick() {
        // Change this to a switch statement, every different case has it's own tick. 
        // Moves objects to next position 60 times a second.
        for(int i = 0; i < this.levelOneHandler.getObject().size(); i++) {
                if(this.levelOneHandler.getObject().get(i).getId() == ID.Player) {
                    levelOneCamera.tick(this.levelOneHandler.getObject().get(i));
                }
            }
        levelOneHandler.tick();
    } // end tick method

    public void render(Graphics2D g) {



        ///////////this is where we draw Objects and Background to game///////////////////////

        // this will be a method in the abstract level class.


        // Create background.
        g.setColor(Color.black);
        g.fillRect(0, 0, 1920, 1080);

        // Translate location of camera.
        g.translate(-levelOneCamera.getX(), -levelOneCamera.getY());

        for(int xx = 0; xx < 30*92; xx += 32) { //debug
            for(int yy = 0; yy < 30*72; yy += 32) {
                g.drawImage(floor, xx, yy, null);
            }
        }

        // Render all GameObjects
        levelOneHandler.render(g);

        // Keep translating camera as coords change. (Follow player)
        g.translate(levelOneCamera.getX(), levelOneCamera.getY());

        // HUD: Creating health bar.
        g.setColor(Color.gray);
        g.fillRect(5, 5, 200, 32);
        g.setColor(Color.red);
        g.fillRect(5, 5, this.getHp()*2, 32);
        g.setColor(Color.black);
        g.drawRect(5, 5, 200, 32);
        g.setColor(Color.white);
        g.drawString("HEALTH", 5, 25);

        // Creating ammo HUD.
        g.setColor(Color.white);
        g.drawString("Ammo: " + this.getAmmo(), 5, 50);

        // Creating level HUD.
        g.setColor(Color.white);
        g.drawString("Level: " + 1, 5, 70); // Change to level numb variable

        // Creating escape button.
        g.setColor(Color.gray);
        g.fillRect(250, 5, 75, 25);
        g.setColor(Color.gray);
        g.fillRect(250, 5, 75, 25);
        g.setColor(Color.gray);
        g.drawRect(250, 5, 75, 25);
        g.setColor(Color.black);
        g.drawString("|| PAUSE", 265, 25);


        // Creating lives HUD.
        // for the amount of lives render an image.
        int x = 100; // Create x coordinate
        for (int i = this.getLives(); i > 0; i--) {
            g.drawImage(lives_image, x, 35, null);
            x += 20;
        }

        // Handle player death event. (See wizard class)
        if(this.getHp() <= 0) {
            // The player has died, loses a life and the player is removed from game, then....
            g.setColor(Color.white);
            g.drawString("You have died!", 400, 281);
            g.drawString("Click to respawn", 810, 150);


            Rectangle resButton = new Rectangle(810, 150, 150, 75); //reset button.
            g.draw(resButton);

            levelOneCamera.setX(0); //reset camera so button coordinates don't glitch.
            levelOneCamera.setY(0);
        }

        if(this.getLives() <= 0) {
            // Handle game over event. (Also done through wizard class)
            g.setColor(Color.white);
            g.drawString("Game Over!", 400, 281);

            game.setGameState(Game.STATE.MENU); // this is how it's done.
            resetLevel();
            // Go back to the menu.

            // Brief explanation, basically this is refreshing lives so we don't end up back here,
            // Starting a new game, then killing this thread. If you don't stop this thread.
            // You're gonna have a bad time....
            // Doesn't need to be here. This should be done in it's own class.
            //stop();
            //lives = 3;
            //new Game(1, lives);
        }

        // Level functionality (possibly refactor into new function)
        //if(totem_flag == true) {
                /* If the public level_numb variable has incremented
                Then kill the thread and start a new game loading a new level.
                If you don't kill the thread with stop(); You're not going to like what happens...
                Every level will be a new thread. Layering on top of the window.
                 */
            //stop();
           // new Game(level_numb, lives);
        } // end render

    public void resetLevel() {
        // Resets hp, lives and reloads level.
        game.setHp(100);
        game.setLives(3);
        levelOneHandler.clearHandler();
        loadLevel(levelImage);
    }

    public int getHp(){
        return game.getHp();
    }
    
    public void decHp(){
        game.decHp();
    }
    
    public int getLives() {
        return game.getLives();
    }
    
    public void decLives() {
        game.decLives();
    }

    public int getAmmo() {
        return game.getAmmo();
    }
    public void incAmmo(int inc) {
        game.incAmmo(inc);
    }

    public Handler getHandler(){
        return levelOneHandler;
    }

    public Camera getCamera() {
        return levelOneCamera;
    }
}
