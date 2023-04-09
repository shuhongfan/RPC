package com.abc.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

public class ZKClientTest2 {
    private static final String CLUSTER = "zookeeperOS:2181";
    private static final String PATH = "/china";

    public static void main(String[] args) {
        // ---------------- 创建会话 -----------
        ZkClient zkClient = new ZkClient(CLUSTER);

        // ---------------- 获取数据内容 -----------
        Object readData = zkClient.readData(PATH);
        System.out.println("节点的数据内容为：" + readData);

        // ---------------- 获取子节点列表 -----------
        List<String> children = zkClient.getChildren(PATH);
        System.out.println(children);

        // ---------------- 注册子节点列表变更的watcher -----------
        zkClient.subscribeChildChanges(PATH, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("节点" + parentPath + "的所有子节点为：" + currentChilds);
            }
        });

        // ---------------- 判断节点存在性 -----------
        boolean isExists = zkClient.exists(PATH);
        System.out.println(PATH + "节点仍存在吗？" + isExists);
    }
}
