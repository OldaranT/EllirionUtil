package com.ellirion.util.model;

import lombok.Getter;
import com.ellirion.util.model.Direction;

public enum DirectionChange {
    NONE(0),
    LEFT(-1),
    RIGHT(1),
    REVERSE(Integer.MAX_VALUE);

    @Getter private int balance;

    DirectionChange(final int balance) {
        this.balance = balance;
    }

    /**
     * Applies this DirectionChange to the given Direction {@code d}.
     * @param d The Direction to apply this change to.
     * @return The resulting Direction
     */
    public Direction apply(final Direction d) {
        switch (this) {
            case LEFT:
                return d.getLeft();
            case RIGHT:
                return d.getRight();
            case REVERSE:
                return d.getReverse();
            default:
                return Direction.NONE;
        }
    }

}
