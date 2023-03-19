package basys.eib.eibnetip;

import basys.eib.eibnetip.exceptions.KNXnetIPInvalidDataException;
import basys.eib.eibnetip.exceptions.KNXnetIPNotSupportedException;

/**
 * KNXnetIPSearchResponse.java
 * 
 * @author  olli
 * @version $Id: KNXnetIPSearchResponse.java,v 1.1 2008/04/28 17:50:58 oalt Exp $
 */
public class KNXnetIPSearchResponse extends KNXnetIPFrameBase
{
    
    private HPAI controlEndpoint;
    private DeviceInformationDIB deviceHwDip;
    private SupportedServicesDIB serviceDip;
    
    public KNXnetIPSearchResponse(HPAI controlEndpoint, DeviceInformationDIB deviceHwDip,
                                  SupportedServicesDIB serviceDip)
    {
        super();
        this.controlEndpoint = controlEndpoint;
        this.deviceHwDip = deviceHwDip;
        this.serviceDip = serviceDip;
    }

    public void fillFrame(byte[] buf) throws KNXnetIPInvalidDataException,
            KNXnetIPNotSupportedException
    {
        // TODO Auto-generated method stub

    }

    public byte[] getFrame()
    {
        int size = controlEndpoint.getSize()+ deviceHwDip.getSize() + serviceDip.getSize();
        
        try
        {
            this.header = new KNXnetIPHeader(KNXnetIPHeader.SEARCH_RESPONSE, size);
        }
        catch(Exception ex)
        { 
        }
        
        byte[] header = this.header.getHeader();
        byte[] cep = this.controlEndpoint.getHPAI();
        byte[] devhw = this.deviceHwDip.getDIP();
        byte[] serd = this.serviceDip.getDIP();
                
        byte[] frame = concat(header, concat(cep, concat(devhw, serd)));
        
        return frame;
    }

}
