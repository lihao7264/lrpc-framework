package com.atlihao.lrpc.framework.core.router;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 9:25 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 9:25 上午
 * @Version: 1.0.0
 */
@AllArgsConstructor
public class RingLinkedList {

    /**
     * 头节点
     */
    private Node head;
    /**
     * 尾节点
     */
    private Node tail;
    /**
     * 大小
     */
    private int size;

    /**
     * 追加节点
     *
     * @param node
     */
    public void add(Node node) {
        if (size == 0) {
            this.head = node;
            this.tail = node;
        }
    }

    @Data
    @AllArgsConstructor
    static class Node {
        /**
         * 数据
         */
        private Object data;
        /**
         * 下一个节点
         */
        private Node next;
        /**
         * 上一个节点
         */
        private Node pre;
    }
}
