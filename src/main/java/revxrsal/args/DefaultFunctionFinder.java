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
package revxrsal.args;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.args.reflect.MethodCaller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static revxrsal.args.util.CollectionUtils.insertAtBeginning;
import static revxrsal.args.util.KotlinSingletons.findCompanion;

/**
 * A utility for finding the default synthetic function generated by
 * the Kotlin compiler. This will attempt to find it the following order:
 * <ol>
 *     <li>
 *         Find a <code>(name)$default</code> function with the parameters + 2 additional
 *         synthetic ones
 *     </li>
 *     <li>
 *         Find a <code>(name)$default</code> function with the declaring class as the parameter,
 *         then the function parameters, then 2 additional synthetic parameters
 *     </li>
 *     <li>
 *         Find an inner Companion object, and apply step 2.
 *     </li>
 * </ol>
 * <p>
 * Special cases:
 * <ol>
 *     <li>
 *         A Kotlin <code>object</code>: The 1st parameter is the object type, and
 *         the synthetic function exists in the same class
 *     </li>
 *     <li>
 *         A Kotlin <code>companion object</code>: The 1st parameter would be
 *         the companion object type
 *     </li>
 * </ol>
 */
final class DefaultFunctionFinder {

    private DefaultFunctionFinder() {
    }

    static @Nullable CallableMethod findDefaultFunction(@NotNull Method method) {
        if (method.getParameterCount() == 0)
            throw new IllegalArgumentException("Method has no parameters!");

        List<Class<?>> syntheticParams = getSyntheticParameters(method);
        String name = getDefaultMethodName(method);

        Method defaultMethod;

        // 1. look into the declaring class
        defaultMethod = getDeclaredMethodOrNull(
                name,
                method.getDeclaringClass(),
                syntheticParams
        );
        if (defaultMethod != null) {
            MethodCaller callerForDefault = MethodCaller.wrap(defaultMethod);
            return CallableMethod.of(
                    defaultMethod,
                    callerForDefault
            );
        }

        // 2. try the same as above, but with our type as the first parameter.
        syntheticParams.add(0, method.getDeclaringClass());
        defaultMethod = getDeclaredMethodOrNull(
                name,
                method.getDeclaringClass(),
                syntheticParams
        );
        if (defaultMethod != null) {

            MethodCaller callerForDefault = bindInstanceParameter(defaultMethod);
            return CallableMethod.of(
                    defaultMethod,
                    callerForDefault
            );
        }

        // 2. look into companions
        Object companion = findCompanion(method.getDeclaringClass());
        syntheticParams.set(0, companion.getClass());
        defaultMethod = getDeclaredMethodOrNull(
                name,
                companion.getClass(),
                syntheticParams
        );

        if (defaultMethod == null)
            return null;

        MethodCaller callerForDefault = bindInstanceParameter(defaultMethod);
        return CallableMethod.of(
                defaultMethod,
                callerForDefault
        );
    }

    @SneakyThrows
    private static @NotNull MethodCaller bindInstanceParameter(Method method) {
        MethodCaller caller = MethodCaller.wrap(method);
        return (instance, arguments) -> {
            Object[] boundArgs = insertAtBeginning(arguments, instance);
            return caller.call(instance, boundArgs);
        };
    }

    private static @Nullable Method getDeclaredMethodOrNull(
            @NotNull String name,
            @NotNull Class<?> type,
            @NotNull List<Class<?>> parameterTypes
    ) {
        try {
            return type.getDeclaredMethod(
                    name,
                    parameterTypes.toArray(new Class[0])
            );
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static @NotNull String getDefaultMethodName(@NotNull Method method) {
        return method.getName() + "$default";
    }

    private static @NotNull List<Class<?>> getSyntheticParameters(Method method) {
        List<Class<?>> parameters = new ArrayList<>(method.getParameterCount() + 3);
        Collections.addAll(parameters, method.getParameterTypes());
        parameters.add(int.class);
        parameters.add(Object.class);
        return parameters;
    }
}
