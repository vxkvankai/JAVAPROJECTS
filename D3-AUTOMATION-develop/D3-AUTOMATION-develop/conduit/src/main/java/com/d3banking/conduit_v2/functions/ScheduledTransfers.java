package com.d3banking.conduit_v2.functions;

import javax.xml.bind.annotation.XmlElement;


public class ScheduledTransfers {

	private String fauid;
	 
    @XmlElement(name = "fauid")
    public String getFromAccountId() {
        return fauid;
    }

    public void setFromAccountId(String accountId) {
        this.fauid = accountId;
    }
}
