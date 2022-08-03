package com.ihp.frontoffice.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

@Entity(tableName = "user")
public class User implements Serializable {

    private static final long serialVersionUID = -9176475615850942210L;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    private String userId;

    @ColumnInfo(name = "user_password")
    @SerializedName("user_password")
    private String userPassword;

    @ColumnInfo(name = "user_token")
    @SerializedName("user_token")
    private String userToken;

    @ColumnInfo(name = "user_token_sio")
    @SerializedName("user_token_sio")
    private String userTokenSio;

    @ColumnInfo(name = "level_user")
    @SerializedName("level_user")
    private String levelUser;

    @ColumnInfo(name = "last_login")
    @SerializedName("last_login")
    private String lastLogin;

    @ColumnInfo(name = "mfo_privileges_master_menu")
    @SerializedName("master_management_mfo")
    private boolean mfoPrivilegesMasterMenu;

    @ColumnInfo(name = "mfo_privileges_transaction_menu")
    @SerializedName("menu_transaksi_mfo")
    private boolean mfoPrivilegesTransactionMenu;

    @ColumnInfo(name = "mfo_privileges_sub_menu")
    @SerializedName("sub_menu_mfo")
    private boolean mfoPrivilegesSubMenu;

    @ColumnInfo(name = "role_fo")
    @SerializedName("fo")
    private boolean roleFo;

    @ColumnInfo(name = "role_pos")
    @SerializedName("pos")
    private boolean rolePos;

    @ColumnInfo(name = "role_pop")
    @SerializedName("pop")
    private boolean rolePop;

    @ColumnInfo(name = "is_login")
    @SerializedName("is_login")
    private boolean isLogin;


    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getLevelUser() {
        return levelUser;
    }

    public void setLevelUser(String levelUser) {
        this.levelUser = levelUser;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isMfoPrivilegesMasterMenu() {
        return mfoPrivilegesMasterMenu;
    }

    public void setMfoPrivilegesMasterMenu(boolean mfoPrivilegesMasterMenu) {
        this.mfoPrivilegesMasterMenu = mfoPrivilegesMasterMenu;
    }

    public boolean isMfoPrivilegesTransactionMenu() {
        return mfoPrivilegesTransactionMenu;
    }

    public void setMfoPrivilegesTransactionMenu(boolean mfoPrivilegesTransactionMenu) {
        this.mfoPrivilegesTransactionMenu = mfoPrivilegesTransactionMenu;
    }

    public boolean isMfoPrivilegesSubMenu() {
        return mfoPrivilegesSubMenu;
    }

    public void setMfoPrivilegesSubMenu(boolean mfoPrivilegesSubMenu) {
        this.mfoPrivilegesSubMenu = mfoPrivilegesSubMenu;
    }

    public boolean isRoleFo() {
        return roleFo;
    }

    public void setRoleFo(boolean roleFo) {
        this.roleFo = roleFo;
    }

    public boolean isRolePos() {
        return rolePos;
    }

    public void setRolePos(boolean rolePos) {
        this.rolePos = rolePos;
    }

    public boolean isRolePop() {
        return rolePop;
    }

    public void setRolePop(boolean rolePop) {
        this.rolePop = rolePop;
    }

    public String getUserTokenSio() {
        return userTokenSio;
    }

    public void setUserTokenSio(String userTokenSio) {
        this.userTokenSio = userTokenSio;
    }
}
