package revxrsal.args.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.args.reflect.MethodCaller;
import revxrsal.args.reflect.MethodCaller.BoundMethodCaller;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isPublic;
import static revxrsal.args.util.KotlinConstants.*;

/**
 * A utility for finding the singleton inside a class
 */
public final class KotlinSingletons {

    private static final String SINGLETON_NAME = "INSTANCE";

    /**
     * NOTE: The companion field name is the same as the companion class
     * name, so it may not be necessarily this value.
     */
    private static final String COMPANION_NAME = "Companion";

    private KotlinSingletons() {
    }

    public static BoundMethodCaller getCallerForNonDefault(
            @NotNull Method method,
            @Nullable Object instance
    ) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        MethodCaller caller = MethodCaller.wrap(method);
        boolean jvmStatic = isJvmStatic(method);
        if (jvmStatic)
            return caller.bindTo(null);

        // an object or a companion object, bind it twice
        if (parameterTypes[0] == method.getDeclaringClass())
            return arguments -> {
                Object[] boundArgs = ArrayUtils.addFirst(arguments, instance);
                return caller.call(instance, boundArgs);
            };

        return caller.bindTo(instance);
    }

    public static Object findCompanion(Class<?> type) {
        Map<String, Field> fields = Arrays
                .stream(type.getDeclaredFields())
                .filter(field -> isPublic(field.getModifiers()) && isStaticFinal(field.getModifiers()))
                .collect(Collectors.toMap(Field::getName, f -> f));

        // try the Companion field
        try {
            Class<?> companion = Class.forName(type.getName() + "$" + COMPANION_NAME);
            Field companionField = fields.get(companion.getSimpleName());
            if (companionField != null)
                return fetch(companionField);
        } catch (ClassNotFoundException ignored) {
        }

        // Companion probably has another name. Look for a class
        // that has a static final field in its parent and has a name
        // that matches it
        for (Class<?> declaredClass : type.getDeclaredClasses()) {
            String name = declaredClass.getSimpleName();
            Field companionField = fields.get(name);
            if (companionField != null) {
                Object singleton = fetch(companionField);
                if (singleton != null)
                    return singleton;
            }
        }
        throw new IllegalStateException("Unable to find companion object.");
    }

    public static Object findSingleton(Class<?> type) {
        // 1. try to find the INSTANCE field
        try {
            Field singletonField = type.getDeclaredField(SINGLETON_NAME);
            Object singleton = fetch(singletonField);
            if (singleton != null)
                return singleton;
        } catch (NoSuchFieldException ignored) {
        }

        // 2. try to find any static final field whose type is our type
        Field singletonField = findStaticFinal(type);
        if (singletonField != null) {
            Object singleton = fetch(singletonField);
            if (singleton != null)
                return singleton;
        }

        // 3. no singleton field was found, attempt to construct one
        try {
            Constructor<?> noArg = type.getDeclaredConstructor();
            makeAccessible(noArg);
            return noArg.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException ignored) {
        }

        // 4. no no-arg constructor was found, attempt to find Kotlin's synthetic constructor
        try {
            Constructor<?> synthetic = type.getDeclaredConstructor(defaultConstructorMarker());
            makeAccessible(synthetic);
            return synthetic.newInstance((Object) null);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static void makeAccessible(@NotNull AccessibleObject accessibleObject) {
        if (!accessibleObject.isAccessible())
            accessibleObject.setAccessible(true);
    }

    private static @Nullable Field findStaticFinal(@NotNull Class<?> type) {
        for (Field declaredField : type.getDeclaredFields()) {
            if (declaredField.getType() != type)
                continue;
            int mods = declaredField.getModifiers();
            if (isStaticFinal(mods))
                return declaredField;
        }
        return null;
    }

    private static Object fetch(@NotNull Field field) {
        try {
            makeAccessible(field);
            return field.get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access the field", e);
        }
    }

}
