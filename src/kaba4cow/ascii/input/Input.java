package kaba4cow.ascii.input;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

public class Input {

	private Input() {

	}

	public static String typeString(String string) {
		if (Keyboard.isKey(Keyboard.KEY_CONTROL_LEFT) && Keyboard.isKeyDown(Keyboard.KEY_V)) {
			string += getClipboardString();
		} else if (Keyboard.getLastTyped() != null) {
			char c = Keyboard.getLastTyped().getKeyChar();
			if (c == 0x08 && string.length() > 0)
				string = string.substring(0, string.length() - 1);
			else if (c >= 32 && c < 0x7F)
				string += c;
			Keyboard.resetLastTyped();
		}
		return string;
	}

	public static String getClipboardString() {
		try {
			String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			data = data.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
			return data;
		} catch (Exception e) {
			return "";
		}
	}

}
