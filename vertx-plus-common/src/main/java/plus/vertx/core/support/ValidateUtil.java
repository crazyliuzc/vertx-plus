package plus.vertx.core.support;

import com.google.common.base.Strings;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 验证工具
 *
 * @author crazyliu
 * @since 2020年06月05日
 */
public class ValidateUtil {
    //===========================正则定义============================

    /**
     * 纯数字
     */
    public static final Pattern NUMERIC_REGEX = Pattern.compile("[0-9]+");

    /**
     * 字母开头
     */
    public static final Pattern START_WITH_WORD_REGEX = Pattern.compile("^[A-Za-z].*");

    /**
     * 中文
     */
    public static final Pattern CHINESE_REGEX = Pattern.compile("[\u4E00-\u9FFF]");
    //===========================字符串校验============================

    /**
     * 判断是否为空字符串
     *
     * @param str
     * @return boolean 如果为空，则返回true
     */
    public static boolean isEmpty(String str) {
        return Strings.isNullOrEmpty(str);
    }

    /**
     * 判断字符串是否非空
     *
     * @param str 如果不为空，则返回true
     * @return boolean
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 是否纯数字
     *
     * @param str 如果为空，则返回false
     * @return
     */
    public static boolean isNumeric(String str) {
        return !isEmpty(str) && NUMERIC_REGEX.matcher(str).matches();
    }

    /**
     * 判断字符串是否以字母开头
     *
     * @param str 如果为空，则返回false
     * @return boolean
     */
    public static boolean isStartWithWord(String str) {
        return !isEmpty(str) && START_WITH_WORD_REGEX.matcher(str).matches();
    }

    /**
     * 计算字符出现次数
     *
     * @param str 如果为空，则返回0
     * @param target
     * @return boolean
     */
    public static int countWord(String str, String target) {
        if (isEmpty(str)) {
            return 0;
        }
        int len1 = str.length();
        int len2 = str.replace(target, "").length();
        return (len1 - len2);
    }

    /**
     * 判断字符串是包含中文
     *
     * @param str 如果为空，则返回false
     * @return boolean
     */
    public static boolean hasChinese(String str) {
        return !isEmpty(str) && CHINESE_REGEX.matcher(str).find();
    }

    /**
     * 是否为中文语言环境
     *
     * @return boolean
     */
    public static boolean isChineseLanguage() {
        return "zh".equals(Locale.getDefault().getLanguage());
    }

    //===========================集合校验============================
    /**
     * 判断collection是否为空
     *
     * @param collection
     * @return boolean
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断Collection是否非空
     *
     * @param collection
     * @return boolean
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断map是否为空
     *
     * @param map
     * @return boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断map是否非
     *
     * @param map
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}
