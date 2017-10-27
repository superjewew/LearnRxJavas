package com.rxjava.grok.normanlie.learnrxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class Example2Activity extends AppCompatActivity {

    RestClient mRestClient;
    RecyclerView mTvShowListView;
    ProgressBar mProgressBar;
    SimpleStringAdapter mSimpleStringAdapter;
    TextView mErrorText;
    private DisposableObserver<List<String>> mTvShowSubscription;
    private DisposableSingleObserver<List<String>> mTvShowSingleObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestClient = new RestClient(this);
        configureLayout();
        createSingleObservable();
    }

    private void createObservable() {
        mTvShowSubscription = Observable
                .fromCallable(() -> mRestClient.getFavoriteTvShows())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<String>>() {
                    @Override
                    public void onNext(List<String> strings) {
                        displayTvShows(strings);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void createSingleObservable() {
        Single<List<String>> tvShowSingle = Single.fromCallable(() -> mRestClient.getFavoriteTvShowsWithException());

        mTvShowSingleObserver = tvShowSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(List<String> strings) {
                        displayTvShows(strings);
                    }

                    @Override
                    public void onError(Throwable e) {
                        displayErrorMessage();
                    }
                });
    }

    private void displayErrorMessage() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorText.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTvShowSubscription != null && !mTvShowSubscription.isDisposed()) {
            mTvShowSubscription.dispose();
        }

        if (mTvShowSingleObserver != null && !mTvShowSingleObserver.isDisposed()) {
            mTvShowSingleObserver.dispose();
        }
    }

    private void displayTvShows(List<String> strings) {
        mSimpleStringAdapter.setStrings(strings);
        mProgressBar.setVisibility(View.INVISIBLE);
        mTvShowListView.setVisibility(View.VISIBLE);
    }

    private void configureLayout() {
        setContentView(R.layout.activity_example2);
        mTvShowListView = findViewById(R.id.tv_show_list);
        mProgressBar = findViewById(R.id.loader);
        mErrorText = findViewById(R.id.error_message);
        mTvShowListView.setLayoutManager(new LinearLayoutManager(this));
        mSimpleStringAdapter = new SimpleStringAdapter(this);
        mTvShowListView.setAdapter(mSimpleStringAdapter);
    }
}
