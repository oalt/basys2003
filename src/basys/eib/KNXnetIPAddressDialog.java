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
 * Copyright (c) 2006        BASys 2003 Project, 
 *                           Vienna University of Technology, 
 *                           Department of Automation - Automation Systems group,
 *               			 B. Malinowsky
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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import basys.client.ui.VerticalFlowLayout;

/**
 * KNXnetIPAddressDialog
 * queries for a server control datapoint address
 * supports server network names with resolution or ip addresses 
 */
public class KNXnetIPAddressDialog extends JDialog {

	public KNXnetIPAddressDialog(String networkAddress) {
		initDialog(networkAddress);
		// center dialog
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		setLocation((screenSize.width - frameSize.width) / 2, 
			(screenSize.height - frameSize.height) / 2);		
	}

	public boolean isCanceled() { return canceled; } 
	public String getAddress() { return inetAddr; }
	
	private void initDialog(String networkAddress)
	{
		setTitle("KNXnet/IP server address (control datapoint)");

		JPanel mainp = new JPanel(new VerticalFlowLayout());
		mainp.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JPanel workp = new JPanel(new VerticalFlowLayout());		
		JPanel buttonp = new JPanel(new FlowLayout());
		
		workp.add(new JLabel("Insert IP adress or server name: "));		
		addr = new JTextField(networkAddress, 35);
		workp.add(addr);
		status = new JLabel(notResolved);
		workp.add(status);

		open = new JButton("Open");
		open.setActionCommand("Open");
		open.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) { handleEvent(e); }
		}
		);
		buttonp.add(open);
		cancel = new JButton("Cancel");
		cancel.setActionCommand("Cancel");
		cancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) { handleEvent(e); }
		}
		);
		buttonp.add(cancel);

		mainp.add(workp);
		mainp.add(buttonp);
		getContentPane().add(mainp);
		
		resolve();
		pack();
		setResizable(false);		
	}
	
	private void handleEvent(ActionEvent e)
	{
		String c = e.getActionCommand();
		if (c.equals("Open")) {
			if (resolve()) {
				canceled = false;
				dispose();
			}
		}
		else if (c.equals("Cancel")) {
			canceled = true;
			dispose();
		}
	}
	
	/**
	 * resolve
	 * try to resolve entered internet address 
	 */
	private boolean resolve()
	{
		try {
			InetAddress a = InetAddress.getByName(addr.getText().toString());
			inetAddr = a.getHostAddress();
			status.setText(resolved);
			return true;
		}
		catch (UnknownHostException e) {
			status.setText(notResolved);
			return false;
		}		
	}
	
	private JTextField addr;
	private JLabel status;
	private JButton cancel;
	private JButton open;
	private String inetAddr = "";
	private boolean canceled = true;
	private static final String notResolved = "Status: address unknown (invalid)";	
	private static final String resolved = "Status: address resolved (valid)";	
}
