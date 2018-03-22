package application.job.arsfutura.newsapp.Activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import application.job.arsfutura.newsapp.Adapters.MainAdapter;
import application.job.arsfutura.newsapp.Data.GetApiData;
import application.job.arsfutura.newsapp.Models.Root;
import application.job.arsfutura.newsapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public LinearLayoutManager mLayoutManager;
    Toolbar toolbar;
    ImageButton button;
    Root searchResults;
    int currentPageIndex = 1;
    private GetApiData apiData;
    private MainAdapter mainAdapter;
    private EditText searchEditText;
    private SwipeRefreshLayout swipeContainer;
    private TextView noDataTextView;
    private RecyclerView recyclerView;
    private String searchParameter;
    private TextView loadingDataTextView;
    private boolean buttonImageClear = false;
    View.OnClickListener imgButtonHandler = new View.OnClickListener() {

        public void onClick(View v) {
            if (buttonImageClear == true) {
                searchEditText.getText().clear();
                button.setImageResource(R.drawable.ic_search_black_24dp);
                swipeContainer.setEnabled(true);
                buttonImageClear = false;
                mainAdapter.refreshAllData();
                noDataTextView.setVisibility(View.GONE);
            }
        }
    };
    private boolean isDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
        setupRecycler();
        startDownloading();
        setSupportActionBar(toolbar);
        setSearchEditTextListener();

        button.setOnClickListener(imgButtonHandler);
        searchScrollObserver(recyclerView);
        setRefreshView();
    }

    private void startDownloading() {
        apiData = new GetApiData();
        if (isOnline() == true) {
            downloadTopHeadlinesData();
            downloadLatestData();
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
        }
    }

    private void initialization() {
        noDataTextView = (TextView) findViewById(R.id.noDataTextView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        loadingDataTextView = (TextView) findViewById(R.id.loadingDataTextView);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        button = (ImageButton) findViewById(R.id.imageButton);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        searchEditText = (EditText) findViewById(R.id.editSearch);
    }

    private void setupRecycler() {
        mainAdapter = new MainAdapter(this);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void setRefreshView() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (buttonImageClear == false) {
                    downloadTopHeadlinesData();
                    downloadLatestData();
                }
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void downloadTopHeadlinesData() {
        apiData.getTopHeadlinesData(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                mainAdapter.refreshTopHeadlinesData(response.body());
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.no_data, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void downloadLatestData() {
        apiData.getLatestNewsData(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                mainAdapter.refreshLatestNewsData(response.body());
                recyclerView.setVisibility(View.VISIBLE);
                loadingDataTextView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.no_data, Toast.LENGTH_LONG).show();
                loadingDataTextView.setVisibility(View.GONE);
            }
        });
    }

    private void downloadSearchData(String searchParameter) {
        this.searchParameter = searchParameter;
        if (isDownloading)
            return;
        isDownloading = true;
        apiData.getSearchResultsData(searchParameter, currentPageIndex, new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                currentPageIndex++;
                if (searchResults == null) {
                    searchResults = response.body();
                } else {
                    searchResults.articles.addAll(response.body().articles);
                }
                mainAdapter.refreshSearchData(searchResults);
                isDownloading = false;

                if (searchResults.articles.size() == 0) {
                    noDataTextView.setVisibility(View.VISIBLE);
                } else {
                    noDataTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.no_data, Toast.LENGTH_LONG).show();
                isDownloading = false;
            }
        });
    }

    private void setSearchEditTextListener() {
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_HOME) {
                    if (searchEditText.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.enter_text, Toast.LENGTH_LONG).show();
                        return true;
                    }
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    searchResults = null;
                    downloadSearchData(searchEditText.getText().toString());
                    button.setImageResource(R.drawable.ic_clear_black_24dp);
                    buttonImageClear = true;
                    swipeContainer.setEnabled(false);
                    return true;
                }
                return false;
            }
        });
    }

    private void searchScrollObserver(RecyclerView mRecyclerView) {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = mLayoutManager.getChildCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (searchResults != null && mainAdapter.currentMode == MainAdapter.MODE_SEARCH && visibleItemCount + pastVisiblesItems > searchResults.getArticles().size() - 5) {
                    downloadSearchData(searchParameter);
                }
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
