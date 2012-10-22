package models;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import play.Logger;
import play.data.validation.Max;
import play.data.validation.MaxSize;
import play.data.validation.Min;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.URL;

@Entity(noClassnameStored = true)
public class Item extends BaseModel {

    @Required
    @MinSize(1)
    @MaxSize(500)
    public String name;
    @MaxSize(20000)
    public String desc;
    @MaxSize(500)
    public String brand;
    @MaxSize(500)
    public String label;
    @Min(0.0d)
    public Double price;
    @URL
    @MaxSize(50000)
    public String url;
    @MaxSize(500)
    public String company;
    public List<ImageInfo> images;
    /* I am not sure this is the right place for stars, each item is going 
    to be a different entry in the DB. */
    @Min(0.0d)
    @Max(5.0d)
    public Double stars;
    @Reference
    public Search search;

    private Item() {
        this.stars = 0.0d;
    }

    public Item(String name) {
        this();
        this.name = name;
    }

    public Item(String name, Search search) {
        this(name);
        this.search = search;
    }

    public void addImageInfo(ImageInfo img) {
        if (null == this.images) {
            this.images = new ArrayList<ImageInfo>();
        }
        if (null != img && (StringUtils.isNotBlank(img.small)
                || StringUtils.isNotBlank(img.medium)
                || StringUtils.isNotBlank(img.large))) {
            this.images.add(img);
        }
    }

    public String faviconUrl() {
        String fav = null;
        try {
            java.net.URL fullUrl = new java.net.URL(this.url);

            fav = StringUtils.join(new String[]{"http://www.google.com/s2/favicons?domain=", fullUrl.getHost()});

        } catch (MalformedURLException ex) {
            Logger.error(ex, "cannot get favicon URL %s", this.url);
        }
        return fav;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
