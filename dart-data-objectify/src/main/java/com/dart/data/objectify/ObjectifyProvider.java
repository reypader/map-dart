package com.dart.data.objectify;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Created by RMPader on 7/25/15.
 */
public class ObjectifyProvider {

    public static Objectify objectify() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }

    public static void register(Class<?> clazz) {
        ObjectifyService.register(clazz);
    }

}
