package net.saidora.api;

public enum ColorEnum {

    BLACK('0', "<black>"),
    DARK_BLUE('1', "<dark_blue>"),
    DARK_GREEN('2', "<dark_green>"),
    DARK_AQUA('3', "<dark_aqua>"),
    DARK_RED('4', "<dark_red>"),
    DARK_PURPLE('5', "<dark_purple>"),
    GOLD('6', "<gold>"),
    GRAY('7', "<gray>"),
    DARK_GRAY('8', "<dark_gray>"),
    BLUE('9', "<blue>"),
    GREEN('a', "<green>"),
    AQUA('b', "<aqua>"),
    RED('c', "<red>"),
    LIGHT_PURPLE('d', "<light_purple>"),
    YELLOW('e', "<yellow>"),
    WHITE('f', "<white>"),
    BOLD('l', "<b>"),
    STRIKETHROUGH('m', "<st>"),
    UNDERLINE('n', "<u>"),
    ITALIC('o', "<i>"),
    OBFUSCATED('k', "<obf>"),
    RESET('r', "<reset>");

    String key;
    String color;

    ColorEnum(char key, String color){
        this.key = String.valueOf(new char[]{'&', key});
        this.color = color;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public static String translateAlternateColorCodes(String context){
        for (ColorEnum value : values()) {
            context = context.replace(value.key, value.color);
        }
        return context;
    }
}
