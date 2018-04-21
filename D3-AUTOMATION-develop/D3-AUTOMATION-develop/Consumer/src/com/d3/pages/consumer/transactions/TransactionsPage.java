package com.d3.pages.consumer.transactions;

import static com.d3.helpers.AccountHelper.verifyAccounts;
import static com.d3.helpers.WebdriverHelper.goToBottomOfPage;

import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.account.D3Transaction;
import com.d3.exceptions.TextNotContainedException;
import com.d3.exceptions.TextNotDisplayedException;
import com.d3.support.D3Element;
import com.d3.support.internal.Element;
import com.d3.support.wrappers.base.CheckBox;
import com.d3.support.wrappers.base.Select;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import javax.annotation.CheckForNull;

@SuppressWarnings("UnusedReturnValue")
@Slf4j
public class TransactionsPage extends TransactionsBasePage {

    @FindBy(id = "showExportView")
    private Element exportButton;
    @FindBy(name = "fileType")
    private Select exportType;
    @FindBy(id = "transactionExportButton")
    private Element downloadButton;
    @FindBy(name = "searchTerm")
    private Element searchInput;
    @FindBy(className = "search-button")
    private Element keywordSearchButton;
    @FindBy(css = "li[class$='entity transaction inline-edit']")
    private List<Element> postedTransactions;
    @FindBy(css = "li.entity.pending")
    private List<Element> pendingTransactions;
    @FindBy(xpath = "//input[starts-with(@value, 'Uncategorized')]")
    private List<Element> uncategorizedTransactions;
    @FindBy(id = "date-range-btn")
    private Element dateRangeButton;
    @FindBy(id = "startDate")
    private Element startDateInput;
    @FindBy(id = "endDate")
    private Element endDateInput;
    @FindBy(css = "button.searchButton")
    private Element searchButtonForDate;
    @FindBy(css = "button.toggle-scroll")
    private Element showMoreButton;
    @FindBy(className = "loading")
    private Element loadingElement;
    @FindBy(id = "toggleUncategorized")
    private Element toggleCategorizedButton;
    @FindBy(css = "button.multiselect.dropdown-toggle")
    private Element accountsDropdown;
    @FindBy(css = "select.account-select option")
    private List<Element> availableAccounts;
    @FindBy(xpath = "//input[@value='multiselect-all']")
    private CheckBox allAccountsCheckbox;
    @FindBy(xpath = "//label[@class='checkbox']")
    private List<CheckBox> accountCheckbox;
    @FindBy(css = "div[class='field qualifier account-name text-nowrap']")
    private List<Element> accountUsedInTransaction;
    @FindBy(xpath = ".//div[contains(@class,'aa-suggestion')][contains(text(),'in description')]")
    private Element descriptionSearchOption;
    @FindBy(xpath = ".//div[contains(@class,'aa-suggestion')][contains(text(),'in all fields')]")
    private Element termSearchOption;
    @FindBy(css = "button.clear-search-date-range")
    private Element clearDatesButton;
    @FindBy(id = "searchCheckNumber")
    private Element checkRangeButton;
    @FindBy(css = "button.search-range-button")
    private Element searchButtonForRange;
    @FindBy(xpath = ".//span[@data-role='remove']")
    private List<Element> removeFiltersList;
    @FindBy(css = ".number.editable")
    private List<Element> checkNumberList;
    @FindBy(css = "div.running-balance > div.amount")
    private List<Element> runningBalanceList;
    @FindBy(css = "button.search-range-button")
    private Element searchRangeButton;
    @FindBy(css = "li.entity.transaction")
    private List<Element> transactions;
    @FindBy(css = "div[class$='help-block error-message']")
    private List<Element> requiredFieldMessageList;

    @FindBy(css = "button.add-transaction")
    private Element addTransactionButton;
    @FindBy(id = "transaction-amount")
    private Element amountInput;
    @FindBy(id = "transaction-name")
    private Element nameInput;
    @FindBy(id = "transaction-memo")
    private Element memoInput;
    @FindBy(name = "checkNum")
    private Element checkInput;


