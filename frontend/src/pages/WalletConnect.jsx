import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import AddBoxIcon from '@material-ui/icons/AddBox';

import img3 from '../assets/images/icon/Favicon.png'
import PathBanner from '../components/header/PathBanner';

const GET_WALLET_API = "https://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/wallets"

const WalletConnect = () => {
    const [data, setData] = useState([])
    let navigate = useNavigate();

    useEffect(() => {

        let token = "test123"

        if (typeof(localStorage.getItem("token")) !== undefined && localStorage.getItem("token") !== null
        && localStorage.getItem("token") !== 'undefined') {
            token = localStorage.getItem("token")
        } else {
            localStorage.clear();
            navigate("/login");
        }

        fetch(
            GET_WALLET_API + "?token=" + token,
            {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                mode: 'cors'
            }
        ).then(response => {
            if (response.ok) {
                return response.json()
            }
            throw response
        })
            .then(json => {
                setData(json)
                console.log("wallet response", json)
            }).catch((ex) => {
                console.log(ex)
            })
    }, [])

    const getWalletLink = (type) => {
        if (type === "ETHEREUM") {
            return <Link to="/my-nfts" >Ethereum Wallet</Link>
        }
        if (type === "BITCOIN") {
            return <Link to="/my-nfts" >Bitcoin Wallet</Link>
        }
        if (type === "CREATE") {
            return <Link to="/create-item" >Create NFTs</Link>
        }
        if (type === "COLLECTION") {
            return <Link to="/my-nfts" >My NFT Collection</Link>
        }
        else {
            return <Link to="/login">Default</Link>
        }
    }

    return (
        <div>
            <Header />
            <PathBanner heading="Wallets" />
            <div className="tf-connect-wallet tf-section">
                <div className="themesflat-container">
                    <div className="row">
                        <div className="col-12">
                            <h2 className="tf-title-heading ct style-2 mg-bt-12">
                                Manage Your Wallet
                            </h2>
                            <h5 className="sub-title ct style-1 pad-400">
                                Your Crypto wallets, NFT Collections, and NFT Creation is available here.
                            </h5>
                        </div>
                        <div className='col-12'>

                            <div className="sc-box-icon-inner style-2 ct col-12">

                                {
                                    (data === undefined || data.length == 0) ?
                                        <></> :
                                        <>
                                            <div key="1" className="sc-box-icon">
                                                <div className="img">
                                                    <img src="https://cryptologos.cc/logos/bitcoin-btc-logo.png" alt="Axies" height="50" width="50" />
                                                </div>
                                                <h4 className="heading">
                                                    {getWalletLink("BITCOIN")}
                                                </h4>
                                                <h1>
                                                    {data.filter(item => item.type === "BITCOIN")[0].balance} BTC
                                                </h1>
                                            </div>

                                            <div key="2" className="sc-box-icon">
                                                <div className="img">
                                                    <img src="https://cryptologos.cc/logos/ethereum-eth-logo.png" alt="Axies" height="50" width="50" />
                                                </div>
                                                <h4 className="heading">
                                                    {getWalletLink("ETHEREUM")}
                                                </h4>
                                                <h1>
                                                    {data.filter(item => item.type === "ETHEREUM")[0].balance} ETH
                                                </h1>
                                            </div>
                                        </>
                                }

                                <div key="3" className="sc-box-icon">
                                    <div className="img">
                                        <img src={img3} alt="Axies" height="50" width="50" />
                                    </div>
                                    <h4 className="heading">
                                        {getWalletLink("CREATE")}
                                    </h4>
                                    <p className="content">
                                        Create a new NFT artifact and sell it on our marketplace.
                                    </p>
                                </div>

                                <div key="4" className="sc-box-icon">
                                    <div className="img">
                                        <img src={img3} alt="Axies" height="50" width="50" />
                                    </div>
                                    <h4 className="heading">
                                        {getWalletLink("COLLECTION")}
                                    </h4>
                                    <p className="content">
                                        View your NFT Collection. Add new NFTs to your collection or sell the ones you have.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default WalletConnect;
