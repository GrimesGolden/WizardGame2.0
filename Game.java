import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    private boolean isRunning = false;
    private Thread thread;
    private Handler handler;
    private Camera camera;
    private SpriteSheet ss; // spritesheet
    private SpriteSheet cs; // character sheet

    private BufferedImage level = null;
    private BufferedImage sprite_sheet = null;
    private BufferedImage char_sheet = null; //char sheet
    private BufferedImage floor = null;

    // Ammo, hp, and frame must be public for rendering.
    public int ammo = 50;
    public int hp = 100;
    //public int updates;
    //public int frames;

    public Game() {
        new Window(1920,1080,"Wizard Game", this);
        start();

        // Don't create a new instance of this handler again, could cause problems.
        // Instead swap it into new classes, since the objects are inside this handler.
        handler = new Handler();
        camera = new Camera(0, 0);
        this.addKeyListener(new KeyInput(handler));


        BufferedImageLoader loader = new BufferedImageLoader();
        level = loader.loadImage("/debug.png");
        sprite_sheet = loader.loadImage("/main_sheet.png");
        char_sheet = loader.loadImage("/wizard_sheet.png"); //char sheet

        ss = new SpriteSheet(sprite_sheet);

        cs = new SpriteSheet(char_sheet); // character sheet

        floor = ss.grabImage(6, 6, 32, 32); // paint floor

        this.addMouseListener(new MouseInput(handler, camera, this, ss));

        loadLevel(level);
    }

    private void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        // Main gameloop thread.


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

        for(int i = 0; i < handler.object.size(); i++) {
            if(handler.object.get(i).getId() == ID.Player) {
                camera.tick(handler.object.get(i));
            }
        }

        handler.tick();

    }

    public void render() {
        // Renders everything in the game.
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            // Three in the chamber strat, preloads 3 frames for each render.
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;
        ///////////this is where we actually draw stuff to game///////////////////////

        // Create background.
        g.setColor(Color.red);
        g.fillRect(0, 0, 1920, 1080);

        // Translate location of camera.
        g2d.translate(-camera.getX(), -camera.getY());

        for(int xx = 0; xx < 30*92; xx += 32) { //debug
            for(int yy = 0; yy < 30*72; yy += 32) {
                g.drawImage(floor, xx, yy, null);
            }
        }

        // Render all GameObjects
        handler.render(g);

        // Keep translating camera as coords change. (Follow player)
        g2d.translate(camera.getX(), camera.getY());

        // HUD: Creating health bar.
        g.setColor(Color.gray);
        g.fillRect(5, 5, 200, 32);
        g.setColor(Color.red);
        g.fillRect(5, 5, hp*2, 32);
        g.setColor(Color.black);
        g.drawRect(5, 5, 200, 32);
        g.setColor(Color.white);
        g.drawString("HEALTH", 5, 25);

        // Creating ammo HUD.
        g.setColor(Color.white);
        g.drawString("Ammo: " + ammo, 5, 50);

        // Handle death
        if(hp <= 0) {
            g.setColor(Color.white);
            g.drawString("Game Over!", 400, 281);
        }
        //////////////////////////////////
        g.dispose();
        bs.show();


        }

        // Loading the level.
        private void loadLevel(BufferedImage image) {
            int w = image.getWidth();
            int h = image.getHeight();

            for(int xx = 0; xx < w; xx++) {
                for(int yy = 0; yy < h; yy++) {
                    int pixel = image.getRGB(xx, yy);
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = (pixel) & 0xff;

                    if(red == 255)
                        handler.addObject(new Block(xx*32, yy*32, ID.Block, ss));

                    if(blue == 255 && green == 0)
                        handler.addObject(new Wizard(xx*32, yy*32, ID.Player, handler, this, cs));

                    if(green == 255 && blue == 0)
                        handler.addObject(new Enemy(xx*32, yy*32, ID.Enemy, handler, cs));

                    if(green == 255 && blue == 255)
                        handler.addObject(new Crate(xx*32, yy*32, ID.Crate, ss));
                }
            }
    }

    public static void main(String[] args){
        new Game();
    }
}
