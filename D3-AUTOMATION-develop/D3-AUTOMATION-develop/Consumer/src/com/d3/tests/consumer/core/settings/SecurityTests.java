package com.d3.tests.consumer.core.settings;

import com.d3.datawrappers.user.D3User;
import com.d3.l10n.settings.SecurityL10N;
import com.d3.pages.consumer.dashboard.Dashboard;
import com.d3.pages.consumer.settings.security.ChangePasswordForm;
import com.d3.pages.consumer.settings.security.ChangeSecurityQuestionsForm;
import com.d3.pages.consumer.settings.security.ChangeUserNameForm;
import com.d3.pages.consumer.settings.security.Security;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import org.testng.Assert;
import org.testng.annotations.Test;


@Feature("Settings")
@Feature("Security Tab")
public class SecurityTests extends SettingsTestBase {

    private String securityAnswer = "D3B@nk!ng";

    @Issue("DBD-1985")
    @TmsLink("288112")
    @Story("Security Questions")
    @Story("Change Security Questions")
    @Test(dataProvider = "Basic User")
    public void verifyChangeSecurityQuestions(D3User user) {
        String securityQuestion1 = "What is the first name of your oldest nephew?";
        String securityQuestion2 = "What was the name of your first pet?";
        String securityQuestion3 = "What was your favorite restaurant in college?";

        Dashboard dashboard = login(user);
        ChangeSecurityQuestionsForm changeSecurityQuestionsForm = dashboard.getHeader()
                .clickSettingsButton()
                .getTabs()
                .clickSecurityLink()
                .clickChangeSecurityQuestionsButton();

        Security securityPage = changeSecurityQuestionsForm
                .enterCurrentPassword(user.getPassword())
                .changeFirstSecurityQuestion(securityQuestion1)
                .changeFirstSecurityQuestionAnswer(securityAnswer)
                .changeSecondSecurityQuestion(securityQuestion2)
                .changeSecondSecurityQuestionAnswer(securityAnswer)
                .changeThirdSecurityQuestion(securityQuestion3)
                .changeThirdSecurityQuestionAnswer(securityAnswer)
                .clickSaveButton();
        Assert.assertTrue(changeSecurityQuestionsForm.isTextDisplayed(SecurityL10N.Localization.CHANGE_SECURITY_QUESTIONS_SUCCESS.getValue()),
                "Security questions changed message not displayed");

        changeSecurityQuestionsForm = securityPage.clickChangeSecurityQuestionsButton();
        Assert.assertTrue(changeSecurityQuestionsForm.verifyQuestionsChanged(securityQuestion1, securityQuestion2, securityQuestion3),
                "Updated security questions did not save");
    }


    @TmsLink("1010")
    @Story("Security Questions")
    @Story("Change Security Answers")
    @Test(dataProvider = "Basic User")
    public void verifyChangeSecurityAnswers(D3User user) {
        Dashboard dashboard = login(user);

        ChangeSecurityQuestionsForm changeSecurityQuestionsForm =
                dashboard.getHeader().clickSettingsButton().getTabs().clickSecurityLink().clickChangeSecurityQuestionsButton();

        Security securityPage = changeSecurityQuestionsForm
                .enterCurrentPassword(user.getPassword())
                .changeFirstSecurityQuestionAnswer(securityAnswer)
                .changeSecondSecurityQuestionAnswer(securityAnswer)
                .changeThirdSecurityQuestionAnswer(securityAnswer)
                .clickSaveButton();

        Assert.assertTrue(changeSecurityQuestionsForm.isTextDisplayed(SecurityL10N.Localization.CHANGE_SECURITY_QUESTIONS_SUCCESS.getValue()),
                "Security questions changed message not displayed");

        changeSecurityQuestionsForm = securityPage.clickChangeSecurityQuestionsButton();
        Assert.assertTrue(changeSecurityQuestionsForm.verifyAnswersChanged(securityAnswer), "Updated security answers did not save");

    }


    @TmsLink("288113")
    @Story("Change Password")
    @Test(dataProvider = "Basic User")
    public void verifyChangingPassword(D3User user) {
        Dashboard dashboard = login(user);
        ChangePasswordForm changePasswordForm = dashboard.getHeader().clickSettingsButton().getTabs().clickSecurityLink().clickChangePasswordButton();

        Assert.assertTrue(
                changePasswordForm.isTextDisplayed(SecurityL10N.Localization.PASSWORD_PATTERN_DESCRIPTION.getValue()),
                "Password requirements text not displayed");

        String newPassword = "Password1!"; // NOSONAR
        changePasswordForm.enterCurrentPassword(user.getPassword())
                .enterNewPassword(newPassword)
                .confirmCurrentPassword(newPassword)
                .clickSaveButton();

        Assert.assertTrue(changePasswordForm.isTextDisplayed(SecurityL10N.Localization.CHANGE_PASSWORD_SUCCESS.getValue()),
                "Password changed message is not displayed");
        // TODO this needs to verify that the password changed
    }

    @TmsLink("288114")
    @Story("Change Username")
    @Feature("Settings")
    @Feature("Security Tab")
    @Test(dataProvider = "Basic User")
    public void verifyChangingUserName(D3User user) {
        String newUsername = user.getLogin() + "1";
        Dashboard dashboard = login(user);
        ChangeUserNameForm changeUserNameForm = dashboard.getHeader().clickSettingsButton().getTabs().clickSecurityLink().clickChangeUserNameButton();

        Assert.assertTrue(changeUserNameForm.isTextDisplayed(SecurityL10N.Localization.CHANGE_USERNAME_WARNING.getValue()));

        changeUserNameForm.enterNewUserName(newUsername)
                .confirmNewUserName(newUsername)
                .enterCurrentPassword(user.getPassword())
                .clickSaveButton();

        Assert.assertTrue(changeUserNameForm.isTextDisplayed(SecurityL10N.Localization.CHANGE_USERNAME_SUCCESS.getValue()),
                "User name changed success message not displayed");
        Assert.assertTrue(changeUserNameForm.verifyUserNameChanged(newUsername));
    }
}
