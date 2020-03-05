package com.ally.demo.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AuctionItem {

    private int auctionItemId;
    private double currentBid;
    private double reservePrice;
    private String bidderName;
    private double maxAutoBidAmount;
    private Item item;
    private boolean isReserveMet;

    @JsonIgnore
    public boolean isReserveMet() {
        return isReserveMet;
    }

    public AuctionItem setReserveMet(boolean reserveMet) {
        isReserveMet = reserveMet;
        return this;
    }
}
