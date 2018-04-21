package com.d3.pages.asserts.dashboard;

import com.d3.database.AccountDatabaseHelper;
import com.d3.datawrappers.user.D3User;
import com.d3.helpers.AccountHelper;
import com.d3.pages.consumer.AssertionsBase;
import com.d3.pages.consumer.dashboard.DashboardPlan;
import io.qameta.allure.Step;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public class DashboardPlanAssert extends AssertionsBase<DashboardPlanAssert, DashboardPlan> {
    private static final String NON_DIGITS = "[^0-9]";
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.##");
    private static final String URL = "#dashboard/plan";

    public DashboardPlanAssert(DashboardPlan actual) {
        super(actual, DashboardPlanAssert.class);
    }

    @Step("Check if the net worth calculations are correct")
    public DashboardPlanAssert hasCorrectNetWorthCalculations(D3User user) {
        long totalAssets = Math.round(Double.parseDouble((
            Optional.ofNullable(AccountDatabaseHelper.getSumOfUsersAssetAccounts(user)).filter(sum -> !sum.isEmpty())
                .orElse("0.00"))));

        long totalLiabilities = Math.round(Double.parseDouble(
            Optional.ofNullable(AccountDatabaseHelper.getSumOfUsersLiabilityAccounts(user)).filter(sum -> !sum.isEmpty())
                .orElse("0.00")));

        String totalAssetsFromDatabase = StringUtils.substringBefore(String.valueOf(totalAssets), ".").trim();
        String totalLiabilitiesFromDatabase = StringUtils.substringBefore(String.valueOf(totalLiabilities), ".").trim();
        String netWorthFromDatabase = String.valueOf(DECIMAL_FORMAT.format(totalAssets - totalLiabilities)).replaceAll(NON_DIGITS, "").trim();

        String totalAssetsDisplayedInWidget = actual.getNetWorthAssets().replaceAll(NON_DIGITS, "").trim();
        String totalLiabilitiesDisplayedInWidget = actual.getNetWorthLiabilities().replaceAll(NON_DIGITS, "").trim();
        String netWorthDisplayedInWidget = actual.getNetWorthTotal().replaceAll(NON_DIGITS, "").trim();

        if (!totalAssetsDisplayedInWidget.equals(totalAssetsFromDatabase)) {
            failWithMessage("Comparing currency failed. Total Assets displayed in net worth widget <%s> did not match the expected total assets amount <%s>", totalAssetsDisplayedInWidget, totalAssetsFromDatabase);
        }

        if (!totalLiabilitiesDisplayedInWidget.equals(totalLiabilitiesFromDatabase)) {
            failWithMessage("Comparing currency failed. Total Liabilities displayed in net worth widget <%s> did not match the expected total liabilities amount <%s>", totalLiabilitiesDisplayedInWidget, totalLiabilitiesFromDatabase);
        }

        if (!netWorthDisplayedInWidget.equals(netWorthFromDatabase)) {
            failWithMessage("Comparing currency failed. Net Worth Total displayed in net worth widget <%s> did not match the expected net worth total amount <%s>", netWorthDisplayedInWidget, netWorthFromDatabase);
        }
        return this;
    }

    @Step("Check if the net worth calculations have updated")
    public DashboardPlanAssert netWorthTotalsHaveChanged(List<BigDecimal> oldNetWorthInfo, List<BigDecimal> newNetWorthInfo) {

        if (!AccountHelper.haveTotalsChanged(oldNetWorthInfo, newNetWorthInfo)) {
            failWithMessage("Net Worth Totals have not changed. Original Net Worth Totals <%s> did not differ from the new Net Worth Totals <%s>", oldNetWorthInfo.toString(), newNetWorthInfo.toString());
        }
        return this;
    }

    @Override
    public DashboardPlanAssert atPage() {
        return checkPageUrl(URL);
    }
}
