import React , { useState } from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';

import img3 from '../assets/images/icon/Favicon.png'
import PathBanner from '../components/header/PathBanner';

const WalletConnect = () => {
    const [data] = useState(
        [
            {
                img: "https://cryptologos.cc/logos/bitcoin-btc-logo.png",
                title: 'Bitcoin Wallet',
                description: '4.23BTC'
            },
            {
                img: "https://cryptologos.cc/logos/ethereum-eth-logo.png",
                title: 'Ethereum Wallet',
                description: '4.06ETH'
            },
            {
                img: img3,
                title: 'My NFT Collections',
                description: ''
            },
        ]
    )
    return (
        <div>
            <Header />
            <PathBanner heading="Manage Wallet" />
            <div className="tf-connect-wallet tf-section">
                <div className="themesflat-container">
                    <div className="row">
                        <div className="col-12">
                            <h2 className="tf-title-heading ct style-2 mg-bt-12">
                                Connect Your Wallet
                            </h2>
                            <h5 className="sub-title ct style-1 pad-400">
                                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Laborum obcaecati dignissimos quae quo ad iste ipsum officiis deleniti asperiores sit.
                            </h5>
                        </div>
                        <div >
                            <div className="sc-box-icon-inner style-2 ct mg-bt-12">
                                {
                                    data.map((item,index) => (
                                        <div key={index} className="sc-box-icon">
                                            <div className="img">
                                                <img src={item.img} alt="Axies" height="50" width="50"/>
                                            </div>
                                            <h4 className="heading"><Link to="/login">{item.title}</Link> </h4>
                                            <p className="content">{item.description}</p>
                                         </div>
                                    ))
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

export default WalletConnect;
