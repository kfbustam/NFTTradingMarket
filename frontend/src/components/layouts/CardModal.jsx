import React from 'react';
import { Link } from 'react-router-dom'
import { Modal } from "react-bootstrap";

const CardModal = ({ item, show, onHide, setSuccessfulToastMessage, setErrorToastMessage, placeBid, buyItem, setCurrencyAmount }) => {
    const onCurrencyAmountChange = (el) => {
        setCurrencyAmount(el.target.value);
    };

    return (

        <Modal
            show={show}
            onHide={() => { setErrorToastMessage(null); setSuccessfulToastMessage(null); onHide(); }}
        >
            <Modal.Header closeButton></Modal.Header>
            <div className="modal-body space-y-20 pd-40">
                {item && item.saleType === "AUCTION" ? <h3>Place a Bid</h3> : <h3>Buy This</h3>}
                {
                    item && item.saleType === "AUCTION" && <input type="text" className="form-control"
                        placeholder="00.00 ETH" onChange={onCurrencyAmountChange} />
                }
                {/* <p>Enter quantity. <span className="color-popup">5 available</span>
                </p>
                <input type="number" className="form-control" placeholder="1" /> */}
                <div className="hr"></div>
                <div className="d-flex justify-content-between">
                    {item && item.saleType === "AUCTION" ? <p> You must bid at least:</p> : <p>Price: </p>}
                    {item && <p className="text-right price color-popup">{item.price} {item.nftType}</p>}
                </div>
                {/* <div className="d-flex justify-content-between">
                    <p> Service free:</p>
                    <p className="text-right price color-popup"> 0,89 ETH </p>
                </div> */}
                {item && item.saleType === "AUCTION" && <div className="d-flex justify-content-between">
                    <p> Total bid amount:</p>
                    <p className="text-right price color-popup"> 4 ETH </p>
                </div>}
                {item && item.saleType === "AUCTION" && <Link onClick={() => placeBid(item.nftId)} className="btn btn-primary" data-toggle="modal" data-target="#popup_bid_success" data-dismiss="modal" aria-label="Close"> Place a bid soon</Link>}
                {item && item.saleType === "IMMEDIATE" && <Link onClick={() => buyItem(item.nftId, item.seller.id)} className="btn btn-primary" data-toggle="modal" data-target="#popup_bid_success" data-dismiss="modal" aria-label="Close"> Buy now</Link>}
            </div>
        </Modal>

    );
};

export default CardModal;
