package com.ellirion.util.model;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.Position;
import org.bukkit.Location;
import org.bukkit.World;

public class Point {

    @Getter private final double x;
    @Getter private final double y;
    @Getter private final double z;

    /**
     * Constructs a Point at (0,0,0).
     */
    public Point() {
        this(0d, 0d, 0d);
    }

    /**
     * Constructs a Point form Position {@code p}.
     * @param p The Position to convert
     */
    public Point(final Position p) {
        this(p.getX(), p.getY(), p.getZ());
    }

    /**
     * Constructs a Point from Location {@code l}.
     * @param l The Location to convert
     */
    public Point(final Location l) {
        this(l.getX(), l.getY(), l.getZ());
    }

    /**
     * Constructs a Point at (x,y,z).
     * @param x The x-component
     * @param y The y-component
     * @param z The z-component
     */
    public Point(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructs a Point at (x,y,z).
     * @param x The x-component
     * @param y The y-component
     * @param z The z-component
     */
    public Point(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the minimum of the two Points as a new Point.
     * @param p The other Point
     * @return The minimum Point
     */
    public Point min(Point p) {
        return new Point(Math.min(x, p.x), Math.min(y, p.y), Math.min(z, p.z));
    }

    /**
     * Returns the maximum o the two Points as a new Point.
     * @param p The other Point
     * @return The maximum Point
     */
    public Point max(Point p) {
        return new Point(Math.max(x, p.x), Math.max(y, p.y), Math.max(z, p.z));
    }

    /**
     * Rounds the components of this Point down and returns the new Point.
     * @return The rounded-down Point
     */
    public Point floor() {
        return new Point(Math.floor(x), Math.floor(y), Math.floor(z));
    }

    /**
     * Rounds the components of this Point up and returns the new Point.
     * @return The rounded-up Point
     */
    public Point ceil() {
        return new Point(Math.ceil(x), Math.ceil(y), Math.ceil(z));
    }

    /**
     * Calculates the Euclidian distance between this Point and Point {@code p}.
     * @param p The other Point
     * @return The Euclidian distance
     */
    public double distanceEuclidian(Point p) {
        return Math.sqrt(Math.pow(p.x - x, 2) +
                         Math.pow(p.y - y, 2) +
                         Math.pow(p.z - z, 2));
    }

    /**
     * Calculates the Manhattan distance between this Point and Point {@code p}.
     * @param p The other Point
     * @return The Manhattan distance
     */
    public double distanceManhattan(Point p) {
        return Math.abs(p.x - x) +
               Math.abs(p.y - y) +
               Math.abs(p.z - z);
    }

    /**
     * Calculates the Euclidian distance between this Point and the line that passes through
     * both Point {@code p1} and Point {@code p2}.
     * @param p1 The first point
     * @param p2 The second point
     * @return The Euclidian distance between this Point and the line
     */
    public double distanceFromLine(Point p1, Point p2) {
        // Heron's formula
        double a = p1.distanceEuclidian(p2); // p1 => p2
        double b = p2.distanceEuclidian(this); // p2 => p0
        double c = this.distanceEuclidian(p1); // p0 => p1
        double s = (a + b + c) / 2;
        double t = Math.sqrt(s * (s - a) * (s - b) * (s - c));

        // Line defined by two points
        // Denominator = distance between p1 and p2
        // Numerator = twice the area of the triangle with its vertices at the three points.
        return 2 * t / a;
    }

    /**
     * @return A new Point exactly 1 unit towards positive X.
     */
    public Point east() {
        return new Point(x + 1, y, z);
    }

    /**
     * @return A new Point exactly 1 unit towards negative X.
     */
    public Point west() {
        return new Point(x - 1, y, z);
    }

    /**
     * @return A new Point exactly 1 unit towards negative Z.
     */
    public Point north() {
        return new Point(x, y, z - 1);
    }

    /**
     * @return A new Point exactly 1 unit towards positive Z.
     */
    public Point south() {
        return new Point(x, y, z + 1);
    }

    /**
     * @return A new Point exactly 1 unit towards positive Y.
     */
    public Point up() {
        return new Point(x, y + 1, z);
    }

    /**
     * @return A new Point exactly 1 unit towards negative Y.
     */
    public Point down() {
        return new Point(x, y - 1, z);
    }

    /**
     * Converts this Point to a Position.
     * @return The resulting position
     */
    public Position toPosition() {
        return new Position(x, y, z);
    }

    /**
     * Converts this Point to a Location using World {@code w}.
     * @param w The world this Location belongs to
     * @return The resulting Location
     */
    public Location toLocation(World w) {
        return new Location(w, x, y, z);
    }

    public int getBlockX() {
        return (int) Math.floor(x);
    }

    public int getBlockY() {
        return (int) Math.floor(y);
    }

    public int getBlockZ() {
        return (int) Math.floor(z);
    }

    @Override
    public int hashCode() {
        return hash(Double.hashCode(x)) ^ hash(Double.hashCode(y)) ^ hash(Double.hashCode(z));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Point) {
            Point p = (Point) o;
            return x == p.x && y == p.y && z == p.z;
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return String.format("Point(x=%f, y=%f, z=%f)", x, y, z);
    }

    // Simpele integer hash functie: https://stackoverflow.com/a/12996028
    private int hash(int x) {
        final int half = 32 / 2;
        final int mult = 0x45d9f3b;
        x = ((x >>> half) ^ x) * mult;
        x = ((x >>> half) ^ x) * mult;
        x = (x >>> half) ^ x;
        return x;
    }

    /**
     * Invert a point.
     * @return an inverted point.
     */
    public Point invert() {
        return new Point(x * -1, y * -1, z * -1);
    }

    /**
     * Translate a point.
     * @param point point to translate with.
     * @return an translated point.
     */
    public Point translate(Point point) {
        return new Point(x + point.getX(), y + point.getY(), z + point.getZ());
    }
}
