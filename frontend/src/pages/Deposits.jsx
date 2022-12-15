import React , { useState, useEffect } from 'react';
import { Link } from 'react-router-dom'
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import PathBanner from '../components/header/PathBanner';
import { useNavigate } from 'react-router-dom';

const GET_WALLET_API = "http://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/wallets"
const POST_DEPOSIT = "http://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/wallet/"

const Deposits = () => {
    const [data, setData] = useState([])
    const [ethAmount, setEthAmount] = useState(0);
    const [btcAmount, setBtcAmount] = useState(0);
    let navigate = useNavigate();

    useEffect(() => {
        hitApi()
    }, [])

    const hitApi = () => {
        let token = "test123"

        if (typeof(localStorage.getItem("token")) !== undefined && localStorage.getItem("token") !== null
        && localStorage.getItem("token") !== 'undefined') {
            token = localStorage.getItem("token")
        } else {
            localStorage.clear();
            navigate("/login");
        }

        fetch(
            GET_WALLET_API + "?token=" + token ,
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
    }

    const depositEth = () => {
        let ethWalletId = data.filter(item => item.type === "ETHEREUM")[0].id
        fetch(
            POST_DEPOSIT + ethWalletId + "/deposit?amount=" + ethAmount,
            {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
            }
        ).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw response
        })
        .then(jsonData => {
            console.log("Deposit Successful", jsonData)
            hitApi();
        }).catch( error => {
            console.log("Deposit Failed", error.json())
        });
    }
    
    const depositBtc = () => {
        let btcWalletId = data.filter(item => item.type === "BITCOIN")[0].id
        fetch(
            POST_DEPOSIT + btcWalletId + "/deposit?amount=" + btcAmount,
            {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
            }
        ).then(response => {
            if (response.ok) {
                return response.json();
            }
            throw response
        })
        .then(jsonData => {
            console.log("Deposit Successful", jsonData)
            hitApi();
        }).catch( error => {
            console.log("Deposit Failed")
        });
    }

    const getWalletLink = (type) => {
        if (type === "ETHEREUM") {
            return <Link to="/create-item" >Ethereum Wallet</Link>
        }
        if (type === "BITCOIN") {
            return <Link to="/create-item" >Bitcoin Wallet</Link>
        }
        if (type === "CREATE") {
            return <Link to="/create-item" >Create NFTs</Link>
        }
        if (type === "COLLECTION") {
            return <Link to="/create-item" >My NFT Collection</Link>
        }
        else {
            return <Link to="/login">Default</Link>
        }
    }
    
    return (
        <div>
            <Header />
            <PathBanner heading="Deposits"/>
            <div className="tf-connect-wallet tf-section">
                <div className="themesflat-container">
                    <div className="row">
                        <div className='col-12'>

                            <div className="sc-box-icon-inner style-2 ct">
                                
                                {
                                    (data === undefined || data.length == 0) ? 
                                        <></> :
                                        <>
                                            <div key="1" className="sc-box-icon col-6">
                                                <div className="img">
                                                    <img src="https://cryptologos.cc/logos/bitcoin-btc-logo.png" alt="Axies" height="50" width="50"/>
                                                </div>
                                                <h4 className="heading">
                                                    {getWalletLink("BITCOIN")}
                                                </h4>
                                                <h1>
                                                    {data.filter(item => item.type === "BITCOIN")[0].balance} BTC
                                                </h1>

                                                <div className="form-inner ">
                                                    <form action="#" id="contactform" onSubmit={depositBtc}>
                                                        <input id="amount" name="amount" tabIndex="1" aria-required="true" type="number" placeholder="Amount to deposit" required step="0.01"
                                                            value={btcAmount} onChange={(event) => {setBtcAmount(event.target.value)}}
                                                        />
                                                        <button className="submit">Deposit to Wallet</button>
                                                    </form>
                                                </div>
                                            </div>

                                            <div key="2" className="sc-box-icon col-5">
                                                <div className="img">
                                                    <img src="https://cryptologos.cc/logos/ethereum-eth-logo.png" alt="Axies" height="50" width="50"/>
                                                </div>
                                                <h4 className="heading">
                                                    {getWalletLink("ETHEREUM")}
                                                </h4>
                                                <h1>
                                                    {data.filter(item => item.type === "ETHEREUM")[0].balance} ETH
                                                </h1>

                                                <div className="form-inner">
                                                    <form action="#" id="contactform" onSubmit={depositEth}>
                                                        <input id="amount" name="amount" tabIndex="1" aria-required="true" type="number" placeholder="Amount to deposit" required step="0.01"
                                                            value={ethAmount} onChange={(event) => {setEthAmount(event.target.value)}}
                                                        />
                                                        <button className="submit">Deposit to Wallet</button>
                                                    </form>
                                                </div>
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
    )
}

export default Deposits;