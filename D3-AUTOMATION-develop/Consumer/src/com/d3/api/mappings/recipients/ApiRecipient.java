package com.d3.api.mappings.recipients;

import com.d3.datawrappers.recipient.CompanyBillPayManualRecipient;
import com.d3.datawrappers.recipient.CompanyBillPaySeedRecipient;
import com.d3.datawrappers.recipient.CompanyWireRecipient;
import com.d3.datawrappers.recipient.PersonBillAddressRecipient;
import com.d3.datawrappers.recipient.PersonWireRecipient;
import com.d3.datawrappers.recipient.base.AccountIOwnRecipient;
import com.d3.datawrappers.recipient.base.OnUsAccountIOwnRecipient;
import com.d3.datawrappers.recipient.base.Recipient;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiRecipient {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("providers")
    @Expose
    private List<Provider> providers = null;
    public ApiRecipient(OnUsAccountIOwnRecipient recipient) {
        init(recipient);
        this.providers.add(new Provider(recipient));
    }

    public ApiRecipient(PersonBillAddressRecipient recipient) {
        init(recipient);
        this.providers.add(new Provider(recipient));
    }

    public ApiRecipient(PersonWireRecipient recipient) {
        init(recipient);
        this.providers.add(new Provider(recipient));
    }

    public ApiRecipient(CompanyBillPaySeedRecipient recipient, SearchResults results) {
        init(recipient);
        this.providers.add(new Provider(recipient, results));
    }

    public ApiRecipient(AccountIOwnRecipient recipient) {
        init(recipient);
        this.providers.add(new Provider(recipient));
    }

    public ApiRecipient(CompanyWireRecipient recipient) {
        init(recipient);
        this.providers.add(new Provider(recipient));
    }

    public ApiRecipient(CompanyBillPayManualRecipient recipient) {
        init(recipient);
        this.providers.add(new Provider(recipient));
    }

    private void init(Recipient recipient) {
        this.name = recipient.getName();
        this.nickname = recipient.getNickname();
        this.providers = new ArrayList<>();
        this.type = recipient.getWho().toString();
    }

    public String toString() {
        return name;
    }
}