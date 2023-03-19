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


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import basys.LocaleResourceBundle;
import basys.client.ui.VerticalFlowLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * NewProjectDialog.java
 * 
 * 
 * @author	oalt
 * @version $Id: NewProjectDialog.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class NewProjectDialog extends JDialog implements ActionListener
{
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	
	private int exitState=0;
	private JTextField projectNameTextField;
	private String prefferedBusSystem = "EIB";
	
	/**
	 * @throws java.awt.HeadlessException
	 */
	public NewProjectDialog(Frame parent)
	{
		super(parent);
		this.setModal(true);
		
		this.setDefaultCloseOperation(
			JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				exitState=0;
				setVisible(false);
			}
		});
		
		initUI();
		this.pack();
		
		// Center frame
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
	 * Returns the project name from the text field
	 * @return the project name
	 */
	public String getProjectName()
	{
		if(this.projectNameTextField.getText().equals(""))
		{
			return "unbenannt";
		}
		else
		{
			return this.projectNameTextField.getText();
		}
	}
	
	/**
	 * Returns the preffered bus system as String.
	 * @return the preffered bus system.
	 */
	public String getPrefferedBusSystem()
	{
		return this.prefferedBusSystem;
	}
	
	/**
	 * Show the dialog.
	 * @return 0 if cancled and 1 if new project is selected.
	 */
	public int showDialog()
	{
		this.setVisible(true);
		while(this.isVisible())
		{
			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException ie)
			{
				
			}
			
		}
		return exitState;
	}
	
	
	/**
	 * Initialize dialog UI
	 *
	 */
	private void initUI()
	{
		String title=locale.getString("tit.newProject");
		
		JPanel mainp=new JPanel(new VerticalFlowLayout());
		JPanel cpanel=new JPanel(new VerticalFlowLayout());
		
		cpanel.setBorder(new TitledBorder(title));
		
		this.setTitle(title);
		
		cpanel.add(new JLabel(locale.getString("l.newProjectName")));
		projectNameTextField=new JTextField("");
		cpanel.add(projectNameTextField);
		
		cpanel.add(new JLabel(locale.getString("l.busSystemPrefferd")));
		
		ButtonGroup bg=new ButtonGroup();
		JPanel bgpanel=new JPanel(new FlowLayout());
		
		JRadioButton rbutton=new JRadioButton("EIB", true);
		rbutton.setActionCommand("EIB");
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);
		
		rbutton=new JRadioButton("LON");
		rbutton.setEnabled(false);
		rbutton.setActionCommand("LON");
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);
		
		rbutton=new JRadioButton("LCN");
		rbutton.setEnabled(false);
		rbutton.setActionCommand("LCN");
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);
		
		rbutton=new JRadioButton(locale.getString("l.noBusSystemPreffered"));
		rbutton.setEnabled(true);
		rbutton.setActionCommand("none");
		rbutton.addActionListener(this);
		bg.add(rbutton);
		bgpanel.add(rbutton);
		
		cpanel.add(bgpanel);
		
		mainp.add(cpanel);
		
		JPanel buttonp=new JPanel(new FlowLayout());
		
		JButton bu=new JButton(locale.getString("blabel.newProject"));
		bu.setActionCommand("newProject");
		bu.addActionListener(this);
		buttonp.add(bu);
		
		bu=new JButton(locale.getString("blabel.cancel"));
		bu.setActionCommand("cancel");
		bu.addActionListener(this);
		buttonp.add(bu);
		
		mainp.add(buttonp);
		
		mainp.doLayout();
		
		this.getContentPane().add(mainp);
		
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		String cmd=e.getActionCommand();
		if(e.equals("EIB"))
		{
			this.prefferedBusSystem="EIB";
		}
		else if(cmd.equals("none"))
		{
			this.prefferedBusSystem="none";	
		}
		else if(cmd.equals("newProject"))
		{
			this.exitState=1;
			this.setVisible(false);
		}
		else if(cmd.equals("cancel"))
		{
			this.exitState=0;
			this.setVisible(false);
		}
	}
	
	
}
