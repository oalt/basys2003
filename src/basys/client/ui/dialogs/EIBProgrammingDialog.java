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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.Vector;

import basys.LocaleResourceBundle;
import basys.client.Project;
import basys.client.eib.EIBProgramConfigurator;
import basys.client.ui.VerticalFlowLayout;
import basys.datamodels.eib.EIBProgrammingTableModel;
import basys.datamodels.installation.InstallationModel;
import basys.eib.EIBApplication;
import basys.eib.EIBApplicationProgrammer;
import basys.eib.EIBConnection;
import basys.eib.EIBPhAddressProgrammer;
import basys.eib.EIBPhaddress;
import basys.eib.EIBProgrammer;
import basys.eib.exceptions.EIBAddressFormatException;


import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

/**
 * EIBProgrammingDialog.java
 * 
 * 
 * @author	oalt
 * @version $Id: EIBProgrammingDialog.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class EIBProgrammingDialog extends JDialog implements ActionListener
{
	
	private static ResourceBundle locale=LocaleResourceBundle.getLocale();
	private static Logger logger=Logger.getLogger(EIBProgrammingDialog.class);
	
	private Project p;
	private EIBConnection con;
	
	private JTable devTable;
	private EIBProgrammingTableModel ptm;
	private JProgressBar dpbar;
	private JProgressBar apbar;
	private JButton programAllButton;
	private JButton programSelectedButton;
	private JButton continueButton;
	private JButton cancelButton;
	private JButton closeButton;
	private JLabel status1;
	private JLabel status2;
	private JCheckBox phAddrOnlyCheckBox;
	
	private ProgrammingThread t;
	
	public EIBProgrammingDialog(Frame parent, Project p, EIBConnection con) 
	{
		super(parent);
		this.p=p;
		this.con=con;
		
		this.setModal(false);
		
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
	
	private void initUI()
	{
		
		this.setTitle(locale.getString("tit.programEIB"));
		
		JPanel mainp=new JPanel(new VerticalFlowLayout());
		
		this.devTable=new JTable();
		ptm=new EIBProgrammingTableModel(this.p);
		this.devTable.setModel(ptm);
		
		JScrollPane sp=new JScrollPane(this.devTable);
		
		sp.setPreferredSize(new Dimension(800,350));
		
		mainp.add(sp);
		
		JPanel buttonp1=new JPanel();
		
		this.phAddrOnlyCheckBox=new JCheckBox(locale.getString("l.phAddrOnly"));
		this.phAddrOnlyCheckBox.setSelected(false);
		buttonp1.add(this.phAddrOnlyCheckBox);
		
		
		
		this.programAllButton=new JButton(locale.getString("blabel.programAll"));
		this.programAllButton.setActionCommand("program all");
		this.programAllButton.addActionListener(this);
		buttonp1.add(this.programAllButton);
		
		this.programSelectedButton=new JButton(locale.getString("blabel.programSelected"));
		this.programSelectedButton.setActionCommand("program selected");
		//this.programSelectedButton.setEnabled(false);
		this.programSelectedButton.addActionListener(this);
		buttonp1.add(this.programSelectedButton);
		
		mainp.add(buttonp1);
		
		this.status1=new JLabel(" ", JLabel.CENTER);
		mainp.add(this.status1);
		this.status2=new JLabel(" ", JLabel.CENTER);
		mainp.add(this.status2);
		
		JPanel buttonp2=new JPanel();
		this.continueButton=new JButton(locale.getString("blabel.continue"));
		this.continueButton.setActionCommand("continue");
		this.continueButton.setEnabled(false);
		this.continueButton.addActionListener(this);
		
		buttonp2.add(this.continueButton);
		mainp.add(buttonp2);
		
		JPanel progresspanel=new JPanel();
		
		progresspanel.add(new JLabel(locale.getString("l.device")+": "));
		
		this.dpbar=new JProgressBar();
		progresspanel.add(this.dpbar);
		
		progresspanel.add(new JLabel(locale.getString("l.all")+": "));
		
		this.apbar=new JProgressBar();
		progresspanel.add(this.apbar);
		
		this.cancelButton=new JButton(locale.getString("blabel.cancelProgramming"));
		this.cancelButton.setActionCommand("cancel programming");
		this.cancelButton.setEnabled(false);
		this.cancelButton.addActionListener(this);
		progresspanel.add(this.cancelButton);
		
		mainp.add(progresspanel);
		
		JPanel buttonp3=new JPanel();
		
		closeButton=new JButton(locale.getString("blabel.close"));
		closeButton.setActionCommand("close");
		closeButton.addActionListener(this);
		
		buttonp3.add(closeButton);
		
		this.getContentPane().add(buttonp3, BorderLayout.SOUTH);
		
		mainp.setBorder(new TitledBorder(locale.getString("tit.programEIB")));
		
		this.getContentPane().add(mainp);
		
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		String cmd=e.getActionCommand();
		
		if(cmd.equals("close"))
		{
			this.dispose();
			this.setVisible(false);
		}
		else if(cmd.equals("program all"))
		{
			int ind[]=new int[this.devTable.getRowCount()];
			for(int i=0; i<this.devTable.getRowCount(); i++)
			{
				ind[i]=i;
			}
			t=new ProgrammingThread(this, ind);
			t.start();	
		}
		else if(cmd.equals("program selected"))
		{
			t=new ProgrammingThread(this, this.devTable.getSelectedRows());
			t.start();
		}
		else if(cmd.equals("continue"))
		{
			t.setContinue(true);
		}
		else if(cmd.equals("cancel programming"))
		{
			t.stopProgramming();
		}
	}

	
	/**
	 * 
	 *  
	 * 
	 * 
	 */
	class ProgrammingThread extends Thread
	{
		
		private boolean cont=false;
		private boolean stopProgramming=false;
		
		private EIBProgrammingDialog dia;
		
		private int[]selection;
				
		public ProgrammingThread(EIBProgrammingDialog dia, int[] selection)
		{
			this.dia=dia;
			this.selection=selection;
		}
		
		public void stopProgramming()
		{
			this.stopProgramming=true;
		}
		
		public void setContinue(boolean b)
		{
			this.cont=b;
		}
		
		/**
		 * 
		 */
		public void run()
		{
			stopProgramming=false;
			
			apbar.setValue(0);
			
			closeButton.setEnabled(false);
			phAddrOnlyCheckBox.setEnabled(false);
			cancelButton.setEnabled(true);
			programAllButton.setEnabled(false);
			programSelectedButton.setEnabled(false);
			
			this.programmDevices(this.selection);
			
			programAllButton.setEnabled(true);
			programSelectedButton.setEnabled(true);
			cancelButton.setEnabled(false);
			closeButton.setEnabled(true);
			
			phAddrOnlyCheckBox.setEnabled(true);
			
			dia.status1.setText(locale.getString("mess.progEnd"));
			
			if(!dia.status2.getText().equals(locale.getString("mess.stopProg")))
			{
				dia.status2.setText(" ");
			}
		}
	
		/**
		 * program devices with the given indices
		 * @param rowIndices
		 */
		private void programmDevices(int[] rowIndices)
		{
			if(rowIndices.length==0)
			{
				JOptionPane.showMessageDialog(null, locale.getString("mess.nodevicesToProgram"));
				return;
			}
			InstallationModel imodel=p.getInstallationModel();
			
			Vector ids=(Vector)imodel.getUnprogrammedEIBDeviceIDs();
			
			int success=0;
			int progcnt=rowIndices.length;
			
			for(int cnt=0; cnt<rowIndices.length; cnt++)
			{
				if(stopProgramming)
				{
					stopProgramming=false;
					status2.setText(locale.getString("mess.stopProg"));
					break;
				}
				
				String id=(String)ids.get(rowIndices[cnt]);
				// program ph address and application
				if(imodel.getPropertyByName(id, "device-state").equals("unprogrammed"))
				{
					dia.status1.setText(locale.getString("stat.actualDevice")+" "+imodel.getName(id)+" "+
								 locale.getString("stat.phAddr"));
					
					dia.status2.setText(locale.getString("stat.pressCont"));
					
					dia.validate();
					
					dia.continueButton.setEnabled(true);
					
					while(!cont)
					{
						if(stopProgramming)
						{
							this.cont=true;
						}
						try
						{
							Thread.sleep(100);
						}
						catch(InterruptedException ie)
						{
							
						}
					}
					
					dia.continueButton.setEnabled(false);
					cont = false;
					
					if(stopProgramming)
					{
						stopProgramming=false;
						status2.setText(locale.getString("mess.stopProg"));
						break;
					}
					
					dia.status2.setText(locale.getString("stat.pressButton"));
					
					dia.dpbar.setValue(0);
					
					dia.validate();
							
					int errorcode=this.programmPhAddress(id);
					
					if(errorcode==0)
					{
						// update data model
						imodel.setProperty(id, "device-state", "addressed");
						ptm.fireTableDataChanged();
						validate();
					}
					try
					{
						Thread.sleep(1000);
					}
					catch(InterruptedException ie)
					{
						
					}
				}
				// program application only
				if(imodel.getPropertyByName(id, "device-state").equals("addressed") && !phAddrOnlyCheckBox.isSelected())
				{
					
					dia.status1.setText(locale.getString("stat.actualDevice")+" "+imodel.getName(id)+
								 		locale.getString("stat.progApp"));
						
					dia.status2.setText(" ");
					
					boolean c=false;
					
					do
					{
						int e2=this.programApplication(id);
						logger.debug("ErrorCode: "+e2);
						if(e2==0)
						{
							imodel.setProperty(id, "device-state", "ready");
							ptm.fireTableDataChanged();
							c=false;
							success++;
							dia.apbar.setValue((int)(100.00 * (double)success/progcnt));
						}
						else if(e2==EIBProgrammer.CANCEL)
						{
							dia.status2.setText(locale.getString("mess.stopProg"));
							
							c=false;
						}
						else
						{
							String message=locale.getString("mess.appProgError");
							
							if(e2==EIBApplicationProgrammer.WRONG_MANUFACTURER_ID)
							{
								message+="\n "+locale.getString("mess.wrongManu");
								JOptionPane.showMessageDialog(null, message);
								c=false;
							}
							else if(e2==EIBApplicationProgrammer.WRONG_MASK_VERSION)
							{
								message+="\n "+locale.getString("mess.wrongMask");
								JOptionPane.showMessageDialog(null, message);
								c=false;
							}
							else
							{
								int sel=JOptionPane.showConfirmDialog(null, message);
								if(sel==0)
								{
									c=true;
								}
								else
								{
									c=false;
								}
							}
						}
									
					}  while(c);
					
				}
				
			}
			
		}
		
		/**
		 * Program application softare to EIB device
		 * @param devID
		 * @return
		 */
		private int programApplication(String devID)
		{
			int errorcode=0;
			
			dia.dpbar.setValue(0);
			
			InstallationModel imodel=dia.p.getInstallationModel();
			
			
			EIBProgramConfigurator config=new EIBProgramConfigurator(p, devID, false);
		
			String phaddr=imodel.getPropertyByName(devID, "eib-physical-address");
			
			EIBPhaddress destaddr=null;
			try
			{
				destaddr=new EIBPhaddress(phaddr);
			}
			catch(EIBAddressFormatException afe)
			{
				afe.printStackTrace();
			}
			EIBApplication app=config.getEIBApplication();
			
			
			EIBApplicationProgrammer programmer=new EIBApplicationProgrammer(con,
																	   destaddr, 
																	   17,
																	   app, 
																	   config.getCompleteMemory() );
			
			
			programmer.startProgramming();
				
				
			while(programmer.getErrorCode()==0)
			{
				int progress=programmer.getProgress();
				
				if(stopProgramming)
				{
					programmer.abortProgramming();
					
					JOptionPane.showMessageDialog(null, locale.getString("mess.stopProg"));
					break;
				}
				
				if(progress == 100)
				{
					return 0;
				}
				
				try
				{
					Thread.sleep(100);
				}
				catch(InterruptedException ie)
				{
					
				}
				
				if(progress!=programmer.getProgress() && programmer.getErrorCode()==0)
				{
					progress=programmer.getProgress();
					dpbar.setValue(progress);
					status2.setText(progress+" %");
					
				}
			}
			
			errorcode=programmer.getErrorCode();
			logger.debug("ErrorCode: "+errorcode);
			
			return errorcode;
			
		}
		
		/**
		 * Programm physical address to EIB device
		 * @param devID
		 * @return
		 */
		private int programmPhAddress(String devID)
		{
			
			
			
			InstallationModel imodel=dia.p.getInstallationModel();
			String phaddr=imodel.getPropertyByName(devID, "eib-physical-address");
			
			EIBPhaddress destaddr=null;
			try
			{
				destaddr=new EIBPhaddress(phaddr);
			}
			catch(EIBAddressFormatException afe)
			{
				afe.printStackTrace();
			}
			
			int errorcode=0;
			
			boolean cont=false;
			do
			{
				
				EIBPhAddressProgrammer programmer=new EIBPhAddressProgrammer(con, 
																			 destaddr);
				
				programmer.startProgramming();
				
				
				while(programmer.getErrorCode()==0)
				{
					int progress=programmer.getProgress();
					if(progress == 100)
					{
						break;
					}
					
					try
					{
						Thread.sleep(100);
					}
					catch(InterruptedException ie)
					{
						
					}
					
					if(progress!=programmer.getProgress() && programmer.getErrorCode()==0)
					{
						progress=programmer.getProgress();
						dpbar.setValue(progress);
					}
				}
				
				errorcode=programmer.getErrorCode();
				
				if(programmer.getErrorCode()==EIBPhAddressProgrammer.ADDRESSINUSE)
				{
				
					JOptionPane.showMessageDialog(null, locale.getString("mess.addressIsInUse"));
				}
				else if(programmer.getErrorCode()==EIBPhAddressProgrammer.MULTIBUTTON)
				{
					JOptionPane.showMessageDialog(null, locale.getString("mess.multiProgramButton"));
				}
				else if(programmer.getErrorCode()==EIBPhAddressProgrammer.NOBUTTONPRESSED)
				{
					int sel=JOptionPane.showConfirmDialog(null, locale.getString("mess.phAddrTimeout"));
					logger.debug("Selection: "+sel);
					if(sel==0)
					{
						cont=true;
						dia.dpbar.setValue(0);
					}
					else
					{
						cont=false;
					}
				}
				else
				{
					dia.dpbar.setValue(100);
					dia.status2.setText(locale.getString("stat.progSucc"));		
				}
				
			} while(cont);
			
			
			return errorcode;
		
		}
	}
}
