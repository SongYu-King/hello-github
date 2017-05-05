package com.busap.wowan.redis;

import com.busap.wowan.redis.choose.DefaultRedisServerChooseStrategy;
import com.busap.wowan.redis.choose.RedisServerChooseStrategy;
import com.busap.wowan.redis.exception.ClusterConfigException;
import com.busap.wowan.redis.util.Utils;
import com.busap.wowan.redis.vo.ClusterInfo;
import com.busap.wowan.redis.vo.MasterServer;
import com.busap.wowan.redis.vo.SlaveServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class ClusterConfigParse {

    private static final Logger logger = LoggerFactory
            .getLogger(ClusterConfigParse.class);

    private static int DEFAULT_MAX_IDLE = 5;

    private static int DEFAULT_MAX_ACTIVE = 10;

    private static int DEFAULT_MAX_WAIT = 2000;

    private static int DEFAULT_TIMEOUT = 3000;

    /**
     * 加载redis的集群信息
     *
     * @param configFilePath redis配置文件路径
     * @return 加载的集群配置，key为集群的名称
     */
    public static Map<String, ClusterInfo> loadConf(String configFilePath) {
        // 集群信息，key为集群名称
        Map<String, ClusterInfo> clusterMap = new HashMap<String, ClusterInfo>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dombuilder = factory.newDocumentBuilder();

            InputStream is = ClusterConfigParse.class
                    .getResourceAsStream("/" + configFilePath);

            if (is == null) {
                is = new FileInputStream(configFilePath);
            }
            Document doc = dombuilder.parse(is);
            // 顶级配置元素
            Element configure = doc.getDocumentElement();
            // cluster节点
            NodeList clusterNode = configure.getChildNodes();

            if (clusterNode != null) {
                for (int i = 0; i < clusterNode.getLength(); i++) {
                    Node cluster = clusterNode.item(i);
                    if (cluster.getNodeType() == Node.ELEMENT_NODE) {
                        // redis集群名称
                        String redisClusterName = cluster.getAttributes()
                                .getNamedItem("name").getNodeValue();
                        ClusterInfo clusterInfo = new ClusterInfo(
                                redisClusterName);
                        // 增加集群信息
                        clusterMap.put(redisClusterName, clusterInfo);

                        // 获取这个集群下的pool和master列表
                        NodeList clusterConfigNode = cluster.getChildNodes();

                        // 该集群每个redis池的默认配置
                        int poolMaxActive = DEFAULT_MAX_ACTIVE;
                        int poolMaxIdle = DEFAULT_MAX_IDLE;
                        int poolMaxWait = DEFAULT_MAX_WAIT;
                        int poolTimeout = DEFAULT_TIMEOUT;

                        if (clusterConfigNode != null
                                && clusterConfigNode.getLength() > 0) {

                            for (int ii = 0; ii < clusterConfigNode.getLength(); ii++) {
                                Node configNode = clusterConfigNode.item(ii);
                                if (configNode.getNodeType() != Node.ELEMENT_NODE) {
                                    continue;
                                }

                                // 解析默认的redis池配置
                                if ("pool".equals(configNode.getNodeName())) {
                                    NodeList poolNode = configNode
                                            .getChildNodes();
                                    if (poolNode != null
                                            && poolNode.getLength() > 0) {
                                        for (int poolIndex = 0; poolIndex < poolNode
                                                .getLength(); poolIndex++) {
                                            Node poolConfig = poolNode
                                                    .item(poolIndex);
                                            if (poolConfig.getNodeType() != Node.ELEMENT_NODE) {
                                                continue;
                                            }

                                            if ("maxIdle".equals(poolConfig
                                                    .getNodeName())) {
                                                poolMaxIdle = Integer
                                                        .valueOf(poolConfig
                                                                .getFirstChild()
                                                                .getNodeValue());
                                                continue;
                                            }
                                            if ("maxActive".equals(poolConfig
                                                    .getNodeName())) {
                                                poolMaxActive = Integer
                                                        .valueOf(poolConfig
                                                                .getFirstChild()
                                                                .getNodeValue());
                                                continue;
                                            }
                                            if ("maxWait".equals(poolConfig
                                                    .getNodeName())) {
                                                poolMaxWait = Integer
                                                        .valueOf(poolConfig
                                                                .getFirstChild()
                                                                .getNodeValue());
                                                continue;
                                            }
                                            if ("timeout".equals(poolConfig
                                                    .getNodeName())) {
                                                poolTimeout = Integer
                                                        .valueOf(poolConfig
                                                                .getFirstChild()
                                                                .getNodeValue());
                                                continue;
                                            }
                                        }
                                    }
                                }

                                // 解析服务器选择策略
                                if ("chooseStrategy".equals(configNode
                                        .getNodeName())) {
                                    String chooseStrategyClass = null;
                                    if (configNode.getFirstChild() != null) {
                                        chooseStrategyClass = configNode
                                                .getFirstChild().getNodeValue();
                                    }
                                    if (chooseStrategyClass != null
                                            && !"".equals(chooseStrategyClass)) {
                                        Object instance = Class
                                                .forName(chooseStrategyClass);
                                        if (Utils.isInterface(instance
                                                        .getClass(),
                                                RedisServerChooseStrategy.class
                                                        .getName())) {
                                            clusterInfo
                                                    .setServerChooseStrategy((RedisServerChooseStrategy) instance);
                                        } else {
                                            throw new ClassCastException(
                                                    "指定的选择策略实现类必须实现["
                                                            + RedisServerChooseStrategy.class
                                                            .getName()
                                                            + "]");
                                        }
                                    }
                                    continue;
                                }

                                if ("master".equals(configNode.getNodeName())) {
                                    NamedNodeMap masterAttr = configNode
                                            .getAttributes();

                                    String masterName = masterAttr
                                            .getNamedItem("name")
                                            .getNodeValue();
                                    String address = masterAttr.getNamedItem(
                                            "address").getNodeValue();
                                    String port = masterAttr.getNamedItem(
                                            "port").getNodeValue();
                                    int masterMaxIdle = poolMaxIdle;
                                    if (masterAttr.getNamedItem("maxIdle") != null
                                            && masterAttr.getNamedItem(
                                            "maxIdle").getNodeValue() != null) {
                                        masterMaxIdle = Integer
                                                .valueOf(masterAttr
                                                        .getNamedItem("maxIdle")
                                                        .getNodeValue());
                                    }
                                    int masterMaxActive = poolMaxActive;
                                    if (masterAttr.getNamedItem("maxActive") != null
                                            && masterAttr.getNamedItem(
                                            "maxActive").getNodeValue() != null) {
                                        masterMaxActive = Integer
                                                .valueOf(masterAttr
                                                        .getNamedItem(
                                                                "maxActive")
                                                        .getNodeValue());
                                    }
                                    int masterMaxWait = poolMaxWait;
                                    if (masterAttr.getNamedItem("maxWait") != null
                                            && masterAttr.getNamedItem(
                                            "maxWait").getNodeValue() != null) {
                                        masterMaxWait = Integer
                                                .valueOf(masterAttr
                                                        .getNamedItem("maxWait")
                                                        .getNodeValue());
                                    }
                                    int masterTimeout = poolTimeout;
                                    if (masterAttr.getNamedItem("timeout") != null
                                            && masterAttr.getNamedItem(
                                            "timeout").getNodeValue() != null) {
                                        masterTimeout = Integer
                                                .valueOf(masterAttr
                                                        .getNamedItem("timeout")
                                                        .getNodeValue());
                                    }

                                    // 生成master的解析信息
                                    MasterServer masterServer = new MasterServer(
                                            clusterInfo, masterName);
                                    masterServer.setServerIp(address);
                                    masterServer.setServerPort(port);
                                    masterServer.setMaxIdle(masterMaxIdle);
                                    masterServer.setMaxActive(masterMaxActive);
                                    masterServer.setMaxWait(masterMaxWait);
                                    masterServer.setTimeout(masterTimeout);
                                    masterServer.setMock(Boolean
                                            .valueOf(masterAttr.getNamedItem(
                                                    "ismock").getNodeValue()));

                                    // 解析该master下的slave列表
                                    NodeList slaveNode = configNode
                                            .getChildNodes();

                                    if (slaveNode != null
                                            && slaveNode.getLength() > 0) {
                                        for (int iii = 0; iii < slaveNode
                                                .getLength(); iii++) {
                                            Node slave = slaveNode.item(iii);
                                            if (slave.getNodeType() != Node.ELEMENT_NODE) {
                                                continue;
                                            }

                                            NamedNodeMap slaveAttr = slave
                                                    .getAttributes();
                                            String slaveName = slaveAttr
                                                    .getNamedItem("name")
                                                    .getNodeValue();
                                            String slaveAddress = slaveAttr
                                                    .getNamedItem("address")
                                                    .getNodeValue();
                                            String slavePort = slaveAttr
                                                    .getNamedItem("port")
                                                    .getNodeValue();
                                            int slaveMaxIdle = masterMaxIdle;
                                            if (masterAttr
                                                    .getNamedItem("maxIdle") != null) {
                                                slaveMaxIdle = Integer
                                                        .valueOf(masterAttr
                                                                .getNamedItem(
                                                                        "maxIdle")
                                                                .getNodeValue());
                                            }
                                            int slaveMaxActive = masterMaxActive;
                                            if (masterAttr
                                                    .getNamedItem("maxActive") != null) {
                                                slaveMaxActive = Integer
                                                        .valueOf(masterAttr
                                                                .getNamedItem(
                                                                        "maxActive")
                                                                .getNodeValue());
                                            }
                                            int slaveMaxWait = masterMaxWait;
                                            if (masterAttr
                                                    .getNamedItem("maxWait") != null) {
                                                slaveMaxWait = Integer
                                                        .valueOf(masterAttr
                                                                .getNamedItem(
                                                                        "maxWait")
                                                                .getNodeValue());
                                            }
                                            int slaveTimeout = masterTimeout;
                                            if (masterAttr
                                                    .getNamedItem("timeout") != null) {
                                                slaveTimeout = Integer
                                                        .valueOf(masterAttr
                                                                .getNamedItem(
                                                                        "timeout")
                                                                .getNodeValue());
                                            }

                                            SlaveServer slaveServer = new SlaveServer(
                                                    masterServer, slaveName);
                                            slaveServer
                                                    .setServerIp(slaveAddress);
                                            slaveServer
                                                    .setServerPort(slavePort);
                                            slaveServer
                                                    .setMaxIdle(slaveMaxIdle);
                                            slaveServer
                                                    .setMaxActive(slaveMaxActive);
                                            slaveServer
                                                    .setMaxWait(slaveMaxWait);
                                            slaveServer
                                                    .setTimeout(slaveTimeout);
                                        }
                                    } else {
                                        // 该master下没有slave，允许其可读
                                        masterServer.setRead(true);
                                    }
                                    clusterInfo.setMaxActive(poolMaxActive);
                                    clusterInfo.setMaxIdle(poolMaxIdle);
                                    clusterInfo.setMaxWait(poolMaxWait);
                                    clusterInfo.setTimeout(poolTimeout);
                                    // 没有设置，采用默认
                                    if (clusterInfo.getServerChooseStrategy() == null) {
                                        clusterInfo
                                                .setServerChooseStrategy(new DefaultRedisServerChooseStrategy());
                                    }
                                }
                            }
                        } else {
                            throw new ClusterConfigException("在【"
                                    + redisClusterName + "】下没有找到master节点");
                        }
                    }
                }

            } else {
                throw new ClusterConfigException("没有找到cluster节点");
            }

        } catch (Exception e) {
            logger.error("解析配置Redis文件出错，出错原因：", e);
            System.exit(1);
        }

        return clusterMap;
    }

    public static void main(String[] args) {
        @SuppressWarnings("unused")
        String filePath = "E:\\workspace_20130508\\redis-cluster\\src\\main\\resoures\\redis.xml";
        Map<String, ClusterInfo> info = ClusterConfigParse.loadConf("redis.xml");

        for (Map.Entry<String, ClusterInfo> infos : info.entrySet()) {
            System.out.println(infos);
        }
    }

}
