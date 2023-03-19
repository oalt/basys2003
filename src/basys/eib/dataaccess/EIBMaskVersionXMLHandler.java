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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.*;

import basys.eib.*;


/**
 * EIBMaskVersionXMLHandler.java
 * 
 * 
 * @author	oalt
 * @version $Id: EIBMaskVersionXMLHandler.java,v 1.1 2004/01/14 21:38:41 oalt Exp $
 * 
 */
public class EIBMaskVersionXMLHandler extends DefaultHandler
{
	
	private boolean isMask=false;
	private boolean isMaskId=false;
	private boolean isMaskVersion=false;
	private boolean isUramStart=false;
	private boolean isUramEnd=false;
	private boolean isEepromStart=false;
	private boolean isEepromEnd=false;
	private boolean isRunErrorAddress=false;
	private boolean isAdrTabAdr=false;
	private boolean isAssTabPtr=false;
	private boolean isCommTabPtr=false;
	private boolean isManDtaAddr=false;
	private boolean isManDtaIdAddr=false;
	private boolean isManDatSize=false;
	private boolean isRouteCntrAddr=false;
	private boolean isManIdProtected=false;
	private boolean isMaskVersionName=false;

	private boolean match=false;
	
	EIBApplication app;
	
	/**
	 * 
	 */
	public EIBMaskVersionXMLHandler(EIBApplication app)
	{
		super();
		this.app=app;
	}
	
	public void startDocument() throws SAXException
	{
		
	}

	public void endDocument() throws SAXException
	{
	}

	public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException
	{

		String eName = sName; // element name
		if ("".equals(eName))
		{
			eName = qName; // not namespaceAware
		}

		if(eName.equals("mask"))
		{
			this.isMask=true;
		}
		else if(eName.equals("MASK_ID"))
		{
			if(this.isMask)
			{   
				this.isMaskId=true;
			}
		}
		else if(eName.equals("MASK_VERSION"))
		{
			if(this.isMask)
			{
				this.isMaskVersion=true;
			}
		}
		else if(eName.equals("USER_RAM_START"))
		{
			if(this.isMask)
			{
				this.isUramStart=true;
			}
		}
		else if(eName.equals("USER_RAM_END"))
		{
			if(this.isMask)
			{
				this.isUramEnd=true;
			}
		}
		else if(eName.equals("USER_EEPROM_START"))
		{
			if(this.isMask)
			{
				this.isEepromStart=true;
			}
		}
		else if(eName.equals("USER_EEPROM_END"))
		{
			if(this.isMask)
			{
				this.isEepromEnd=true;
			}
		}
		else if(eName.equals("RUN_ERROR_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isRunErrorAddress=true;
			}
		}
		else if(eName.equals("ADDRESS_TAB_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isAdrTabAdr=true;
			}
		}
		else if(eName.equals("ASSOCTABPTR_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isAssTabPtr=true;
			}
		}
		else if(eName.equals("COMMSTABPTR_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isCommTabPtr=true;
			}
		}
		else if(eName.equals("MANUFACTURER_DATA_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isManDtaAddr=true;
			}
		}
		else if(eName.equals("MANUFACTURER_DATA_SIZE"))
		{
			if(this.isMask)
			{
				this.isManDatSize=true;
			}
		}
		else if(eName.equals("MANUFACTURER_ID_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isManDtaIdAddr=true;
			}
		}
		else if(eName.equals("ROUTECNT_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isRouteCntrAddr=true;
			}
		}
		else if(eName.equals("MANUFACTURER_ID_PROTECTED"))
		{
			if(this.isMask)
			{
				this.isManIdProtected=true;
			}
		}
		else if(eName.equals("MASK_VERSION_NAME"))
		{
			if(this.isMask)
			{
				this.isMaskVersionName=true;
			}
		}		
		else    
		{
        
		}

	}

