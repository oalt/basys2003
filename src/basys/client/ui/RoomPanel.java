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
package basys.client.ui;

import basys.LocaleResourceBundle;
import basys.client.Project;
import basys.client.ui.tablemodels.*;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;



import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.w3c.dom.*;

import org.apache.log4j.Logger;

/**
 * RoomPanel.java
 * 
 * 
 * @author	oalt
 * @version $Id: RoomPanel.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class RoomPanel extends JPanel implements Observer
{
	private static Logger logger=Logger.getLogger(RoomPanel.class);
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	
	private Project p;
	private String roomID;
	private InstallationDeviceTableModel idevmodel;
	private JunctionboxTableModel jbmodel;
	private BusDeviceTablemodel bdmodel;
	
	
	public RoomPanel(Project p, String roomID) 
	{
		this.p=p;
		this.roomID=roomID;
		this.idevmodel=new InstallationDeviceTableModel(p, roomID);
		
		this.initUI();
		//this.setBackground(Color.WHITE);
	}
	
	private void initUI()
	{
		this.setLayout(new VerticalFlowLayout());
		
		this.add(new JLabel("<html><h1><u>"+p.getArchitecturalDataModel().getName(this.roomID), JLabel.CENTER));
		
		String label=locale.getString("l.installedComponents");
		RoomDnDTable roomtable = new RoomDnDTable(this.idevmodel, this.p, this.roomID);
		
		// Popup Menu
		roomtable.addMouseListener(new TablePopupListener(roomtable, this.p));
		
		this.add( new MyTablePanel(roomtable, label));	
		
		
		label=locale.getString("l.junctionboxes");
		jbmodel=new JunctionboxTableModel(this.p, this.roomID);
		
		StructuralDnDTable juncboxtable = new StructuralDnDTable(this.jbmodel, this.p.getArchitecturalDataModel(),
								   								 roomID);
		// Popup Menu
		juncboxtable.addMouseListener(new TablePopupListener(juncboxtable, this.p));
		
		this.add( new MyTablePanel(juncboxtable, label));
		
		label=locale.getString("l.busdevices");
		bdmodel=new BusDeviceTablemodel(this.p, this.roomID);
		this.add( new MyTablePanel( new DnDTable(this.bdmodel) , label));
	}

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg)
	{
		this.idevmodel.fireTableDataChanged();
		this.jbmodel.fireTableDataChanged();
		this.bdmodel.fireTableDataChanged();
	}
	
	
	/*
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		ArchitecturalDataModel amodel=this.p.getArchitecturalDataModel();
		
		Node n=amodel.getDataRootNode(this.roomID);
		NodeList nl=((Element)n).getElementsByTagName("enddevice");
		
		for(int cnt=0; cnt<nl.getLength(); cnt++)
		{
			int x=(int)Double.parseDouble(amodel.readDOMNodeValue(nl.item(cnt), new StringTokenizer("location/point/x", "/")));
			int y=(int)Double.parseDouble(amodel.readDOMNodeValue(nl.item(cnt), new StringTokenizer("location/point/y", "/")));
			
			int type=Integer.parseInt(amodel.readDOMNodeValue(nl.item(cnt), new StringTokenizer("type", "/")));
			
			switch(type)
			{
				case NewEndDeviceDialog.LAMP:
					g.drawImage(Util.getIcon("lamp16x16.png").getImage(), x, y, this);
				break;
				case NewEndDeviceDialog.DIMMABLE_LAMP:
					g.drawImage(Util.getIcon("dimminglamp16x16.png").getImage(), x, y, this);	
				break;
				case NewEndDeviceDialog.VALVE:
					g.drawImage(Util.getIcon("valve16x16.png").getImage(), x, y, this);
				break;
				case NewEndDeviceDialog.JALOUSIE:
					g.drawImage(Util.getIcon("jal16x16.png").getImage(), x, y, this);
				break;
			}
			
		
		}
		
			
		
	}
	*/
	
	



}
