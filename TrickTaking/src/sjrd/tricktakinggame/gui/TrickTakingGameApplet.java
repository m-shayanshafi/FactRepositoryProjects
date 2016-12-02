/*
 * TrickTakingGame - Trick-taking games platform on-line
 * Copyright (C) 2008  Sébastien Doeraene
 * All Rights Reserved
 *
 * This file is part of TrickTakingGame.
 *
 * TrickTakingGame is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * TrickTakingGame is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * TrickTakingGame.  If not, see <http://www.gnu.org/licenses/>.
 */
package sjrd.tricktakinggame.gui;

import java.awt.*;

import javax.swing.*;

import sjrd.tricktakinggame.gui.connection.*;

/**
 * Applet pour joueur au jeu de cartes à plis dans une page Web
 * @author sjrd
 */
public class TrickTakingGameApplet extends JApplet
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() throws HeadlessException
	{
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				public void run()
				{
					new ConnectionFrame();
					add(new JLabel("Ne quittez pas cette page avant d'avoir " +
							"fini de joueur !"));
				}
			});
		}
		catch (Exception error)
		{
		}
	}
}
