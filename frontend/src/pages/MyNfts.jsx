import React , {useEffect, useState} from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import img1 from '../alice_video.png'

const GET_TRANSACTIONS = "http://localhost:8080/nft?token="

const MyNfts = () => {
    let navigate = useNavigate();

    const [apiResponse, setApiResponse] = useState([])

    const [dataFilter, setDataFilter] = useState(
        [
            {
                name: 'Current Listings',
                checked: true,
                type: "current"
            },
            {
                name: 'Past Listings',
                checked: true,
                type: "past"
            },
            {
                name: 'Ethereum',
                checked: true,
                type: "ETHEREUM"
            },
            {
                name: 'Bitcoin',
                checked: true,
                type: "BITCOIN"
            },
            {
                name: 'Last 24 hours',
                checked: true,
                type: "24H"
            },
            {
                name: 'Last Week',
                checked: false,
                type: "1W"
            },
            {
                name: 'Last Month',
                checked: false,
                type: "1M"
            }
        ]
    )

    const [visible , setVisible] = useState(8);

    const applyFilter = () => {

    }

    useEffect(() => {
        console.log("fron useeffect")
    }, [dataFilter])

    const clearAllFilters = () => {
        setDataFilter(
            [
                {
                    name: 'Current List',
                    checked: false,
                    type: "current"
                },
                {
                    name: 'Past Listings',
                    checked: false,
                    type: "past"
                },
                {
                    name: 'Ethereum',
                    checked: false,
                    type: "ETHEREUM"
                },
                {
                    name: 'Bitcoin',
                    checked: false,
                    type: "BITCOIN"
                },
                {
                    name: 'Last 24 hours',
                    checked: false,
                    type: "24H"
                },
                {
                    name: 'Last Week',
                    checked: false,
                    type: "1W"
                },
                {
                    name: 'Last Month',
                    checked: false,
                    type: "1M"
                }
            ]
        )
    }

    useEffect(() => {
        getTransactionsForUser();
    }, [])

    const getTransactionsForUser = () => {
        let token = "test123"

        if (typeof(localStorage.getItem("token")) !== undefined && localStorage.getItem("token") !== null
        && localStorage.getItem("token") !== 'undefined') {
            token = localStorage.getItem("token")
        } else {
            localStorage.clear();
            navigate("/login");
        }
        
        let fetchUrl = GET_TRANSACTIONS + token
        fetch(
            fetchUrl,
            {
                method: "GET",
                header: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }
        ).then(response => {
            if (response.ok) {
                return response.json()
            }
            throw response
        }).then(jsonData => {
            setApiResponse(jsonData)
            console.log("leng", apiResponse.length)
            console.log("tranactions data", jsonData)
        }).catch(error => {
            console.log("transactions fetch failed", error)
        })
    } 

    return (
        <div>
            <Header />
            <section className="flat-title-page inner">
                <div className="overlay"></div>
                <div className="themesflat-container">
                    <div className="row">
                        <div className="col-md-12">
                            <div className="page-title-heading mg-bt-12">
                                <h1 className="heading text-center">My NFTs</h1>
                            </div>
                            <div className="breadcrumbs style2">
                                <ul>
                                    <li><Link to="/">Home</Link></li>
                                    <li><Link to="#">Activity</Link></li>
                                    <li>My NFTs</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>                    
            </section>
            <section className="tf-activity tf-section">
                <div className="themesflat-container">
                    <div className="row">
                        <div className="col-xl-8 col-lg-9 col-md-8 col-12">
                            <div className="box-activity">
                                {
                                    apiResponse.map((item,index) => (
                                        <div key={index} className="sc-card-activity">
                                            <div className="content">
                                                <div className="media">
                                                    <img src={"http://localhost:8080/images?image_name=" + item.imageUrl} alt="" />
                                                </div>
                                                <div className="infor">
                                                    <h4><Link to="/item-details-01">{item.name}</Link></h4>
                                                    <p>{item.description}</p>
                                                    <div className="status"> <span className="author">Smart Contract Address: {item.smartContractAddress}</span></div>
                                                    <div className="time">
                                                    <p><b>Last Recorded Time</b></p>
                                                    {item.lastRecordedTime}
                                                    
                                                    <p><b>NFT ID</b></p>
                                                    {item.tokenId}

                                                    <p><b>Asset URL</b></p>
                                                    <Link to="/item-details-01">Click Here</Link>
                                                    </div>
                                        
                                                </div>
                                            </div>
                                        </div>
                                    ))
                                }
                            </div>
                        </div>
                        <div className="col-xl-4 col-lg-3 col-md-4 col-12">

                            <div id="side-bar" className="side-bar style-2">
                                <div className="widget widget-filter style-1 mgbt-0">
                                    <div className="header-widget-filter">
                                        <h3 className="title-widget">Filter</h3>
                                        <Link to="#" className="clear-checkbox btn-filter style-2" onClick={clearAllFilters}>
                                            Clear All
                                        </Link>
                                    </div>
                                    <form action="#" className="form-inner">
                                        {
                                            dataFilter.map((item,index) => (
                                                <div key={index}>
                                                <label >
                                                    {item.name}
                                                    <input type="checkbox" defaultChecked={item.checked} />
                                                    <span className="btn-checkbox"></span>
                                                </label><br/>
                                                </div>
                                            ))
                                        }

                                    </form>
                                </div>

                            </div>

                        </div>
                    </div>
                </div>
            </section>
            <Footer />

        </div>
    );
}

export default MyNfts;
