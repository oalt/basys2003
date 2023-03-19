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
package basys.client.ui.tablemodels;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import basys.LocaleResourceBundle;
import basys.client.Project;
import basys.datamodels.installation.InstallationModel;
import basys.datamodels.architectural.ArchitecturalDataModel;
import basys.eib.EIBGrpaddress;

import javax.swing.table.AbstractTableModel;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * InstallationDeviceTableModel.java
 * 
 * 
 * @author	oalt
 * @version $Id: InstallationDeviceTableModel.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class InstallationDeviceTableModel extends AbstractTableModel
{
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	
	public static final int LAMP 			= 1;
	public static final int DIMMABLE_LAMP 	= 2;
	public static final int VALVE			= 3;
	public static final int JALOUSIE		= 4;
	public static final int SENSOR			= 100;
	
	private Project p;
	private String roomID;
	private String[] colnames={"tc.name", "tc.idevtype", "l.bussystem", "tc.actuator", "l.il", "tc.manu", "tc.actuatortype", 
							   "tc.funcgroupname", "tc.addresses"};
	
	/**
	 * 
	 */
	public InstallationDeviceTableModel(Project p, String roomID)
	{
		super();
		this.p=p;
		this.roomID=roomID;
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount()
	{
		ArchitecturalDataModel amodel=this.p.getArchitecturalDataModel();
		
		Node n=amodel.getDataRootNode(this.roomID);
		NodeList nl=((Element)n).getElementsByTagName("enddevice");
		return nl.getLength();
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount()
	{
		return this.colnames.length;
	}
	
	public String getColumnName(int col) 
	{
		return locale.getString(colnames[col]);
	}
	
	public Class getColumnClass(int c) 
	{
        return getValueAt(0, c).getClass();
    }
        
	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		ArchitecturalDataModel amodel=this.p.getArchitecturalDataModel();
		
		Node n=amodel.getDataRootNode(this.roomID);
		NodeList nl=((Element)n).getElementsByTagName("enddevice");
		
		
		InstallationModel imodel=this.p.getInstallationModel();
		
		String enddeviceid=amodel.getEndDeviceID(nl.item(rowIndex));
				
		Object o="";
		
		switch(columnIndex)
		{
			case 0: // installation device name
				o=imodel.getName(enddeviceid);
			break;
			case 1: // installation device type
				String type=amodel.readDOMNodeValue(nl.item(rowIndex), new StringTokenizer("type", "/"));
				int t=Integer.parseInt(type);
				switch(t)
				{
					case LAMP:
						o=locale.getString("l.lamp");
					break;
					case DIMMABLE_LAMP:
						o=locale.getString("l.dimlamp");
					break;
					case VALVE:
						o=locale.getString("l.valve");
					break;
					case JALOUSIE:
						o=locale.getString("l.jal");
					break;
					case SENSOR:
						o=locale.getString("l.sensor");
					break;
				}
			break; 
			case 2:	// bus system
				o=imodel.getPropertyByName(enddeviceid, "bussystem");
			break;
			case 3: // actuator
				Vector conns=(Vector)imodel.getConnections(enddeviceid);
				
				
				for(Enumeration e=conns.elements(); e.hasMoreElements(); )
				{
					String con=(String)e.nextElement();
					
					String bussystem=imodel.getPropertyByName(enddeviceid, "bussystem");
					if(bussystem != null)
					{
						if(con.startsWith("functiongroup") && 
						   bussystem.equals("EIB"))
						{
							String eibid=imodel.getEIBDeviceID(con);
							o=imodel.getName(eibid);
							break;
						}
					}
				}
				
			
			break;
			
			case 4:
				conns=(Vector)imodel.getConnections(enddeviceid);
				
				String eid="";
				
				for(Enumeration e=conns.elements(); e.hasMoreElements(); )
				{
					String con=(String)e.nextElement();
					
					String bussystem=imodel.getPropertyByName(enddeviceid, "bussystem");
					if(bussystem != null)
					{
						if(con.startsWith("functiongroup") && 
						   bussystem.equals("EIB"))
						{
							eid=imodel.getEIBDeviceID(con);
							break;
						}
					}
					
				}
				o=amodel.getInstallationLocationName(eid);
			break;
			
			case 5: // manufacturer
				conns=(Vector)imodel.getConnections(enddeviceid);
				
				
				for(Enumeration e=conns.elements(); e.hasMoreElements(); )
				{
					String con=(String)e.nextElement();
					
					String bussystem=imodel.getPropertyByName(enddeviceid, "bussystem");
					if(bussystem != null)
					{
						if(con.startsWith("functiongroup") && 
						   bussystem.equals("EIB"))
						{
							String eibid=imodel.getEIBDeviceID(con);
							o=imodel.getPropertyByName(eibid, "manufacturer");
							break;
						}
					}
				}
			break;
			case 6: // device type
				conns=(Vector)imodel.getConnections(enddeviceid);
				
				
				for(Enumeration e=conns.elements(); e.hasMoreElements(); )
				{
					String con=(String)e.nextElement();
					
					String bussystem=imodel.getPropertyByName(enddeviceid, "bussystem");
					if(bussystem != null)
					{
						if(con.startsWith("functiongroup") && 
						   bussystem.equals("EIB"))
						{
							String eibid=imodel.getEIBDeviceID(con);
							o=imodel.getPropertyByName(eibid, "device-name");
							break;
						}
					}
				}
			break;
			case 7: // function group name 
				conns=(Vector)imodel.getConnections(enddeviceid);
				
				
				for(Enumeration e=conns.elements(); e.hasMoreElements(); )
				{
					String con=(String)e.nextElement();
					
					String bussystem=imodel.getPropertyByName(enddeviceid, "bussystem");
					if(bussystem != null)
					{
						if(con.startsWith("functiongroup") && 
						   bussystem.equals("EIB"))
						{
							o=imodel.getName(con);
							break;
						}
					}
				}
			break;
			// Address(e(s)
			case 8:
				conns=(Vector)imodel.getConnections(enddeviceid);
				
				
				for(Enumeration e=conns.elements(); e.hasMoreElements(); )
				{
					String con=(String)e.nextElement();
					
					String bussystem=imodel.getPropertyByName(enddeviceid, "bussystem");
					if(bussystem != null)
					{
						if(con.startsWith("functiongroup") && 
						   bussystem.equals("EIB"))
						{
							
							Vector addrs=(Vector)imodel.getGroupAddresses(con);
							
							for(Enumeration e3=addrs.elements(); e3.hasMoreElements(); )
							{
								o=o+((EIBGrpaddress)e3.nextElement()).toString()+ " ";
							}
							break;
						}
					}
				}
			break;
			
		}
		
		return o;
	}
}