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

import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;

import basys.datamodels.architectural.ArchitecturalDataModel;
import javax.swing.event.*;

import org.apache.log4j.Logger;
import org.w3c.dom.*;
import java.util.*;

/**
 * ArchitecturalTreeModel.java
 * 
 * @author 	oalt
 * @version	$Id: ArchitecturalTreeModel.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 */
public class ArchitecturalTreeModel implements TreeModel, Observer
{
	private static Logger logger=Logger.getLogger(ArchitecturalTreeModel.class);

	ArchitecturalDataModel model;
	
	/**
	 * Constructor for ArchitecturalTreeModel.
	 */
	public ArchitecturalTreeModel(ArchitecturalDataModel model)
	{
		super();
		this.model=model;
	}

	/**
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	public Object getRoot()
	{
		
		return new ArchitecturalTreeAdapterNode(this.model, this.model.getDataRootNode(this.model.getRootID()));
	}

	/**
	 * @see javax.swing.tree.TreeModel#getChild(Object, int)
	 */
	public Object getChild(Object parent, int index)
	{
		ArchitecturalTreeAdapterNode node = (ArchitecturalTreeAdapterNode) parent;
        return node.child(index);
	}

	/**
	 * @see javax.swing.tree.TreeModel#getChildCount(Object)
	 */
	public int getChildCount(Object parent)
	{
		ArchitecturalTreeAdapterNode node = (ArchitecturalTreeAdapterNode) parent;
        return node.childCount();
	}

	/**
	 * @see javax.swing.tree.TreeModel#isLeaf(Object)
	 */
	public boolean isLeaf(Object aNode)
	{
		ArchitecturalTreeAdapterNode node = (ArchitecturalTreeAdapterNode) aNode;
        if (node.childCount() > 0) 
        {
        	return false;
        }
        else
        {
        	return true;
        }
	}

	/**
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(TreePath, Object)
	 */
	public void valueForPathChanged(TreePath path, Object newValue)
	{
		logger.debug((String)newValue);
		ArchitecturalTreeAdapterNode anode=(ArchitecturalTreeAdapterNode)path.getLastPathComponent();
		Node n=model.getDataRootNode(anode.getID());
		model.writeDOMNodeValue(n, new StringTokenizer("name", "/"), (String) newValue);
				
	}

	/**
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(Object, Object)
	 */
	public int getIndexOfChild(Object parent, Object child)
	{
		ArchitecturalTreeAdapterNode node = (ArchitecturalTreeAdapterNode) parent;
        return node.index((ArchitecturalTreeAdapterNode) child);
	}

	 /**
        * Builds the parents of node up to and including the root node,
        * where the original node is the last element in the returned array.
        * The length of the returned array gives the node's depth in the
        * tree.
        *
        * @param aNode the TreeNode to get the path for
        * @param an array of TreeNodes giving the path from the root to the
        *        specified node.
        */
    public ArchitecturalTreeAdapterNode[] getPathToRoot(ArchitecturalTreeAdapterNode aNode)
    {
        return getPathToRoot(aNode, 0);
    }

