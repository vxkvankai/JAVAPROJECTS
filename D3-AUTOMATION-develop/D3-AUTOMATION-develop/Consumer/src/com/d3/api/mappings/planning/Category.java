package com.d3.api.mappings.planning;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {
    private Integer id;
    private String type;
    private String group;
    private String name;
    private String owner;
    private String description;
    private String profileType;
}
