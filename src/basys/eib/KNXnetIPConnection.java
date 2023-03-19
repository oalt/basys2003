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
 * Copyright (c) 2006        BASys 2003 Project, 
 *                           Vienna University of Technology, 
 *                           Department of Automation - Automation Systems group,
 *               			 B. Malinowsky
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

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.apache.log4j.Logger;

import basys.eib.event.EIBFrameEvent;
import basys.eib.exceptions.EIBConnectionNotPossibleException;
import basys.eib.eibnetip.*;
import basys.eib.eibnetip.exceptions.*;

//class Semaphore
//{
//	public Semaphore(int cnt) { this.cnt = cnt > 0 ? cnt : 1; }
//	public synchronized void enter()
//	{
//		while (cnt <= 0)
//			try { wait(); }
//			catch (InterruptedException e) { }
//		cnt--;
//	}
//	public synchronized void leave()
//	{
//		if (++cnt > 0)
//			notify();
//	}
//	private int cnt = 1;	
//}

/**
 * KNXnetIPConnection
 * Implements part of the KNXnet/IP specification version 1.0
 * a KNX protocol on top of IP networks for interconnection of KNX devices 
 * 
 * Supports tunneling on KNX linklayer on TP medium
 * Notes: not yet supported KNXnet/IP features are handled by an 
 * 		  internal exception (KNXnetIPNotSupportedException) 
 */
public class KNXnetIPConnection extends EIBConnection
{
	private KNXnetIPConnection() { super();	}
	
	/**
	 * Accessor for KNXnetIPConnection singelton
	 * @return an instance of this class
	 */
	public static EIBConnection getEIBConnection()
	{
		if (con == null) {
			con = new KNXnetIPConnection();
		}
		logger.debug("KNXnet/IP connection requested");
		return con;
	}

	public String getConnectionType()
	{
		return "KNXnet/IP Connection";
	}

	/**
	 * try to connect to KNXnet/IP server
	 */
	public void connect() throws EIBConnectionNotPossibleException
	{
		if (!isConnected()) {

			// Note: this dialog code is only necessary, since
			// there is no way to the set the server ip address for 
			// the control data point in the application
			KNXnetIPAddressDialog dlg = new KNXnetIPAddressDialog(serverCtrlIP);
			dlg.setModal(true);
			dlg.setVisible(true);
			if (dlg.isCanceled())
				throw new EIBConnectionNotPossibleException("Connect was canceled by user");
			serverCtrlIP = dlg.getAddress();
			
			// reset members
			serverDataIP = "";
			serverDataPort = 0;
			channelID = 0;
			serverSeqCounter = 0;	
			localSeqCounter = 0; 			
			if (socket != null)
				try {
					socket.close();
				} catch (Exception e) { }
				
			try {
				socket = new DatagramSocket(localPort);
			}
			catch (Exception e) {
				System.out.println(e);
			}
			if (socket == null) {
				throw new EIBConnectionNotPossibleException("Port is busy");
			}
			// request connection
			try {
				KNXnetIPConRequest request = new KNXnetIPConRequest(
					InetAddress.getLocalHost().getHostAddress(), localPort);
				request.send(socket, serverCtrlIP, serverCtrlPort);
			}
			catch (Exception e)	{
				logger.debug("Datagram error: " + e);
				return;
			} 
			// receive connection response
			KNXnetIPConResponse response = new KNXnetIPConResponse();
			receive(response);
			if (response.getStatus() != KNXnetIPConResponse.E_NO_ERROR)
				throw new EIBConnectionNotPossibleException("KNXnet/IP error: server refused connection");
			// only frames after connection response are of interest
			frames.clear();
			channelID = response.getChannelID();
			serverDataIP = response.getDataEndPoint().getIP();
			serverDataPort = response.getDataEndPoint().getPort();

			int ia = response.getDesc().getIndividualAddress();
			individualAddress = new EIBPhaddress((ia >>> 12) & 0x0F, (ia >>> 8) & 0x0F, ia & 0xFF); 
			System.out.println("Individual Address: " + individualAddress.toString());
			
			setConnected(true);
			logger.debug("KNXnet/IP connection opened");
			conState = new HeartbeatMonitor();
			conState.start();
			receiver = new KNXnetIPReceiver();
			receiver.start();
		}
	}

