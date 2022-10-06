package ke.co.tracom.telpotest.card;

import static ke.co.tracom.telpotest.Constant.*;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.telpo.emv.EmvAmountData;
import com.telpo.emv.EmvCandidateApp;
import com.telpo.emv.EmvOnlineData;
import com.telpo.emv.EmvParam;
import com.telpo.emv.EmvPinData;
import com.telpo.emv.EmvService;
import com.telpo.emv.EmvServiceListener;
import com.telpo.emv.EmvTLV;
import com.telpo.pinpad.PinParam;
import com.telpo.pinpad.PinpadService;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOUtil;
import org.jpos.tlv.TLVList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ke.co.tracom.telpotest.config.Country;
import ke.co.tracom.telpotest.config.TelpoConfig;
import ke.co.tracom.telpotest.utils.LTVListDecoder;
import ke.co.tracom.telpotest.utils.PinPadCustomConfig;

public class EmvProcess extends Application {
    private static int ret;
    private long Amt;
    private static String currentPin;

    private EmvServiceListener listener = new EmvServiceListener() {
        @Override
        public int onInputAmount(EmvAmountData emvAmountData) {
            Amt = emvAmountData.Amount = 100;
            emvAmountData.TransCurrCode = (short) 156;//rupay834
            emvAmountData.ReferCurrCode = (short) 156;//rupay834
            emvAmountData.TransCurrExp = (byte) 2;
            emvAmountData.ReferCurrExp = (byte) 2;
            emvAmountData.ReferCurrCon = 0;
            emvAmountData.CashbackAmount = 0;
            return EmvService.EMV_TRUE;
        }

        @Override
        public int onInputPin(EmvPinData emvPinData) {
            Log.w(TAG, "onInputPin: " + "callback [onInputPIN]:" + emvPinData.type);
            PinParam param = new PinParam(getApplicationContext());
            param.KeyIndex = 1;
            param.WaitSec = 30;
            param.MaxPinLen = 6;
            param.MinPinLen = 4;
            param.IsShowCardNo = 1;
            param.CardNo = getPAN();
            param.Amount = String.valueOf(Amt);

            int pinRequest;
            PinPadCustomConfig customConfig = new PinPadCustomConfig();
            pinRequest = showPinPad(param, customConfig, Amt);

            return pinRequest;
        }

        @Override
        public int onSelectApp(EmvCandidateApp[] emvCandidateApps) {
            Log.i(TAG, "onSelectApp()");
            return emvCandidateApps[0].index;
        }

        @Override
        public int onSelectAppFail(int ErrCode) {
            Log.i(TAG, "onSelectAppFail()" + ErrCode);
            return EmvService.EMV_FALSE;
        }

        @Override
        public int onFinishReadAppData() {
            Log.i(TAG, "onFinishReadAppData()");
            return EmvService.EMV_TRUE;
        }

        @Override
        public int onVerifyCert() {
            Log.i(TAG, "onVerifyCert()");
            return EmvService.EMV_TRUE;
        }

        @Override
        public int onOnlineProcess(EmvOnlineData emvOnlineData) {
            TransactionData transactionData = new TransactionData();
            ISOPackager isoPackager = new TestIsoPackager();

            Log.i(TAG, "onOnlineProcess()");
            int response = EmvService.EMV_TRUE;
            TelpoConfig config = new TelpoConfig();
            EmvParam param = TelpoConfig.getDefaultEmvParam();
            ISOMsg isoMsg = new ISOMsg();
            try {
                isoMsg.setMTI("0200");
                isoMsg.set(2, getPAN());
                isoMsg.set(3, "000000");
                isoMsg.set(4, getTlv(0x9F02));
                isoMsg.set(11, ISOUtil.zeropad(config.getTransactionCounter(), 6));
                isoMsg.set(14, getExpiry());
                isoMsg.set(22, "051");
                isoMsg.set(24, "003");
                isoMsg.set(25, "00");
                isoMsg.set(35, getTrack2());
                isoMsg.set(41, param.TermId);
                isoMsg.set(42, param.MerchId);
                isoMsg.set(49, String.valueOf(Country.TZ.getNumeric()));
                isoMsg.set(52, getCurrentPin());

                String tagVal = getTlv(0x9F26);
                Log.e("9F26: ", "tagVal");

                TLVList field55 = new TLVList();
                field55.append(0x9F26, getTlv(0x9F26));
                field55.append(0x9F02, getTlv(0x9F02));
                field55.append(0x9F03, getTlv(0x9F03));
                field55.append(0x9F1A, getTlv(0x9F1A));
                field55.append(0x95, getTlv(0x95));
                field55.append(0x5F2A, getTlv(0x5F2A));
                field55.append(0x9A, getTlv(0x9A));
                field55.append(0x9C, getTlv(0x9C));
                field55.append(0x9F37, getTlv(0x9F37));
                field55.append(0x82, getTlv(0x82));
                field55.append(0x9F36, getTlv(0x9F36));
                field55.append(0x9F10, getTlv(0x9F10));
                field55.append(0x9F1E, getTlv(0x9F1E));
                field55.append(0x9F33, getTlv(0x9F33));
                field55.append(0x5F34, getTlv(0x5F34));
                field55.append(0x84, getTlv(0x84));
                field55.append(0x9F09, getTlv(0x9F09));
                field55.append(0x9F34, getTlv(0x9F34));
                field55.append(0x9F35, getTlv(0x9F35));
                field55.append(0x9F41, getTlv(0x9F41));
                field55.append(0x9F27, getTlv(0x9F27));
                field55.append(0x9B, getTlv(0x9B));
                isoMsg.set(55, ISOUtil.hexString(field55.pack()));

            } catch (Exception e) {
                e.printStackTrace();
                response = EmvService.EMV_FALSE;
            }

            isoMsg.setPackager(isoPackager);
            Log.d(TAG, "sendMessage()");
            transactionData.setRequestData(isoMsg);

            try {
                ISOMsg resp = Channel.sendIsoMessage(isoMsg, isoPackager);
                transactionData.setResponseData(resp);

                Log.d(TAG, "After receive response...");
                Toast.makeText(getApplicationContext(), "Response code " + resp.getString("39"), Toast.LENGTH_SHORT).show();

            } catch (IOException | ISOException e) {
                e.printStackTrace();

            }
            return response;
        }

        @Override
        public int onRequireTagValue(int i, int i1, byte[] bytes) {
            Log.i(TAG, "onRequireTagValue()");
            return EmvService.EMV_TRUE;
        }

        @Override
        public int onRequireDatetime(byte[] datetime) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            byte[] time = new byte[0];
            try {
                time = str.getBytes("ascii");
                System.arraycopy(time, 0, datetime, 0, datetime.length);
                return EmvService.EMV_TRUE;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.e("MyEmvService", "onRequireDatetime failed");
                return EmvService.EMV_FALSE;
            }
        }

