package com.ksenobyte.lab01;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.math.geometry.point.Point2d;
import org.openimaj.math.geometry.point.Point2dImpl;
import org.openimaj.math.geometry.shape.Circle;
import org.openimaj.math.geometry.shape.Rectangle;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class Main {
    private static final Float[] BLACK = {0f, 0f, 0f};
    private static final Float[] GRAY = {0.9f, 0.9f, 0.9f};
    private static final int RADIUS = 100;

    public static void main(String[] args) throws IOException {
        File input = new File("C:\\Users\\vladi\\IdeaProjects\\AI\\src\\main\\resources\\images\\lab01.jpg");
        MBFImage image = ImageUtilities.readMBF(input);
//        drawBigPixel(100, 100, image);

//        image.drawPoint(new Point2dImpl(100, 100), BLACK, 10);

//        generating
        List<Cluster> clusters = generateClusters();
        List<DistanceResult> closestResults = findClosestDistances(clusters);
        List<DistanceResult> centerResults = findCenterDistances(clusters);

//        drawing
        drawClusters(clusters, image);

        drawResults(closestResults, image, Color.BLACK);
        drawResults(centerResults, image, Color.RED);

        DisplayUtilities.display(image);
    }


    private static void drawResults(List<DistanceResult> distanceResults, MBFImage image, Color color) {
        distanceResults.forEach(distanceResult ->
                image.drawLine(distanceResult.getP1(), distanceResult.getP2(), getColor(color)));
    }

    private static List<DistanceResult> findClosestDistances(List<Cluster> clusters) {
        List<DistanceResult> result = new ArrayList<>();
        for (int i = 0; i < clusters.size() - 1; i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                DistanceResult min = findDistance(clusters.get(i), clusters.get(j), DistanceType.MIN);
                DistanceResult max = findDistance(clusters.get(i), clusters.get(j), DistanceType.MAX);
                System.out.println("Cluster " + i + " x Cluster " + j);
                System.out.println("Min distance : " + min.getDistance());
                System.out.println("Max distance : " + max.getDistance());
                result.add(min);
                result.add(max);
            }
        }
        return result;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private static List<DistanceResult> findCenterDistances(List<Cluster> clusters) {
        List<DistanceResult> result = new ArrayList<>();

        List<Point2dImpl> averagePoints = clusters.stream()
                .map(Cluster::getPoints)
                .map(points -> {
                    double averageX = points.stream().mapToDouble(Point2d::getX).average().getAsDouble();
                    double averageY = points.stream().mapToDouble(Point2d::getY).average().getAsDouble();
                    return new Point2dImpl(averageX, averageY);
                }).collect(Collectors.toList());

        for (int i = 0; i < clusters.size() - 1; i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                DistanceResult distanceResult = new DistanceResult(averagePoints.get(i), averagePoints.get(j));
                result.add(distanceResult);
                System.out.println("Cluster " + i + " x Cluster " + j);
                System.out.println("Mass center distance : " + distanceResult.getDistance());
            }
        }
        return result;
    }

    private static DistanceResult findDistance(Cluster c1, Cluster c2, DistanceType distanceType) {
        double distance = distanceType.equals(DistanceType.MAX) ? 0 : Double.MAX_VALUE;

        DistanceResult distanceResult = null;
        for (Point2d p1 : c1.getPoints()) {
            for (Point2d p2 : c2.getPoints()) {
                DistanceResult tmp = new DistanceResult(p1, p2);
                boolean condition = DistanceType.MAX.equals(distanceType) ? tmp.getDistance() > distance : tmp.getDistance() < distance;
                if (condition) {
                    distanceResult = tmp;
                    distance = tmp.getDistance();
                }
            }
        }
        return distanceResult;
    }

    private static List<Cluster> generateClusters() {
        return Arrays.asList(
                generateCluster(300, 150, Color.CYAN),
                generateCluster(150, 300, Color.BLUE),
                generateCluster(500, 500, Color.lightGray)
        );
    }

    private static Cluster generateCluster(int x, int y, Color color) {
        Cluster result = new Cluster(x, y, color);
        List<Point2d> points = result.getPoints();

        Random rand = new Random();
        int numberOfPoints = rand.nextInt(10) + 11; // 10..20
        for (int i = 0; i < numberOfPoints; i++) {
            double a = random() * 2 * PI;
            double r = RADIUS * sqrt(random());
            int randX = (int) (r * cos(a)) + x;
            int randY = (int) (r * sin(a)) + y;
            Point2dImpl point = new Point2dImpl(randX, randY);
            points.add(point);
        }
        return result;
    }

    private static void drawClusters(List<Cluster> clusters, MBFImage image) {
        clusters.forEach(cluster -> {
            Float[] color = getColor(cluster.getColor());
            image.drawShapeFilled(new Circle(cluster.getX(), cluster.getY(), RADIUS + 5), color);
            cluster.getPoints().forEach(point -> drawPoint(image, point));
        });
    }

    private static Float[] getColor(Color rgb) {
        return new Float[]{(float) (rgb.getRed() / 255.), (float) (rgb.getBlue() / 255.), (float) (rgb.getGreen() / 255.)};
    }

    private static void drawPoint(MBFImage image, Point2d point) {
        image.drawShapeFilled(new Rectangle(point.getX(), point.getY(), 2, 2), BLACK);
    }

}
