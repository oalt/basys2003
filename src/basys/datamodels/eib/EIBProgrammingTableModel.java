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
package basys.datamodels.eib;

import java.util.ResourceBundle;
import java.util.Vector;

import basys.LocaleResourceBundle;
import basys.client.Project;
import basys.datamodels.architectural.ArchitecturalDataModel;
import basys.datamodels.installation.InstallationModel;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

/**
 * EIBProgrammingTableModel.java
 * 
 * 
 * @author	oalt
 * @version $Id: EIBProgrammingTableModel.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class EIBProgrammingTableModel extends AbstractTableModel
{

	private Project p;
	
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	private static Logger logger=Logger.getLogger(EIBDeviceTableModel.class);
	
	private String[] colnames={"tc.name", "tc.manu", "tc.prod", "tc.phaddress", "tc.state", "l.il" };
	
	/**
	 * 
	 */
	public EIBProgrammingTableModel(Project p)
	{
		super();
		this.p=p;
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount()
	{
		InstallationModel imodel=this.p.getInstallationModel();
		return (imodel.getUnprogrammedEIBDeviceIDs()).size();
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount()
	{
		return this.colnames.length;
	}
	
	/**
	 * 
	 */
	public String getColumnName(int col) 
	{
		return locale.getString(colnames[col]);
	}
	
	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Object o="";
		
		InstallationModel imodel=this.p.getInstallationModel();
		
		Vector ids=(Vector)imodel.getUnprogrammedEIBDeviceIDs();
		
		String id=(String)ids.elementAt(rowIndex);
		
		switch(columnIndex)
		{
			// Name
			case 0:
				o=imodel.getName(id);
			break;
			// manufacturer
			case 1:
				o=imodel.getPropertyByName(id, "manufacturer");
			break;
			// product
			case 2:
				o=imodel.getPropertyByName(id, "device-name");
			break;
			// physical address
			case 3:
				o=imodel.getPropertyByName(id, "eib-physical-address");
			break;
			// state
			case 4:
				o=imodel.getPropertyByName(id, "device-state");
			break;
			case 5:
				ArchitecturalDataModel amodel=this.p.getArchitecturalDataModel();
				o=amodel.getInstallationLocationName(id);
			break;
		}
		
		return o;
	}

}
