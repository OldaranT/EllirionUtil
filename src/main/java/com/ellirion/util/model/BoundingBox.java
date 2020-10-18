package com.ellirion.util.model;

import lombok.Getter;
import net.minecraft.server.v1_16_R2.NBTTagCompound;

public class BoundingBox {

    @Getter private int x1, x2;
    @Getter private int y1, y2;
    @Getter private int z1, z2;

    /**
     * Create a BoundingBox between (inclusive) point (0,0,0) and point (0,0,0).
     */
    public BoundingBox() {
        this(0, 0, 0, 0, 0, 0);
    }

    /**
     * Creates a BoundingBox at exactly the given point {@code p}.
     * @param p The point the BoundingBox should be created at
     */
    public BoundingBox(final Point p) {
        this(p, p);
    }

    /**
     * Creates a BoundingBox at exactly the given point (x1,y1,z1).
     * @param x1 The x-component
     * @param y1 The y-component
     * @param z1 The z-component
     */
    public BoundingBox(final int x1, final int y1, final int z1) {
        this(x1, y1, z1, x1, y1, z1);
    }

    /**
     * Creates a BoundingBox between (inclusive) point {@code p1} and {@code p2}.
     * @param p1 The first point
     * @param p2 The second point
     */
    public BoundingBox(final Point p1, final Point p2) {
        Point pMin = p1.min(p2).floor();
        Point pMax = p1.max(p2).floor();

        this.x1 = pMin.getBlockX();
        this.y1 = pMin.getBlockY();
        this.z1 = pMin.getBlockZ();

        this.x2 = pMax.getBlockX();
        this.y2 = pMax.getBlockY();
        this.z2 = pMax.getBlockZ();
    }

    /**
     * Create a BoundingBox between (inclusive) point (x1,y1,z1) and point (x2,y2,z2).
     * @param x1 The first x-component
     * @param y1 The first y-component
     * @param z1 The first z-component
     * @param x2 The second x-component
     * @param y2 The second y-component
     * @param z2 The second z-component
     */
    public BoundingBox(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        this.x1 = Math.min(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.z1 = Math.min(z1, z2);

        this.x2 = Math.max(x1, x2);
        this.y2 = Math.max(y1, y2);
        this.z2 = Math.max(z1, z2);
    }

    /**
     * Checks if the point {@code p} lies within the blocks contained in this BoundingBox.
     * @param p The point to check
     * @return Whether the point lies within the bounds of this BoundingBox
     */
    public boolean intersects(Point p) {
        int px = p.getBlockX();
        int py = p.getBlockY();
        int pz = p.getBlockZ();
        return x1 <= px && px <= x2 && y1 <= py && py <= y2 && z1 <= pz && pz <= z2;
    }

    /**
     * Checks if the BoundingBox {@code bb} intersects with the current BoundingBox.
     * @param bb The BoundingBox to check for intersection with
     * @return Whether the two BoundingBoxes intersect
     */
    public boolean intersects(final BoundingBox bb) {
        return x1 <= bb.x2 && bb.x1 <= x2 && y1 <= bb.y2 && bb.y1 <= y2 && z1 <= bb.z2 && bb.z1 <= z2;
    }

    /**
     * Translates the BoundingBox to local coordinates.
     * @return A new BoundingBox with local coordinates
     */
    public BoundingBox toLocal() {
        return new BoundingBox(0, 0, 0, x2 - x1, y2 - y1, z2 - z1);
    }

    /**
     * Translates the BoundingBox to world coordinates, with {@code pos} as the origin.
     * @param pos The new origin
     * @return The BoundingBox at the world coordinates
     */
    public BoundingBox toWorld(Point pos) {
        BoundingBox local = toLocal();
        int px = (int) Math.round(pos.getX());
        int py = (int) Math.round(pos.getY());
        int pz = (int) Math.round(pos.getZ());
        return new BoundingBox(px, py, pz,
                               px + (local.x2 - local.x1),
                               py + (local.y2 - local.y1),
                               pz + (local.z2 - local.z1));
    }

    /**
     * Gets the smallest-component point of this BoundingBox.
     * @return The smallest-component point of this BoundingBox
     */
    public Point getPoint1() {
        return new Point(x1, y1, z1);
    }

    /**
     * Gets the largest-component point of this BoundingBox.
     * @return The largest-component point of this BoundingBox
     */
    public Point getPoint2() {
        return new Point(x2, y2, z2);
    }

    /**
     * Get the width (x-axis) of this BoundingBox.
     * @return the width
     */
    public int getWidth() {
        return x2 - x1 + 1;
    }

    /**
     * Gets the height (y-axis) of this BoundingBox.
     * @return the height
     */
    public int getHeight() {
        return y2 - y1 + 1;
    }

    /**
     * Gets the depth (z-axis) of this BoundingBox.
     * @return the depth
     */
    public int getDepth() {
        return z2 - z1 + 1;
    }

    /**
     * Serialize BoundingBox {@code bb} to an NBTTagCompound.
     * @param bb The BoundingBox to serialize
     * @return The resulting NBTTagCompound
     */
    public static NBTTagCompound toNBT(BoundingBox bb) {
        NBTTagCompound root = new NBTTagCompound();
        root.setInt("x1", bb.x1);
        root.setInt("y1", bb.y1);
        root.setInt("z1", bb.z1);
        root.setInt("x2", bb.x2);
        root.setInt("y2", bb.y2);
        root.setInt("z2", bb.z2);
        return root;
    }

    /**
     * Deserialize a BoundingBox from NBTTagCompound {@code root}.
     * @param root The NBTTagCompound to deserialize from
     * @return The resulting BoundingBox
     */
    public static BoundingBox fromNBT(NBTTagCompound root) {
        return new BoundingBox(
                root.getInt("x1"),
                root.getInt("y1"),
                root.getInt("z1"),
                root.getInt("x2"),
                root.getInt("y2"),
                root.getInt("z2"));
    }

    /**
     * Get all corners of this BoundingBox.
     * @return an array of points.
     */
    public Point[] getCorners() {
        return new Point[] {
                new Point(x1, y1, z1),
                new Point(x1, y1, z2),
                new Point(x1, y2, z2),
                new Point(x1, y2, z1),
                new Point(x2, y1, z1),
                new Point(x2, y1, z2),
                new Point(x2, y2, z2),
                new Point(x2, y2, z1)
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BoundingBox) {
            BoundingBox bb = (BoundingBox) obj;
            return x1 == bb.x1 && y1 == bb.y1 && z1 == bb.z1 && x2 == bb.x2 && y2 == bb.y2 && z2 == bb.z2;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return String.format("BoundingBox(x1=%d, y1=%d, z1=%d, x2=%d, y2=%d, z2=%d)",
                             x1, y1, z1, x2, y2, z2);
    }
}
