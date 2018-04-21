package com.d3.datawrappers.company;

import com.d3.conduit.ConduitBuilder;
import com.d3.datawrappers.common.AttributeType;
import com.d3.exceptions.ConduitException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class CompanyHelper {

    private static final String TRUE_STR = "true";
    private static final String FALSE_STR = "false";

    private CompanyHelper() {
        throw new IllegalAccessError("Utility Class");
    }

    public static void setupCompanyAttributes(String fi, String baseApiUrl, String apiVersion) throws ConduitException {
        List<D3Company> companies = new ArrayList<>();
        for (Map.Entry<D3Company, List<D3CompanyAttribute>> entry : COMPANY_ATTRIBUTE_MAP.entrySet()) {
            D3Company key = entry.getKey();
            List<D3CompanyAttribute> value = entry.getValue();
            Pair<D3Company, List<D3CompanyAttribute>> pair = pairCompanyWithAttributes(key.getUid(), value);
            companies.add(pair.getValue0());
        }

        try {
            ConduitBuilder builder = new ConduitBuilder().addCompanies(companies);

            builder.uploadFileToApi(fi, baseApiUrl, apiVersion);
        } catch (ConduitException e) {
            log.error("Error uploading conduit", e);
            throw new ConduitException("Error uploading conduit");
        }
    }

    private static Pair<D3Company, List<D3CompanyAttribute>> pairCompanyWithAttributes(String fi, List<D3CompanyAttribute> attributes) {
        D3Company company = new D3Company(fi, attributes);
        return Pair.with(company, attributes);
    }

    private static final Map<D3Company, List<D3CompanyAttribute>> COMPANY_ATTRIBUTE_MAP = ImmutableMap.of(
            new D3Company("ROOT"), ImmutableList.of(
                    new D3CompanyAttribute(CompanyAttribute.USER_MANAGEMENT.getDefinition(), AttributeType.BOOLEAN, TRUE_STR),
                    new D3CompanyAttribute(CompanyAttribute.TOGGLE.getDefinition(), AttributeType.BOOLEAN, TRUE_STR),
                    new D3CompanyAttribute(CompanyAttribute.OUT_OF_BAND_VERIFICATION.getDefinition(), AttributeType.BOOLEAN, TRUE_STR),
                    new D3CompanyAttribute(CompanyAttribute.EXPEDITED_PAYMENT_ENABLED.getDefinition(), AttributeType.BOOLEAN, TRUE_STR)),
            new D3Company("fi1"), ImmutableList.of(
                    new D3CompanyAttribute(CompanyAttribute.TOGGLE.getDefinition(), AttributeType.BOOLEAN, FALSE_STR),
                    new D3CompanyAttribute(CompanyAttribute.OUT_OF_BAND_VERIFICATION.getDefinition(), AttributeType.BOOLEAN, FALSE_STR)),
            new D3Company("fi2"), ImmutableList.of(
                    new D3CompanyAttribute(CompanyAttribute.TOGGLE.getDefinition(), AttributeType.BOOLEAN, TRUE_STR),
                    new D3CompanyAttribute(CompanyAttribute.OUT_OF_BAND_VERIFICATION.getDefinition(), AttributeType.BOOLEAN, FALSE_STR)),
            new D3Company("fi3"), ImmutableList.of(
                    new D3CompanyAttribute(CompanyAttribute.TOGGLE.getDefinition(), AttributeType.BOOLEAN, FALSE_STR),
                    new D3CompanyAttribute(CompanyAttribute.OUT_OF_BAND_VERIFICATION.getDefinition(), AttributeType.BOOLEAN, TRUE_STR),
                    new D3CompanyAttribute(CompanyAttribute.OUT_OF_BAND_VERIFICATION_ON_LOGIN.getDefinition(), AttributeType.BOOLEAN, FALSE_STR)));

}
