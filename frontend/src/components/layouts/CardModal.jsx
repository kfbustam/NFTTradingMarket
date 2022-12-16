import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom'
import { Modal } from "react-bootstrap";

const BUY_NOW_URL = "http://localhost:8080/nft/buy"
const PLACE_BID_URL = "http://localhost:8080/nft/auction/offer"

const CardModal = ({ item, show, onHide, setSuccessfulToastMessage, setErrorToastMessage }) => {
    let navigate = useNavigate();
    const [currencyAmount, setCurrencyAmount] = useState(0);
    const onCurrencyAmountChange = (el) => {
        setCurrencyAmount(el.target.value);
    };
    // TODO: Change to localStorage.getItem("token"); and test
    const token = "test123"

    const buyNowFunc = (nftID, sellerID) => {
        fetch(BUY_NOW_URL + "?token=" + token + "&nftID=" + nftID + "&sellerID=" + sellerID, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST"
        })
            .then(response => {
                if (response.ok) {
                    setSuccessfulToastMessage("Purchase Successful.");
                    return response.json()
                }
                throw response
            })
            .then(data => {
            })
            .catch(error => {
                setErrorToastMessage("Not enough balance.");
                console.error(error)
            }).finally(() => {
                navigate("/wallet-connect");
            });

    }

    const placeBid = (nftID) => {
        fetch(PLACE_BID_URL + "?token=" + token + "&nftID=" + nftID + "&offerPrice=" + currencyAmount, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST"
        })
            .then(response => {
                if (response.ok) {
                    setSuccessfulToastMessage("Offer Successfully Sent.")
                    return response.json()
                }
                throw response
            })
            .then(data => {
            })
            .catch(error => {
                setErrorToastMessage("Not enough balance.")
                console.error(error)
            }).finally(() => {
            });
        //toast.success("Purchase Successful.")
        //toast.error("Not enough balance.")


        //navigate("/wallet-connect");

    }
    return (

        <Modal
            show={show}
            onHide={onHide}
        >
            <Modal.Header closeButton></Modal.Header>
            <div className="modal-body space-y-20 pd-40">
                <h3>!!Place a Bid</h3>
                <p className="text-center">You must bid at least <span className="price color-popup">4.89 ETH</span>
                </p>
                <input type="text" className="form-control"
                    placeholder="00.00 ETH" onChange={onCurrencyAmountChange} />
                {/* <p>Enter quantity. <span className="color-popup">5 available</span>
                </p>
                <input type="number" className="form-control" placeholder="1" /> */}
                <div className="hr"></div>
                <div className="d-flex justify-content-between">
                    <p> You must bid at least:</p>
                    <p className="text-right price color-popup"> 4.89 ETH </p>
                </div>
                <div className="d-flex justify-content-between">
                    <p> Service free:</p>
                    <p className="text-right price color-popup"> 0,89 ETH </p>
                </div>
                <div className="d-flex justify-content-between">
                    <p> Total bid amount:</p>
                    <p className="text-right price color-popup"> 4 ETH </p>
                </div>
                {item && item.saleType === "AUCTION" && <Link onClick={() => placeBid(item.nftId)} className="btn btn-primary" data-toggle="modal" data-target="#popup_bid_success" data-dismiss="modal" aria-label="Close"> Place a bid soon</Link>}
                {item && item.saleType === "IMMEDIATE" && <Link onClick={() => buyNowFunc(item.nftId, item.sellerId)} className="btn btn-primary" data-toggle="modal" data-target="#popup_bid_success" data-dismiss="modal" aria-label="Close"> Buy now</Link>}
            </div>
        </Modal>

    );
};

export default CardModal;
