package com.game.game;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestBaseBoard.class, TestTicTacToeGame.class, TestConnectFourBoard.class })
public class AllTests {

}
