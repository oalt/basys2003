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
 *                     2006, Individual address setting moved here from parent class
 *                           G. Neugschwandtner
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

import basys.eib.event.*;
import basys.eib.exceptions.EIBConnectionNotPossibleException;

import java.net.*;

import org.apache.log4j.Logger;

/**
 * EIBGatewayConnection
 * This class realizes the functions for transmitting and receiving EIB frames
 * to and from the EIB Gateway
 *
 * @author  oalt
 * @version $Id: EIBGatewayConnection.java,v 1.3 2006/10/03 11:51:42 fritzchen Exp $
 *
 *
 */
public class EIBGatewayConnection extends EIBConnection
{

	private static Logger logger = Logger.getLogger(EIBGatewayConnection.class);

	private static EIBConnection con = null;

	private Receiver r;
	private Thread eh;
	
	private DatagramSocket socket;

	private static String gwaddress = "192.168.1.87";
	private static int port = 0xE1B0;

	private static int rip1 = 192;
	private static int rip2 = 168;
	private static int rip3 = 1;
	private static int rip4 = 1;
	
	/**
	 * Constructor
	 */
	private EIBGatewayConnection()
	{
           super();
           individualAddress = new EIBPhaddress(1,1,254);		
	}

	/** 
	 * Singelton for EIB Gateway Connection
	 * @return
	 */
	public static EIBConnection getEIBConnection()
	{
		if (con == null)
		{
			con = new EIBGatewayConnection();
		}
		return con;
	}

	public static void setParameter(String gwIP, int gwPort, int ip1, int ip2, int ip3, int ip4)
	{
		if (con == null)
		{
			gwaddress = gwIP;
			port = gwPort;
			rip1 = ip1;
			rip2 = ip2;
			rip3 = ip3;
			rip4 = ip4;
		}
	}
	
	/**
	 * 
	 * @see basys.eib.EIBConnection#getConnectionType()
	 */
	public String getConnectionType()
	{
		return "Gateway TU-Darmstadt";
	}

	/**
	 * Start the EIB gateway
	 *
	 * Give the IP of the telegram receiver as parameter !
	 */
	private int start(int ip1, int ip2, int ip3, int ip4)
	{
		int ok = 0;
		byte[] buf1 = new byte[5];

		buf1[0] = (byte) 0x01; // start GW identifier
		buf1[1] = (byte) ip1; // recv. IP
		buf1[2] = (byte) ip2;
		buf1[3] = (byte) ip3;
		buf1[4] = (byte) ip4;

		try
		{
			DatagramPacket p = new DatagramPacket(buf1, buf1.length, InetAddress.getByName(gwaddress), port);
			socket.send(p);
		}
		catch (Exception e)
		{
			logger.debug("Fehler bei start EIB GW. " + e);
			ok = 1;
		}
		return ok;
	}

	/**
	 * internal Gateway start routines
	 */
	private void startGW()
	{
		this.frames = new Vector();
		this.r = new Receiver(this.frames, this.socket);
		eh = new Thread(new EventHandler(super.frames, super.listeners));
		r.start();
		eh.start();
		logger.debug("EIB Gateway Connection established.");
	}

	/**
	 * Stop Gatway functions and free port
	 * Called by disconnect()
	 */
	private void stopGateway()
	{
		this.r.stopThread();
		//this.eh.stopEventHandling();
		this.socket.close();
		logger.debug("EIB Gateway Connection closed.");
	}

	/**
	 * Call this method to send an EIBFrame
	 *
	 * @param ef the EIB frame you want to send
	 */
	public void sendEIBFrame(EIBFrame ef)
	{

		int[] eif = ef.getFrame();
		byte[] buf1 = new byte[eif.length + 2];

		buf1[0] = (byte) 0x10;

		for (int cnt = 0; cnt < eif.length; cnt++)
		{
			buf1[cnt + 1] = (byte) eif[cnt];
		}
		buf1[buf1.length - 1] = (byte) ef.calcparity();
		try
		{
			DatagramPacket p = new DatagramPacket(buf1, buf1.length, InetAddress.getByName(gwaddress), port);

			socket.send(p);
		}
		catch (Exception e)
		{
			System.out.println("Fehler bei sendEIBframe " + e);
		}
	}

	/**
	 * @see basys.eib.EIBConnection#conect()
	 */
	public void connect() throws EIBConnectionNotPossibleException
	{

		if (!this.isConnected())
		{

			try // open socket
				{
				socket = new DatagramSocket(port);
			}
			catch (Exception e)
			{
				System.out.println(e + "1");
			}

			if (socket == null)
			{
				throw new EIBConnectionNotPossibleException("Port is busy.");
			}

			startGW();

			start(rip1, rip2, rip3, rip4);

			this.setConnected(true);

		}
	}

	/**
	 * @see basys.eib.EIBConnection#disconnect()
	 */
	public void disconnect()
	{
		if (this.isConnected())
		{
			this.stopGateway();
			this.setConnected(false);
		}

	}

}

/**
 * EIB Frame receiver thread
 */

class Receiver extends Thread
{

	private Vector frames;
	private DatagramSocket socket;

	private boolean goOn = true;

	/**
	 * Constructor
	 */
	public Receiver(Vector frames, DatagramSocket socket)
	{
		this.frames = frames;
		this.socket = socket;
	}

	public void stopThread()
	{
		this.goOn = false;
	}

	public void run()
	{

		byte[] buf = new byte[256];

		while (this.goOn)
		{
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try
			{
				socket.receive(packet);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				break;
			}

			int length = 256;
			if (buf[0] == 0x10)
			{
				length = (int) ((buf[6] & 0x0F) + 9);
				int[] ref = new int[length];
				for (int cnt = 0; cnt < length; cnt++)
				{
					ref[cnt] = (int) buf[cnt + 1] & 0xFF;
				}
				// EIBFrame rf=new EIBFrame(ref);
				// String ack=Integer.toHexString((ref[length-1] & 0xFF));
				// frame.textout.append(sf.toHexString()+" "+ack.toUpperCase()+"\n\r");

				frames.addElement(ref);

			}

		}
	}

}

/**
 * Event handler thread for EIBGatewayConnection
 */

class EventHandler extends Thread
{

	private Vector frames;
	private Vector listeners;

	private boolean goOn = true;

	public EventHandler(Vector frames, Vector listeners)
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
				/*
				for(int cnt=0; cnt<frame.length; cnt++)  {
				  System.out.print(Integer.toHexString(frame[cnt]).toUpperCase()+" ");
				
				}
				System.out.println();
				*/	

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
