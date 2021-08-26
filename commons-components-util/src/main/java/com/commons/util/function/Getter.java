package com.util.function;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

/**
 * @author caijie
 */
public interface Getter extends Serializable {
    String GETTER_PREFIX = "get";

    Object get();

    default SerializedLambda getSerializedLambda() throws Exception {
        Method write = this.getClass().getDeclaredMethod("writeReplace");
        write.setAccessible(true);
        return (SerializedLambda) write.invoke(this);
    }

    default String getPropName() {
        try {
            String implMethodName = getSerializedLambda().getImplMethodName();
            if (implMethodName.startsWith(GETTER_PREFIX)) {
                implMethodName = implMethodName.replace(GETTER_PREFIX, "");
            }
            return implMethodName.substring(0, 1).toLowerCase() + implMethodName.substring(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String attributeName(Getter getter) {
        return getter.getPropName();
    }

    static void use(Getter getter, BiConsumer<String, Object> consumer) {
        consumer.accept(getter.getPropName(), getter.get());
    }

}
