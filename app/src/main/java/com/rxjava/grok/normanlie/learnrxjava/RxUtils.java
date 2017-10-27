package com.rxjava.grok.normanlie.learnrxjava;

import android.databinding.ObservableField;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

import static android.databinding.Observable.OnPropertyChangedCallback;

/**
 * Created by Norman Lie on 10/19/2017.
 */

public class RxUtils {

    public RxUtils() {

    }

    public static <T> Observable<T> toObservable(@NonNull final ObservableField<T> observableField) {
        return toObs(observableField)
                .filter(observableField1 -> observableField1.get() != null)
                .map(ObservableField::get);
    }

    public static <T>Observable<ObservableField<T>> toObs(@NonNull final ObservableField<T> observableField) {
        return Observable.create(emitter -> {
            final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable observable, int i) {
                    if(observable == observableField) {
                        emitter.onNext(observableField);
                    }
                }
            };

            observableField.addOnPropertyChangedCallback(callback);

            emitter.setCancellable(() -> observableField.removeOnPropertyChangedCallback(callback));

        });

    }
}
