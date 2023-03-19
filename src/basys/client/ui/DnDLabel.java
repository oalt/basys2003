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

import java.awt.dnd.DragSource;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

import java.awt.dnd.*;
import java.awt.datatransfer.*;

/**
 * DnDLabel.java 
 * 
 * @author 	oalt
 * @version	$Id: DnDLabel.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public class DnDLabel extends JLabel implements DragSourceListener, DragGestureListener
{
	private static Logger logger = Logger.getLogger(DnDLabel.class);
	 
	private DragSource dragSource;
	private String dndText;
	
	/**
	 * Constructor for DnDLabel.
	 */
	public DnDLabel(String dndText)
	{
		super();
		this.dndText=dndText;	
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer( this, DnDConstants.ACTION_MOVE, this);
	}

	public void dragGestureRecognized( DragGestureEvent event) 
	{
    	//logger.info("Drag gesture recoginzed.");    
    	
    	StringSelection text = new StringSelection(this.dndText); 
    
    	// as the name suggests, starts the dragging
    	dragSource.startDrag(event, DragSource.DefaultMoveDrop, text , this);
    	   
    
  	}
  
	/**
	 * @see java.awt.dnd.DragSourceListener#dragEnter(DragSourceDragEvent)
	 */
	public void dragEnter(DragSourceDragEvent dsde)
	{
		//logger.info("Drag enter.");
	}

	/**
	 * @see java.awt.dnd.DragSourceListener#dragOver(DragSourceDragEvent)
	 */
	public void dragOver(DragSourceDragEvent dsde)
	{
		//logger.info("Drag Over.");
	}

	/**
	 * @see java.awt.dnd.DragSourceListener#dropActionChanged(DragSourceDragEvent)
	 */
	public void dropActionChanged(DragSourceDragEvent dsde)
	{
		//logger.info("Drag action changed.");
	}

	/**
	 * @see java.awt.dnd.DragSourceListener#dragExit(DragSourceEvent)
	 */
	public void dragExit(DragSourceEvent dse)
	{
		//logger.debug("Drag exit.");
	}

	/**
	 * @see java.awt.dnd.DragSourceListener#dragDropEnd(DragSourceDropEvent)
	 */
	public void dragDropEnd(DragSourceDropEvent dsde)
	{
		//logger.debug("Drag drop end.");
	}

}
