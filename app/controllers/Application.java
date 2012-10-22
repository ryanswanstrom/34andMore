package controllers;

import play.mvc.*;


import models.*;
import org.apache.commons.lang.StringUtils;
import play.Logger;
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
        render(result);
    }
    
    public static void showItem(String id) {
        Item item = Item.findById(id);
        notFoundIfNull(item);
        render(item);
    }
    
    public static void about() {
        render();
    }

}