    @FindBy(css = "div.amount.credit,div.amount.debit")
    private List<Element> amountList;
    @FindBy(id = "startCheckNumber")
    private Element startCheckInput;
    @FindBy(id = "endCheckNumber")
    private Element endCheckInput;
    @FindBy(css = "button.clear-search-check-range")
    private Element clearCheckRangeButton;
    @FindBy(css = "li[class$='entity transaction inline-edit'] div.running-balance div.amount")
    private List<Element> runningBalancePostedTransaction;
    @FindBy(xpath = "//li[contains(@class,'pending')]//div[@class='running-balance']/div[contains(@class,'amount')]")
    private List<Element> runningBalancePendingTransaction;

    public TransactionsPage(WebDriver driver) {
        super(driver);
    }

    private DateTime returnDate(String date1) {
        DateTimeFormatter format = DateTimeFormat.forPattern("MM-dd-yyyy");
        return format.parseDateTime(date1);
    }

    private String getDateTransaction(String name) {
        By by = By.xpath(String.format(".//p[contains(text(),'%s')]", name));
        return StringUtils.substringAfter(driver.findElement(by).getText(), " on ").replace("/", "-");
    }

    @CheckForNull
    private D3Element transactionNameElementBySpan(String name) {
        By bySpan = By.xpath(String.format("//span[.='%s']", name));
        return transactionNameElementBy(bySpan);
    }

    @CheckForNull
    private D3Element transactionNameElementByInput(String name) {
        By byInput = By.xpath(String.format("//input[@value='%s']", name));
        return transactionNameElementBy(byInput);
    }

    @CheckForNull
    private D3Element transactionNameElementByDiv(String name) {
        By byDiv = By.xpath(String.format("//div[text()='%s']", name));
        return transactionNameElementBy(byDiv);
    }

    @CheckForNull
    private D3Element transactionNameElementBy(By by) {
        log.info("Attempting to find transaction by: {}", by);
        try {
            return new D3Element(driver.findElement(by));
        } catch (NoSuchElementException ignored) {
            return null;
        }
    }

    private D3Element transactionNameElement(String name) {
        D3Element returnElement;
        returnElement = transactionNameElementBySpan(name);
        if (returnElement == null) {
            returnElement = transactionNameElementByInput(name);
        }
        if (returnElement == null) {
            returnElement = transactionNameElementByDiv(name);
        }
        if (returnElement == null) {
            throw new NotFoundException("Transaction element was not found");
        }

        return returnElement;
    }

    @Override
    protected TransactionsPage me() {
        return this;
    }

    @Step("Download the transcations via {type}")
    public TransactionsPage downloadTransactions(DownloadType type) {
        exportButton.click();
        exportType.selectByValue(type.toString());
        downloadButton.click();
        return this;
    }

    private TransactionsPage searchForSomething(String searchTerm, Element whichTypeToSearchFor) {
        // NOTE (JMoravec): Don't add tab to make sure the suggestion dropdown appears
        searchInput.sendKeys(true, true, false, searchTerm);
        whichTypeToSearchFor.click();
        keywordSearchButton.click();
        return this;
    }

    @Step("Search for {search}")
    public TransactionsPage searchForTerm(String search) {
        return searchForSomething(search, termSearchOption);
    }

    @Step("Search for {search} in the description")
    public TransactionsPage searchForDescription(String search) {
        return searchForSomething(search, descriptionSearchOption);
    }

    @Step("Get the number of transactions shown")
    public int getNumberOfTransactionsShown() {
        return postedTransactions.size() + pendingTransactions.size();
    }

    @Step("Click the date range button")
    public TransactionsPage clickDateRangeButton() {
        dateRangeButton.click();
        return this;
    }

    @Step("Click the check range button")
    public TransactionsPage clickCheckRangeButton() {
        checkRangeButton.click();
        return this;
    }

    @Step("Enter {date} for the start date")
    public TransactionsPage enterStartDate(String date) {
        startDateInput.sendKeys(date);
        return this;
    }

