import React , {useState} from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import img1 from '../alice_video.png'


const Activity02 = () => {
    const [dataBox, setDataBox] = useState([
        {
            img: img1,
            title: 'Alice Best Picture',
            status: 'Purchased',
            author: 'alice',
            time: '2022-12-15',
            icon: 'icon-1'
        }]);
    const [dataFilter] = useState(
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
    const showMoreItems = () => {
        setVisible((prevValue) => prevValue + 4);
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
                                    dataBox.slice(0,visible).map((item,index) => (
                                        <div key={index} className="sc-card-activity style-2">
                                            <div className="content">
                                                <div className="media">
                                                    <img src={item.img} alt="" />
                                                </div>
                                                <div className="infor">
                                                    <h4><Link to="/item-details-01">{item.title}</Link></h4>
                                                    <div className="status">{item.status1} <span className="author">{item.author}</span></div>
                                                    <div className="time">{item.time}</div>
                                                </div>
                                            </div>
                                        </div>
                                    ))
                                }
                            </div>
                            {
                                visible < dataBox.length && 
                                <div className="btn-activity mg-t-10 center"> 
                                    <Link to="#" id="load-more" className="sc-button loadmore fl-button pri-3" onClick={showMoreItems}><span>Load More</span></Link>
                                </div>
                            }
                        </div>
                        <div className="col-xl-4 col-lg-3 col-md-4 col-12">

                            <div id="side-bar" className="side-bar style-2">
                                <div className="widget widget-filter style-1 mgbt-0">
                                    <div className="header-widget-filter">
                                        <h3 className="title-widget">Filter</h3>
                                        <Link to="#" className="clear-checkbox btn-filter style-2">
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
