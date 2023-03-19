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

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.naming.*;
import org.apache.log4j.*;

import basys.Util;
import basys.client.ui.VerticalFlowLayout;

/**
 * AboutDialog.java
 *
 * Dieser Dialog zeigt eine HTML Seite an, die das BASys Programm beschreibt
 *
 * @author  bin, oalt
 * @version $Id: AboutDialog.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public class AboutDialog extends JDialog
{
	private static Logger logger=Logger.getLogger(AboutDialog.class);

    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JPanel panel_icon = new JPanel();
    JEditorPane htmlPanel;
    JScrollPane spanel=new JScrollPane();

    ImageIcon webqicIcon ;

    JButton ok = new JButton();

    FlowLayout flowLayout1 = new FlowLayout();
    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();


    public AboutDialog(Frame frame, String title, boolean modal)
    {
        super(frame, title, modal);
        try
        {
            jbInit();
            pack();
        }
        catch(Exception ex)
        {
			logger.error(ex);
            //ex.printStackTrace();
        }
    }

    /**
     * TestQicAboutDialog wird geoeffnet
     */
    void jbInit() throws Exception
    {
        
        webqicIcon = Util.getIcon("basys.gif");
       

        panel_icon = new JPanel();
        JLabel jl = new JLabel(webqicIcon);
        panel_icon.add(jl);

        panel1.setLayout(verticalFlowLayout1);
        // panel1.add(panel_icon);

        //aktuelle Property abhollen
     
        String url = "file://" + Util.getDataPathPrefix() + "/global-data/help/info.html";
        logger.info(url);

		try
		{
            this.htmlPanel = new JEditorPane(url);
            htmlPanel.setEditable(false);
            this.spanel =new JScrollPane(htmlPanel);
		}
		catch(Exception ce)
		{
		    this.spanel=new JScrollPane(new JLabel("HTML-Seite konnte nicht geladen werden !", JLabel.CENTER));
		}
		//this.spanel = new JScrollPane(htmlPanel);
        getContentPane().add(spanel, BorderLayout.CENTER);



        //alles zusammenbauen
        spanel.setPreferredSize(new Dimension(600, 500));

        panel2.setLayout(flowLayout1);

        ok.setText("OK");

		panel2.add(ok);

        panel1.add(spanel);
        panel1.add(panel2);

        //MouseListener Windows schliessen
        ok.addMouseListener(new java.awt.event.MouseAdapter()
                            {
                                public void mouseClicked(MouseEvent e)
                                {
                                    ok_mouseClicked(e);
                                }
                            }
                           );

        getContentPane().add(panel1);

		this.pack();
        //this.setSize(800,700);

        //Frame in die mittel
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

        //setVisible(true);

    }
    //ende jbInit()


    /**
    *die Fenster wird geschlossen
       */
    void ok_mouseClicked(MouseEvent e)
    {
        this.dispose();
    }
}
