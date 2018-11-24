package com.ksenobyte.lab03;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Feature {
    private Double probability;
    private Indicator indicator;
}
