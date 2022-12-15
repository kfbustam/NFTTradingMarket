import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Header from '../components/header/Header';
import Footer from '../components/footer/Footer';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const SignUp = () => {
    const [errorText, setErrorText] = React.useState(null)
    const [signupResponse, setSignupResponse] = React.useState(null)
    const [email, setEmail] = React.useState("")
    const [password, setPassword] = React.useState("")
    const [firstName, setFirstName] = React.useState("")
    const [lastName, setLastName] = React.useState("")
    const [nickName, setNickName] = React.useState("")
    

    const EMAIL_REGEX = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/
    const SIGN_UP_URL = "http://localhost:8080/signup"
    let navigate = useNavigate();

    React.useEffect(() => {
        // onSuccess -> Go to Sign in page
        if (signupResponse !== null && signupResponse.email === email) {
            toast.success("Sign Up Successful!")
            navigate("/login");
        }
    }, [signupResponse])

    React.useEffect(() => {
        // Render the error msg
        if(errorText !== null) {
            toast.error("Something went wrong:")
        }
    }, [errorText]);

    const handleSubmit = (event) => {
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
                    return response.json()
                }
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
            <ToastContainer theme="dark" position="top-center" />
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
                                        <Link to="#" className="sc-button style-2 fl-button pri-3">
                                            <i className="icon-fl-google-2"></i>
                                            <span>Google</span>
                                        </Link>
                                    </li>
                                </ul>
                            </div>

                            <div className="flat-form box-login-email">
                                <div className="box-title-login">
                                    <h5>Or signup with email</h5>
                                </div>

                                <div className="form-inner">
                                    <form action="#" id="contactform" onSubmit={handleSubmit}>
                                        <input id="email" name="email" tabIndex="1" aria-required="true" type="email" placeholder="Your Email Address" required value={email} onChange={(event) => {setEmail(event.target.value)}} />
                                        <input id="firstname" name="firstname" tabIndex="2" aria-required="true" required type="text" placeholder="Your First Name" value={firstName} onChange={(event) => {setFirstName(event.target.value)}} />
                                        <input id="lastname" name="lastname" tabIndex="3" aria-required="true" required type="text" placeholder="Your Last Name" value={lastName} onChange={(event) => {setLastName(event.target.value)}}/>
                                        <input id="nickname" name="nickname" tabIndex="4" aria-required="true" required type="text" placeholder="Your Nick Name" value={nickName} onChange={(event) => {setNickName(event.target.value)}} />
                                        <input id="password" name="password" tabIndex="5" aria-required="true" type="password" placeholder="Set Your Password" required value={password} onChange={(event) => {setPassword(event.target.value)}} />
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
