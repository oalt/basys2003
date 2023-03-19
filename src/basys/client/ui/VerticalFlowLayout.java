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

import java.awt.*;

/**
 * VerticalFlowLayout.java
 * 
 * 
 * @author	David Risner
 * @version $Id: VerticalFlowLayout.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class VerticalFlowLayout implements LayoutManager
{

	private int vgap = 0;

	/**
	 * VerticalFlowLayout constructor comment.
	 */
	public VerticalFlowLayout() {
	  this(0);
	}

	/**
	 * VerticalFlowLayout constructor comment.
	 */
	public VerticalFlowLayout(int vgap) {
	  if (vgap < 0) {
		this.vgap = 0;
	  } else {
		this.vgap = vgap;
	  } 
	}

	/**
	 * addLayoutComponent method comment.
	 */
	public void addLayoutComponent(String name, Component comp) {
	} 

	/**
	 * layoutContainer method comment.
	 */
	public void layoutContainer(Container parent) {
	  Insets insets = parent.getInsets();
	  int w = parent.getSize().width - insets.left - insets.right;

	  // int h = parent.size().height - insets.top - insets.bottom;
	  int numComponents = parent.getComponentCount();

	  if (numComponents == 0) {
		return;
	  } 
	  int y = insets.top;
	  int x = insets.left;

	  for (int i = 0; i < numComponents; ++i) {
		Component c = parent.getComponent(i);

		if (c.isVisible()) {
		  Dimension d = c.getPreferredSize();

		  c.setBounds(x, y, w, d.height);
		  y += d.height + vgap;
		} 
	  } 
	} 

	/**
	 * minimumLayoutSize method comment.
	 */
	public Dimension minimumLayoutSize(Container parent) {
	  Insets insets = parent.getInsets();
	  int maxWidth = 0;
	  int totalHeight = 0;
	  int numComponents = parent.getComponentCount();

	  for (int i = 0; i < numComponents; ++i) {
		Component c = parent.getComponent(i);

		if (c.isVisible()) {
		  Dimension cd = c.getMinimumSize();

		  maxWidth = Math.max(maxWidth, cd.width);
		  totalHeight += cd.height;
		} 
	  } 
	  Dimension td = new Dimension(maxWidth + insets.left + insets.right, 
								   totalHeight + insets.top + insets.bottom 
								   + vgap * numComponents);

	  return td;
	} 

	/**
	 * preferredLayoutSize method comment.
	 */
	public Dimension preferredLayoutSize(Container parent) {
	  Insets insets = parent.getInsets();
	  int maxWidth = 0;
	  int totalHeight = 0;
	  int numComponents = parent.getComponentCount();

	  for (int i = 0; i < numComponents; ++i) {
		Component c = parent.getComponent(i);

		if (c.isVisible()) {
		  Dimension cd = c.getPreferredSize();

		  maxWidth = Math.max(maxWidth, cd.width);
		  totalHeight += cd.height;
		} 
	  } 
	  Dimension td = new Dimension(maxWidth + insets.left + insets.right, 
								   totalHeight + insets.top + insets.bottom 
								   + vgap * numComponents);

	  return td;
	} 

	/**
	 * removeLayoutComponent method comment.
	 */
	public void removeLayoutComponent(Component comp) {
	} 

}
