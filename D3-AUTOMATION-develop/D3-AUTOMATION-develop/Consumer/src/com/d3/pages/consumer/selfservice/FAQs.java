package com.d3.pages.consumer.selfservice;

import com.d3.exceptions.TextNotDisplayedException;
import com.d3.pages.consumer.BaseConsumerPage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public class FAQs extends BaseConsumerPage {

    public enum FrequentlyAskedQuestions {
        GENERAL_QUESTIONS("General Questions", "I get a System Unavailable dialog, what should I do?"),
        DASHBOARD("Dashboard", "What is Net Worth?"),
        ACCOUNTS("Accounts", "Why is an account on the Accounts page grayed out?"),
        TRANSACTIONS("Transactions", "Can I remove a category from a split transaction?"),
        BUDGET("Budget", "Can I modify a budgeted amount for a category?"),
        FINANCIAL_GOALS("Financial Goals", "How does my financial goal impact my budget?");

        protected String header;
        protected String question;

        FrequentlyAskedQuestions(String header, String question) {
            this.header = header;
            this.question = question;
        }

        public String getHeader() {
            return header;
        }
        public String getQuestion() {
            return question;
        }
    }

    public FAQs(WebDriver driver) {
        super(driver);
    }

    @Override
    protected FAQs me() {
        return this;
    }

    public boolean isCorrectInformationDisplayed() {
        String errorMsg = "FAQ information %s: %s was not found on the DOM";
        for (FrequentlyAskedQuestions section : FrequentlyAskedQuestions.values()) {

            try {
                checkIfTextDisplayed(section.getHeader(), errorMsg, "Header");
                checkIfTextDisplayed(section.getQuestion(), errorMsg, "Question");
            } catch (TextNotDisplayedException e) {
                log.warn("FAQ page not validated:", e);
                return false;
            }

        }
        return true;
    }
}
