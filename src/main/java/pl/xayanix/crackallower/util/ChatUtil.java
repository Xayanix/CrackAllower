package pl.xayanix.crackallower.util;

public class ChatUtil {

    public static String fixColors(final String message) {
        if (message == null) {
            return "";
        }
        return message.replace("&", "§");
    }

}
