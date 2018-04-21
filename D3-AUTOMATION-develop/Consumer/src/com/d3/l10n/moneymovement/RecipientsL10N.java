package com.d3.l10n.moneymovement;

import com.d3.database.DatabaseUtils;
import com.d3.l10n.transactions.TransactionsL10N;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class RecipientsL10N {
    public enum Localization {
        
        OPTION_ON_US_TEXT("recipient.option.on_us.text",false),
        FEDWIRE_OPTION_TEXT("recipient.option.fedwire.text",false),
        RAIL_ON_US("recipient.rail.on-us",false),
        PRE_NOTE_TEXT("recipient.option.pre.note.text",true),
        DELETE("recipient.delete.text",false);


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
                    this.value = DatabaseUtils.getL10nValueString(dbKey);
                } catch (SQLException e) {
                    LoggerFactory.getLogger(TransactionsL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return hasParameters ? replaceParameters(value) : value;
        }
    }
        
        
        /**
         * Replaces any text between underscores %s. If text between underscores contains amount it replaces it with $%s ex: Pre-note activation will be initiated. Activation may take up to __days__ days. ->
         * returns "Pre-note activation will be initiated. Activation may take up to %s days."
         * @return String local_value from l10n_text_resource table.
         */
        private static String replaceParameters(String value) {
            return value.replaceAll("__([a-zA-Z]+)__", "%s");
        }
}
