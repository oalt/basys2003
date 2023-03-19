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
package basys.client.ui.dialogs;

import basys.LocaleResourceBundle;
import basys.Util;
import basys.client.ui.EIBFunctionsTable;
import basys.client.ui.VerticalFlowLayout;
import basys.eib.dataaccess.DataExtractor;
import basys.eib.dataaccess.EIBProduct;
import basys.datamodels.eib.EIBDeviceTableModel;
import basys.datamodels.eib.EIBDevicesDataModel;
import basys.datamodels.eib.EIBFunctionsTableModel;
import basys.eib.EIBApplication;
import basys.eib.EIBDeviceFunction;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import org.apache.log4j.Logger;

/**
 * EIBApplicationSelectorDialog.java
 * 
 * 
 * @author	oalt
 * @version $Id: EIBApplicationSelectorDialog.java,v 1.2 2006/09/14 13:42:41 fritzchen Exp $
 * 
 */
public class EIBApplicationSelectorDialog extends JDialog implements ListSelectionListener, ActionListener
{
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	private static Logger logger=Logger.getLogger(EIBApplicationSelectorDialog.class);
	
	private JList manulist;
	private JList prodlist;
	private JList applist;
	private JButton setFunctionGroupButton;
	private JButton addDeviceButton;
	private JButton modifyFunctionsButton;
	private JButton deleteButton;
	private JTable tab;
	
	private DataExtractor extractor;
	private EIBApplication eibapp=null;		// actual selected application
	
	private String installationLocation="REG";
	private String devtype="actuator";
	
	private EIBDevicesDataModel model;
	private EIBDeviceTableModel tabmodel;
	private EIBFunctionsTableModel funcmodel;
	private EIBFunctionsTable ftab;
	
	/**
	 * @param owner
	 * @throws java.awt.HeadlessException
	 */
	public EIBApplicationSelectorDialog(Frame owner, EIBDevicesDataModel model)
	{
		super(owner);
		
		this.model=model;
		
		this.setModal(true);
		
		initUI();
		
		this.pack();
				
		//		Center frame
		 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		 Dimension frameSize = this.getSize();
		
		 if (frameSize.height > screenSize.height)
		 {
			 frameSize.height = screenSize.height;
		 }
		 if (frameSize.width > screenSize.width)
		 {
			 frameSize.width = screenSize.width;
		 }
		 this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}
	
	/**
	 * Init dialog elements
	 *
	 */
	private void initUI()
	{
		this.setTitle(locale.getString("tit.eibdataadmin"));
		
		this.getContentPane().setLayout(new BorderLayout());
		JPanel mainp = new JPanel(new FlowLayout());
		
		JPanel centerpanel=new JPanel(new VerticalFlowLayout());
		
		File f=new File(Util.getDataPathPrefix()+"/global-data/eib/devicedata");
		
		// Selection lists
		JPanel listp=new JPanel();
		listp.setBorder(new TitledBorder(locale.getString("l.manufacturerfile")));
		ScrollPane sp=new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		sp.setSize(250,150);
		manulist=new JList();
		
		manulist.setListData(f.list(new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				if(name.endsWith(".xml"))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}));
		
		manulist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		manulist.addListSelectionListener(this);
		sp.add(manulist);
		listp.add(sp);
		mainp.add(listp);
		
		listp=new JPanel();
		listp.setBorder(new TitledBorder(locale.getString("l.product")));
		sp=new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		sp.setSize(250,150);
		prodlist=new JList();
		prodlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		prodlist.addListSelectionListener(this);
		sp.add(prodlist);
		listp.add(sp);
		mainp.add(listp);
		
		listp=new JPanel();
		listp.setBorder(new TitledBorder(locale.getString("l.application")));
		sp=new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		sp.setSize(250,150);
		applist=new JList();
		applist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		applist.addListSelectionListener(this);
		sp.add(applist);
		listp.add(sp);
		mainp.add(listp);
		
		centerpanel.add(mainp);
		
		// device type
		
