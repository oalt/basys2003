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
 *                     2006, Dynamic setting of KNX interface individual address
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

import org.apache.log4j.Logger;

import basys.eib.exceptions.*;

/**
 * EIBApplicationProgrammer.java
 * 
 * @author 	oalt
 * @version	$Id: EIBApplicationProgrammer.java,v 1.2 2006/09/14 13:42:41 fritzchen Exp $
 */
public class EIBApplicationProgrammer extends EIBProgrammer 
{
	private static Logger logger=Logger.getLogger(EIBApplicationProgrammer.class);
	
	// private static final EIBPhaddress SYSTEMADDRESS= new EIBPhaddress(0,0,5);
	private EIBConnection con;
	private EIBPhaddress address;
	private int maskVersion;
	private EIBApplication app;
	int[] memoryData;
	private Thread t;
	
	
	/**
	 * Constructor
	 */
	public EIBApplicationProgrammer(EIBConnection con, EIBPhaddress addr, int maskVersion, EIBApplication app, 
								    int[] memoryData)
	{
		this.con=con;	
		this.address=addr;
		this.maskVersion=maskVersion;
		this.app=app;
		this.memoryData=memoryData;
	}
	
	/**
	 * 
	 */
	public void startProgramming()
	{
		t=new InternalThread(this);
		t.start();
	}

