import React from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import { Tab, Tabs, TabList, TabPanel  } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';

const POST_CREATE_NFT = "http://localhost:8080/nft/create"
const CreateItem = () => {

    const [price, setPrice] = React.useState(0);
    const [selectedCrypto, setSelectedCrypto] = React.useState("ETH")
    const [title, setTitle] = React.useState("")
    const [description, setDescription] = React.useState("")
    const [filePath, setFilePath] = React.useState("")

    const postItemApi = () => {
        let email = window.localStorage.getItem("email");
        let walletId = window.localStorage.getItem("walletId")
        const formData = new FormData();
        // formData.append("nft_image", )
        fetch(
            POST_CREATE_NFT +
             "?email=" + email +
             "&name=" + title + 
             "&wallet_id=" + walletId + 
             "&description=" + description + 
             "&price=" + price,
             {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                mode: 'cors'
             }
        )
    }

    return (
        <div className='create-item'>
            <Header />
            <section className="flat-title-page inner">
                <div className="overlay"></div>
                <div className="themesflat-container">
                    <div className="row">
                        <div className="col-md-12">
                            <div className="page-title-heading mg-bt-12">
                                <h1 className="heading text-center">Create Item</h1>
                            </div>
                            <div className="breadcrumbs style2">
                                <ul>
                                    <li><Link to="/">Home</Link></li>
                                    <li><Link to="#">Pages</Link></li>
                                    <li>Create Item</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>                    
            </section>
            <div className="tf-create-item tf-section">
                <div className="themesflat-container">
                    <div className="column">
                         <div className="col-12">
                         </div>
                         <div className="col-xl-9 col-lg-6 col-md-12 col-12">
                             <div className="form-create-item">
                                 <form action="#">
                                    <h4 className="title-create-item">Upload file</h4>
                                    <label className="uploadFile">
                                        <span className="filename">PNG, JPG, GIF, WEBP</span>
                                        <input type="file" className="inputfile form-control" name="file" />
                                    </label>
                                 </form>
                                <div className="flat-tabs tab-create-item">
                                    <Tabs>
                                        <TabPanel>
                                            <form action="#">
                                                <div className="seclect-box">
                                                    <div id="item-create" className="dropdown">
                                                        <Link to="#" className="btn-selector nolink">Select Crypto</Link>
                                                        <ul >
                                                            <li><span>ETH</span></li>
                                                            <li><span>BTC</span></li>
                                                        </ul>
                                                    </div>
                                                </div>
                                                <br /><br /><br />
                                                <h4 className="title-create-item">Price</h4>
                                                <input type="text" placeholder="Enter price for item" />

                                                <h4 className="title-create-item">Title</h4>
                                                <input type="text" placeholder="Item Name" />

                                                <h4 className="title-create-item">Description</h4>
                                                <textarea placeholder="e.g. “This is very limited item”"></textarea>

                                                <div className="row-form style-3">
                                                    
                                                    <div className="style-2">
                                                        <div className="seclect-box">
                                                            <div id="item-create" className="dropdown">
                                                            <button className="submit">Add NFT</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </TabPanel>
                                    </Tabs>
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

export default CreateItem;
