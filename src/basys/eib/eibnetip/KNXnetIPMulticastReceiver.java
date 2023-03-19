package basys.eib.eibnetip;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import basys.eib.EIBPhaddress;
import basys.eib.eibnetip.exceptions.KNXnetIPInvalidDataException;

/**
 * KNXnetIPMulticastReceiver.java
 * 
 * @author  olli
 * @version $Id: KNXnetIPMulticastReceiver.java,v 1.1 2008/04/28 17:50:58 oalt Exp $
 */
public class KNXnetIPMulticastReceiver
{

    private int knxCtrlPort = 3671;
    private String multicasetIP = "224.0.23.12";
    
    private MulticastSocket msocket = null;
    
    public KNXnetIPMulticastReceiver()
    {
        super();
        connect();
    }

    private void connect()
    {
        byte[] buffer = new byte[512];
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        
        
        try
        {
            this.msocket = new MulticastSocket(this.knxCtrlPort);
            
            InetAddress mcAddress = InetAddress.getByName(this.multicasetIP);
            
            this.msocket.joinGroup(mcAddress);
            
            while(true)
            {
                this.msocket.receive(dp);
                byte[] data = dp.getData();
                
                
                
                String s = "";
                
                for(int i=0; i< dp.getLength(); i++)
                {
                    int b = (int)data[i] & 0xFF;
                    if(b<16)
                    {
                        s+="0";
                    }
                    s+=Integer.toHexString((int)b & 0xFF).toUpperCase()+" ";
                }
                
                System.out.println(s);
                
                try
                {
                    KNXnetIPSearchRequest request = new KNXnetIPSearchRequest();
                    request.fillFrame((byte[])data.clone());
                    
                    HPAI controlEndpoint = new HPAI(true, "192.168.1.1", this.knxCtrlPort);
                    
                    int[] serialnumber = {0, 0x01, 0x11, 0x11, 0x11, 0x11};
                    int[] macAddr = {0x00, 0xC1, 0x26, 0x07, 0x1f, 0x9c};
                    
                    DeviceInformationDIB did = new DeviceInformationDIB(DIB.KNX_MEDIUM_TP1,
                                                                        DIB.DEVICE_STATUS_NON_PROGRAM_MODE,
                                                                        new EIBPhaddress("1.2.1"),
                                                                        1,
                                                                        serialnumber,
                                                                        macAddr,
                                                                        "BASys 2003"
                                                                        );
                    
                    int[] supportedServices = {0x02, 0x03, 0x04};
                    SupportedServicesDIB ssd = new SupportedServicesDIB(supportedServices);
                    
                    KNXnetIPSearchResponse response = new KNXnetIPSearchResponse(controlEndpoint, did, ssd);
                    
                    String clientip = request.getDiscoveryEndpoint().getIP();
                    int port = request.getDiscoveryEndpoint().getPort();
                    
                    response.send(this.msocket, clientip, port);
                    
                    
                    
                }
                catch(KNXnetIPInvalidDataException idex)
                {
                    idex.printStackTrace();
                }
                
        
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public static void main(String args[])
    {
        new KNXnetIPMulticastReceiver();
    }
    

}
