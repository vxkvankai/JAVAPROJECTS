package com.d3.api.mappings.ebills;

import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.datawrappers.user.D3Contact;
import com.d3.datawrappers.user.D3User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import okhttp3.ResponseBody;
import org.apache.commons.lang.StringUtils;
import retrofit2.Response;

import java.io.IOException;


@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tokens {

    private String fisvEbillCache;
    private String fisvEbillBillerName;
    private String fisvEbillIncludeToken1RegExValidator;
    private String fisvEbillIncludeToken2RegExValidator;
    private String fisvEbillType4BillerRegExvalidator;
    private String fisvEbillAction;
    private String fisvEbillTCURLOption1;
    private String fisvEbillTCURLOption2;
    private String fisvEbillParentFormName;
    private String fisvEbillEmbeddedTCRequired;
    private String fisvEbillEmbeddedTCURL;
    private String fisvEbillPresenterId;
    private String fisvEbillEmailShare;
    private String fisvEbillWidgetID;
    private String fisvEbillBackButtonPressed;
    private String fisvEbillToken1Value;
    private String fisvEbillUserFirstName;
    private String fisvEbillUserMiddleName;
    private String fisvEbillUserLastName;
    private String fisvEbillUserAddress1;
    private String fisvEbillUserAddress2;
    private String fisvEbillUserCity;
    private String fisvEbillUserState;
    private String fisvEbillUserZip;
    private String fisvEbillUserZip4;
    private String fisvEbillSelectedTCOption;

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

        D3Contact phoneContact = user.getAPhoneNumberContact();
        if (phoneContact != null) {
            tokens.setFisvEbillToken1Value(phoneContact.getValue());
        }

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
