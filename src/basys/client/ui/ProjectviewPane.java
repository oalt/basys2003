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

import java.awt.Dimension;
import java.util.ResourceBundle;

import basys.LocaleResourceBundle;
import basys.client.Project;
import basys.client.ui.event.ShowViewEvent;
import basys.client.ui.event.ShowViewEventListener;
import basys.client.ui.tablemodels.*;
import basys.client.ui.tree.ArchitecturalTree;
import basys.datamodels.installation.InstallationModel;
import basys.datamodels.architectural.ArchitecturalDataModel;

import javax.swing.*;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;

/**
 * ProjectviewPane.java
 * 
 * 
 * @author	oalt
 * @version $Id: ProjectviewPane.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class ProjectviewPane extends JSplitPane implements ShowViewEventListener
{
	private static Logger logger=Logger.getLogger(ProjectviewPane.class);
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	
	private Project p;
	private StructuralViewPane svp;
	
	public ProjectviewPane(Project p)
	{
		this.p=p;	
		initView();
		this.setPreferredSize(new Dimension(750, 550));
	}
	
	public void initView()
	{
		JPanel panel=new JPanel();
		
		ArchitecturalTree archtree=new ArchitecturalTree(p);
		
		archtree.setPreferredSize(new Dimension(200, 550));
		archtree.setMinimumSize(new Dimension(200, 550));
		
		this.add(archtree, JSplitPane.LEFT);
		
		JTabbedPane tabpane=new JTabbedPane();

		this.svp=new StructuralViewPane();
		
		tabpane.addTab(locale.getString("tabname.structural"), svp);
		

		this.add(tabpane, JSplitPane.RIGHT);
		
		this.p.getArchitecturalDataModel().addObserver(archtree);
		
		archtree.addShowViewEventListener(this);
		
		
	}
	
	/**
	 * @see basys.client.ui.event.ShowViewEventListener#updateView(basys.client.ui.event.ShowViewEvent)
	 */
	public void updateView(ShowViewEvent e)
	{
		
		
		String id=e.getSelectedObjectId();
		
		logger.debug("updateView called: "+id);
		
		ArchitecturalDataModel amodel=p.getArchitecturalDataModel();
		
		InstallationModel imodel=p.getInstallationModel();
		
		if(id.startsWith("project-"))
		{
			ProjectTableModel model=new ProjectTableModel(this.p, id);
			
			amodel.addObserver(model);
			
			StructuralDnDTable table=new StructuralDnDTable(model, amodel, id);
			
			table.addMouseListener(new TablePopupListener(table, this.p));
			
			String name=amodel.getName(id)+": ";
			
			svp.setStructuralViewPanel(new MyTablePanel(table, name+locale.getString("l.buildings")));
		}
		else if(id.startsWith("building-"))
		{
			BuildingTableModel model=new BuildingTableModel(this.p, id);
			
			amodel.addObserver(model);
			
			StructuralDnDTable table=new StructuralDnDTable(model, amodel, id);
			
			table.addMouseListener(new TablePopupListener(table, this.p));
			
			String name=amodel.getName(id)+": ";
			
			svp.setStructuralViewPanel(new MyTablePanel(table, name+locale.getString("l.floors")));
		}
		else if(id.startsWith("floor-"))
		{
			FloorTableModel model=new FloorTableModel(this.p, id);
			
			amodel.addObserver(model);
			
			StructuralDnDTable table=new StructuralDnDTable(model, amodel, id);
			
			table.addMouseListener(new TablePopupListener(table, this.p));
			
			String name=amodel.getName(id)+": ";
			
			svp.setStructuralViewPanel(new MyTablePanel(table, name+locale.getString("l.rooms")));
		}
		else if(id.startsWith("room-"))
		{
			RoomPanel rp=new RoomPanel(this.p, id);
			
			p.getArchitecturalDataModel().addObserver(rp);
			
			svp.setStructuralViewPanel(rp);
		}
		else if(id.startsWith("junctionbox-"))
		{
			BusDeviceTablemodel model=new BusDeviceTablemodel(this.p, id);
			
			amodel.addObserver(model);
			
			DnDTable table=new DnDTable(model);
			
			String name=amodel.getName(id)+": ";
			
			svp.setStructuralViewPanel(new MyTablePanel(table, name+locale.getString("l.busdevices")));
		}
		this.validate();
		
	}
	
}
