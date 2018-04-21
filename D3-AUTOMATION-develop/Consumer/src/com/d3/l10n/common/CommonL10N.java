package com.d3.l10n.common;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class CommonL10N {

    public enum Localization {
        OOB_VERIFICATION_SENT("out-of-band.description"),
        CHARACTERS_NOT_ALLOWED("pattern.text-field"),
        ENTER_VALID_URL("pattern.url"),
        ENTER_ONLY_DIGITS("pattern.digits");

        private String dbKey;
        private String value = "";

        Localization(String dbKey) {
            this.dbKey = dbKey;
        }

        public String getValue() {
            if (value.isEmpty()) {
                try {
                    this.value = DatabaseUtils.getL10nValueString(dbKey);
                } catch (SQLException e) {
                    LoggerFactory.getLogger(CommonL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }

    }
}
