package com.example.dogs;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "TagMainViewModel";
    private MutableLiveData<DogImage> dogImage = new MutableLiveData<>(); // для подписки
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(); // для прогресс бара
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isError = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getIsError() {
        return isError;
    }

    public MutableLiveData<DogImage> getDogImage() {
        return dogImage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadDogImage() {
//        isLoading.setValue(true); // в момент загрузки установим значение true - не корректно
        Disposable disposable = loadDogImageRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // оператор реагирования
                // отвечает за регистрацию действия, которое будет выполнено в момент,
                // когда наблюдатель (Subscriber) подписывается на Observable
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        // метод отработает в момент подписки, а не в момент загрузки
                        // см. выше, так некорректно
                        isError.setValue(false); // для Toast - ошибки
                        isLoading.setValue(true);
                    }
                })
                // оператор реагирования отвечает за регистрацию действия,
                // которое будет выполнено сразу после завершения Observable,
                // независимо от того, завершилось оно успешно или с ошибкой.
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        // установим false, когда операция будет завершена
                        // независимо от результата
                        isLoading.setValue(false);
                    }
                })
                // оператор реагирования на ошибки
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        isError.setValue(true); // для Toast - ошибки
                    }
                })
                .subscribe(new Consumer<DogImage>() {
                    @Override
                    public void accept(DogImage image) throws Throwable {
                        dogImage.setValue(image);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "Error: " + throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }

    private Single<DogImage> loadDogImageRx() {
        return ApiFactory.getApiService().loadDogImage();
    }

    // и отменим все подписки
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
