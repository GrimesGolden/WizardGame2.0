import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Window {

    public boolean startGame = false;

    public Window(int width, int height, String title, Game game) {

        JFrame window;
        Container con;
        JPanel titleNamePanel, startButtonPanel;
        JLabel titleNameLabel;
        JFrame frame = new JFrame(title);
        Font titleFont = new Font("Times New Roman", Font.PLAIN, 72);
        Font normalFont = new Font ("Times New Roman", Font.PLAIN, 30);
        JButton startButton;

        //TitleScreenHandler tsHandler = new TitleScreenHandler();


        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setResizable((false));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.black);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        //frame.setVisible(true);
        con = frame.getContentPane();

        // Menu panel
        titleNamePanel = new JPanel();
        titleNamePanel.setBounds(540,100, 600, 150);
        titleNamePanel.setBackground(Color.black);
        titleNameLabel = new JLabel("Wizard Game");
        titleNameLabel.setForeground(Color.white);
        titleNameLabel.setFont(titleFont);

        startButtonPanel = new JPanel();
        startButtonPanel.setBounds(740, 400, 200, 100);
        startButtonPanel.setBackground(Color.black);

        startButton = new JButton("START");
        startButton.setBackground(Color.black);
        startButton.setForeground(Color.red);
        startButton.setFont(normalFont);
        //startButton.addActionListener(tsHandler);

        titleNamePanel.add(titleNameLabel);
        startButtonPanel.add(startButton);

        con.add(titleNamePanel);
        con.add(startButtonPanel);
        frame.setVisible(true);
    }

    //public class TitleScreenHandler implements ActionListener {

       // public void actionPerformed(ActionEvent event) {
            //
        //}