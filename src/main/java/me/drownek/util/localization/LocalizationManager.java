package me.drownek.util.localization;

import java.util.HashMap;
import java.util.Map;

public class LocalizationManager {
    private static final Map<MessageKey, String> messages = new HashMap<>();

    static {
        messages.put(MessageKey.COMMAND_UTIL_SUCCESS_MESSAGE, "&aSuccess!");
        messages.put(MessageKey.COMMAND_UTIL_INVALID_ELEMENT, "&cWrong item!");
        messages.put(MessageKey.COMMAND_UTIL_ELEMENT_MISSING, "&cThere is no such item in the list!");
        messages.put(MessageKey.COMMAND_UTIL_ELEMENT_ALREADY_EXIST, "&cThe item is already on the list!");
        messages.put(MessageKey.COMMAND_UTIL_LIST_EMPTY, "&cList is empty!");
        messages.put(MessageKey.COMMAND_UTIL_LIST_CONTENTS, "&cList:");
        messages.put(MessageKey.COMMAND_UTIL_NO_SUCH_ELEMENT, "&cNo such element on the list!");

        messages.put(MessageKey.CONFIRM_GUI_ARE_YOU_SURE, "&cAre you sure?");
        messages.put(MessageKey.CONFIRM_GUI_YES, "&aYes");
        messages.put(MessageKey.CONFIRM_GUI_NO, "&cNo");

        messages.put(MessageKey.GUI_HELPER_BACK, "&cBack");
        messages.put(MessageKey.GUI_HELPER_NEXT, "&aNext page");
        messages.put(MessageKey.GUI_HELPER_PREVIOUS, "&cPrevious page");

        messages.put(MessageKey.TEXT_UTIL_NOTHING, "&cNothing.");
        messages.put(MessageKey.TEXT_UTIL_LOCATION_TELEPORT_FORMAT,
                "<click:run_command:'/tp {x} {y} {z}'><hover:show_text:Click to teleport>[tp]</hover></click>");

        messages.put(MessageKey.TIME_UTIL_NEVER, "&cNever");

        messages.put(MessageKey.WAITING_TASK_ALREADY_DOING_ACTION, "&cYou already in middle of some action!");
        messages.put(MessageKey.WAITING_TASK_SNEAK_TO_CANCEL, "Press {keybind} to cancel");
    }

    public static String getMessage(MessageKey key) {
        return getMessage(key, Map.of());
    }

    public static String getMessage(MessageKey key, Map<String, Object> placeholders) {
        String message = messages.getOrDefault(key, key.toString());

        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            String placeholder = entry.getKey();
            Object value = entry.getValue();
            message = message.replace(placeholder, String.valueOf(value));
        }

        return message;
    }
    
    public static void setMessage(MessageKey key, String value) {
        messages.put(key, value);
    }
}