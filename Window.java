import java.awt.Dimension;
import javax.swing.JFrame;

public class Window {

    protected static JFrame frame; // This is protected and static, so other classes can access the window for closing, resizing etc.

    public Window(int width, int height, String title, Game game) {

        frame = new JFrame(title);

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.add(game);
        frame.setResizable((false));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void quitGame(){
        // If quit game is activated, the window will close and the program will exit.
        frame.setVisible(false);
        frame.dispose();;
        System.exit(0);
    }
}
