package loutre.imgui.cursor;

import com.sun.jna.Platform;

/**
 * Optional interface to change cursor shape
 * to standard OS defined cursors.
 * <p>
 * Unfortunately there is no API for
 * doing this in LWJGL2, so we have to
 * use JNA and talk directly to the OS.
 * If JNA is not available no
 * implementations are returned.
 */
public interface CursorHandler {
    /**
     * Gets the handler instance appropriate for
     * the current operating system.
     * <p>
     * Returns <tt>null</tt> if no handler for the current OS
     * is found, if initializing the appropriate handler fails
     * (including if JNA is not found in the classpath), or if the
     * {@code imgui.lwjgl2.disableNative} system property is <tt>true</tt>.
     */
    static CursorHandler getInstance() {
        if (Boolean.getBoolean("imgui.lwjgl2.disableNative")) return null;
        try {
            if (Platform.isWindows()) {
                return WindowsCursorHandler.INSTANCE;
            } else if (Platform.isLinux()) {
                return X11CursorHandler.INSTANCE;
            }
            // TODO: Implement support for other platforms
        } catch (Throwable e) {
            // Something failed to initialize, ignore
        }
        return null;
    }

    /**
     * Sets the cursor shape to one of the supported
     * ImGui shapes from {@link imgui.flag.ImGuiMouseCursor}.
     *
     * @param imGuiCursorShape The shape of the cursor
     * @throws ArrayIndexOutOfBoundsException If {@code imGuiCursorShape} is not a valid cursor shape
     */
    void setCursor(int imGuiCursorShape);
}
