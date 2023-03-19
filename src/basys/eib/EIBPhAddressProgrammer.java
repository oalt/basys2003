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

import basys.eib.exceptions.EIBAddressInUseException;
import basys.eib.exceptions.EIBMultipleDeviceSelectionException;
import basys.eib.exceptions.NoProgrammingButtonPressedException;

/**
 * EIBPhAddressProgrammer.java
 * 
 * 
 * @author	oalt
 * @version $Id: EIBPhAddressProgrammer.java,v 1.2 2006/09/14 13:42:41 fritzchen Exp $
 * 
 */
public class EIBPhAddressProgrammer extends EIBProgrammer
{
	
	public static final int MULTIBUTTON 	= 1;
	public static final int ADDRESSINUSE 	= 2;
	public static final int NOBUTTONPRESSED = 3;
	
	private Thread t;
	private EIBConnection con;
	private EIBPhaddress addr;
	// private static final EIBPhaddress SYSTEMADDRESS= new EIBPhaddress(0,0,5);
	
	public EIBPhAddressProgrammer(EIBConnection con, EIBPhaddress addr)
	{
		this.con=con;
		this.addr=addr;
	}
	
	/**
	 * @see basys.eib.EIBProgrammer#startProgramming()
	 */
	public void startProgramming()
	{
		t=new InternalThread(this);
		t.start();
	}
	
	class InternalThread extends Thread
	{
		private EIBProgrammer p;
		
		
		public InternalThread(EIBPhAddressProgrammer p)
		{
			this.p=p;	
		}

		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run()
		{
			EIBSystemProgrammer prog=new EIBSystemProgrammer(con, con.getIndividualAddress(), addr);
			
			setProgress(10);
			
			try
			{
				prog.writePhysicalAddress();
			}
			catch(EIBAddressInUseException iue)
			{
				setErrorCode(ADDRESSINUSE);
			}
			catch(EIBMultipleDeviceSelectionException mde)
			{
				setErrorCode(MULTIBUTTON);
			}
			catch(NoProgrammingButtonPressedException npbpe)
			{
				setErrorCode(NOBUTTONPRESSED);
			}
			
			setProgress(100);
		}
	}

}
