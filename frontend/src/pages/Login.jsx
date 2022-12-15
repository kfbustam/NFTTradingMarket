import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import { gapi } from 'gapi-script';
import { GoogleLogin } from 'react-google-login';

const CLIENT_ID = "104101427642-9kkv6e3v2hk1rd01k96nqk1pmgu81vpe.apps.googleusercontent.com"
const SIGN_IN_URL = "http://localhost:8080/signin"

const Login = () => {
    const [profileData, setProfileData] = useState(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState()
  
    useEffect(() => {
      const initClient = () => {
            gapi.client.init({
            clientId: CLIENT_ID,
            scope: ''
          });
       };
       gapi.load('client:auth2', initClient);
    });

    useEffect(() => {
        window.sessionStorage.setItem("auth_data", JSON.stringify(profileData));
        console.log("my profile data", profileData)
    }, [profileData])

    const handleSubmit = (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
    
        fetch(SIGN_IN_URL, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: 'POST',
            body: JSON.stringify({
                email: data.get('email'),
                password: data.get('password'),
            })
        })
        .then(response => {
            if (response.ok) {
                return response.json()
            }
            throw response
        })
        .then(data => {
            setProfileData(data)
        })
        .catch(error => {
            console.error(error)
            setProfileData(null)
            setError(error)
        }).finally(() => {
            setLoading(false)
        });
    };

    const responseGoogleSuccess = (response) => {
		console.log("Successful");
        console.log(response);
        setProfileData(response);
	};

	const responseGoogleFailure = (response) => {
		console.log("Failure");
    console.log(response);
	};

    return (
        <div>
            <Header />
            <section className="flat-title-page inner">
                <div className="overlay"></div>
                <div className="themesflat-container">
                    <div className="row">
                        <div className="col-md-12">
                            <div className="page-title-heading mg-bt-12">
                                <h1 className="heading text-center">Account</h1>
                            </div>
                            <div className="breadcrumbs style2">
                                <ul>
                                    <li><Link to="/">Home</Link></li>
                                    <li>Account</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>                    
            </section>
            <section className="tf-login tf-section">
                <div className="themesflat-container">
                    <div className="row">
                        <div className="col-12">
                            <h2 className="tf-title-heading ct style-1">
                                Login To NFTs
                            </h2>

                            <div className="flat-form box-login-social" >
                                <div className="box-title-login">
                                    <h5>Social Login</h5>
                                </div>
                                {/* <ul>
                                    <li>
                                        <Link to="#" className="sc-button style-2 fl-button pri-3">
                                            <i className="icon-fl-google-2"></i>
                                            <span>Google</span>
                                        </Link>
                                    </li>
                                    <li> */}
                                    <div >
                                        <GoogleLogin
                                            theme="dark"
                                            className="sc-button style-2 fl-button pri-3"
                                            clientId={CLIENT_ID}
                                            buttonText="Sign in with Google"
                                            onSuccess={responseGoogleSuccess}
                                            onFailure={responseGoogleFailure}
                                            cookiePolicy={'single_host_origin'}
                                            isSignedIn={true}
                                        />
                                    </div>
                                        
                                    {/* </li>
                                </ul> */}
                            </div>

                            <div className="flat-form box-login-email">
                                <div className="box-title-login">
                                    <h5>Or login with email</h5>
                                </div>

                                <div className="form-inner">
                                    <form action="#" id="contactform">
                                        <input id="name" name="name" tabIndex="1" aria-required="true" required type="text" placeholder="Your Full Name" />
                                        <input id="email" name="email" tabIndex="2"  aria-required="true" type="email" placeholder="Your Email Address" required />
                                        <div className="row-form style-1">
                                            <label>Remember me
                                                <input type="checkbox" />
                                                <span className="btn-checkbox"></span>
                                            </label>
                                            <Link to="#" className="forgot-pass">Forgot Password ?</Link>
                                        </div>

                                        <button className="submit">Login</button>
                                    </form>
                                </div>

                            </div>

                        </div>
                    </div>
                </div>
            </section>
            <Footer />
        </div>
    );
}

export default Login;
