package jongwon.lee.org.aanews;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class APIRequest {

    Context context;

    // New Retrofit object with gson
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Get API response
    public void getArticles(Listener listener, String category) {
        CallAPI callAPI = retrofit.create(CallAPI.class);

        // parameters (German general articles)
        Call<jongwon.lee.org.aanews.model.Response> call = callAPI.callArticles("de", category, context.getString(R.string.api_key));

        try {
            call.enqueue(new Callback<jongwon.lee.org.aanews.model.Response>() {
                @Override
                public void onResponse(Call<jongwon.lee.org.aanews.model.Response> call, Response<jongwon.lee.org.aanews.model.Response> response) {

                    // response error
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Articles could not be loaded.", Toast.LENGTH_SHORT).show();
                    }

                    // successful request
                    listener.fetch(response.body().getArticles(), response.message());
                }

                @Override
                public void onFailure(Call<jongwon.lee.org.aanews.model.Response> call, Throwable t) {

                    listener.error("Request failed.");

                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public APIRequest(Context context) {
        this.context = context;
    }

    // Interface: Call API with request parameters
    public interface CallAPI {
        @GET("top-headlines")
        Call<jongwon.lee.org.aanews.model.Response> callArticles(
                @Query("country") String country,
                @Query("category") String category,
                @Query("apiKey") String apiKey
        );
    }
}
