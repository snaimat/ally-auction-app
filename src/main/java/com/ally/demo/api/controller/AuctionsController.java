package com.ally.demo.api.controller;

import com.ally.demo.api.exception.ItemIdNotFoundException;
import com.ally.demo.api.exception.MissingRequiredParameterException;
import com.ally.demo.api.exception.ReserveNotMetException;
import com.ally.demo.api.model.AuctionItem;
import com.ally.demo.api.model.Item;
import com.ally.demo.api.service.Auctions;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AuctionsController {

    @Autowired
    Auctions auctions;

    @ApiOperation(
            value = "Get Auction Item details.",
            notes = "Returns resource of auction item in JSON format",
            httpMethod = "GET"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AuctionItem.class),
            @ApiResponse(code = 400, message = "Missing mandatory parameter", response = String.class),
            @ApiResponse(code = 404, message = "No Auction Item Found", response = String.class),
            @ApiResponse(code = 500, message = "-Internal server error", response = String.class),
            })
    @GetMapping(value = "/auctionItems/{auctionItem}", produces = "application/json")
    public ResponseEntity<AuctionItem> getAuctionItem(
            @PathVariable("auctionItem") int auctionItemId) throws ItemIdNotFoundException, MissingRequiredParameterException {

        log.info("Auction Item Requested:" + auctionItemId);
        AuctionItem auctionItem = auctions.getAuctionItem(auctionItemId);
        return ResponseEntity.ok(auctionItem);
    }

    @ApiOperation(
            value = "Get All Auction Item details.",
            notes = "Returns list of auction items in JSON format",
            httpMethod = "GET"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AuctionItem.class),
            @ApiResponse(code = 404, message = "No Auction Item Found", response = String.class),
            @ApiResponse(code = 500, message = "-Internal server error", response = String.class),
    })
    @GetMapping(value="/auctionItems", produces = "application/json")
    public ResponseEntity<List<AuctionItem>> getAuctionItems() throws ItemIdNotFoundException {
        log.info("ALL Auction Item Requested.");
        List<AuctionItem> auctionItems = auctions.getAuctionItems();
        return ResponseEntity.ok(auctionItems);
    }

    @ApiOperation(
            value = "Saves an auction Item.",
            notes = "Returns auction item id for auction item saved.",
            httpMethod = "POST"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AuctionItem.class),
            @ApiResponse(code = 500, message = "-Internal server error", response = String.class),
    })
    @PostMapping(value="/auctionItems", consumes = "application/json")
    public ResponseEntity<AuctionItem> saveAuctionItems(
            @RequestBody @Valid AuctionItem auctionItem){

        log.info("Saving auction Item:" + auctionItem.toString());
        AuctionItem postedAuctionItem = new AuctionItem();
        postedAuctionItem = auctions.setAuctionItem(auctionItem);
        log.info("Saved auction Item:" + postedAuctionItem.toString());
        return ResponseEntity.ok(postedAuctionItem);
    }

    @ApiOperation(
            value = "Save bid for auction.",
            notes = "Saves a bid to auction item",
            httpMethod = "POST"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = AuctionItem.class),
            @ApiResponse(code = 500, message = "-Internal server error", response = String.class),
    })
    @PostMapping(value="/bids", consumes = "application/json")
    public ResponseEntity<AuctionItem> saveBid(
            @RequestBody @Valid AuctionItem auctionItem) throws ReserveNotMetException {

        AuctionItem postedAuctionItem = auctions.setBid(auctionItem);
        log.info("Saved bid:" + postedAuctionItem.toString());
        return ResponseEntity.ok(postedAuctionItem);
    }
}
