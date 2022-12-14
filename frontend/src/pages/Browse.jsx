import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom'
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import TodayPicks from '../components/layouts/explore-02/TodayPicks'
import todayPickData from '../assets/fake-data/data-today-pick';
import PathBanner from '../components/header/PathBanner';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const GET_ALL_NFTS = process.env.REACT_APP_API_URL + "/nft"
const LISTINGS_URL = process.env.REACT_APP_API_URL + "/listings"

const Browse = () => {
    const [assetCollection, setAssetCollection] = React.useState([]);
    const [dataPanel, setDataPanel] = useState();

    useEffect(() => {
        fetch(
            LISTINGS_URL, // replace with LISTINGS_URL
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
                console.log("listings request successful");
                return response.json();
            }
            throw response
        })
            .then(collection => {
                console.log("listings response data: ");
                console.log(collection);
                setDataPanel([{ id: 1, dataContent: collection }]);
            })
    }, [])


    return (
        <div className='explore'>
            <Header />
            <PathBanner heading="Marketplace" />
            {dataPanel != null && <TodayPicks dataPanel={dataPanel} />}
            <Footer />
        </div>
    );
}

export default Browse;
