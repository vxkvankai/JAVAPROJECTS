package com.d3.api.mappings.ebills;

import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.user.D3User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import okhttp3.ResponseBody;
import org.apache.commons.lang.StringUtils;
import retrofit2.Response;

import java.io.IOException;

public class Tokens {

    @SerializedName("FisvEbillCache")
    @Expose
    private String fisvEbillCache;
    @SerializedName("FisvEbillBillerName")
    @Expose
    private String fisvEbillBillerName;
    @SerializedName("FisvEbillIncludeToken1RegExValidator")
    @Expose
    private String fisvEbillIncludeToken1RegExValidator;
    @SerializedName("FisvEbillIncludeToken2RegExValidator")
    @Expose
    private String fisvEbillIncludeToken2RegExValidator;
    @SerializedName("FisvEbillType4BillerRegExvalidator")
    @Expose
    private String fisvEbillType4BillerRegExvalidator;
    @SerializedName("FisvEbillAction")
    @Expose
    private String fisvEbillAction;
    @SerializedName("FisvEbillTCURLOption1")
    @Expose
    private String fisvEbillTCURLOption1;
    @SerializedName("FisvEbillTCURLOption2")
    @Expose
    private String fisvEbillTCURLOption2;
    @SerializedName("FisvEbillParentFormName")
    @Expose
    private String fisvEbillParentFormName;
    @SerializedName("FisvEbillEmbeddedTCRequired")
    @Expose
    private String fisvEbillEmbeddedTCRequired;
    @SerializedName("FisvEbillEmbeddedTCURL")
    @Expose
    private String fisvEbillEmbeddedTCURL;
    @SerializedName("FisvEbillPresenterId")
    @Expose
    private String fisvEbillPresenterId;
    @SerializedName("FisvEbillEmailShare")
    @Expose
    private String fisvEbillEmailShare;
    @SerializedName("FisvEbillWidgetID")
    @Expose
    private String fisvEbillWidgetID;
    @SerializedName("FisvEbillBackButtonPressed")
    @Expose
    private String fisvEbillBackButtonPressed;
    @SerializedName("FisvEbillToken1Value")
    @Expose
    private String fisvEbillToken1Value;
    @SerializedName("FisvEbillUserFirstName")
    @Expose
    private String fisvEbillUserFirstName;
    @SerializedName("FisvEbillUserMiddleName")
    @Expose
    private String fisvEbillUserMiddleName;
    @SerializedName("FisvEbillUserLastName")
    @Expose
    private String fisvEbillUserLastName;
    @SerializedName("FisvEbillUserAddress1")
    @Expose
    private String fisvEbillUserAddress1;
    @SerializedName("FisvEbillUserAddress2")
    @Expose
    private String fisvEbillUserAddress2;
    @SerializedName("FisvEbillUserCity")
    @Expose
    private String fisvEbillUserCity;
    @SerializedName("FisvEbillUserState")
    @Expose
    private String fisvEbillUserState;
    @SerializedName("FisvEbillUserZip")
    @Expose
    private String fisvEbillUserZip;
    @SerializedName("FisvEbillUserZip4")
    @Expose
    private String fisvEbillUserZip4;
    @SerializedName("FisvEbillSelectedTCOption")
    @Expose
    private String fisvEbillSelectedTCOption;

    public String getFisvEbillCache() {
        return fisvEbillCache;
    }

    public void setFisvEbillCache(String fisvEbillCache) {
        this.fisvEbillCache = fisvEbillCache;
    }

    public String getFisvEbillBillerName() {
        return fisvEbillBillerName;
    }

    public void setFisvEbillBillerName(String fisvEbillBillerName) {
        this.fisvEbillBillerName = fisvEbillBillerName;
    }

    public String getFisvEbillIncludeToken1RegExValidator() {
        return fisvEbillIncludeToken1RegExValidator;
    }

    public void setFisvEbillIncludeToken1RegExValidator(String fisvEbillIncludeToken1RegExValidator) {
        this.fisvEbillIncludeToken1RegExValidator = fisvEbillIncludeToken1RegExValidator;
    }

    public String getFisvEbillIncludeToken2RegExValidator() {
        return fisvEbillIncludeToken2RegExValidator;
    }

    public void setFisvEbillIncludeToken2RegExValidator(String fisvEbillIncludeToken2RegExValidator) {
        this.fisvEbillIncludeToken2RegExValidator = fisvEbillIncludeToken2RegExValidator;
    }

