import React , { useState, useEffect } from 'react';
import { Link } from 'react-router-dom'
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import PathBanner from '../components/header/PathBanner';

const GET_WALLET_API = "http://localhost:8080/wallets"
const POST_DEPOSIT = "http://localhost:8080/wallet"

const Withdrawal = () => {

    const [data, setData] = useState([])
    const [ethAmount, setEthAmount] = useState(0);
    const [btcAmount, setBtcAmount] = useState(0);
    
    const AUTH_TOKEN = "test123" // fetch token from localStorage

    useEffect(() => {
        hitApi();
    }, [])

    const hitApi = () => {
        fetch(
            GET_WALLET_API + "?token=" + AUTH_TOKEN ,
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

    const withdrawAmountEth = () => {
        let ethWalletId = data.filter(item => item.type === "ETHEREUM")[0].id
        fetch(
            POST_DEPOSIT + "/" + ethWalletId + "/withdraw?amount=" + ethAmount,
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
            console.log("Withdrawal Successful", jsonData)
            hitApi()
        }).catch( error => {
            console.log("Withdrawal Failed")
        });
    }
    
    const withdrawAmountBtc = () => {
        let btcWalletId = data.filter(item => item.type === "BITCOIN")[0].id
        fetch(
            POST_DEPOSIT + "/" + btcWalletId + "/withdraw?amount=" + btcAmount,
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
            console.log("Withdrawal Successful", jsonData)
            hitApi()
        }).catch( error => {
            console.log("Withdrawal Failed")
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
            <PathBanner heading="Withdrawals"/>
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
                                                    <form action="#" id="contactform" onSubmit={withdrawAmountBtc}>
                                                        <input id="amount" name="amount" tabIndex="1" aria-required="true" type="number" placeholder="Amount to withdraw" required step="0.01"
                                                            value={btcAmount} onChange={(event) => {setBtcAmount(event.target.value)}}
                                                        />
                                                        <button className="submit">Withdraw from Wallet</button>
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
                                                    <form action="#" id="contactform" onSubmit={withdrawAmountEth}>
                                                        <input id="amount" name="amount" tabIndex="1" aria-required="true" type="number" placeholder="Amount to withdraw" required step="0.01" 
                                                            value={ethAmount} onChange={(event) => {setEthAmount(event.target.value)}}
                                                        />
                                                        <button className="submit">Withdraw from Wallet</button>
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

export default Withdrawal;