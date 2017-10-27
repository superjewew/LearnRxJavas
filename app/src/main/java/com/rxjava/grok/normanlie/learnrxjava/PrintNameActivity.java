package com.rxjava.grok.normanlie.learnrxjava;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rxjava.grok.normanlie.learnrxjava.databinding.ActivityPrintNameBinding;

import io.reactivex.Observable;

import static com.rxjava.grok.normanlie.learnrxjava.RxUtils.toObservable;

public class PrintNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPrintNameBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_print_name);
        binding.setViewModel(new MainViewModel());
        binding.executePendingBindings();
    }

    public static class MainViewModel {
        public ObservableField<String> firstName = new ObservableField<>();
        public ObservableField<String> lastName = new ObservableField<>();
        public ObservableField<String> helloText = new ObservableField<>();
        public ObservableBoolean helloButtonEnabled = new ObservableBoolean(false);

        public MainViewModel() {
            Observable.combineLatest(toObservable(firstName), toObservable(lastName),
                    (firstName, lastName) -> StringUtils.isNotNullOrEmpty(firstName) && StringUtils.isNotNullOrEmpty(lastName))
                    .subscribe(result -> {
                        helloButtonEnabled.set(result);
                        if(!result) {
                            helloText.set(StringUtils.EMPTY);
                        }
                    }, Throwable::printStackTrace);
        }

        public void buttonClicked() {
            helloText.set(String.format("Hello %s %s !", firstName.get(), lastName.get()));
        }
    }
}
