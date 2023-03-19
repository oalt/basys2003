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

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import org.apache.log4j.Logger;

import java.util.*;

import basys.LocaleResourceBundle;
import basys.Util;
import basys.eib.event.*;
import basys.eib.exceptions.*;
import basys.eib.exceptions.EIBConnectionNotPossibleException;
import basys.eib.exceptions.EIBGatewayException;

/**
 * This class implements a swing table for tracing incomming EIB frames.
 *
 * @version $Id: EIBTelegrammtraceframe.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * @author  oalt
 */

public class EIBTelegrammtraceframe extends JFrame implements EIBFrameListener, ActionListener
{
	private static Logger logger = Logger.getLogger(EIBTelegrammtraceframe.class);
	private static ResourceBundle locale = LocaleResourceBundle.getLocale();

	private JPanel contentPane;
	private BorderLayout borderLayout1 = new BorderLayout();
	private Vector tabledata;
	private boolean init = true;
	private JTable jTable;
	private MyTableModel mtm;
	private TableColumn tcol = null;
	private JButton clearButton;
	
	private EIBConnection egc;
	
	/**
	 * Construct the frame
	 */
	public EIBTelegrammtraceframe(EIBConnection con) throws EIBGatewayException
	{
		try
		{
			//EIBConnectionFactory.setConnectorType("Linux BCU1 Connection");
			this.egc = con;		

			egc.connect();

			enableEvents(AWTEvent.WINDOW_EVENT_MASK);

			jbInit();

			egc.addEIBFrameListener(this);
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
			
			this.setIconImage(Util.getImage("mainicon16x16.png"));
		}
		catch (EIBConnectionNotAvailableException cnae)
		{
			JOptionPane.showMessageDialog(null, locale.getString("mess.noEIBConnectionAvaliable"));
			throw new EIBGatewayException();
		}
		catch (EIBConnectionNotPossibleException npe)
		{
			JOptionPane.showMessageDialog(null, locale.getString("mess.noEIBConnectionPossible"));
			throw new EIBGatewayException();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * JBuilder generated method for frame construction
	 */
	private void jbInit() throws Exception
	{

		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(borderLayout1);
		this.setSize(new Dimension(1000, 300));
		this.setTitle("EIB Telegrammtracer - " + this.egc.getConnectionType());

		Object ef[] = { "", new Boolean(false), "", "", "", "", "", "", "", "" };
		tabledata = new Vector();
		tabledata.addElement(ef);
		mtm = new MyTableModel(this.tabledata);
		jTable = new JTable(mtm);

		JScrollPane sp = new JScrollPane(jTable);
		jTable.setPreferredScrollableViewportSize(new Dimension(800, 100));

		this.clearButton = new JButton(locale.getString("blabel.clearData"));
		this.clearButton.setActionCommand("clear");
		this.clearButton.addActionListener(this);
		contentPane.add(this.clearButton, BorderLayout.SOUTH);

		this.setCols();

		contentPane.add(sp, BorderLayout.CENTER);
	}

	/**
	 * Realizing EIBframeListener interface
	 */
	public void frameReceived(EIBFrameEvent efe)
	{
		Object frame[];
		EIBFrame ef = efe.getEIBFrame();
		
		//logger.debug(ef.toHexString());
		
		frame = new Object[10];

		frame[0] = ef.getPriority();
		frame[1] = new Boolean(ef.getRepeat());
		frame[2] = ef.getSourceAddress().toString();
		frame[3] = ef.getDestAddress().toString();
		frame[4] = (new Integer(ef.getRC())).toString();
		frame[5] = ef.getTPCIstring();
		frame[6] = ef.getAPCIstring();
		frame[7] = ef.getApdataString();
		String p = "";
		if (ef.getParity() < 16)
		{
			p += "0";
		}
		frame[8] = p + Integer.toHexString(ef.getParity()).toUpperCase();
		frame[9] = Integer.toHexString(ef.getAck()).toUpperCase();

		if (init)
		{
			tabledata.removeElementAt(0);
			tabledata.addElement(frame);
			init = false;
		}
		else
		{
			tabledata.insertElementAt(frame, 0);
		}

		mtm.fireTableDataChanged();

	}

	private void setCols()
	{
		int size = 0;
		for (int i = 0; i < mtm.getColumnCount(); i++)
		{
			tcol = jTable.getColumnModel().getColumn(i);
			switch (i)
			{
				case 0 :
				case 1 :
					size = 100;
					break;
				case 2 :
				case 3 :
					size = 150;
					break;
				case 4 :
					size = 50;
					break;
				case 5 :
				case 6 :
				case 7 :
					size = 200;
					break;
				case 8 :
				case 9 :
					size = 50;
					break;

			}
			tcol.setPreferredWidth(size);
		}

	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();

		if (cmd.equals("clear"))
		{
			this.mtm.clearData();
			this.init = true;
		}
	}

}

/**
 * Table model
 */
class MyTableModel extends AbstractTableModel
{
	private static ResourceBundle locale = LocaleResourceBundle.getLocale();

	final String[] colnames =
		{
			"tc.priority",
			"tc.repeat",
			"tc.sourceAddr",
			"tc.destAddr",
			"tc.RC",
			"tc.TPCI",
			"tc.APCI",
			"tc.data",
			"tc.parity",
			"tc.ack" };
	private Vector data;

	public MyTableModel(Vector data)
	{
		this.data = data;
	}
	public int getColumnCount()
	{
		return colnames.length;
	}

	public String getColumnName(int col)
	{
		return locale.getString(colnames[col]);
	}

	public Object getValueAt(int parm1, int parm2)
	{
		Object o[] = (Object[]) data.elementAt(parm1);

		return o[parm2];
	}
	public int getRowCount()
	{
		return data.size();
	}

	public Class getColumnClass(int c)
	{
		return getValueAt(0, c).getClass();
	}

	public void clearData()
	{
		Object ef[] = { "", new Boolean(false), "", "", "", "", "", "", "", "" };
		this.data.removeAllElements();
		this.data.addElement(ef);
		this.fireTableDataChanged();
	}
}
