package com.example.dogs;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface ApiService {
    @GET("random") // указывает тип запроса, что мы хотим от сети (т.е. получить картинку)
    // random - end point (конечная часть) базового запроса https://dog.ceo/api/breeds/image/
    Single<DogImage> loadDogImage(); // метод загружает изображение собаки
}
