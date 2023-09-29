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

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Parameter;
import java.util.StringJoiner;
import java.util.logging.Logger;

public final class MissingParameterNameLogger {

    @Getter
    @Setter
    private static boolean enabled = true;

    private static boolean warned = false;

    private MissingParameterNameLogger() {
    }

    public static void warnIfMissing(Parameter parameter) {
        if (enabled && !warned && !parameter.isNamePresent()) {
            StringJoiner message = new StringJoiner("\n");
            message.add("Parameters did not preserve their compile-time names");
            message.add("Attempting to invoke methods from parameter names may not work");
            message.add("To preserve names, compile the code with the '-parameters' flag.");
            message.add("To disable this warning, call 'MissingParameterNameLogger.setEnabled(false);'");
            Logger.getGlobal().warning(message.toString());
            warned = true;
        }
    }

}