	/**
	 * disconnect from KNXnet/IP server
	 */
	public void disconnect()
	{
		if (!isConnected())
			return;
		synchronized (socket) { // couple send/receive together
			// send disconnect request
			try {
				KNXnetIPDisconRequest request = new KNXnetIPDisconRequest(channelID, 
					InetAddress.getLocalHost().getHostAddress(), localPort);
				request.send(socket, serverCtrlIP, serverCtrlPort);
			}
			catch (Exception e)	{
				logger.debug("Datagram error: " + e);
				return; // connection is still open...
			} 			
			// receive disconnect response
			KNXnetIPDisconResponse response = new KNXnetIPDisconResponse();
			try {
				receive(response);
			}
			catch (EIBConnectionNotPossibleException e) { 
				logger.debug("Error on closing KNXnet/IP connection");
			}
		}
		// clean up
		frames.clear();
		conState.interrupt();
		receiver.interrupt();
		
		try {
			// Note: this method is called by receiver and conState, too
			// so always check thread context, before join!
			if (receiver != Thread.currentThread())
				receiver.join(2000);
			if (conState != Thread.currentThread())
				conState.join(2000);
		} catch (InterruptedException e) { }
		
		socket.close();
		setConnected(false);
		logger.debug("KNXnet/IP connection closed");
	}

	/**
	 * send an EIBFrame as encapsulated cEMI frame within
	 * IP packets (tunnelling) 
	 */
	public void sendEIBFrame(EIBFrame ef)
	{
		if (!isConnected()) {
			System.out.println("KNXnet/IP server not connected");
			return; 
		}
		CEMIFrame frame = new CEMIFrame(CEMIFrame.L_Data_req, ef); 
		try {
			synchronized (socket) { // couple send/receive
				KNXnetIPTunnelRequest request = new KNXnetIPTunnelRequest(
					channelID, getLocalSeqCounter(), frame);
				// request: generate a tunnelling request (and acknowledge) for L_Data.req
				Debug.printFrame(request.getFrame());
				request.send(socket, serverDataIP, serverDataPort);
				KNXnetIPTunnelAck ack = new KNXnetIPTunnelAck();
				try { 
					receive(ack);
				}
				catch (EIBConnectionNotPossibleException e) 
				{ 
					// try once again on timeout
					request.send(socket, serverDataIP, serverDataPort);
					receive(ack);
				}
				incLocalSeqCounter();
				
				// Note: L_Data.conf is received in Receiver thread
				// so code below not necessary
				
				// confirmation: wait for tunnelling request *with* L_Data.con message
//				KNXnetIPTunnelRequest conRequest = new KNXnetIPTunnelRequest();
//				while (true) { 
//					receive(conRequest);
//					if (conRequest.getCEMIFrame().getMsgCode() == CEMIFrame.L_Data_con)
//						break;
//					else
//						frames.add(conRequest.getFrame()); // store frame in queue
//				}
//				if (conRequest.getConnHeader().getCounter() != getServerSeqCounter()) {
//					logger.debug("sequence counter of server out of sync - corrected");
//					setServerSeqCounter(conRequest.getConnHeader().getCounter());
//				}
//				KNXnetIPTunnelAck conAck = new KNXnetIPTunnelAck(channelID, 
//						getServerSeqCounter(), KNXnetIPFrameBase.E_NO_ERROR);
//				conAck.send(socket, serverDataIP, serverDataPort);
//				System.out.println("my: " + Integer.toString(getServerSeqCounter()) + 
//						", server: " + Integer.toString(conRequest.getConnHeader().getCounter()));				
//				incServerSeqCounter();
			}
		}
		catch (Exception e)
		{
			logger.debug("Datagram sending error: " + e);			
		}
	}
	