        @Override
        public int onReferProc() {
            Log.i(TAG, "onReferProc()");
            return EmvService.EMV_TRUE;
        }

        @Override
        public int OnCheckException(String s) {
            Log.i(TAG, "OnCheckException()");
            // TODO: Confirm if this is correct
            return EmvService.EMV_FALSE;
        }

        @Override
        public int OnCheckException_qvsdc(int i, String s) {
            Log.i(TAG, "OnCheckException_qvsdc()");
            return EmvService.EMV_TRUE;
        }

        @Override
        public int onMir_FinishReadAppData() {
            Log.i(TAG, "onMir_FinishReadAppData()");
            return 0;
        }

        @Override
        public int onMir_DataExchange() {
            Log.i(TAG, "onMir_DataExchange()");
            return 0;
        }

        @Override
        public int onMir_Hint() {
            Log.i(TAG, "onMir_Hint()");
            return 0;
        }
    };

    private int showPinPad(PinParam param, PinPadCustomConfig config, long amt) {
        int mResult;
        config.addText(PinPadCustomConfig.text("Account : " + getPAN(), 0x000000, 28));
        config.addText(PinPadCustomConfig.text(String.format("Amount : %.2f", (double) Amt), 0x000000, 28));

        Log.i(TAG, "onInputPin(): onBeforeOpenPinpad");
        int getPinResponse = PinpadService.TP_PinpadGetPinCustomize(param, config.getInfos(), 0, 500, 500);

        if (ret == PinpadService.PIN_ERROR_CANCEL) {
            mResult = EmvService.ERR_USERCANCEL;
            Log.d(TAG, "Input pin Cancel");
        } else if (ret == PinpadService.PIN_OK && ISOUtil.hexString(param.Pin_Block).contains("00000000")) {
            mResult = EmvService.ERR_NOPIN;
            Log.d(TAG, "No pin");
        } else if (ret == PinpadService.PIN_OK) {
            mResult = EmvService.EMV_TRUE;
            Log.d(TAG, "Pin input success");
            Log.d(TAG, "Pin Block: " + ISOUtil.hexString(param.Pin_Block));
            currentPin = ISOUtil.hexString(param.Pin_Block);
        } else {
            mResult = EmvService.EMV_FALSE;
            Log.d(TAG, "Error getting pin");
        }

        return mResult;
    }

    public void processIcc() {
        EmvService emvService = EmvService.getInstance();

        Log.d(TAG, "ICC Power on");
        ret = EmvService.IccCard_Poweron();
        if (ret != 0) {
            Log.e(TAG, "EmvService.IccCard_Poweron Fail");
        }
        Log.d(TAG, "Transaction init");
        ret = emvService.Emv_TransInit();

        Log.d(TAG, "set EMV params");
        EmvParam emvParam = TelpoConfig.getDefaultEmvParam();
        ret = emvService.Emv_SetParam(emvParam);

        EmvTLV serial = new EmvTLV(0x9F1E);
        serial.Value = "12345678".getBytes();
        emvService.Emv_SetTLV(serial);
        emvService.setListener(listener);
        emvService.Emv_StartApp(1);
    }

    public String getPAN() {
        String track2 = getTrack2();
        return track2.substring(0, track2.lastIndexOf("D"));
    }

    public String getTrack2() {
        return getTlv(0x57);
    }

    public String getTlv(int tag) {
        EmvTLV pan = new EmvTLV(tag);
        int ret = EmvService.getInstance().Emv_GetTLV(pan);
        // EmvService.getInstance().TL
        if (ret == EmvService.EMV_TRUE) {
            StringBuffer p = new StringBuffer(ISOUtil.hexString(pan.Value));
            /*if (p.charAt(p.toString().length()-1) == 'F'){
                p.deleteCharAt(p.toString().length()-1);
            }*/
            return p.toString();
        }

        return null;
    }

    public String getExpiry() {
        String track2 = getTrack2();

        return track2.substring(track2.lastIndexOf("D") + 1,
                track2.lastIndexOf("D") + 5);
    }

    public String getCurrentPin() {
        return currentPin;
    }

    public static void processNFC() {
        //TODO: To be implemented
    }

    public static void processMagStripe() {
        //TODO: To be implemented
    }

}
