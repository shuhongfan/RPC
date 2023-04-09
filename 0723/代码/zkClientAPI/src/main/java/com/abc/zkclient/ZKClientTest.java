package com.abc.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

public class ZKClientTest {
    // zk集群
    // private static final String CLUSTER = "zookeeperOS1:2181,zookeeperOS2:2181,zookeeperOS3:2181";
    private static final String CLUSTER = "zookeeperOS:2181";
    private static final String PATH = "/xxx";

    public static void main(String[] args) {
        // ---------------- 创建会话 -----------
        ZkClient zkClient = new ZkClient(CLUSTER);

        // ---------------- 创建节点 -----------
        CreateMode mode = CreateMode.PERSISTENT;
        String nodeName = zkClient.create(PATH, "first log", mode);
        System.out.println("新创建的节点名称为：" + nodeName);

        // ---------------- 获取数据内容 -----------
        Object readData = zkClient.readData(PATH);
        System.out.println("节点的数据内容为：" + readData);

        // ---------------- 注册数据变更watcher -----------
        zkClient.subscribeDataChanges(PATH, new IZkDataListener() {

            // 当PATH节点中的数据内容发生变更，就会触发该方法的执行
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("节点" + dataPath + "的数据已经更新为了" + data);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println(dataPath + "的数据内容被删除");
            }
        });

        // ---------------- 更新数据内容 -----------
        zkClient.writeData(PATH, "second log");
        String updatedData = zkClient.readData(PATH);
        System.out.println("更新过的数据内容为：" + updatedData);

        // ---------------- 删除节点 -----------
        // zkClient.delete(PATH);

        // ---------------- 判断节点存在性 -----------
        boolean isExists = zkClient.exists(PATH);
        System.out.println(PATH + "节点仍存在吗？" + isExists);
    }
}
