package com.d3.api.mappings.alerts;

import static com.d3.helpers.RandomHelper.getRandomNumber;
import static com.d3.helpers.RandomHelper.getRandomNumberString;

import com.d3.datawrappers.alerts.CheckClearedAlert;
import com.d3.datawrappers.alerts.MerchantActivityAlert;
import com.d3.datawrappers.alerts.enums.BalanceThreshold;
import com.d3.datawrappers.alerts.enums.BudgetThreshold;
import com.d3.helpers.RandomHelper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("SpellCheckingInspection")
public class Properties {

    private String OPERATOR; // NOSONAR needs to match api var
    private Object THRESHOLD; // NOSONAR needs to match api var
    private String THRESHOLD_ATTR; // NOSONAR needs to match api var
    private String THRESHOLD_TYPE; // NOSONAR needs to match api var
    private String CHECK_NUMBER; // NOSONAR needs to match api var
    private String MERCHANT; // NOSONAR needs to match api var
    private String TRAN_TYPE; // NOSONAR needs to match api var

    public Properties balanceThreshold() {
        Properties properties = new Properties();
        properties.setOPERATOR(RandomHelper.getRandomElementFromArray(BalanceThreshold.values()).name());
        properties.setTHRESHOLD(RandomHelper.getRandomNumberInt(1000, 9999));
        return properties;
    }

    public Properties categoryThreshold() {
        Properties properties = new Properties();
        properties.setTHRESHOLD_TYPE(RandomHelper.getRandomElementFromArray(BudgetThreshold.Type.values()).name());
        properties.setTHRESHOLD_ATTR(RandomHelper.getRandomElementFromArray(BudgetThreshold.Value.values()).name());
        properties.setTHRESHOLD(getRandomNumber(1, 99));
        return properties;
    }

    public Properties checkCleared(CheckClearedAlert alert) {
        Properties properties = new Properties();
        properties.setCHECK_NUMBER(alert.getCheckNumber());
        return properties;
    }

    public Properties merchantActivity(MerchantActivityAlert alert) {
        Properties properties = new Properties();
        properties.setMERCHANT(alert.getMerchantName());
        return properties;
    }

    public Properties totalBudgetThreshold() {
        Properties properties = new Properties();
        properties.setTHRESHOLD_TYPE(RandomHelper.getRandomElementFromArray(BudgetThreshold.Type.values()).name());
        properties.setTHRESHOLD_ATTR(RandomHelper.getRandomElementFromArray(BudgetThreshold.Value.values()).name());
        properties.setTHRESHOLD(getRandomNumber(1, 99).toString());
        return properties;
    }

    public Properties transactionExceedsAmount() {
        String [] transactionType = {"ALL", "CREDIT", "DEBIT"};
        Properties properties = new Properties();
        properties.setTRAN_TYPE(RandomHelper.getRandomElementFromArray(transactionType));
        properties.setTHRESHOLD(Integer.valueOf(getRandomNumberString(3,false)));
        return properties;
    }

}
