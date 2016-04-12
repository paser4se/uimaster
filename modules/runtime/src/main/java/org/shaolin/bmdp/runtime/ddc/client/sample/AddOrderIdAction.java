package org.shaolin.bmdp.runtime.ddc.client.sample;

import org.shaolin.bmdp.runtime.ddc.client.api.DataAction;
import org.shaolin.bmdp.runtime.ddc.client.api.ZData;

/**
 * Created by lizhiwe on 4/6/2016.
 */
public class AddOrderIdAction implements DataAction {

    private StringBuilder idsTobeAppend = new StringBuilder();

    public String getPath() {
        return "/uimaster/order/orderid";
    }

    @Override
    public byte[] execute(ZData data) {
        byte[] raws = data.getData();
        StringBuilder builder = new StringBuilder(raws.length + 10);
        builder.append(new String(raws));
        if (idsTobeAppend.length() > 0) {
            builder.append(";");
            builder.append(idsTobeAppend);
        }
        return builder.toString().getBytes();
    }

    public void appendOrderId(String orderId) {
        if (idsTobeAppend.length() > 0) {
            idsTobeAppend.append(";");
        }
        idsTobeAppend.append(orderId);
    }

    public String getAppendOrderIds() {
        return idsTobeAppend.toString();
    }
}