    @Step("Enter {date} for the end date")
    public TransactionsPage enterEndDate(String date) {
        endDateInput.sendKeys(date);
        return this;
    }

    @Step("Click the date search button")
    public TransactionsPage clickDateSearchButton() {
        searchButtonForDate.click();
        return this;
    }

    @Step("Check if the transactions are in the range of {startDate} to {endDate} with name {name}")
    public boolean transactionsHaveDatesInRangeDate(String startDate, String endDate, String name) {
        boolean value = false;
        for (Element element : transactions) {
            element.click();
            String date = getDateTransaction(name);
            if (returnDate(date).compareTo(returnDate(startDate)) == 0 || returnDate(date).compareTo(returnDate(endDate)) == 0) {
                value = true;
            } else if (returnDate(date).isAfter(returnDate(startDate)) && returnDate(date).isBefore(returnDate(endDate))) {
                value = true;
            } else {
                value = false;
                break;
            }
        }
        return value;
    }


    @Step("Check if {name} is shown on the transaction")
    public boolean isTransactionNameShown(String name) {
        return transactionNameElement(name).isDisplayed();
    }

    @Step("Check if transaction {transaction} is shown")
    public boolean isTransactionShown(D3Transaction transaction) {
        return isTransactionNameShown(transaction.getName());
    }

    @Step("Check if all transactions with {name} are displayed")
    public boolean allTransactionsWithWordInNameDisplayed(String name) {
        By by = By.xpath(String.format("//div[contains(@class,'description')][contains(text(),'%s')]", name));
        By byInput = By.xpath(String.format("//input[@value='%s']", name));
        List<WebElement> elements;
        log.info(" Attempting to find transaction named: {} via By: {}", name, by);
        elements = driver.findElements(by);
        if (elements.isEmpty()) {
            log.info("Failed to find name via div tag, attempting to find it via the input tag: {}", byInput);
            elements = driver.findElements(byInput);
        }
        return transactions.size() == elements.size();
    }

    @Step("Click the show more button")
    public TransactionsPage clickShowMoreButton() {
        goToBottomOfPage(driver);
        showMoreButton.click();
        return this;
    }

    @Step("Scroll until there are no more transactions")
    public TransactionsPage scrollUntilNoMoreTransactions() {
        do {
            ((JavascriptExecutor) driver).executeScript("scrollTo(0,document.body.scrollHeight);");
        } while (loadingElement.isDisplayed());

        return this;
    }

    /**
     * Verify the correct running balance for each transactions by adding the last transaction running balance value and adding (credit) or
     * subtracting (debit) to get next transaction up running balance value until added up to the top pending transaction. Example: 103,746.61 - 200 =
     * 103,546.61 103,546.61 + 1.00 = 103, 547.61 103,547-61 - 11.04 = 103,536.57
     *
     * @return True if the value of previous balance adding or subtracting previous amount is equal to running balance of next transaction, otherwise
     * return False
     */
    @Step("Check if the running balance is correct")
    public boolean isRunningBalanceCorrect() {
        boolean value = false;
        for (int i = runningBalanceList.size() - 1; i > 0; i--) {

            String amountText = amountList.get(i - 1).getText();
            if (amountList.get(i - 1).getAttribute("class").equals("amount debit")) {
                amountText = "-" + amountText;
            }
            Double amount = Double.parseDouble(amountText.replaceAll("[,$]", ""));
            Double previousBalance = Double.parseDouble(runningBalanceList.get(i - 1).getText().replaceAll("[,$]", ""));
            Double balance = Double.parseDouble(runningBalanceList.get(i).getText().replaceAll("[,$]", ""));
            Double result = Math.round((balance + amount) * 100d) / 100d;

            if (Double.compare(previousBalance, result) == 0) {
                value = true;
            } else {
                value = false;
                log.warn("The balance is not correct ( {} - {}) is not equal to {}", balance, amount, previousBalance);
                break;
            }
        }
        return value;
    }

    @Step("Click the show uncategorized button")
    public TransactionsPage clickShowUncategorizedButton() {
        toggleCategorizedButton.click();
        return this;
    }

