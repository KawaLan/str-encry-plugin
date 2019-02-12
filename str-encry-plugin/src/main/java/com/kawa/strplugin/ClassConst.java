package com.kawa.strplugin;

import java.util.HashMap;
import java.util.Map;

public final class ClassConst {

    public static String NATIVE_ENCRYPT = "-";
    public static String DECRYPT_METHOD_NAME = "-";
    public final static String DECRYPT_METHOD_SIGN = "(Ljava/lang/String;)Ljava/lang/String;";
    public static Map<String, GuardField> guardFinalStaticFieldMaps = new HashMap<>();
    public static Map<String, GuardField> guardFinalFiedlMaps = new HashMap<>();
}
