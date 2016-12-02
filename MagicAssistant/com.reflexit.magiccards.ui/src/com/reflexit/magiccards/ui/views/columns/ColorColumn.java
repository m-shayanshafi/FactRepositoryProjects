package com.reflexit.magiccards.ui.views.columns;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Listener;

import com.reflexit.magiccards.core.model.Colors;
import com.reflexit.magiccards.core.model.IMagicCard;
import com.reflexit.magiccards.core.model.MagicCardField;
import com.reflexit.magiccards.ui.utils.SymbolConverter;

public class ColorColumn extends AbstractImageColumn implements Listener {
	public ColorColumn() {
		super(MagicCardField.COLOR, "Color");
	}

	@Override
	public String getText(Object element) {
		String text = super.getText(element);
		if (text.length() == 0)
			return text;
		return Colors.getColorName(text);
	}

	@Override
	public Image getActualImage(Object element) {
		if (element instanceof IMagicCard) {
			String icost = Colors.getInstance().getColorAsCost((IMagicCard) element);
			return SymbolConverter.buildCostImage(icost);
		}
		return null;
	}
}
