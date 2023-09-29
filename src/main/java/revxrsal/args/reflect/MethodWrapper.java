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
package revxrsal.args.reflect;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import revxrsal.args.reflect.MethodCaller.BoundMethodCaller;
import revxrsal.args.util.Preconditions;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public final class MethodWrapper implements BoundMethodCaller {

    @Getter
    private final Method method;
    private final BoundMethodCaller caller;

    MethodWrapper(@NotNull Method method, @NotNull BoundMethodCaller caller) {
        this.method = method;
        this.caller = caller;
    }

    public Class<?> getDeclaringClass() {
        return method.getDeclaringClass();
    }

    public String getName() {
        return method.getName();
    }

    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    public int getParameterCount() {
        return method.getParameterCount();
    }

    public Parameter[] getParameters() {
        return method.getParameters();
    }

    @Override
    public Object call(Object... arguments) {
        return caller.call(arguments);
    }

    public static MethodWrapper of(@NotNull Method method, @NotNull BoundMethodCaller caller) {
        Preconditions.checkNotNull(method, "method");
        Preconditions.checkNotNull(caller, "caller");
        return new MethodWrapper(method, caller);
    }

}
