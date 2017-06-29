package org.shaolin.bmdp.runtime.be;

import java.io.Serializable;

import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;

/**
 * Interface of Business Entity. All Business Entity must implement this
 * interface.
 * 
 * @author Shaolin
 */
public interface IBusinessEntity extends Serializable {
	
	public long getId();
	
	public IBusinessEntity createEntity();
	
	public BEExtensionInfo get_extField();
	
	public JSONObject toJSON() throws JSONException;
	
	public void fromJSON(JSONObject json) throws JSONException;
}
