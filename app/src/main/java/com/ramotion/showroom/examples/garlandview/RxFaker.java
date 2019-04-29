package com.ramotion.showroom.examples.garlandview;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

import io.bloco.faker.Faker;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

public class RxFaker {

    private static ReplaySubject<Faker> rxFaker = createFaker();

    public interface FakerReadyListener {
        void onFakerReady(Faker faker);
    }

    private static ReplaySubject<Faker> createFaker() {
        final ReplaySubject<Faker> subject = ReplaySubject.create();

        Observable.create(new ObservableOnSubscribe<Faker>() {
            @Override
            public void subscribe(ObservableEmitter<Faker> e) throws Exception {
                final Faker faker = new Faker();

                if (!e.isDisposed()) {
                    e.onNext(faker);
                    e.onComplete();
                }
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subject);

        return subject;
    }

    private static void subscriberListener(@NonNull final WeakReference<FakerReadyListener> refListener) {
        final Disposable disposable = getInstance().subscribe(new Consumer<Faker>() {
            @Override
            public void accept(Faker faker) throws Exception {
                final FakerReadyListener listener = refListener.get();
                if (listener != null) {
                    listener.onFakerReady(faker);
                }
            }
        });
    }

    private RxFaker() {
    }

    public static ReplaySubject<Faker> getInstance() {
        if (rxFaker == null) {
            rxFaker = createFaker();
        }

        return rxFaker;
    }

    public static void addListener(@NonNull FakerReadyListener listener) {
        subscriberListener(new WeakReference<>(listener));
    }

}
