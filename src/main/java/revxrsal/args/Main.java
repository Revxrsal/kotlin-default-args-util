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

import revxrsal.args.util.DefaultFunction;
import revxrsal.args.util.DefaultFunctionFinder;
import revxrsal.args.util.MissingParameterNameLogger;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Throwable {
        MissingParameterNameLogger.setEnabled(false);

        Method testStatic = Test.class.getMethod("testStatic", String.class, String.class, String.class);
        DefaultFunction defaultMethod = DefaultFunctionFinder.findDefaultFunction(
                null,
                testStatic
        );
        defaultMethod.callByParameterNames(new HashMap<String, Object>() {{
            put("arg1", "HELLO");
//            put("arg2", "BELLO");
            put("arg3", "CELLO");
        }}, p -> p.getName().equals("arg2"));
        defaultMethod.callByParameterNames(new HashMap<String, Object>() {{
            put("arg1", "HELLO");
            put("arg2", "BELLO");
            put("arg3", "CELLO");
        }}, p -> p.getName().equals("arg2"));
    }
}