/*************************************************************************
 *  Compilation:  javac Point2D.java
 *  http://algs4.cs.princeton.edu/12oop/Point2D.java.html
 *  Immutable point data type for points in the plane.
 *
 *************************************************************************/

import java.util.Arrays;
import java.util.Comparator;

public class Point2D{

    private int x;    // x coordinate
    private int y;    // y coordinate

    // create a new point (x, y)
    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // return the x-coorindate of this point
    public int x() { return x; }

    // return the y-coorindate of this point
    public int y() { return y; }



    // does this point equal y?
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Point2D that = (Point2D) other;
        return this.x == that.x && this.y == that.y;
    }
}
