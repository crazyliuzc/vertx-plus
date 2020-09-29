package plus.vertx.test.beanCopy;

import io.vertx.core.json.JsonObject;
import plus.vertx.core.support.CopyUtil;
import plus.vertx.core.support.JsonUtil;
import plus.vertx.core.support.yaml.ClusterYaml;
import plus.vertx.core.support.yaml.YamlBean;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试cglib的Bean复制
 *
 * @author crazyliu
 */
public class TestBeanCopy {

    public static void main(String[] args) {
        //测试map与Bean的互相转换
        User user2 = new User();
        user2.setAge(10);
        user2.setName("zhangsan");
        Map<String,Object> beanToMap = CopyUtil.toMap(user2);
        System.out.println(beanToMap);
        User mapToBean = CopyUtil.toBean(beanToMap, User.class);
        System.out.println(mapToBean);
        //半转换
        Map map1 = new HashMap();
        map1.put("name", "江红");
        User mapToBean1 = CopyUtil.toBean(map1, User.class);
        System.out.println(mapToBean1);

        //属性名称相同类型相同的属性拷贝
        User user = new User();
        user.setAge(10);
        user.setName("zhangsan");
        UserDto userDto = new UserDto();
        CopyUtil.copy(user, userDto);
        System.out.println(userDto);

        //属性名称相同而类型不同的属性不会被拷贝
        //即使源类型是原始类型(int, short和char等)，目标类型是其包装类型(Integer, Short和Character等)，或反之：都 不会被拷贝
        User user1 = new User();
        user1.setAge(10);
        user1.setName("zhangsan");
        UserWithDiffType userDto1 = new UserWithDiffType();
        CopyUtil.copy(user, userDto1);
        System.out.println(userDto1);
        
        //测试Proto转换
        HelloProto.Builder newBuilder = HelloProto.newBuilder();
        newBuilder.setMessage("测试一下");
        //proto Bean复制，不能复制
        HelloProto.Builder copy = CopyUtil.copy(newBuilder, HelloProto.newBuilder().getClass());
        //proto Bean转map，可以转换，但是生成的map会多一些属性
        Map<String,Object> beanToMap1 = CopyUtil.toMap(newBuilder);
        //map转proto Bean，不能转换
        HelloProto.Builder mapToBean2 = CopyUtil.toBean(beanToMap1, HelloProto.newBuilder().getClass());

        System.out.println("");

        YamlBean yamlBean = new YamlBean();
        ClusterYaml clusterYaml = new ClusterYaml();
        clusterYaml.setEnable(true);
        clusterYaml.setName("tettt");
        clusterYaml.setPassword("password");
        clusterYaml.setType("telll");
        yamlBean.setCluster(clusterYaml);
        System.out.println(JsonUtil.toJson(CopyUtil.copy(yamlBean, YamlBean.class),false));;

        System.out.println(JsonUtil.toJson(CopyUtil.toMap(yamlBean),false));

        String aa = "{\"cluster\":{\"enable\":false,\"type\":\"Hazelcast\",\"name\":\"test\",\"password\":123456,\"ip\":\"192.168.6.118\"},\"module\":{\"name\":\"core\",\"startScan\":null},\"http\":{\"host\":\"0.0.0.0\",\"port\":6666,\"site\":\"https://www.abc.com\",\"servicePath\":null},\"rsfRpc\":{\"host\":\"0.0.0.0\",\"port\":6666,\"servicePath\":null,\"timeout\":null},\"googleRpc\":{\"host\":\"0.0.0.0\",\"port\":6666,\"servicePath\":null,\"timeout\":null},\"mapper\":{\"jdbcUrl\":\"jdbc:mysql://localhost:3306/myshop?useUnicode=true&characterEncoding=UTF-8&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai\",\"driverClassName\":\"com.mysql.cj.jdbc.Driver\",\"maximumPoolSize\":20,\"minPoolSize\":0,\"initialPoolSize\":1,\"minIdleTime\":1,\"timeBetweenEvictionRunsMillis\":1,\"keepAlive\":true,\"username\":\"root\",\"password\":\"company\",\"mapperScan\":\"xxx.xxx\",\"sqlXmlScan\":\"sql\"}}";
        JsonObject entries = new JsonObject(aa);
        YamlBean copy1 = CopyUtil.toBean(entries.getMap(), YamlBean.class);
        System.out.println(JsonUtil.toJson(copy1,false));
    }
}
