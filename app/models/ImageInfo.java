
package models;

import com.google.code.morphia.annotations.Embedded;
import org.apache.commons.lang.builder.ToStringBuilder;

@Embedded
public class ImageInfo {
    
    public String small;
    public String medium;
    public String large;
    public String original;
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
