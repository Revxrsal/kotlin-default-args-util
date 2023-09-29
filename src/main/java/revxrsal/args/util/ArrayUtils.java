package revxrsal.args.util;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ArrayUtils {

    private ArrayUtils() {
    }

    @Contract(pure = true, value = "null, _ -> fail; _, _ -> new")
    @CheckReturnValue
    public static <T> Object[] addFirst(
            @NotNull T[] original,
            @Nullable T item
    ) {
        Preconditions.checkNotNull(original, "original array");
        int newSize = original.length + 1;
        Object[] newArr = new Object[newSize];
        newArr[0] = item;
        System.arraycopy(original, 0, newArr, 1, original.length);
        return newArr;
    }

}
