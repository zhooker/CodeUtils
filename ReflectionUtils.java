package example.com.daggerdemo.reflect;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射相关的工具类
 *
 * getFieldValue        获取当前类的属性
 * getSuperFieldValue   获取当前类的属性及其父类的属性
 * setFieldValue        设置当前类的属性
 * setSuperFieldValue   设置当前类的属性及其父类的属性
 * invokeMethod         执行当前类的方法
 * invokeSuperMethod    执行当前类的方法及其父类的方法
 */
public class ReflectionUtils {


    /**
     * 打印当前类所有的属性
     *
     * @param object
     * @param tag
     */
    public static void dumpAllFields(Object object, String tag) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            Log.d(tag, "name=" + field.getName() + " ,Modifiers=" + field.getModifiers());
        }
    }

    /**
     * 打印当前类所有的方法
     *
     * @param object
     * @param tag
     */
    public static void dumpAllMethods(Object object, String tag) {
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Log.d(tag, "name=" + method.getName() + " return type = " + method.getGenericReturnType());
            for (Class<?> clazz : method.getParameterTypes()) {
                Log.d(tag, "parameter=" + clazz);
            }
        }
    }

    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName, false);
        if (field == null)
            return null;

        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getSuperFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName, true);
        if (field == null)
            return null;

        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setSuperFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName, true);
        if (field == null)
            return;

        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName, false);
        if (field == null)
            return;

        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    public static Field getDeclaredField(Object object, String fieldName, boolean findParent) {
        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            if (!findParent)
                break;
        }

        return null;
    }

    public static Method getDeclaredMethod(Object object, String methodName, boolean findParent, Class<?>... parameterTypes) {
        Method method = null;
        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {

            }
            if (!findParent)
                break;
        }
        return null;
    }

    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes,
                                      Object[] parameters) {
        Method method = getDeclaredMethod(object, methodName, false, parameterTypes);

        try {
            if (null != method) {
                method.setAccessible(true);
                return method.invoke(object, parameters);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeSuperMethod(Object object, String methodName, Class<?>[] parameterTypes,
                                           Object[] parameters) {
        Method method = getDeclaredMethod(object, methodName, true, parameterTypes);

        try {
            if (null != method) {
                method.setAccessible(true);
                return method.invoke(object, parameters);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
}
