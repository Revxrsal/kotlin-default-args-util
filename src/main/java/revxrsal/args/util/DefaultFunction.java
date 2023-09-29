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

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.args.KotlinFunction;
import revxrsal.args.reflect.MethodWrapper;

import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static revxrsal.args.util.KotlinConstants.defaultPrimitiveValue;
import static revxrsal.args.util.MissingParameterNameLogger.warnIfMissing;

@Data
public final class DefaultFunction implements KotlinFunction {

    private final MethodWrapper mainMethod;
    private final MethodWrapper defaultMethod;

    @Override
    public <T> T callByIndices(@NotNull Map<Integer, Object> arguments, @NotNull Function<Parameter, Boolean> isOptional) {
        return null;
    }

    @Override
    public <T> T callByParameterNames(@NotNull Map<String, Object> arguments, @NotNull Function<Parameter, Boolean> isOptional) {
        return callDefaultMethod(
                (parameter, _maskIndex) -> {
                    warnIfMissing(parameter);
                    return arguments.get(parameter.getName());
                },
                isOptional
        );
    }

    @Override
    public <T> T callByParameters(@NotNull Map<Parameter, Object> arguments, @NotNull Function<Parameter, Boolean> isOptional) {
        return null;
    }

    // Re-adapted from KCallableImpl#callDefaultMethod
    @SuppressWarnings("unchecked")
    private <T> T callDefaultMethod(
            BiFunction<Parameter, Integer, @Nullable Object> getProvidedArgument,
            Function<Parameter, Boolean> isOptional
    ) {
        List<Object> args = new ArrayList<>(defaultMethod.getParameterCount() + 2);
        int mask = 0;
        List<Integer> masks = new ArrayList<>(1);
        int maskIndex = 0;

        boolean anyOptional = false;
        Parameter[] parameters = defaultMethod.getParameters();
        int pLength = parameters.length;
        for (int paramIndex = 0; paramIndex < parameters.length; paramIndex++) {
            Parameter parameter = parameters[paramIndex];
            if (maskIndex != 0 && maskIndex % Integer.SIZE == 0) {
                masks.add(mask);
                mask = 0;
            }
            @Nullable Object providedArg = getProvidedArgument.apply(parameter, maskIndex);
            if (parameter.getType() == defaultMethod.getDeclaringClass()) {
                continue;
            }
            else if (providedArg != null) {
                args.add(providedArg);
            }

            // Parameter is not present

            else if (isOptional.apply(parameter)) {
                mask = mask | 1 << maskIndex % 32;
                args.add(defaultPrimitiveValue(parameter.getType()));
                anyOptional = true;
                continue;
            } else if (parameter.isVarArgs()) {
                args.add(Array.newInstance(parameter.getType(), 0));
            } else if (paramIndex == pLength - 1 || paramIndex == pLength - 2)
                // Synthetic parameters (the mask and the marker)
                continue;
            else {
                throw new IllegalArgumentException("No argument provided for a required parameter: " + parameter + ".");
            }
            maskIndex++;
        }

        // TODO: Check for continuation argument??

        if (!anyOptional) {
            return (T) getMainMethod().call(args.toArray());
        }

        masks.add(mask);
        args.addAll(masks);

        // DefaultConstructorMarker or MethodHandle
        args.add(null);
        return (T) getDefaultMethod().call(args.toArray());
    }
}