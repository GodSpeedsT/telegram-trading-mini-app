package com.app.tradeguess.model.enums;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_SUPER_ADMIN;

    public boolean isAdmin() {
        return this == ROLE_ADMIN || this == ROLE_SUPER_ADMIN;
    }

    public boolean isSuperAdmin() {
        return this == ROLE_SUPER_ADMIN;
    }

    public boolean isRegularAdmin() {
        return this == ROLE_ADMIN;
    }

}
