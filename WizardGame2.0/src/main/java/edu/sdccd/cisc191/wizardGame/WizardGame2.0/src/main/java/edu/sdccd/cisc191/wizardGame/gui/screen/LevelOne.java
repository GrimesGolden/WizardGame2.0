package edu.sdccd.cisc191.wizardGame.gui.screen;

import edu.sdccd.cisc191.wizardGame.Game;


public class LevelOne extends AbstractLevel {

    public LevelOne(Game game) { // Level will take the game controller, then pass it to the abstract super. Pretty confusing
        super(game);

        floor = floorOne;



        loadLevel(levelOneImage);

    }
}