	/**
	 * receiving wait loop for object frame 
	 * invalid datagrams are saved in queue
	 * method is only left through exception
	 */
	private void receive(KNXnetIPFrameBase obj) throws EIBConnectionNotPossibleException
	{
		while (!obj.receive(socket))
			frames.add(obj.getInvalidPacket());
	}
	
	// thread safe counter get/set/increment
	private synchronized void incLocalSeqCounter() { localSeqCounter = ++localSeqCounter & 255; }  
	private synchronized int getLocalSeqCounter() { return localSeqCounter; }
	private synchronized void incServerSeqCounter() { serverSeqCounter = ++serverSeqCounter & 255; }
	private synchronized int getServerSeqCounter() { return serverSeqCounter; }	
	private synchronized void setServerSeqCounter(int c) { serverSeqCounter = c; }
	
	private static EIBConnection con = null;

	// N.B. it is necessary to couple send/receive calls that belong together,
	// so no other thread can 'filter' out frames inbetween. 
	// This is done through synchronized socket blocks for now...	
	private DatagramSocket socket;
	// address and ports for KNXnetIP server
	private String serverCtrlIP = "knxnetserver.myhome.org";
	// port 3671 *shall* be used at least for discovery purposes
	private int serverCtrlPort = 3671;
	private String serverDataIP = "";
	private int serverDataPort = 0;

	// we should check if that port is actually available here - currently we just guess
	private int localPort = 3675;
	private int channelID = 0; // tunnel communication channel id

	// communication channel sequence counters
	private int serverSeqCounter = 0;	
	private int localSeqCounter = 0; 

	private HeartbeatMonitor conState;
	private KNXnetIPReceiver receiver;
	
	// instead of synchronized socket blocks: semaphore would be cleaner solution
	// private Semaphore socketLock = new Semaphore(1);
	
	private static Logger logger = Logger.getLogger(EIBGatewayConnection.class);
	
	/**
	 * HeartbeatMonitor 
	 * thread implementation for heartbeat monitoring 
	 * (CONNECTIONSTATE_REQUEST / CONNECTIONSTATE_RESPONSE)
	 */
	class HeartbeatMonitor extends Thread
	{
		public HeartbeatMonitor()
		{
			// create connection request only once since it never changes
			try {
				state = new KNXnetIPConstateRequest(channelID, 
					InetAddress.getLocalHost().getHostAddress(), localPort);
				Debug.printFrame(state.getFrame());
			}
			catch (UnknownHostException e) { }
		}
		
		public void run()
		{
			while (true) {
				if (isInterrupted())
					break;
				try {
					Thread.sleep(HEARTBEAT_INTERVAL);
					int i = 0;
					for ( ; i < MAX_REPEAT_REQUESTS; i++) {
						synchronized (socket) {
							logger.debug("Sending connection state request");
							state.send(socket, serverCtrlIP, serverCtrlPort);
							KNXnetIPConstateResponse res = new KNXnetIPConstateResponse();
							try {
								receive(res);
								if (res.getStatus() == KNXnetIPConstateResponse.E_NO_ERROR)
									break;
								else
									System.out.println("Connectionstate response error 0x" + 
										Integer.toHexString(res.getStatus() & 255));
							}
							catch (EIBConnectionNotPossibleException e) { } // no answer, repeat...
						}
					}
					// disconnect on no success
					if (i == MAX_REPEAT_REQUESTS) {
						System.out.println("Communication failure: " +
								"no heartbeat response - closing KNXnet/IP connection");
						disconnect();
					}
				}
				catch (InterruptedException e) {
					interrupt();
				}
			}
		}
	
		private KNXnetIPConstateRequest state;
		private static final int HEARTBEAT_INTERVAL = 60000;
		// number of attempts to get a successful response
		private static final byte MAX_REPEAT_REQUESTS = 4;  
	}	
	
	/**
	 * KNXnetIPReceiver
	 * socket listener thread which
	 * - waits for incoming KNXnet/IP data packets,
	 * - observes the frame list and 
	 * - notifies event listener of new eibframes
	 */
	class KNXnetIPReceiver extends Thread
	{
		public KNXnetIPReceiver() { }

