package com.mclinica.taskmaster;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by TrieNoir on 24/11/2016.
 */

public abstract class TaskMaster<T>  {

    private static final String LOG = " || STURF || ";

    private Observable<T> sObservable;
    private Subscriber<T> sSubscriber;
    private boolean isCancelled = false;

    public TaskMaster() {

        onStart();

        sObservable = Observable.defer(new Func0<Observable<T>>() {
            @Override
            public Observable<T> call() {
                return Observable.just(onProcess());
            }
        })
                .filter(new Func1<T, Boolean>() {
                    @Override
                    public Boolean call(T t) {
                        return t != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        sSubscriber = new Subscriber<T>() {
            @Override
            public void onCompleted() {
                Log.e(LOG, "onCompleted -> OK");
                cancel(true);
            }

            @Override
            public void onError(Throwable throwable) {

                Log.e(LOG, "onError -> " + throwable.getMessage());
            }

            @Override
            public void onNext(T t) {
                onEnd(t);
                Log.e(LOG, "OnNext -> " + t.toString());
            }
        };
    }

    protected abstract void onStart();
    protected abstract T onProcess();
    protected abstract void onEnd(T t);

    public void cancel(boolean isGoingToCancel) {
        if (sSubscriber != null && isGoingToCancel) {
            isCancelled = true;
            sSubscriber.unsubscribe();
        }
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void execute() {
        if (sObservable != null && sSubscriber != null) sObservable.subscribe(sSubscriber);
    }
}
