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
package basys.client.ui.tree;

import basys.client.Project;
import basys.client.commands.AddStructuralElementCommand;
import basys.client.ui.event.ShowViewEvent;
import basys.client.ui.event.ShowViewEventListener;
import basys.datamodels.architectural.ArchitecturalDataModel;

import java.awt.dnd.*;
import java.util.*;
import java.awt.Point;
import java.awt.datatransfer.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.io.*;

import org.apache.log4j.*;


/**
 * ArchitecturalTree.java
 * 
 * @author 	oalt
 * @version	$Id: ArchitecturalTree.java,v 1.2 2006/09/14 13:42:41 fritzchen Exp $
 */
public class ArchitecturalTree extends JTree implements DragSourceListener, 
														DragGestureListener, 
														DropTargetListener,
														TreeSelectionListener,
														Observer
{
	private static Logger logger=Logger.getLogger(ArchitecturalTree.class); 
	
	private Project p;
	
	private Vector listeners;
	DragSource dragSource = null;
	DropTarget dropTarget = null;
	
	ArchitecturalDataModel model;
	ArchitecturalTreeModel treemodel;
	
	
	
	/**
	 * Constructor for ArchitecturalTree.
	 */
	public ArchitecturalTree(Project p)
	{
		super();
		this.p=p;
		this.listeners=new Vector();
		this.model=p.getArchitecturalDataModel();
		
		this.addTreeSelectionListener(this);
		ArchitecturalTreeModel treemodel=new ArchitecturalTreeModel(model);
		
		this.treemodel=treemodel;
			
		this.setModel(treemodel);
		this.setCellRenderer(new ArchitecturalTreeRenderer());
		
		this.setEditable(true);
				
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer( this, DnDConstants.ACTION_MOVE, this);
		
		dropTarget=new DropTarget(this, this);
		
		this.addMouseListener(new TreePopupListener(this.p, this));
	}

	public void dragGestureRecognized( DragGestureEvent event) 
	{
    	logger.info("Drag gesture recoginzed.");    
    	
    	ArchitecturalTreeAdapterNode n=(ArchitecturalTreeAdapterNode)this.getSelectionPath().getLastPathComponent();
    	
    	logger.info(n.getID());
    	
    	StringSelection text = new StringSelection(n.getID()); 
    
    	// as the name suggests, starts the dragging
    	dragSource.startDrag(event, DragSource.DefaultMoveDrop, text , this);
    	      
  	}
  	
  	/**
	 * @see java.awt.dnd.DropTargetListener#drop(DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent dtde) 
	{
		logger.debug(dtde);
		
		try 
		{
        	Transferable transferable = dtde.getTransferable();
                   
        	// we accept only Strings      
        	if (transferable.isDataFlavorSupported (DataFlavor.stringFlavor))
        	{
        
            	dtde.acceptDrop(DnDConstants.ACTION_MOVE);
            	
            	String elementID = (String)transferable.getTransferData ( DataFlavor.stringFlavor);
            	          	
            	Point p=dtde.getLocation();
            	TreePath path=this.getPathForLocation((int)p.getX(), (int)p.getY());
            	
            	if(path!=null)
            	{          		
            			
            		
            		logger.debug("ElementID: "+elementID);
            		logger.debug("Drop at: "+((ArchitecturalTreeAdapterNode)path.getLastPathComponent()).getID());
         			String destID=((ArchitecturalTreeAdapterNode)path.getLastPathComponent()).getID();
         			
            		if(elementID.startsWith("new-"))
            		{
            			elementID=elementID.replaceAll("new-", "");
            			AddStructuralElementCommand command=new AddStructuralElementCommand(this.getParent().getParent(), 
            																				this.model,
            																				destID, elementID);	
            			
            			if(command.execute())
            			{
	            			ArchitecturalTreeModel treemodel=(ArchitecturalTreeModel)this.getModel();
	            			
	            			ArchitecturalTreeAdapterNode aanode=(ArchitecturalTreeAdapterNode)path.getLastPathComponent();          		
		            		
		            		logger.debug(aanode.getID());
		            				
            			}
            		}		
            		
            	}
            	else
            	{
            		//dtde.rejectDrop();		
            	}
            	dtde.getDropTargetContext().dropComplete(true);
        	} 
        	else
        	{
            	dtde.rejectDrop();
        	}
    	}
    	catch (IOException exception) 
    	{
        	exception.printStackTrace();
        	logger.debug( "Exception" + exception.getMessage());
        	dtde.rejectDrop();
    	} 
    	catch (UnsupportedFlavorException ufException ) 
    	{
      		ufException.printStackTrace();
      		logger.debug( "Exception" + ufException.getMessage());
     		dtde.rejectDrop();
    	}
	}
  
	/**
	 * @see java.awt.dnd.DragSourceListener#dragEnter(DragSourceDragEvent)
	 */
	public void dragEnter(DragSourceDragEvent dsde)
	{
		// logger.info("Drag enter.");
	}

	/**
	 * @see java.awt.dnd.DragSourceListener#dragOver(DragSourceDragEvent)
	 */
	public void dragOver(DragSourceDragEvent dsde)
	{
		// logger.info("Drag Over.");
	}

	/**
	 * @see java.awt.dnd.DragSourceListener#dropActionChanged(DragSourceDragEvent)
	 */
	public void dropActionChanged(DragSourceDragEvent dsde)
	{
		// logger.info("Drag action changed.");
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
		// logger.info("Drag drop end.");
	}
	
	/**
	 * @see java.awt.dnd.DropTargetListener#dragEnter(DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent dtde)
	{
	}

	/**
	 * @see java.awt.dnd.DropTargetListener#dragOver(DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent dtde)
	{
	}

	/**
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent dtde)
	{
	}

	/**
	 * @see java.awt.dnd.DropTargetListener#dragExit(DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent dte)
	{
	}

	public void addShowViewEventListener(ShowViewEventListener l)
	{
		this.listeners.addElement(l);
	}

	/**
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e)
	{
		
		String destID=((ArchitecturalTreeAdapterNode)e.getPath().getLastPathComponent()).getID();
		
		
		for(Enumeration enu=this.listeners.elements(); enu.hasMoreElements(); )
		{
			((ShowViewEventListener)enu.nextElement()).updateView(new ShowViewEvent(this, destID));
		}
		
	}

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg)
	{
		String cmd="";
		
		if(arg instanceof String)
		{
			cmd=(String)arg;
		}
		if(cmd.startsWith("new-"))
		{
			cmd=cmd.replaceAll("new-", "");
			
			((ArchitecturalTreeModel)this.getModel()).reload();
			
			for(int cnt=0; cnt<this.getRowCount(); cnt++)
			{
				this.expandRow(cnt);
			}
			
		}
	}


	
}
