package net.sf.bloodball.model;

public class GameComponents {
	private Ball ball;
	private Field field;
	private Teams teams;
	private Game game;
	
	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public GameComponents(Game game) {
		this.game = game;
		instantiateGameComponents();
	}

	public Ball getBall() {
		return ball;
	}

	public Field getField() {
		return field;
	}

	public Teams getTeams() {
		return teams;
	}

	private void instantiateGameComponents() {
		teams = new Teams(game);
		field = new Field();
		ball = new Ball(field);
	}
}