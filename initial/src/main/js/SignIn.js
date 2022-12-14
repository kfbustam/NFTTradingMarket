import React, { useState, useEffect } from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme } from '@mui/material/styles';
import { GoogleLogin } from 'react-google-login';
import Copyright from './Copyright';
import { gapi } from 'gapi-script';

const CLIENT_ID = "104101427642-9kkv6e3v2hk1rd01k96nqk1pmgu81vpe.apps.googleusercontent.com"
const SIGN_IN_URL = "http://localhost:8080/signin"

export default function SignIn({setProfileData, setIsSigningUp}) {
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

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    fetch(SIGN_IN_URL + "?email=" + data.get('email') + "&password=" + data.get('password'), {
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      method: "POST"
    })
    .then(response => {
      if (response.ok) {
        return response.json()
      }
      throw response
    })
    .then(data => {
      setProfileData({...data, profileObj: {}})
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
	}
	const responseGoogleFailure = (response) => {
		console.log("Failure");
    console.log(response);
	}
  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Sign in
        </Typography>
        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
          <TextField
            margin="normal"
            required
            fullWidth
            id="email"
            label="Email Address"
            name="email"
            autoComplete="email"
            autoFocus
          />
          <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            autoComplete="current-password"
          />
          <FormControlLabel
            control={<Checkbox value="remember" color="primary" />}
            label="Remember me"
          />
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <Button
                type="submit"
                variant="contained"
                style={{width: 180, height: 41}}
              >
                Sign In
              </Button>
            </Grid>
            <Grid item xs={12} sm={6}>
              <GoogleLogin
                clientId={CLIENT_ID}
                buttonText="Sign in with Google"
                onSuccess={responseGoogleSuccess}
                onFailure={responseGoogleFailure}
                cookiePolicy={'single_host_origin'}
                style={{display: "flex", flexDirection: "row", justifyContent: "center"}}
              />
            </Grid>
          </Grid>
          <Grid container>
            <Grid item xs>
              <Link href="#" variant="body2">
                Forgot password?
              </Link>
            </Grid>
            <Grid item>
              <Link href="#" variant="body2" onClick={() => setIsSigningUp(true)}>
                {"Don't have an account? Sign Up"}
              </Link>
            </Grid>
          </Grid>
        </Box>
      </Box>
      <Copyright sx={{ mt: 8, mb: 4 }} />
    </Container>
  );
}