package plus.vertx.test.beanCopy;

import java.util.HashMap;
import java.util.Map;
import plus.vertx.core.support.BeanCopyUtil;

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
        Map<?, ?> beanToMap = BeanCopyUtil.beanToMap(user2);
        System.out.println(beanToMap);
        User mapToBean = BeanCopyUtil.mapToBean(beanToMap, User.class);
        System.out.println(mapToBean);
        //半转换
        Map map1 = new HashMap();
        map1.put("name", "江红");
        User mapToBean1 = BeanCopyUtil.mapToBean(map1, User.class);
        System.out.println(mapToBean1);

        //属性名称相同类型相同的属性拷贝
        User user = new User();
        user.setAge(10);
        user.setName("zhangsan");
        UserDto userDto = new UserDto();
        BeanCopyUtil.copy(user, userDto);
        System.out.println(userDto);

        //属性名称相同而类型不同的属性不会被拷贝
        //即使源类型是原始类型(int, short和char等)，目标类型是其包装类型(Integer, Short和Character等)，或反之：都 不会被拷贝
        User user1 = new User();
        user1.setAge(10);
        user1.setName("zhangsan");
        UserWithDiffType userDto1 = new UserWithDiffType();
        BeanCopyUtil.copy(user, userDto1);
        System.out.println(userDto1);
        
        //测试Proto转换
        HelloProto.Builder newBuilder = HelloProto.newBuilder();
        newBuilder.setMessage("测试一下");
        //proto Bean复制，不能复制
        HelloProto.Builder copy = BeanCopyUtil.copy(newBuilder, HelloProto.newBuilder().getClass());
        //proto Bean转map，可以转换，但是生成的map会多一些属性
        Map<?, ?> beanToMap1 = BeanCopyUtil.beanToMap(newBuilder);
        //map转proto Bean，不能转换
        HelloProto.Builder mapToBean2 = BeanCopyUtil.mapToBean(beanToMap1, HelloProto.newBuilder().getClass());

        System.out.println("");        
    }
}
