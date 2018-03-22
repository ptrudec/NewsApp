package application.job.arsfutura.newsapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import application.job.arsfutura.newsapp.Models.Root;
import application.job.arsfutura.newsapp.R;

public class LatestNewsAdapter extends RecyclerView.Adapter<LatestNewsAdapter.MyViewHolder> {
    Root data;
    Context context;

    public LatestNewsAdapter() {
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_news_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String formatedText = data.articles.get(position).getPublishedAt() + " by <font color=\"" + context.getResources().getColor(R.color.latest_news_source_color) + "\">" + data.articles.get(position).getSource().getName() + "</font>";
        holder.latestNewsSource.setText(Html.fromHtml(formatedText));
        holder.latestNewsTitle.setText(data.articles.get(position).getTitle());
        holder.latestNewsText.setText(data.articles.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        if (data != null && data.articles.size() > 0) {
            return data.articles.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView latestNewsTitle, latestNewsText, latestNewsSource;

        public MyViewHolder(View itemView) {
            super(itemView);
            latestNewsTitle = (TextView) itemView.findViewById(R.id.latest_news_article_title);
            latestNewsText = (TextView) itemView.findViewById(R.id.latest_news_text);
            latestNewsSource = (TextView) itemView.findViewById(R.id.latest_news_time);
        }
    }
}
