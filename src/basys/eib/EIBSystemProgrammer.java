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

import java.util.Vector;

import org.apache.log4j.Logger;

import basys.eib.event.EIBFrameEvent;
import basys.eib.exceptions.*;

/**
 * EIBSystemProgrammer.java
 * 
 * This class handles the transport protocol and implements methods for accessing the EIB application 
 * layer.
 * 
 * @author 	oalt
 * @version	$Id: EIBSystemProgrammer.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public class EIBSystemProgrammer implements EIBFrameListener
{
	private static Logger logger=Logger.getLogger(EIBSystemProgrammer.class);
	
	private static final int CLOSED 		= 0;
	private static final int CONNECTING 	= 1;
	private static final int CONNECTED 		= 2;
	private static final int DISCONNECT		= 3; 
	private static final int WAIT			= 5;
	private static final int AWAIT_BUTTON   = 6;
	
	
	
	private EIBConnection con;
	private EIBPhaddress  srcAddr;
	private EIBPhaddress  destAddr;
	
	private int conState=CLOSED;
	
	private int localSeq=0;
	private int remoteSeq=0;
	
	private boolean read_access=false;
	private boolean write_access=false;
	
	private boolean send_request=false;
	private boolean receive_request_ack=false;
	private boolean receive_response=false;
	private boolean send_response_ack=false;
	private boolean disconnecting=false;
	
	private boolean ack=false;
	private boolean disconn=false;
	private boolean request=false;
	
	private int [] data;
	
	private Vector addresses;
	
	/**
	 * Constructor for EIBSystemProgrammer.
	 */
	public EIBSystemProgrammer(EIBConnection con, EIBPhaddress srcAddr, EIBPhaddress destAddr)
	{
		super();
		this.con=con;
		this.srcAddr=srcAddr;
		this.destAddr=destAddr;
		con.addEIBFrameListener(this);
	}
	
	/**
	 * Open connection oriented connection
	 * 
	 * @throws EIBConnectionAlreadyOpenException if connection is still open
	 * @throws EIBConnectionNotPossibleException if device does not ack the connection frame
	 */
	public void doConnect() throws EIBConnectionNotPossibleException, EIBConnectionAlreadyOpenException
	{
        if(this.conState==CONNECTED)
        {
        	throw new EIBConnectionAlreadyOpenException();	
        }
        
		EIBFrame f=new EIBFrame(false, EIBFrame.PRIORITY_SYSTEM, this.srcAddr, this.destAddr,
								7, 0x20, 0, null);
		
		this.con.sendEIBFrame(f);
		this.conState=CONNECTING;		
		int timeout=0;
		
		while(this.conState!=CONNECTED)
		{
			if(timeout > 15)
			{
				break;	
			}
			try
			{
				Thread.sleep(50); // Wait until frame is received and acknowledged
			}
			catch(InterruptedException ie)
			{
				
			}
			timeout++;
		}
		
		if(timeout == 16)
		{
			throw new EIBConnectionNotPossibleException("No connection to device "+this.destAddr.toString()+ "possible.");	
		}
		
	}
	
	/**
	 * Close Connection
	 */
	public void doDisconnect()
	{
		
		EIBFrame f=new EIBFrame(false, EIBFrame.PRIORITY_SYSTEM, this.srcAddr, this.destAddr,
								7, 0x20, 0x100, null);
		
		//logger.debug("disconnectiong... "+  f.toHexString());
		this.con.sendEIBFrame(f);
		this.conState=CLOSED;
		this.localSeq=0;
		this.remoteSeq=0;
		this.read_access=false;
		this.write_access=false;
		this.receive_request_ack=false;
		this.receive_response=false;
		
		this.disconn=true;
		while(this.disconn)
		{
			try
			{
				Thread.sleep(20);
			}
			catch(InterruptedException ie)
			{
			
			}
		}
		
	}
	
	/**
	 * 
	 */
	private void doAck(int seq)
	{
		EIBFrame fResAck = new EIBFrame(false, EIBFrame.PRIORITY_SYSTEM, this.srcAddr, this.destAddr,
									 	7, 0x30 | seq, 0x200, null);
		
		con.sendEIBFrame(fResAck);
		this.ack=true;
		while(this.ack)
		{
			try
			{
				Thread.sleep(20);
			}
			catch(InterruptedException ie)
			{
			
			}
		}	
	}
	
	/**
	 * Read mask version
	 */ 
	public int readMaskversion() throws EIBConnectionClosedException
	{
		if(this.conState!=CONNECTED)
		{
			throw new EIBConnectionClosedException("Connection closed.");	
		} 
	
		this.read_access=true;
		
		
		// read mask req
		EIBFrame freq = new EIBFrame(false, EIBFrame.PRIORITY_SYSTEM, this.srcAddr, this.destAddr,
									 7, 0x10 | this.localSeq, 0x300, new int[1]);
		
		con.sendEIBFrame(freq);
		
		this.request=true;
		this.send_request=true;
		while(this.request)
		{
			try
			{
				Thread.sleep(20);
			}
			catch(InterruptedException ie)
			{
			
			}
		}
			
		
		
		this.receiveData();
		
		if(data!=null)
		{	
			return data[2];	
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Read Memory Data
	 */
	public int[] readMemory(int memAddr, int length) throws EIBConnectionClosedException
	{
		if(this.conState!=CONNECTED)
		{
			throw new EIBConnectionClosedException("Connection closed.");	
		}
	
		this.read_access=true;
		
		int request[]=new int[3];
		
		request[0]=length & 0x0F;
		request[1]=(memAddr >> 8) & 0xFF;
		request[2]=memAddr & 0xFF;
		
		// read mask req
		EIBFrame freq = new EIBFrame(false, EIBFrame.PRIORITY_SYSTEM, this.srcAddr, this.destAddr,
									 7, 0x10 | this.localSeq, 0x200, request);
		
		//logger.debug("Read memory request frame: "+freq.toHexString());
		
		con.sendEIBFrame(freq);
		
		this.request=true;
		this.send_request=true;
		while(this.request)
		{
			try
			{
				Thread.sleep(10);
			}
			catch(InterruptedException ie)
			{
			
			}
		}
		
		
		
		this.receiveData();
			
		return data;
		
	}
	
	/**
	 * Call this method after sending the application data request frame
	 */
	private void receiveData()
	{
		
		int timeout=0;
	
		/*
		while(!this.receive_request_ack) // wait for ack for data request frame
		{
			
			if(timeout > 450)
			{
				break;	
			}
			
			try
			{
				Thread.sleep(10);	
			}
			catch(InterruptedException ie)
			{
				
			}
			timeout++;
		}
		if(timeout>450)
		{
			logger.debug("end receive ack timeout. "+timeout);
		}
		
		// check timeout here
		
		//logger.debug("end receive ack timeout. "+timeout); 
		*/
		
		timeout=0;
		while(!this.receive_response)
		{
			if(timeout > 450)
			{
				break;	
			}
			try
			{
				Thread.sleep(10);	
			}
			catch(InterruptedException ie)
			{
				
			}
			timeout++;
		}
		
		if(timeout>450)
		{
			logger.debug("end data receive timeout. "+timeout);
		}
		// check timeout here
		
		// Acknowledge response
		doAck(this.remoteSeq);
		
		this.remoteSeq=(this.remoteSeq+1)%16;
		/*
		try
		{
			Thread.sleep(10);
		}
		catch(InterruptedException ie)
		{
			
		}
		*/
		this.read_access=false;
	
	}
	
	/**
	 * Restart device service
	 */
	public void restartDevice() throws EIBConnectionClosedException
	{
		/*
		if(this.conState!=this.CONNECTED && this.conState!=AWAIT_BUTTON)
		{
			throw new EIBConnectionClosedException("Connection closed.");	
		}
		*/	
	
		EIBFrame f = new EIBFrame(false, EIBFrame.PRIORITY_SYSTEM, this.srcAddr, this.destAddr,
								  7, 0x10 | this.localSeq, 0x380, new int[1]);
		
		this.con.sendEIBFrame(f);
		
		try
		{						  
			Thread.sleep(300);
		}
		catch(InterruptedException ie)
		{
		}
		this.conState=CLOSED;
	
	}
	
	/**
	 * Write Memory data
	 */
	public void writeMemory(int memAddr, int[] data) throws EIBConnectionClosedException
	{
		if(this.conState!=CONNECTED)
		{
			throw new EIBConnectionClosedException("Connection closed.");	
		}
		this.write_access=true;
		
		int[] request=new int[data.length+3];
		request[0]=data.length & 0x0F;
		request[1]=(memAddr>>8) & 0xFF;
		request[2]=memAddr & 0xFF;
		for(int cnt=0; cnt < data.length; cnt++)
		{
			request[cnt+3]=data[cnt];	
		}
		
		EIBFrame freq = new EIBFrame(false, EIBFrame.PRIORITY_SYSTEM, this.srcAddr, this.destAddr,
									 7, 0x10 | this.localSeq, 0x280, request);
									 
		this.con.sendEIBFrame(freq);
		this.send_request=true;
		
		int timeout=0;
		while(!this.receive_request_ack || this.send_request)
		{
			if(timeout > 450)
			{
				break;	
			}
			try
			{
				Thread.sleep(10);	
			}
			catch(InterruptedException ie)
			{
				
			}
			timeout++;
		}
		if(timeout>450)
		{
			System.out.println("Timeout !!");	
		}
		this.receive_request_ack=false;
		this.write_access=false;
		
	}
	
	/**
	 * Write Memory and verify written data by reading and comparing the written data.
	 * 
	 * @param memAddr write start memory address
	 * @param data the data to be written (length <= 12 !!)
	 * @throws EIBMemoryVerifyException thrown if verify fails
	 * @throws EIBConnectionClosedException thrown if the EIB connection is closed.
	 */
	public void writeAndVerifyMemory(int memAddr, int[] data) throws EIBMemoryVerifyException,
																	 EIBConnectionClosedException
	{
		try
		{
			this.writeMemory(memAddr, data);
			int[] read=this.readMemory(memAddr, data.length);
			for(int cnt=0; cnt<data.length; cnt++)
			{
				if(read[cnt+3]!=data[cnt])
				{
					throw new EIBMemoryVerifyException("EIB Memory verify failed. Data: "+data[cnt]+", Read: "+read[cnt]);
				}
			}
		}
		catch(EIBConnectionClosedException cce)
		{
			throw new EIBConnectionClosedException("Connection to EIB closed !");
		}
		 
	}
	
	public void writePhysicalAddress() throws EIBAddressInUseException,
											  EIBMultipleDeviceSelectionException,
											  NoProgrammingButtonPressedException
	{
		int cnt=0;
		
		String state="NOT EXISTEND";
		
		while(cnt<3)
		{
			
			try
			{
				this.doConnect();
				this.readMaskversion();
				this.doDisconnect();
				state="EXISTING";
				break;
			}
			catch(EIBConnectionNotPossibleException npe)
			{
				cnt++;		
			}
			catch(EIBConnectionAlreadyOpenException aoe)
			{
				aoe.printStackTrace();
			}
			catch(EIBConnectionClosedException cce)
			{
				cce.printStackTrace();
			}
		}
		if(!state.equals("EXISTING"))
		{
			this.doDisconnect();
		}
		
		this.addresses=new Vector();
		EIBFrame frame = new EIBFrame(false, EIBFrame.PRIORITY_SYSTEM, this.srcAddr, new EIBGrpaddress(0,0),
									  7, 0, 0x100, new int[1]);
		
		this.conState=AWAIT_BUTTON;
		int timeout=20;
		while(timeout > 0)
		{
			this.con.sendEIBFrame(frame);
			
			try
			{
				Thread.sleep(500);
			}
			catch(InterruptedException ie)
			{
			}
			
			if(!this.addresses.isEmpty())
			{
				break;
			}
			timeout--;
		}
		
		if(timeout<=0)
		{
			throw new NoProgrammingButtonPressedException("No programming button pressed in 10 seconds.");
		}
		
		if(addresses.size()>1)
		{
			throw new EIBMultipleDeviceSelectionException("More than one programming button pressed !");
		}
		if(addresses.size()==1)
		{
			EIBPhaddress pa=(EIBPhaddress)this.addresses.firstElement();
			if(!pa.equals(this.destAddr) && state.equals("EXISTING"))
			{
				throw new EIBAddressInUseException("Address already in use.");
			}
			else if(!state.equals("EXISTING"))
			{
				int[] data=new int[3];
				data[1]=this.destAddr.getHigh();
				data[2]=this.destAddr.getLow();
				EIBFrame setAdrFrame=new EIBFrame(false, EIBFrame.PRIORITY_SYSTEM, this.srcAddr, new EIBGrpaddress(0,0),
									  			  7, 0, 0xC0, data);
			
				this.con.sendEIBFrame(setAdrFrame);
				
				try
				{
					Thread.sleep(200);
					
					this.conState=CLOSED;
					
					this.doConnect();
					
					Thread.sleep(200);
					
					this.readMaskversion();
					
					
					this.restartDevice();
					
				}
				catch(Exception cce)
				{
					cce.printStackTrace();
				}
			}
			else // device is programmed and selected
			{
				try
				{
					Thread.sleep(200);
					
					this.conState=CLOSED;
					
					this.doConnect();
					
					Thread.sleep(200);
					
					this.readMaskversion();
					
					this.restartDevice();
					this.conState=CLOSED;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				return;
			}
			
		}
		
	}
	
	
	/**
	 * Event handler for incomming frames
	 */	
	public void frameReceived(EIBFrameEvent e)
	{
		EIBFrame f=e.getEIBFrame();
		// Frame from me
		if(f.getDestAddress().equals(this.destAddr) && f.getSourceAddress().equals(this.srcAddr)) 
		{
			if(this.conState==CONNECTING)
			{
				if(f.getAck()==0xCC || 
				   this.con.getConnectionType().equals("Linux BCU1 Connection"))
				{
					this.conState=CONNECTED;	
					this.localSeq=0;
				}
					
			}
			if(this.ack)
			{
				this.ack=false;	
			}
			if(this.request)
			{
				this.request=false;	
			}
			if(this.disconn)
			{
				this.disconn=false;	
			}
		}
		// Frame for me
		else if(f.getDestAddress().equals(this.srcAddr) && f.getSourceAddress().equals(this.destAddr))
		{
			if(read_access)
			{
				if(this.send_request)
				{
					if(f.getTPCI()==(0x30 | this.localSeq) && (f.getAPCI()>>8)==2) // ack frame
					{
						this.send_request=false;
						this.localSeq=(this.localSeq+1)%16;	
						
						this.receive_request_ack=true;
						
						//logger.debug("receive request ack.");
						
						return;
						
					}	
				}
				else if(this.receive_request_ack)
				{
					if(f.getTPCI()==(0x10 | this.remoteSeq)) // response frame
					{
						this.receive_request_ack=false;	
						this.data=f.getApdata();
						this.receive_response=true;
						
						//logger.debug("receive data "+this.remoteSeq);
						
						return;
						
					}
						
				}
					
			}
			else if(write_access)
			{
				if(this.send_request)
				{
					if(f.getTPCI()==(0x30 | this.localSeq) && (f.getAPCI()>>8)==2) // ack frame
					{
						this.send_request=false;
						this.localSeq=(this.localSeq+1)%16;	
						
						this.receive_request_ack=true;
						
						//logger.debug("receive request ack.");
						
						return;
						
					}	
				}
			}
		}
		else if(f.getDestAddress().equals(new EIBPhaddress(0,0,0)) && !f.getSourceAddress().equals(this.srcAddr))		
		{
			if( this.conState==AWAIT_BUTTON && f.getRepeat()==false)
			{
				this.addresses.addElement(f.getSourceAddress());
			}
		}
	}
		
}
