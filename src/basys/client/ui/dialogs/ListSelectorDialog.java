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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * ListSelectorDialog.java
 * 
 * 
 * @author	oalt
 * @version $Id: ListSelectorDialog.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class ListSelectorDialog extends JDialog implements ActionListener, ListSelectionListener
{
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	
	JButton cbutton;
	private JList list;
	
	private Vector listdata;
	
	private Object value=null;
	
	public ListSelectorDialog(Frame parent, String title, Vector listdata) 
	{
		super(parent, title, true);
		this.listdata=listdata;
		
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

	public void initUI()
	{
		this.getContentPane().setLayout(new BorderLayout());
		
		this.list=new JList(this.listdata);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(this);
		JScrollPane listScrollPane = new JScrollPane(list);
		this.getContentPane().add(listScrollPane, BorderLayout.CENTER);
	
		JPanel buttonp=new JPanel(new FlowLayout());
		
		JButton button=new JButton(locale.getString("blabel.cancel"));
		button.setActionCommand("cancel");
		button.addActionListener(this);
		buttonp.add(button);
		
		cbutton=new JButton(locale.getString("blabel.continue"));
		cbutton.setActionCommand("continue");
		cbutton.setEnabled(false);
		cbutton.addActionListener(this);
		buttonp.add(cbutton);
		
		this.getContentPane().add(buttonp, BorderLayout.SOUTH);
	}

	public Object getSelection()
	{
		return this.value;
	}
	
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		String cmd=e.getActionCommand();
		
		if(cmd.equals("cancel"))
		{
			this.value=null;
			this.setVisible(false);
		}
		else if(cmd.equals("continue"))
		{
			this.value=list.getSelectedValue();
			this.setVisible(false);
		}
		
	}

	/**
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		this.cbutton.setEnabled(true);
	}
	
	
}
