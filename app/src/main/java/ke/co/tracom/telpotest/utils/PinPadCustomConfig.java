package ke.co.tracom.telpotest.utils;

import com.telpo.pinpad.PinTextInfo;

import java.util.ArrayList;

public class PinPadCustomConfig {
    private ArrayList<PinTextInfo> infos;
    private boolean showCardNumber = false;
    private int yPosition = 40;

    /**
     * Creates custom text for display on PINPAD
     *
     * @param displayText | The text to display on Pinpad. Each instance takes one line
     * */
    public PinPadCustomConfig(PinPadText... displayText) {
        infos = new ArrayList<>();

        for (PinPadText text : displayText) {
            buildText(text);
        }

    }

    private void buildText(PinPadText text){
        PinTextInfo pinTextInfo = new PinTextInfo();
        pinTextInfo.FontColor = text.color;
        pinTextInfo.FontFile = "";
        pinTextInfo.FontSize = text.size;
        pinTextInfo.PosX = 70;
        pinTextInfo.PosY = yPosition;
        pinTextInfo.sText = text.text;
        pinTextInfo.LanguageID = "en";

        infos.add(pinTextInfo);
        yPosition = yPosition + text.size;
    }

    /**
     * Add text to the PINPAD TextView
     *
     * @param text | {@link PinPadText}
    * */
    public void addText(PinPadText text){
        buildText(text);
    }

    public boolean isShowCardNumber() {
        return showCardNumber;
    }

    public void setShowCardNumber(boolean showCardNumber) {
        this.showCardNumber = showCardNumber;
    }

    public PinTextInfo[] getInfos() {

        PinTextInfo[] pinTextInfo = new PinTextInfo[infos.size()];
        return infos.toArray(pinTextInfo);
    }

    /**
     * Build PinPadText
     *
     * @param text | Text to display
     * @param color | Text color
     * @param size | text size between 0 and 100
     * */
    public static PinPadText text(String text, int color, int size){
        return new PinPadText(text, color, size);
    }

    public static class PinPadText{
        private String text = "";
        private int color = 0x000000;
        private int size = 28;

        protected PinPadText(String text, int color, int size) {
            this.text = text;
            this.color = color;

            if(size <= 10 || size > 100){
            }else{
                this.size = size;
            }

        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}