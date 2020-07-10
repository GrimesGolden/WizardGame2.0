import java.awt.*;
import java.awt.image.BufferedImage;

public class Block extends GameObject {

    private BufferedImage block_image;

    public Block(int x, int y, ID id, SpriteSheet ss) {
        super(x, y, id, ss);

        block_image = ss.grabImage(6, 9 ,32, 32); // paint block
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(block_image, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }
}
