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
package revxrsal.args

import revxrsal.args.util.KotlinConstants.continuation
import java.lang.reflect.Parameter
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

/**
 * Calls a [KotlinFunction] in the current suspend context.
 *
 * If the callable is not a suspend function, it behaves as [KotlinFunction.callByNames].
 * Otherwise, calls the suspend function with current continuation.
 */
suspend fun <T> KotlinFunction.callSuspend(
    arguments: List<Any?>,
    isOptional: (Parameter) -> Boolean
): T {
    if (!isSuspend) {
        // Function is not suspend
        return call(arguments, isOptional)
    }

    val result = suspendCoroutineUninterceptedOrReturn<T> { continuation ->
        call(
            arguments + continuation,
            isOptional
        )
    }

    // If suspend function returns Unit and tail-call, it might appear, that it returns not Unit,
    // see comment above replaceReturnsUnitMarkersWithPushingUnitOnStack for explanation.
    // In this case, return Unit manually.
    @Suppress("UNCHECKED_CAST")
    if (method.method.returnType == Unit::class.java)
        return Unit as T
    return result
}

/**
 * Calls a [KotlinFunction] in the current suspend context.
 *
 * If the callable is not a suspend function, it behaves as [KotlinFunction.callByNames].
 * Otherwise, calls the suspend function with current continuation.
 */
suspend fun <T> KotlinFunction.callByNamesSuspend(
    arguments: Map<String, Any?>,
    isOptional: (Parameter) -> Boolean
): T {
    val lastParameter = parameters.last()
    if (lastParameter.type != continuation()) {
        // Function is not suspend
        return callByNames(arguments, isOptional)
    }

    val result = suspendCoroutineUninterceptedOrReturn<T> { continuation ->
        callByNames(
            arguments + (lastParameter.name to continuation),
            isOptional
        )
    }

    // If suspend function returns Unit and tail-call, it might appear, that it returns not Unit,
    // see comment above replaceReturnsUnitMarkersWithPushingUnitOnStack for explanation.
    // In this case, return Unit manually.
    @Suppress("UNCHECKED_CAST")
    if (method.method.returnType == Unit::class.java)
        return Unit as T
    return result
}

/**
 * Calls a [KotlinFunction] in the current suspend context.
 *
 * If the callable is not a suspend function, it behaves as [KotlinFunction.callByNames].
 * Otherwise, calls the suspend function with current continuation.
 */
suspend fun <T> KotlinFunction.callByIndicesSuspend(
    arguments: Map<Int, Any?>,
    isOptional: (Parameter) -> Boolean
): T {

    if (!isSuspend) {
        // Function is not suspend
        return callByIndices(arguments, isOptional)
    }

    val result = suspendCoroutineUninterceptedOrReturn<T> { continuation ->
        callByIndices(
            arguments + (parameters.lastIndex to continuation),
            isOptional
        )
    }

    // If suspend function returns Unit and tail-call, it might appear, that it returns not Unit,
    // see comment above replaceReturnsUnitMarkersWithPushingUnitOnStack for explanation.
    // In this case, return Unit manually.
    @Suppress("UNCHECKED_CAST")
    if (method.method.returnType == Unit::class.java)
        return Unit as T
    return result
}

/**
 * Calls a [KotlinFunction] in the current suspend context.
 *
 * If the callable is not a suspend function, it behaves as [KotlinFunction.callByNames].
 * Otherwise, calls the suspend function with current continuation.
 */
suspend fun <T> KotlinFunction.callByParametersSuspend(
    arguments: Map<Parameter, Any?>,
    isOptional: (Parameter) -> Boolean
): T {

    val lastParameter = parameters.last()
    if (lastParameter.type != continuation()) {
        // Function is not suspend
        return callByParameters(arguments, isOptional)
    }

    val result = suspendCoroutineUninterceptedOrReturn<T> { continuation ->
        callByParameters(
            arguments + (lastParameter to continuation),
            isOptional
        )
    }

    // If suspend function returns Unit and tail-call, it might appear, that it returns not Unit,
    // see comment above replaceReturnsUnitMarkersWithPushingUnitOnStack for explanation.
    // In this case, return Unit manually.
    @Suppress("UNCHECKED_CAST")
    if (method.method.returnType == Unit::class.java)
        return Unit as T
    return result
}
