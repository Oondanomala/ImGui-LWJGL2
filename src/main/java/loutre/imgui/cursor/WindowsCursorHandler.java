package loutre.imgui.cursor;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HCURSOR;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.W32APIOptions;
import imgui.flag.ImGuiMouseCursor;

/**
 * Implementation of {@code CursorHandler} for Windows.
 */
final class WindowsCursorHandler implements CursorHandler {
    private WindowsCursorHandler() {
    }

    static final WindowsCursorHandler INSTANCE = new WindowsCursorHandler();
    private static final HCURSOR[] cursors = new HCURSOR[ImGuiMouseCursor.COUNT];

    static {
        Native.register(NativeLibrary.getInstance("user32", W32APIOptions.DEFAULT_OPTIONS));

        // See https://github.com/java-native-access/jna/blob/2cee7f3193fd65812a4a5d8c1dfb821bf6693dd6/contrib/platform/src/com/sun/jna/platform/win32/WinUser.java#L1712
        // and https://github.com/libsdl-org/SDL/blob/412a7c5db639399b1bbaa4516d56f390884ea28b/include/SDL3/SDL_mouse.h#L97
        cursors[ImGuiMouseCursor.Arrow] = loadCursor(32512);
        cursors[ImGuiMouseCursor.TextInput] = loadCursor(32513);
        cursors[ImGuiMouseCursor.ResizeAll] = loadCursor(32646);
        cursors[ImGuiMouseCursor.ResizeNS] = loadCursor(32645);
        cursors[ImGuiMouseCursor.ResizeEW] = loadCursor(32644);
        cursors[ImGuiMouseCursor.ResizeNESW] = loadCursor(32643);
        cursors[ImGuiMouseCursor.ResizeNWSE] = loadCursor(32642);
        cursors[ImGuiMouseCursor.Hand] = loadCursor(32649);
        cursors[ImGuiMouseCursor.Wait] = loadCursor(32514);
        cursors[ImGuiMouseCursor.Progress] = loadCursor(32650);
        cursors[ImGuiMouseCursor.NotAllowed] = loadCursor(32648);
    }

    private static HCURSOR loadCursor(int cursor) {
        WinNT.HANDLE handle = LoadImage(
                null, cursor,
                WinUser.IMAGE_CURSOR, 0, 0,
                WinUser.LR_DEFAULTSIZE | WinUser.LR_SHARED
        );
        if (handle == null) {
            throw new IllegalStateException("Failed to load cursor image for cursor " + cursor);
        }
        return new HCURSOR(handle.getPointer());
    }

    public void setCursor(int imGuiCursorShape) {
        if (imGuiCursorShape == -1) {
            SetCursor(null);
        } else {
            SetCursor(cursors[imGuiCursorShape]);
        }
    }

    /**
     * @see <a href="https://learn.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-loadimagea">MSDN for documentation</a>
     */
    private static native WinNT.HANDLE LoadImage(WinDef.HINSTANCE hInst, int name, int type, int cx, int cy, int fuLoad);
    /**
     * @see <a href="https://learn.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-setcursor">MSDN for documentation</a>
     */
    private static native HCURSOR SetCursor(HCURSOR cursor);
}
