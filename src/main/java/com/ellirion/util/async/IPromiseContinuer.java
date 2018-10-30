package com.ellirion.util.async;

@FunctionalInterface
public interface IPromiseContinuer<TResult, TNext> {

    /**
     * A function that is invoked upon the resolving or rejecting of a previous promise.
     * @param t The result of the previous Promise
     * @return Optionally a new Promise.
     */
    TNext run(TResult t);
}