    @Step("Check if only uncategorized transactions are shown")
    public boolean areOnlyUncategorizedTransactionsShown() {
        return postedTransactions.size() == uncategorizedTransactions.size();
    }

    @Step("Click the first posted transaction")
    public SingleTransactionPage clickFirstPostedTransaction() {
        postedTransactions.get(0).click();
        return SingleTransactionPage.initialize(driver, SingleTransactionPage.class);
    }


    @Step("Filter the transactions by accounts")
    public TransactionsPage filterByAccounts(List<D3Account> accounts) {
        if (!accounts.isEmpty()) {
            accountsDropdown.click();
            allAccountsCheckbox.uncheck();
        }

        for (D3Account account : accounts) {
            accountCheckbox.stream().filter(availableAccount ->
                availableAccount.getText().toUpperCase().contains(account.getName().toUpperCase())).forEach(CheckBox::check);
        }
        accountsDropdown.click();
        return this;
    }

    @Step("Click on the clear dates button")
    public TransactionsPage clickOnClearDatesButton() {
        clearDatesButton.click();
        return this;
    }

    @Step("Click on the search for check range button")
    public TransactionsPage clickOnSearchForCheckRangeButton() {
        searchButtonForRange.click();
        return this;
    }

    @Step("Remove the filters")
    public TransactionsPage removeFilters() {
        removeFiltersList.forEach(Element::click);
        return this;
    }

    @Step("Check if the available accounts correct")
    public boolean areAvailableAccountsCorrect(List<String> userAccounts) {
        return verifyAccounts(userAccounts, availableAccounts, element -> element.getAttribute("label"), false);

    }

    @Step("Check if the correct ransactions are displayed")
    public boolean areCorrectTransactionsDisplayed(List<String> userAccounts) {
        return verifyAccounts(userAccounts, accountUsedInTransaction, Element::getText, false);
    }


    @Step("Check if the dates are removed")
    public boolean areRemovedDates() {
        return startDateInput.getValueAttribute().isEmpty() && endDateInput.getValueAttribute().isEmpty();
    }

    @Step("Check if the correct transactions are displayed with check numbers {startCheckNumber}-{endCheckNumber}")
    public boolean areCorrectTransactionsDisplayedByCheckNumber(String startCheckNumber, String endCheckNumber) {
        boolean value = false;
        for (Element element : checkNumberList) {
            int checkNumber = Integer.parseInt(element.getText().substring(5).trim());
            if (Integer.valueOf(startCheckNumber) <= checkNumber && Integer.valueOf(endCheckNumber) >= checkNumber) {
                value = true;
            } else {
                value = false;
                log.error("Check range start: {}, end: {}, check number: {}", startCheckNumber, endCheckNumber, checkNumber);
                break;
            }
        }

        return value;
    }


    @Step("Click on the add transaction button")
    public TransactionsPage clickAddTransaction() {
        addTransactionButton.click();
        return this;
    }

    @Step("Fill out the transaction form")
    public SingleTransactionPage fillAddTransactionForm(D3Transaction transaction) {
        enterTransactionAmount(transaction.getAmountString());
        enterTransactionName(transaction.getName());
        enterTransactionMemo(transaction.getMemo());
        enterCheckNumber(transaction.getCheckNumber());
        return SingleTransactionPage.initialize(driver, SingleTransactionPage.class);
    }

    @Step("Enter {transactionName} as the transaction name")
    public TransactionsPage enterTransactionName(String transactionName) {
        nameInput.sendKeys(transactionName);
        return this;
    }

    @Step("Enter {amount} as the amount")
    public TransactionsPage enterTransactionAmount(String amount) {
        amountInput.sendKeys(amount);
        return this;
    }

    @Step("Enter {memo} as the memo")
    public TransactionsPage enterTransactionMemo(String memo) {
        memoInput.sendKeys(memo);
        return this;
    }

    @Step("Enter {checkNumber} as the check number")
    public TransactionsPage enterCheckNumber(String checkNumber) {
        checkInput.sendKeys(checkNumber);
        return this;
    }



