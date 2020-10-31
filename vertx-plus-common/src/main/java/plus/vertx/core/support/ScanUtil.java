package plus.vertx.core.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.util.Set;

import com.google.common.collect.Sets;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtPrimitiveType;
import javassist.bytecode.AccessFlag;

/**
 * 类扫描工具
 * @author crazyliu
 * @date 2020/9/30
 */
public class ScanUtil {
    public static final Logger log = LoggerFactory.getLogger(ScanUtil.class);

    /**
     * 获取扫码到的服务类
     *
     * @param serviceScan 待扫描的路径集合，以英文逗号分隔
     * @param annotation 带扫描的注解类
     * @return 返回扫描到的去重复类集合
     */
    public static Set<Class<?>> getClasses(String serviceScan, Class<? extends Annotation> annotation) {
        String split = ",";
        if (ValidateUtil.isEmpty(serviceScan)) {
            return new Reflections().getTypesAnnotatedWith(annotation);
        } else if (serviceScan.contains(split)) {
            return new Reflections(Sets.newHashSet(serviceScan.split(split))).getTypesAnnotatedWith(annotation);
        } else {
            return new Reflections(serviceScan).getTypesAnnotatedWith(annotation);
        }
    }

    public static Object newProxyInstance(ClassLoader loader, Class<?> interfaceClass, InvocationHandler h) throws Throwable {
        ClassPool pool = ClassPool.getDefault();
     
        // 1.创建代理类：public class NewProxyClass
        CtClass proxyCc = pool.makeClass("NewProxyClass");
     
        /* 2.给代理类添加字段：private InvocationHandler h; */
        CtClass handlerCc = pool.get(InvocationHandler.class.getName());
        CtField handlerField = new CtField(handlerCc, "h", proxyCc);
        handlerField.setModifiers(AccessFlag.PRIVATE);
        proxyCc.addField(handlerField);
     
        /* 3.添加构造函数：public NewProxyClass(InvocationHandler h) { this.h = h; } */
        CtConstructor ctConstructor = new CtConstructor(new CtClass[] { handlerCc }, proxyCc);
        ctConstructor.setBody("$0.h = $1;"); // $0代表this, $1代表构造函数的第1个参数
        proxyCc.addConstructor(ctConstructor);
     
        /* 4.为代理类添加相应接口方法及实现 */
        CtClass interfaceCc = pool.get(interfaceClass.getName());
     
        // 4.1 为代理类添加接口：public class NewProxyClass implements IHello
        proxyCc.addInterface(interfaceCc);
     
        // 4.2 为代理类添加相应方法及实现
        CtMethod[] ctMethods = interfaceCc.getDeclaredMethods();
        for (int i = 0; i < ctMethods.length; i++) {
            String methodFieldName = "m" + i; // 新的方法名
     
            // 4.2.1 为代理类添加反射方法字段
            // 如：private static Method m1 = Class.forName("chapter9.javassistproxy3.IHello").getDeclaredMethod("sayHello2", new Class[] { Integer.TYPE });
            /* 构造反射字段声明及赋值语句 */
            String classParamsStr = "new Class[0]";
            if (ctMethods[i].getParameterTypes().length > 0) {
                for (CtClass clazz : ctMethods[i].getParameterTypes()) {
                    classParamsStr = ((classParamsStr == "new Class[0]") ? clazz.getName() : classParamsStr + "," + clazz.getName()) + ".class";
                }
                classParamsStr = "new Class[] {" + classParamsStr + "}";
            }
            String methodFieldTpl = "private static java.lang.reflect.Method %s=Class.forName(\"%s\").getDeclaredMethod(\"%s\", %s);";
            String methodFieldBody = String.format(methodFieldTpl, "m" + i, interfaceClass.getName(), ctMethods[i].getName(), classParamsStr);
            // 为代理类添加反射方法字段
            CtField methodField = CtField.make(methodFieldBody, proxyCc);
            proxyCc.addField(methodField);
     
            System.out.println("methodFieldBody: " + methodFieldBody);
     
            /* 4.2.2 为方法添加方法体 */
            /* 构造方法体 */
            String methodBody = "$0.h.invoke($0, " + methodFieldName + ", $args)";
            // 如果有返回类型，则需要转换为相应类型后返回
            if (CtPrimitiveType.voidType != ctMethods[i].getReturnType()) {
                // 对8个基本类型进行转型
                // 例如：((Integer)this.h.invoke(this, this.m2, new Object[] { paramString, new Boolean(paramBoolean), paramObject })).intValue();
                if (ctMethods[i].getReturnType() instanceof CtPrimitiveType) {
                    CtPrimitiveType ctPrimitiveType = (CtPrimitiveType) ctMethods[i].getReturnType();
                    methodBody = "return ((" + ctPrimitiveType.getWrapperName() + ") " + methodBody + ")." + ctPrimitiveType.getGetMethodName() + "()";
                } else { // 对于非基本类型直接转型即可
                    methodBody = "return (" + ctMethods[i].getReturnType().getName() + ") " + methodBody;
                }
            }
            methodBody += ";";
            /* 为代理类添加方法 */
            CtMethod newMethod = new CtMethod(ctMethods[i].getReturnType(), ctMethods[i].getName(),
                    ctMethods[i].getParameterTypes(), proxyCc);
            newMethod.setBody(methodBody);
            proxyCc.addMethod(newMethod);
     
            System.out.println("Invoke method: " + methodBody);
        }
     
        proxyCc.writeFile("D:/tmp");
     
        // 5.生成代理实例. 将入参InvocationHandler h设置到代理类的InvocationHandler h变量
        // @SuppressWarnings("unchecked")
        Object proxy = proxyCc.toClass().getConstructor(InvocationHandler.class).newInstance(h);
     
        return proxy;
    }
    
}
