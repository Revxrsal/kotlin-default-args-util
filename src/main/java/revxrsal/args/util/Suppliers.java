/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package revxrsal.args.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static revxrsal.args.util.Preconditions.checkNotNull;

public final class Suppliers {

    private Suppliers() {
    }

    public static <T> @NotNull Supplier<T> memoize(@NotNull Supplier<T> fetch) {
        checkNotNull(fetch, "fetch supplier");
        return new MemoizingSupplier<>(fetch);
    }

    static final class MemoizingSupplier<T extends @Nullable Object> implements Supplier<T> {
        final Supplier<T> delegate;
        transient volatile boolean initialized;
        // "value" does not need to be volatile; visibility piggy-backs
        // on volatile read of "initialized".
        @Nullable
        transient T value;

        MemoizingSupplier(Supplier<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public T get() {
            // A 2-field variant of Double Checked Locking.
            if (!initialized) {
                synchronized (this) {
                    if (!initialized) {
                        T t = delegate.get();
                        value = t;
                        initialized = true;
                        return t;
                    }
                }
            }
            // This is safe because we checked `initialized.`
            return value;
        }

        @Override
        public String toString() {
            return "Suppliers.memoize("
                    + (initialized ? "<supplier that returned " + value + ">" : delegate)
                    + ")";
        }
    }
}