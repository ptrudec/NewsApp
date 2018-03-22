package application.job.arsfutura.newsapp.Adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import application.job.arsfutura.newsapp.Models.Root;
import application.job.arsfutura.newsapp.R;

public class TopHeadlinesAdapter extends RecyclerView.Adapter<TopHeadlinesAdapter.MyViewHolder> {
    Root data;

    public TopHeadlinesAdapter() {
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_headlines_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.headlineSource.setText(data.articles.get(position).getSource().getName());
        holder.headlineTitle.setText(data.articles.get(position).getTitle());
        holder.headlineTime.setText(data.articles.get(position).getPublishedAt());
        holder.headlineImage.setImageURI(Uri.parse(data.articles.get(position).getUrlToImage()));
    }

    @Override
    public int getItemCount() {
        if(data!=null && data.articles.size()>0){
            return data.articles.size();
        }
       return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView headlineTitle, headlineSource, headlineTime;
        ImageView headlineImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            headlineTitle = (TextView) itemView.findViewById(R.id.headline_article_title);
            headlineSource = (TextView) itemView.findViewById(R.id.headline_source);
            headlineTime = (TextView) itemView.findViewById(R.id.headline_time);
            headlineImage = (SimpleDraweeView) itemView.findViewById(R.id.headline_image);
        }
    }
}
