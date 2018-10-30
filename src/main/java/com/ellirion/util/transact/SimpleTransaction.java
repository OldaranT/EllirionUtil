package com.ellirion.util.transact;

import lombok.Getter;
import lombok.Setter;
import com.ellirion.util.async.Promise;

import java.util.function.Supplier;

public final class SimpleTransaction extends Transaction {

    @Getter @Setter private Supplier<Promise<Boolean>> applier;
    @Getter @Setter private Supplier<Promise<Boolean>> reverter;

    /**
     * Constructs a new SimpleTransaction without any specified
     * applier and reverter functions. These must still be specified
     * before {@code apply()} and {@code revert()} can be used.
     */
    public SimpleTransaction() {
        this(null, null);
    }

    /**
     * Construct a new SimpleTransaction that uses the given {@code applier} and
     * {@code reverter} functions as applier and reverter respectively.
     * @param applier The applier function to use
     * @param reverter The reverter function to use
     */
    public SimpleTransaction(final Supplier<Promise<Boolean>> applier,
                             final Supplier<Promise<Boolean>> reverter) {
        this.applier = applier;
        this.reverter = reverter;
    }

    @Override
    protected Promise<Boolean> applier() {
        if (applier == null) {
            throw new RuntimeException("SimpleTransaction cannot apply without an applier function");
        }
        return applier.get();
    }

    @Override
    protected Promise<Boolean> reverter() {
        if (reverter == null) {
            throw new RuntimeException("SimpleTransaction cannot revert without a reverter function");
        }
        return reverter.get();
    }
}
