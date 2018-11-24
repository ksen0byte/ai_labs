package com.ksenobyte.lab03;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleBinaryOperator;
import java.util.function.ToDoubleFunction;

/*
Situation:
Joggers, while running could have 2 possible features:
    W1. Increased heart rate
    W2. Shortness of breathe.

There are 3 conditions where above-mentioned features could be observed:
    D1. Speed over 15 km/h
    D2. Distance over 20 km
    D3. Normal conditions

    Di      P(Di)       P(W1|Di)        P(W2|Di)
    D1      0.05        0.2             0.3
    D2      0.15        0.4             0.5
    D3      0.80        0.0             0.05

 */
public class Lab03 {
    private static final List<String> conditions = Arrays.asList(
            "Speed over 15 km/h",
            "Distance over 20 km",
            "Normal conditions"
    );
    public static final DecimalFormat df = new DecimalFormat("#.##");

    public static void main(String[] args) {
        Feature c1f1 = new Feature(0.2, Indicator.HEART_RATE);
        Feature c1f2 = new Feature(0.3, Indicator.SHORT_BREATHE);
        Feature c2f1 = new Feature(0.4, Indicator.HEART_RATE);
        Feature c2f2 = new Feature(0.5, Indicator.SHORT_BREATHE);
        Feature c3f1 = new Feature(0.0, Indicator.HEART_RATE);
        Feature c3f2 = new Feature(0.05, Indicator.SHORT_BREATHE);

        Condition c1 = new Condition(0.05, conditions.get(0), c1f1, c1f2);
        Condition c2 = new Condition(0.15, conditions.get(1), c2f1, c2f2);
        Condition c3 = new Condition(0.80, conditions.get(2), c3f1, c3f2);

        List<Condition> conditions = Arrays.asList(c1, c2, c3);

        calculateProbability(conditions, Indicator.HEART_RATE, Indicator.SHORT_BREATHE);
        calculateProbability(conditions, Indicator.HEART_RATE);
        calculateProbability(conditions, Indicator.SHORT_BREATHE);
        calculateProbability(conditions);
    }

    private static void calculateProbability(List<Condition> conditions, Indicator... indicators) {
        System.out.println("Indicators: " + Arrays.toString(indicators));
        List<Indicator> indicatorList = Arrays.asList(indicators);
        DoubleBinaryOperator multiplication = (left, right) -> left * right;
        DoubleBinaryOperator sum = (left, right) -> left + right;
        ToDoubleFunction<Feature> probabilityByIndicatorFunction = f -> indicatorList.contains(f.getIndicator()) ? f.getProbability() : 1 - f.getProbability();

        double baseProbability = conditions.stream()
                .mapToDouble(c -> c.getProbability() * c.getFeatureList().stream().mapToDouble(probabilityByIndicatorFunction).reduce(1, multiplication))
                .reduce(0, sum);

        for (Condition condition : conditions) {
            double featureProbability = condition.featureList.stream()
                    .mapToDouble(probabilityByIndicatorFunction)
                    .reduce(1, multiplication);

            Double probability = (condition.getProbability() * featureProbability) / (baseProbability);
            System.out.println(condition.getDescription() + ": " + df.format(probability));
        }
        System.out.println();
    }

}
