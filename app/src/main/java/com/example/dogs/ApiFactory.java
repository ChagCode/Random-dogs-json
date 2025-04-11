package com.example.dogs;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    // для создания экземпляра ApiService нам необходим Retrofit
    private static ApiService apiService;
    private static final String BASE_URL = "https://dog.ceo/api/breeds/image/";

    public static ApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // BASE_URL, на который мы будем отправлять запросы
                    .addConverterFactory(GsonConverterFactory.create()) // преобразование объектов
                                        // с помощью библиотеки - GsonConverterFactory
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // для совместной
                    // работы RxJava и Retrofit используем библиотеку - RxJava3CallAdapterFactory
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}
