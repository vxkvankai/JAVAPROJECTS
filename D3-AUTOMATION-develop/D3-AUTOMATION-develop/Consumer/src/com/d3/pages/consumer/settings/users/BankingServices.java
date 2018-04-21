package com.d3.pages.consumer.settings.users;

import com.d3.datawrappers.user.D3SecondaryUser;
import com.d3.datawrappers.user.enums.AccessLevel;
import com.d3.datawrappers.user.enums.UserServices;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class BankingServices extends PageObjectBase {

    @FindBy(css = "button.btn-submit")
    private Element submitButton;

    @FindBy(name = "accessValue")
    private List<Select> accessLevel;

    @FindBy(css = "label[for^='access_view']")
    private List<Element> availableService;

    public BankingServices(WebDriver driver) {
        super(driver);
    }

    @Override
    protected BankingServices me() {
        return this;
    }

    public UsersPage clickSubmitButton() {
        submitButton.click();
        return UsersPage.initialize(driver, UsersPage.class);
    }


    /**
     * This will go through all of the Banking Services available to set permission levels for
     * If the secondary user has access to an available service, set it's access level to the max access (Read Only or Full Access depending on service)
     * Otherwise, set the Banking Service access level to No Access
     *
     * @param secondaryUser Secondary user to set access levels for
     * @return Banking Services page
     */
    public BankingServices setBankingServicePermissions(D3SecondaryUser secondaryUser) {
        for (int x = 0; x < availableService.size(); x++) {
            String service = availableService.get(x).getText().trim().replaceAll("-", "").toUpperCase();
            if (secondaryUser.getServices().contains(UserServices.valueOf(service))) {
                accessLevel.get(x).selectByValue(UserServices.valueOf(service).getMaxAccess().name());
            } else {
                accessLevel.get(x).selectByText(AccessLevel.NO_ACCESS.toString());
            }
        }
        return this;
    }
}
