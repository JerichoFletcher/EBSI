package ebsi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Log {
    private static final Map<Class<?>, Logger> loggerMap = new HashMap<>();

    public static Logger get(Object obj) {
        Class<?> key = obj instanceof Class<?> ? (Class<?>)obj : obj.getClass();
        if (!loggerMap.containsKey(key)) loggerMap.put(key, LoggerFactory.getLogger(key));
        return loggerMap.get(key);
    }
}
