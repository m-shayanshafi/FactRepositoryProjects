package bo.solitario;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

public class FormPreference extends Form {

	private static FormPreference singleton;

	public static FormPreference getFormPreference(MenuListener menu) {

		if (singleton == null) {
			singleton = new FormPreference(menu);

		}
		return singleton;
	}

	private FormPreference(final MenuListener menu) {

		super("Preferences");

		this.addCommand(new Command("Save", Command.OK, 0));
		this.addCommand(new Command("Cancel", Command.CANCEL, 0));

		this.append("Chance preferences");

		final TextField text = new TextField("Number rounds", String
				.valueOf(Preference.getNumberRound()), 2, TextField.NUMERIC);
		this.append(text);

		this.setCommandListener(new CommandListener() {

			public void commandAction(Command com, Displayable arg1) {
				if (com.getCommandType() == Command.OK) {
					Preference.setNumberRound(Integer
							.parseInt(text.getString()));
				}
				menu.commandAction(com, arg1);
				// Preference.show();
			}
		});
	}
}
