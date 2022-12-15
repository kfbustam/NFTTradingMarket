import React, { useState, useEffect, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import { gapi } from 'gapi-script';
import { GoogleLogin } from 'react-google-login';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const CLIENT_ID = "104101427642-9kkv6e3v2hk1rd01k96nqk1pmgu81vpe.apps.googleusercontent.com"
const SIGN_IN_URL = "http://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/signin"
const SIGN_UP_URL = "http://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/signup"

const Login = ({ fromSignUp = false }) => {
    const [profileData, setProfileData] = useState(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState()
    const [email, setEmail] = React.useState("")
    const [password, setPassword] = React.useState("")

    let navigate = useNavigate();

    React.useEffect(() => {
        // onSuccess -> Go to Sign in page
        if (profileData !== null) {
            localStorage.setItem("profileData", profileData)
            window.sessionStorage.setItem("profileData", profileData);
            toast.success("Logging you in!", {
                autoClose: 1500,
                onClose: () => navigate("/wallet-connect")
            })
        }
    }, [profileData])

    React.useEffect(() => {
        // Render the error msg
        if (profileData !== null && error !== null) {
            toast.error("Something went wrong: " + error)
        }
    }, [error]);

    useEffect(() => {
        if (fromSignUp) {
            toast.info("Verify account before Sign In.", {
                toastId: 1
            })
        }

        const initClient = () => {
            gapi.client.init({
                clientId: CLIENT_ID,
                scope: ''
            });
        };
        gapi.load('client:auth2', initClient);
    });

    const handleSubmit = (event) => {
        event.preventDefault();
        // const data = new FormData(event.currentTarget);
        setLoading(true);

        fetch(SIGN_IN_URL + "?email=" + email + "&password=" + password, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: 'POST',
            body: JSON.stringify({
                email: email,
                password: password,
            }),
            mode: 'cors'
        })
            .then(response => {
                if (response.ok) {
                    return response.json()
                } else {
                    toast.error("Please verify your email.");
                }
                throw response
            })
            .then(data => {
                setProfileData(data)
                localStorage.setItem("token", data.token)
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
        localStorage.setItem("token", response.googleId)

        console.log(response);
        
        fetch(
            SIGN_UP_URL
            + "?email="
            + response.profileObj.email
            + "&password="
            + ""
            + "&firstname="
            + response.profileObj.givenName
            + "&type="
            + "GOOGLE"
            + "&lastname="
            + response.profileObj.familyName
            + "&nickname="
            + response.profileObj.name
            + "&social_token="
            + response.googleId //use google id as token for safety as access tokens have higher refresh rate
            ,
            {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: "POST",
                mode: 'cors'
            })
            .then(response => {
                if (response.ok) {
                    return response.json()
                }
                throw response
            })
            .then(data => {
                console.log("sign up response", data);
            })
            .catch(error => {
                console.error(error)
            }).finally(() => { });

            // check if email is verified for social login
            fetch(SIGN_IN_URL + "?email=" + response.profileObj.email + "&password=token", {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: 'POST',
                body: JSON.stringify({
                    email: response.profileObj.email,
                    password: "token",
                }),
                mode: 'cors'
            })
                .then(response => {
                    if (response.ok) {
                        return response.json()
                    } else {
                        toast.error("Please verify your email.");
                    }
                    throw response
                })
                .then(data => {
                    setProfileData(data)
                    localStorage.setItem("token", data.token)
                })
                .catch(error => {
                    console.error(error)
                    setProfileData(null)
                    setError(error)
                }).finally(() => {
                    setLoading(false)
                });
    };

    const responseGoogleFailure = (response) => {
        toast.error("Login Failed! Please try again.", {
            autoClose: 1000
        })
        console.log("Failure");
        console.log(response);
    };

    return (
        <div>
            <ToastContainer theme="dark" position="top-center" />
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
                                        isSignedIn={false}
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
                                    <form action="#" id="contactform" onSubmit={handleSubmit}>
                                        <input id="email" name="email" tabIndex="1" aria-required="true" type="email" placeholder="Email Address" required value={email} onChange={(event) => { setEmail(event.target.value) }} />
                                        <input id="password" name="password" tabIndex="2" aria-required="true" required type="password" placeholder="Password" value={password} onChange={(event) => { setPassword(event.target.value) }} />
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
