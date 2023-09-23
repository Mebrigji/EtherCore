package net.saidora.api.helpers;


import com.iridium.iridiumcolorapi.IridiumColorAPI;

import java.util.List;
import java.util.Objects;

public class ColorHelper {

    /**
     * @param context - Raw context whose going to process translate
     * @return Translated Colors with Pattern
     */
    public static String applyColors(String context){
        Objects.requireNonNull(context, "String");
        return IridiumColorAPI.process(context);
    }

    /**
     * @param list - Raw context whose going to process translate
     * @return Translated Colors with Pattern
     */
    public static List<String> applyColors(List<String> list){
        Objects.requireNonNull(list, "List<String>");
        return list.stream().map(ColorHelper::applyColors).toList();
    }

}
