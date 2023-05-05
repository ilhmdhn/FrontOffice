package com.ihp.frontoffice.helper;

import com.ihp.frontoffice.data.entity.User;

public class UserAuthRole {

     public static boolean isAllowTransferRoom(User user){

        if (user.getLevelUser().equals(UserRole.SUPERVISOR.getRole()) ||
                user.getLevelUser().equals(UserRole.ACCOUNTING.getRole()) ||
                user.getLevelUser().equals(UserRole.KAPTEN.getRole())){
            return true;
        }
        return false;
    }

    public static boolean isAllowCancelOrder(User user){
        if (user.getLevelUser().equals(UserRole.SUPERVISOR.getRole()) ||
                user.getLevelUser().equals(UserRole.ACCOUNTING.getRole()) ||
                user.getLevelUser().equals(UserRole.KAPTEN.getRole())){
            return true;
        }
        return false;
    }

    public static boolean isAllowPiutangComplimentPayment(User user){
        if (user.getLevelUser().equals(UserRole.SUPERVISOR.getRole()) ||
                user.getLevelUser().equals(UserRole.ACCOUNTING.getRole()) ||
                user.getLevelUser().equals(UserRole.KAPTEN.getRole())){
            return true;
        }
        return false;
    }

    public static boolean isAllowCancelPromotion(User user){
        if (user.getLevelUser().equals(UserRole.SUPERVISOR.getRole()) ||
                user.getLevelUser().equals(UserRole.ACCOUNTING.getRole()) ||
                user.getLevelUser().equals(UserRole.KAPTEN.getRole())){
            return true;
        }
        return false;
    }

    public static boolean isAllowReduceCheckinDuration(User user){
        if (user.getLevelUser().equals(UserRole.SUPERVISOR.getRole()) ||
                user.getLevelUser().equals(UserRole.ACCOUNTING.getRole()) ||
                user.getLevelUser().equals(UserRole.KAPTEN.getRole())){
            return true;
        }
        return false;
    }

    public static boolean isAllowReprintBill(User user){
        if (user.getLevelUser().equals(UserRole.SUPERVISOR.getRole()) ||
                user.getLevelUser().equals(UserRole.ACCOUNTING.getRole()) ||
                user.getLevelUser().equals(UserRole.KAPTEN.getRole())){
            return true;
        }
        return false;
    }

    public static boolean isAllowReprintInvoice(User user){
        if (user.getLevelUser().equals(UserRole.SUPERVISOR.getRole()) ||
                user.getLevelUser().equals(UserRole.ACCOUNTING.getRole()) ||
                user.getLevelUser().equals(UserRole.KAPTEN.getRole())){
            return true;
        }
        return false;
    }

    public static boolean isAllowSendRPH(User user){
        if (user.getLevelUser().equals(UserRole.SUPERVISOR.getRole()) ||
                user.getLevelUser().equals(UserRole.ACCOUNTING.getRole()) ||
                user.getLevelUser().equals(UserRole.KAPTEN.getRole())){
            return true;
        }
        return false;
    }

    public static boolean isAllowReceiveNotifCall(User user){
        if (user.getLevelUser().equals(UserRole.KASIR.getRole()) ||
                user.getLevelUser().equals(UserRole.WAITER.getRole()) ||
                user.getLevelUser().equals(UserRole.SERVER.getRole()) ||
                user.getLevelUser().equals(UserRole.ACCOUNTING.getRole())
        ){
            return true;
        }
        return false;
    }

    public static boolean isAllowReceiveNotifSo(User user){
        if (user.getLevelUser().equals(UserRole.KASIR.getRole()) ||
                user.getLevelUser().equals(UserRole.WAITER.getRole()) ||
                user.getLevelUser().equals(UserRole.SERVER.getRole()) ||
                user.getLevelUser().equals(UserRole.ACCOUNTING.getRole())
        ){
            return true;
        }
        return false;
    }

    public static boolean isAllowReceiveNotifDo(User user){
        if (user.getLevelUser().equals(UserRole.KASIR.getRole()) ||
                user.getLevelUser().equals(UserRole.WAITER.getRole()) ||
                user.getLevelUser().equals(UserRole.SERVER.getRole()) ||
                user.getLevelUser().equals(UserRole.ACCOUNTING.getRole())
        ){
            return true;
        }
        return false;
    }
}