package livs.code.frontoffice.data.remote.respons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import livs.code.frontoffice.data.entity.InventoryPromo;


/**
 * Created by program on 24/10/2017.
 */

public class FoodPromoResponse extends BaseResponse {

    @SerializedName("data")
    private List<InventoryPromo> inventoryPromos;

    public List<InventoryPromo> getInventoryPromos() {
        return inventoryPromos;
    }

}
