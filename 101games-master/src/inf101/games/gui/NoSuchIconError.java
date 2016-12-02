package inf101.games.gui;

public class NoSuchIconError extends RuntimeException {
	private static final long serialVersionUID = 2619557059193139982L;

	public NoSuchIconError(String name) {
		super(name);
	}
}