		JPanel bgpanel=new JPanel(new FlowLayout());
		bgpanel.setBorder(new TitledBorder(locale.getString("l.devtype")));
		
		ButtonGroup bg=new ButtonGroup();
		
		JRadioButton rbutton=new JRadioButton(locale.getString("l.actuator"));
		rbutton.setActionCommand("actuator");
		rbutton.setSelected(true);
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);
		
		rbutton=new JRadioButton(locale.getString("l.sensor"), true);
		rbutton.setActionCommand("sensor");
		rbutton.setEnabled(true);
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);
		
		centerpanel.add(bgpanel);
		
		// installation location
		
		bgpanel=new JPanel(new FlowLayout());
		bgpanel.setBorder(new TitledBorder(locale.getString("l.il")));
		
		bg=new ButtonGroup();
		
		rbutton=new JRadioButton(locale.getString("l.ilREG"), true);
		rbutton.setActionCommand("REG");
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);

		rbutton=new JRadioButton(locale.getString("l.ilUP"));
		rbutton.setEnabled(true);
		rbutton.setActionCommand("UP");
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);

		rbutton=new JRadioButton(locale.getString("l.ilKI"));
		rbutton.setEnabled(true);
		rbutton.setActionCommand("KI");
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);

		rbutton=new JRadioButton(locale.getString("l.ilByDevice"));
		rbutton.setEnabled(true);
		rbutton.setActionCommand("ByDevice");
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);
		
		centerpanel.add(bgpanel);
		
		// Com object table
		
		funcmodel=new EIBFunctionsTableModel(new Vector());
		
		ftab=new EIBFunctionsTable(funcmodel);
		ftab.setPreferredScrollableViewportSize(new Dimension(800,100));
		JScrollPane ftsp=new JScrollPane(ftab, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		ftsp.setBorder(new TitledBorder(locale.getString("l.comobjects")));
		centerpanel.add(ftsp);
		
		// Middle button
		JPanel buttonp = new JPanel(new FlowLayout());
		
		this.setFunctionGroupButton=new JButton(locale.getString("blabel.setFunctionGroup"));
		this.setFunctionGroupButton.setActionCommand("set function group");
		this.setFunctionGroupButton.addActionListener(this);
		this.setFunctionGroupButton.setEnabled(false);
		buttonp.add(this.setFunctionGroupButton);
		
		this.addDeviceButton=new JButton(locale.getString("blabel.addDevice"));
		this.addDeviceButton.setActionCommand("add device");
		this.addDeviceButton.setEnabled(false);
		this.addDeviceButton.addActionListener(this);
		buttonp.add(this.addDeviceButton);
		
		centerpanel.add(buttonp);		
		
		
		// Scrollable Table
		JPanel tabpanel=new JPanel(new BorderLayout());
		tabmodel=new EIBDeviceTableModel(this.model);
		
		tab=new JTable(tabmodel);
		tab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		tab.setPreferredScrollableViewportSize(new Dimension(800,100));
		
		JScrollPane tscpanel=new JScrollPane(tab, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		tabpanel.add(tscpanel, BorderLayout.CENTER);
		
		centerpanel.add(tabpanel);
		
		
		// South buttons
		JPanel tabbuttonp = new JPanel(new FlowLayout());
		
		JButton closeButton=new JButton(locale.getString("blabel.close"));
		closeButton.setActionCommand("close");
		closeButton.addActionListener(this);
		tabbuttonp.add(closeButton);
		
		this.modifyFunctionsButton=new JButton(locale.getString("blabel.modifyFunctions"));
		this.modifyFunctionsButton.setActionCommand("modify functions");
		this.modifyFunctionsButton.setEnabled(false);
		this.modifyFunctionsButton.addActionListener(this);
		tabbuttonp.add(this.modifyFunctionsButton);
		
		this.deleteButton=new JButton(locale.getString("blabel.delete"));
		this.deleteButton.setActionCommand("delete");
		this.deleteButton.addActionListener(this);
		tabbuttonp.add(this.deleteButton);
		
		centerpanel.add(tabbuttonp);
		
		this.getContentPane().add(centerpanel, BorderLayout.CENTER);
	}

	/**
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		String[] emptylist={};
		if(e.getSource()==this.manulist && e.getValueIsAdjusting())
		{
			logger.info("Hersteller ausgewählt !!");
						
			this.extractor=new DataExtractor(Util.getDataPathPrefix()+"/global-data/eib/devicedata/"+manulist.getSelectedValue());
			this.prodlist.setListData(emptylist);
			this.applist.setListData(emptylist);
			
			this.prodlist.setListData(this.extractor.getEIBProducts());
			this.setFunctionGroupButton.setEnabled(false);
			this.funcmodel.setContent(new Vector());
			this.addDeviceButton.setEnabled(false);
		}
		else if(e.getSource()==this.prodlist && e.getValueIsAdjusting())
		{
			logger.info("Produkt gewählt !!");
			Vector pids=extractor.getProgramIDsForProduct(((EIBProduct)this.prodlist.getSelectedValue()).getID());
			
			this.applist.setListData(extractor.getAplications(pids));
			this.setFunctionGroupButton.setEnabled(false);
			this.funcmodel.setContent(new Vector());
			this.addDeviceButton.setEnabled(false);
		}
		else if(e.getSource()==this.applist && e.getValueIsAdjusting())
		{
			logger.info("Applikation gewählt !!");
			
			Vector v=extractor.getComObjectsForApplication(((EIBApplication)this.applist.getSelectedValue()).getProgramID());
			
			funcmodel.setContent(extractor.getNormalizedComObjects(v));		
			
			this.setFunctionGroupButton.setEnabled(true);
			
			this.addDeviceButton.setEnabled(true);
		}
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Event handler for buttons and radio buttons.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		String cmd=e.getActionCommand();
		
		
		if(cmd.equals("close"))			// close button
		{
			this.setVisible(false);
			this.dispose();
		}
		else if(cmd.equals("delete"))
		{
			int rowIndex=tab.getSelectedRow();
			this.tabmodel.removeEntry(rowIndex);
		}
		else if(cmd.equals("sensor"))	// device types
		{
			this.devtype="sensor";
		}
		else if(cmd.equals("actuator"))
		{
			this.devtype="actuator";
		}
		else if(cmd.equals("REG"))		// radio buttons
		{
			this.installationLocation="REG";
		}
		else if(cmd.equals("UP"))
		{
			this.installationLocation="UP";
		}
		else if(cmd.equals("KI"))
		{
			this.installationLocation="CI";
		}
		else if(cmd.equals("ByDevice"))
		{
			this.installationLocation="DEVICE";
		}
		else if(cmd.equals("add device"))
		{
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			eibapp=(EIBApplication)applist.getSelectedValue();
			extractor.setApplicationMaskData(eibapp);
			
			String id=model.addDevice(((EIBProduct)this.prodlist.getSelectedValue()).getName());
			model.setManufacturer(id, extractor.getManufacturerName(eibapp.getManufacturerID()));
			model.setInstallationType(id, this.installationLocation);
			model.setDeviceType(id, this.devtype);
			model.setApplication(id, eibapp);
			
			
			
			Vector funcs=this.funcmodel.getDeviceFunctions();
			logger.debug(funcs);
			
			for(Enumeration en=funcs.elements(); en.hasMoreElements(); )
			{
				Vector eibfunctions=(Vector)en.nextElement();
				String fgid=model.addFunctionGroup(id);
				for(Enumeration enu=eibfunctions.elements(); enu.hasMoreElements(); )
				{
					model.addFunction(fgid, (EIBDeviceFunction)enu.nextElement());
				}
			}
			
			tabmodel.fireTableDataChanged();
			
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
		}
		else if(cmd.equals("set function group"))
		{
			this.funcmodel.setFunctionGroup(this.ftab.getSelectedRows());
		}
	}
	
	

}
