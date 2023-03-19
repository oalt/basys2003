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
package basys.eib.dataaccess;

import basys.eib.EIBAddress;
import basys.eib.EIBGrpaddress;
import basys.eib.EIBPhaddress;

import java.util.Enumeration;
import java.util.Vector;

/**
 * EIBAddressTable.java
 * 
 * Class for the EIB adress table
 * 
 * @author 	oalt
 * @version	$Id: EIBAddressTable.java,v 1.1 2004/01/14 21:38:41 oalt Exp $
 */
public class EIBAddressTable
{
	private EIBPhaddress phAddress=new EIBPhaddress(); 
	private Vector grpaddresses=new Vector();
	
	/**
	 * 
	 */
	public EIBAddressTable()
	{
		super();
		
	}
	
	public int getSize()
	{
		return this.grpaddresses.size()+1;
	}

	/**
	 * Add new groupaddress to the table
	 * @param address the group address to add
	 */
	public void addAddress(EIBGrpaddress address)
	{
		this.grpaddresses.add(address);
	}
	
	/**
	 * Set physical address
	 * @param a the physical address
	 */
	public void setPhAddress(EIBPhaddress a)
	{
		this.phAddress=a;
	}
	
	/**
	 * Returns the physical address
	 * 
	 * @return the physical address
	 */
	public EIBPhaddress getPhysicalAddress()
	{
		return this.phAddress;
	}
	
	/**
	 * Returns true if the Address is already included in the table 
	 * @param a the address to compare
	 * @return true if included and false otherwise
	 */
	public boolean isIncluded(EIBAddress a)
	{
		for(Enumeration e=this.grpaddresses.elements(); e.hasMoreElements(); )
		{
			if(((EIBAddress)e.nextElement()).equals(a))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the address table in the programmable/binary form
	 * 
	 * @return the address table array
	 */
	public int[] getTable()
	{
		int[] t=new int[this.getSize()*2+1];
		
		t[0]=this.getSize();
		
		// physical address
		t[1]=this.phAddress.getHigh();
		t[2]=this.phAddress.getLow();
		
		// group addresses
		int cnt=3;
		for(Enumeration e=this.grpaddresses.elements(); e.hasMoreElements(); )
		{
			EIBGrpaddress ga=(EIBGrpaddress)e.nextElement();
			t[cnt]=ga.getHigh();
			t[cnt+1]=ga.getLow();
			cnt+=2;
		}
		
		return t;
	}
}