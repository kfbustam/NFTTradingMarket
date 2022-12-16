import React , {useEffect, useState} from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import img1 from '../alice_video.png'

const GET_TRANSACTIONS = "http://localhost:8080/transactions?token="

const Activity02 = () => {
    const [apiResponse, setApiResponse] = useState([])

    const [dataFilter, setDataFilter] = useState(
        [
            {
                name: 'Current Listings',
                checked: 'checked',
                type: "current"
            },
            {
                name: 'Past Listings',
                checked: 'checked',
                type: "past"
            },
            {
                name: 'Ethereum',
                checked: 'checked',
                type: "ETHEREUM"
            },
            {
                name: 'Bitcoin',
                checked: 'checked',
                type: "BITCOIN"
            },
            {
                name: 'Last 24 hours',
                checked: 'checked',
                type: "24H"
            },
            {
                name: 'Last Week',
                checked: '',
                type: "1W"
            },
            {
                name: 'Last Month',
                checked: '',
                type: "1M"
            }
        ]
    )

    const [visible , setVisible] = useState(8);

    const applyFilter = () => {

    }

    const clearAllFilters = () => {
        setDataFilter(
            [
                {
                    name: 'Current Listings',
                    checked: '',
                    type: "current"
                },
                {
                    name: 'Past Listings',
                    checked: '',
                    type: "past"
                },
                {
                    name: 'Ethereum',
                    checked: '',
                    type: "ETHEREUM"
                },
                {
                    name: 'Bitcoin',
                    checked: '',
                    type: "BITCOIN"
                },
                {
                    name: 'Last 24 hours',
                    checked: '',
                    type: "24H"
                },
                {
                    name: 'Last Week',
                    checked: '',
                    type: "1W"
                },
                {
                    name: 'Last Month',
                    checked: '',
                    type: "1M"
                }
            ]
        )
    }

    useEffect(() => {
        getTransactionsForUser();
    }, [])

    const getTransactionsForUser = () => {
        let auth_token = "test123"
        let fetchUrl = GET_TRANSACTIONS + auth_token
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
                                                    <img src={"http://localhost:8080/images?image_name=" + item.nft.imageUrl} alt="" />
                                                </div>
                                                <div className="infor">
                                                    <h4><Link to="/item-details-01">{item.nft.name}</Link></h4>
                                                    <div className="status">{item.seller.nickName} <span className="author">{item.buyer.nickName}</span></div>
                                                    <div className="time">{item.status}</div>
                                                    <div className="time">
                                                        <h3>
                                                        {item.type == "BITCOIN" ? "₿" : "Ξ" } {item.amount}/-
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
                                                    <input type="checkbox" defaultChecked={item.checked.length !== 0} />
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
