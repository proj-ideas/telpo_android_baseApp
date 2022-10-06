package ke.co.tracom.telpotest.utils;

import com.telpo.emv.EmvCAPK;
import com.telpo.emv.EmvService;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

public class CRDBCapk {
    public static void addMasterCardCAPK(){

    }

    public static void addVisaCAPK() throws ISOException {
        addCAPK("A000000003", (byte)0x92, "03",
                "996AF56F569187D09293C14810450ED8EE3357397B18A2458EFAA92DA3B6DF6514EC060195318FD43BE9B8F0CC669E3F844057CBDDF8BDA191BB64473BC8DC9A730DB8F6B4EDE3924186FFD9B8C7735789C23A36BA0B8AF65372EB57EA5D89E7D14E9C7B6B557460F10885DA16AC923F15AF3758F0F03EBD3C5C2C949CBA306DB44E6A2C076C5F67E281D7EF56785DC4D75945E491F01918800A9E2DC66F60080566CE0DAF8D17EAD46AD8E30A247C9F",
                "429C954A3859CEF91295F663C963E582ED6EB253",
                "211231"
                );
    }

    private static int addCAPK(String RID, byte RIDIndex, String exponent, String modulus, String checkSum, String expiry) throws ISOException {
        EmvCAPK emvCAPK = new EmvCAPK();
        emvCAPK.RID = ISOUtil.hex2byte(RID);
        emvCAPK.KeyID = RIDIndex;
        emvCAPK.Exponent = ISOUtil.hex2byte(ISOUtil.zeropad(exponent, 6));
        emvCAPK.Modul = ISOUtil.hex2byte(modulus);
        emvCAPK.CheckSum = ISOUtil.hex2byte(checkSum);
        emvCAPK.ExpDate = ISOUtil.hex2byte(expiry);


        emvCAPK.HashInd = (byte)0x01;
        emvCAPK.ArithInd = (byte)0x01;

        return EmvService.Emv_AddCapk(emvCAPK);
    }

}
