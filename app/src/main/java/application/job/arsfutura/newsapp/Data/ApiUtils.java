package application.job.arsfutura.newsapp.Data;

import application.job.arsfutura.newsapp.Models.Root;
import application.job.arsfutura.newsapp.Utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ApiUtils {

    public static <T> T createService(Class<T> hideMeServiceClass, OkHttpClient client, String endPoint) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(endPoint)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(hideMeServiceClass);
    }

    public interface GetNews {
        @GET(Constants.LATESTNEWSURL)
        Call<Root> getLatestNews();

        @GET(Constants.TOPHEADLINESURL)
        Call<Root> getTopHeadlines();

        @GET(Constants.SEARCHRESULTSURL)
        Call<Root> getSearchResults(@Query("q") String searchParameter, @Query("page") int pageNumber);
    }
}
