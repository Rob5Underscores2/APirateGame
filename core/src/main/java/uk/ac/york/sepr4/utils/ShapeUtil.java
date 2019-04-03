package uk.ac.york.sepr4.utils;


import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class ShapeUtil {

    /***
     * Checks whether a polygon and rectangle overlap.
     * Used for checking collisions.
     * @param polygon
     * @param rectangle
     * @return true if polygon and rectangle overlap
     */
    public static boolean overlap(Polygon polygon, Rectangle rectangle) {
        float[] points = polygon.getTransformedVertices();
        for(int i=0;i<points.length;i+=2) {
            float x = points[i], y = points[i+1];
            if(rectangle.contains(x, y)){
                return true;
            }
        }
        return false;
    }

    public static double distance(Polygon poly1, Polygon poly2) {
        //some arbitrarily large number
        double minDist = 1000000;

        for(int i=0;i<poly1.getTransformedVertices().length;i+=2) {
            float p1x = poly1.getTransformedVertices()[i];
            float p1y = poly1.getTransformedVertices()[i + 1];
            for (int i2 = 0; i2 < poly1.getTransformedVertices().length; i2 += 2) {
                float p2x = poly1.getTransformedVertices()[i2];
                float p2y = poly1.getTransformedVertices()[i2 + 1];

                double dist = Math.sqrt(Math.pow((p1x-p2x), 2) + Math.pow((p1y-p2y), 2));
                if(dist < minDist) {
                    minDist = dist;
                }
            }
        }
        return minDist;
    }

}
