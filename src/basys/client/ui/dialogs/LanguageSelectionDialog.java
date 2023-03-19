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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;


import basys.LocaleResourceBundle;
import basys.client.ui.VerticalFlowLayout;

/**
 * LanguageSelectionDialog.java
 * 
 * 
 * @author	olli
 * @version $Id: LanguageSelectionDialog.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class LanguageSelectionDialog extends JDialog implements ActionListener
{
	
	
	private static Logger logger=Logger.getLogger(EIBProgrammingDialog.class);
	
	private final String[] langs={"deutsch", "english"};
	private JComboBox languageSelector;
	private Locale loc=new Locale("de", "DE");
	
	/**
	 * 
	 */
	public LanguageSelectionDialog()
	{
		super();
		this.initUI();
		this.pack();
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

	private void initUI()
	{
		this.setModal(true);
		
		this.setTitle("BASys 2003");
		
		JPanel mainp=new JPanel(new VerticalFlowLayout());
		mainp.add(new JLabel("Bitte Sprache auswählen/Please select language"));
		
		this.languageSelector=new JComboBox(langs);
		mainp.add(this.languageSelector);
		
		JPanel buttonp=new JPanel();
		JButton okButton=new JButton("Ok");
		okButton.setActionCommand("ok");
		okButton.addActionListener(this);
		
		buttonp.add(okButton);
		
		mainp.add(buttonp);
		
		this.getContentPane().add(mainp);
	}

	public Locale getSelectedLocale()
	{
		return this.loc;
	}
	
	public String getLocaleString()
	{
		return this.loc.getLanguage();	
	}
	
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		String cmd=e.getActionCommand();
		if(cmd.equals("ok"))
		{
			int sel=this.languageSelector.getSelectedIndex();
			switch(sel)
			{
				case 0:
					this.loc=new Locale("de", "DE");
				break;
				case 1:
					this.loc=new Locale("en", "EN");
				break;
			}
			this.dispose();
		}
		else
		{
			System.exit(0);
		}
	}
}
