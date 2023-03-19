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
package basys.eib.tests;

import org.apache.log4j.Logger;

import basys.eib.*;
import basys.eib.exceptions.*;
import basys.eib.EIBGatewayConnection;
import basys.eib.EIBSystemProgrammer;
import junit.framework.TestCase;

/**
 * EIBSystemProgrammerTest.java
 * 
 * 
 * @author	oalt
 * @version $Id: EIBSystemProgrammerTest.java,v 1.2 2006/09/14 13:42:41 fritzchen Exp $
 * 
 */
public class EIBSystemProgrammerTest extends TestCase
{
	private static Logger logger=Logger.getLogger(EIBSystemProgrammerTest.class);
	
	// private static final EIBPhaddress SYSTEMADDRESS= new EIBPhaddress(0,0,5);
	private EIBPhaddress address;
	private EIBConnection con;
	private EIBSystemProgrammer prog;
	
	/**
	 * Constructor for EIBSystemProgrammerTest.
	 * @param arg0
	 */
	public EIBSystemProgrammerTest(String arg0)
	{
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		this.con=EIBGatewayConnection.getEIBConnection();
		this.address=new EIBPhaddress("1.1.50");
		this.prog=new EIBSystemProgrammer(con, con.getIndividualAddress(), address);
	}

	public void testEIBSystemProgrammer()
	{
		int data[] = {0};
		
		try
		{		
			/*
			prog.writeMemory(0x10D,data);
			int[] result=prog.readMemory(0x10D, 1);
			System.out.println("write 1, 0x10D: "+result[3]);
	
	
			data[0] = 0xFF;
			prog.writeMemory(0x10D,data);
			result=prog.readMemory(0x10D, 1);
			System.out.println("write 2, 0x10D: "+result[3]);
	
			//prog.restartDevice();
			prog.doDisconnect();
			*/
			EIBTelegrammtraceframe ttf=new EIBTelegrammtraceframe(this.con);
			ttf.setVisible(true);
			prog.writePhysicalAddress();
		}
		catch(Exception e)
		{
			e.printStackTrace();	
		}
	
	}

	public void testDoConnect()
	{
	}

	public void testDoDisconnect()
	{
	}

	public void testReadMaskversion()
	{
	/*
		int mask=0;
		int[] mem=null;


		// start event based programming cycle
		try
		{
			prog.doConnect();
		}
		catch(EIBConnectionAlreadyOpenException caoe)
		{
		}
		catch(EIBConnectionNotPossibleException cnpe)
		{
			
		}
		try
		{
			mask=prog.readMaskversion();
		}
		catch(EIBConnectionClosedException cce)
		{
			
		}
		assertEquals(mask, 18);
	*/
	}

	/**
	 * Read 12 bytes of memory data and print them out
	 *
	 */
	public void testReadMemory()
	{
		/*
		int mem[]=null;
		
		try
		{
			prog.doConnect();
		}
		catch(Exception e)
		{
		}
		
		for(int i=0; i<17*12; i=i+12)
		{
			try
			{
				mem=prog.readMemory(0x100+i, 12);
			}
			catch(EIBConnectionClosedException cce)
			{
				
			}
			System.out.println("0x"+Integer.toHexString(0x100+i)+": ");
			for(int cnt=0; cnt < mem.length; cnt++)
			{
				if(mem[cnt]<16)
				{
					System.out.print("0");	
				}
				System.out.print(Integer.toHexString(mem[cnt]).toUpperCase()+ " ");	
			}
			System.out.println();
		}
		
		prog.doDisconnect();
		*/
	}

	public void testRestartDevice()
	{
	}

	public void testWriteMemory()
	{
		
	}

	public void testWriteAndVerifyMemory()
	{
		/*
		int data[]={0};
		try
		{
			prog.doConnect();
		}
		catch(Exception e)
		{
			
		}
		try
		{
			prog.writeAndVerifyMemory(0x10D,data);
			data[0]=0xFF;
			prog.writeAndVerifyMemory(0x10D,data);
		}
		catch(EIBConnectionClosedException cce)
		{
		}
		catch(EIBMemoryVerifyException mve)
		{
			assertTrue(false);	
		}
		
		*/
	}

	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(EIBSystemProgrammerTest.class);
	}
}
