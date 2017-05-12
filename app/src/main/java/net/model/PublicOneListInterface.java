package net.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 */

public interface PublicOneListInterface {
    void onGetDataSuccess(String succmessage);
    void onGetDataError(String errmessage);
    void onEmptyData(String Emptymessage);
}
