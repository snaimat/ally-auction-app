package com.ally.demo.api.service;

import com.ally.demo.api.exception.ItemIdNotFoundException;
import com.ally.demo.api.exception.MissingRequiredParameterException;
import com.ally.demo.api.exception.ReserveNotMetException;
import com.ally.demo.api.model.AuctionItem;

import java.nio.charset.MalformedInputException;
import java.util.List;

public interface Auctions {

    public List<AuctionItem> getAuctionItems() throws ItemIdNotFoundException;
    public AuctionItem getAuctionItem(int auctionItemId) throws ItemIdNotFoundException, MissingRequiredParameterException;
    public AuctionItem setAuctionItem(AuctionItem auctionItem);
    public AuctionItem setBid(AuctionItem auctionItem) throws ReserveNotMetException;
}