package com.ellirion.util.transact;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import com.ellirion.util.async.Counter;
import com.ellirion.util.async.Promise;

import java.util.function.Supplier;

public abstract class Transaction {

    private Counter latch;
    @Getter private Promise<Boolean> promise;
    @Getter @Setter(AccessLevel.PROTECTED) private boolean applied;

    /**
     * Construct a new unapplied Transaction.
     */
    protected Transaction() {
        this(false);
    }

    /**
     * Construct a new Transaction that may already have been applied,
     * depending on the {@code applied} parameter.
     * @param applied Whether this Transaction has been applied
     */
    protected Transaction(final boolean applied) {
        this.latch = new Counter(applied ? 1 : 0);
        this.promise = Promise.resolve(true);
        this.applied = applied;
    }

    /**
     * Applies this Transaction and returns a Promise thereof.
     * @return The Promise for the result of this operation.
     */
    protected abstract Promise<Boolean> applier();

    /**
     * Applies this Transaction and returns a Promise thereof.
     * @return The Promise for the result of this operation.
     */
    protected abstract Promise<Boolean> reverter();

    /**
     * Apply this transaction.
     * @return Whether the operation succeeded or not.
     */
    public final Promise<Boolean> apply() {
        return perform(this::applier, true);
    }

    /**
     * Revert this transaction.
     * @return Whether the operation succeeded or not.
     */
    public final Promise<Boolean> revert() {
        return perform(this::reverter, false);
    }

    private Promise<Boolean> perform(Supplier<Promise<Boolean>> supplier, boolean becomesApplied) {
        // If someone forgot to set the applier or reverter functions, throw an exception.
        if (supplier == null) {
            throw new IllegalStateException("Cannot perform transaction operation without supplier");
        }

        // If there is any Promise still pending that will change our appliedness, wait for it.
        latch.perform(() -> {

            // Make sure we don't run twice simultaneously. It might seem that the fact
            // that the apply() and revert() methods are synchronized prevents this, but that's
            // not necessarily the case since the Promise may go unresolved for an extended period of time.
            latch.await(0);

            // After we've waited for the latch to reach zero, we can now
            // safely increment it *without* yielding the synchronized(latch) block.
            // This prevents another thread from getting control of latch between
            // our reading and writing of the value.
            latch.increment();

            // Now that we have ensured our ownership of the latch, we can
            // start by invoking our supplier to get a new Promise.

            // But first, make sure we don't try to apply when we're already
            // applied, or try to revert when we're not applied.
            if (applied == becomesApplied) {
                latch.decrement();
                throw new RuntimeException("Cannot apply when applied, or revert when not applied");
            }

            // Get a Promise produced by our supplier function. We store this Promise
            // so additional handlers can be added if desired.
            promise = supplier.get();

            // Once the Promise finishes in any way, we set pending to false and
            // applied to whatever boolean value was passed as becomesApplied.
            promise.always(() -> {
                // Acquire the lock, and then decrement it. This prevents another thread
                // from changing the value between our check and assignment.
                latch.perform(() -> {
                    applied = becomesApplied;
                    latch.decrement();
                });
            });
        });

        // And we return the Promise to the callee.
        return promise;
    }

    /**
     * Await the Promise of the last action this Transaction performed.
     * @return True if the Promise was resolved, or false if it was rejected
     */
    public boolean await() {
        return promise.await();
    }
}
