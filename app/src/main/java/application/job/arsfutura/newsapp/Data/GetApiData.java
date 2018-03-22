package application.job.arsfutura.newsapp.Data;

import application.job.arsfutura.newsapp.Models.Root;
import application.job.arsfutura.newsapp.Utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Callback;

public class GetApiData {
    ApiUtils.GetNews getNews;

    public GetApiData() {
        OkHttpClient client = new OkHttpClient();
        getNews = ApiUtils.createService(ApiUtils.GetNews.class, client, Constants.BASEURL);
    }

    public void getTopHeadlinesData(Callback<Root> callback) {
        getNews.getTopHeadlines().enqueue(callback);
    }

    public void getLatestNewsData(Callback<Root> callback) {
        getNews.getLatestNews().enqueue(callback);
    }

    public void getSearchResultsData(String searchParameter, int pageNumber, Callback<Root> callback) {
        getNews.getSearchResults(searchParameter, pageNumber).enqueue(callback);
    }
}
