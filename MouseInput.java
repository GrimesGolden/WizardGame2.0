import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* Note about whats happening here as it's extremely convoluted - Jordan,
so we notice that in the Menu buttons are drawn as seen below playButton etc, we now check to see if the points of the mouse click event
denoted with variables mx and my are within the boundaries of that button, if that is true and we are in the menu state, then switch to game.
The reason to check if we are in menu state first, is otherwise a mouse click will restart the game during the actual game, not good since we click to
shoot ammo. This is not a good technique simply to create a menu button but it does work. The use of states is definately the way to go, but it will require refactoring.
 */

//n the mouse pressed event, put all the button checks within a if statement that checks if Game.State == Game.STATE.MENU

public class MouseInput extends MouseAdapter {

    // These buttons are here so we can check for their size values and calculate clicks.
    public Rectangle playButton = new Rectangle(810, 150, 150, 75);
    public Rectangle helpButton = new Rectangle(810, 450, 150, 75);
    public Rectangle quitButton = new Rectangle(810, 750, 150, 75);

    private Handler handler;
    private Camera camera;
    private Game game;
    private SpriteSheet ss;

    public MouseInput (Handler handler, Camera camera, Game game, SpriteSheet ss) {
        this.handler = handler;
        this.camera = camera;
        this.game = game;
        this.ss = ss;
    }

    public void mousePressed(MouseEvent e) {
        int mx = (int) (e.getX() + camera.getX());
        int my = (int) (e.getY() + camera.getY());

        if (Game.State == Game.STATE.GAME) {
            for(int i = 0; i < handler.object.size(); i++) {
                GameObject tempObject = handler.object.get(i);

                if(tempObject.getId() == ID.Player && game.ammo >= 1) {
                    handler.addObject(new Bullet(tempObject.getX()+16, tempObject.getY()+24, ID.Bullet, handler, mx, my, ss));
                    game.ammo--;
                }
            }
        } else if (Game.State == Game.STATE.MENU) {
            if(playButton.contains(mx, my)) {
                Game.State = Game.STATE.GAME;
            } // end if
        } // end else if (god what a mess).
    }
}

