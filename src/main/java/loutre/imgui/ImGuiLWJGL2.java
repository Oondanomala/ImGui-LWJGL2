package loutre.imgui;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.extension.implot.ImPlot;
import imgui.flag.ImGuiConfigFlags;

public final class ImGuiLWJGL2 {
    private final static ImGuiPlatform IMGUI_PLATFORM = new ImGuiPlatform();
    private final static ImGuiRenderer IMGUI_RENDERER = new ImGuiRenderer();

    private static boolean isCreated = false;

    private ImGuiLWJGL2() {
    }

    public static void create(boolean initImPlot) {
        if (isCreated) return;

        ImGui.createContext();
        if (initImPlot) {
            ImPlot.createContext();
        }

        final ImGuiIO data = ImGui.getIO();
        data.setIniFilename("imguilib.ini");
        data.setFontGlobalScale(1F);

        // data.setConfigFlags(ImGuiConfigFlags.DockingEnable);
        // In case you want to enable Viewports on Windows, you have to do this instead of the above line:
        // data.setConfigFlags(ImGuiConfigFlags.DockingEnable | ImGuiConfigFlags.ViewportsEnable);

        IMGUI_PLATFORM.init();
        IMGUI_RENDERER.init();
        isCreated = true;
    }

    public static boolean isCreated() {
        return isCreated;
    }

    public static void handleKey() {
        IMGUI_PLATFORM.onKey();
    }

    public static void handleMouse() {
        IMGUI_PLATFORM.onMouse();
    }

    public static void draw(final Runnable runnable) {
        if (isCreated) {
            IMGUI_PLATFORM.newFrame();
            ImGui.newFrame();
            runnable.run();
            ImGui.render();

            IMGUI_RENDERER.newFrame();
            IMGUI_RENDERER.renderDrawData(ImGui.getDrawData());

            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
            }
        }
    }

    /**
     * @deprecated With the new font ImGui font system this will soon be unnecessary
     *             (once ImGui-Java adds bindings for the stuff we need to implement texture support)
     */
    @Deprecated
    public static short[] getGlyphRangesChineseFull() {
        char[] ranges = {
                0x0020, 0x00FF,
                0x2000, 0x206F,
                0x3000, 0x30FF,
                0x31F0, 0x31FF,
                0xFF00, 0xFFEF,
                0xFFFD, 0xFFFD,
                0x4e00, 0x9FAF,
                0
        };

        short[] convertedRanges = new short[ranges.length];
        for (int i = 0; i < ranges.length; i++) {
            convertedRanges[i] = (short) ranges[i];
        }

        return convertedRanges;
    }
}
