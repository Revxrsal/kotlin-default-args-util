/*
 * This file is part of kotlin-default-args-util, licensed under the MIT License.
 *
 *  Copyright (c) Revxrsal <reflxction.github@gmail.com>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the seconds
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package revxrsal.args.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import static revxrsal.args.util.Suppliers.lazy;

/**
 * A utility that abstracts away certain Kotlin constant
 * values and classes
 */
public final class KotlinConstants {

    private KotlinConstants() {
    }

    /**
     * The internal DefaultConstructorMarker class. Used to instantiate Companion
     * objects if necessary
     */
    private static final Supplier<Class<?>> DEFAULT_CONSTRUCTOR_MARKER = lazy(() -> {
        try {
            return Class.forName("kotlin.jvm.internal.DefaultConstructorMarker");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Can't find DefaultConstructorMarker class");
        }
    });

    /**
     * The {@link kotlin.jvm.JvmStatic} annotation
     */
    private static final Supplier<Class<? extends Annotation>> JVM_STATIC = lazy(() -> {
        try {
            return Class.forName("kotlin.jvm.JvmStatic")
                    .asSubclass(Annotation.class);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Can't find JvmStatic class");
        }
    });
    /**
     * The {@link kotlin.jvm.JvmStatic} annotation
     */
    private static final Supplier<Class<?>> CONTINUATION = lazy(() -> {
        try {
            return Class.forName("kotlin.coroutines.Continuation");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Can't find Continuation class");
        }
    });

    /**
     * Tests whether the given element has {@link kotlin.jvm.JvmStatic} annotation
     * on it or not
     *
     * @param element Element to check for
     * @return whether it has JvmStatic or not
     */
    public static boolean isJvmStatic(@NotNull AnnotatedElement element) {
        return element.isAnnotationPresent(JVM_STATIC.get());
    }

    /**
     * Tests whether the given element has {@code static final} modifiers.
     *
     * @param modifiers Element modifiers
     * @return True if it has them, false otherwise
     */
    public static boolean isStaticFinal(int modifiers) {
        return Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }

    /**
     * Returns the 'zero'-like value for a primitive type.
     * <ul>
     *     <li>For numbers, this is 0</li>
     *     <li>For booleans, this is false</li>
     *     <li>For characters, this is the NULL terminator</li>
     *     <li>For other objects, this is null</li>
     * </ul>
     *
     * @param type The type
     * @return The default primitive value
     */
    public static @Nullable Object defaultPrimitiveValue(Class<?> type) {
        if (type == int.class)
            return 0;
        if (type == long.class)
            return 0L;
        if (type == float.class)
            return 0.0f;
        if (type == double.class)
            return 0.0;
        if (type == short.class)
            return (short) 0;
        if (type == byte.class)
            return (byte) 0;
        if (type == boolean.class)
            return false;
        if (type == char.class)
            return '\u0000';
        return null;
    }

    /**
     * Returns the default constructor marker type
     *
     * @return The default constructor marker type
     */
    public static Class<?> defaultConstructorMarker() {
        return DEFAULT_CONSTRUCTOR_MARKER.get();
    }

    /**
     * Returns the continuation class
     *
     * @return the continuation class
     */
    public static Class<?> continuation() {
        return CONTINUATION.get();
    }
}
