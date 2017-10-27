package com.rxjava.grok.normanlie.learnrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class SearchExampleActivity extends AppCompatActivity {

    RestClient mRestClient;
    EditText mSearchInput;
    TextView mNoResultsIndicator;
    RecyclerView mSearchResults;
    SimpleStringAdapter mSimpleStringAdapter;

    private PublishSubject<String> mSearchResultsSubject;
    private DisposableObserver mTextWatchObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestClient = new RestClient(this);
        configureLayout();
        createObservables();
        listenToSearchInput();
    }

    private void listenToSearchInput() {
        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSearchResultsSubject.onNext(editable.toString());
            }
        });
    }

    private void createObservables() {
        mSearchResultsSubject = PublishSubject.create();
        mTextWatchObserver = mSearchResultsSubject
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .map(s -> mRestClient.searchForCity(s))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<String>>() {
                    @Override
                    public void onNext(List<String> strings) {
                        handleSearchResult(strings);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTextWatchObserver != null && !mTextWatchObserver.isDisposed()) {
            mTextWatchObserver.dispose();
        }
    }

    private void handleSearchResult(List<String> strings) {
        if(strings == null) {
            mNoResultsIndicator.setVisibility(View.VISIBLE);
            mSearchResults.setVisibility(View.INVISIBLE);
        } else {
            mSimpleStringAdapter.setStrings(strings);
            mNoResultsIndicator.setVisibility(View.INVISIBLE);
            mSearchResults.setVisibility(View.VISIBLE);
        }
    }

    void configureLayout() {
        setContentView(R.layout.activity_search_example);
        mSearchInput = findViewById(R.id.search_input);
        mNoResultsIndicator = findViewById(R.id.no_results_indicator);
        mSearchResults = findViewById(R.id.search_results);
        mSearchResults.setLayoutManager(new LinearLayoutManager(this));
        mSimpleStringAdapter = new SimpleStringAdapter(this);
        mSearchResults.setAdapter(mSimpleStringAdapter);
    }

    
}
