import React , {useEffect, useState} from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import img1 from '../alice_video.png'
import { useNavigate } from 'react-router-dom';

const GET_TRANSACTIONS = "http://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/transactions?token="

const Activity02 = () => {
    const [apiResponse, setApiResponse] = useState([])
    let navigate = useNavigate();

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
        }        let fetchUrl = GET_TRANSACTIONS + token
        let debutUrl = "https://60261217186b4a001777fbd7.mockapi.io/api/ndkshr/transactions"
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
                                <h1 className="heading text-center">Transactions</h1>
                            </div>
                            <div className="breadcrumbs style2">
                                <ul>
                                    <li><Link to="/">Home</Link></li>
                                    <li><Link to="#">Activity</Link></li>
                                    <li>Transactions</li>
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
                                        <div key={index} className="sc-card-activity style-2">
                                            <div className="content">
                                                <div className="media">
                                                    <img src={"http://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/images?image_name=" + item.nft.imageUrl} alt="" />
                                                </div>
                                                <div className="infor">
                                                    <h4><Link to="/item-details-01">{item.nft.name}</Link></h4>
                                                    <div className="status"><span className="author"> Seller: {item.seller.nickName} </span> / <span className="author">  Buyer: {item.buyer.nickName}</span></div>
                                                    <p>{item.status}</p>
                                                    <div className="time">
                                                        <h3>
                                                        {item.type} {item.amount}
                                                        </h3>
                                                    </div>
                                                    <div className="time">{item.date}</div>
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

export default Activity02;
