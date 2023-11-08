package com.full_party.util;

import com.full_party.auth.userdetails.UserDetail;
import org.springframework.security.core.userdetails.UserDetails;

public class Utility {

    public static Long getUserId(UserDetails userDetails) {
        return ((UserDetail) userDetails).getId();
    }
}
