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

import basys.eib.exceptions.EIBAddressFormatException;

/**
 * EIB physical addresses
 *
 * @author  oalt
 * @version $Id: EIBPhaddress.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */

public class EIBPhaddress extends EIBAddress {

  /**
   * Default constructor
   * Sets all bits in the address to 0
   */
  public EIBPhaddress() {
    this.addr_h = 0x0;
    this.addr_l = 0x0;
  }

  /**
   * Constructor
   * Sets EIB physical address with a string
   *
   * @param s string with an EIB group address
   */
  public EIBPhaddress(String s)  throws EIBAddressFormatException
  {
  	
    setAddress(s);
  }

  /**
   * Constructor
   * Set EIB physical addres form three values
   *
   * @param zone device zone number
   * @param line device line number
   * @param device device number
   */
  public EIBPhaddress(int zone, int line, int device)  {
    setAddress(zone, line, device);
  }

  /**
   * Set EIB physical address from a string (e.g. 1.1.1)
   *
   * @param s address string
   */
  public void setAddress(String s) throws EIBAddressFormatException
  {
	try
	{
	
    int idx=s.indexOf('.');

    Integer mgrp = new Integer(s.substring(0,idx));

    this.addr_h=0;
    this.addr_h=(mgrp.intValue() << 4);
    int idx2 = s.indexOf('.', idx+1);
    mgrp =new Integer(s.substring(idx+1,idx2));
    this.addr_h |= mgrp.intValue();

    mgrp =new Integer(s.substring(idx2+1,s.length()));
    this.addr_l = mgrp.intValue();
	}
	catch(Exception e)
	{
		throw new EIBAddressFormatException("Wrong address format.");	
	}
  }

  /**
   * Set EIB physical addres form three values
   *
   * @param zone device zone number
   * @param line device line number
   * @param device device number
   */
  public void setAddress(int zone, int line, int device) {
    this.addr_h = (zone << 4) | line;
    this.addr_l = device;
  }

  /**
   * Get a string representation of the address
   *
   * @return the address as string
   */
  public String toString()  {
    String s="";

    s+=(this.addr_h >> 4)+"."+(this.addr_h & 0x0F)+"."+this.addr_l;

    return s;
  }

}