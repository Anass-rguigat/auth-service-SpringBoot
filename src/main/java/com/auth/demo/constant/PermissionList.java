package com.auth.demo.constant;

import java.util.List;

public class PermissionList {

    public static final List<String> DEFAULT_PERMISSIONS = List.of(
        "READ_USERS",
        "EDIT_USERS",
        "DELETE_USERS",
        "MANAGE_ROLES",
        "VIEW_REPORTS"
    );

    private PermissionList() {}
}
