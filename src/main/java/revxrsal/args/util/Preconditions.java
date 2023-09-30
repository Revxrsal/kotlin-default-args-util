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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Objects;

import static java.lang.reflect.Modifier.isStatic;

/**
 * A utility class for checking argument preconditions
 */
public final class Preconditions {

    private Preconditions() {
    }

    @Contract("null -> fail; !null -> param1")
    public static @NotNull <T> T checkNotNull(@Nullable T value) {
        return Objects.requireNonNull(value);
    }

    @Contract("null, _ -> fail; !null, _ -> param1")
    public static @NotNull <T> T checkNotNull(@Nullable T value, @NotNull String message) {
        return Objects.requireNonNull(value, message + " cannot be null!");
    }

    public static void checkCallableStatic(@Nullable Object instance, @NotNull Method method) {
        if (instance == null && !isStatic(method.getModifiers()))
            throw new IllegalArgumentException("The given method is not static, and no instance was provided. "
                    + "Either mark the function as static with @JvmStatic, or pass the object/companion object value for the instance.");
    }

}
