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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import basys.eib.exceptions.EIBConnectionNotPossibleException;

/**
 * EIBLinuxBCU1Connection.java
 * 
 * EIBConnection to send EIB data via EIB4Linux device driver.
 * 
 * @author  olli
 * @version $Id: EIBLinuxBCU1Connection.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public class EIBLinuxBCU1Connection extends EIBConnection
{
	private static Logger logger = Logger.getLogger(EIBLinuxBCU1Connection.class);
	
	private static final String EIBDEVICE = "/dev/eib";
	
	private Thread dispatch=null;
	
	public static final int TIMEOUT = 25;
	
	private static EIBConnection connection=null;
	
	private FileWriter eibWriter;
	private FileReader eibReader;
	
	private byte con[] = new byte[32];
	private byte valueResponse[] = new byte[32];
	
	/**
	 * Constructor 
	 */
	private EIBLinuxBCU1Connection()
	{
		super();
	}

	/**
	 * Singelton for EIBLinuxBCU1Connection.
	 * 
	 * @return an instance of that class.
	 */
	public static EIBConnection getEIBConnection() 
	{
		if(connection==null)
		{	
			connection = new EIBLinuxBCU1Connection();	
			connection.setConnected(false);	
		}
		logger.debug("Linux EIB connection requested.");
		return connection;	
	}
	
	/**
	 * @see basys.eib.EIBConnection#getConnectionType()
	 */
	public String getConnectionType()
	{
		return "Linux BCU1 Connection";
	}

	/**
	 * @see basys.eib.EIBConnection#sendEIBFrame(basys.eib.EIBFrame)
	 */
	public void sendEIBFrame(EIBFrame ef)
	{
		char out[] = new char[32];
		int framedata[]=ef.getFrame();
		
		int len=framedata.length+1;
		
		if(!this.isConnected())
		{
			return;
		}
		
		out[0]=0x11; // L_Data.req
		for(int i=0; i<framedata.length; i++)
		{
			out[i+1]=(char)framedata[i];
		}
		
		
		con[0] = 0;
		try
		{
			eibWriter.write(out, 0, 8 + len);
			eibWriter.flush();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		for (int i = 0;(con[0] == 0) && (i < TIMEOUT); i++)
		{
			try
			{
				Thread.currentThread().sleep(20);
			}
			catch (Exception e)
			{
			}
		}

		if (con[0] != 0x4e)
		{
			//throw new IOException("timeout L_Data.con message in writeGroupData");
		}
		else
		{
			for (int i = 4; i < 7 + len; i++)
			{
				if (con[i] != (byte) out[i])
				{
					//throw new IOException("error in L_Data.con message");
				}
			}
			return;
		}

	}

	/**
	 * @see basys.eib.EIBConnection#connect()
	 */
	public void connect() throws EIBConnectionNotPossibleException
	{
		if(this.isConnected())
		{
			logger.debug("BCU1 Buscommunicator already opened");
			return;
		}
		else
		{
			try
			{
				eibWriter = new FileWriter(EIBDEVICE);
				eibReader = new FileReader(EIBDEVICE);

				char[] data = new char[] { 0x46, 0x01, 0x00, 0x60, 0x12 }; // System State, Link-Layer
				eibWriter.write(data, 0, 5);
				eibWriter.flush();
				data = new char[] { 0x46, 0x01, 0x01, 0x16, 0x00 }; // AdrTab, length=0, all group-addresses accepted
				eibWriter.write(data, 0, 5);
				eibWriter.flush();
				this.setConnected(true);
				/*
				eventListenerThread = new Thread(new EventListenerThread());
				eventListenerThread.start();
				sendMessage(
					BusCommunicatorStatusEvent.STATUS_CONNECTED,
				*/
				this.eh=new MyEventHandler(super.frames, super.listeners);
				this.eh.start();
				
				this.dispatch=new Thread(new EIBDataDispatcher());
				this.dispatch.start();
				logger.debug("LinuxBCU1EIBBusCommunicator successful opened");
				
			}
			catch (java.io.IOException e)
			{
				this.setConnected(false);
				logger.debug(e.toString());
				throw new EIBConnectionNotPossibleException(e.toString()+"open failed");
			}
		}

	}

	/**
	 * @see basys.eib.EIBConnection#disconnect()
	 */
	public void disconnect()
	{
		if (!this.isConnected())
		{
			return;
		}
		else
		{
			try
			{
				this.setConnected(false);

				con[0] = 0;
				char[] data = new char[] { 0x4b, 0x01, 0x00, 0x60 }; // PC_GET_VAL.req, System State
				eibWriter.write(data, 0, 4);
				eibWriter.flush();

				for (int i = 0; (con[0] == 0) && (i < TIMEOUT); i++)
				{
					try
					{
						Thread.currentThread().sleep(20);
					}
					catch (Exception e)
					{
					}
				}
				if (con[0] != 0x4b)
				{
					this.setConnected(true);
					throw new IOException("BCU1 close fail.");
				}
				else
				{
					dispatch = null;
					data = new char[] { 0x46, 0x01, 0x00, 0x60, 0xc0 }; // PC_SET_VAL.req, System State, Reset
					eibWriter.write(data, 0, 5);
					eibWriter.flush();
					eibWriter.close();
					eibWriter = null;
					eibReader.close();
					eibReader = null;
					/*
					sendMessage(
						BusCommunicatorStatusEvent.STATUS_DISCONNECTED,
					*/
					logger.debug("LinuxBCU1EIBBusCommunicator closed");
					
				}
			}
			catch (java.io.IOException e)
			{
				logger.debug("close failed");
			}
			finally
			{
				try
				{
					eibWriter.close();
					eibWriter = null;
					eibReader.close();
					eibReader = null;
					this.setConnected(false);
					dispatch=null;
					logger.debug("BCU1 Readers and Writers closed.");
				}
				catch(Exception e)
				{
					logger.debug("close failed in finally block.");
				}
			}
		}

	}
	
	/**
	 * Thread for receiving EIB data.
	 */
	class EIBDataDispatcher implements Runnable
	{
		int len=0;
		char[] cbuf = new char[32];
		int character;
		
		public void run()
		{
			logger.debug("Starting thread.");
			
			while(isConnected())
			{
				len=0;
				try
				{
					len = eibReader.read(cbuf);
					if (len > 32)
					{
						logger.debug("unallowed data block length (" + len + ")");
					}
					else if (len > 0)
					{
						logger.debug("BCU1 receiving data.");
						if (cbuf[0] == 0x49 || cbuf[0] == 0x4e || cbuf[0] == 0x4b)
						{
							// L_Data.ind, L_Data.con, PC_GET_VAL.con, PC_GET_VAL.con
							//byte[] eibFrameData;
							int eibdata[]=new int[len+1];
							int apci;

							if (cbuf[0] == 0x4e || cbuf[0] == 0x4b) 
							{ // L_Data.con, PC_GET_VAL.con
								
								for (int i = len - 1; i >= 0; i--)
								{
									con[i] = (byte) cbuf[i];
								}
							}
							if (cbuf[0] == 0x49 || cbuf[0] == 0x4e)
							{ // L_Data.ind, L_Data.con
								//logger.debug(""+(int)cbuf[1]);
								for(int i=0; i<len-1; i++)
								{
									eibdata[i]=cbuf[i+1];
								}
								
								frames.addElement(eibdata);
								String s = "";
								for (int i = 0; i < len; i++)
								{
									s += Integer.toHexString(cbuf[i]) + " ";
								}
								logger.debug("unexpected data block received (" + s + ")");
							}
						}
						else // len <= 0
						{
							String s = "";
							for (int i = 0; i < len; i++)
							{
								s += Integer.toHexString(cbuf[i]) + " ";
							}
							logger.debug("unexpected data block received (" + s + ")");
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.out.println("cdd");
				}
			} // while(opened)
		} // run()
	}

}