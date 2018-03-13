package com.xinlan.rxdemoshow;

import android.util.Log;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public static final String TAG = "RxJava Research";

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSingle(){
        String input = "input1";
        System.out.println("start");
        Single<String> single = Single.just(input).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(new Function<String,String>(){
                    @Override
                    public String apply(String s) throws Exception {
                        System.out.println("Runing on Thread = "+Thread.currentThread().getName());
                        s = s+"#" +s;
                        Thread.sleep(1000);
                        return s;
                    }
                }).doOnSuccess(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("Runing on Thread = "+Thread.currentThread().getName());
                        System.out.println("result = "+s);
                    }
                });



        single.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("Runing on Thread = "+Thread.currentThread().getName());
                System.out.println("result = "+s);
            }
        });
        System.out.println("Runing on Thread = "+Thread.currentThread().getName());

        Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> e) throws Exception {
                    e.onSuccess("Hello World");
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return s+"###"+s;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("success = "+s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("error ");
            }
        });
    }
    @Test
    public void singleTest2(){

        Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> e) throws Exception {
                e.onSuccess("Hello World");
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                System.out.println("Runing on Thread = "+Thread.currentThread().getName());
                Thread.sleep(1000);
                return s+" ### "+s;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("success = "+s);
                System.out.println("success Runing on Thread = "+Thread.currentThread().getName());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("error ");
            }
        });
    }

    @Test
    public void rxjavaMain() throws Exception {
        //创建一个上游 Observable
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>(){
            @Override
            public void subscribe(ObservableEmitter<Integer> emmiter) throws Exception {
                emmiter.onNext(100);
                emmiter.onNext(200);
                emmiter.onNext(300);

                emmiter.onComplete();

                emmiter.onNext(400);
            }
        } );

        //创建一个下游 Observer
        Observer<Integer> observer = new Observer<Integer>(){
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("subscribe");
            }

            @Override
            public void onNext(Integer value) {
                System.out.println("" + value);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
            }
            @Override
            public void onComplete() {
                System.out.println("complete");
            }
        };

        observable.subscribe(observer);
    }
}