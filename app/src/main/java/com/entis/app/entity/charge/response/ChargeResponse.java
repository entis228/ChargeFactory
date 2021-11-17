package com.entis.app.entity.charge.response;

public record ChargeResponse(String time, String stationId, double consumedEnergy, double withdraw) {
}
