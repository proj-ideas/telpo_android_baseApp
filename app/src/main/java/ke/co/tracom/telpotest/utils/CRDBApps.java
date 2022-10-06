package ke.co.tracom.telpotest.utils;
import static ke.co.tracom.telpotest.utils.DefaultAPPCAPK.Log;

import com.telpo.emv.EmvApp;
import com.telpo.emv.EmvService;

import org.jpos.iso.ISOUtil;

import java.io.UnsupportedEncodingException;

public class CRDBApps {
    public static void addAppALL(){
        addApp_AMERICAN_EXPRESS();
        addApp_MASTER_CARD();
        addApp_VISA();
        addApp_UNION_PAY();
    }

    public static void addApp_AMERICAN_EXPRESS(){
        /* AMERICAN EXPRESS */

        EmvApp APP_AMEX = new EmvApp();
        String name = "AMEX";
        try {
            APP_AMEX.AppName = name.getBytes("ascii");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /*A0 00 00 00 25 01 05 01*/
        APP_AMEX.AID = ISOUtil.hex2byte("A00000002501");
        APP_AMEX.SelFlag = (byte)0x00;
        APP_AMEX.Priority = (byte)0x00;
        APP_AMEX.TargetPer = (byte)99;
        APP_AMEX.MaxTargetPer = (byte)99;
        APP_AMEX.FloorLimitCheck = (byte)0;
        APP_AMEX.RandTransSel = (byte)1;
        APP_AMEX.VelocityCheck = (byte)1;
        APP_AMEX.FloorLimit = new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};//9F1B:FloorLimit
        APP_AMEX.Threshold = new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
        APP_AMEX.TACDenial = ISOUtil.hex2byte("0010000000");
        APP_AMEX.TACOnline = ISOUtil.hex2byte("DE00FC9800");
        APP_AMEX.TACDefault = ISOUtil.hex2byte("DC50FC9800");
        APP_AMEX.DDOL = ISOUtil.hex2byte("9F3704");
        APP_AMEX.Version = new byte[]{(byte)0x00,(byte)0x01};

        int result = EmvService.Emv_AddApp(APP_AMEX);
        Log("ADD APP_AMEX:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("AMEX added:" + result);
        }
    }

