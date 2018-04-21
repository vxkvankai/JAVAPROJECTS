
package com.d3.api.mappings.session;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile {
    private String loginId;
    private Boolean enrolled;
    private String firstName;
    private String middleName;
    private String lastName;
    private Boolean taxIdExists;
    private String dateOfBirth;
    private List<EmailAddress> emailAddresses = null;
    private List<Object> phoneNumbers = null;
    private Inbox inbox;
    private Push push;
    private PhysicalAddress physicalAddress;
    private Attributes attributes;
    private List<ValidEmailType> validEmailTypes = null;
    private List<ValidPhoneType> validPhoneTypes = null;
    private List<String> validProfileModes = null;
    private String type;
    private ServicePermissions servicePermissions;
    private String profileType;
    private String previousLogin;
}
