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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import basys.XMLStringRepresentor;

/**
 * EIBApplication.java
 * 
 * Dataclass for EIB application data.
 * 
 * @author 	oalt
 * @version	$Id: EIBApplication.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public class EIBApplication implements XMLStringRepresentor
{
	private String program_id="";
	private String mask_version="";
	private String mask_id="";
	private String name="";
	private String version="";
	private int devtype=0;
	private int pei_type=0;
	private int addr_tab_addr=0;
	private int addr_tab_size=0;
	private int ass_tab_addr=0;
	private int ass_tab_size=0;
	private int comm_tab_addr=0;
	private int comm_tab_size=0;
	private String man_id="";
	private String eeprom_data="";
	private String orig_man_id="";
	private int usr_ram_start=0;
	private int usr_ram_end=0;
	private int usr_eeprom_start=0;
	private int usr_eeprom_end=0;
	private int run_error_addr=0;
	private int ass_tab_ptr=0;
	private int comm_tab_ptr=0;
	private int mandata_addr=0;
	private int mandata_size=0;
	private int manid_addr=0;
	private int routecnt_addr=0;
	private boolean manid_protected=true;
	private String maskversion_name="";
	
	/**
	 * Constructor for EIBApplication.
	 */
	public EIBApplication()
	{
		super();
	}
	
	public void setProgramID(String id)
	{
		this.program_id=id;
	}
	
	public String getProgramID()
	{
		return this.program_id;
	}

	public void setMaskID(String maskID)
	{
		this.mask_id=maskID;
	}	

	public String getMaskID()
	{
		return this.mask_id;
	}
	
	public void setMaskVersion(String maskVersion)
	{
		this.mask_version=maskVersion;
	}	

	public String getMaskVersion()
	{
		return this.mask_version;
	}
	
	public void setName(String name)
	{
		this.name=name;
	}

	public String getName()
	{
		return this.name;
	}
	
	public void setVersion(String version)
	{
		this.version=version;
	}
	
	public String getVersion()
	{
		return this.version;
	}
	
	public void setDeviceType(String devType)
	{
		try
		{
			this.devtype=Integer.parseInt(devType);
		}
		catch(NumberFormatException nfe)
		{
			this.devtype=0;
		}
	}
	
	public int getDeviceType()
	{
		return this.devtype;
	}
	
	
	public void setPEIType(String peiType)
	{
		try
		{
			this.pei_type=Integer.parseInt(peiType);
		}
		catch(NumberFormatException nfe)
		{
			this.pei_type=0;
		}
	}
	
	public int getPEIType()
	{
		return this.pei_type;	
	}
	
	public void setAddressTableAddress(String addr)
	{
		this.addr_tab_addr=Integer.parseInt(addr);
	}

	public int getAddressTableAddress()
	{
		return this.addr_tab_addr;	
	}
	
	public void setAddressTableSize(String size)
	{
		this.addr_tab_size=Integer.parseInt(size);
	}

	public int getAddressTableSize()
	{
		return this.addr_tab_size;	
	}
	
	public void setAssociationTableSize(String size)
	{
		this.ass_tab_size=Integer.parseInt(size);
	}

	public int getAssociationTableSize()
	{
		return this.ass_tab_size;	
	}
	
	public void setAssociationTableAddress(String address)
	{
		this.ass_tab_addr=Integer.parseInt(address);
	}

	public int getAssociationTableAddress()
	{
		return this.ass_tab_addr;	
	}
	
	public void setCommTableAddress(String address)
	{
		this.comm_tab_addr=Integer.parseInt(address);
	}
	
	public int getCommTableAddress()
	{
		return this.comm_tab_addr;
	}
	
	public void setCommTableSize(String size)
	{
		this.comm_tab_size=Integer.parseInt(size);
	}
	
	public int getCommTableSize()
	{
		return this.comm_tab_size;
	}
	
	public void setManufacturerID(String manID)
	{
		this.man_id=manID;
	}

	public String getManufacturerID()
	{
		return this.man_id;
	}
	
	public void setEEPROMData(String data)
	{
		this.eeprom_data=data;
	}	
	
	public String getEEPROMData()
	{
		return this.eeprom_data;
	}
	
	/**
	 * Returns the EEPROM data as an int array
	 * 
	 * @return the EEPROM data
	 */
	public int[] getEEPROMDataArray()
	{
		int data[]=new int[this.eeprom_data.length()/2];
		
		for(int cnt=0; cnt<this.eeprom_data.length()/2; cnt++)
		{
			String b="0x"+this.eeprom_data.substring(cnt*2, cnt*2+2);
			data[cnt]=(Integer.decode(b)).intValue();
		}
		
		return data;
	}
	
	public void setOriginalManufacturerID(String orig_manID)
	{
		this.orig_man_id=orig_manID;
	}
	
	public String getOriginalManufacturerID()
	{
		return this.orig_man_id;
	}

	public void setUserRAMStart(String uram_start)
	{
		this.usr_ram_start=Integer.parseInt(uram_start);
	}
	
	public int getUserRAMStart()
	{
		return this.usr_ram_start;
	}
	
	public void setUserRAMEnd(String uram_end)
	{
		this.usr_ram_end=Integer.parseInt(uram_end);
	}

	public int getUserRAMEnd()
	{
		return this.usr_ram_end;
	}
	
	public void setUserEEPROMStart(String ueeprom_start)
	{
		this.usr_eeprom_start=Integer.parseInt(ueeprom_start);
	}

	public int getUserEEPROMStart()
	{
		return this.usr_eeprom_start;
	}

	public void setUserEEPROMEnd(String ueeprom_end)
	{
		this.usr_eeprom_end=Integer.parseInt(ueeprom_end);
	}

	public int getUserEEPROMEnd()
	{
		return this.usr_eeprom_end;
	}	

	public void setRunErrorAddress(String readdress)
	{
		this.run_error_addr=Integer.parseInt(readdress);
	}
	
	public int getRunErrorAdress()
	{
		return this.run_error_addr;
	}
	
	
	public void setAssociationTabPointer(String atp)
	{
		this.ass_tab_ptr=Integer.parseInt(atp);
	}
	
	public int getAssociationTabPointer()
	{
		return this.ass_tab_ptr;
	}
	
	public void setComTabPointer(String ctp)
	{
		this.comm_tab_ptr=Integer.parseInt(ctp);
	}

	public int getComTabPointer()
	{
		return this.comm_tab_ptr;
	}

	public void setManufacturerDataAddress(String mdaddress)
	{
		this.mandata_addr=Integer.parseInt(mdaddress);
	}

	public int getManufacturerDataAdress()
	{
		return this.mandata_addr;
	}
	
	public void setManufacturerDataSize(String size)
	{
		this.mandata_size=Integer.parseInt(size);
	}

	public int getManufacturerDataSize()
	{
		return this.mandata_size;
	}
	
	public void setManufacturerIdAddress(String midaddress)
	{
		this.manid_addr=Integer.parseInt(midaddress);
	}

	public int getManufacturerIdAdress()
	{
		return this.manid_addr;
	}
	
	public void setRouteConterAddress(String rca)
	{
		this.routecnt_addr=Integer.parseInt(rca);
	}
	
	public int getRouteCounterAddress()
	{
		return this.routecnt_addr;
	}
	
	public void setManIDProtected(boolean b)
	{
		this.manid_protected=b;
	}
	
	/**
	 * @param string
	 */
	public void setManIDProtected(String string)
	{
		if(string.equals("true"))
		{
			this.setManIDProtected(true);
		}
		else
		{
			this.setManIDProtected(false);	
		}
	}
	
	public boolean isManIDProtected()
	{
		return this.manid_protected;	
	}
	
	public void setMaskVersionName(String name)
	{
		this.maskversion_name=name;
	}
	
	public String getMaskVersionName()
	{
		return this.maskversion_name;
	}
	
	/**
	 * 
	 * @see basys.XMLStringRepresentor#getXMLString()
	 */
	public String getXMLString()
	{
		String s="";
		
		s+="<application id=\""+this.getProgramID()+"\">\n";
		
		s+="\t<mask-version>"+this.getMaskVersion()+"</mask-version>\n";
		s+="\t<mask-version-name>"+this.getMaskVersionName()+"</mask-version-name>\n";
		s+="\t<name>"+this.getName()+"</name>\n";
		s+="\t<version>"+this.getVersion()+"</version>\n";
		s+="\t<device-type>"+this.getDeviceType()+"</device-type>\n";
		s+="\t<pei-type>"+this.getPEIType()+"<pei-type>\n";
		s+="\t<addr-table-address>"+this.getAddressTableAddress()+"</addr-table-address>\n";
		s+="\t<addr-table-size>"+this.getAddressTableSize()+"</addr-table-size>\n";
		s+="\t<association-table-address>"+this.getAssociationTableAddress()+"</association-table-address>\n";
		s+="\t<association-table-size>"+this.getAssociationTableSize()+"</association-table-size>\n";
		s+="\t<association-table-pointer>"+this.getAssociationTabPointer()+"</association-table-pointer>\n";
		s+="\t<communication-table-address>"+this.getCommTableAddress()+"</communication-table-address>\n";
		s+="\t<communication-table-size>"+this.getCommTableSize()+"</communication-table-size>\n";
		s+="\t<communication-table-pointer>"+this.getComTabPointer()+"</communication-table-pointer>\n";
		s+="\t<manufacturer>"+this.getManufacturerID()+"</manufacturer>\n";
		s+="\t<original-manufacturer>"+this.getOriginalManufacturerID()+"</original-manufacturer>\n";
		s+="\t<eeprom-data>"+this.getEEPROMData()+"</eeprom-data>\n";
		s+="\t<eeprom-start>"+this.getUserEEPROMStart()+"</eeprom-start>\n";
		s+="\t<eeprom-end>"+this.getUserEEPROMEnd()+"</eeprom-end>\n";
		s+="\t<uram-start>"+this.getUserRAMStart()+"</uram-start>\n";
		s+="\t<uram-end>"+this.getUserRAMEnd()+"</uram-end>\n";
		s+="\t<run-error-address>"+this.getRunErrorAdress()+"</run-error-address>\n";
		s+="\t<manufacturer-data-address>"+this.getManufacturerDataAdress()+"</manufacturer-data-address>\n";
		s+="\t<manufacturer-data-size>"+this.getManufacturerDataSize()+"</manufacturer-data-size>\n";
		s+="\t<manufacturer-id-address>"+this.getManufacturerIdAdress()+"</manufacturer-id-address>\n";
		s+="\t<manufacturer-id-protected>"+this.isManIDProtected()+"</manufacturer-id-protected>\n";
		s+="\t<route-counter-address>"+this.getRouteCounterAddress()+"</route-counter-address>\n";
		
		s+="</application>\n";
		
		return s;
	}
	
	/**
	 * Returns the DOM representation of the data
	 * @param doc owner document
	 * @return the DOM data Element node
	 */
	public Element getDOMElement(Document doc)
	{
		Element root=doc.createElement("application");
		root.setAttribute("id", this.getProgramID());
		
		root.appendChild(doc.createElement("mask-version")).appendChild(doc.createTextNode(this.getMaskVersion()));
		root.appendChild(doc.createElement("mask-version-name")).appendChild(doc.createTextNode(this.getMaskVersionName()));
		root.appendChild(doc.createElement("name")).appendChild(doc.createTextNode(this.getName()));
		root.appendChild(doc.createElement("version")).appendChild(doc.createTextNode(this.getVersion()));
		root.appendChild(doc.createElement("device-type")).appendChild(doc.createTextNode(""+this.getDeviceType()));
		root.appendChild(doc.createElement("pei-type")).appendChild(doc.createTextNode(""+this.getPEIType()));
		root.appendChild(doc.createElement("addr-table-address")).appendChild(doc.createTextNode(""+this.getAddressTableAddress()));
		root.appendChild(doc.createElement("addr-table-size")).appendChild(doc.createTextNode(""+this.getAddressTableSize()));
		root.appendChild(doc.createElement("association-table-address")).appendChild(doc.createTextNode(""+this.getAssociationTableAddress()));
		root.appendChild(doc.createElement("association-table-size")).appendChild(doc.createTextNode(""+this.getAssociationTableSize()));
		root.appendChild(doc.createElement("association-table-pointer")).appendChild(doc.createTextNode(""+this.getAssociationTabPointer()));
		root.appendChild(doc.createElement("communication-table-address")).appendChild(doc.createTextNode(""+this.getCommTableAddress()));
		root.appendChild(doc.createElement("communication-table-size")).appendChild(doc.createTextNode(""+this.getCommTableSize()));
		root.appendChild(doc.createElement("communication-table-pointer")).appendChild(doc.createTextNode(""+this.getComTabPointer()));
		root.appendChild(doc.createElement("manufacturer")).appendChild(doc.createTextNode(""+this.getManufacturerID()));
		root.appendChild(doc.createElement("original-manufacturer")).appendChild(doc.createTextNode(""+this.getOriginalManufacturerID()));
		root.appendChild(doc.createElement("eeprom-data")).appendChild(doc.createTextNode(""+this.getEEPROMData()));
		root.appendChild(doc.createElement("eeprom-start")).appendChild(doc.createTextNode(""+this.getUserEEPROMStart()));
		root.appendChild(doc.createElement("eeprom-end")).appendChild(doc.createTextNode(""+this.getUserEEPROMEnd()));
		root.appendChild(doc.createElement("uram-start")).appendChild(doc.createTextNode(""+this.getUserRAMStart()));
		root.appendChild(doc.createElement("uram-end")).appendChild(doc.createTextNode(""+this.getUserRAMEnd()));
		root.appendChild(doc.createElement("run-error-address")).appendChild(doc.createTextNode(""+this.getRunErrorAdress()));
		root.appendChild(doc.createElement("manufacturer-data-address")).appendChild(doc.createTextNode(""+this.getManufacturerDataAdress()));
		root.appendChild(doc.createElement("manufacturer-data-size")).appendChild(doc.createTextNode(""+this.getManufacturerDataSize()));
		root.appendChild(doc.createElement("manufacturer-id-address")).appendChild(doc.createTextNode(""+this.getManufacturerIdAdress()));
		root.appendChild(doc.createElement("manufacturer-id-protected")).appendChild(doc.createTextNode(""+this.isManIDProtected()));
		root.appendChild(doc.createElement("route-counter-address")).appendChild(doc.createTextNode(""+this.getRouteCounterAddress()));
		
		return root;
	}

	public String toString()
	{
		return this.getName();
	}
		
	
}
