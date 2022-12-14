import React, {useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import { Tab, Tabs, TabList, TabPanel  } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import { useForm } from "react-hook-form";

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from 'react-router-dom';

const POST_CREATE_NFT = process.env.REACT_APP_API_URL + "/listing?token="

const GET_TRANSACTIONS = process.env.REACT_APP_API_URL + "/nft/token?token="


const ListItem = () => {
    const [apiResponse, setApiResponse] = useState([])

    const { register, handleSubmit } = useForm();

    const [price, setPrice] = React.useState(0);
    const [saleType, setSaleType] = React.useState("IMMEDIATE")
    const [selectedNft, setSelectedNft] = React.useState("")
    const [selectedNftName, setSelectedNftName] = React.useState("Select")

    let navigate = useNavigate();

    const postItemApi = (data) => {
        toast.info("Listing your NFT...", {
            toastId: 1
        })

        let token = "test123"

        if (typeof(localStorage.getItem("token")) !== undefined && localStorage.getItem("token") !== null
        && localStorage.getItem("token") !== 'undefined') {
            token = localStorage.getItem("token")
        } else {
            localStorage.clear();
            navigate("/login");
        }
        
        let fetchUrl = POST_CREATE_NFT + token

        var formData = new FormData();
        fetch(
             fetchUrl +
             "&nftId=" + selectedNft + 
             "&saleType=" + saleType + 
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
                toast.success("NFT has been listed successfully!", {
                   onClose: () => navigate("/my-listings"),
                   autoClose: 1500
                })

                return response.json()
            }
            throw response
        })
        .then(jsonData => {
            console.log("Upload Successful", jsonData)
        }).catch(error => {
            toast.error("Please try again!", {
                toastId: 2
            })
            console.log("error", error)
        })
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
        }
        
        let fetchUrl = GET_TRANSACTIONS + token
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
            jsonData = jsonData.filter(data => data.listing == null || (data.listing !== null && data.listing.status === "SOLD"));
            setApiResponse(jsonData)
            console.log("leng", apiResponse.length)
            console.log("tranactions data", jsonData)
        }).catch(error => {
            console.log("transactions fetch failed", error)
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
                                <h1 className="heading text-center">List NFT</h1>
                            </div>
                            <div className="breadcrumbs style2">
                                <ul>
                                    <li><Link to="/">Home</Link></li>
                                    <li><Link to="#">Pages</Link></li>
                                    <li>List Item</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>                    
            </section>

            <ToastContainer theme="dark" position="top-center" />

            <div className="tf-create-item tf-section">
                <div className="themesflat-container">
                    <div className="column">
                         <div className="col-12">
                         </div>
                         <div className="col-lg-6 col-md-12 col-12">
                             <div className="form-create-item">
                                <div className="flat-tabs tab-create-item">
                                    <Tabs>
                                        <TabPanel>
                                            <form action="#" onSubmit={handleSubmit(postItemApi)}>
                                                            
                                                <h4 className="title-create-item">Listing Type</h4>

                                                <div className="seclect-box">
                                                    <div id="item-create" className="dropdown">
                                                        <Link to="#" className="btn-selector nolink">{saleType}</Link>
                                                        <ul >
                                                            

                                                            <li onClick={(event) => {setSaleType("IMMEDIATE")}}><span>IMMEDIATE</span></li>

                                                            <li onClick={(event) => {setSaleType("AUCTION")}}><span>AUCTION</span></li>
                                                        </ul>
                                                    </div>
                                                </div>
                                                <br></br>
                                                <br></br>
                                                <br></br>
                                                <br></br>
                                                <h4 className="title-create-item">Select your NFT</h4>

                                                <div className="seclect-box">
                                                    <div id="item-create" className="dropdown">
                                                        
                                                        <Link to="#" className="btn-selector nolink">{selectedNftName}</Link>
                                                        <ul >
                                                            {
                                                        apiResponse.map((item,index) => (

                                                            <li onClick={(event) => {setSelectedNft(item.id); setSelectedNftName(item.name)}}><span>{item.name}</span></li>

                                                            ))
                                                            }
                                                        </ul>
                                                    </div>
                                                </div>
                                                <br></br>
                                                        <br></br>
                                                        <br></br>
                                                <h4 className="title-create-item">Price</h4>
                                                <input type="text" placeholder="Enter price for item" value={price} onChange={(event) => {setPrice(event.target.value)}}/>

                                                <div className="row-form">
                                                    
                                                    <div className="text-right">
                                                        <br></br>
                                                        <br></br>
                                                        <br></br>
                                                        
                                                            <button className="submit">List NFT</button>
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

export default ListItem;