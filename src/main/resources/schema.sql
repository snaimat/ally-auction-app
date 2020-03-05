CREATE TABLE ITEMS (
    id varchar(250) PRIMARY KEY,
    description varchar(250)
);

CREATE TABLE AUCTION_ITEMS (
    auction_item_id int identity not null PRIMARY KEY,
    current_bid decimal not null,
    reserve_price decimal not null,
    bidder_name varchar(250),
    max_auto_bid_amount decimal not null,
    item_id varchar(250) not null,
    FOREIGN KEY (item_id) REFERENCES ITEMS(ID)
);

CREATE TABLE BIDS (
    bid_id int identity not null PRIMARY KEY,
    auction_item_id int not null,
    bidder_name varchar(250),
    max_auto_bid_amount decimal not null,
    FOREIGN KEY (auction_item_id) REFERENCES AUCTION_ITEMS(auction_item_id)
);