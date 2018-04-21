package com.d3.pages.asserts.login;

import com.d3.l10n.login.LoginL10N;
import com.d3.pages.consumer.AssertionsBase;
import com.d3.pages.consumer.termsofservice.TermsOfService;

public class TermsOfServiceAssert extends AssertionsBase<TermsOfServiceAssert, TermsOfService> {
    public TermsOfServiceAssert(TermsOfService termsOfService) {
        super(termsOfService, TermsOfServiceAssert.class);
    }

    @Override
    public TermsOfServiceAssert atPage() {
        if (!actual.getTosTitleText().trim().equals(LoginL10N.Localization.TOS_TITLE.getValue())) {
            failWithMessage("TOS Title <%s> did not match the expected L10n value <%s>", actual.getTosTitleText().trim(), LoginL10N.Localization.TOS_TITLE.getValue());
        }
        return myself;
    }
}
