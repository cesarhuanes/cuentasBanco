package com.bancos.cuentasbancarias.util;

import org.bson.types.ObjectId;

public class Util {
    public static String convertObjectIdToString(ObjectId objectId) {
        // Devolver el ObjectId como una cadena
        return objectId.toHexString();
    }

}
