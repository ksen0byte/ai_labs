package com.ksenobyte.lab01;

import lombok.Data;
import org.openimaj.math.geometry.point.Point2d;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

@Data
public class DistanceResult {
    private Point2d p1;
    private Point2d p2;
    private double distance;

    DistanceResult(Point2d p1, Point2d p2) {
        this.p1 = p1;
        this.p2 = p2;
        distance = sqrt(pow(p1.getX() - p2.getX(), 2) + pow(p1.getY() - p2.getY(), 2));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 3 - 1; i++) {
            for (int j = i + 1; j < 3; j++) {
                System.out.println(i + " : " + j);
            }
        }
    }
}
