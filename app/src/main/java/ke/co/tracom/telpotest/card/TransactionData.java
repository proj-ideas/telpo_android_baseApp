package ke.co.tracom.telpotest.card;



import androidx.annotation.Nullable;

import org.jpos.iso.ISOMsg;

public class TransactionData {
    private long amount = 0;
    private long cashBackAmount = 0;
    /**
     * Transaction currency
     * */
    private short transactionCurrency = (short) 834;
    @Nullable
    private ISOMsg requestData;
    @Nullable
    private ISOMsg responseData;

    private int errorCode = -1;

    /**
     * Get the amount transacted
     *
     * @return amount
     * */
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    /**
     * Get cashback amount
     *
     * @return cashBackAmount
     * */
    public long getCashBackAmount() {
        return cashBackAmount;
    }

    public void setCashBackAmount(long cashBackAmount) {
        this.cashBackAmount = cashBackAmount;
    }

    /**
     * Get transaction currency
     *
     * */
    public short getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(short transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    /**
     * Request ISO Message
     *
     * @return {@link ISOMsg}
     * */
    @Nullable
    public ISOMsg getRequestData() {
        return requestData;
    }

    public void setRequestData(ISOMsg requestData) {
        this.requestData = requestData;
    }

    /**
     * Response ISO Message
     *
     * @return {@link ISOMsg}
     * */
    @Nullable
    public ISOMsg getResponseData() {
        return responseData;
    }

    public void setResponseData(ISOMsg responseData) {
        this.responseData = responseData;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
