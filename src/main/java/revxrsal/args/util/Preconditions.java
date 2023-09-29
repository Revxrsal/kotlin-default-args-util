package revxrsal.args.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
}
