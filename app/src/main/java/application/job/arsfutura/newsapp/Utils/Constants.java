package application.job.arsfutura.newsapp.Utils;

public class Constants {
    public static final String APIKEY = "aaaf1b622e18425fa280d559444922e5";
    public static final String TOPHEADLINESURL = "/v2/top-headlines?sources=bbc-news&pageSize=10&page=1&apiKey=" + APIKEY;
    public static final String LATESTNEWSURL = "/v2/top-headlines?country=us&apiKey=" + APIKEY;
    public static final String SEARCHRESULTSURL = "/v2/everything?pageSize=10&apiKey=" + APIKEY;
    public static final String BASEURL = "https://newsapi.org";
}
