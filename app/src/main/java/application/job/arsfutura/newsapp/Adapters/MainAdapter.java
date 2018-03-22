package application.job.arsfutura.newsapp.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import application.job.arsfutura.newsapp.Models.Root;
import application.job.arsfutura.newsapp.R;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int MODE_REGULAR = 101;
    public static final int MODE_SEARCH = 102;
    private final int SIZEROWS = 2;
    private final int VERTICAL = 1;
    private final int HORIZONTAL = 0;
    public int currentMode = MODE_REGULAR;
    private Root data;
    private LatestNewsAdapter latestNewsAdapter;
    private TopHeadlinesAdapter topHeadlinesAdapter;
    private Context context;

    public MainAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        if (currentMode == MODE_SEARCH) {
            view = inflater.inflate(R.layout.latest_news_item, parent, false);
            holder = new SearchHolder(view);
        } else {
            switch (viewType) {
                case VERTICAL:
                    view = inflater.inflate(R.layout.latest_news_layout, parent, false);
                    holder = new LatestNewsHolder(view);
                    break;
                case HORIZONTAL:
                    view = inflater.inflate(R.layout.top_headlines_layout, parent, false);
                    holder = new TopHeadlinesHolder(view);
                    break;

                default:
                    view = inflater.inflate(R.layout.top_headlines_layout, parent, false);
                    holder = new TopHeadlinesHolder(view);
                    break;
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (currentMode == MODE_SEARCH) {
            searchView((SearchHolder) holder, position);
        } else if (holder.getItemViewType() == VERTICAL)
            latestNewsView((LatestNewsHolder) holder);
        else if (holder.getItemViewType() == HORIZONTAL)
            topHeadlinesView((TopHeadlinesHolder) holder);
    }

    private void latestNewsView(LatestNewsHolder holder) {
        if (latestNewsAdapter == null) {
            latestNewsAdapter = new LatestNewsAdapter();
        }
        holder.innerRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.innerRecyclerView.setAdapter(latestNewsAdapter);
    }

    private void topHeadlinesView(TopHeadlinesHolder holder) {
        if (topHeadlinesAdapter == null) {
            topHeadlinesAdapter = new TopHeadlinesAdapter();
        }
        holder.innerRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.innerRecyclerView.setAdapter(topHeadlinesAdapter);
    }

    private void searchView(SearchHolder holder, int position) {
        String formatedText = data.articles.get(position).getPublishedAt() + " by <font color=\"" + context.getResources().getColor(R.color.latest_news_source_color) + "\">" + data.articles.get(position).getSource().getName() + "</font>";
        holder.latestNewsSource.setText(Html.fromHtml(formatedText));
        holder.latestNewsTitle.setText(data.articles.get(position).getTitle());
        holder.latestNewsText.setText(data.articles.get(position).getDescription());
        if (position == 0) {
            holder.topTitle.setVisibility(View.VISIBLE);
        } else {
            holder.topTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (currentMode == MODE_SEARCH) {
            return data.articles.size();
        }
        return SIZEROWS;
    }

    @Override
    public int getItemViewType(int position) {
        if (currentMode == MODE_SEARCH) {
            return MODE_SEARCH;
        }
        if (position == 1)
            return VERTICAL;
        if (position == 0)
            return HORIZONTAL;
        return -1;
    }

    public void refreshTopHeadlinesData(Root data) {
        currentMode = MODE_REGULAR;
        topHeadlinesAdapter.data = data;
        topHeadlinesAdapter.notifyDataSetChanged();
    }

    public void refreshAllData() {
        currentMode = MODE_REGULAR;
        this.notifyDataSetChanged();
        latestNewsAdapter.notifyDataSetChanged();
        topHeadlinesAdapter.notifyDataSetChanged();
    }

    public void refreshLatestNewsData(Root data) {
        currentMode = MODE_REGULAR;
        latestNewsAdapter.data = data;
        latestNewsAdapter.notifyDataSetChanged();
    }

    public void refreshSearchData(Root data) {
        currentMode = MODE_SEARCH;
        this.data = data;
        this.notifyDataSetChanged();
    }

    public class TopHeadlinesHolder extends RecyclerView.ViewHolder {
        RecyclerView innerRecyclerView;

        TopHeadlinesHolder(View itemView) {
            super(itemView);
            innerRecyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
        }
    }

    public class LatestNewsHolder extends RecyclerView.ViewHolder {
        RecyclerView innerRecyclerView;

        LatestNewsHolder(View itemView) {
            super(itemView);
            innerRecyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
        }
    }

    public class SearchHolder extends RecyclerView.ViewHolder {
        TextView topTitle, latestNewsTitle, latestNewsText, latestNewsSource;

        public SearchHolder(View itemView) {
            super(itemView);
            topTitle = (TextView) itemView.findViewById(R.id.top_title);
            latestNewsTitle = (TextView) itemView.findViewById(R.id.latest_news_article_title);
            latestNewsText = (TextView) itemView.findViewById(R.id.latest_news_text);
            latestNewsSource = (TextView) itemView.findViewById(R.id.latest_news_time);
        }
    }
}
