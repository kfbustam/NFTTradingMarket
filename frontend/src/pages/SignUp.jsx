import React, {useEffect, useState} from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { GoogleLogin } from 'react-google-login';
import { gapi } from 'gapi-script';

const CLIENT_ID = "104101427642-9kkv6e3v2hk1rd01k96nqk1pmgu81vpe.apps.googleusercontent.com"
const SIGN_IN_URL = "https://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/signin"
const SIGN_UP_URL = "https://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/signup"

const SignUp = () => {
    const [errorText, setErrorText] = React.useState(null)
    const [signupResponse, setSignupResponse] = React.useState(null)
    const [email, setEmail] = React.useState("")
    const [password, setPassword] = React.useState("")
    const [firstName, setFirstName] = React.useState("")
    const [lastName, setLastName] = React.useState("")
    const [nickName, setNickName] = React.useState("")
    const [profileData, setProfileData] = useState(null)

    const EMAIL_REGEX = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/
    const SIGN_UP_URL = "https://cmpe275nftapp-env.eba-3guv8rep.us-east-1.elasticbeanstalk.com/signup"
    let navigate = useNavigate();


    useEffect(() => {

        const initClient = () => {
            gapi.client.init({
                clientId: CLIENT_ID,
                scope: ''
            });
        };
        gapi.load('client:auth2', initClient);
    });

    React.useEffect(() => {
        // onSuccess -> Go to Sign in page
        if (profileData !== null) {
            localStorage.setItem("profileData", profileData)
            window.sessionStorage.setItem("profileData", profileData);
        }
    }, [profileData])

    const responseGoogleSuccess = (response) => {
        toast.info("Processing your request...", {
            id: 1,
            autoClose: 500
        })
        console.log("Successful");
        localStorage.setItem("token", response.googleId)

        console.log(response);
        setProfileData({
            email: response.profileObj.email,
            ...response
        })
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
                    if (response.status === 200) {
                        toast.success("Logging you in!", {
                            autoClose: 1000,
                            onClose: () => navigate("/wallet-connect")
                        })

                        
                    } else {
                        toast.info("New Account Created. Please verify email!", {
                            autoClose: 1000,
                            onClose: () => navigate("/login")
                        })
                    }
                    return response.json();
                }
                toast.error("Something went wrong. Please try again.", {id: 1});
                throw response
            })
            .then(data => {
                console.log("sign up response", data);
            })
            .catch(error => {
                console.error(error)
            }).finally(() => { });
    };

    const responseGoogleFailure = (response) => {
        console.log("Failure");
        console.log(response);
    };

    React.useEffect(() => {
        // onSuccess -> Go to Sign in page
        if (signupResponse !== null && signupResponse.email === email) {
            navigate("/login");
        }
    }, [signupResponse])

    React.useEffect(() => {
        // Render the error msg
        if (errorText !== null) {
            toast.error("Something went wrong:")
        }
    }, [errorText]);

    const handleSubmit = (event) => {

        if (/^[a-zA-Z0-9]*$/.test(nickName) == false) {
            toast.error("Only Alpha-Numeric characters allowed in nickname.");
            console.log('Invalid nickname')
        } 

            toast.info("Processing...");

            event.preventDefault();
            const data = new FormData(event.currentTarget);
            fetch(
                SIGN_UP_URL
                + "?email="
                + email
                + "&password="
                + password
                + "&firstname="
                + firstName
                + "&type="
                + "LOCAL"
                + "&lastname="
                + lastName
                + "&nickname="
                + nickName,
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
                        toast.success("Sign Up Successful! Please verify your email.")
                        return response.json()
                    }
                    toast.error("Something went wrong. Please try again.");
                    throw response
                })
                .then(data => {
                    setSignupResponse(data)
                    console.log("sign up response", data);
                })
                .catch(error => {
                    console.error(error)
                    setErrorText(error)
                }).finally(() => { });
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
                                <h1 className="heading text-center">Signup</h1>
                            </div>
                            <div className="breadcrumbs style2">
                                <ul>
                                    <li><Link to="/">Home</Link></li>
                                    <li><Link to="#">Pages</Link></li>
                                    <li>Signup</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <ToastContainer theme="dark" position="top-center" />

            <section className="tf-login tf-section">
                <div className="themesflat-container">
                    <div className="row">
                        <div className="col-12">
                            <h2 className="tf-title-heading ct style-1">
                                Sigup To NFTs
                            </h2>

                            <div className="flat-form box-login-social">
                                <div className="box-title-login">
                                    <h5>Sign Up with Google</h5>
                                </div>
                                <ul className='col-12'>
                                    <li className='col-12'>
                                    <div >
                                    <GoogleLogin
                                        theme="dark"
                                        className="sc-button style-2 fl-button pri-3"
                                        clientId={CLIENT_ID}
                                        buttonText="Sign Up with Google"
                                        onSuccess={responseGoogleSuccess}
                                        onFailure={responseGoogleFailure}
                                        cookiePolicy={'single_host_origin'}
                                        isSignedIn={false}
                                    />
                                </div>
                                    </li>
                                </ul>
                            </div>

                            <div className="flat-form box-login-email">
                                <div className="box-title-login">
                                    <h5>Or signup with email</h5>
                                </div>

                                <div className="form-inner">
                                    <form action="#" id="contactform" onSubmit={handleSubmit}>
                                        <input id="email" name="email" tabIndex="1" aria-required="true" type="email" placeholder="Your Email Address" required value={email} onChange={(event) => { setEmail(event.target.value) }} />
                                        <input id="firstname" name="firstname" tabIndex="2" aria-required="true" required type="text" placeholder="Your First Name" value={firstName} onChange={(event) => { setFirstName(event.target.value) }} />
                                        <input id="lastname" name="lastname" tabIndex="3" aria-required="true" required type="text" placeholder="Your Last Name" value={lastName} onChange={(event) => { setLastName(event.target.value) }} />
                                        <input id="nickname" name="nickname" tabIndex="4" aria-required="true" required type="text" placeholder="Your Nick Name" value={nickName} onChange={(event) => { setNickName(event.target.value) }} />
                                        <input id="password" name="password" tabIndex="5" aria-required="true" type="password" placeholder="Set Your Password" required value={password} onChange={(event) => { setPassword(event.target.value) }} />
                                        <button className="submit">Sign Up</button>
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

export default SignUp;
