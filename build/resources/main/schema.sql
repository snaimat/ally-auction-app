DROP TABLE IF EXISTS ITEMS;
DROP TABLE IF EXISTS AUCTION_ITEM;

CREATE TABLE ITEMS (
    id varchar(250) PRIMARY KEY,
    description varchar(250) not null
);

CREATE TABLE AUCTION_ITEM (
    auction_item_id int identity not null PRIMARY KEY,
    current_bid decimal not null,
    reserve_price decimal not null,
    bidder_name varchar(250),
    max_auto_bid_amount decimal not null,
    item_id varchar(250)
);