package com.zjzf.shoescircle.lib.helper;


import android.support.annotation.NonNull;
import android.widget.EditText;

import com.zjzf.shoescircle.lib.R;
import com.zjzf.shoescircle.lib.helper.rx.base.RxCall;
import com.zjzf.shoescircle.lib.helper.rx.base.RxCallImpl;
import com.zjzf.shoescircle.lib.helper.rx.subscriber.DefaultLogThrowableConsumer;
import com.zjzf.shoescircle.lib.utils.TextWatcherAdapter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxHelper {

    public static Disposable debounceListenEdittext(final EditText editText, long debounceDelay, final RxCall<String> call) {
        if (editText == null) return null;
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                TextWatcherAdapter adapter = (TextWatcherAdapter) editText.getTag(R.id.tag_edit_textwatchadapter);
                if (adapter == null) {
                    adapter = new TextWatcherAdapter() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            emitter.onNext(String.valueOf(s));
                        }
                    };
                    editText.addTextChangedListener(adapter);
                    editText.setTag(R.id.tag_edit_textwatchadapter, adapter);
                }
            }
        }).debounce(debounceDelay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (call != null) {
                            call.onCall(s);
                        }
                    }
                });
    }

    //-----------------------------------------transformer-----------------------------------------
    public static <T> ObservableTransformer<T, T> io_main() {

        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> FlowableTransformer<T, T> io_main_flowable() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> SingleTransformer<T, T> single_io_main() {

        return new SingleTransformer<T, T>() {

            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> computation_main() {

        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> SingleTransformer<T, T> single_computation_main() {

        return new SingleTransformer<T, T>() {

            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream.subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    //-----------------------------------------method-----------------------------------------
    public static Disposable runOnBackground(@NonNull final RxCall<Void> call) {
        return Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> emitter) throws Exception {
                if (call != null) {
                    call.onCall(null);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public static <T> Disposable runOnUiThread(@NonNull RxCall<T> call) {
        return runOnUiThread(call, new DefaultLogThrowableConsumer("RxRunOnUiThread"));
    }

    public static <T> Disposable runOnUiThread(@NonNull RxCall<T> call, @NonNull Consumer<Throwable> errorConsumer) {
        return Flowable.just(call)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RxCall<T>>() {
                    @Override
                    public void accept(RxCall<T> tiRxCall) throws Exception {
                        if (tiRxCall instanceof RxCallImpl) {
                            RxCallImpl<T> uiCall = ((RxCallImpl<T>) tiRxCall);
                            uiCall.onCall(uiCall.getData());
                        } else {
                            tiRxCall.onCall(null);
                        }
                    }
                }, errorConsumer);
    }


    public static Disposable delay(long delayTime, @NonNull RxCall<Long> call) {
        return delay(delayTime, TimeUnit.MILLISECONDS, call);

    }

    public static Disposable delay(long delayTime, TimeUnit unit, @NonNull RxCall<Long> call) {
        return delay(delayTime, unit, call, new DefaultLogThrowableConsumer());
    }

    public static Disposable delay(long delayTime, TimeUnit unit, @NonNull final RxCall<Long> call, @NonNull Consumer<Throwable> errorConsumer) {
        return Flowable.timer(delayTime, unit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        call.onCall(aLong);
                    }
                }, errorConsumer);
    }

}
