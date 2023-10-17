/*
 * This file is part of kotlin-default-args-util, licensed under the MIT License.
 *
 *  Copyright (c) Revxrsal <reflxction.github@gmail.com>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
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

    /**
     * Throws any throwable 'sneakily' - you don't need to catch it, nor declare that you throw it onwards.
     * The exception is still thrown - javac will just stop whining about it.
     * <p>
     * Example usage:
     * <pre>public void run() {
     *     throw sneakyThrow(new IOException("You don't need to catch me!"));
     * }</pre>
     * <p>
     * NB: The exception is not wrapped, ignored, swallowed, or redefined. The JVM actually does not know or care
     * about the concept of a 'checked exception'. All this method does is hide the act of throwing a checked exception
     * from the java compiler.
     * <p>
     * Note that this method has a return type of {@code RuntimeException}; it is advised you always call this
     * method as argument to the {@code throw} statement to avoid compiler errors regarding no return
     * statement and similar problems. This method won't of course return an actual {@code RuntimeException} -
     * it never returns, it always throws the provided exception.
     *
     * @param t The throwable to throw without requiring you to catch its type.
     * @return A dummy RuntimeException; this method never returns normally, it <em>always</em> throws an exception!
     */
    public static RuntimeException sneakyThrow(Throwable t) {
        if (t == null) throw new NullPointerException("t");
        return Preconditions.sneakyThrow0(t);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> T sneakyThrow0(Throwable t) throws T {
        throw (T)t;
    }

}