    public String getFisvEbillType4BillerRegExvalidator() {
        return fisvEbillType4BillerRegExvalidator;
    }

    public void setFisvEbillType4BillerRegExvalidator(String fisvEbillType4BillerRegExvalidator) {
        this.fisvEbillType4BillerRegExvalidator = fisvEbillType4BillerRegExvalidator;
    }

    public String getFisvEbillAction() {
        return fisvEbillAction;
    }

    public void setFisvEbillAction(String fisvEbillAction) {
        this.fisvEbillAction = fisvEbillAction;
    }

    public String getFisvEbillTCURLOption1() {
        return fisvEbillTCURLOption1;
    }

    public void setFisvEbillTCURLOption1(String fisvEbillTCURLOption1) {
        this.fisvEbillTCURLOption1 = fisvEbillTCURLOption1;
    }

    public String getFisvEbillTCURLOption2() {
        return fisvEbillTCURLOption2;
    }

    public void setFisvEbillTCURLOption2(String fisvEbillTCURLOption2) {
        this.fisvEbillTCURLOption2 = fisvEbillTCURLOption2;
    }

    public String getFisvEbillParentFormName() {
        return fisvEbillParentFormName;
    }

    public void setFisvEbillParentFormName(String fisvEbillParentFormName) {
        this.fisvEbillParentFormName = fisvEbillParentFormName;
    }

    public String getFisvEbillEmbeddedTCRequired() {
        return fisvEbillEmbeddedTCRequired;
    }

    public void setFisvEbillEmbeddedTCRequired(String fisvEbillEmbeddedTCRequired) {
        this.fisvEbillEmbeddedTCRequired = fisvEbillEmbeddedTCRequired;
    }

    public String getFisvEbillEmbeddedTCURL() {
        return fisvEbillEmbeddedTCURL;
    }

    public void setFisvEbillEmbeddedTCURL(String fisvEbillEmbeddedTCURL) {
        this.fisvEbillEmbeddedTCURL = fisvEbillEmbeddedTCURL;
    }

    public String getFisvEbillPresenterId() {
        return fisvEbillPresenterId;
    }

    public void setFisvEbillPresenterId(String fisvEbillPresenterId) {
        this.fisvEbillPresenterId = fisvEbillPresenterId;
    }

    public String getFisvEbillEmailShare() {
        return fisvEbillEmailShare;
    }

    public void setFisvEbillEmailShare(String fisvEbillEmailShare) {
        this.fisvEbillEmailShare = fisvEbillEmailShare;
    }

    public String getFisvEbillWidgetID() {
        return fisvEbillWidgetID;
    }

    public void setFisvEbillWidgetID(String fisvEbillWidgetID) {
        this.fisvEbillWidgetID = fisvEbillWidgetID;
    }

    public String getFisvEbillBackButtonPressed() {
        return fisvEbillBackButtonPressed;
    }

    public void setFisvEbillBackButtonPressed(String fisvEbillBackButtonPressed) {
        this.fisvEbillBackButtonPressed = fisvEbillBackButtonPressed;
    }

    public String getFisvEbillToken1Value() {
        return fisvEbillToken1Value;
    }

    public void setFisvEbillToken1Value(String fisvEbillToken1Value) {
        this.fisvEbillToken1Value = fisvEbillToken1Value;
    }

    public String getFisvEbillUserFirstName() {
        return fisvEbillUserFirstName;
    }

    public void setFisvEbillUserFirstName(String fisvEbillUserFirstName) {
        this.fisvEbillUserFirstName = fisvEbillUserFirstName;
    }

    public String getFisvEbillUserMiddleName() {
        return fisvEbillUserMiddleName;
    }

    public void setFisvEbillUserMiddleName(String fisvEbillUserMiddleName) {
        this.fisvEbillUserMiddleName = fisvEbillUserMiddleName;
    }

    public String getFisvEbillUserLastName() {
        return fisvEbillUserLastName;
    }

    public void setFisvEbillUserLastName(String fisvEbillUserLastName) {
        this.fisvEbillUserLastName = fisvEbillUserLastName;
    }

    public String getFisvEbillUserAddress1() {
        return fisvEbillUserAddress1;
    }

    public void setFisvEbillUserAddress1(String fisvEbillUserAddress1) {
        this.fisvEbillUserAddress1 = fisvEbillUserAddress1;
    }

    public String getFisvEbillUserAddress2() {
        return fisvEbillUserAddress2;
    }

