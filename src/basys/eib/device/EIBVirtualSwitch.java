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
package basys.eib.device;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import basys.eib.*;

import basys.eib.event.*;

/**
 * This class implements a virtual EIB switch
 * The switch is implemented as a swing component
 *
 * @version $Id: EIBVirtualSwitch.java,v 1.1 2004/01/14 21:38:41 oalt Exp $
 * @author  oalt
 */
public class EIBVirtualSwitch extends JPanel implements EIBFrameListener 
{
  private GridLayout gridLayout1 = new GridLayout();
  private JLabel jLabel1 = new JLabel();
  private JRadioButton jRadioButton1 = new JRadioButton();
  private JButton jButton1 = new JButton();
  private JButton jButton2 = new JButton();
  private EIBPhaddress  pa;
  private EIBGrpaddress desta;
  private EIBFrame ef;
  private EIBConnection egc;

  private int[] data=new int[1];
  private boolean state=false;
  private JLabel jLabel2 = new JLabel();

  public EIBVirtualSwitch(EIBConnection egc, EIBPhaddress pa, EIBGrpaddress desta)  
  {

    this.egc=egc;
    this.pa=pa;
    this.desta=desta;

    data[0]=0;

    ef = new EIBFrame(false, 3, this.pa, this.desta, 6, 0, 0x80, data);

    try 
    {
      jbInit();
    }
    catch(Exception e) 
    {
      e.printStackTrace();
    }
    egc.addEIBFrameListener(this);
  }

  private void jbInit() throws Exception {
    jLabel1.setText("Virtual Switch");
    jLabel1.setForeground(new Color(255,100,50));
    jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
    this.setLayout(gridLayout1);
    gridLayout1.setRows(5);
    gridLayout1.setColumns(1);
    jButton1.setText("On");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton1_actionPerformed(e);
      }
    });
    jButton2.setText("Off");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton2_actionPerformed(e);
      }
    });
    this.setBorder(BorderFactory.createEtchedBorder());
    this.setToolTipText("Virtual Switch");
    jRadioButton1.setEnabled(false);
    jRadioButton1.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel2.setText("Grp.Addr.: "+this.desta.toString());
    this.add(jLabel1, null);
    this.add(jLabel2, null);
    this.add(jRadioButton1, null);
    this.add(jButton1, null);
    this.add(jButton2, null);
  }

  void jButton1_actionPerformed(ActionEvent e) {
    this.data[0]=1;
    ef.setApdata(this.data);
    // System.out.println(ef.toHexString());
    egc.sendEIBFrame(this.ef);
  }

  void jButton2_actionPerformed(ActionEvent e) {
    this.data[0]=0;
    ef.setApdata(this.data);
    // System.out.println(this.ef.toHexString());
    egc.sendEIBFrame(this.ef);
  }

  public void setState(boolean state)  {
    this.jRadioButton1.setSelected(state);
  }

  /**
   * Implementation of the EIBFrameListener interface
   */
  public void frameReceived(EIBFrameEvent efe)  // setup state radio button
  {
    int apdata[];
    EIBFrame ef = efe.getEIBFrame();

    if((ef.getDestAddress()).equals(this.desta))
    {
      // System.out.println("Frame: "+ ef.toHexString());
       // System.out.println("APCI: "+ef.getAPCI());
      if(ef.getAPCI()==0x080)
      {
        if(ef.getAck()==0xCC)
        {
          apdata=ef.getApdata();
          if(apdata[0]==0)
          {
            setState(false);
          }
          else
          {
            setState(true);
          }
        }
      }
    }
  }
}