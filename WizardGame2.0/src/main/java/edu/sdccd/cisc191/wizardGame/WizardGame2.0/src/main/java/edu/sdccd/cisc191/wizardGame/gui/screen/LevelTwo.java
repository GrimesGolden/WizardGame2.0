package edu.sdccd.cisc191.wizardGame.gui.screen;

import edu.sdccd.cisc191.wizardGame.Game;


public class LevelTwo extends AbstractLevel {

    public LevelTwo(Game game) {
        super(game);

        floor = floorTwo;

        loadLevel(levelTwoImage);
        game.Update();





    }
}
