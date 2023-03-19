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
package basys.eib;

import basys.Util;
import basys.eib.exceptions.EIBAddressFormatException;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

/**
 * Filename:
 * Decription:
 * Copyright:   Copyright (c) 2001
 *
 * @author
 * @version $Id: EIBFrameDialog.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */

public class EIBFrameDialog extends JDialog
{

	EIBConnection egc;
	EIBFrame eibframe = null;

	JPanel mainpanel;
	GridLayout mainGridLayout = new GridLayout(8, 1);
	JComboBox priorityCombo;
	JCheckBox repeatCheckBox;
	JTextField srcAddr1;
	JTextField srcAddr2;
	JTextField srcAddr3;
	JCheckBox isPhAddrCheckBox;
	JTextField destAddr1;
	JTextField destAddr2;
	JTextField destAddr3;
	JComboBox rcCombo;
	JComboBox tpciCombo;
	JComboBox apciCombo;
	JComboBox datalenCombo;
	JTextField[] dataField;
	JTextField frameField;

	JButton sendButton;
	JButton exitButton;

	/**
	 * Constructor
	 */
	public EIBFrameDialog(EIBConnection egc)
	{
		this.egc = egc;

		initDialog();
		this.updateFrameData();

		this.pack();
		this.setResizable(false);

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
	 */
	private void initDialog()
	{

		this.setTitle("EIB Frame Dialog - "+this.egc.getConnectionType());
		
		this.getContentPane().setLayout(new BorderLayout());

		this.mainpanel = new JPanel(this.mainGridLayout);

		// Control Field
		JPanel panel1 = new JPanel();
		panel1.add(new JLabel("Priority: "));

		panel1.setBorder(new TitledBorder("Control Field"));
		String[] prioritynames = { "System", "High Priority", "Alarm", "Low Priority" };
		this.priorityCombo = new JComboBox(prioritynames);
		this.priorityCombo.setSelectedIndex(3);
		this.priorityCombo.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		panel1.add(this.priorityCombo);

		this.repeatCheckBox = new JCheckBox("Repeat");
		this.repeatCheckBox.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		panel1.add(this.repeatCheckBox);

		this.mainpanel.add(panel1);

		// Source Address
		JPanel panel2 = new JPanel();

		panel2.setBorder(new TitledBorder("Source Address (Physical Address)"));
		panel2.add(new JLabel("Zone: "));
		this.srcAddr1 = new JTextField("1", 3);
		this.srcAddr1.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		panel2.add(this.srcAddr1);

		panel2.add(new JLabel("Line: "));
		this.srcAddr2 = new JTextField("2", 3);
		this.srcAddr2.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		panel2.add(this.srcAddr2);

		panel2.add(new JLabel("Node: "));
		this.srcAddr3 = new JTextField("1", 3);
		this.srcAddr3.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		panel2.add(this.srcAddr3);

		this.mainpanel.add(panel2);

		// Destination Address
		JPanel panel3 = new JPanel();
		panel3.setBorder(new TitledBorder("Destination Address"));
		this.isPhAddrCheckBox = new JCheckBox("Physical Address");
		this.isPhAddrCheckBox.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		panel3.add(this.isPhAddrCheckBox);

		this.destAddr1 = new JTextField("0", 3);
		this.destAddr1.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		panel3.add(this.destAddr1);

		panel3.add(new JLabel(":"));

		this.destAddr2 = new JTextField("1", 3);
		this.destAddr2.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		panel3.add(this.destAddr2);

		panel3.add(new JLabel(":"));

		this.destAddr3 = new JTextField("1", 3);
		this.destAddr3.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		panel3.add(this.destAddr3);

		this.mainpanel.add(panel3);

		// Routing Counter
		JPanel panel4 = new JPanel();
		panel4.setBorder(new TitledBorder("Routing Counter"));
		String[] rcName = { "0", "1", "2", "3", "4", "5", "6", "7" };
		this.rcCombo = new JComboBox(rcName);
		this.rcCombo.setSelectedIndex(6);
		this.rcCombo.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		panel4.add(new JLabel("Routing Counter: "));
		panel4.add(this.rcCombo);

		this.mainpanel.add(panel4);

		// TPCI
		JPanel panel5 = new JPanel();
		panel5.setBorder(new TitledBorder("TPCI"));

		panel5.add(new JLabel("TPCI: "));
		this.tpciCombo = new JComboBox(EIBFrame.getTPCIStringList());
		this.tpciCombo.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		panel5.add(this.tpciCombo);

		this.mainpanel.add(panel5);

		// APCI
		JPanel panel6 = new JPanel();
		panel6.setBorder(new TitledBorder("APCI"));

		panel6.add(new JLabel("APCI: "));
		this.apciCombo = new JComboBox(EIBFrame.getAPCIStringList());
		this.apciCombo.setSelectedIndex(2);
		this.apciCombo.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		panel6.add(this.apciCombo);

		this.mainpanel.add(panel6);

		// Data
		JPanel panel7 = new JPanel();
		panel7.setBorder(new TitledBorder("Data"));

		panel7.add(new JLabel("Data length: "));

		String[] datalenString = { "short Data", "1", "2", "3", "4", "5", "6" };
		this.datalenCombo = new JComboBox(datalenString);
		this.datalenCombo.setActionCommand("datalen");
		this.datalenCombo.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		panel7.add(datalenCombo);

		panel7.add(new JLabel("Data: "));

		this.dataField = new JTextField[7];
		for (int cnt = 0; cnt < 7; cnt++)
		{
			this.dataField[cnt] = new JTextField("0", 3);
			if (cnt != 0)
			{
				this.dataField[cnt].setEditable(false);
				this.dataField[cnt].setText("");
			}
			this.dataField[cnt].addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					handleEvent(e);
				}
			});
			panel7.add(this.dataField[cnt]);
		}
		this.mainpanel.add(panel7);

		//EIB Frame
		JPanel panel8 = new JPanel();
		panel8.setBorder(new TitledBorder("EIB Frame"));

		panel8.add(new JLabel("Frame: "));

		this.frameField = new JTextField(41);
		this.frameField.setEditable(false);
		panel8.add(this.frameField);
		this.mainpanel.add(panel8);

		this.getContentPane().add(this.mainpanel, BorderLayout.CENTER);

		// Buttons
		JPanel buttonPanel = new JPanel();

		this.sendButton = new JButton("Send EIB Frame");
		this.sendButton.setActionCommand("Send");
		this.sendButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		buttonPanel.add(this.sendButton);
		this.exitButton = new JButton("Close");
		this.exitButton.setActionCommand("Exit");
		this.exitButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				handleEvent(e);
			}
		});
		buttonPanel.add(this.exitButton);

		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	}

	/**
	 *
	 */
	private void handleEvent(ActionEvent e)
	{

		String command = e.getActionCommand();

		if (command.equals("Send"))
		{
			this.updateFrameData();
			// send Frame
			egc.sendEIBFrame(this.eibframe);
		}
		else if (command.equals("Exit"))
		{
			this.dispose();
		}
		else if (command.equals("datalen"))
		{
			// clear and disable unused Text Fields
			for (int cnt = this.datalenCombo.getSelectedIndex() + 1; cnt < this.dataField.length; cnt++)
			{
				this.dataField[cnt].setText("");
				this.dataField[cnt].setEditable(false);
			}

			if (this.datalenCombo.getSelectedIndex() == 0) // short data
			{
				this.dataField[0].setText("0");
				this.dataField[0].setEditable(true);
			}
			else // long data
				{
				this.dataField[0].setText("---");
				this.dataField[0].setEditable(false);
				for (int cnt = 1; cnt <= this.datalenCombo.getSelectedIndex(); cnt++)
				{
					this.dataField[cnt].setText("0");
					this.dataField[cnt].setEditable(true);
				}
			}
			this.updateFrameData();
		}
		else
		{
			this.updateFrameData();
		}

	}

	/**
	 * Returns the entered source address
	 */
	private EIBPhaddress getSrcAddr()
	{
		int n1 = 1;
		int n2 = 2;
		int n3 = 1;

		try
		{
			n1 = Integer.parseInt(this.srcAddr1.getText());
			n2 = Integer.parseInt(this.srcAddr2.getText());
			n3 = Integer.parseInt(this.srcAddr3.getText());
		}
		catch (NumberFormatException nfe)
		{
			this.srcAddr1.setText("1");
			this.srcAddr2.setText("2");
			this.srcAddr3.setText("1");
		}

		EIBPhaddress srcaddr = new EIBPhaddress(n1, n2, n3);

		return srcaddr;

	}

	/**
	 * Returns the entered destination address
	 */
	private EIBAddress getDestAddr()
	{
		int n1 = 1;
		int n2 = 1;
		int n3 = 1;

		try
		{
			n1 = Integer.parseInt(this.destAddr1.getText());
			n2 = Integer.parseInt(this.destAddr2.getText());
			n3 = Integer.parseInt(this.destAddr3.getText());
		}
		catch (NumberFormatException nfe)
		{
			this.destAddr1.setText("1");
			this.destAddr2.setText("1");
			this.destAddr3.setText("1");
		}

		EIBAddress destaddr = null;
		if (this.isPhAddrCheckBox.isSelected())
		{
			destaddr = new EIBPhaddress(n1, n2, n3);
		}
		else
		{
			try
			{

				destaddr = new EIBGrpaddress("" + n1 + "/" + ((n2 << 8) | n3));
			}
			catch (EIBAddressFormatException afe)
			{
				afe.printStackTrace();
			}
		}

		return destaddr;

	}

	/**
	 *
	 */
	private int[] getData()
	{
		int[] data = null;
		;
		if (this.datalenCombo.getSelectedIndex() == 0)
		{
			data = new int[1];
			int i = 0;
			try
			{
				i = Integer.parseInt(this.dataField[0].getText());
			}
			catch (NumberFormatException nfe)
			{
				this.dataField[0].setText("0");
			}
			data[0] = i;
		}
		else
		{
			data = new int[this.datalenCombo.getSelectedIndex()];
			for (int cnt = 1; cnt <= this.datalenCombo.getSelectedIndex(); cnt++)
			{
				int i = 0;
				try
				{
					i = Integer.parseInt(this.dataField[cnt].getText());
				}
				catch (NumberFormatException nfe)
				{
					this.dataField[cnt].setText("0");
				}
				data[cnt - 1] = i;
			}
			//System.err.println("Debug: Datalen: "+ data.length);
		}
		return data;
	}

	/**
	 * Update EIBFrame with given data
	 */
	private void updateFrameData()
	{
		if (this.eibframe == null)
		{
			int[] data = { 0 };
			this.eibframe =
				new EIBFrame(false, 0, new EIBPhaddress(1, 2, 1), new EIBGrpaddress(1, 257), 6, 0, 0x80, data);
		}

		this.eibframe.setPriority(this.priorityCombo.getSelectedIndex());
		this.eibframe.setRepeat(this.repeatCheckBox.isSelected());
		this.eibframe.setSrcAddress(this.getSrcAddr());
		this.eibframe.setDestAddress(this.getDestAddr());
		this.eibframe.setRC(this.rcCombo.getSelectedIndex());

		// TPCI

		// APCI

		// data
		this.eibframe.setApdata(this.getData());

		this.frameField.setText(eibframe.toHexString());

	}
}