    public void setFisvEbillUserAddress2(String fisvEbillUserAddress2) {
        this.fisvEbillUserAddress2 = fisvEbillUserAddress2;
    }

    public String getFisvEbillUserCity() {
        return fisvEbillUserCity;
    }

    public void setFisvEbillUserCity(String fisvEbillUserCity) {
        this.fisvEbillUserCity = fisvEbillUserCity;
    }

    public String getFisvEbillUserState() {
        return fisvEbillUserState;
    }

    public void setFisvEbillUserState(String fisvEbillUserState) {
        this.fisvEbillUserState = fisvEbillUserState;
    }

    public String getFisvEbillUserZip() {
        return fisvEbillUserZip;
    }

    public void setFisvEbillUserZip(String fisvEbillUserZip) {
        this.fisvEbillUserZip = fisvEbillUserZip;
    }

    public String getFisvEbillUserZip4() {
        return fisvEbillUserZip4;
    }

    public void setFisvEbillUserZip4(String fisvEbillUserZip4) {
        this.fisvEbillUserZip4 = fisvEbillUserZip4;
    }

    public String getFisvEbillSelectedTCOption() {
        return fisvEbillSelectedTCOption;
    }

    public void setFisvEbillSelectedTCOption(String fisvEbillSelectedTCOption) {
        this.fisvEbillSelectedTCOption = fisvEbillSelectedTCOption;
    }


    public static Tokens getActivationTokens(D3User user, Recipient recipient, Response<ResponseBody> response) throws IOException {
        String activationFormDetails = new String(response.body().bytes());
        Tokens tokens = new Tokens();
        tokens.setFisvEbillAction("Activate");
        tokens.setFisvEbillBillerName(recipient.getName());
        tokens.setFisvEbillCache(getFormValue(activationFormDetails, "FisvEbillCache"));
        tokens.setFisvEbillEmailShare(String.valueOf(false));
        tokens.setFisvEbillEmbeddedTCRequired(getFormValue(activationFormDetails, "FisvEbillEmbeddedTCRequired"));
        tokens.setFisvEbillEmbeddedTCURL(null);
        tokens.setFisvEbillIncludeToken1RegExValidator(null);
        tokens.setFisvEbillIncludeToken2RegExValidator(null);
        tokens.setFisvEbillParentFormName(getFormValue(activationFormDetails, "FisvEbillParentFormName"));
        tokens.setFisvEbillPresenterId(getFormValue(activationFormDetails, "FisvEbillPresenterId"));
        tokens.setFisvEbillTCURLOption1(null);
        tokens.setFisvEbillTCURLOption2(null);
        tokens.setFisvEbillToken1Value(user.getAPhoneNumberContact().getValue());
        tokens.setFisvEbillType4BillerRegExvalidator(null);
        tokens.setFisvEbillUserAddress1(user.getProfile().getPhysicalAdd().getAddress1());
        tokens.setFisvEbillUserAddress2(user.getProfile().getPhysicalAdd().getAddress1());
        tokens.setFisvEbillUserCity(user.getProfile().getPhysicalAdd().getCity());
        tokens.setFisvEbillUserFirstName(user.getProfile().getFirstName());
        tokens.setFisvEbillUserMiddleName(user.getProfile().getMiddleName());
        tokens.setFisvEbillUserLastName(user.getProfile().getLastName());
        tokens.setFisvEbillUserState(user.getProfile().getPhysicalAdd().getState());
        tokens.setFisvEbillUserZip(user.getProfile().getPhysicalAdd().getPostalCode());
        tokens.setFisvEbillUserZip4(null);
        tokens.setFisvEbillWidgetID(getFormValue(activationFormDetails, "FisvEbillWidgetID"));
        return tokens;
    }

    /**
     * The method will get the value of an element from the ebill activation form html
     *
     * Example HTML: <input type=\"hidden\" id =\"FisvEbillWidgetID\" name=\"FisvEbillWidgetID\" value=\"ebc1\"
     * Would return the value ebc1
     * @param formHtml The JSON Response String from the @GET Call BankingService.getActivationForm(providerId);
     * @param element The locator id or name of the element you want the value of
     * @return String locator value
     */
    private static String getFormValue(String formHtml, String element) {
        String locator = StringUtils.substringAfterLast(formHtml, element);
        String value = StringUtils.substringBefore(locator, "/>\\n");
        return StringUtils.substringBetween(value, "value=\\", "\\").replaceAll("\"", "");
    }

}
