package loutre.imgui.cursor;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.X11;
import imgui.flag.ImGuiMouseCursor;
import org.lwjgl.opengl.Display;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Implementation of {@code CursorHandler} for X11.
 * <p>
 * Note that LWJGL2 does not support Wayland,
 * so there is no Wayland implementation.
 */
final class X11CursorHandler implements CursorHandler {
    private X11CursorHandler() {
    }

    static final X11CursorHandler INSTANCE = new X11CursorHandler();
    private static final X11.Display display;
    private static final X11.Window window;
    private static final X11.Cursor[] cursors = new X11.Cursor[ImGuiMouseCursor.COUNT];

    static {
        Native.register("Xcursor");

        try {
            // Get the X11 display & window handles through LWJGL2
            Method getImplMethod = Display.class.getDeclaredMethod("getImplementation");
            getImplMethod.setAccessible(true);
            Object displayImpl = getImplMethod.invoke(null);
            Class<?> implClass = displayImpl.getClass();
            Field displayField = implClass.getDeclaredField("display");
            displayField.setAccessible(true);
            Field windowField = implClass.getDeclaredField("current_window");
            windowField.setAccessible(true);
            long displayPtr = displayField.getLong(displayImpl);
            long windowPtr = windowField.getLong(displayImpl);
            display = new X11.Display();
            display.setPointer(new Pointer(displayPtr));
            window = new X11.Window(windowPtr);

            // See https://github.com/libsdl-org/SDL/blob/5924f36dc6e3d00cf3d25f81d18c5e1bce75ed7c/src/video/SDL_video.c#L6458
            // and https://github.com/libsdl-org/SDL/blob/412a7c5db639399b1bbaa4516d56f390884ea28b/include/SDL3/SDL_mouse.h#L97
            cursors[ImGuiMouseCursor.Arrow] = loadCursor("default");
            cursors[ImGuiMouseCursor.TextInput] = loadCursor("text");
            cursors[ImGuiMouseCursor.ResizeAll] = loadCursor("move");
            cursors[ImGuiMouseCursor.ResizeNS] = loadCursor("ns-resize");
            cursors[ImGuiMouseCursor.ResizeEW] = loadCursor("ew-resize");
            cursors[ImGuiMouseCursor.ResizeNESW] = loadCursor("nesw-resize");
            cursors[ImGuiMouseCursor.ResizeNWSE] = loadCursor("nwse-resize");
            cursors[ImGuiMouseCursor.Hand] = loadCursor("pointer");
            cursors[ImGuiMouseCursor.Wait] = loadCursor("wait");
            cursors[ImGuiMouseCursor.Progress] = loadCursor("progress");
            cursors[ImGuiMouseCursor.NotAllowed] = loadCursor("not-allowed");
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static X11.Cursor loadCursor(String name) {
        X11.Cursor cursor = XcursorLibraryLoadCursor(display, name);
        if (cursor == null) {
            throw new IllegalStateException("Failed to load cursor " + name);
        }
        return cursor;
    }

    @Override
    public void setCursor(int imGuiCursorShape) {
        if (imGuiCursorShape == -1) {
            // TODO: Hide cursor (rarely used)
        } else {
            XDefineCursor(display, window, cursors[imGuiCursorShape]);
        }
    }

    /**
     * @see <a href="https://xorg.freedesktop.org/archive/current/doc/man/man3/Xcursor.3.xhtml">Xorg documentation</a>
     */
    private static native X11.Cursor XcursorLibraryLoadCursor(X11.Display display, String name);

    /**
     * @see <a href="https://xorg.freedesktop.org/archive/current/doc/man/man3/XDefineCursor.3.xhtml">Xorg documentation</a>
     */
    private static native int XDefineCursor(X11.Display display, X11.Window window, X11.Cursor cursor);
}
