package livs.code.frontoffice.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import livs.code.frontoffice.data.entity.RoomType;
import livs.code.frontoffice.data.remote.ApiRestService;
import livs.code.frontoffice.data.remote.PromoInventoryClient;
import livs.code.frontoffice.data.remote.RoomClient;
import livs.code.frontoffice.data.remote.RoomOrderClient;
import livs.code.frontoffice.data.remote.PromoRoomClient;
import livs.code.frontoffice.data.remote.RoomTypeClient;
import livs.code.frontoffice.data.remote.respons.FoodPromoResponse;
import livs.code.frontoffice.data.remote.respons.RoomOrderResponse;
import livs.code.frontoffice.data.remote.respons.RoomPromoResponse;
import livs.code.frontoffice.data.remote.respons.RoomListResponse;
import livs.code.frontoffice.data.remote.respons.RoomTypeResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteRepository {
    private static RemoteRepository remoteRepository;
    private RoomClient roomClient;
    private RoomTypeClient roomTypeClient;
    private PromoRoomClient promoRoomClient;
    private PromoInventoryClient promoInventoryClient;
    private RoomOrderClient roomOrderClient;

    private RemoteRepository(String baseUrl) {
        this.roomClient = ApiRestService.getClient(baseUrl).create(RoomClient.class);
        this.roomTypeClient = ApiRestService.getClient(baseUrl).create(RoomTypeClient.class);
        this.promoRoomClient = ApiRestService.getClient(baseUrl).create(PromoRoomClient.class);
        this.promoInventoryClient = ApiRestService.getClient(baseUrl).create(PromoInventoryClient.class);
        this.roomOrderClient = ApiRestService.getClient(baseUrl).create(RoomOrderClient.class);
    }

    public static RemoteRepository getInstance(String baseUrl) {
        if (remoteRepository == null) {
            remoteRepository = new RemoteRepository(baseUrl);
        }
        return remoteRepository;
    }

    public MutableLiveData<RoomListResponse> getAllRoom(String cariData){
        MutableLiveData<RoomListResponse> allRooms = new MutableLiveData<>();
        roomClient.getAllRoom(cariData).enqueue(new Callback<RoomListResponse>() {
            @Override
            public void onResponse(Call<RoomListResponse> call, Response<RoomListResponse> response) {
                RoomListResponse res = response.body();
                if (!res.isOkay()) {
                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                    /*res.setMessage(errorReport.toString());*/
                    Log.e("ERROR SERVER",errorReport.toString());
                }
                allRooms.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomListResponse> call, Throwable t) {

                RoomListResponse res = new RoomListResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());

                allRooms.setValue(res);
            }
        });
        return allRooms;
    }

    public MutableLiveData<RoomListResponse> getAllRoomHistory(String cariData){
        MutableLiveData<RoomListResponse> allRooms = new MutableLiveData<>();
        roomClient.getAllRoomHistory(0,cariData).enqueue(new Callback<RoomListResponse>() {
            @Override
            public void onResponse(Call<RoomListResponse> call, Response<RoomListResponse> response) {
                RoomListResponse res = response.body();
                if (!res.isOkay()) {
                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                    /*res.setMessage(errorReport.toString());*/
                    Log.e("ERROR SERVER",errorReport.toString());
                }
                allRooms.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomListResponse> call, Throwable t) {

                RoomListResponse res = new RoomListResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());

                allRooms.setValue(res);
            }
        });
        return allRooms;
    }

    public MutableLiveData<RoomListResponse> getAllRoomReady(){
        MutableLiveData<RoomListResponse> allRooms = new MutableLiveData<>();
        roomClient.getAllRoomReady().enqueue(new Callback<RoomListResponse>() {
            @Override
            public void onResponse(Call<RoomListResponse> call, Response<RoomListResponse> response) {
                RoomListResponse res = response.body();
                if (!res.isOkay()) {

                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                    /*res.setMessage(errorReport.toString());*/
                    Log.e("ERROR SERVER",errorReport.toString());
                }
                allRooms.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomListResponse> call, Throwable t) {
                RoomListResponse res = new RoomListResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());

                allRooms.setValue(res);

            }
        });
        return allRooms;
    }

    public MutableLiveData<RoomListResponse> getAllRoomCheckin(String roomCode){
        MutableLiveData<RoomListResponse> allRooms = new MutableLiveData<>();
        roomClient.getAllRoomCheckin(roomCode).enqueue(new Callback<RoomListResponse>() {
            @Override
            public void onResponse(Call<RoomListResponse> call, Response<RoomListResponse> response) {
                RoomListResponse res = response.body();
                if (!res.isOkay()) {

                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                    /*res.setMessage(errorReport.toString());*/
                    Log.e("ERROR SERVER",errorReport.toString());
                }
                allRooms.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomListResponse> call, Throwable t) {
                RoomListResponse res = new RoomListResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());

                allRooms.setValue(res);

            }
        });
        return allRooms;
    }

    public MutableLiveData<RoomListResponse> getAllRoomCheckinByType(String roomCode,String roomMbr,String roomType,String roomCapacity){
        MutableLiveData<RoomListResponse> allRooms = new MutableLiveData<>();
        roomClient.getAllRoomCheckinByType(roomCode,roomMbr,roomType,roomCapacity,1).enqueue(new Callback<RoomListResponse>() {
            @Override
            public void onResponse(Call<RoomListResponse> call, Response<RoomListResponse> response) {
                RoomListResponse res = response.body();
                if (!res.isOkay()) {

                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                    /*res.setMessage(errorReport.toString());*/
                    Log.e("ERROR SERVER",errorReport.toString());
                }
                allRooms.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomListResponse> call, Throwable t) {
                RoomListResponse res = new RoomListResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());

                allRooms.setValue(res);

            }
        });
        return allRooms;
    }

    public MutableLiveData<RoomListResponse> getAllRoomPaid(String roomCode){
        MutableLiveData<RoomListResponse> allRooms = new MutableLiveData<>();
        roomClient.getAllRoomPaid(roomCode).enqueue(new Callback<RoomListResponse>() {
            @Override
            public void onResponse(Call<RoomListResponse> call, Response<RoomListResponse> response) {
                RoomListResponse res = response.body();
                if (!res.isOkay()) {

                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server response : ");
                    errorReport.append(res.getMessage());
                   /* res.setMessage(errorReport.toString());*/
                    Log.e("ERROR SERVER",errorReport.toString());
                }
                allRooms.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomListResponse> call, Throwable t) {
                RoomListResponse res = new RoomListResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());

                allRooms.setValue(res);

            }
        });
        return allRooms;
    }

    public MutableLiveData<RoomListResponse> getAllRoomCheckout(String roomCode){
        MutableLiveData<RoomListResponse> allRooms = new MutableLiveData<>();
        roomClient.getAllRoomCheckout(roomCode).enqueue(new Callback<RoomListResponse>() {
            @Override
            public void onResponse(Call<RoomListResponse> call, Response<RoomListResponse> response) {
                RoomListResponse res = response.body();
                if (!res.isOkay()) {

                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                    /*res.setMessage(errorReport.toString());*/
                    Log.e("ERROR SERVER",errorReport.toString());
                }
                allRooms.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomListResponse> call, Throwable t) {
                RoomListResponse res = new RoomListResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());

                allRooms.setValue(res);

            }
        });
        return allRooms;
    }

    public MutableLiveData<RoomListResponse> getAllRoomByType(RoomType roomType){
        MutableLiveData<RoomListResponse> allRooms = new MutableLiveData<>();
        roomClient
                .getAllRoomByType(roomType.getRoomType(), String.valueOf(roomType.getRoomTypeCapacity()))
                .enqueue(new Callback<RoomListResponse>() {
            @Override
            public void onResponse(Call<RoomListResponse> call, Response<RoomListResponse> response) {
                RoomListResponse res = response.body();
                if (!res.isOkay()) {

                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                   /* res.setMessage(errorReport.toString());*/
                    Log.e("ERROR SERVER",errorReport.toString());
                }
                allRooms.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomListResponse> call, Throwable t) {
                RoomListResponse res = new RoomListResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());

                allRooms.setValue(res);

            }
        });
        return allRooms;
    }


    public MutableLiveData<RoomTypeResponse> getGroupingRoomTypeReady(){
        MutableLiveData<RoomTypeResponse> allRoomType = new MutableLiveData<>();
        roomTypeClient.getAllGroupingRoomTypeReady().enqueue(new Callback<RoomTypeResponse>() {
            @Override
            public void onResponse(Call<RoomTypeResponse> call, Response<RoomTypeResponse> response) {
                RoomTypeResponse res = response.body();
                if (!res.isOkay()) {
                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                    res.setMessage(errorReport.toString());
                }

                allRoomType.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomTypeResponse> call, Throwable t) {
                RoomTypeResponse res = new RoomTypeResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());
                allRoomType.setValue(res);

            }
        });
        return allRoomType;
    }

    public MutableLiveData<RoomTypeResponse> getRoomTypeReady(){
        MutableLiveData<RoomTypeResponse> allRoomType = new MutableLiveData<>();
        roomTypeClient.getAllRoomTypeReady().enqueue(new Callback<RoomTypeResponse>() {
            @Override
            public void onResponse(Call<RoomTypeResponse> call, Response<RoomTypeResponse> response) {
                RoomTypeResponse res = response.body();
                if (!res.isOkay()) {
                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                    res.setMessage(errorReport.toString());
                }

                allRoomType.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomTypeResponse> call, Throwable t) {
                RoomTypeResponse res = new RoomTypeResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());
                allRoomType.setValue(res);

            }
        });
        return allRoomType;
    }

    public MutableLiveData<RoomTypeResponse> getAllRoomType(){
        MutableLiveData<RoomTypeResponse> allRoomType = new MutableLiveData<>();
        roomTypeClient.getRoomTypes().enqueue(new Callback<RoomTypeResponse>() {
            @Override
            public void onResponse(Call<RoomTypeResponse> call, Response<RoomTypeResponse> response) {
                RoomTypeResponse res = response.body();
                if (!res.isOkay()) {
                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                    res.setMessage(errorReport.toString());
                }

                allRoomType.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomTypeResponse> call, Throwable t) {
                RoomTypeResponse res = new RoomTypeResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());
                allRoomType.setValue(res);

            }
        });
        return allRoomType;
    }

    public MutableLiveData<RoomPromoResponse> getAllRoomPromo(String roomType){
        MutableLiveData<RoomPromoResponse> allRoomPromo = new MutableLiveData<>();
        promoRoomClient.getRoomPromoResponseCall(roomType).enqueue(new Callback<RoomPromoResponse>() {
            @Override
            public void onResponse(Call<RoomPromoResponse> call, Response<RoomPromoResponse> response) {
                RoomPromoResponse res = response.body();
                if (!res.isOkay()) {
                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                    res.setMessage(errorReport.toString());
                }

                allRoomPromo.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomPromoResponse> call, Throwable t) {
                RoomPromoResponse res = new RoomPromoResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());
                allRoomPromo.setValue(res);
            }
        });
        return allRoomPromo;
    }

    public MutableLiveData<FoodPromoResponse> getAllFoodPromo(String roomType,String roomCode){
        MutableLiveData<FoodPromoResponse> allFoodPromo = new MutableLiveData<>();
        promoInventoryClient.getFoodPromoResponseCall(roomType,roomCode).enqueue(new Callback<FoodPromoResponse>() {
            @Override
            public void onResponse(Call<FoodPromoResponse> call, Response<FoodPromoResponse> response) {
                FoodPromoResponse res = response.body();
                if (!res.isOkay()) {
                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                    res.setMessage(errorReport.toString());
                }

                allFoodPromo.setValue(res);
            }

            @Override
            public void onFailure(Call<FoodPromoResponse> call, Throwable t) {
                FoodPromoResponse res = new FoodPromoResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());
                allFoodPromo.setValue(res);
            }
        });
        return allFoodPromo;
    }

    public MutableLiveData<RoomOrderResponse> getRoomOrder(String roomCode) {
        MutableLiveData<RoomOrderResponse> roomOrderRespon = new MutableLiveData<>();

        roomOrderClient.getDetailRoomOrder(roomCode).enqueue(new Callback<RoomOrderResponse>() {
            @Override
            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                RoomOrderResponse res = response.body();
                if (!res.isOkay()) {
                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                    res.setMessage(errorReport.toString());
                }

                roomOrderRespon.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                RoomOrderResponse res = new RoomOrderResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());
                roomOrderRespon.setValue(res);
            }
        });
        return roomOrderRespon;
    }

    public MutableLiveData<RoomOrderResponse> getHistoryRoomOrder(String reception,String roomCode) {
        MutableLiveData<RoomOrderResponse> roomOrderRespon = new MutableLiveData<>();

        roomOrderClient.getDetailHistoryRoomOrder(reception,roomCode).enqueue(new Callback<RoomOrderResponse>() {
            @Override
            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                RoomOrderResponse res = response.body();
                if (!res.isOkay()) {
                    StringBuilder errorReport = new StringBuilder();
                    errorReport.append("Server Response : ");
                    errorReport.append(res.getMessage());
                    res.setMessage(errorReport.toString());
                }

                roomOrderRespon.setValue(res);
            }

            @Override
            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                RoomOrderResponse res = new RoomOrderResponse();
                res.setOkay(false);

                StringBuilder errorReport = new StringBuilder();
                errorReport.append("Error Failure : ");
                errorReport.append(t.getMessage());

                res.setMessage(errorReport.toString());
                Log.e("ERROR FAILURE",errorReport.toString());
                roomOrderRespon.setValue(res);
            }
        });
        return roomOrderRespon;
    }


    public MutableLiveData<RoomListResponse> getAllRoomReadyByTypeGrouping(RoomType roomType) {
        MutableLiveData<RoomListResponse> allRooms = new MutableLiveData<>();
        roomClient
                .getAllRoomReadyByTypeGrouping(roomType.getRoomType())
                .enqueue(new Callback<RoomListResponse>() {
                    @Override
                    public void onResponse(Call<RoomListResponse> call, Response<RoomListResponse> response) {
                        RoomListResponse res = response.body();
                        if (!res.isOkay()) {

                            StringBuilder errorReport = new StringBuilder();
                            errorReport.append("Server Response : ");
                            errorReport.append(res.getMessage());
                            /* res.setMessage(errorReport.toString());*/
                            Log.e("ERROR SERVER",errorReport.toString());
                        }
                        allRooms.setValue(res);
                    }

                    @Override
                    public void onFailure(Call<RoomListResponse> call, Throwable t) {
                        RoomListResponse res = new RoomListResponse();
                        res.setOkay(false);

                        StringBuilder errorReport = new StringBuilder();
                        errorReport.append("Error Failure : ");
                        errorReport.append(t.getMessage());

                        res.setMessage(errorReport.toString());
                        Log.e("ERROR FAILURE",errorReport.toString());

                        allRooms.setValue(res);

                    }
                });
        return allRooms;
    }

    public MutableLiveData<RoomListResponse> getAllRoomReadyByType(RoomType roomType){
        MutableLiveData<RoomListResponse> allRooms = new MutableLiveData<>();
        roomClient
                .getAllRoomReadyByType(roomType.getRoomType(), String.valueOf(roomType.getRoomTypeCapacity()))
                .enqueue(new Callback<RoomListResponse>() {
                    @Override
                    public void onResponse(Call<RoomListResponse> call, Response<RoomListResponse> response) {
                        RoomListResponse res = response.body();
                        if (!res.isOkay()) {

                            StringBuilder errorReport = new StringBuilder();
                            errorReport.append("Server Response : ");
                            errorReport.append(res.getMessage());
                            /* res.setMessage(errorReport.toString());*/
                            Log.e("ERROR SERVER",errorReport.toString());
                        }
                        allRooms.setValue(res);
                    }

                    @Override
                    public void onFailure(Call<RoomListResponse> call, Throwable t) {
                        RoomListResponse res = new RoomListResponse();
                        res.setOkay(false);

                        StringBuilder errorReport = new StringBuilder();
                        errorReport.append("Error Failure : ");
                        errorReport.append(t.getMessage());

                        res.setMessage(errorReport.toString());
                        Log.e("ERROR FAILURE",errorReport.toString());

                        allRooms.setValue(res);

                    }
                });
        return allRooms;
    }
}
