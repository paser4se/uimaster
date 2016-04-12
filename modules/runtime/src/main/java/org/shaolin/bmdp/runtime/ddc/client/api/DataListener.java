package org.shaolin.bmdp.runtime.ddc.client.api;

/**
 * Created by lizhiwe on 4/5/2016.
 */
public interface DataListener {

    public void onNodeUpdate(ZData data);

    public void onNodeDelete(ZData data);

    public void onNodeCreated(ZData data);

    public void onChildChanged(ZData data);
}