    public static void addApp_VISA(){
        EmvApp APP_Visa = new EmvApp();
        String name = "Visa";
        try {
            APP_Visa.AppName = name.getBytes("ascii");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /*A0 00 00 00 03 10 10*/
        APP_Visa.AID = ISOUtil.hex2byte("A0000000031010");
        APP_Visa.SelFlag = (byte)0x00;
        APP_Visa.Priority = (byte)0x00;
        APP_Visa.TargetPer = (byte)99;
        APP_Visa.MaxTargetPer = (byte)99;
        APP_Visa.FloorLimitCheck = (byte)1;
        APP_Visa.RandTransSel = (byte)1;
        APP_Visa.VelocityCheck = (byte)1;
        APP_Visa.FloorLimit = new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};//9F1B:FloorLimit
        APP_Visa.Threshold = new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
        APP_Visa.TACDenial = ISOUtil.hex2byte("0010000000");
        APP_Visa.TACOnline = ISOUtil.hex2byte("DC4004F800");
        APP_Visa.TACDefault = ISOUtil.hex2byte("DC4000A800");
        APP_Visa.AcquierId = new byte[]{(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,(byte)0x89,(byte)0x10};
        APP_Visa.DDOL = ISOUtil.hex2byte("9F3704");
        APP_Visa.TDOL = ISOUtil.hex2byte("9F0206");
        APP_Visa.Version = new byte[]{(byte)0x00,(byte)0x96};

        int result = EmvService.Emv_AddApp(APP_Visa);
        Log("ADD APP_Visa:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("Added APP_Visa database : A0 00 00 00 03 10 10");
        }

        APP_Visa.AID = ISOUtil.hex2byte("A0000000032010");
        result = EmvService.Emv_AddApp(APP_Visa);
        Log("ADD APP_Visa:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("Added APP_Visa database : A0000000032010");
        }

        APP_Visa.AID = ISOUtil.hex2byte("A0000000032020");
        result = EmvService.Emv_AddApp(APP_Visa);
        Log("ADD APP_Visa:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("Added APP_Visa database : A0000000032020");
        }

        APP_Visa.AID = ISOUtil.hex2byte("A0000000038010");
        result = EmvService.Emv_AddApp(APP_Visa);
        Log("ADD APP_Visa:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("Added APP_Visa database : A0000000038010");
        }
    }

    public static void addApp_MASTER_CARD(){
        EmvApp APP_MasterCard = new EmvApp();
        String name = "Master Card";
        try {
            APP_MasterCard.AppName = name.getBytes("ascii");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /*A0 00 00 00 04 10 10*/
        APP_MasterCard.AID = ISOUtil.hex2byte("A0000000041010");
        APP_MasterCard.SelFlag = (byte)0x00;
        APP_MasterCard.Priority = (byte)0x00;
        APP_MasterCard.TargetPer = (byte)99;
        APP_MasterCard.MaxTargetPer = (byte)99;
        APP_MasterCard.FloorLimitCheck = (byte)1;
        APP_MasterCard.RandTransSel = (byte)1;
        APP_MasterCard.VelocityCheck = (byte)1;
        APP_MasterCard.FloorLimit = new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};//9F1B:FloorLimit
        APP_MasterCard.Threshold = new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
        APP_MasterCard.TACDenial = ISOUtil.hex2byte("0000000000");
        APP_MasterCard.TACOnline = ISOUtil.hex2byte("FC50808800");
        APP_MasterCard.TACDefault = ISOUtil.hex2byte("FC50B8A000");
        APP_MasterCard.AcquierId = new byte[]{(byte)0x01,(byte)0x22,(byte)0x55,(byte)0x66,(byte)0x33,(byte)0x40};
        APP_MasterCard.DDOL = ISOUtil.hex2byte("9F3704");
        APP_MasterCard.TDOL = ISOUtil.hex2byte("9F02065F2A029A039C0195059F3704");
        APP_MasterCard.Version = new byte[]{(byte)0x00,(byte)0x02};

        int result = EmvService.Emv_AddApp(APP_MasterCard);
        Log("ADD APP_MasterCard:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("Added APP_MasterCard : A0000000041010");
        }

        APP_MasterCard.AID = ISOUtil.hex2byte("A0000000049999");
        result = EmvService.Emv_AddApp(APP_MasterCard);
        Log("ADD APP_MasterCard:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("Added APP_MasterCard : A0000000049999");
        }

        APP_MasterCard.AID = ISOUtil.hex2byte("A0000000043060");
        APP_MasterCard.TACDefault = ISOUtil.hex2byte("FC50BCA000");
        APP_MasterCard.TACOnline = ISOUtil.hex2byte("FC50BCF800");
        result = EmvService.Emv_AddApp(APP_MasterCard);
        Log("ADD APP_MasterCard:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("Added APP_MasterCard : A0000000043060");
        }

        APP_MasterCard.AID = ISOUtil.hex2byte("A0000000046000");
        result = EmvService.Emv_AddApp(APP_MasterCard);
        Log("ADD APP_MasterCard:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("Added APP_MasterCard : A0000000046000");
        }

        APP_MasterCard.AID = ISOUtil.hex2byte("A0000000050001");
        result = EmvService.Emv_AddApp(APP_MasterCard);
        Log("ADD APP_MasterCard:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("Added APP_MasterCard : A0000000050001");
        }
    }

    public static void addApp_UNION_PAY(){
        EmvApp APP_CUP_01 = new EmvApp();
        String name = "China Union Pay";
        try {
            APP_CUP_01.AppName = name.getBytes("ascii");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /* A0 00 00 03 33 01 01 01 */
        APP_CUP_01.AID = new byte[]{(byte)0xA0,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0x33,(byte)0x01,(byte)0x01,(byte)0x01};
        APP_CUP_01.SelFlag = (byte)0x00;
        APP_CUP_01.Priority = (byte)0x00;
        APP_CUP_01.TargetPer = (byte)0;
        APP_CUP_01.MaxTargetPer = (byte)0;
        APP_CUP_01.FloorLimitCheck = (byte)1;
        APP_CUP_01.RandTransSel = (byte)1;
        APP_CUP_01.VelocityCheck = (byte)1;
        APP_CUP_01.FloorLimit = new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x50,(byte)0x00};//9F1B:FloorLimit
        APP_CUP_01.Threshold = new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
        APP_CUP_01.TACDenial = ISOUtil.hex2byte("0010000000");
        APP_CUP_01.TACOnline = ISOUtil.hex2byte("D84004F800");
        APP_CUP_01.TACDefault = ISOUtil.hex2byte("D84000A800");
        APP_CUP_01.AcquierId = new byte[]{(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,(byte)0x89,(byte)0x10};
        APP_CUP_01.DDOL = ISOUtil.hex2byte("9F3704");
        APP_CUP_01.Version = new byte[]{(byte)0x00,(byte)0x20};

        int result = EmvService.Emv_AddApp(APP_CUP_01);
        Log("ADD APP_CUP_01:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("added APP_UNION_PAY database : A0 00 00 03 33 01 01 01");
        }

        APP_CUP_01.AID = ISOUtil.hex2byte("A000000333010102");
        result = EmvService.Emv_AddApp(APP_CUP_01);
        Log("ADD APP_CUP_01:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("added APP_UNION_PAY database : A000000333010102");
        }

        APP_CUP_01.AID = ISOUtil.hex2byte("A000000333010103");
        result = EmvService.Emv_AddApp(APP_CUP_01);
        Log("ADD APP_CUP_01:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("added APP_UNION_PAY database : A000000333010103");
        }

        APP_CUP_01.AID = ISOUtil.hex2byte("A000000333010106");
        result = EmvService.Emv_AddApp(APP_CUP_01);
        Log("ADD APP_CUP_01:" + result);
        if(result == EmvService.EMV_TRUE){
            Log("added APP_UNION_PAY database : A000000333010106");
        }
    }

}