	public void endElement(String namespaceURI, String sName, String qName) throws SAXException
	{
		String eName = sName; // element name
		if ("".equals(eName))
			eName = qName; // not namespaceAware
    
		if ("".equals(eName))
		{
			eName = qName; // not namespaceAware
		}
		if(eName.equals("mask"))
		{
			this.isMask=false;
			this.match=false;
		}
		else if(eName.equals("MASK_ID"))
		{
			if(this.isMask)
			{   
				this.isMaskId=false;
			}
		}
		else if(eName.equals("MASK_VERSION"))
		{
			if(this.isMask)
			{
				this.isMaskVersion=false;
			}
		}
		else if(eName.equals("USER_RAM_START"))
		{
			if(this.isMask)
			{
				this.isUramStart=false;
			}
		}
		else if(eName.equals("USER_RAM_END"))
		{
			if(this.isMask)
			{
				this.isUramEnd=false;
			}
		}
		else if(eName.equals("USER_EEPROM_START"))
		{
			if(this.isMask)
			{
				this.isEepromStart=false;
			}
		}
		else if(eName.equals("USER_EEPROM_END"))
		{
			if(this.isMask)
			{
				this.isEepromEnd=false;
			}
		}
		else if(eName.equals("RUN_ERROR_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isRunErrorAddress=false;
			}
		}
		else if(eName.equals("ADDRESS_TAB_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isAdrTabAdr=false;
			}
		}
		else if(eName.equals("ASSOCTABPTR_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isAssTabPtr=false;
			}
		}
		else if(eName.equals("COMMSTABPTR_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isCommTabPtr=false;
			}
		}
		else if(eName.equals("MANUFACTURER_DATA_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isManDtaAddr=false;
			}
		}
		else if(eName.equals("MANUFACTURER_DATA_SIZE"))
		{
			if(this.isMask)
			{
				this.isManDatSize=false;
			}
		}
		else if(eName.equals("MANUFACTURER_ID_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isManDtaIdAddr=false;
			}
		}
		else if(eName.equals("ROUTECNT_ADDRESS"))
		{
			if(this.isMask)
			{
				this.isRouteCntrAddr=false;
			}
		}
		else if(eName.equals("MANUFACTURER_ID_PROTECTED"))
		{
			if(this.isMask)
			{
				this.isManIdProtected=false;
			}
		}
		else if(eName.equals("MASK_VERSION_NAME"))
		{
			if(this.isMask)
			{
				this.isMaskVersionName=false;
			}
		}		
		else    
		{
        
		}
		

	}

	public void characters(char buf[], int offset, int len) throws SAXException
	{
		String s = new String(buf, offset, len);
		if(this.isMaskId)
		{
			
			if(this.app.getMaskID().equals(s))
			{
				match=true;
			}
		}
		if(this.match)
		{
			if(this.isMaskVersion)
			{
				this.app.setMaskVersion(s);	
			} 
			else if(this.isUramStart)
			{
				this.app.setUserRAMStart(s);	
			}
			else if(this.isUramEnd)
			{
				this.app.setUserRAMEnd(s);	
			}
			else if(this.isEepromStart)
			{
				this.app.setUserEEPROMStart(s);	
			}
			else if(this.isEepromEnd)
			{
				this.app.setUserEEPROMEnd(s);	
			}
			else if(this.isRunErrorAddress)
			{
				this.app.setRunErrorAddress(s);	
			}
			else if(this.isAdrTabAdr)
			{
				this.app.setAddressTableAddress(s);	
			}
			else if(this.isAssTabPtr)
			{
				this.app.setAssociationTabPointer(s);	
			}
			else if(this.isCommTabPtr)
			{
				this.app.setComTabPointer(s);	
			}
			else if(this.isManDtaAddr)
			{
				this.app.setManufacturerDataAddress(s);	
			}
			else if(this.isManDatSize)
			{
				this.app.setManufacturerDataSize(s);	
			}
			else if(this.isManDtaIdAddr)
			{
				this.app.setManufacturerIdAddress(s);	
			}
			else if(this.isRouteCntrAddr)
			{
				this.app.setRouteConterAddress(s);
			}
			else if(this.isManIdProtected)
			{
				if(s.equals("1"))
				{
					this.app.setManIDProtected("true");
				}
				else
				{
					this.app.setManIDProtected("false");
				}
			}
			else if(this.isMaskVersionName)
			{
				this.app.setMaskVersionName(s);
			}			
		}
	}
}
