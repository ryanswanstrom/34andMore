package models;

import com.google.code.morphia.annotations.Entity;
import java.util.List;
import play.Logger;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;


@Entity(noClassnameStored=true)
public class Search extends BaseModel {
    @Required    
    @MinSize(1)
    @MaxSize(500)
    public String query;

    public Search(String txt) {
        Logger.debug("search is '%s'", txt);
        this.query = txt;
    }

    public List<Item> getItems() {
        return Item.find("bySearch", this).order("price").asList();
    }
}