    /**
     * Builds the parents of node up to and including the root node,
     * where the original node is the last element in the returned array.
     * The length of the returned array gives the node's depth in the
     * tree.
     *
     * @param aNode  the TreeNode to get the path for
     * @param depth  an int giving the number of steps already taken towards
     *        the root (on recursive calls), used to size the returned array
     * @return an array of TreeNodes giving the path from the root to the
     *         specified node
     */
    public ArchitecturalTreeAdapterNode[] getPathToRoot(ArchitecturalTreeAdapterNode aNode, int depth)
    {
        ArchitecturalTreeAdapterNode[]              retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
        if(aNode == null)
        {
            if(depth == 0)
            {
                return null;
            }
            else
            {
                retNodes = new ArchitecturalTreeAdapterNode[depth];
            }
        }
        else
        {
            depth++;
            if(aNode == getRoot())
            {
                retNodes = new ArchitecturalTreeAdapterNode[depth];
            }
            else
            {
                retNodes = getPathToRoot(aNode.getParent(), depth);
            }
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

	 /**
        * Invoke this method if you've modified the TreeNodes upon which this
        * model depends.  The model will notify all of its listeners that the
        * model has changed.
        */
    public void reload()
    {
        reload((ArchitecturalTreeAdapterNode)getRoot());
    }

    /**
     * Invoke this method if you've modified the TreeNodes upon which this
     * model depends.  The model will notify all of its listeners that the
     * model has changed below the node <code>node</code> (PENDING).
     */
    public void reload(ArchitecturalTreeAdapterNode node)
    {
        if(node != null)
        {
            fireTreeStructureChanged(this.getTreeModelEvent(this, getPathToRoot(node), null, null));
        }
       
    }
	
	/**
	 * 
	 */
	public void nodeWhereInserted(ArchitecturalTreeAdapterNode node)
	{
		TreeModelEvent e=new TreeModelEvent(this, getPathToRoot(node));
		
		logger.debug(e.toString());
		
		//fireTreeNodesInserted(e);
		//fireTreeNodesChanged(e);
		
		fireTreeStructureChanged(e);	
	}
	
	/*
       * Use these methods to add and remove event listeners.
       * (Needed to satisfy TreeModel interface, but not used.)
       */
      private Vector listenerList = new Vector();
      public void addTreeModelListener(TreeModelListener listener) {
        if ( listener != null 
        && ! listenerList.contains( listener ) ) {
           listenerList.addElement( listener );
        }
      }
      public void removeTreeModelListener(TreeModelListener listener) {
        if ( listener != null ) {
           listenerList.removeElement( listener );
        }
      }

      // Note: Since XML works with 1.1, this example uses Vector.
      // If coding for 1.2 or later, though, I'd use this instead:
      //   private List listenerList = new LinkedList();
      // The operations on the List are then add(), remove() and
      // iteration, via:
      //  Iterator it = listenerList.iterator();
      //  while ( it.hasNext() ) {
      //    TreeModelListener listener = (TreeModelListener) it.next();
      //    ...
      //  }

      /*
       * Invoke these methods to inform listeners of changes.
       * (Not needed for this example.)
       * Methods taken from TreeModelSupport class described at 
       *   http://java.sun.com/products/jfc/tsc/articles/jtree/index.html
       * That architecture (produced by Tom Santos and Steve Wilson)
       * is more elegant. I just hacked 'em in here so they are
       * immediately at hand.
       */
      public void fireTreeNodesChanged( TreeModelEvent e ) {
        Enumeration listeners = listenerList.elements();
        while ( listeners.hasMoreElements() ) {
          TreeModelListener listener = 
            (TreeModelListener) listeners.nextElement();
          listener.treeNodesChanged( e );
        }
      } 
      
      /**
       * 
       */
      public void fireTreeNodesInserted( TreeModelEvent e ) {
        Enumeration listeners = listenerList.elements();
        while ( listeners.hasMoreElements() ) {
           TreeModelListener listener =
             (TreeModelListener) listeners.nextElement();
           listener.treeNodesInserted( e );
        }
      }  
      
      /**
       * 
       */ 
      public void fireTreeNodesRemoved( TreeModelEvent e ) {
        Enumeration listeners = listenerList.elements();
        while ( listeners.hasMoreElements() ) {
          TreeModelListener listener = 
            (TreeModelListener) listeners.nextElement();
          listener.treeNodesRemoved( e );
        }
      }
      
      /**
       * 
       */   
      public void fireTreeStructureChanged( TreeModelEvent e ) {
        Enumeration listeners = listenerList.elements();
        while ( listeners.hasMoreElements() ) {
          TreeModelListener listener =
            (TreeModelListener) listeners.nextElement();
          listener.treeStructureChanged( e );
        }
      }

	/**
     *
     */
    private TreeModelEvent getTreeModelEvent(Object source, Object[] path, int[] childIndices, Object[] children)
    {

        TreeModelEvent e = new TreeModelEvent(source, path, childIndices, children);
        return e;
    }

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg)
	{
		this.reload();
		
	}
}
