package com.d3.pages.consumer.moneymovement.recipients.edit.base;

import com.d3.datawrappers.recipient.base.Recipient;
import com.d3.pages.consumer.moneymovement.recipients.edit.EditRecipients;

public interface EditRecipientsForm<T, R extends Recipient> {
    boolean isFormInformationCorrect(R recipient);
    EditRecipientsForm<T, R> fillOutForm(R recipient);
    EditRecipients clickSaveButton();
    EditRecipients clickCancelButton(boolean isEditable);
}
