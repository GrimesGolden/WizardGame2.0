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

public class LevelOne extends AbstractLevel {

    public LevelOne(Game game) { // Level will take the game controller, then pass it to the abstract super. Pretty confusing
        super(game);

        loadLevel(levelOneImage);





    }
}

