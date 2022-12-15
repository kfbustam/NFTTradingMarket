import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom'
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import { Link } from 'react-router-dom';
import Countdown from "react-countdown";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import liveAuctionData from '../assets/fake-data/data-live-auction';
import LiveAuction from '../components/layouts/LiveAuction';
import img1 from '../assets/images/avatar/avt-3.jpg'
import img2 from '../assets/images/avatar/avt-11.jpg'
import img3 from '../assets/images/avatar/avt-1.jpg'
import img4 from '../assets/images/avatar/avt-5.jpg'
import img5 from '../assets/images/avatar/avt-7.jpg'
import img6 from '../assets/images/avatar/avt-8.jpg'
import img7 from '../assets/images/avatar/avt-2.jpg'
import imgdetail1 from '../assets/images/box-item/images-item-details.jpg'
import CardModal from '../components/layouts/CardModal';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const BID_HISTORY_URL = "http://localhost:8080/nft/auction/offers"
const IMAGE_BASE_URL = "http://localhost:8080/images?image_name="
const BUY_NOW_URL = "http://localhost:8080/nft/buy"
const PLACE_BID_URL = "http://localhost:8080/nft/auction/offer"
const CANCEL_BID_URL = "http://localhost:8080/nft/auction/offer/cancel"

const ItemDetails01 = () => {
    const token = "test123";
    const location = useLocation()
    const [item] = useState(location.state != null ? location.state.item : null);
    const [successfulToastMessage, setSuccessfulToastMessage] = useState();
    const [errorToastMessage, setErrorToastMessage] = useState();
    const [currencyAmount, setCurrencyAmount] = useState(0);
    const [itemShown, setItemShown] = useState(item);
    const [modalShow, setModalShow] = useState(false);
    const [dataHistory, setDataHistory] = useState(
        [
            {
                img: img1,
                name: "Mason Woodward",
                time: "8 hours ago",
                price: "4.89 ETH",
                priceChange: "$12.246"
            },
            {
                img: img2,
                name: "Mason Woodward",
                time: "at 06/10/2021, 3:20 AM",
                price: "4.89 ETH",
                priceChange: "$12.246"
            },
            {
                img: img3,
                name: "Mason Woodward",
                time: "8 hours ago",
                price: "4.89 ETH",
                priceChange: "$12.246"
            },
            {
                img: img4,
                name: "Mason Woodward",
                time: "8 hours ago",
                price: "4.89 ETH",
                priceChange: "$12.246"
            },
            {
                img: img5,
                name: "Mason Woodward",
                time: "8 hours ago",
                price: "4.89 ETH",
                priceChange: "$12.246"
            },
            {
                img: img6,
                name: "Mason Woodward",
                time: "8 hours ago",
                price: "4.89 ETH",
                priceChange: "$12.246"
            },
        ]
    )

    const cancelBid = (nftID) => {
        fetch(CANCEL_BID_URL + "?token=" + token + "&nftID=" + nftID, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST"
        })
            .then(response => {
                if (response.ok) {
                    toast.success("Canceled Bid")
                    return response.json()
                }
                throw response
            })
            .then(data => {
            })
            .catch(error => {
                toast.error("Something went wrong")
                console.error(error)
            }).finally(() => {
                setModalShow(false)
            });
    }

    const buyItem = (nftID, sellerID) => {
        fetch(BUY_NOW_URL + "?token=" + token + "&nftID=" + nftID + "&sellerID=" + sellerID, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST"
        })
            .then(response => {
                if (response.ok) {
                    toast.success("Item successfully bought!")
                    return response.json()
                }
                throw response
            })
            .then(data => {
            })
            .catch(error => {
                toast.error("Something went wrong!")
                console.error(error)
            }).finally(() => {
                setModalShow(false)
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
                    toast.success("Offer Successfully Placed!")
                    return response.json()
                }
                throw response
            })
            .then(data => {
            })
            .catch(error => {
                toast.error("Something went wrong!")
                console.error(error)
            }).finally(() => {
                setModalShow(false)
            });


        //navigate("/wallet-connect");

    }

    useEffect(
        () => {
            if (item && item.nftId) {
                console.log("Fetching History...");
                fetch(BID_HISTORY_URL + "?token=" + token + "&nftID=" + item.nftId, {
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    method: "GET"
                })
                    .then(response => {
                        if (response.ok) {
                            console.log("History Fetched Successfully!");
                            return response.json()
                        }
                        throw response
                    })
                    .then(data => {
                        console.log(data);
                        setDataHistory(data)
                    })
                    .catch(error => {
                        console.error(error)
                    }).finally(() => {
                    });
            }
        }
        , [item, item.nftId]
    )

    return (
        <div className='item-details'>
            <Header />
            <ToastContainer theme="dark" position="top-center" />
            <section className="flat-title-page inner">
                <div className="overlay"></div>
                <div className="themesflat-container">
                    <div className="row">
                        <div className="col-md-12">
                            <div className="page-title-heading mg-bt-12">
                                <h1 className="heading text-center">Item Details</h1>
                            </div>
                            <div className="breadcrumbs style2">
                                <ul>
                                    <li><Link to="/">Home</Link></li>
                                    <li><Link to="#">Explore</Link></li>
                                    <li>Item Details</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <div className="tf-section tf-item-details">
                <div className="themesflat-container">
                    <div className="row">
                        <div className="col-xl-6 col-md-12">
                            <div className="content-left">
                                <div className="media">
                                    <img src={IMAGE_BASE_URL + item.imageURL ?? imgdetail1} alt="Axies" />
                                </div>
                            </div>
                        </div>
                        <div className="col-xl-6 col-md-12">
                            <div className="content-right">
                                <div className="sc-item-details">
                                    <h2 className="style2">{item.name ?? "The Fantasy Flower illustration"}</h2>
                                    <div className="meta-item">
                                        <div className="left">
                                            <span className="viewed eye">225</span>
                                            <span to="/login" className="liked heart wishlist-button mg-l-8"><span className="number-like">100</span></span>
                                        </div>
                                        <div className="right">
                                            <Link to="#" className="share"></Link>
                                            <Link to="#" className="option"></Link>
                                        </div>
                                    </div>
                                    <div className="client-infor sc-card-product">
                                        <div className="meta-info">
                                            <div className="author">
                                                <div className="avatar">
                                                    <img src={img6} alt="Axies" />
                                                </div>
                                                <div className="info">
                                                    <span>Owned By</span>
                                                    <h6> <Link to="/author-02">Ralph Garraway</Link> </h6>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="meta-info">
                                            <div className="author">
                                                <div className="avatar">
                                                    <img src={img7} alt="Axies" />
                                                </div>
                                                <div className="info">
                                                    <span>Create By</span>
                                                    <h6> <Link to="/author-02">Freddie Carpenter</Link> </h6>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <p>Smart Contract Address: {item.smartContractAddress}</p>
                                    <p>Habitant sollicitudin faucibus cursus lectus pulvinar dolor non ultrices eget.
                                        Facilisi lobortisal morbi fringilla urna amet sed ipsum vitae ipsum malesuada.
                                        Habitant sollicitudin faucibus cursus lectus pulvinar dolor non ultrices eget.
                                        Facilisi lobortisal morbi fringilla urna amet sed ipsum</p>
                                    <div className="meta-item-details style2">
                                        <div className="item meta-price">
                                            <span className="heading">Current Bid/Price</span>
                                            <div className="price">
                                                <div className="price-box">
                                                    <h5>{(parseFloat(item.price) > parseFloat(item.minimumPrice ?? 0) ? item.price : item.minimumPrice)} {item.nftType}</h5>
                                                    {/* <span>= $12.246</span> */}
                                                </div>
                                            </div>
                                        </div>
                                        <div className="item count-down">
                                            <span className="heading style-2">Countdown</span>
                                            <Countdown date={item.expirationTime ?? (Date.now() + 500000000)}>
                                                <span>You are good to go!</span>
                                            </Countdown>
                                        </div>
                                    </div>
                                    {
                                        (item.saleType === "BOTH" || item.saleType === "AUCTION") &&
                                        <Link onClick={() => { setModalShow(true); }} className="sc-button loadmore style bag fl-button pri-3"><span>Place a bid</span></Link>
                                    }
                                    {
                                        (item.saleType === "BOTH" || item.saleType === "AUCTION") &&
                                        <Link onClick={() => cancelBid(item.nftId)} className="sc-button loadmore style bag fl-button pri-3"><span>Cancel bid</span></Link>
                                    }
                                    {
                                        (item.saleType === "BOTH" || item.saleType === "IMMEDIATE") &&
                                        <Link onClick={() => { setModalShow(true); }} className="sc-button loadmore style bag fl-button pri-3"><span>Buy this now</span></Link>
                                    }
                                    <div className="flat-tabs themesflat-tabs">
                                        <Tabs>
                                            <TabList>
                                                <Tab>Bid History</Tab>
                                                <Tab>Info</Tab>
                                                <Tab>Provenance</Tab>
                                            </TabList>

                                            <TabPanel>
                                                <ul className="bid-history-list">
                                                    {
                                                        dataHistory.map((dataItem, index) => (
                                                            <li key={index} item={dataItem}>
                                                                <div className="content">
                                                                    <div className="client">
                                                                        <div className="sc-author-box style-2">
                                                                            <div className="author-avatar">
                                                                                <Link to="#">
                                                                                    <img src={img1} alt="Axies" className="avatar" />
                                                                                </Link>
                                                                                <div className="badge"></div>
                                                                            </div>
                                                                            <div className="author-infor">
                                                                                <div className="name">
                                                                                    <h6><Link to="/author-02">{item.seller.name ?? "someone"} </Link></h6> <span> placed a bid</span>
                                                                                </div>
                                                                                <span className="time">{dataItem.time}</span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div className="price">
                                                                        <h5>{dataItem.price} {item.nftType}</h5>
                                                                    </div>
                                                                </div>
                                                            </li>
                                                        ))
                                                    }
                                                </ul>
                                            </TabPanel>
                                            <TabPanel>
                                                <ul className="bid-history-list">
                                                    <li>
                                                        <div className="content">
                                                            <div className="client">
                                                                <div className="sc-author-box style-2">
                                                                    <div className="author-avatar">
                                                                        <Link to="#">
                                                                            <img src={img1} alt="Axies" className="avatar" />
                                                                        </Link>
                                                                        <div className="badge"></div>
                                                                    </div>
                                                                    <div className="author-infor">
                                                                        <div className="name">
                                                                            <h6> <Link to="/author-02">Mason Woodward </Link></h6> <span> place a bid</span>
                                                                        </div>
                                                                        <span className="time">8 hours ago</span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </TabPanel>
                                            <TabPanel>

                                                <div className="provenance">
                                                    <p>Lorem Ipsum is simply dummy text of the printing and typesetting industry.
                                                        Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,
                                                        when an unknown printer took a galley of type and scrambled it to make a type specimen book.
                                                        It has survived not only five centuries, but also the leap into electronic typesetting,
                                                        remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages,
                                                        and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.</p>
                                                </div>
                                            </TabPanel>
                                        </Tabs>
                                    </div>
                                </div>
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
                onHide={() => { setModalShow(false); }}
            />
            <LiveAuction data={liveAuctionData} />
            <Footer />
        </div>
    );
}

export default ItemDetails01;
