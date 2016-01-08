package italo.com.br.teste.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmObject;
import italo.com.br.teste.R;
import italo.com.br.teste.model.Geoname;
import italo.com.br.teste.model.Geonames;
import italo.com.br.teste.utils.GeonamesService;
import italo.com.br.teste.view.HomeFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    // usado na conversao do resultado para classe model
    final Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getDeclaringClass().equals(RealmObject.class);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .create();
    // lib usada para requisicao REST, criando url base para os requests
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.geonames.org")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    GeonamesService service = retrofit.create(GeonamesService.class);
    Call<Geonames> names = service.getGeonames();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //executando request
        names.enqueue(new Callback<Geonames>() {
            @Override
            public void onResponse(Response<Geonames> response) {

                Realm realm = Realm.getInstance(getApplicationContext());
                if (response.isSuccess()) {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(response.body().getGeonames());
                    realm.commitTransaction();
                } else if (realm.getTable(Geoname.class).isEmpty()) {
                    //tabela vazia, avisa e fecha por ser uma demo, certo seria colocar um retry automatico
                    Toast.makeText(MainActivity.this, R.string.fail_empty, Toast.LENGTH_LONG).show();
                    finish();
                }
                realm.close();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, new HomeFragment()).commit();


            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                //Falha ao conectar com servidor, fechando por ser demo, certo seria verificar internet e coisas do tipo
                Toast.makeText(MainActivity.this, R.string.fail_connect, Toast.LENGTH_LONG).show();
                finish();
            }

        });

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

}
