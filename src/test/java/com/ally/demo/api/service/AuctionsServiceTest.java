package com.ally.demo.api.service;

import com.ally.demo.api.exception.ItemIdNotFoundException;
import com.ally.demo.api.exception.MissingRequiredParameterException;
import com.ally.demo.api.exception.ReserveNotMetException;
import com.ally.demo.api.model.AuctionItem;
import com.ally.demo.api.model.Bid;
import com.ally.demo.api.model.Item;
import com.ally.demo.api.repository.DBRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AuctionsServiceTest {

    @Autowired
    private AuctionsService auctionsService;

    @MockBean
    private DBRepository dbRepository;

    @Test
    void when_request_all_auctionItems_then_getAuctionItems() throws ItemIdNotFoundException {

        List<AuctionItem> aItems = new ArrayList<>();
        aItems.add(new AuctionItem(1,1000,5000,"JOHN",0,new Item("sa-01","super duty f-150"),false));
        aItems.add(new AuctionItem(2,2000,6000,"TOM",0,new Item("sa-01","super duty f-150"),false));

        Mockito.when(dbRepository.getAuctionItems()).thenReturn(aItems);
        assertThat(auctionsService.getAuctionItems()).isEqualTo(aItems);

    }

    @Test
    void when_no_auctions_exists__then_returnNotFoundException() throws ItemIdNotFoundException {

        List<AuctionItem> aItems = new ArrayList<>();
        Mockito.when(dbRepository.getAuctionItems()).thenReturn(aItems);
        Assertions.assertThrows(ItemIdNotFoundException.class,()->{
            auctionsService.getAuctionItems();
        });
    }

    @Test
    void when_GetAuctionItem_then_return_AuctionItem() throws ItemIdNotFoundException, MissingRequiredParameterException {

        List<AuctionItem> aItems = new ArrayList<>();
        aItems.add(new AuctionItem(1,1000,5000,"JOHN",0,new Item("sa-01","super duty f-150"),false));
        aItems.add(new AuctionItem(2,2000,6000,"TOM",0,new Item("sa-01","super duty f-150"),false));

        Mockito.when(dbRepository.getAuctionItem(1)).thenReturn(aItems.get(0));
        assertThat(auctionsService.getAuctionItem(1)).isEqualTo(aItems.get(0));
    }

    @Test
    void when_No_AuctionItemId_found_then_return_NotFoundException() throws ItemIdNotFoundException, MissingRequiredParameterException {

        AuctionItem ai = null;
        Mockito.when(dbRepository.getAuctionItem(1)).thenReturn(ai);
        Assertions.assertThrows(ItemIdNotFoundException.class,()->{
            auctionsService.getAuctionItem(1);
        });
    }

    @Test
    void when_Post_AuctionItem_Return_AuctionItem_Posted() {
        AuctionItem ai = new AuctionItem(1,5000,6000,"JOHN",5000,new Item("sa-01","super duty f-150"),false);

        Mockito.when(dbRepository.saveAuctionItem(ai)).thenReturn(ai);
        assertThat(auctionsService.setAuctionItem(ai)).isEqualTo(ai);
    }

    @Test
    void when_Post_Bid_Return_AuctionItem_BidPosted() throws ReserveNotMetException {

        AuctionItem ai = new AuctionItem(1,0,0,"JOHN",5000,new Item("sa-01","super duty f-150"),false);

        Mockito.when(dbRepository.saveBid(ai)).thenReturn(ai);
        assertThat(auctionsService.setBid(ai)).isEqualTo(ai);
    }

    @Test
    void when_Post_Bid_ReserveNotMet_then_Return_Exception() {

        AuctionItem ai = new AuctionItem(1,2000,4000,"JOHN",2000,new Item("sa-01","super duty f-150"),false);

        Mockito.when(dbRepository.saveBid(ai)).thenReturn(ai);

        Assertions.assertThrows(ReserveNotMetException.class,()->{
            auctionsService.setBid(ai);
        });
    }

    @Test
    void when_BidderIsOutBid_then_return_Notification(){

    }
}