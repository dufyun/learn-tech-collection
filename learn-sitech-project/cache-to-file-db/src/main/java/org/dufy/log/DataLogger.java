package org.dufy.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.dufy.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据日志文件类
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/17
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public abstract class DataLogger {


    public static final Logger userLogger = LoggerFactory.getLogger("user");


    /**
     * File Separator in ASCII
     */
    public static final char ASCII_GS = 0x1D;

    /**
     * Record Separator in ASCII
     */
    public static final char ASCII_RS = 0x1E;

    /**
     * Unit Separator in ASCII
     */
    public static final char ASCII_US = 0x1F;

    /**
     * We use two US characters and a '|' between them to be the field separator,
     * so that the data file can be more readable ( '|' is  readable character but US isn't).
     *
     * <p><code>String.format("%c|%c", US, US)
     */
    public static final String UNIT_SEP = String.format("%c|%c", ASCII_US, ASCII_US);

    /**
     * To avoid ordinary line break '\n' characters in user chat message cause confusion when parse the
     * record back to instance, we use a RS join with '\n' as our record separator.
     *
     * <p><code>String.format("%c\n", RS)
     */
    public static final String REC_SEP = String.format("%c\n", ASCII_RS);

    /**
     * Record {@code User} datagram to data log file and keep all fields in proper order
     *
     * @param user {@code User} instance
     */
    public static void recordUser(User user) {
        userLogger.info(
                filterUnitSeperatorAndJoin("User", UNIT_SEP,
                        user.getId(),
                        user.getUserName(),
                        user.getPassword(),
                        user.getAge()));
    }

    /**
     * Translate US character in all fields to space, and return the joined filtered fields.
     *
     * @param prefix to indicate what kind of data record this is
     * @param separator to separate each field when all fields are joined together
     * @param fields the fields need to be joined
     * @return joined filtered fields with separator among them
     */
    public static String filterUnitSeperatorAndJoin(String prefix, String separator, Object...fields) {
        StringBuilder buf = new StringBuilder(1024);
        buf.append(prefix);
        for(int i=0; i<fields.length; i++) {
            buf.append(separator).append(toString(fields[i]).replace(ASCII_US, ' ').replace(ASCII_RS, ' '));
        }
        buf.append(ASCII_RS).append('\n');
        return buf.toString();
    }

    /**
     * Get the string representation of {@code instance}.
     *
     * <p>The function render {@code null} to empty string,
     * {@link Date} instance to yyyyMMddHHmmssSSS format with {@link SimpleDateFormat},
     * and the other types with directly calling {@link Object#toString()} method.
     *
     * @param instance
     * @return
     */
    public static String toString(Object instance) {
        if(instance == null) {
            return "";
        }

        if(instance instanceof java.util.Date) {
            return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(instance);
        }

        return instance.toString();
    }
}
