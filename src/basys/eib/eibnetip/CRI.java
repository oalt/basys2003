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

import basys.eib.eibnetip.exceptions.KNXnetIPNotSupportedException;

/**
 * CRI Connection Request Information structure
 */
public class CRI
{
    // connection types
    public static final byte DEVICE_MGMT_CONNECTION = 0x03;

    public static final byte TUNNEL_CONNECTION = 0x04;

    public static final byte REMLOG_CONNECTION = 0x06;

    public static final byte REMCONF_CONNECTION = 0x07;

    public static final byte OBJSVR_CONNECTION = 0x08;

    // tunnelling knx layer
    public static final short TUNNEL_LINKLAYER = 0x02;

    public static final short TUNNEL_RAW = 0x04;

    public static final short TUNNEL_BUSMONITOR = 0x80;

    private static final byte CRI_SIZE = 0x04;

    private byte type = TUNNEL_CONNECTION;

    private short layer = TUNNEL_LINKLAYER;

    public CRI(byte connectionType, short KNXLayer)
            throws KNXnetIPNotSupportedException
    {
        if (connectionType != TUNNEL_CONNECTION)
            throw new KNXnetIPNotSupportedException(
                    "Only tunnelling is supported");
        type = connectionType;
        if (KNXLayer != TUNNEL_LINKLAYER)
            throw new KNXnetIPNotSupportedException(
                    "Only KNX linklayer tunnelling is supported");
        layer = KNXLayer;
    }

    byte[] getCRI()
    {
        byte[] cri = new byte[CRI_SIZE];
        cri[0] = CRI_SIZE;
        cri[1] = type;
        cri[2] = (byte) (layer & 255);
        return cri;
    }

}
