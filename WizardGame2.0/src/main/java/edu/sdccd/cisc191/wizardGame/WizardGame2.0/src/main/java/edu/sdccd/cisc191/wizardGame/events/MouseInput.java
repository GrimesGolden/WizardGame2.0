package edu.sdccd.cisc191.wizardGame.events;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.sdccd.cisc191.wizardGame.Game;
import edu.sdccd.cisc191.wizardGame.gui.anim.Camera;
import edu.sdccd.cisc191.wizardGame.gui.screen.Window;
import edu.sdccd.cisc191.wizardGame.objects.Bullet;
import edu.sdccd.cisc191.wizardGame.objects.GameObject;
import edu.sdccd.cisc191.wizardGame.objects.Handler;
import edu.sdccd.cisc191.wizardGame.objects.ID;
import edu.sdccd.cisc191.wizardGame.objects.Wizard;
import edu.sdccd.cisc191.wizardGame.utils.images.SpriteSheet;

/* Note about whats happening here as it's extremely convoluted - Jordan,
so we notice that in the Menu buttons are drawn as seen below playButton etc, we now check to see if the points of the mouse click event
denoted with variables mx and my are within the boundaries of that button, if that is true and we are in the menu state, then switch to game.
The reason to check if we are in menu state first, is otherwise a mouse click will restart the game during the actual game, not good since we click to
shoot ammo. This is not a good technique simply to create a menu button but it does work. The use of states is definately the way to go, but it will require refactoring.
 */

//n the mouse pressed event, put all the button checks within a if statement that checks if Game.State == Game.STATE.MENU

public class MouseInput extends MouseAdapter {

    // These buttons are here so we can check for their size values and calculate clicks. Why are these public...good question.
    public Rectangle resButton = new Rectangle(810, 150, 150, 75);
    public Rectangle playButton = new Rectangle(810, 150, 150, 75);
    public Rectangle helpButton = new Rectangle(810, 450, 150, 75);
    public Rectangle quitButton = new Rectangle(810, 750, 150, 75);
    public Rectangle escButton = new Rectangle(250, 5, 75, 25);

    private Handler handler;
    private Camera camera;
    private Game game;
    private SpriteSheet ss;
    private SpriteSheet cs; //Needs two sprite sheets, that's the reason I would like to just have one big sprite sheet.

    public MouseInput (Handler handler, Camera camera, Game game, SpriteSheet ss, SpriteSheet cs) {
        this.handler = handler;
        this.camera = camera;
        this.game = game;
        this.ss = ss;
        this.cs = cs;
    }

    public void mousePressed(MouseEvent e) {
        // Current mouse click and camera location (players location)
        int mx = (int) (e.getX() + camera.getX());
        int my = (int) (e.getY() + camera.getY());

        // Raw coordinates no camera, use for menu, buttons etc.
        int x = e.getX();
        int y = e.getY();

        if (game.getGameState() == Game.STATE.LEVEL_1) {

            for(int i = 0; i < handler.getObject().size(); i++) {
                GameObject tempObject = handler.getObject().get(i);

                if(tempObject.getId() == ID.Player && game.getAmmo() >= 1) {
                    // Fire bullet from player location
                    handler.addObject(new Bullet(tempObject.getX()+16, tempObject.getY()+24, ID.Bullet, handler, mx, my, ss));
                    game.setAmmo(100); // actually should decrement ammo
                }
            } // end object for loop.

            if(resButton.contains(mx, my) && game.getHp() <= 0) {
                game.setHp(100);
                System.out.println("here I am");
                game.respawn();
                System.out.println("line 72 added a wizard");
            }

            if(escButton.contains(x, y)) {
                // Pauses the game when pause button is clicked durinf game.
                game.setGameState(Game.STATE.PAUSE);
            }

        }

        else if (game.getGameState() == Game.STATE.MENU) {
            if(playButton.contains(x, y)) {
                game.setGameState(Game.STATE.LEVEL_1);
            }

            else if(helpButton.contains(x, y)) {
                game.setGameState(Game.STATE.HELP);
            }

            else if(quitButton.contains(x,y)) {
                Window.quitGame();
            }
        }

        else if (game.getGameState()== Game.STATE.PAUSE) {
            if(playButton.contains(x, y)) {
                game.setGameState(Game.STATE.GAME);
            }

            else if(quitButton.contains(x,y)) {
                Window.quitGame();
            }
        }// end PAUSE state (refactor)

        else if (game.getGameState()== Game.STATE.HELP) {
            if(quitButton.contains(x,y)) {
                game.setGameState(Game.STATE.MENU);
            }
        } // End HELP state
    }
} // end class
