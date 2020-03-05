package com.ally.demo.api.repository;

import com.ally.demo.api.exception.ReserveNotMetException;
import com.ally.demo.api.model.AuctionItem;

import java.util.List;

public interface DBRepository {

    public AuctionItem getAuctionItem(int auctionItemId);
    public List<AuctionItem> getAuctionItems();
    public AuctionItem saveAuctionItem(AuctionItem auctionItem);
    public AuctionItem saveBid(AuctionItem auctionItem);
}
