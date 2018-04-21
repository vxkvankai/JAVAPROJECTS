package com.d3.pages.consumer.moneymovement.ebills.forms;

import com.d3.datawrappers.ebills.enums.FileReason;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.l10n.moneymovement.EBillsL10N;
import com.d3.pages.consumer.moneymovement.MoneyMovementBasePage;
import com.d3.pages.consumer.moneymovement.ebills.EBillsPage;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.Select;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;


public class FileForm extends MoneyMovementBasePage {

    @FindBy(id = "reason")
    private Select fileReason;

    @FindBy(css = "li.entity.active #filingNote")
    private Element fileNote;

    @FindBy(css = "button.saveFile")
    private Element fileSave;

    public FileForm(WebDriver driver) {
        super(driver);
    }

    @Override
    protected PageObjectBase me() {
        return this;
    }

    public FileForm selectFileReason(FileReason reason) {
        fileReason.selectByValue(reason.toString());
        return this;
    }

    public FileForm enterFillingNote(String note) {
        fileNote.sendKeys(note);
        return this;
    }

    public EBillsPage clickFileSaveButton() {
        fileSave.click();
        waitForSpinner();
        return EBillsPage.initialize(driver, EBillsPage.class);
    }

    public boolean isFileFormCorrect() {
        String errMsg = "The following text was not displayed on the File E-Bill Form: %s";

        try {
            checkIfTextDisplayed(EBillsL10N.Localization.MINIMUM_DUE.getValue(), errMsg);
            checkIfTextDisplayed(EBillsL10N.Localization.BALANCE.getValue(), errMsg);
            checkIfTextDisplayed(EBillsL10N.Localization.FILE_EBILL.getValue(), errMsg);
            checkIfTextDisplayed(EBillsL10N.Localization.FILE_REASON.getValue(), errMsg);
            checkIfTextDisplayed(EBillsL10N.Localization.FILE_NOTE.getValue(), errMsg);
            checkIfTextDisplayed(EBillsL10N.Localization.ADDITIONAL_DETAILS_LINK.getValue(), errMsg);
            return fileReason.getValueAttribute().equals(FileReason.BANK.toString());
        } catch (TextNotDisplayedException e) {
            logger.error("E-Bills L10N was not displayed on File form", e);
            return false;
        }
    }
}
