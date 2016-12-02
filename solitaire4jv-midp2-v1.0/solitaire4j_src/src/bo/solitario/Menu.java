package bo.solitario;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class Menu extends List implements CommandListener {

	private Display display;
	private Displayable back;
	private MenuListener listener;

	public Menu(Display display, MenuListener listener) {

		super("Menu", List.IMPLICIT);

		this.display = display;
		this.listener = listener;

		// this.append("Preferencias",null);
		this.append("Volver al juego", null);
		this.append("Salir", null);

		this.setCommandListener(this);
	}

	public void show() {
		if (display != null) {
			back = display.getCurrent();
			display.setCurrent(this);
		}
	}

	public void commandAction(Command arg0, Displayable arg1) {

		int index = this.getSelectedIndex();

		switch (index) {
		case 0: {
			display.setCurrent(back);
		}
			break;
		case 1: {
			listener.commandAction(arg0, arg1);
		}
			break;

		}
	}
}
