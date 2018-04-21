package com.d3.l10n.settings;

import com.d3.database.DatabaseUtils;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class CategoriesL10N {

    public enum Localization {
        HELP("settings.categories.help"); //Jmoravec note: This is the l10n for no categories as well

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
                    LoggerFactory.getLogger(CategoriesL10N.class).error("Error getting the L10N value string for {}: {}", dbKey, e);
                }
            }
            return value;
        }

    }
}
