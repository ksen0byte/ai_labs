package com.ksenobyte.lab03;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Condition {
    public Condition(Double probability, String description, Feature... pwdArray) {
        this.probability = probability;
        this.description = description;
        this.featureList = new ArrayList<>(Arrays.asList(pwdArray));
    }

    private Double probability;
    List<Feature> featureList;
    private String description;
}
