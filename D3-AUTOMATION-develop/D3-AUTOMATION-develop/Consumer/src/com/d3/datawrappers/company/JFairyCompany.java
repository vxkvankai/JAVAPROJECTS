package com.d3.datawrappers.company;

import com.d3.datawrappers.user.Loginable;
import com.d3.helpers.RandomHelper;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.company.Company;
import io.codearte.jfairy.producer.company.CompanyProperties;

import java.io.Serializable;

public class JFairyCompany implements Serializable, Loginable {
    private String name;
    private String domain;
    private String email;
    private String vatIdentificationNumber;
    private String loginId;

    public JFairyCompany(Company company) {
        this.name = company.getName();
        this.domain = company.getDomain();
        this.email = company.getEmail();
        this.vatIdentificationNumber = company.getVatIdentificationNumber();
        this.loginId = company.getName().replaceAll("[ .]", "").toLowerCase().trim() + RandomHelper.getRandomString(3);

    }

    public static JFairyCompany createNewCompany() {
        return new JFairyCompany(Fairy.create().company());
    }

    public static JFairyCompany createNewCompany(CompanyProperties.CompanyProperty withProperty) {
        return new JFairyCompany(Fairy.create().company(withProperty));
    }

    public Company toCompany() {
        return new Company(name, domain, email, vatIdentificationNumber);
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return "http://www." + domain;
    }

    public String getEmail() {
        return email;
    }

    public String getDomain() {
        return domain;
    }

    public String getVatIdentificationNumber() {
        return vatIdentificationNumber;
    }

    public String getLogin() {
        return loginId;
    }
}
