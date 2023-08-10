package com.atlihao.lrpc.framework.core.router;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.registry.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.*;

/**
 * @Description: 新版的随机筛选策略
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 9:33 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 9:33 上午
 * @Version: 1.0.0
 */
public class NewRandomLRouterImpl implements LRouter {

    /**
     * 刷新路由数组
     *
     * @param selector
     */
    @Override
    public void refreshRouterArr(Selector selector) {
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(selector.getProviderServiceName());
        // 计算总的权重
        Integer totalWeight = calTotalWeights(channelFutureWrappers);
        SERVICE_ROUTER_TOTAL_WEIGHT_MAP.put(selector.getProviderServiceName(), totalWeight);
    }

    /**
     * 获取请求到连接通道
     *
     * @param selector
     * @return
     */
    @Override
    public ChannelFutureWrapper select(Selector selector) {
        // 1、获取总权重
        Integer totalWeight = SERVICE_ROUTER_TOTAL_WEIGHT_MAP.get(selector.getProviderServiceName());
        int randomWeight = ThreadLocalRandom.current().nextInt(totalWeight) + 1;
        ChannelFutureWrapper channelFutureWrapper = queryChannelFutureWrapperByWeight(CONNECT_MAP.get(selector.getProviderServiceName()), randomWeight);
        return channelFutureWrapper;
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
        // 计算总的权重
        Integer totalWeight = calTotalWeights(channelFutureWrappers);
        SERVICE_ROUTER_TOTAL_WEIGHT_MAP.put(url.getServiceName(), totalWeight);
    }

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


    /*********************** 随机权重计算方式二：累加+随机方式 **************************/
    private static Integer calTotalWeights(List<ChannelFutureWrapper> channelFutureWrappers) {
        Integer totalWeight = 0;
        for (int k = 0; k < channelFutureWrappers.size(); k++) {
            Integer weight = channelFutureWrappers.get(k).getWeight();
            totalWeight += weight;
        }
        return totalWeight;
    }

    private static ChannelFutureWrapper queryChannelFutureWrapperByWeight(List<ChannelFutureWrapper> futureWrappers, int randomWeight) {
        for (ChannelFutureWrapper futureWrapper : futureWrappers) {
            if (0 < randomWeight && randomWeight <= futureWrapper.getWeight()) {
                return futureWrapper;
            }
            randomWeight -= futureWrapper.getWeight();
        }
        return null;
    }



    public static void main(String[] args) {
        List<ChannelFutureWrapper> channelFutureWrappers = new ArrayList<>();
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 100));
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 200));
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 9300));
        channelFutureWrappers.add(new ChannelFutureWrapper(null, null, 400));
        /********** 方式一 *********/
//        Integer[] r = createWeightArr(channelFutureWrappers);
//        r = createRandomArr(r);
        /**
         * 0 1 1 2 2 .....
         */
//        for (int i = 0; i < r.length; i++) {
//            System.out.println(r[i]);
//        }
        /********** 方式二 *********/
        Integer totalWeight = calTotalWeights(channelFutureWrappers);
        for (int i = 0; i < 100; i++) {
            int randomWeight = ThreadLocalRandom.current().nextInt(totalWeight) + 1;
            ChannelFutureWrapper channelFutureWrapper = queryChannelFutureWrapperByWeight(channelFutureWrappers, randomWeight);
            System.out.println(channelFutureWrapper);
        }
    }

}
