package com.d3.pages.consumer.transactions;

import com.d3.database.TransactionDatabaseHelper;
import com.d3.datawrappers.account.D3Account;
import com.d3.helpers.RandomHelper;
import com.d3.support.D3Element;
import com.d3.support.PageObjectBase;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.CheckBox;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class SingleTransactionPage extends TransactionsBasePage {
    @FindBy(css = "button.txn-image-toggle")
    private Element viewImageButton;
    @FindBy(css = "img.img-responsive")
    private List<Element> checkImages;
    @FindBy(css = "li.entity.transaction.active input[name='description']")
    private Element descriptionInput;
    @FindBy(css = "button.cancel")
    private Element ignoreButton;
    @FindBy(css = "button.submit-one")
    private Element viewSimilarButton;
    @FindBy(css = "input.select-all")
    private CheckBox selectAllCheckBox;
    @FindBy(css = "button.saveSimilar")
    private Element saveSelectedButton;
    @FindBy(css = "input.transaction-category")
    private Element transactionCategory;
    @FindBy(css = "input.select-item")
    private CheckBox selectSimilarTransactions;
    @FindBy(css = "button.btn-create-split")
    private Element splitButton;
    @FindBy(css = "div.split-memo input")
    private List<Element> splitMemoInput;
    @FindBy(css = "input[name='amount']")
    private List<Element> splitAmountInput;
    @FindBy(css = "button.btn-save-split")
    private Element saveSplitButton;
    @FindBy(css = "button.btn-add-split")
    private Element addSplitButton;
    @FindBy(css = "span.value.cursor-pointer")
    private Element categoryListSplit;
    @FindBy(css = "li.select-item")
    private List<Element> categoryElementsSplit;
    @FindBy(css = "button.add-category")
    private Element addCategoryButton;
    @FindBy(css = "input#categoryExpense + label")
    private Element expenseCategoryButton;
    @FindBy(css = "input#categoryIncome + label")
    private Element incomeCategoryButton;
    @FindBy(name = "name")
    private Element categoryName;
    @FindBy(css = "input.parent-category-cb")
    private CheckBox subcategoryCheckbox;
    @FindBy(css = "button.create-category")
    private List<Element> addCategorySubmitButton;
    @FindBy(css = "span.dataLabel")
    private Element parentCategoryDropdown;
    @FindBy(css = "button.transactionDelete")
    private Element deleteTransactionButton;
    @FindBy(css = "button.submit-one")
    private Element confirmDeleteTransactionButton;
    @FindBy(css = "button.btn-change-split")
    private Element changeSplitButton;
    @FindBy(name = "memo")
    private Element postedTransactionMemo;
    @FindBy(css = "button.multiselect.dropdown-toggle")
    private Element accountsDropdown;
    @FindBy(id = "transaction-category-name")
    private Element selectCategoryInput;
    @FindBy(css = "button.saveTransaction")
    private Element saveTransaction;

    /**
     * Inits the driver and header, don't use the constructor manually, use {@link PageObjectBase#initialize(WebDriver, Class)}
     *
     * @param driver WebDriver object
     */
    public SingleTransactionPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected PageObjectBase me() {
        return this;
    }

    private D3Element subcategoryParent(String parentCategory) {
        By by = By.xpath(String.format("//a[@class='dropDownItemSelect'][.='%s']", parentCategory));
        return new D3Element(driver.findElement(by));
    }

    private D3Element categoryName(String categoryName) {
        By by = By.xpath(String.format("//span[@class='category-name'][.='%s']", categoryName));
        return new D3Element(driver.findElement(by));
    }

    private D3Element categoryExpandIcon(String parentCategory) {
        By by = By.cssSelector(String.format("span[aria-label='%s'] i.glyphicon-plus", parentCategory));
        return new D3Element(driver.findElement(by));
    }

    @Step("Click the view image button")
    public SingleTransactionPage clickViewImageButton() {
        viewImageButton.click();
        return this;
    }

    @Step("Check if the check image is shown")
    public boolean isCheckImageShown() {
        return checkImages.get(0).isDisplayed();
    }

    @Step("Change the active transaction name to {newName}")
    public SingleTransactionPage changeActiveTransactionName(String newName) {
        descriptionInput.click();
        descriptionInput.sendKeys(newName);
        waitForSpinner();
        return this;
    }

    @Step("Click the ignore button")
    public SingleTransactionPage clickIgnoreButton() {
        ignoreButton.click();
        return this;
    }

    @Step("Click the view similar button")
    public SingleTransactionPage clickViewSimilar() {
        viewSimilarButton.click();
        return this;
    }

    @Step("Select all the transactions to be renamed")
    public SingleTransactionPage selectAllTransactionsToBeRenamed() {
        selectAllCheckBox.check();
        return this;
    }

    @Step("Click teh save selected button")
    public TransactionsPage clickSaveSelectedButton() {
        saveSelectedButton.click();
        return TransactionsPage.initialize(driver, TransactionsPage.class);
    }

    @Step("Click the category name")
    public SingleTransactionPage clickCategoryName() {
        transactionCategory.click();
        return this;
    }

    @Step("Click the add category button")
    public SingleTransactionPage clickAddCategoryButton() {
        addCategoryButton.click();
        return this;
    }

    @Step("Click the expense category button")
    public SingleTransactionPage clickExpenseCategoryButton() {
        expenseCategoryButton.click();
        return this;
    }

    @Step("Click the income category button")
    public SingleTransactionPage clickIncomeCategoryButton() {
        incomeCategoryButton.click();
        return this;
    }

    @Step("Toggle the subcategory checkbox")
    public SingleTransactionPage clickSubcategoryCheckbox() {
        subcategoryCheckbox.check();
        return this;
    }

    @Step("Select {category} as the parent category")
    public SingleTransactionPage selectParentCategory(String category) {
        parentCategoryDropdown.click();
        subcategoryParent(category).click();
        return this;
    }

    @Step("Enter {name} as the category name")
    public SingleTransactionPage enterCategoryName(String name) {
        log.info("Entering category name {}", name);
        categoryName.sendKeys(name);
        return this;
    }

    @Step("Click the add category submit button")
    public SingleTransactionPage clickAddCategorySubmitButton() {
        getDisplayedElement(addCategorySubmitButton).click();
        return this;
    }

    @Step("Click on the split button")
    public SingleTransactionPage clickOnSplitButton() {
        splitButton.click();
        return this;
    }

    @Step("Enter {memoText} as the memo text")
    public SingleTransactionPage enterSplitMemoText(String memoText) {
        getLastListValue(splitMemoInput).sendKeys(memoText);
        return this;
    }

    @Step("Enter {amount} as the amount")
    public SingleTransactionPage enterAmount(String amount) {
        getLastListValue(splitAmountInput).sendKeys(amount);
        return this;
    }

    @Step("Click the add split button")
    public SingleTransactionPage clickAddSplitButton() {
        addSplitButton.click();
        return this;
    }

    @Step("Get the transaction category")
    public String getTransactionCategory() {
        return transactionCategory.getValueAttribute();
    }

    @Step("Select similar transactions")
    public SingleTransactionPage selectSimilarTransactions() {
        selectSimilarTransactions.click();
        return this;
    }

    @Step("Click on the save split button")
    public SingleTransactionPage clickOnSaveSplit() {
        saveSplitButton.click();
        return this;
    }

    @Step("Get the split value")
    public String getSplitValue() {
        return getLastListValue(splitAmountInput).getValueAttribute();
    }

    @Step("Click on the delete transaction button")
    public SingleTransactionPage clickOnDeleteTransactionButton() {
        deleteTransactionButton.click();
        return this;
    }

    @Step("Click on the confirm delete transaction button")
    public SingleTransactionPage clickOnConfirmDeleteTransactionButton() {
        confirmDeleteTransactionButton.click();
        return this;
    }

    @Step("Select a random category item")
    public SingleTransactionPage selectRandomCategoryItem() {
        categoryListSplit.click();
        // Will use first 7 elements, because some of the other are hidden (sub categories) and will make this to fail
        int randomCategory = RandomHelper.getRandomNumberInt(0, 6);
        categoryElementsSplit.get(randomCategory).click();
        return this;
    }

    @Step("Click on the change split button")
    public SingleTransactionPage clickOnChangeSplit() {
        changeSplitButton.click();
        return this;
    }

    @Step("Change the memo to {memo}")
    public SingleTransactionPage changeMemoTransaction(String memo) {
        // NOTE (JMarshall): Was getting StaleElementReferenceException when sending keys the first time
        try {
            postedTransactionMemo.sendKeys(memo);
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            postedTransactionMemo.sendKeys(memo);
        }
        return this;
    }

    public TransactionsPage getTransactionPage() {
        return TransactionsPage.initialize(driver, TransactionsPage.class);
    }

    @Step("Check if the category {category} exists")
    public boolean doesCategoryExist(String category) {
        try {
            return categoryName(category).isDisplayed();
        } catch (NoSuchElementException e) {
            log.warn("Wasn't able to find Category {} on the DOM", category);
            return false;
        }
    }

    @Step("Check if the correct categories are displayed")
    public boolean areCorrectCategoriesDisplayed(D3Account account) {
        String errorMsg = "Category: {} was not available on selected transaction for account: {}";
        String profile = (account.getProductType().toString().startsWith("BUSINESS")) ? "BUSINESS" : "CONSUMER";
        String category = TransactionDatabaseHelper.getRandomCategoryNotSharedAcrossProfileTypes(profile);
        log.info("Checking if category {} is available on selected transaction for the user account {}",
            category,
            account.getName());
        if (!categoryName(category).isDisplayed()) {
            log.warn(errorMsg, category, account.getName());
            return false;
        }
        return true;
    }

    @Step("Click on the {category} category")
    public SingleTransactionPage categorizeTransaction(String category) {
        categoryName(category).click();
        return this;
    }

    @Step("Expand the {parent} subcategories")
    public SingleTransactionPage expandSubcategories(String parent) {
        categoryExpandIcon(parent).click();
        return this;
    }

    @Step("Open category options")
    public SingleTransactionPage openCategoryOptions() {
        selectCategoryInput.click();
        return this;
    }

    @Step("Click the save transaction button")
    public SingleTransactionPage clickSaveTransaction() {
        saveTransaction.scrollIntoView().click();
        return this;
    }

}
