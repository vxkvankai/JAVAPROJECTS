package com.d3.api.mappings.alerts;

import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomNumberString;

import com.d3.datawrappers.alerts.CheckClearedAlert;
import com.d3.datawrappers.alerts.MerchantActivityAlert;
import com.d3.datawrappers.alerts.enums.BalanceThreshold;
import com.d3.datawrappers.alerts.enums.BudgetThreshold;
import com.d3.helpers.RandomHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties {

    @SerializedName("OPERATOR")
    @Expose
    private String oPERATOR;
    @SerializedName("THRESHOLD")
    @Expose
    private Object tHRESHOLD;
    @SerializedName("THRESHOLD_ATTR")
    @Expose
    private String tHRESHOLDATTR;
    @SerializedName("THRESHOLD_TYPE")
    @Expose
    private String tHRESHOLDTYPE;
    @SerializedName("CHECK_NUMBER")
    @Expose
    private String cHECKNUMBER;
    @SerializedName("MERCHANT")
    @Expose
    private String mERCHANT;
    @SerializedName("TRAN_TYPE")
    @Expose
    private String tRANTYPE;

    public String getOPERATOR() {
        return oPERATOR;
    }

    public void setOPERATOR(String oPERATOR) {
        this.oPERATOR = oPERATOR;
    }

    public Object getTHRESHOLD() {
        return tHRESHOLD;
    }

    public void setTHRESHOLD(Object tHRESHOLD) {
        this.tHRESHOLD = tHRESHOLD;

    }

    public String getTHRESHOLDATTR() {
        return tHRESHOLDATTR;
    }

    public void setTHRESHOLDATTR(String tHRESHOLDATTR) {
        this.tHRESHOLDATTR = tHRESHOLDATTR;
    }

    public String getTHRESHOLDTYPE() {
        return tHRESHOLDTYPE;
    }

    public void setTHRESHOLDTYPE(String tHRESHOLDTYPE) {
        this.tHRESHOLDTYPE = tHRESHOLDTYPE;
    }

    public String getCHECKNUMBER() {
        return cHECKNUMBER;
    }

    public void setCHECKNUMBER(String cHECKNUMBER) {
        this.cHECKNUMBER = cHECKNUMBER;
    }

    public String getMERCHANT() {
        return mERCHANT;
    }

    public void setMERCHANT(String mERCHANT) {
        this.mERCHANT = mERCHANT;
    }

    public String getTRANTYPE() {
        return tRANTYPE;
    }

    public void setTRANTYPE(String tRANTYPE) {
        this.tRANTYPE = tRANTYPE;
    }

    public Properties balanceThreshold() {
        Properties properties = new Properties();
        properties.setOPERATOR(RandomHelper.getRandomElementFromArray(BalanceThreshold.values()).name());
        properties.setTHRESHOLD(RandomHelper.getRandomNumberInt(1000, 9999));
        return properties;
    }

    public Properties categoryThreshold() {
        Properties properties = new Properties();
        properties.setTHRESHOLDTYPE(RandomHelper.getRandomElementFromArray(BudgetThreshold.Type.values()).name());
        properties.setTHRESHOLDATTR(RandomHelper.getRandomElementFromArray(BudgetThreshold.Value.values()).name());
        properties.setTHRESHOLD(getRandomNumber(1, 99));
        return properties;
    }

    public Properties checkCleared(CheckClearedAlert alert) {
        Properties properties = new Properties();
        properties.setCHECKNUMBER(alert.getCheckNumber());
        return properties;
    }

    public Properties merchantActivity(MerchantActivityAlert alert) {
        Properties properties = new Properties();
        properties.setMERCHANT(alert.getMerchantName());
        return properties;
    }

    public Properties totalBudgetThreshold() {
        Properties properties = new Properties();
        properties.setTHRESHOLDTYPE(RandomHelper.getRandomElementFromArray(BudgetThreshold.Type.values()).name());
        properties.setTHRESHOLDATTR(RandomHelper.getRandomElementFromArray(BudgetThreshold.Value.values()).name());
        properties.setTHRESHOLD(getRandomNumber(1, 99).toString());
        return properties;
    }

    public Properties transactionExceedsAmount() {
        String [] transactionType = {"ALL", "CREDIT", "DEBIT"};
        Properties properties = new Properties();
        properties.setTRANTYPE(RandomHelper.getRandomElementFromArray(transactionType));
        properties.setTHRESHOLD(Integer.valueOf(getRandomNumberString(3,false)));
        return properties;
    }

}