	class InternalThread extends Thread
	{
		private EIBProgrammer p;
		
		
		public InternalThread(EIBApplicationProgrammer p)
		{
			this.p=p;	
		}

		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run()
		{
			int m=0;
			EIBSystemProgrammer prog=new EIBSystemProgrammer(con, con.getIndividualAddress(), address);
			
			if(p.goOn)
			{
				try
				{
					prog.doConnect();
					p.setProgress(5); // 5% progress
				}
				catch(EIBConnectionNotPossibleException cnpe)
				{
					cnpe.printStackTrace();
					p.setErrorCode(EIBProgrammer.CONNECTION_NOT_POSSIBLE);
					p.cancelProgramming();	
				}
				catch(EIBConnectionAlreadyOpenException caoe)
				{
					caoe.printStackTrace();
				}
			}
			
			// check mask version
			
			if(p.goOn)
			{
				try
				{
					// check mask version 
					m=prog.readMaskversion();
					if(m < maskVersion)
					{
						p.setErrorCode(EIBProgrammer.WRONG_MASK_VERSION);
						prog.doDisconnect();
						p.cancelProgramming();
					}
					else
					{
						p.setProgress(10);
					}
				}
				catch(EIBConnectionClosedException cce)
				{
					p.setErrorCode(EIBProgrammer.CONNECTION_WAS_CLOSED);
					p.cancelProgramming();
				}
			}
			else
			{
				prog.doDisconnect();
			}
			
			// read manufacturer id
			if(p.goOn)
			{
				int[] manid={0,0,0,0};
				try
				{
					manid=prog.readMemory(app.getManufacturerIdAdress(), 1);
					p.setProgress(20);
				}
				catch(EIBConnectionClosedException cce)
				{
					p.setErrorCode(EIBProgrammer.CONNECTION_WAS_CLOSED);
					p.cancelProgramming();
				}
				if(manid[3]!=Integer.parseInt(app.getManufacturerID()) )
				{
					/*
					p.setErrorCode(EIBProgrammer.WRONG_MANUFACTURER_ID);
					prog.doDisconnect();
					p.cancelProgramming();
					JOptionPane.showMessageDialog(null, "appid "+app.getManufacturerID());
					*/
				}
				
			}
			else
			{
				prog.doDisconnect();
			}
			
			// --------------- start programming
			// set RUN ERROR to 00
			if(p.goOn)
			{
				try
				{
					int[] data={0};
					prog.writeAndVerifyMemory(0x10D, data);
					p.setProgress(30);
				}
				catch(EIBConnectionClosedException cce)
				{
					p.setErrorCode(EIBProgrammer.CONNECTION_WAS_CLOSED);
					p.cancelProgramming();
				}
				catch(EIBMemoryVerifyException mve)
				{
					mve.printStackTrace();
					p.setErrorCode(EIBProgrammer.MEMORY_VERIFY_PROBLEM);
					prog.doDisconnect();
					p.cancelProgramming();
				}
			}
			else
			{
				prog.doDisconnect();
			}
			
			// set address table length to 1 (ph. address only)
			
			if(p.goOn)
			{
				try
				{
					int[] data={1};
					prog.writeAndVerifyMemory(0x116, data);
					p.setProgress(40);
				}
				catch(EIBConnectionClosedException cce)
				{
					p.setErrorCode(EIBProgrammer.CONNECTION_WAS_CLOSED);
					p.cancelProgramming();
				}
				catch(EIBMemoryVerifyException mve)
				{
					p.setErrorCode(EIBProgrammer.MEMORY_VERIFY_PROBLEM);
					prog.doDisconnect();
					p.cancelProgramming();
				}
			}
			else
			{
				prog.doDisconnect();
			}
			
			// Optionregister = 0xFF
			if(p.goOn)
			{
				try
				{
					int[] data={0xFF};
					prog.writeAndVerifyMemory(0x100, data);
					p.setProgress(50);
				}
				catch(EIBConnectionClosedException cce)
				{
					p.setErrorCode(EIBProgrammer.CONNECTION_WAS_CLOSED);
					p.cancelProgramming();
				}
				catch(EIBMemoryVerifyException mve)
				{
					p.setErrorCode(EIBProgrammer.MEMORY_VERIFY_PROBLEM);
					prog.doDisconnect();
					p.cancelProgramming();
				}
			}
			else
			{
				prog.doDisconnect();
			}
			
			// Write parameter
			if(p.goOn)
			{
				try
				{
					int[] data=new int[9];
					
					for(int i=0; i<9; i++)
					{
						data[i]=memoryData[0x4+i];
					}
					
					prog.writeAndVerifyMemory(0x104, data);
				
					data=new int[8];
					for(int i=0; i<8; i++)
					{
						data[i]=memoryData[0xE+i];
					}
					
					prog.writeAndVerifyMemory(0x10E, data);
					p.setProgress(60);
				}
				catch(EIBConnectionClosedException cce)
				{
					p.setErrorCode(EIBProgrammer.CONNECTION_WAS_CLOSED);
					p.cancelProgramming();
				}
				catch(EIBMemoryVerifyException mve)
				{
					p.setErrorCode(EIBProgrammer.MEMORY_VERIFY_PROBLEM);
					prog.doDisconnect();
					p.cancelProgramming();
				}
			}
			else
			{
				prog.doDisconnect();
			}
			
			// Write rest (application and tables)
			if(p.goOn)
			{
				try
				{
					int[] data;
					
					int i=0x119;
					int j=0;
					
						
					while(i<0x1FF)
					{
						if(0x1FF-i >= 12)
						{
							j=12;
								
						}
						else
						{
							j=0x1FF-i;
						}
						data=new int[j];
						
						for(int k=0; k<j; k++)
						{
							data[k]=memoryData[i+k-0x100];
						}
						
						prog.writeAndVerifyMemory(i, data);
						
						
						
						i=i+j;
					}
					p.setProgress(70);
				}
				catch(EIBConnectionClosedException cce)
				{
					p.setErrorCode(EIBProgrammer.CONNECTION_WAS_CLOSED);
					p.cancelProgramming();
				}
				catch(EIBMemoryVerifyException mve)
				{
					p.setErrorCode(EIBProgrammer.MEMORY_VERIFY_PROBLEM);
					prog.doDisconnect();
					p.cancelProgramming();
				}
			}
			else
			{
				prog.doDisconnect();
			}
			
			// clear RAM
			if(p.goOn)
			{
				try
				{
					
					int ramsize=app.getUserRAMEnd()-app.getUserRAMStart()+1;
					
					logger.debug("Ramsize: "+ramsize );
					
					int i=0;
					int j=0;
					
					int data[];
					
					while(i < ramsize)
					{
						if(ramsize-i >= 12)
						{
							j=12;
								
						}
						else
						{
							j=ramsize-i;
						}
						data=new int[j];
						
						for(int k=0; k<j; k++)
						{
							data[k]=0;
						}
						
						prog.writeAndVerifyMemory(app.getUserRAMStart()+i, data);
						
						i=i+j;
					}
					p.setProgress(90);
				}
				catch(EIBConnectionClosedException cce)
				{
					p.setErrorCode(EIBProgrammer.CONNECTION_WAS_CLOSED);
					p.cancelProgramming();
				}
				catch(EIBMemoryVerifyException mve)
				{
					p.setErrorCode(EIBProgrammer.MEMORY_VERIFY_PROBLEM);
					prog.doDisconnect();
					p.cancelProgramming();
				}
			}
			else
			{
				prog.doDisconnect();
			}
			
			// set address table length
			if(p.goOn)
			{
				try
				{
					int[] data=new int[1];
					data[0]=memoryData[0x16];
					prog.writeAndVerifyMemory(0x116, data);
					p.setProgress(95);
				}
				catch(EIBConnectionClosedException cce)
				{
					p.setErrorCode(EIBProgrammer.CONNECTION_WAS_CLOSED);
					p.cancelProgramming();
				}
				catch(EIBMemoryVerifyException mve)
				{
					p.setErrorCode(EIBProgrammer.MEMORY_VERIFY_PROBLEM);
					prog.doDisconnect();
					p.cancelProgramming();
				}
			}
			else
			{
				prog.doDisconnect();
			}
			
			// reset run error;
			if(p.goOn)
			{
				try
				{
					int[] data={0xFF};
					prog.writeAndVerifyMemory(0x10D, data);
					p.setProgress(98);
				}
				catch(EIBConnectionClosedException cce)
				{
					p.setErrorCode(EIBProgrammer.CONNECTION_WAS_CLOSED);
					p.cancelProgramming();
				}
				catch(EIBMemoryVerifyException mve)
				{
					p.setErrorCode(EIBProgrammer.MEMORY_VERIFY_PROBLEM);
					prog.doDisconnect();
					p.cancelProgramming();
				}
			}
			else
			{
				prog.doDisconnect();
			}
			
		
			logger.debug("Error Code: "+p.getErrorCode());
			
			try
			{
				prog.restartDevice();
			}
			catch(EIBConnectionClosedException cce)
			{
				cce.printStackTrace();
			}
			p.setProgress(100);
			
			
			
		} // run()
		
				
		
	}
	
}
