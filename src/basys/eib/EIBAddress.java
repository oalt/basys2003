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
 * EIBAddress.java
 *
 * Abstract Class for EIB Adresses
 *
 * @author  oalt
 * @version $Id: EIBAddress.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */

public abstract class EIBAddress {
  protected int addr_h;
  protected int addr_l;

  /**
   * Constructor
   */
  public EIBAddress() {
  }

  /**
   * Parse EIB Address from a String.
   *
   * @param s String to parse
   */
  public abstract void setAddress(String s) throws EIBAddressFormatException;

  /**
   * Get EIB Address high byte
   *
   * @return the high byte value
   */
  public int getHigh()  {
    return this.addr_h;
  }

  /**
   * Get EIB Address low byte
   *
   * @return low byte value
   */
  public int getLow()  {
    return this.addr_l;
  }

  /**
   * Set EIB Address high byte
   *
   * @param addr_h high byte address value
   */
  public void setHigh(int addr_h)  {
    this.addr_h=addr_h;
  }

  /**
   * Set EIB Address low byte
   *
   * @param addr_l low byte address value
   */
  public void setLow(int addr_l)  {
    this.addr_l=addr_l;
  }

  /**
   * Compare EIBAddress objects
   *
   * @param ea EIBAddress object to compare with actual object
   * @return true if equals.
   */
  public boolean equals(EIBAddress ea)  {
    if(ea.getHigh()==this.getHigh() && ea.getLow()==this.getLow())
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  	/**
  	 * Returns true if this address is greater than the given address.
  	 * @param ea the address to compare
  	 * @return true if this address is greater than the given address
  	 */
  	public boolean isGreaterThan(EIBAddress ea)
	{
		long l1=ea.getHigh()<<8 | (ea.getLow() & 0xFF);
		long l2=this.getHigh()<<8 | (this.getLow() & 0xFF);
		
		return l2 > l1;	
	}

}