		public void run()
		{
			while (true) {
				if (isInterrupted())
					break;
				// wait for data packet
				try {
					// we set our own shorter receiving time 
					// for data packets, since we're doing lazy polling here
					KNXnetIPTunnelRequest req = new KNXnetIPTunnelRequest() {
						{  responseTime = 50; }
					};
					receive(req);
					// HACK:
					// readd acknowledge octet (ACK/NAK) to byte stream
					// because EIBFrame.getFrame() does *not* return this flag
					// on its own...
					byte[] fr = req.getFrame();
					byte[] tmp = new byte[fr.length + 2];
					System.arraycopy(fr, 0, tmp, 0, fr.length);
					tmp[tmp.length - 1] = (byte) req.getCEMIFrame().getEIBFrame().getAck();
					// add to received frames queue
					frames.addElement(tmp);
				}
				catch (EIBConnectionNotPossibleException e) { }

				// check for new frames in queue
				while (frames.size() > 0) {
					try {
						KNXnetIPTunnelRequest frame = new KNXnetIPTunnelRequest(
							(byte[]) frames.elementAt(0));
						
						// there *should* be only L_Data.conf and L_Data.ind messages...
						if (frame.getCEMIFrame().getMsgCode() == CEMIFrame.L_Data_con) {
							// confirmation frame
						}
						else if (frame.getCEMIFrame().getMsgCode() != CEMIFrame.L_Data_ind)
							logger.debug("tunnel request in queue with unexpected cEMI msg code: 0x" + 
								Integer.toHexString(frame.getCEMIFrame().getMsgCode()));

						// if sequence count is one less than expected, we acknowledge and
						// discard the message...
						boolean discard = false;
						if (frame.getConnHeader().getCounter() == getServerSeqCounter() - 1)
							discard = true;
						else if (frame.getConnHeader().getCounter() != getServerSeqCounter()) {
							logger.debug("sequence counter of server out of sync - corrected");
							logger.debug("mine: " + Integer.toString(getServerSeqCounter()) + 
									", server: " + Integer.toString(frame.getConnHeader().getCounter()));							
							setServerSeqCounter(frame.getConnHeader().getCounter());
						}
						
						// acknowledge received tunnelled frame
						KNXnetIPTunnelAck conAck = new KNXnetIPTunnelAck(channelID, 
							getServerSeqCounter(), KNXnetIPFrameBase.E_NO_ERROR);
						conAck.send(socket, serverDataIP, serverDataPort);
						if (!discard) {
							incServerSeqCounter();
							// notify about new frame
							notifyListeners(frame.getCEMIFrame().getEIBFrame());
						}
					}
					catch (KNXnetIPInvalidDataException e) {
						// try find out what service type it might be
						try {
							KNXnetIPHeader header = new KNXnetIPHeader(
								(byte []) frames.elementAt(0));
							short type = header.getServiceType();
							// watch out for server disconnect
							if (type == KNXnetIPHeader.DISCONNECT_REQUEST) {
								System.out.println("DISCONNECT_REQUEST in receiver - disconnect");
								disconnect();
								break;
							}
							else if (type == KNXnetIPHeader.CONNECTIONSTATE_RESPONSE)
								logger.debug("CONNECTIONSTATE_RESPONSE in receiver");
						} catch (KNXnetIPInvalidDataException ide) {
							logger.debug("Unknown (invalid) data packet received: " + e);
							Debug.printFrame((byte[]) frames.elementAt(0));
						}
					}
					catch (KNXnetIPNotSupportedException e) {
						logger.debug("Unsupported frame received:" + e);					
						Debug.printFrame((byte[]) frames.elementAt(0));
					}
					frames.removeElementAt(0);
				}
			}
		}
		
		private void notifyListeners(EIBFrame frame)
		{
			EIBFrameEvent event = new EIBFrameEvent(this, frame);
			for (Enumeration e = listeners.elements(); e.hasMoreElements(); )
				((EIBFrameListener) e.nextElement()).frameReceived(event);
		}
	}
}
