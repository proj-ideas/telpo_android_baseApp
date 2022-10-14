package ke.co.tracom.telpotest;

import static ke.co.tracom.telpotest.Constant.*;
import static ke.co.tracom.telpotest.card.EmvProcess.processMagStripe;
import static ke.co.tracom.telpotest.card.EmvProcess.processNFC;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.telpo.emv.EmvParam;
import com.telpo.emv.EmvService;
import com.telpo.pinpad.PinpadService;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

import ke.co.tracom.telpotest.card.CardType;
import ke.co.tracom.telpotest.card.EmvProcess;
import ke.co.tracom.telpotest.config.TelpoConfig;
import ke.co.tracom.telpotest.utils.CRDBApps;
import ke.co.tracom.telpotest.utils.CRDBCapk;
import ke.co.tracom.telpotest.utils.DefaultAPPCAPK;

public class EMVAction extends Application {
    EmvService emvService;
    int ret;
    String sMasterKey = "11111111111111111111111111111111";
    String sPinKey = "B7444C47AEC3E0D0CDEDB086252F54AE";

    private static boolean magReaderOpen = false;
    private static boolean iccReaderOpen = false;
    private static boolean nfcReaderOpen = false;
    String BDK = "1A1A1A1A1A1A1A1A1C1C1C1C1C1C1C1C";
    String KSN = "FFFF9876540002000001";

    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
    }



    public void init(Context context) {
        emvService = EmvService.getInstance();
        ret = EmvService.Open(context);
        if (ret != EmvService.EMV_TRUE) {
            Log.e(TAG, "EmvService.Open fail");
            Toast.makeText(this, "EmvService.Open fail", Toast.LENGTH_SHORT).show();
        }
        EmvService.Emv_SetDebugOn(1);
//        EmvService.Emv_RemoveAllApp();
//        EmvService.Emv_RemoveAllCapk();



       ret =  EmvService.deviceOpen();
        if (ret != 0) {
            Log.e(TAG, "EmvService.Open fail");
            Toast.makeText(context, "EmvService.Open fail", Toast.LENGTH_SHORT).show();
        }

        ret = PinpadService.Open(context);
        if (ret == PinpadService.PIN_ERROR_NEED_TO_FOMRAT) {
            PinpadService.TP_PinpadFormat(this);
            ret = PinpadService.Open((this));//返回0成功其他失败
        }
        Log.d("telpo", "PinpadService deviceOpen open:" + ret);
        if (ret != 0) {
            Toast.makeText(context, "PinpadService open fail", Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "Writing TP_WriteMasterKey:");
        int i = PinpadService.TP_WriteMasterKey(0, ISOUtil.hex2byte(sMasterKey), PinpadService.KEY_WRITE_DIRECT);
        if (i == 0) {
            Log.i(TAG, "Master key write success");
            Log.i(TAG, "Writing Pin Key");
            int t = PinpadService.TP_WritePinKey(1, ISOUtil.hex2byte(sPinKey), PinpadService.KEY_WRITE_DECRYPT, 0);
            Log.d(TAG, "TP_WritePinKey Result:" + t);
        }

        Log.i(TAG, "init(): Writing BDK and KSN... ");
        int r = PinpadService.TP_PinpadWriteDukptKey(ISOUtil.hex2byte(BDK),
                ISOUtil.hex2byte(KSN), 0, PinpadService.KEY_WRITE_DIRECT, 3);
        Log.i(TAG, "init(): DUKPT write result:  " + r);
        addAppsAndCapk();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        EmvService.Device_Close();
        PinpadService.Close();
    }

    public void startEmv(Context context) {
        int ret;
        final EmvParam emvParam = TelpoConfig.getDefaultEmvParam();
        startCardReading(context);
    }


    private void startCardReading(Context context) {
        openDevices(context);
        if (!magReaderOpen && !iccReaderOpen && !nfcReaderOpen) {
            onFailure("No Card reader device open!");
            return;
        }
        CardType detectedCard = detectCard();

        if (detectedCard == null) {
            onFailure("No card detected");
            return;
        }

        switch (detectedCard) {
            case MAGSTRIPE:
                processMagStripe();
                break;
            case ICC:
                EmvProcess emvProcess = new EmvProcess(context);
                emvProcess.processIcc(context);
                break;
            case NFC:
                processNFC();
                break;
            default:
                break;
        }


    }

    private static void onFailure(String message) {
        destroySession();
    }

    private static void openDevices(Context context) {
        int ret;
        Log.d(TAG, "Opening card reader devices");
        ret = EmvService.Open(context);
       ret = EmvService.deviceOpen();
        // Open MAGSTRIPE
        int magsOpen = EmvService.MagStripeOpenReader();
        Log.d(TAG, "Open Magstripe Reader: " + (magsOpen == EmvService.EMV_DEVICE_TRUE ? "SUCCESS" : "FAILED"));
        if (magsOpen == EmvService.EMV_DEVICE_TRUE) {
            magReaderOpen = true;
        }

        int iccOpen = EmvService.IccOpenReader();
        Log.d(TAG, "Open ICC Reader" + (iccOpen == EmvService.EMV_DEVICE_TRUE ? "SUCCESS" : "FAILED"));
        if (iccOpen == EmvService.EMV_DEVICE_TRUE) {
            iccReaderOpen = true;
        }

        int nfcOpen = EmvService.NfcOpenReader(1000);
        Log.d(TAG, "Open NFC Reader" + (nfcOpen == EmvService.EMV_DEVICE_TRUE ? "SUCCESS" : "FAILED"));

        if (nfcOpen == EmvService.EMV_DEVICE_TRUE) {
            nfcReaderOpen = true;
        }
    }

    private static CardType detectCard() {
        {
            int ret;

            if (magReaderOpen) {
                ret = EmvService.MagStripeCheckCard(1000);

                if (ret == 0) {
                    Log.d(TAG, "Card Found: Magstripe");
                    return CardType.MAGSTRIPE;
                }
            }

            if (iccReaderOpen) {
                ret = EmvService.IccCheckCard(300);
                if (ret == 0) {
                    Log.d(TAG, "Card Found: ICC");
                    return CardType.ICC;
                }
            }

            if (nfcReaderOpen) {
                ret = EmvService.NfcCheckCard(1000);
                if (ret == 0) {
                    Log.d(TAG, "Card Found: ICC");
                    return CardType.NFC;
                }
            }

            // This point is never reached if a card is detected.
            return null;
        }
    }

    private static void destroySession() {

        if (magReaderOpen)
            EmvService.MagStripeCloseReader();

        if (iccReaderOpen)
            EmvService.IccCloseReader();

        if (nfcReaderOpen)
            EmvService.NfcCloseReader();
    }


    private static void addAppsAndCapk() {
        Log.d(TAG, "Adding APPs and CAPKs");

        EmvService.Emv_RemoveAllApp();
        //DefaultAPPCAPK.Add_All_APP();
        CRDBApps.addAppALL();

        EmvService.Emv_RemoveAllCapk();
        DefaultAPPCAPK.Add_All_CAPK_Test();
        DefaultAPPCAPK.Add_All_CAPK();
        try {
            CRDBCapk.addVisaCAPK();
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }

}
