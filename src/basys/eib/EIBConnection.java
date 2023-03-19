/*
 * BBBBBB     AAAAA     SSSSS  YY      YY   SSSSS    22222    00000    00000   33333
 * BB   BB   AA   AA   SS       YY    YY   SS       22   22  00   00  00   00 33   33
 * BB   BB   AA   AA    SS       YY  YY     SS          22   00   00  00   00      33
 * BBBBBB    AA   AA     SS       YYYY       SS        22    00   00  00   00    333
 * BB   BB   AAAAAAA      SS       YY         SS      22     00   00  00   00      33
 * BB   BB   AA   AA       SS      YY          SS    22      00   00  00   00 33   33
 * BBBBBB    AA   AA   SSSSS       YY      SSSSS     222222   00000    00000   33333
 * 
 * http://www.basys2003.org
 *   
 * Copyright (c) 2003, 2004, BASys 2003 Project, 
 *                           Vienna University of Technology, 
 *                           Department of Automation - Automation Systems group,
 *                           Oliver Alt 
 * 
 *                     2006, Interface individual address getter added (for KNXnet/IP)
 *                           B. Malinowsky, G. Neugschwandtner
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *	- Redistributions of source code must retain the above copyright notice, this list
 *    of conditions and the following disclaimer.
 *
 *  - Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 
 *  - Neither the name of the BASys 2003 Project nor the names of its contributors 
 *    may be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH 
 * DAMAGE.
 */
package basys.eib;

import java.util.*;

import basys.eib.event.EIBFrameEvent;
import basys.eib.exceptions.EIBConnectionNotPossibleException;

/**
 * EIBConnection.java
 * 
 * @author 	oalt
 * @version	$Id: EIBConnection.java,v 1.2 2006/09/14 13:42:41 fritzchen Exp $
 */
public abstract class EIBConnection
{
	protected MyEventHandler eh;
	
	protected Vector listeners;
	protected Vector frames;
	protected EIBPhaddress individualAddress;
	private boolean connected = false;

	public abstract String getConnectionType();

	public abstract void sendEIBFrame(EIBFrame ef);
	public abstract void connect() throws EIBConnectionNotPossibleException;
	public abstract void disconnect();

	/**
	 * Constructor for EIBConnection.
	 */
	public EIBConnection()
	
	{
		super();
		this.listeners = new Vector();
		this.frames = new Vector();
	}

	/**
	* Call this method to add an EIBframeListener for receiving a frame
	*
	* @param efl the EIBFrameListener
	*/
	public void addEIBFrameListener(EIBFrameListener efl)
	{
		for (int cnt = 0; cnt < listeners.size(); cnt++)
		{
			if ((EIBFrameListener) listeners.get(cnt) == efl)
			{
				return;
			}
		}
		listeners.addElement(efl);
	}

	/**
	 * Returns the individual (physical) address of the EIB interface
	 * 
	 * @return the individual (physical) address of the EIB interface
	 */
	public EIBPhaddress getIndividualAddress()
	{	
		return this.individualAddress;
	}
	
	public boolean isConnected()
	{
		return this.connected;
	}

	public void setConnected(boolean connected)
	{
		this.connected = connected;
	}

	/**
	 * Remove a EIBFrameListener
	 *
	 * @param efl the instance of the Listener you want to remove
	 */
	public void removeEIBFrameListener(EIBFrameListener efl)
	{

		for (int cnt = 0; cnt < listeners.size(); cnt++)
		{
			if ((EIBFrameListener) listeners.get(cnt) == efl)
			{
				listeners.remove(cnt);
			}
		}

	}

}

/**
 * Event handler thread for EIBGatewayConnection
 */
class MyEventHandler extends Thread
{

	private Vector frames;
	private Vector listeners;

	private boolean goOn = true;

	public MyEventHandler(Vector frames, Vector listeners)
	{
		this.frames = frames;
		this.listeners = listeners;
	}

	public void stopEventHandling()
	{
		this.goOn = false;
	}

	public void run()
	{
		while (goOn)
		{
			synchronized (this)
			{
				while (frames.size() == 0)
				{
					try
					{
						wait(100);
					}
					catch (InterruptedException ie)
					{

					}
				}
			}
			if (frames.size() >= 1)
			{

				int[] frame = (int[]) frames.elementAt(0);
				
				for(int cnt=0; cnt<frame.length; cnt++)  {
				  System.out.print(Integer.toHexString(frame[cnt]).toUpperCase()+" ");
				
				}
				System.out.println();
				

				EIBFrame rf = new EIBFrame((int[]) frames.elementAt(0));

				frames.removeElementAt(0);

				if (listeners.size() >= 1)
				{
					EIBFrameEvent efe = new EIBFrameEvent(this, rf);
					for (Enumeration el = listeners.elements(); el.hasMoreElements();)
					{
						((EIBFrameListener) el.nextElement()).frameReceived(efe);
					}

				}

			} // if

		} // while(true)

	}
}
