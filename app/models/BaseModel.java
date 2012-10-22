
package models;

import play.modules.morphia.Model;

/**
 *
 */
public class BaseModel extends Model {
	public enum Valid{N, Y}
	public Valid valid;

	public BaseModel() {
		this.valid = Valid.Y;
	}
    
}
