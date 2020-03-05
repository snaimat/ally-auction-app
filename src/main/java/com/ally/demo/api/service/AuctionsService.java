package com.ally.demo.api.service;


import com.ally.demo.api.exception.ItemIdNotFoundException;
import com.ally.demo.api.exception.MissingRequiredParameterException;
import com.ally.demo.api.exception.ReserveNotMetException;
import com.ally.demo.api.model.AuctionItem;
import com.ally.demo.api.repository.DBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionsService implements Auctions{

    @Autowired
    DBRepository dbRepository;

    @Override
    public List<AuctionItem> getAuctionItems() throws ItemIdNotFoundException{
        List<AuctionItem> auctionItems = dbRepository.getAuctionItems();
        if(auctionItems.isEmpty()) throw new ItemIdNotFoundException("No Items Found.", HttpStatus.NOT_FOUND);
        return auctionItems;
    }

    @Override
    public AuctionItem getAuctionItem(int auctionItemId) throws ItemIdNotFoundException, MissingRequiredParameterException {
        if(auctionItemId<=0) throw new MissingRequiredParameterException("Missed a required paramenter", HttpStatus.BAD_REQUEST);
        AuctionItem auctionItem = dbRepository.getAuctionItem(auctionItemId);
        if(auctionItem==null) throw new ItemIdNotFoundException("No Item Found.", HttpStatus.NOT_FOUND);
        return auctionItem;
    }

    @Override
    public AuctionItem setAuctionItem(AuctionItem auctionItem) {
        AuctionItem auctionItemResponse = dbRepository.saveAuctionItem(auctionItem);
        return auctionItemResponse;
    }

    @Override
    public AuctionItem setBid(AuctionItem auctionItem) throws ReserveNotMetException {
        AuctionItem auctionItemResponse = dbRepository.saveBid(auctionItem);
        if(!auctionItemResponse.isReserveMet()) throw new ReserveNotMetException("Reserve not met.", HttpStatus.OK);
        return auctionItemResponse;
    }
}
