package com.atlihao.lrpc.framework.interfaces.user;

import java.util.List;
import java.util.Map;

/**
 * @Author lihao726726
 */
public interface UserRpcService {

    String getUserId();

    List<Map<String, String>> findMyGoods(String userId);
}
