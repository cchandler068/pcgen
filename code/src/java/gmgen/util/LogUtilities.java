/*
 *  LogUtilities.java - Provides logging facilities for GMGen
 *  Copyright (C) 2003 Tod Milam
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *  Created on May 24, 2003
 */
package gmgen.util;

import java.util.ArrayList;
import java.util.List;

/**
 *  LogUtilities is the class used to log messages in gmgen. It provides access
 *  to a singleton instance that can be used by system classes as well as
 *  plugins.
 *
 *@author     Tod Milam
 *@since      GMGen 3.3
 */
public class LogUtilities implements LogReceiver
{
	private static LogUtilities singleton = null;
	private List<LogReceiver> receivers;
	private boolean logging;

	/**
	 *  The private constructor. Called by inst to create the singleton instance if
	 *  it doesn't already exist.
	 *
	 *@since    GMGen 3.3
	 */
	private LogUtilities()
	{
		logging = false;
		receivers = new ArrayList<LogReceiver>();
	}

	/**
	 *  Returns the singleton instance of the LogUtilties class.
	 *
	 *@return    LogUtilities the singleton instance of this class.
	 *@since     GMGen 3.3
	 */
	public static LogUtilities inst()
	{
		if (singleton == null)
		{
			singleton = new LogUtilities();
		}

		return singleton;
	}

	/**
	 * Turns on or off logging.
	 *
	 * @param loggingFlag - A boolean specifying to turn on/off logging.
	 */
	public void setLogging(boolean loggingFlag)
	{
		logging = loggingFlag;
	}

	/**
	 * Add a receiver that will be called when a message is to be logged.
	 *
	 * @param receiver The receiver to be called with new messages.
	 */
	public void addReceiver(LogReceiver receiver)
	{
		receivers.add(receiver);
	}

	/**
	 * Log a message associated with a specific owner - from LogReceiver
	 *
	 * @param  owner    The owning component of the message
	 * @param  message  The message to send
	 * @since        GMGen 3.3
	 */
    @Override
	public void logMessage(String owner, String message)
	{
		if (logging)
		{
			// send the message to all registered receivers
			for (LogReceiver rcvr : receivers)
			{
				rcvr.logMessage(owner, message);
			}
		}
	}

	/**
	 * Log a message without an owner - from LogReceiver
	 *
	 * @param  message  The message to send
	 * @since        GMGen 3.3
	 */
    @Override
	public void logMessage(String message)
	{
		if (logging)
		{
			for (LogReceiver rcvr : receivers)
			{
				rcvr.logMessage(message);
			}
		}
	}
}
