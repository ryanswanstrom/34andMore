
import models.Item;
import models.Search;
import org.junit.Before;
import org.junit.Test;
import play.test.UnitTest;



public class BasicTest extends UnitTest {
    
    @Before
    public void setup() {
        Search.deleteAll();
        Item.deleteAll();
    }

    @Test
    public void testItem() {
        assertEquals("should be no items yet", 0, Item.count());
        Item item = new Item("test item");
        assertTrue("model should save", item.validateAndSave());
        assertEquals("should be one item now", 1, Item.count());               
    }
        
    @Test
    public void testSearchObject() {
        assertEquals("should be no searches yet", 0, Search.count());
        Search srch = new Search("random search query");
        assertTrue("model should save", srch.validateAndSave());
        assertEquals("should be one search now", 1, Search.count());  
    }

}
