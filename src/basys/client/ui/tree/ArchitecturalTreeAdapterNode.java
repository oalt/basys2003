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

import org.w3c.dom.*;
import basys.datamodels.architectural.*;

/**
 * ArchitecturalTreeAdapterNode.java
 * 
 * @author 	oalt
 * @version	$Id: ArchitecturalTreeAdapterNode.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public class ArchitecturalTreeAdapterNode
{
	private ArchitecturalDataModel model;
	private Node domNode;
	
	/**
	 * Constructor for ArchitecturalTreeAdapterNode.
	 */
	public ArchitecturalTreeAdapterNode(ArchitecturalDataModel model, Node domNode)
	{
		super();
		this.domNode=domNode;
		this.model=model;
	}

    /**
     *  Return a string that identifies this node in the tree
     */
    public String toString() 
    {
        Element e=(Element)this.domNode;
        NodeList nl=e.getChildNodes();
        for(int i=0; i<nl.getLength(); i++)
        {
        	if(nl.item(i).getNodeName().equals("name"))
        	{
        		if(nl.item(i).hasChildNodes())
        		{
        			return nl.item(i).getFirstChild().getNodeValue();		
        		}	
        	}	
        }
        
        return "";
    }

     /*
      * Return children, index, and count values
      */
      public int index(ArchitecturalTreeAdapterNode child) 
      {
        //System.err.println("Looking for index of " + child);
        int count = childCount();
        for (int i=0; i<count; i++) 
        {
        	ArchitecturalTreeAdapterNode n = this.child(i);
          	if (child.domNode == n.domNode) 
          	{
          		return i;
          	}
        }
        return -1; // Should never get here.
      }
		
	/**
	* Return child with the specified index
	*/
	public ArchitecturalTreeAdapterNode child(int searchIndex) 
    {
    	Element e=(Element)this.domNode;
    	
    	String tagname=e.getNodeName();
    	
    	NodeList nl=null;
    	
    	if(tagname.equals(ArchitecturalDataModel.PROJECT_PREFIX))
    	{
    		nl=e.getElementsByTagName(ArchitecturalDataModel.BUILDING_PREFIX);		
    	}
    	else if(tagname.equals(ArchitecturalDataModel.BUILDING_PREFIX))
    	{
    		nl=e.getElementsByTagName(ArchitecturalDataModel.FLOOR_PREFIX);		
    	} 
    	else if(tagname.equals(ArchitecturalDataModel.FLOOR_PREFIX))
    	{
    		nl=e.getElementsByTagName(ArchitecturalDataModel.ROOM_PREFIX);		
    	}
    	else if(tagname.equals(ArchitecturalDataModel.ROOM_PREFIX))
    	{
    		nl=e.getElementsByTagName(ArchitecturalDataModel.JUNCTION_BOX_PREFIX);		
    	}
    	
    	
        //Note: JTree index is zero-based. 
        Node node = nl.item(searchIndex);
  
		return new ArchitecturalTreeAdapterNode(model, node); 
    }

	/**
	 * 
	 */
	public int childCount() 
    {
        Element e=(Element)this.domNode;
    	
    	String tagname=e.getNodeName();
    	
    	NodeList nl=null;
    	
    	if(tagname.equals(ArchitecturalDataModel.PROJECT_PREFIX))
    	{
    		nl=e.getElementsByTagName(ArchitecturalDataModel.BUILDING_PREFIX);		
    	}
    	else if(tagname.equals(ArchitecturalDataModel.BUILDING_PREFIX))
    	{
    		nl=e.getElementsByTagName(ArchitecturalDataModel.FLOOR_PREFIX);		
    	} 
    	else if(tagname.equals(ArchitecturalDataModel.FLOOR_PREFIX))
    	{
    		nl=e.getElementsByTagName(ArchitecturalDataModel.ROOM_PREFIX);		
    	}
    	else if(tagname.equals(ArchitecturalDataModel.ROOM_PREFIX))
    	{
    		nl=e.getElementsByTagName(ArchitecturalDataModel.JUNCTION_BOX_PREFIX);		
    	}
		
		if(nl!=null)
		{
        	return nl.getLength();
		}
		else
		{
			return 0;	
		}
    }	

	public ArchitecturalTreeAdapterNode getParent()
	{
		if(this.domNode.getNodeName().equals("project"))
		{
			return null;	
		}
		return new ArchitecturalTreeAdapterNode(this.model, this.domNode.getParentNode());	
	}
	
	/**
	 * Return the ID
	 */
	public String getID()
	{
		return ((Element)domNode).getAttribute("id");
	}
	
	/**
	 * 
	 */
	public ArchitecturalDataModel getModel()
	{
		return this.model;
	}
	
}
