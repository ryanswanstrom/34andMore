package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.ImageInfo;
import models.Search;
import models.Item;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.WS.WSRequest;

/**
 * This file will contain
 * a call to the Google Product search API.
 */
public class GoogleSearch {

    private static final String KEY = Play.configuration.getProperty("google.shopping.api");
    private static final String COUNTRY = "US";
    private static final int SIZE_SMALL = 64;
    private static final int SIZE_MEDIUM = 128;
    private static final int SIZE_LARGE = 220;
    private static final int ITEMS_RETURNED = 44;

    /**
     * This method will go to the Google API
     * and search for results based upon a query string.
     * 
     * TODO: Lots of null checking, possibly breakup the method.
     * 
     * @param query
     * @return A search object or null if the search object cannot be saved
     *      (invalid search query)
     */
    public static Search search(String query) {
        Search search = new Search(query);
        if (!search.validateAndSave()) {
            return null;
        }

        WSRequest request = WS.url("https://www.googleapis.com/shopping/search/v1/public/products");
        request.setParameter("key", KEY);
        request.setParameter("country", COUNTRY);
        request.setParameter("q", StringUtils.replace(query, " ", "+"));
        request.setParameter("thumbnails", SIZE_SMALL + ":*," + SIZE_MEDIUM + ":*," + SIZE_LARGE + ":*");
        request.setParameter("maxResults", ITEMS_RETURNED);
        JsonElement json = request.get().getJson();
        Logger.info("json: %s", json);
        if (json != null) {
            JsonArray items = json.getAsJsonObject().getAsJsonArray("items");
            Logger.info("number of items returned: %d", (items == null)?0:items.size());
            for (int i = 0; items != null && i < items.size(); i++) {
                Item item = populateItem(items.get(i).getAsJsonObject(), search);
                if (item != null) {
                    item.save();
                }
            }
        }
        return search;
    }

    /**
     * 
     * @param itemObj
     * @param search
     * @return An Item or null if a problem occurs
     */
    private static Item populateItem(JsonObject itemObj, Search search) {
        Item item = null;
        JsonElement productEl = itemObj.get("product");
        if (productEl != null) {
            JsonObject product = productEl.getAsJsonObject();
            Logger.info("product %s", product);
            String name = getString(product, "title");
            item = new Item(name, search);
            item.url = getString(product, "link");
            item.brand = getString(product, "brand");
            item.desc = getString(product, "description");
            JsonElement ownerEl = product.get("author");
            if (ownerEl != null) {
                item.company = getString(ownerEl.getAsJsonObject(), "name");
            }

            // Categories do not work unless searching in a specific store
//            JsonArray categories = product.getAsJsonArray("categories");
//            if (null != categories) {
//                Logger.info("# of categories %d", categories.size());
//                for (int c = 0; c < categories.size(); c++) {
//                    JsonObject category = categories.get(c).getAsJsonObject();
//                    Logger.info("category is %s", category);
//                }
//            }
            JsonArray inventories = product.getAsJsonArray("inventories");
            Logger.info("inventories: %s", inventories);
            if (null != inventories) {
                Logger.info("# of inventories %d", inventories.size());
                for (int c = 0; c < inventories.size(); c++) {
                    JsonObject inventory = inventories.get(c).getAsJsonObject();
                    Logger.info("inventory is %s", inventory);
                    item.price = inventory.get("price").getAsDouble();
                }
            }

            // get images
            JsonArray imgArray = product.getAsJsonArray("images");
            Logger.info("# of images: %d", (imgArray == null)?0:imgArray.size());
            for (int j = 0; imgArray != null && j < imgArray.size(); j++) {
                JsonObject image = imgArray.get(j).getAsJsonObject();
                ImageInfo imageInfo = new ImageInfo();
                // this image can sometimes be too big
                imageInfo.original = getString(image, "link");
                JsonArray thumbs = image.getAsJsonArray("thumbnails");
                for (int k = 0; thumbs != null && k < thumbs.size(); k++) {
                    JsonObject thumbnail = thumbs.get(k).getAsJsonObject();
                    JsonElement thumbEl = thumbnail.get("width");
                    if (thumbEl != null) {
                        int thumbSize = thumbEl.getAsInt();
                        if (thumbSize == SIZE_SMALL) {
                            imageInfo.small = getString(thumbnail, "link");
                        } else if (thumbSize == SIZE_MEDIUM) {
                            imageInfo.medium = getString(thumbnail, "link");
                        } else if (thumbSize == SIZE_LARGE) {
                            imageInfo.large = getString(thumbnail, "link");
                        }
                    }
                }
                item.addImageInfo(imageInfo);
            }
        }
        return item;
    }

    /**
     * This method will get the element with name.
     * 
     * @param jsonObj
     * @param name
     * @return The String value, null if name not found
     */
    private static String getString(JsonObject jsonObj, String name) {
        String rVal = null;
        if (jsonObj != null
                && StringUtils.isNotBlank(name)
                && jsonObj.get(name) != null) {
            rVal = jsonObj.get(name).getAsString();
        }
        return rVal;
    }
}
