package com.d3.l10n.moneymovement;

import com.d3.database.DatabaseHelper;
import com.d3.exceptions.D3DatabaseException;
import com.d3.l10n.L10nCommon;
import com.d3.l10n.transactions.TransactionsL10N;
import lombok.extern.slf4j.Slf4j;

/**
 * Holds the L10N values for Recipients
 */
@Slf4j
public class RecipientsL10N extends L10nCommon {

    /**
     * Holds the L10N values for Recipients
     */
    public enum Localization {
        
        OPTION_ON_US_TEXT("recipient.option.on_us.text",false),
        FEDWIRE_OPTION_TEXT("recipient.option.fedwire.text",false),
        RAIL_ON_US("recipient.rail.on-us",false),
        PRE_NOTE_TEXT("recipient.option.pre.note.text",true),
        DELETE("recipient.delete.text",false),
        WIRE("recipient.rail.fedwire",false);


        private String dbKey;
        private String value = "";
        private boolean hasParameters;

        Localization(String dbKey, boolean hasParameters) {
            this.dbKey = dbKey;
            this.hasParameters = hasParameters;
        }

        public String getValue() {
            if (value.isEmpty()) {
                try {
                    this.value = DatabaseHelper.getL10nValueString(dbKey);
                } catch (D3DatabaseException e) {
                    log.error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return hasParameters ? replaceParameters(value) : value;
        }
    }
}
