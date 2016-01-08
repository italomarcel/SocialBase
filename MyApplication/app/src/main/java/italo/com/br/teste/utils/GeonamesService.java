package italo.com.br.teste.utils;

import italo.com.br.teste.model.Geonames;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GeonamesService {
    @GET("citiesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&lang=de&username=demo")
    Call<Geonames> getGeonames();
}