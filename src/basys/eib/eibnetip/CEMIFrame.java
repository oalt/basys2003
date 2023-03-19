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
 * Copyright (c) 2006        BASys 2003 Project, 
 *                           Vienna University of Technology, 
 *                           Department of Automation - Automation Systems group,
 *                           B. Malinowsky
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

import basys.eib.EIBFrame;

/**
 * 
 * CEMIFrame common external message interface frame for medium independent KNX
 * messages (cEMI specification)
 * 
 * @version $Id: CEMIFrame.java,v 1.2 2008/04/28 17:55:31 oalt Exp $
 */
public class CEMIFrame
{
    

    private short code = 0;

    private EIBFrame eib = null;

    // message codes
    public static final short L_Data_req = 0x11;

    public static final short L_Data_con = 0x2E;

    public static final short L_Data_ind = 0x29;

    public CEMIFrame(short msgCode, EIBFrame eibFrame)
    {
        code = msgCode;
        eib = eibFrame;
    }

    public CEMIFrame(byte[] buf)
    {
        fillFrame(buf);
    }

    public byte[] getcEMI()
    {
        int[] frame = eib.getFrame();
        // length of service information N-SDU (= TPCI/APCI & data)
        int infoLen = frame.length - 6;
        // cEMI buffer
        byte[] buf = new byte[9 + infoLen];
        buf[0] = (byte) code;
        // no additional info for now
        buf[1] = 0;
        // control field 1 same as in eib frame
        buf[2] = (byte) frame[0];
        if (eib.getAck() != 0xCC)
        {
            buf[2] += 1;
        }
        // control field2
        // we use address type and ring counter from eib frame
        buf[3] = (byte) (frame[5] & (15 << 4));
        // src address from eib frame
        buf[4] = (byte) frame[1];
        buf[5] = (byte) frame[2];
        // dst address from eib frame
        buf[6] = (byte) frame[3];
        buf[7] = (byte) frame[4];
        // length octet
        buf[8] = (byte) (frame[5] & 15);
        // copy service information out of eib frame (N-SDU)
        // NB: eib frame contains *no* check octet!
        for (int i = 0; i < infoLen; i++)
        {
            buf[i + 9] = (byte) (frame[i + 6] & 255);
        }
        return buf;
    }

    public void fillFrame(byte[] buf)
    {
        code = (short) (buf[0] & 255);
        int i = buf[1]; // additional info length
        if (i != 0)
        {
            System.out.println("cEMI frame contains additional information - ignored");
        }
        i += 2; // set index after additional info in buffer
        int[] eibFrame = new int[9 + buf[i + 6]]; // allocate eib frame length
        // parse cEMI frame and fill into eib frame
        int ctrl1 = buf[i++] & 255;
        eibFrame[0] = ctrl1; // ctrl1
        int ctrl2 = buf[i++] & 255;
        eibFrame[1] = buf[i++] & 255; // src addr
        eibFrame[2] = buf[i++] & 255;
        eibFrame[3] = buf[i++] & 255; // dst addr
        eibFrame[4] = buf[i++] & 255;
        eibFrame[5] = ctrl2 & 0xF0; // ctrl2
        int dataLength = buf[i++];
        eibFrame[5] |= dataLength & 0xF;
        for (int cnt = 0; cnt <= dataLength; cnt++)
        {
            eibFrame[cnt + 6] = buf[i++] & 255;
        }
        // last octet is parity, no problem to leave it 0
        eibFrame[dataLength + 7] = 0;
        // acknowledge octet
        if ((ctrl1 & 1) == 1)
        {
            // confirmation error
            eibFrame[dataLength + 8] = 0xC; // NAK
        }
        else
        // standard set to ACK (bin. 11001100)
        {
            eibFrame[dataLength + 8] = 0xCC;
        }
        eib = new EIBFrame(eibFrame);
    }
    
    public EIBFrame getEIBFrame()
    {
        return eib;
    }

    public short getMsgCode()
    {
        return code;
    }
}
