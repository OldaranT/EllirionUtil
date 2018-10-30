package com.ellirion.util.model;

import lombok.Getter;

public enum Direction {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0),
    NONE(0, 0);

    static {
        NORTH.left = Direction.WEST;
        NORTH.right = Direction.EAST;
        NORTH.reverse = Direction.SOUTH;

        EAST.left = Direction.NORTH;
        EAST.right = Direction.SOUTH;
        EAST.reverse = Direction.WEST;

        SOUTH.left = Direction.EAST;
        SOUTH.right = Direction.WEST;
        SOUTH.reverse = Direction.NORTH;

        WEST.left = Direction.SOUTH;
        WEST.right = Direction.NORTH;
        WEST.reverse = Direction.EAST;

        NONE.left = Direction.NONE;
        NONE.right = Direction.NONE;
        NONE.reverse = Direction.NONE;
    }

    private int dx, dz;
    @Getter private Direction left, right, reverse;

    /**
     * Constructs a Direction with the given deltas.
     * @param dx The delta X
     * @param dz The delta Z
     */
    Direction(final int dx, final int dz) {
        this.dx = dx;
        this.dz = dz;
    }

    /**
     * Applies the Direction to the given point.
     * @param p The point to apply this Direction to
     * @return The Point in this Direction relative to point {@code p}
     */
    public Point apply(final Point p) {
        return new Point(p.getBlockX() + dx, p.getBlockY(), p.getBlockZ() + dz);
    }

    /**
     * Gets the DirectionChange that, when applied to this Direction, will yield Direction {@code d}.
     * @param d The Direction that we want to go in
     * @return The DirectionChange that would yield {@code d} when applied to this Direction
     */
    public DirectionChange getChangeTo(final Direction d) {
        if (left == d) {
            return DirectionChange.LEFT;
        } else if (right == d) {
            return DirectionChange.RIGHT;
        } else if (this == d) {
            return DirectionChange.NONE;
        } else {
            return DirectionChange.REVERSE;
        }
    }

    /**
     * Checks if this Direction is perpendicular to Direction {@code d}.
     * @param d The other Direction that this Direction may be perpendicular to
     * @return Whether this Direction is perpendicular to Direction {@code d}
     */
    public boolean isPerpendicularTo(final Direction d) {
        int x = dx + d.dx;
        int z = dz + d.dz;
        // If they are the same direction, X or Y is 2.
        // If they are opposite directions, X and Y are 0.
        // If they are perpendicular, they are both 1 or -1.
        return !(x == 2 || z == 2 || (x == 0 && z == 0));
    }

    /**
     * Checks if this Direction is the opposite of Direction {@code d}.
     * @param d The other Direction that this Direction may be opposite of
     * @return Whether this Direction is the opposite of Direction {@code d}
     */
    public boolean isOppositeOf(final Direction d) {
        return -dx == d.dx && -dz == d.dz;
    }

    /**
     * Gets the Direction that, when applied to Point {@code a}, will yield Point {@code b}.
     * Points {@code a} and {@code b} have to be adjacent.
     * @param a The first Point
     * @param b The second Point
     * @return The determined Direction
     */
    public static Direction getDirectionTo(final Point a, final Point b) {
        int dx = b.getBlockX() - a.getBlockX();
        int dz = b.getBlockZ() - a.getBlockZ();
        for (Direction d : Direction.values()) {
            if (d.dx == dx && d.dz == dz) {
                return d;
            }
        }
        throw new IllegalArgumentException("Point B is not adjacent to Point A");
    }

}