    /**
     * Checks that required fields 'Start Check' and 'End Check' display the correct error message when the two have incorrect values Returns a
     * boolean value
     *
     * @param message a string with the expect error message
     */
    @Step("Check if {message} message displays")
    public boolean areRequiredFieldMessagesDisplayed(String message) {
        String numberOfReqFields = "There should be 2 required field messages, but found {}";
        log.info("Verifying 2 error messages are displayed for message: {}", message);
        if (requiredFieldMessageList.size() != 2) {
            log.warn(numberOfReqFields, requiredFieldMessageList.size());
            return false;
        }

        for (Element errorMessage : requiredFieldMessageList) {
            try {
                checkIfTextEquals(errorMessage.getText(), message);
            } catch (TextNotDisplayedException e) {
                log.warn("Error matching error messages on the form: ", e);
                return false;
            }
        }
        log.info("Error messages all appeared correctly for message: {}", message);
        return true;
    }

    @Step("Check if the date is different from {date}")
    public boolean areDatesChanged(String date) {
        return startDateInput.getValueAttribute().equals(date);
    }

    @Step("Click teh search range button")
    public TransactionsPage clickSearchRangeButton() {
        searchRangeButton.click();
        return this;
    }




    @Step("Click on the {transactionName} transaction")
    public SingleTransactionPage clickOnTransactionByName(String transactionName) {
        transactionNameElement(transactionName).click();
        return SingleTransactionPage.initialize(driver, SingleTransactionPage.class);
    }

    /**
     * Checks if the given D3Account is available in the dropdown filter
     *
     * @param account D3Account to check for in the Transaction Page account filter
     * @return true if account name is found in list of accounts, false otherwise
     */
    public boolean isAccountAvailableInDropdown(D3Account account) {
        String errMsg = String.format("Account: {%s} not available in dropdown", account.getName());
        try {
            checkIfTextContains(accountsDropdown.getAttribute("title"), errMsg, account.getName());
        } catch (TextNotContainedException e) {
            log.warn(errMsg);
            return false;
        }
        return true;
    }

    @Step("Enter {startCheck} for the start check field")
    public TransactionsPage enterStartCheck(String startCheck) {
        startCheckInput.sendKeys(startCheck);
        return this;
    }

    @Step("Enter {endCheck} for the end check field")
    public TransactionsPage enterEndCheck(String endCheck) {
        endCheckInput.sendKeys(endCheck);
        return this;
    }

    @Step("Click the clear range button")
    public TransactionsPage clickClearRangeButton() {
        clearCheckRangeButton.click();
        return this;
    }

    @Step("Check if the range inputs are all hidden")
    public boolean areRangeInputsAllHidden() {
        try {
            return !(startCheckInput.isDisplayed() || endCheckInput.isDisplayed() || clearCheckRangeButton.isDisplayed());
        } catch (NoSuchElementException e) {
            log.info("Check range elements were not displayed", e);
            return true;
        }
    }

    /**
     * Verify if the balance of the first posted transaction is equal to the Dashboard balance and the dropdown balance.
     *
     * @param balance a String "balance" with the Dashboard balance.
     * @return a boolean value, true if the balance is equal to the dashboard and dropdown balance, otherwise return false.
     */
    @Step("Check if the running balance ({balance}) is correct for the first posted transaction")
    public boolean runningBalanceIsCorrectForTheFirstPostedTransaction(String balance) {
        String balanceDropDown = StringUtils.substringAfterLast(accountsDropdown.getText(), ") ");
        String amount = runningBalancePostedTransaction.get(0).getText();
        return (amount.equals(balance) && amount.equals(balanceDropDown));
    }

    @Step("Check if the running balance for pending transaction is empty")
    public boolean isRunningBalancePendingTransactionEmpty() {
        return runningBalancePendingTransaction.isEmpty();
    }

    public enum DownloadType {
        PDF,
        CSV,
        OFX,
        QBO,
        QFX
    }
}




