package com.d3.database;

import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.account.enums.AccountProductAttributes;
import com.d3.datawrappers.company.CompanyAttribute;
import com.d3.exceptions.D3DatabaseException;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.testng.Assert;

import java.sql.SQLException;
import javax.annotation.CheckForNull;

/**
 * Helper class that does actions and queries against the database related to Company and Account Product Attributes
 */
@Slf4j
public class AttributeDatabaseHelper {

    private AttributeDatabaseHelper() {
    }

    /**
     * Updates the value string for a company attribute at the system level based on the company attribute given and bool value
     *
     * @param attribute CompanyAttribute to update
     * @param value bool value to set as the value_string for a given company attribute, gets converted to a string "true", or "false"
     */
    @Step("Update the attribute {attribute.definition} to {value}")
    public static void updateCompanyAttributeValueString(CompanyAttribute attribute, boolean value) {
        updateCompanyAttributeValueString(attribute.getDefinition(), String.valueOf(value));
    }

    /**
     * Updates the value string for a company attribute at the system level based on the company attribute definition given and String value
     *
     * @param definition String value of the company attribute definition
     * @param value String value to set as the value_string for a given company attribute
     */
    @Step("Update the attribute {definition} to {value}")
    private static void updateCompanyAttributeValueString(String definition, String value) {
        try (DatabaseUtils utils = new DatabaseUtils()) {
            @Language("SQL") String query = "UPDATE company_attribute SET value_string = ? WHERE DEFINITION  = ? "
                + "AND company_id = (SELECT id FROM company WHERE bank_structure = 'ROOT')";
            utils.executeUpdateQuery(query, value, definition);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error updating company attribute {} to {}", definition, value, e);
        }
    }

    /**
     * Updates the value string for a company attribute at the system level based on the company attribute given and String value
     *
     * @param attribute The Company attribute to update
     * @param value String value to set as the value_string for a given company attribute
     */
    @Step("Update the attribute {attribute.definition} to {value}")
    public static void updateCompanyAttributeValueString(CompanyAttribute attribute, String value) {
        updateCompanyAttributeValueString(attribute.getDefinition(), value);
    }


    /**
     * Updates the value string for an account product attribute at the system level based on the Product Type, Account Product Attribute, and String value given
     *
     * @param productType Account Product Type to update
     * @param attribute Account Product Attribute to update
     * @param value Value to set for Account Product Attribute
     */
    @Step("Update the account product attribute {attribute} to {value} for product type {productType}")
    public static void updateAccountProductAttribute(ProductType productType, AccountProductAttributes attribute, String value) {
        updateAccountProductAttribute(productType.getAccountProductId(), attribute.getAttributeName(), value);
    }

    /**
     * Updates the value string for an account product attribute at the system level based on the Product Type, Account Product Attribute, and boolean value given
     *
     * @param productType Account Product Type to update
     * @param attribute Account Product Attribute to update
     * @param value Value to set for Account Product Attribute
     */
    @Step("Update the account product attribute {attribute} to {value} for product type {productType}")
    public static void updateAccountProductAttribute(ProductType productType, AccountProductAttributes attribute, boolean value) {
        updateAccountProductAttribute(productType.getAccountProductId(), attribute.getAttributeName(), String.valueOf(value));
    }

    /**
     * Enables or disables an Account Product permission or give a new value_string
     *
     * @param accountProductId Id of an Account product type {@link com.d3.datawrappers.account.ProductType}
     * @param attribute Name column value from the account_product_attribute table ex: d3Permission.allow.check.orders
     * @param value to set as the value_string for a given account product attribute
     */
    @Step("Update Account Product Attribute {attribute} to {value} for account product id: {accountProductId}")
    private static void updateAccountProductAttribute(Integer accountProductId, String attribute, String value) {
        @Language("SQL") String query = "UPDATE apa SET apa.value_string = ? FROM account_product_attribute AS apa "
            + "WHERE apa.account_product_id = ? AND apa.name = ?";
        try (DatabaseUtils utils = new DatabaseUtils()) {
            utils.executeUpdateQuery(query, value, accountProductId, attribute);
        } catch (D3DatabaseException | SQLException e) {
            log.error("Error updating account product attribute {} to {}", attribute, value, e);
            Assert.fail("Error updating account product permissions");

        }
    }

    /**
     * Returns the value string for a company attribute at the system level based on the company attribute definition given
     *
     * @param definition String value of the company attribute definition
     * @return String value of the value_string column, null if error or data is sent back null
     */
    @CheckForNull
    @Step("Get a company attribute value: {definition}")
    public static String selectCompanyAttributeValueString(String definition) {
        @Language("SQL") String query = "SELECT value_string FROM company_attribute WHERE definition  = ? "
            + "AND company_id = (SELECT id FROM company WHERE bank_structure = 'ROOT')";
        try (DatabaseUtils utils = new DatabaseUtils()) {
            return utils.getDataFromSelectQuery("value_string", query, definition);
        } catch (SQLException | D3DatabaseException e) {
            log.error("Error  getting company attribute  {}", definition, e);
            return null;
        }
    }
}
