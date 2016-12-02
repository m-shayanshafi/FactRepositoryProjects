/* Display3DAction.java
   
   Copyright (C) 2004  Yu Zhang

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the
   Free Software Foundation, Inc.,
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package soccer.client.action;

import soccer.client.view.j3d.FieldJ3D;
import java.awt.event.ActionEvent;

/**
 *
 */
public class Display3DResetAction extends AbstractClientAction
{
  public Display3DResetAction() 
  {
    super();
    putValue( NAME, "3D Display View Reset"  );
    //setAccelerator( KeyEvent.VK_G, Event.CTRL_MASK );
	setEnabled( true );

  }
  /**
   * The toggle was changed
   */
  public void actionPerformed(ActionEvent e)
  {

  	if(getSoccerMaster().isIn3D()) {
        ((FieldJ3D)(getSoccerMaster().arena3D)).viewReset();
  	}
  }

}
