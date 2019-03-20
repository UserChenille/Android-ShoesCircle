package com.zjzf.shoescircle.lib.net.retrofit;

import io.reactivex.Observable;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static com.zjzf.shoescircle.lib.net.retrofit.Preconditions.checkNotNull;


public class RxLifecycle {

    private RxLifecycle() {
        throw new AssertionError("No instances");
    }



    @CheckReturnValue
    public static <T, R> LifecycleTransformer<T> bindUntilEvent( final Observable<R> lifecycle,
                                                                 final R event) {
        checkNotNull(lifecycle, "lifecycle == null");
        checkNotNull(event, "event == null");
        return bind(takeUntilEvent(lifecycle, event));
    }

    private static <R> Observable<R> takeUntilEvent(final Observable<R> lifecycle, final R event) {
        return lifecycle.filter(new Predicate<R>() {
            @Override
            public boolean test(R lifecycleEvent) throws Exception {
                return lifecycleEvent.equals(event);
            }
        });
    }


    @CheckReturnValue
    public static <T, R> LifecycleTransformer<T> bind(final Observable<R> lifecycle) {
        return new LifecycleTransformer<>(lifecycle);
    }


    @CheckReturnValue
    public static <T, R> LifecycleTransformer<T> bind( Observable<R> lifecycle,
                                                      final Function<R, R> correspondingEvents) {
        checkNotNull(lifecycle, "lifecycle == null");
        checkNotNull(correspondingEvents, "correspondingEvents == null");
        return bind(takeUntilCorrespondingEvent(lifecycle.share(), correspondingEvents));
    }

    private static <R> Observable<Boolean> takeUntilCorrespondingEvent(final Observable<R> lifecycle,
                                                                       final Function<R, R> correspondingEvents) {
        return Observable.combineLatest(
                lifecycle.take(1).map(correspondingEvents),
                lifecycle.skip(1),
                new BiFunction<R, R, Boolean>() {
                    @Override
                    public Boolean apply(R bindUntilEvent, R lifecycleEvent) throws Exception {
                        return lifecycleEvent.equals(bindUntilEvent);
                    }
                })
                .onErrorReturn(Functions.RESUME_FUNCTION)
                .filter(Functions.SHOULD_COMPLETE);
    }
}
