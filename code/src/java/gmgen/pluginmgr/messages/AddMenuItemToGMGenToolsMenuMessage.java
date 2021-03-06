/*
 * AddMenuItemToGMGenToolsMenuMessage.java
 * Copyright James Dempsey, 2014
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on 16/02/2014 10:16:37 pm
 *
 * $Id$
 */
package gmgen.pluginmgr.messages;

import javax.swing.JMenuItem;

import pcgen.pluginmgr.PCGenMessage;

/**
 * The Class <code>AddMenuItemToGMGenToolsMenuMessage</code> encapsulates a 
 * request for a menu item to be shown in the GMGen tools menu.
 *
 * <br/>
 * Last Editor: $Author$
 * Last Edited: $Date$
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision$
 */
@SuppressWarnings("serial")
public class AddMenuItemToGMGenToolsMenuMessage extends PCGenMessage
{
	private final JMenuItem menuItem;

	/**
	 * Create a new instance of AddMenuItemToGMGenToolsMenuMessage
	 * @param source The object requesting the addition.
	 * @param menuItem  The menu item to be added.
	 */
	public AddMenuItemToGMGenToolsMenuMessage(Object source,
		JMenuItem menuItem)
	{
		super(source);
		this.menuItem = menuItem;
	}

	/**
	 * @return the menuItem
	 */
	public JMenuItem getMenuItem()
	{
		return menuItem;
	}

}
