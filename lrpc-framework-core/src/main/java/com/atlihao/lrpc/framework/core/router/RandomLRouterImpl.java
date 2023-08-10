package com.atlihao.lrpc.framework.core.router;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.registry.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.*;

/**
 * @Description: 随机筛选策略
 * @Author: 仲阳-李豪
 * @CreateDate: 2023/8/10 9:33 上午
 * @UpdateUser: 仲阳-李豪
 * @UpdateDate: 2023/8/10 9:33 上午
 * @Version: 1.0.0
 */
public class RandomLRouterImpl implements LRouter {

    /**
     * 刷新路由数组
     *
     * @param selector
     */
    @Override
    public void refreshRouterArr(Selector selector) {
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] arr = new ChannelFutureWrapper[channelFutureWrappers.size()];
        // 提前生成调用先后顺序的随机数组
        int[] result = createRandomIndex(arr.length);
        // 生成对应服务集群的每台机器的调用顺序
        for (int i = 0; i < result.length; i++) {
            arr[i] = channelFutureWrappers.get(result[i]);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), arr);
    }

    /**
     * 获取请求到连接通道
     *
     * @param selector
     * @return
     */
    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector.getProviderServiceName());
    }

    /**
     * 更新权重信息
     *
     * @param url
     */
    @Override
    public void updateWeight(URL url) {
        // 服务节点的权重
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(url.getServiceName());
        // 获取权重数组（根据权重来设置提供者下标）
        Integer[] weightArr = createWeightArr(channelFutureWrappers);
        // 随机权重数组
        Integer[] finalArr = createRandomArr(weightArr);
        ChannelFutureWrapper[] finalChannelFutureWrappers = new ChannelFutureWrapper[finalArr.length];
        for (int j = 0; j < finalArr.length; j++) {
            finalChannelFutureWrappers[j] = channelFutureWrappers.get(finalArr[j]);
        }
        SERVICE_ROUTER_MAP.put(url.getServiceName(), finalChannelFutureWrappers);
    }

    /*********************** 随机权重计算方式二：累加+随机方式 **************************/

    /*********************** 随机权重计算方式一：数组方式 **************************/
    private static Integer[] createWeightArr(List<ChannelFutureWrapper> channelFutureWrappers) {
        List<Integer> weightArr = new ArrayList<>();
        for (int k = 0; k < channelFutureWrappers.size(); k++) {
            Integer weight = channelFutureWrappers.get(k).getWeight();
            int c = weight / 100;
            for (int i = 0; i < c; i++) {
                weightArr.add(k);
            }
        }
        Integer[] arr = new Integer[weightArr.size()];
        return weightArr.toArray(arr);
    }


    /**
     * 创建随机乱序数组
     *
     * @param arr
     * @return
     */
    private static Integer[] createRandomArr(Integer[] arr) {
        int total = arr.length;
        for (int i = 0; i < total; i++) {
            int j = ThreadLocalRandom.current().nextInt(total);
            // 跳过
            if (i == j) {
                continue;
            }
            // 交换位置
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return arr;
    }

    private int[] createRandomIndex(int len) {
        int[] arrInt = new int[len];
        Arrays.fill(arrInt, -1);
        int index = 0;
        while (index < len) {
            int num = ThreadLocalRandom.current().nextInt(len);
            // 如果数组中不包含该元素,则赋值给数组
            if (!contains(arrInt, num)) {
                arrInt[index++] = num;
            }
        }
        return arrInt;
    }


    public boolean contains(int[] arr, int key) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == key) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        List<ChannelFutureWrapper> channelFutureWrappers = new ArrayList<>();
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 100));
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 200));
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 9300));
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 400));
        Integer[] r = createWeightArr(channelFutureWrappers);
        r = createRandomArr(r);
        /**
         * 0 1 1 2 2 .....
         */
        for (int i = 0; i < r.length; i++) {
            System.out.println(r[i]);
        }
    }

}
