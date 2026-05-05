package com.keifa.bookease.common.util;

import com.keifa.bookease.common.exception.UnauthorizedAccessException;
import org.springframework.lang.NonNull;

public class OwnershipValidator {
    private OwnershipValidator() {
    }

    public static void validateOwnership(Object owner, Object entity) {
        if (!owner.equals(entity)) {
            throw new UnauthorizedAccessException("You are not authorized to access this resource");
        }
    }
}
