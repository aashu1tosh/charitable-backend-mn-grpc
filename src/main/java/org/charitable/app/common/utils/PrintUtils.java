package org.charitable.app.common.utils;


import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PrintUtils {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void prettyPrint(Object obj) {
        System.out.println(gson.toJson(obj));
    }
}
