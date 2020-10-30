package plus.vertx.core.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
/**
 * 类工具
 * @author crazyliu
 */
public class ClassUtil {
    public static final Logger log = LoggerFactory.getLogger(ClassUtil.class);
    /**
     * 获取方法的参数变量名称
     * 
     * @param classname
     * @param methodname
     * @return
     */
    public static String[] getMethodVariableName(String classname, String methodname) {
        String[] result = null;
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(classname);
            CtMethod cm = cc.getDeclaredMethod(methodname);
            //使用javassist的反射方法的参数名
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            String[] paramNames = new String[cm.getParameterTypes().length];
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                    .getAttribute(LocalVariableAttribute.tag);
            if (attr != null) {
                // 非静态的成员函数的第一个参数是this
                int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
                for (int i = 0; i < paramNames.length; i++) {
                    paramNames[i] = attr.variableName(i + pos);
                }
                result = paramNames;
            }
            //把CtClass object 从ClassPool中移除
            cc.detach();
        } catch (Exception e) {
            log.error("getMethodVariableName fail ",e);
        }
        return result;
    }

}
