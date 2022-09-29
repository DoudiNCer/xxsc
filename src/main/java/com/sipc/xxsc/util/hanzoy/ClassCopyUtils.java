package com.sipc.xxsc.util.hanzoy;

import java.lang.reflect.Field;

public class ClassCopyUtils {
        public ClassCopyUtils() {
        }

        public static void ClassCopy(Object dest, Object src) {
            Class<?> srcClass = src.getClass();
            Field[] srcClassDeclaredFields = srcClass.getDeclaredFields();
            Class<?> destClass = dest.getClass();
            Field[] destClassDeclaredFields = destClass.getDeclaredFields();
            int var7 = destClassDeclaredFields.length;

            for (Field destClassDeclaredField : destClassDeclaredFields) {
                destClassDeclaredField.setAccessible(true);
                String name = destClassDeclaredField.getName();
                if (destClassDeclaredField.isAnnotationPresent(CopyProperty.class)) {
                    name = destClassDeclaredField.getAnnotation(CopyProperty.class).value();
                }

                int var12 = srcClassDeclaredFields.length;

                for (Field srcClassDeclaredField : srcClassDeclaredFields) {
                    srcClassDeclaredField.setAccessible(true);
                    if (srcClassDeclaredField.getName().equals(name)) {
                        try {
                            destClassDeclaredField.set(dest, srcClassDeclaredField.get(src));
                        } catch (IllegalAccessException var16) {
                            var16.printStackTrace();
                        }
                    }
                }
            }

        }
}
