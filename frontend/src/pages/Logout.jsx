import React, { useState, useEffect, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import { gapi } from 'gapi-script';
import { GoogleLogin } from 'react-google-login';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const CLIENT_ID = "104101427642-9kkv6e3v2hk1rd01k96nqk1pmgu81vpe.apps.googleusercontent.com"
const SIGN_IN_URL = "http://localhost:8080/signin"


const Logout = ({ fromSignUp=false }) => {
    const [profileData, setProfileData] = useState(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState()
    const [email, setEmail] = React.useState("")
    const [password, setPassword] = React.useState("")

    let navigate = useNavigate();

    useEffect(() => {
        localStorage.clear()
        window.sessionStorage.clear();
        navigate("/");

    });

    return (
        <div>
            <ToastContainer theme="dark" position="top-center" />
            <Header />
           
            <Footer />
        </div>
    );
}

export default Logout;
