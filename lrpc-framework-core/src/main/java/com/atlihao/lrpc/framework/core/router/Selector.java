package com.atlihao.lrpc.framework.core.router;

import lombok.Data;

/**
 * @Description:
 * @Author: 仲阳-李豪
 * @CreateDate: 2023/8/10 9:22 上午
 * @UpdateUser: 仲阳-李豪
 * @UpdateDate: 2023/8/10 9:22 上午
 * @Version: 1.0.0
 */
@Data
public class Selector {

    /**
     * 服务命名
     * 举例: com.atlihao.test.DataService
     */
    private String providerServiceName;

}
