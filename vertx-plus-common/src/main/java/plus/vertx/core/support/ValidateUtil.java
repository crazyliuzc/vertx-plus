package plus.vertx.core.support;

import com.google.common.base.Strings;
import plus.vertx.core.Constants;

import java.util.Collection;
import java.util.Map;

/**
 * 验证工具
 *
 * @author 刘则昌
 * @since 2020年06月05日
 */
public class ValidateUtil {
    //===========================字符串校验============================

    /**
     * 判断是否为空字符串
     * @param str
     * @return boolean 如果为空，则返回true
     */
    public static boolean isEmpty(String str){
        return Strings.isNullOrEmpty(str);
    }

    /**
     * 判断字符串是否非空
     * @param str 如果不为空，则返回true
     * @return boolean
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    /**
     * 是否纯数字
     * @param str 如果为空，则返回false
     * @return
     */
    public static boolean isNumeric(String str){
        if(isEmpty(str)){
            return false;
        }
        return Constants.NUMERIC_REGEX.matcher(str).matches();
    }

    /**
     * 判断字符串是否以字母开头
     * @param str 如果为空，则返回false
     * @return boolean
     */
    public static boolean isStartWithWord(String str){
        if(isEmpty(str)){
            return false;
        }
        return Constants.START_WITH_WORD_REGEX.matcher(str).matches();
    }

    /**
     * 计算字符出现次数
     * @param str 如果为空，则返回0
     * @return boolean
     */
    public static int countWord(String str, String target){
        if(isEmpty(str)){
            return 0;
        }
        int len1 = str.length();
        int len2 = str.replace(target, "").length();
        return (len1-len2);
    }

    /**
     * 判断字符串是包含中文
     * @param str 如果为空，则返回false
     * @return boolean
     */
    public static boolean hasChinese(String str){
        if(isEmpty(str)){
            return false;
        }
        return Constants.CHINESE_REGEX.matcher(str).find();
    }

    //===========================集合校验============================
    /**
     * 判断collection是否为空
     * @param collection
     * @return boolean
     */
    public static boolean isEmpty(Collection<?> collection){
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断Collection是否非空
     * @return boolean
     */
    public static boolean isNotEmpty(Collection<?> collection){
        return ! isEmpty(collection);
    }

    /**
     * 判断map是否为空
     * @param map
     * @return boolean
     */
    public static boolean isEmpty(Map<?,?> map){
        return map == null || map.isEmpty();
    }

    /**
     * 判断map是否非
     * @param map
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?,?> map){
        return ! isEmpty(map);
    }
}
