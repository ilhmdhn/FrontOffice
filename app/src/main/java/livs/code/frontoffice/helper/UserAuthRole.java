package livs.code.frontoffice.helper;

import livs.code.frontoffice.data.entity.User;

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
}