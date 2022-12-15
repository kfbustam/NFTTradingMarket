import React , {useEffect, useState} from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import PathBanner from '../components/header/PathBanner';
import Grid from '@material-ui/core/Grid'


const SystemDashboard = () => {

    const [stats, setStats] = useState(null);
    const [selectedCurrency, setSelectedCurrency] = useState("ALL")
    const [selectedTime, setSelectedTime] = useState("DAY")

    useEffect(() => {
        fetchStats()
    }, [selectedCurrency, selectedTime])

    const fetchStats = () => {
        // alert("in here")
        let FETCH_API = "https://localhost:8080/stats?currency="+ selectedCurrency + "&time=" + selectedTime;
        let fetchStatsApiDebug = "https://60261217186b4a001777fbd7.mockapi.io/api/ndkshr/stats";

        fetch(fetchStatsApiDebug, {
            method: "GET",
            header: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (response.ok) {
                return response.json()
            }
            throw response
        }).then(jsonData => {
            setStats(jsonData)
            console.log("success", jsonData)
        }).catch(error => {
            console.log("error", error)
        })
    }

    return(
        <div>
            <Header />
            <PathBanner heading="System Stats" />
                <div className="tf-connect-wallet tf-section">
                <div className="themesflat-container">
                    <div className="row">
                        <div className='col-12'>
                            <div className="sc-box-icon-inner style-2 ct">
                                {
                                    stats === null ? <></> :
                                        <>
                                            <div key="1" className="sc-box-icon col-5">
                                                <h2 className="heading">
                                                    NFT Stats
                                                </h2>
                                                <ul>
                                                    <h4 className='heading'>Active NFTs : {stats.nft_stats.active_nfts}</h4>
                                                    <h4 className='heading'>Active Priced NFTs : {stats.nft_stats.active_priced_nfts}</h4>
                                                    <h4 className='heading'>Active Auction NFTs : {stats.nft_stats.active_auction_nfts}</h4>
                                                    <h4 className='heading'>With Offer NFTs : {stats.nft_stats.with_offer_nfts}</h4>
                                                    <h4 className='heading'>Without Offer NFTs : {stats.nft_stats.without_offer_nfts}</h4>
                                                    <h4 className='heading'>Total Active Offers NFTs : {stats.nft_stats.total_active_offers}</h4>
                                                </ul>
                                            </div>

                                            <div key="2" className="sc-box-icon col-6">
                                                <h2 className="heading">
                                                    Transaction Stats
                                                </h2>

                                                <Grid container spacing={2} sx={{alignContent: "center", justifyCenter: "center"}}>
                                                    <Grid item xs={3} md={3}>
                                                    <div className="seclect-box">
                                                        <div id="item-create" className="dropdown">
                                                            <Link to="#" className="btn-selector nolink">{selectedCurrency}</Link>
                                                            <ul >
                                                                <li onClick={(event) => {setSelectedCurrency("ETH");}}><span>ETH</span></li>
                                                                <li onClick={(event) => {setSelectedCurrency("BTC")}}><span>BTC</span></li>
                                                                <li onClick={(event) => {setSelectedCurrency("ALL")}}><span>ALL</span></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    </Grid>
                                                    <Grid item xs={3} md={3}>
                                                    <div className="seclect-box">
                                                        <div id="item-create" className="dropdown">
                                                            <Link to="#" className="btn-selector nolink">{selectedTime}</Link>
                                                            <ul >
                                                                <li onClick={(event) => {setSelectedTime("DAY");}}><span>Day</span></li>
                                                                <li onClick={(event) => {setSelectedTime("WEK")}}><span>Week</span></li>
                                                                <li onClick={(event) => {setSelectedTime("MON")}}><span>Month</span></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    </Grid>
                                                </Grid>

                                                

                                                <br /> <br /><br /> <br />

                                                

                                                <br /> <br />

                                                <ul>
                                                    <h4 className='heading'>Total Deposits : {stats.txn_stats.total_deposits}</h4>
                                                    <h4 className='heading'>Total Deposit Amount : {stats.txn_stats.total_deposit_amount}</h4>
                                                    <h4 className='heading'>Total Withdrawals : {stats.txn_stats.total_withdrawals}</h4>
                                                    <h4 className='heading'>Total Withdrawal Amount : {stats.txn_stats.total_withdraal_amount}</h4>
                                                    <h4 className='heading'>Total NFT Sales : {stats.txn_stats.total_nft_sales}</h4>
                                                    <h4 className='heading'>Total NFT Sales Amount : {stats.txn_stats.total_nft_sales_amount}</h4>
                                                </ul>
                                            </div>
                                        </>
                                }
                            </div>  
                        </div>    
                    </div>              
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default SystemDashboard;
