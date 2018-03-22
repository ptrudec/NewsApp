package application.job.arsfutura.newsapp.Models;


import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Locale;

public class Articles {

    private String urlToImage, title, publishedAt, description;
    private Source source;

    public Articles(String urlToImage, String title, String publishedAt, String description, Source source) {
        this.urlToImage = urlToImage;
        this.title = title;
        this.publishedAt = publishedAt;
        this.description = description;
        this.source = source;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedAt() {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ").withLocale(Locale.getDefault());
            DateTime dateTime = dateTimeFormatter.parseDateTime(publishedAt);

            Period period = new Period(dateTime, new DateTime());

            PeriodFormatterBuilder builder = new PeriodFormatterBuilder();
            if (period.getYears() != 0) {
                builder.appendYears().appendSuffix(" years ago\n");
            } else if (period.getMonths() != 0) {
                builder.appendMonths().appendSuffix(" months ago\n");
            } else if (period.getDays() != 0) {
                builder.appendDays().appendSuffix(" days ago\n");
            } else if (period.getHours() != 0) {
                builder.appendHours().appendSuffix(" hours ago\n");
            } else if (period.getMinutes() != 0) {
                builder.appendMinutes().appendSuffix(" minutes ago\n");
            } else if (period.getSeconds() != 0) {
                builder.appendSeconds().appendSuffix(" seconds ago\n");
            }

            PeriodFormatter formatter = builder.printZeroNever().toFormatter();
            String elapsed = formatter.print(period);
            return elapsed;
        } catch (IllegalArgumentException e) {
            return publishedAt.toString();
        }
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
