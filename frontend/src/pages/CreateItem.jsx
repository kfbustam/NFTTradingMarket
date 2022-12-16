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
    const [filePath, setFilePath] = React.useState()

    const postItemApi = () => {
        let email = "nandugop@gmail.com"
        let walletId = "8a8080e185157648018515acabea0005"
        var formData = new FormData();
        formData.append("nft_image", filePath);
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
                    'Accept': 'application/json'
                },
                body: formData,
                mode: 'cors'
             }
        ).then(response => {
            if (response.ok) {
                return response.json()
            }
            throw response
        })
        .then(jsonData => {
            console.log("Upload Successful", jsonData)
        }).catch(error => {
            console.log("error", error)
        })
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
                                <div className="flat-tabs tab-create-item">
                                    <Tabs>
                                        <TabPanel>
                                            <form action="#" onSubmit={postItemApi}>
                                                <h4 className="title-create-item">Upload file</h4>
                                                <label className="uploadFile">
                                                    <span className="filename">PNG, JPG, GIF, WEBP</span>
                                                    <input id="nft_selector" type="file" className="inputfile form-control" name="file" onChange={(event) => {setFilePath(event.target.file[0])}} />
                                                </label>
                                                <div className="seclect-box">
                                                    <div id="item-create" className="dropdown">
                                                        <Link to="#" className="btn-selector nolink">{selectedCrypto}</Link>
                                                        <ul >
                                                            <li onCLick={(event) => {setSelectedCrypto("ETH")}}><span>ETH</span></li>
                                                            <li onClick={(event) => {setSelectedCrypto("BTC")}}><span>BTC</span></li>
                                                        </ul>
                                                    </div>
                                                </div>
                                                <br /><br /><br />
                                                <h4 className="title-create-item">Price</h4>
                                                <input type="text" placeholder="Enter price for item" value={price} onChange={(event) => {setPrice(event.target.value)}}/>

                                                <h4 className="title-create-item">Title</h4>
                                                <input type="text" placeholder="Item Name" value={title} onChange={(event) => {setTitle(event.target.value)}}/>

                                                <h4 className="title-create-item">Description</h4>
                                                <textarea placeholder="e.g. “This is very limited item”" value={description} onChange={(event) => {setDescription(event.target.value)}}></textarea>

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
