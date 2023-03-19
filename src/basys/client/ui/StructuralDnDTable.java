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

import basys.client.commands.AddDeviceToRoomCommand;
import basys.client.commands.AddStructuralElementCommand;
import basys.datamodels.architectural.ArchitecturalDataModel;

import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * StructuralDnDTable.java
 * 
 * 
 * @author	oalt
 * @version $Id: StructuralDnDTable.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class StructuralDnDTable extends DnDTable implements DropTargetListener
{
	private ArchitecturalDataModel amodel;
	private String parentID;
	
	/**
	 * @param dm
	 */
	public StructuralDnDTable(TableModel dm, ArchitecturalDataModel amodel, String parentID)
	{
		super(dm);
		this.amodel=amodel;
		this.parentID=parentID;
	}
	
	public String getParentID()
	{
		return this.parentID;
	}
	
	/**
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent dtde)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent dtde)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent dtde)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent dte)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent dtde)
	{
		try 
		{
			Transferable transferable = dtde.getTransferable();
           
			// we accept only Strings      
			if (transferable.isDataFlavorSupported (DataFlavor.stringFlavor))
			{

				dtde.acceptDrop(DnDConstants.ACTION_MOVE);
    	
				String elementID = (String)transferable.getTransferData ( DataFlavor.stringFlavor);
				
				if(elementID.startsWith("new-"))
				{
					elementID=elementID.replaceAll("new-", "");
        			AddStructuralElementCommand command=new AddStructuralElementCommand(this, 
        																				this.amodel,
        																				this.parentID, elementID);	
        			
        			if(command.execute())
        			{
	
        			}
					
				}
			
			}
		}
		catch(IOException ioe)
		{
			dtde.rejectDrop();
		}
		catch (UnsupportedFlavorException ufException ) 
		{
			ufException.printStackTrace();
			// logger.debug( "Exception" + ufException.getMessage());
			dtde.rejectDrop();
		}		
	}

}
