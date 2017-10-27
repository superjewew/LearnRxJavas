package com.rxjava.grok.normanlie.learnrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;

import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

public class Example3Activity extends AppCompatActivity {

    private TextView mCounterDisplay;
    private Button mIncrementButton;
    private PublishSubject<Integer> mCounterEmitter;

    private int mCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        createCounterEmitter();
    }

    private void createCounterEmitter() {
        mCounterEmitter = PublishSubject.create();
        mCounterEmitter.subscribe(
                this::setCounterText
        );

    }

    void setCounterText(int integer) {
        mCounterDisplay.setText(String.valueOf(integer));
    }

    void configureLayout() {
        setContentView(R.layout.activity_example3);
        mCounterDisplay = findViewById(R.id.counter_display);
        mCounterDisplay.setText(String.valueOf(mCounter));
        mIncrementButton = findViewById(R.id.increment_button);
        mIncrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onIncrementButtonClick();
            }
        });
    }

    private void onIncrementButtonClick() {
        mCounter++;
        mCounterEmitter.onNext(mCounter);
    }
}
