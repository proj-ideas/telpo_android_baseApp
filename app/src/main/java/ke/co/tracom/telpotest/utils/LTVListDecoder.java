package ke.co.tracom.telpotest.utils;

import org.jpos.iso.ISOUtil;

import java.util.HashMap;
import java.util.Map;

public class LTVListDecoder {

    private Map<String, String> tv;

    public LTVListDecoder(byte[] ltvList) {
        this.tv = new HashMap<>();

        int offset = 0;

        while (offset < ltvList.length) {
            // Get the first two bytes from the list
            int l = Integer.parseInt(
                    ISOUtil.zeroUnPad(ISOUtil.bcd2str(ltvList, offset, 4, false))
            );


            offset += 2;

            String payload = new String(
                    ISOUtil.hex2byte(
                            ISOUtil.bcd2str(ltvList, offset, l * 2, false)
                    )
            );

            offset += l;


            byte[] payloadBytes = ISOUtil.hex2byte(payload);


            String tag = ISOUtil.bcd2str(payloadBytes, 0, 2, false);
            String value = ISOUtil.bcd2str(payloadBytes, 1, (payloadBytes.length * 2) - 2, false);


            if (tv.containsKey(tag)) {
                int takenValue = Math.max(
                        Integer.parseInt(tv.get(tag)),
                        Integer.parseInt(value)
                );

                value = ISOUtil.zeropad(takenValue, value.length());
            }


            tv.put(tag, value);
        }
    }

    public String getValue(String tag) {
        return tv.get(tag);
    }
}
