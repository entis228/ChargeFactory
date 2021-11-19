package com.entis.app.entity.charge.response;

import com.entis.app.entity.charge.Charge;

public record ChargeResponse(String time, String stationId, double consumedEnergy, double withdraw) {

    public static ChargeResponse fromCharge(Charge charge){
        return new ChargeResponse(
          charge.getEndTime().toString(),
          charge.getStation().getId().toString(),
          charge.getConsumedEnergy(),
          charge.getWithdraw()
        );
    }
}
