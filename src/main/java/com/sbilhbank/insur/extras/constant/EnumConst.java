package com.sbilhbank.insur.extras.constant;

public class EnumConst {
    public static enum Permission {
        READ,
        WRITE,
        DELETE,
        UPDATE,
        MAKER,
        CHECKER,
        DOWNLOAD,
        UPLOAD
    };
    public static enum PrefixPermission {
      USER_,
      ROLE_,
      AUTHORITY_,
      DASHBOARD_
    };
    public static enum Role {
        DASHBOARD,
        ADMIN
    }
}
