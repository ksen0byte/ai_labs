package com.ksenobyte.lab01;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.openimaj.math.geometry.point.Point2d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
class Cluster {
    private final int x;
    private final int y;
    private final Color color;
    private List<Point2d> points = new ArrayList<>();
}
