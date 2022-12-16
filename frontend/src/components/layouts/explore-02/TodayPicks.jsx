import React, { useState, Fragment, useEffect } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import CardModal from '../CardModal';
import { ToastContainer, toast } from 'react-toastify';

import { Link, useNavigate } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css';

const LISTINGS_URL = "http://localhost:8080/nft/listings"
const IMAGE_BASE_URL = "http://localhost:8080/images?image_name=";

const TodayPicks = ({ dataPanel }) => {
    const token = "test123";


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

    let navigate = useNavigate();


    const buyNowFunc = () => {
        //toast.success("Purchase Successful.")
        setModalShow(true)
        //toast.error("Not enough balance.")


        //navigate("/wallet-connect");

    }


    const [modalShow, setModalShow] = useState(false);

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
                                    {/* <TabList>
                                        {
                                            dataTab.map(data=> (
                                                <Tab key={data.id} >{data.title}</Tab>
                                            ))
                                        }
                                    </TabList> */}
                                    {
                                        dataPanel.map(data => (
                                            <TabPanel key={data.id}>
                                                {

                                                    data.dataContent.slice(0, visible).map(item => (
                                                        <div key={item.id} className={`sc-card-product explode style2 mg-bt ${item.nftType} `}>
                                                            <div className="card-media">
                                                                <Link to="/item-details-01"><img src={IMAGE_BASE_URL + item.imageUrl} alt="Axies" state={{ item }} /></Link>
                                                                <div className="button-place-bid">
                                                                    <button onClick={() => buyNowFunc()} className="sc-button style-place-bid style bag fl-button pri-3"><span>Purchase</span></button>
                                                                </div>
                                                                <Link to="/login" className="wishlist-button heart"><span className="number-like">{item.lastRecordedTime}</span></Link>
                                                                <div className="coming-soon">{item.nftType}</div>
                                                            </div>
                                                            <div className="card-title">
                                                                <h5><Link to="/item-details-01" state={{ item }}>"{item.name}"</Link></h5>

                                                            </div>
                                                            <div className="meta-info">
                                                                <div className="author">
                                                                    <div className="avatar">
                                                                        <img src={item.imgAuthor} alt="Axies" />
                                                                    </div>
                                                                    <div className="info">
                                                                        <span>Creator</span>
                                                                        <h6> <Link to="/authors-02">{item.nameAuthor}</Link> </h6>
                                                                    </div>
                                                                </div>
                                                                <div className="tags">{item.tags}</div>
                                                            </div>
                                                            <div className="card-bottom style-explode">
                                                                <div className="price">
                                                                    {
                                                                        (item.saleType === "immediate" || item.saleType === "auction")
                                                                        &&
                                                                        <>
                                                                            <span>Current Price</span>
                                                                            <div className="price-details">
                                                                                <h5>{item.price}</h5>
                                                                                <span>= {item.smartContractAddress}</span>
                                                                            </div>
                                                                        </>

                                                                    }
                                                                    {
                                                                        (item.saleType === "all" || item.saleType === "auction")
                                                                        &&
                                                                        <>
                                                                            <span>Current Bid</span>
                                                                            <div className="price-details">
                                                                                <h5>{item.price}</h5>
                                                                                <span>= {item.smartContractAddress}</span>
                                                                            </div>
                                                                        </>
                                                                    }
                                                                </div>
                                                                <Link to="/activity-01" className="view-history reload">View History</Link>
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
                show={modalShow}
                onHide={() => setModalShow(false)}
            />
        </Fragment>
    );
}

export default TodayPicks;
