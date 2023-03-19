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
package basys.client.eib;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;

import basys.client.Project;
import basys.datamodels.installation.InstallationModel;
import basys.datamodels.eib.EIBDevicesDataModel;
import basys.eib.EIBApplication;
import basys.eib.EIBDeviceFunction;
import basys.eib.EIBGrpaddress;

/**
 * EIBProgramConfigurator.java
 * 
 * 
 * @author	oalt
 * @version $Id: EIBProgramConfigurator.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class EIBProgramConfigurator
{
	private static Logger logger=Logger.getLogger(EIBProgramConfigurator.class);
	
	private Project p;
	private String deviceID;
	private boolean centralSwitching=false;
	
	private int[] memoryData;
	private EIBApplication app;
	

	/**
	 * 
	 */
	public EIBProgramConfigurator(Project p, String deviceID, boolean centralSwitching)
	{
		super();
		this.p=p;
		this.deviceID=deviceID;
		this.centralSwitching=centralSwitching;
		
		EIBDevicesDataModel devmodel=(EIBDevicesDataModel)this.p.getApplication().getBusDeviceDataModel("EIB");
		InstallationModel imodel=this.p.getInstallationModel();
		
		String devmodelID=imodel.getPropertyByName(deviceID, "device-id");
		this.app=devmodel.getApplication(devmodelID);
	
		int maskVersion=Integer.parseInt(this.app.getMaskVersion());
		
		if(maskVersion<=17)
		{
			this.memoryData=new int[256];
			int[] eeprom_data=this.app.getEEPROMDataArray();
			for(int i=0; i<256; i++)
			{
				this.memoryData[i]=eeprom_data[i];	// copy eeprom default data to array
			}
		
			this.initTables();
		}
		else
		{
		}
		
	}
	
	/*
	public int[] getAddressTable()
	{
		return null;
	}
	
	public int[] getAssociationTable()
	{
		return null;
	}
	
	public int[] getCommObjectTable()
	{
		return null;
	}
	
	public int[] getSystemParameters()
	{
		return null;	
	}
	*/
	
	
	public int[] getCompleteMemory()
	{
		return this.memoryData;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCompleteMemoryString()
	{
		
		String s="\n         ";
		
		int cnt=0;
		int memaddr=this.app.getUserEEPROMStart();
		
		for(int i=0; i<16; i++)
		{
			s+=Integer.toHexString(i).toUpperCase()+"  ";
		}
		s+="\n";
		
		
		while(cnt<this.memoryData.length)
		{
			if(cnt % 16 == 0 || cnt==0)
			{
				s+="\n0x"+Integer.toHexString(memaddr).toUpperCase()+":  ";	
				memaddr+=16;
			}
			if(memoryData[cnt]<16)
			{
				s+="0";
			}
			s+=Integer.toHexString(memoryData[cnt]).toUpperCase()+" ";
			cnt++;
		}
		
		return s;
	}
	
	/**
	 * Set EIB device tables (address, com object, association).
	 *
	 */
	private void initTables()
	{
		InstallationModel imodel=this.p.getInstallationModel();
		
		Vector funcs=(Vector)imodel.getUsedDeviceFunctions(this.deviceID);	
		
		this.memoryData[this.app.getAddressTableAddress()-0x100]=funcs.size()+1; // address table length
		
		int ats=this.app.getAssociationTableAddress()+1;
		int atl=this.app.getAddressTableSize();
		
		int cosize=(this.app.getCommTableSize()-2)/3;
		
		// init association table with no associations
		int costart=this.app.getCommTableAddress()+2;
		
		// association table length == cosize (only one association per object)
		this.memoryData[ats-1-0x100]=cosize;
		
		logger.debug("Com Obj. size: "+cosize);
		
		for(int i=0; i<cosize; i++)
		{
			this.memoryData[ats-0x100+(i*2)]=0xFE;
			this.memoryData[(ats-0x100)+(i*2)+1]=i;
			
			// disable all com objects
			this.memoryData[(costart-0x100+(i*3))+1]&=0xFB;
		}
		
		// sort addresses
		// The EIB device searches for a matching address in ascending order. If the next address in the table is
		// greater than the searched address the device stops the search and the bus telegram will not accepted.
		// Therfore the address table must be sorted !!
		
		int index=this.app.getAddressTableAddress()-0x100+3;
		
		Vector tmpaddrtab=new Vector();
		
		for(int cnt=0; cnt<funcs.size(); cnt++)
		{
			EIBDeviceFunction func=(EIBDeviceFunction)funcs.get(cnt);
			EIBGrpaddress addr=new EIBGrpaddress(func.getGroupAddressCount());
			
			int ind = 0;
			
			for(Enumeration e=tmpaddrtab.elements(); e.hasMoreElements(); )
			{
				EIBGrpaddress a=(EIBGrpaddress)e.nextElement();
				
				if(addr.isGreaterThan(a))
				{
					ind++;
				}
			}
			tmpaddrtab.add(ind, addr);
		}
		
		// set associations
		int addrcnt = 1;
		
		for(Enumeration e=tmpaddrtab.elements(); e.hasMoreElements(); )
		{
			EIBGrpaddress addr=(EIBGrpaddress)e.nextElement();
			
		    this.memoryData[index]   = addr.getHigh() & 0xFF;
			this.memoryData[index+1] = addr.getLow() & 0xFF;
			index=index + 2;
		
			for(int cnt=0; cnt<funcs.size(); cnt++)
			{
				EIBDeviceFunction func=(EIBDeviceFunction)funcs.get(cnt);
				EIBGrpaddress ga=new EIBGrpaddress(func.getGroupAddressCount());
				
				if(ga.equals(addr))
				{
					// set association
					int co=func.getComObject();
					this.memoryData[ ats - 0x100 + co * 2  ]=addrcnt; // set association
						
					// enable com object, set comunication enable bit
					this.memoryData[((costart-0x100)+(co*3))+1] |= 0x04;
				}
			}
			
			addrcnt++;
		
		}
	}
	
	/**
	 * Return EIBApplication
	 * @return
	 */
	public EIBApplication getEIBApplication()
	{
		return this.app;
	}
}
