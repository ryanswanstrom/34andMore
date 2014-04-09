package controllers;

import java.util.HashMap;
import java.util.Map;
import models.Item;
import models.Search;
import org.apache.commons.lang.StringUtils;
import play.mvc.Controller;
import play.mvc.Router;
import utils.GoogleSearch;

public class Application extends Controller {

    public static void index() { 
        render();
    }

    public static void search(String txt) {
        if (StringUtils.isEmpty(txt)) {
            index();
        }
        String query = StringUtils.trimToEmpty(txt);
        Search search = GoogleSearch.search(query);
        notFoundIfNull(search);
        results(search.getId().toString());
    }

    public static void results(String id) {
        Search result = Search.findById(id);
        notFoundIfNull(result);
        Map map = new HashMap();
        map.put("id", id);
        String url = Router.getFullUrl("Application.results", map);
        render(result, url);
    }

    public static void showItem(String id) {
        Item item = Item.findById(id);
        notFoundIfNull(item);
        Map map = new HashMap();
        map.put("id", id);
        String url = Router.getFullUrl("Application.showItem", map);
        render(item, url);
    }

    public static void about() {
        render();
    }

}