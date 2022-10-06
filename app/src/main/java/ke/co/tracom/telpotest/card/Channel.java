package ke.co.tracom.telpotest.card;

import android.util.Log;

import org.jpos.core.Configuration;
import org.jpos.core.SimpleConfiguration;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.NACChannel;
import org.jpos.util.LogListener;
import org.jpos.util.Logger;
import org.jpos.util.ProtectedLogListener;
import org.jpos.util.SimpleLogListener;

import java.io.IOException;


import ke.co.tracom.telpotest.config.TelpoConfig;

public class Channel {
    private static final String TAG = "CRDB-Channel";

    public static ISOMsg sendIsoMessage(ISOMsg isoMsg, ISOPackager packager)
            throws ISOException, IOException {

        TelpoConfig config = new TelpoConfig();
        String server = config.getSERVER_IP();
        int port = config.getSERVER_PORT();
        ISOMsg response = new ISOMsg();

        Logger logger = new Logger();
        ProtectedLogListener protLog = new ProtectedLogListener();
        Configuration conf = new SimpleConfiguration();
        conf.put("protect", "2 35");
        conf.put("wipe", "52");
        protLog.setConfiguration(conf);
        logger.addListener((LogListener) new SimpleLogListener(System.out));
        logger.addListener(protLog);

        NACChannel channel = new NACChannel(server, port, packager,
                ISOUtil.hex2byte("6000030000"));
        channel.setLogger(logger, "ProtectedLogListener");
        ISOMsg duplicate = isoMsg;
        /*if(isoMsg.hasField(2)){
            isoMsg.set(2, ProtectedLogListener.BINARY_WIPED);
        }
        if(isoMsg.hasField(35)){
            isoMsg.set(35, ProtectedLogListener.BINARY_WIPED);
        }
        if(isoMsg.hasField(52)){
            isoMsg.set(52, ProtectedLogListener.WIPED);
        }*/
        //System.out.println(ISOUtil.hexdump(isoMsg.pack()));
        isoMsg.dump(System.out, "Test/request");

        channel.setTimeout(config.getConnectionTimeout() * 1000);
        channel.connect();
        if(channel.isConnected()) {
            channel.send(duplicate);
            Log.i(TAG, "send(): waiting for response");
            response = channel.receive();
            /*if(response.hasField(2)){
                response.set(2, ProtectedLogListener.BINARY_WIPED);
            }
            if(response.hasField(35)){
                response.set(35, ProtectedLogListener.BINARY_WIPED);
            }
            if(response.hasField(52)){
                response.set(52, ProtectedLogListener.WIPED);
            }*/
            Log.i(TAG, "send(): Received response");
            response.dump(System.out, "Test/response");

            channel.disconnect();
        }else{
            Log.i(TAG, "connection failed");
        }

        return response;
    }
}
