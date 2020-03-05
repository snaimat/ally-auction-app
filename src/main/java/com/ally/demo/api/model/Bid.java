package com.ally.demo.api.model;

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
public class Bid {

    private int auctionItemId;
    private String bidderName;
    private double maxAutoBidAmount;
}
