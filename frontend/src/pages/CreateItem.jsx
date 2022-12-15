import React from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import { Tab, Tabs, TabList, TabPanel  } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import { useForm } from "react-hook-form";

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate } from 'react-router-dom';

const POST_CREATE_NFT = "http://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/nft/create?token="
const CreateItem = () => {

    const { register, handleSubmit } = useForm();

    const [price, setPrice] = React.useState(0);
    const [selectedCrypto, setSelectedCrypto] = React.useState("ETHEREUM")
    const [title, setTitle] = React.useState("")
    const [description, setDescription] = React.useState("")
    const [filePath, setFilePath] = React.useState()
    let navigate = useNavigate();

    const postItemApi = (data) => {
        toast.info("Uploading your NFT...", {
            toastId: 1
        })

        var formData = new FormData();
        formData.append("nft_image", data.file[0]);
        
        let token = "test123"

        if (typeof(localStorage.getItem("token")) !== undefined && localStorage.getItem("token") !== null
        && localStorage.getItem("token") !== 'undefined') {
            token = localStorage.getItem("token")
        } else {
            localStorage.clear();
            navigate("/login");
        }

        fetch(
            POST_CREATE_NFT + token +
             "&name=" + title + 
             "&description=" + description + 
             "&type=" + selectedCrypto,
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
                toast.success("NFT has been created successfully!", {
                    toastId: 1
                })

                return response.json()
            }
            throw response
        })
        .then(jsonData => {
            
            navigate("/my-nfts");

            console.log("Upload Successful", jsonData)
        }).catch(error => {
            toast.error("Please refresh as your file has changed while you were here!", {
                toastId: 2
            })
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
                                <h1 className="heading text-center">Create NFT</h1>
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

            <ToastContainer theme="dark" position="top-center" />

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
                                            <form action="#" onSubmit={handleSubmit(postItemApi)}>
                                                <h4 className="title-create-item">Upload file</h4>
                                                <label className="uploadFile">
                                                    <span className="filename">PNG, JPG Only</span>
                                                    <input id="nft_selector" type="file" className="inputfile form-control" name="file" onChange={(event) => {setFilePath(event.target.file[0])}} {...register("file")} />
                                                </label>
                                                <div className="seclect-box">
                                                    <div id="item-create" className="dropdown">
                                                        <Link to="#" className="btn-selector nolink">{selectedCrypto}</Link>
                                                        <ul >
                                                            <li onClick={(event) => {setSelectedCrypto("ETHEREUM")}}><span>ETHEREUM</span></li>
                                                            <li onClick={(event) => {setSelectedCrypto("BITCOIN")}}><span>BITCOIN</span></li>
                                                        </ul>
                                                    </div>
                                                </div>
                                                <br /><br /><br />
                                     
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
