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
 * EIBgrpaddr.java
 *
 * Realization of the EIBAddress class for group addresses
 *
 *
 * @author  oalt
 * @version $Id: EIBGrpaddress.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */

public class EIBGrpaddress extends EIBAddress {

  /**
   * Default constructor
   * Sets all bits in the address to 0
   */
  public EIBGrpaddress() {
    this.addr_h=0;
    this.addr_l=0;
  }

  /**
   * Constructor
   * Sets EIB group address with a string
   *
   * @param s string with an EIB group address
   */
  public EIBGrpaddress(String s) throws EIBAddressFormatException
  {
    setAddress(s);
  }

  /**
   * Constructor
   * Set ERIB group address with main and subgroup
   *
   * @param maingroup main group number
   * @param subgrp subgroup number
   */
  public EIBGrpaddress(int maingrp, int subgrp) {
    setAddress(maingrp, subgrp);
  }

  public EIBGrpaddress(int groupAddressCount)
  {
  		int addr=0x100+groupAddressCount;
  		this.addr_h=addr>>8;
  		this.addr_l=addr & 0xFF;
  		
  }
  
  /**
   * Set address with a string
   *
   * @param s string with the group address (e.g. 1/1/1)
   */
  public void setAddress(String s) throws EIBAddressFormatException
  {
  	try
  	{
  	
    int idx=s.indexOf('/');

    Integer mgrp = new Integer(s.substring(0,idx));

    this.addr_h=0;
    this.addr_h=(mgrp.intValue() << 3);

    mgrp= new Integer(s.substring(idx+1, s.length()));
    this.addr_h |= (mgrp.intValue() >> 8);
    this.addr_l = mgrp.intValue() & 0xFF;
  	}
  	catch(Exception e)
  	{
  		throw new EIBAddressFormatException("Wrong EIB group address format: "+s);
  	}
  }

  /**
   * Set address with main and subgroup
   *
   * @param maingrp main group number
   * @param subgrp subgroup number
   */
  public void setAddress(int maingrp, int subgrp)  {
    this.addr_h=0;
    this.addr_h=(maingrp << 3) | (subgrp >> 8);
    this.addr_l=(subgrp & 0xFF);
  }

  /**
   * Get a string representation of the address
   *
   * @return the address as string
   */
  public String toString()  {
    String s="";
    s+=(this.addr_h>>3)+"/"+(((this.addr_h & 0x07)<<8) + this.addr_l);
    return s;
  }
}