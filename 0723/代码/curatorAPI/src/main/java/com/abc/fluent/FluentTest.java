package com.abc.fluent;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;

public class FluentTest {
    // private static final String CLUSTER = "zookeeperOS1:2181,zookeeperOS2:2181,zookeeperOS3:2181";
    private static final String CLUSTER = "zookeeperOS:2181";
    private static final String ROOT_PATH = "mytest";

    public static void main(String[] args) throws Exception {
        // ---------------- 创建会话 -----------
        // 定义重试策略：每1000毫秒重试一次，最多重试3次
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory
                                    .builder()
                                    .connectString(CLUSTER)
                                    .sessionTimeoutMs(15000)
                                    .connectionTimeoutMs(13000)
                                    .retryPolicy(retryPolicy)
                                    .namespace(ROOT_PATH)
                                    .build();
        client.start();

        String nodePath = "/host";

        // ---------------- 创建节点 -----------
        // String nodeName = client.create().forPath(nodePath);
        // String nodeName = client.create().forPath(nodePath, "aaa".getBytes());
        String nodeName = client.create().withMode(CreateMode.PERSISTENT).forPath(nodePath, "aaa".getBytes());
        System.out.println("新创建的节点名称为：" + nodeName);

        // ---------------- 获取数据内容并注册watcher -----------
        byte[] data = client.getData().usingWatcher((CuratorWatcher) event -> {
            System.out.println(event.getPath() + "数据内容发生变化");
        }).forPath(nodePath);
        System.out.println("节点的数据内容为：" + new String(data));

        // ---------------- 获取子节点列表并注册watcher -----------
        List<String> children = client.getChildren().usingWatcher(
                (CuratorWatcher) event -> {
            System.out.println(event.getPath() + "子节点列表发生变化");
        }).forPath(nodePath);
        System.out.println("当前节点的子节点列表为：" + children);


        // ---------------- 更新数据内容 -----------
        client.setData().forPath(nodePath, "newhost".getBytes());
        byte[] newData = client.getData().forPath(nodePath);
        System.out.println("更新过的数据内容为：" + new String(newData));

        // ---------------- 删除节点 -----------
        // if(client.checkExists().forPath(nodePath) != null) {
        //     client.delete().forPath(nodePath);
        // }
    }
}
