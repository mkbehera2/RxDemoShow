package com.xinlan.rxdemoshow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //doTest();
        test2();
    }


    private void test2(){
        String[] array = new String[]{"Hello World", "ni hao  shi jie"};

        Observable.fromArray(array).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println(" onSubscribe");
            }

            @Override
            public void onNext(String value) {
                System.out.println(value);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error");
            }

            @Override
            public void onComplete() {
                System.out.println("on complete");
            }
        });

//        Single<String> sin = Single.just(array).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .map(new Function<String[], String>() {
//                    @Override
//                    public String apply(String[] params) throws Exception {
//                        String content = params[0]+"  "+params[1];
//                        return content;
//                    }
//                }).doOnSubscribe()

    }

    private void doTest(){
        Single task = Single.create(new SingleOnSubscribe() {
            @Override
            public void subscribe(SingleEmitter e) throws Exception {
                System.out.println("Runing on Thread = "+Thread.currentThread().getName());
                e.onSuccess("Hello");
            }
        }).subscribeOn(Schedulers.io()).map(new Function<String,String>(){

            @Override
            public String apply(String s) throws Exception {
                System.out.println("Functtion on Thread = "+Thread.currentThread().getName());
                return s +"___"+s;
            }
        });
        task.observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                    System.out.println(o);
                    System.out.println("Runing on Thread = "+Thread.currentThread().getName());
            }
        });
    }
}
