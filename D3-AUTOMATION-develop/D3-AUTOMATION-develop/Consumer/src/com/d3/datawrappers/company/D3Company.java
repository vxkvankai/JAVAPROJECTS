package com.d3.datawrappers.company;


import com.d3.datawrappers.account.ProductType;
import com.d3.datawrappers.common.D3Attribute;

import java.util.ArrayList;
import java.util.List;

public class D3Company {

    private String uid; // Required
    private String puid; // Optional
    private Boolean delete; // Optional
    private Type tp; // Required
    private String busn; // Required if adding new company branch/fi
    private List<ProductType> acctprodlst; // Optional
    private List<String> l10nlst; // Optional
    private List<D3Attribute> attributes;

    public D3Company(String uid, List<D3CompanyAttribute> attributes) {
        this.uid = uid;
        this.puid = "ROOT";
        this.tp = "root".equalsIgnoreCase(uid) ? Type.ROOT : Type.INSTITUTION;
        this.attributes = new ArrayList<>();
        if (attributes != null) {
            this.attributes.addAll(attributes);
        }
    }

    public D3Company(String uid, D3CompanyAttribute attribute) {
        this.uid = uid;
        this.puid = "ROOT";
        this.tp = "root".equalsIgnoreCase(uid) ? Type.ROOT : Type.INSTITUTION;
        this.attributes = new ArrayList<>();
        this.attributes.add(attribute);
    }

    public D3Company(String uid) {
        this.uid = uid;
        this.puid = "ROOT";
        this.tp = "root".equalsIgnoreCase(uid) ? Type.ROOT : Type.INSTITUTION;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public String getDelete() {
        if (delete == null) {
            return "";
        }
        return delete ? "1" : "0";
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public String getType() {
        return tp.getCompanyType();
    }

    public void setType(Type tp) {
        this.tp = tp;
    }

    public String getBusiness() {
        return busn;
    }

    public void setBusiness(String busn) {
        this.busn = busn;
    }

    public List<ProductType> getAccountProductTypeList() {
        return acctprodlst;
    }

    public void setAccountProductTypeList(List<ProductType> acctprodlst) {
        this.acctprodlst = acctprodlst;
    }

    public List<String> getL10nList() {
        return l10nlst;
    }

    public void setL10nList(List<String> l10nlst) {
        this.l10nlst = l10nlst;
    }

    public void addAttribute(D3CompanyAttribute attribute) {
        this.attributes.add(attribute);
    }

    public List<D3Attribute> getAttributes() {
        return attributes;
    }

    public enum Type {
        ROOT("root"),
        PROCESSOR("processor"),
        HOLDING("holding"),
        INSTITUTION("institution"),
        REGION("region"),
        BRANCH("branch");

        protected String companyType;

        Type(String value) {
            this.companyType = value;
        }

        public String getCompanyType() {
            return companyType;
        }
    }


}
