package com.d3.api.mappings.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {
    private String name;
    private Integer displayOrder;
    private String l10nKey;
    private String groupL10nKey;
    private Integer id;
    private String type;
    private List<String> availablePermissions = null;
    private List<String> access = null;
}
