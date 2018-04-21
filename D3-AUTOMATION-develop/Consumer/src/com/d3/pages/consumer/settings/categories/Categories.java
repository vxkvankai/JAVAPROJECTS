package com.d3.pages.consumer.settings.categories;

import com.d3.exceptions.TextNotDisplayedException;
import com.d3.l10n.settings.CategoriesL10N;
import com.d3.support.D3Element;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class Categories extends PageObjectBase {

    @FindBy(css = "button.delete-category")
    private Element deleteCategoryButton;

    @FindBy(css = "button.delete-cancel")
    private Element deleteCategoryCancel;

    @FindBy(css = "button.submit-one")
    private Element deleteCategoryConfirm;

    @FindBy(name = "name")
    private Element categoryName;

    @FindBy(css = "li.entity.category")
    private List<Element> customCategory;

    private D3Element categoryWithName(String categoryName) {
        By by = By.xpath(String.format("//input[contains(@value,'%s')]", categoryName));
        return new D3Element(driver.findElement(by));
    }

    public Categories(WebDriver driver) {
        super(driver);
    }

    @Override
    protected Categories me() {
        return this;
    }

    public Categories clickDeleteButton() {
        logger.info("Clicking delete button for custom category");
        deleteCategoryButton.click();
        return this;
    }

    public Categories clickDeleteCancelButton() {
        logger.info("Clicking cancel button for deleting custom category");
        deleteCategoryCancel.click();
        return this;
    }

    public Categories clickDeleteConfirmButton() {
        logger.info("Clicking confirm delete button for custom category");
        deleteCategoryConfirm.click();
        return this;
    }

    public Categories clickCategory(String categoryName) {
        logger.info("Clicking category with name {}", categoryName);
        categoryWithName(categoryName).click();
        return this;
    }

    public Categories changeCategoryName(String newCategoryName) {
        logger.info("Changing category name to {}", newCategoryName);
        categoryName.sendKeys(newCategoryName);
        return this;
    }

    public boolean noCustomCategoriesAvailable() {
        String errorMsg = "Found a custom category, was looking for text %s. User should not have any custom categories for this profile";

        logger.info("Checking if user has any custom categories available");
        try {
            checkIfTextDisplayed(CategoriesL10N.Localization.HELP.getValue(), errorMsg);
        } catch (TextNotDisplayedException e) {
            logger.warn("No Custom Categories was not validated", e);
            return false;
        }
        return true;
    }


}
