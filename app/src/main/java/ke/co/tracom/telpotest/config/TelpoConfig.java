package ke.co.tracom.telpotest.config;

import com.telpo.emv.EmvParam;
import com.telpo.emv.EmvService;

import org.jpos.iso.ISOUtil;

public class TelpoConfig {
    int transactionCounter = 1;
    private final String SERVER_IP = "41.215.130.247";
    private final int SERVER_PORT = 10051;

    public int getTransactionCounter() {
        return transactionCounter;
    }
    public void updateTransactionCounter(int counter) {
        this.transactionCounter = counter;
    }

    public String getSERVER_IP() {
        return SERVER_IP;
    }

    public int getSERVER_PORT() {
        return SERVER_PORT;
    }
    public int getConnectionTimeout() {
        return 30;
    }


    public static EmvParam getDefaultEmvParam(){

        EmvParam emvParam = new EmvParam();
        emvParam.MerchName = ISOUtil.hex2byte("BANK");
        emvParam.MerchId = ISOUtil.hex2byte("POSSL1000000001");
        emvParam.TermId = ISOUtil.hex2byte("POSSL001");
        emvParam.TerminalType = 0x22;
        emvParam.Capability = new byte[]{(byte) 0xE0, (byte) 0x40, (byte) 0xC8};
        emvParam.ExCapability = new byte[]{(byte) 0xFF, 0x00, (byte) 0xF0, (byte) 0xA0, 0x01};
        emvParam.CountryCode = ISOUtil.hex2byte(ISOUtil.zeropad(Country.TZ.getNumeric(), 4));
        emvParam.TransType = EmvService.TYPE_GOODS;
        return emvParam;
    }
}
