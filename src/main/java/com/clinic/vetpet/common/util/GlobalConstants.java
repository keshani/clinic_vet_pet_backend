package com.clinic.vetpet.common.util;

import java.util.Set;

public class GlobalConstants {

    public static String IN_MEMORY_URL = "/h2-console/**";
    public static String AUTHENTICATE_URL = "/clinicvetpet/v1/authenticate";
    public static String USER_REGISTER_URL = "/clinicvetpet/v1/userInfoHandler/registerUser";
    public static Set<String> WHITE_LIST_URL = Set.of("h2-console","authenticate","registerUser");


}
