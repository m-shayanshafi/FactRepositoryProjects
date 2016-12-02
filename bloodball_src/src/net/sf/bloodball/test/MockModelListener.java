package net.sf.bloodball.test;

import com.mockobjects.ExpectationSet;
import com.mockobjects.MockObject;
import java.awt.Point;
import java.util.Iterator;
import net.sf.bloodball.model.ModelListener;
import net.sf.bloodball.model.player.Team;

public class MockModelListener extends MockObject implements ModelListener {
	
	private ExpectationSet squareChangePositions = new ExpectationSet("squareContentChanges");

  public MockModelListener() {
  }

  public void gameEnded() {
  }

  public void touchdownScored() {
  }

  public void dugOutPositionActivated(Team team, int playerNumber) {
  }

  public void dugOutPositionDeactivated(Team team, int playerNumber) {
  }

  public void dugOutContentChanged(Team team, int playerNumber) {
  }

  public void squareContentChanged(Point position) {
  	squareChangePositions.addActual(position);
  }

  public void endTurnOperationChanged() {
  }

  public void inTurnOperationChanged() {
  }
  
  public void addExpectedContentChange(Point square) {
  	squareChangePositions.addExpected(square);
  }
  
  public void verify() {
  	super.verify();
  }

}
