package me.drownek.util.localization;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocalizationManager {
    private static final Map<Locale, Map<MessageKey, String>> messages = new HashMap<>();

    public static Locale DEFAULT_LOCALE = new Locale("pl", "PL");

    static {
        var englishMessages = new HashMap<MessageKey, String>();
        englishMessages.put(MessageKey.COMMAND_UTIL_SUCCESS_MESSAGE, "&aSuccess!");
        englishMessages.put(MessageKey.COMMAND_UTIL_INVALID_ELEMENT, "&cWrong item!");
        englishMessages.put(MessageKey.COMMAND_UTIL_ELEMENT_MISSING, "&cThere is no such item in the list!");
        englishMessages.put(MessageKey.COMMAND_UTIL_ELEMENT_ALREADY_EXIST, "&cThe item is already on the list!");
        englishMessages.put(MessageKey.COMMAND_UTIL_LIST_EMPTY, "&cList is empty!");
        englishMessages.put(MessageKey.COMMAND_UTIL_LIST_CONTENTS, "&cList:");
        englishMessages.put(MessageKey.COMMAND_UTIL_NO_SUCH_ELEMENT, "&cNo such element on the list!");

        englishMessages.put(MessageKey.CONFIRM_GUI_ARE_YOU_SURE, "&cAre you sure?");
        englishMessages.put(MessageKey.CONFIRM_GUI_YES, "&aYes");
        englishMessages.put(MessageKey.CONFIRM_GUI_NO, "&cNo");

        englishMessages.put(MessageKey.GUI_HELPER_BACK, "&cBack");
        englishMessages.put(MessageKey.GUI_HELPER_NEXT, "&aNext page");
        englishMessages.put(MessageKey.GUI_HELPER_PREVIOUS, "&cPrevious page");

        englishMessages.put(MessageKey.TEXT_UTIL_NOTHING, "&cNothing.");
        englishMessages.put(MessageKey.TEXT_UTIL_LOCATION_TELEPORT_FORMAT,
                "<click:run_command:'/tp {x} {y} {z}'><hover:show_text:Click to teleport>[tp]</hover></click>");

        englishMessages.put(MessageKey.TIME_UTIL_NEVER, "&cNever");

        englishMessages.put(MessageKey.WAITING_TASK_ALREADY_DOING_ACTION, "&cYou already in middle of some action!");
        englishMessages.put(MessageKey.WAITING_TASK_SNEAK_TO_CANCEL, "Press {keybind} to cancel");

        englishMessages.put(MessageKey.AMOUNT_GUI_CONFIRM, "&aConfirm");
        englishMessages.put(MessageKey.AMOUNT_GUI_CANCEL, "&aCancel");
        messages.put(Locale.ENGLISH, englishMessages);

        var polishMessages = new HashMap<MessageKey, String>();
        polishMessages.put(MessageKey.COMMAND_UTIL_SUCCESS_MESSAGE, "&aSukces!");
        polishMessages.put(MessageKey.COMMAND_UTIL_INVALID_ELEMENT, "&cZły item!");
        polishMessages.put(MessageKey.COMMAND_UTIL_ELEMENT_MISSING, "&cNie ma takiego elementu na liście!");
        polishMessages.put(MessageKey.COMMAND_UTIL_ELEMENT_ALREADY_EXIST, "&cElement jest już na liście!");
        polishMessages.put(MessageKey.COMMAND_UTIL_LIST_EMPTY, "&cLista jest pusta!");
        polishMessages.put(MessageKey.COMMAND_UTIL_LIST_CONTENTS, "&cLista:");
        polishMessages.put(MessageKey.COMMAND_UTIL_NO_SUCH_ELEMENT, "&cNie ma takiego elementu na liście!");

        polishMessages.put(MessageKey.CONFIRM_GUI_ARE_YOU_SURE, "&cCzy na pewno?");
        polishMessages.put(MessageKey.CONFIRM_GUI_YES, "&aTak");
        polishMessages.put(MessageKey.CONFIRM_GUI_NO, "&cNie");

        polishMessages.put(MessageKey.GUI_HELPER_BACK, "&cPowrót");
        polishMessages.put(MessageKey.GUI_HELPER_NEXT, "&aNastępna strona");
        polishMessages.put(MessageKey.GUI_HELPER_PREVIOUS, "&cPoprzednia strona");

        polishMessages.put(MessageKey.TEXT_UTIL_NOTHING, "&cBrak.");
        polishMessages.put(MessageKey.TEXT_UTIL_LOCATION_TELEPORT_FORMAT,
            "<click:run_command:'/tp {x} {y} {z}'><hover:show_text:Kliknij aby teleportować>[tp]</hover></click>");

        polishMessages.put(MessageKey.TIME_UTIL_NEVER, "&cNigdy");

        polishMessages.put(MessageKey.WAITING_TASK_ALREADY_DOING_ACTION, "&cJesteś już w trakcie jakiejś akcji!");
        polishMessages.put(MessageKey.WAITING_TASK_SNEAK_TO_CANCEL, "Naciśnij {keybind} aby anulować");

        polishMessages.put(MessageKey.AMOUNT_GUI_CONFIRM, "&aPotwierdź");
        polishMessages.put(MessageKey.AMOUNT_GUI_CANCEL, "&aAnuluj");

        messages.put(new Locale("pl", "PL"), polishMessages);
    }

    public static String getMessage(MessageKey key) {
        return getMessage(key, Map.of());
    }

    public static String getMessage(Locale locale, MessageKey key, Map<String, Object> placeholders) {
        String message = messages.get(locale).getOrDefault(key, key.toString());

        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            String placeholder = entry.getKey();
            Object value = entry.getValue();
            message = message.replace(placeholder, String.valueOf(value));
        }

        return message;
    }

    public static String getMessage(MessageKey key, Map<String, Object> placeholders) {
        return getMessage(DEFAULT_LOCALE, key, placeholders);
    }
    
    public static void setMessage(Locale locale, MessageKey key, String value) {
        messages.computeIfAbsent(locale, locale1 -> new HashMap<>()).put(key, value);
    }
}