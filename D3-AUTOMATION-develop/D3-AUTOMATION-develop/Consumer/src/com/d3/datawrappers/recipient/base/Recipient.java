package com.d3.datawrappers.recipient.base;

import com.d3.api.helpers.banking.MoneyMovementApiHelper;
import com.d3.database.AuditDatabaseHelper;
import com.d3.database.RecipientMMDatabaseHelper;
import com.d3.datawrappers.account.TransferableAccount;
import com.d3.datawrappers.recipient.CompanyBillPayManualRecipient;
import com.d3.datawrappers.recipient.CompanyBillPaySeedRecipient;
import com.d3.datawrappers.recipient.PersonBillAddressRecipient;
import com.d3.datawrappers.user.D3User;
import com.d3.exceptions.D3ApiException;
import com.d3.l10n.moneymovement.RecipientsL10N;
import com.d3.monitoring.audits.AuditAttribute;
import com.d3.monitoring.audits.Audits;
import com.d3.pages.consumer.moneymovement.recipients.add.base.AddRecipientForm;
import com.d3.pages.consumer.moneymovement.recipients.edit.base.EditRecipientsForm;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Slf4j
public abstract class Recipient implements TransferableAccount, Serializable {

    protected RecipientType type;
    protected RecipientWho who;
    protected String name;
    protected String nickname;

    protected String deleteText = RecipientsL10N.Localization.DELETE.getValue();

    public Recipient(String name) {
        this.name = name;
    }

    public static List<Recipient> createAllRecipients() {
        return createAllRecipients(true, false);
    }

    public abstract boolean isEditable();

    public RecipientType getType() {
        return type;
    }

    public RecipientWho getWho() {
        return who;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isFormValid(WebDriver driver) {
        //noinspection unchecked
        return getEditRecipientsForm(driver).isFormInformationCorrect(this);
    }

    public abstract EditRecipientsForm getEditRecipientsForm(WebDriver driver);

    public abstract AddRecipientForm getAddRecipientForm(WebDriver driver);

    public abstract By getEditButtonBy();

    public abstract boolean wasSeeded();

    public abstract Recipient getNewRandomVersion();

    public abstract void addApiRecipient(MoneyMovementApiHelper api) throws D3ApiException;

    protected abstract String getAuditTransferString();

    /**
     * Creates a list of random recipients, one of each type.
     * @param includeSeededBillpay whether to include creating a CompanyBillPaySeedRecipient
     * @param includeExpedited whether to include Person & Company Bill Address Recipients eligible for expedited payments
     * @return List<Recipient> 
     */
    public static List<Recipient> createAllRecipients(boolean includeSeededBillpay, boolean includeExpedited) {
        List<Recipient> recipients = new ArrayList<>();

        recipients.add(PersonBillAddressRecipient.createRandomRecipient(false));
        if (includeSeededBillpay) {
            recipients.add(CompanyBillPaySeedRecipient.createRandomExistingRecipient("Best Buy"));
        }
        if (includeExpedited) {
            recipients.add(PersonBillAddressRecipient.createRandomRecipient(true));
            recipients.add(CompanyBillPayManualRecipient.createRandomRecipient(true));
        }
        recipients.add(CompanyBillPayManualRecipient.createRandomRecipient(false));
        recipients.add(WireRecipient.createRandomPersonRecipient());
        recipients.add(WireRecipient.createRandomCompanyRecipient());
        recipients.add(AccountIOwnRecipient.createRandomAccountRecipient());
        recipients.add(OnUsAccountIOwnRecipient.createRandomOnUsAccountRecipient());
        return recipients;
    }

    @Nullable
    protected Map<String, String> getRecipientCreatedAudit(D3User user) {
        return AuditDatabaseHelper.getAuditRecordAttributes(user, Audits.RECIPIENT_CREATED);
    }

    protected boolean doesAuditHaveCorrectRecipientName(@Nonnull Map<String, String> auditRecord) {
        log.info("Checking if audit record has correct recipient name: {}, {}", auditRecord, getName());
        return auditRecord.get(AuditAttribute.RECIPIENT_NAME.getAttributeName()).equals(getName());
    }

    protected boolean doesAuditHaveCorrectTransferType(@Nonnull Map<String, String> auditRecord) {
        log.info("Checking if audit record has correct transfer String: {}, {}", auditRecord, getAuditTransferString());
        return auditRecord.get(AuditAttribute.TRANSFER_METHOD.getAttributeName()).equals(getAuditTransferString());
    }

    public boolean isAddingAuditRecordCreated(D3User user) {
        Map<String, String> audit = getRecipientCreatedAudit(user);
        if (audit == null || audit.isEmpty()) {
            log.error("Audit record is null");
            return false;
        }

        return doesAuditHaveCorrectRecipientName(audit) && doesAuditHaveCorrectTransferType(audit);
    }

    @Override
    public String getInternalExternalId(D3User user) {
        return String.format("EXTERNAL:%s", RecipientMMDatabaseHelper.getExternalEndpointId(user, this.getName()));
    }

    public String toString() {
        return String.format("Recipient name: %s, nickname: %s, who: %s, type: %s, class: %s", getName(), getNickname(), getWho(), getType(), getClass());
    }

    @Override
    public String getTransferableName() {
        return Optional.ofNullable(getNickname()).orElse(getName());
    }

    public String getDeleteText() {
        return deleteText;
    }


    @Override
    public boolean hasNoteField() {
        return true;

    }

    @Override
    public boolean eligibleForExpedite() {
        return true;

    }
}
