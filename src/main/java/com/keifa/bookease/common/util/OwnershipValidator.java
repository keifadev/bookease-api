package com.keifa.bookease.common.util;

import com.keifa.bookease.common.exception.UnauthorizedAccessException;

public class OwnershipValidator {
    private OwnershipValidator() {
    }

    public static void validateOwnership(Object resourceOwnerId, Object requesterId) {
        if (!resourceOwnerId.equals(requesterId)) {
            throw new UnauthorizedAccessException("Você não tem permissão para acessar este recurso");
        }
    }
}
