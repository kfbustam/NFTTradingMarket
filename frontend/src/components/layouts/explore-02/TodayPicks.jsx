import React, { useState, Fragment, useEffect } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import CardModal from '../CardModal';
import { ToastContainer, toast } from 'react-toastify';
import img1 from '../../../assets/images/avatar/avt-3.jpg'
import Countdown from "react-countdown";

import { Link, useNavigate } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css';

const IMAGE_BASE_URL = "http://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/images?image_name="
const BUY_NOW_URL = "http://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/nft/buy"
const PLACE_BID_URL = "http://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/nft/auction/offer"

const TodayPicks = ({ dataPanel }) => {
    const [itemShown, setItemShown] = useState();
    const [modalShow, setModalShow] = useState(false);
    const [successfulToastMessage, setSuccessfulToastMessage] = useState();
    const [errorToastMessage, setErrorToastMessage] = useState();
    const [currencyAmount, setCurrencyAmount] = useState(0);
    let navigate = useNavigate();

    const buyItem = (nftID, sellerID) => {
        let token = "test123"

        if (typeof(localStorage.getItem("token")) !== undefined && localStorage.getItem("token") !== null
        && localStorage.getItem("token") !== 'undefined') {
            token = localStorage.getItem("token")
        } else {
            localStorage.clear();
            navigate("/login");
        }

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
                setSuccessfulToastMessage(null);
                setErrorToastMessage("Validation failed.. Please try again.");
                console.error(error)
            }).finally(() => {
                setModalShow(false);
                setItemShown(null);
                setErrorToastMessage(null);
            });

    }

    const placeBid = (nftID) => {
        let token = "test123"

        if (typeof(localStorage.getItem("token")) !== undefined && localStorage.getItem("token") !== null
        && localStorage.getItem("token") !== 'undefined') {
            token = localStorage.getItem("token")
        } else {
            localStorage.clear();
            navigate("/login");
        }

        fetch(PLACE_BID_URL + "?token=" + token + "&nftID=" + nftID + "&offerPrice=" + currencyAmount, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST"
        })
            .then(response => {
                if (response.ok) {
                    return response.json()
                }
                throw response
            })
            .then(data => {
            })
            .catch(error => {
                setSuccessfulToastMessage(null);
                setErrorToastMessage("Not enough balance.");
                console.error(error)
            }).finally(() => {
                setModalShow(false); setItemShown(null);
                setErrorToastMessage(null);
                setSuccessfulToastMessage("Offer Successfully Sent.")
            });


        //navigate("/wallet-connect");

    }

    useEffect(() => {
        // fetch(LISTINGS_URL + "?token=" + token, {
        //     headers: {
        //         'Accept': 'application/json',
        //         'Content-Type': 'application/json'
        //     },
        //     method: "POST"
        // })
        //     .then(response => {
        //         if (response.ok) {
        //             return response.json()
        //         }
        //         throw response
        //     })
        //     .then(data => {
        //         setDataPanel(data)
        //     })
        //     .catch(error => {
        //         console.error(error)
        //     }).finally(() => {
        //     });
    }, []);

    const [visible, setVisible] = useState(8);
    const showMoreItems = () => {
        setVisible((prevValue) => prevValue + 4);
    }

    useEffect(() => {
        if (successfulToastMessage != null) {
            toast.success(successfulToastMessage)
        } else if (errorToastMessage != null) {
            toast.error(errorToastMessage);
        }
    }, [modalShow, errorToastMessage, successfulToastMessage]);

    return (
        <Fragment>
            <ToastContainer theme="dark" position="top-center" />

            <div className="tf-section sc-explore-2">
                <div className="themesflat-container">
                    <div className="row">
                        <div className="col-md-12">
                            {/* Filters */}
                            {/* <div className="seclect-box style3">
                                <div id="artworks" className="dropdown">
                                    <Link to="#" className="btn-selector nolink">All Artworks</Link>
                                    <ul>
                                        <li><span>Abstraction</span></li>
                                        <li className="active"><span>Skecthify</span></li>
                                        <li><span>Patternlicious</span></li>
                                        <li><span>Virtuland</span></li>
                                        <li><span>Papercut</span></li>
                                    </ul>
                                </div>
                                <div id="sort-by" className="dropdown style-2">
                                    <Link to="#" className="btn-selector nolink">Sort by</Link>
                                    <ul>
                                        <li><span>Top rate</span></li>
                                        <li className="active"><span>Mid rate</span></li>
                                        <li><span>Low rate</span></li>
                                    </ul>
                                </div>    
                            </div> */}
                            <div className="flat-tabs explore-tab">
                                <Tabs >
                                    {/* Tab names list */}
                                    <TabList>
                                        {
                                            (dataPanel).map(data => (
                                                <Tab key={data.id}>All {data.id}</Tab>
                                            ))
                                        }
                                    </TabList>
                                    {
                                        dataPanel.map(data => (
                                            <TabPanel key={data.id}>
                                                {

                                                    data.dataContent.map(item => (
                                                        <div key={item.id} className={`sc-card-product explode style2 mg-bt ${item.nftType} `}>
                                                            <div className="card-media">
                                                                <Link to="/item-details-01" state={{ item: item }}><img src={IMAGE_BASE_URL + item.imageURL} alt="Axies" state={{ item }} /></Link>
                                                                <div className="button-place-bid">
                                                                    <button onClick={() => { setModalShow(true); setItemShown(item); }} className="sc-button style-place-bid style bag fl-button pri-3"><span>Purchase</span></button>
                                                                </div>
                                                                {/* <Link to="/login" className="wishlist-button heart"><span className="number-like"></span></Link>
                                                                <div className="coming-soon">{item.nftType}</div> */}
                                                            </div>
                                                            <div className="card-title">
                                                                <h5><Link to="/item-details-01" state={{ item: item }}>"{item.name}"</Link></h5>

                                                            </div>
                                                            <div className="meta-info">
                                                                <div className="author">
                                                                    <div className="avatar">
                                                                        <img src={item.imgAuthor ?? img1} alt="Axies" />
                                                                    </div>
                                                             
                                                                    <div className="info">
                                                                        <span>Creator</span>
                                                                        <h6> <Link to="/authors-02">{item.seller.name}</Link> </h6>
                                                                    </div>
                                                                    
                                                                </div>
                                                                <div className="tags">{item.tags ?? ["BTC"]}</div>
                                                            </div>
                                                            <div className="info">
                                                                        <span>&nbsp;</span>
                                                                    </div>
                                                            <div className="info">
                                                                        <span>Description</span>
                                                                        <h6>{item.description} </h6>
                                                            </div>
                                                            <div className="info">
                                                                        <span>&nbsp;</span>
                                                                    </div>
                                                                    {
                                                                        (item.saleType === "AUCTION")
                                                                        &&
                                                            <div className="info">
                                                           
                                                            <div className="featured-countdown">
                                                                <span className="slogan"></span>
                                                                <h6><Countdown date={item.expirationTime}>
                                                                    <span>Auction Over!</span>
                                                                </Countdown>
                                                                </h6>
                                                            </div>
                                                            </div>
                                                                    }
                                                            <div className="card-bottom style-explode">
                                                                <div className="price">
                                                                    {
                                                                        (item.saleType === "IMMEDIATE" || item.saleType === "AUCTION")
                                                                        &&
                                                                        <>
                                                                            <span>Current Price</span>
                                                                            <div className="price-details">
                                                                                <h5>{item.price} {item.nftType}</h5>
                                                                                <span>Smart Contract ID: {item.smartContractAddress}</span>
                                                                            </div>
                                                                        </>

                                                                    }
                                                                    {
                                                                        (item.saleType === "all" || item.saleType === "AUCTION")
                                                                        &&
                                                                        <>
                                                                            <span>Current Bid</span>
                                                                            <div className="price-details">
                                                                                <h5>{item.price}</h5>
                                                                                <span>Smart Contract ID: {item.smartContractAddress}</span>
                                                                            </div>
                                                                        </>
                                                                    }
                                                                </div>
                                                                {
                                                                        (item.saleType === "ALL" || item.saleType === "AUCTION")
                                                                        &&
                                                                <Link to="/activity-01" className="view-history reload">View History</Link>
}
                                                            </div>
                                                        </div>
                                                    ))
                                                }
                                                {
                                                    visible < data.dataContent.length &&
                                                    <div className="col-md-12 wrap-inner load-more text-center">
                                                        <Link to="#" id="load-more" className="sc-button loadmore fl-button pri-3" onClick={showMoreItems}><span>Load More</span></Link>
                                                    </div>
                                                }
                                            </TabPanel>
                                        ))
                                    }
                                </Tabs>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <CardModal
                placeBid={placeBid}
                buyItem={buyItem}
                setCurrencyAmount={setCurrencyAmount}
                setSuccessfulToastMessage={setSuccessfulToastMessage}
                setErrorToastMessage={setErrorToastMessage}
                item={itemShown}
                show={modalShow}
                onHide={() => { setModalShow(false); setItemShown(null); }}
            />
        </Fragment>
    );
}

export default TodayPicks;
