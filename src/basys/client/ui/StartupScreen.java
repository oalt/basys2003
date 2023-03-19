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

import javax.swing.*;
import java.awt.*;

import org.apache.log4j.*;

import basys.Util;

/**
 * StartupScreen.java
 *
 * Klasse implementiert den Startup Bildschirm, der beim Start von WebQIC angezeigt wird.
 * Die Klasse implementiert das Interface Runnable, damit der Startup Screen als nebenläufiger Thread angezeigt
 * werden kann.
 *
 * @author  oalt
 * @version $Id: StartupScreen.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */

public class StartupScreen extends JWindow implements Runnable
{
    static Logger logger=Logger.getLogger(StartupScreen.class);

    ClassLoader cl=this.getClass().getClassLoader();

    JPanel jPanel1 = new JPanel();
    JLabel logolabel = new JLabel();
    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
    JLabel jLabel1 = new JLabel();
    JFrame mainframe;

    /**
     * Konstruktor
     *
     * @param loginframe Instanz des Login Frame, um den Frame nach dem Anzeigen in den Vordergrund zu bringen.
     */
    public StartupScreen(JFrame mainframe)
    {
        super(new Frame());

        this.mainframe=mainframe;

        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        // this.dispose();
    }

    /**
     * Implementation der run-Methode
     * Thread wartet 5 Sekunden und bringt dann den Loginframe in den Vordergrund
     */
    public void run()
    {
        logger.info("StartupScreen showed.");
        try
        {
            Thread.sleep(5000);
        }
        catch(InterruptedException ie)
        {
        }
        this.dispose();
        this.mainframe.setVisible(true);
        logger.info("Startup Thread ended.");
    }

    /**
     * Aufbau der Oberfläche
     */
    private void jbInit() throws Exception
    {
        Icon logoicon = Util.getIcon("basys.gif");
        
        logolabel.setIcon(logoicon);

		JPanel imgpanel=new JPanel(new FlowLayout(FlowLayout.CENTER));
		imgpanel.setBackground(Color.WHITE);
		
        jPanel1.setBackground(Color.white);
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
        jPanel1.setLayout(verticalFlowLayout1);
        jLabel1.setHorizontalAlignment(JLabel.CENTER);
        jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);

        jLabel1.setText(" Softwaresystem für Gebäudeautomationssysteme " );
		
		JLabel jLabel2=new JLabel("(c) 2003 Oliver Alt");
		jLabel2.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel2.setHorizontalAlignment(JLabel.CENTER);

        this.getContentPane().add(jPanel1);
        imgpanel.add(logolabel);
        jPanel1.add(imgpanel);
        jPanel1.add(jLabel1);
		jPanel1.add(jLabel2);
		
        this.pack();

        // zentrieren
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
        this.setVisible(true);

        // in den Vordergrund
        this.toFront();
    }
}
