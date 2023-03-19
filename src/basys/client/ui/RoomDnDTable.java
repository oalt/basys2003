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

import basys.client.Project;
import basys.client.commands.AddDeviceToRoomCommand;
import basys.client.commands.AddSensorCommand;

import javax.swing.JButton;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import java.awt.Point;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.IOException;

/**
 * RoomDnDTable.java
 * 
 * 
 * @author	oalt
 * @version $Id: RoomDnDTable.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class RoomDnDTable extends DnDTable
{
	
	private static Logger logger=Logger.getLogger(RoomDnDTable.class);
	
	private Project p;
	private String roomID;
	/**
	 * @param dm
	 */
	public RoomDnDTable(TableModel dm, Project p, String roomID)
	{
		super(dm);
		this.p=p;
		this.roomID=roomID;
		
	}
	
	public String getRoomID()
	{
		return this.roomID;
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
				
				if(elementID.startsWith("dev-"))
				{
					elementID=elementID.replaceAll("dev-", "");
					dtde.getDropTargetContext().dropComplete(true);
					
					Point p=dtde.getLocation();
					
					AddDeviceToRoomCommand command=new AddDeviceToRoomCommand(this, this.p, this.roomID, elementID, p );
					
					command.execute();
					
					//this.add(new JButton(elementID));
					//this.validate();
					
				}
				else if(elementID.equals("sensor"))
				{
					logger.debug("DND: "+elementID);
					
					
					dtde.getDropTargetContext().dropComplete(true);
					
					Point p=dtde.getLocation();
					
					AddSensorCommand command=new AddSensorCommand(this, this.p, this.roomID, p );
					
					command.execute();
					
					//this.add(new JButton(elementID));
					//this.validate();
					
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
