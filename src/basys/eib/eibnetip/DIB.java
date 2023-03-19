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
 * Copyright (c) 2003-2008,  BASys 2003 Project, 
 *                           Vienna University of Technology, 
 *                           Department of Automation - Automation Systems group,
 *                           Oliver Alt 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *  - Redistributions of source code must retain the above copyright notice, this list
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
package basys.eib.eibnetip;

/**
 * DIB.java
 * 
 * @author  olli
 * @version $Id: DIB.java,v 1.1 2008/04/28 17:51:44 oalt Exp $
 */
public abstract class DIB
{
    
    public static final int DESCRIPTION_TYPE_DEVICE_INFO = 0x01;
    public static final int DESCRIPTION_TYPE_SUPP_SVC_FAMILIES = 0x02;
    public static final int DESCRIPTION_TYPE_MFR_DATA = 0xFE;
    
    public static final int KNX_MEDIUM_TP0 = 0x01;
    public static final int KNX_MEDIUM_TP1 = 0x02;
    public static final int KNX_MEDIUM_PL110 = 0x04;
    public static final int KNX_MEDIUM_PL132 = 0x08;
    public static final int KNX_MEDIUM_RF = 0x10;
    
    public static final int DEVICE_STATUS_PROGRAM_MODE = 1;
    public static final int DEVICE_STATUS_NON_PROGRAM_MODE = 0;
    
    protected int descriptionType;
    protected int size = 0; 
    
    public DIB()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public int getDescriptionType()
    {
        return descriptionType;
    }

    public void setDescriptionType(int descriptionType)
    {
        this.descriptionType = descriptionType;
    }
    
    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public abstract byte[] getDIP();
}
