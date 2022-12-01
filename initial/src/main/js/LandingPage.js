import React, { useState } from 'react';
import SignIn from './SignIn'
import SignUp from './SignUp'
import Dashboard from './dashboard/Dashboard'
import { createTheme, ThemeProvider } from '@mui/material/styles';

const theme = createTheme();

export default function LandingPage() {
  const [profileData, setProfileData] = useState(null)
  const [isSigningUp, setIsSigningUp] = useState(false)

  return (
    <ThemeProvider theme={theme}>
      {
        isSigningUp ? <SignUp setIsSigningUp={setIsSigningUp}/> 
        : (
          profileData != null && profileData.profileObj != null
            ? <Dashboard profileData={profileData.profileObj}/> 
            : <SignIn setProfileData={setProfileData} setIsSigningUp={setIsSigningUp}/>
        )
      }
    </ThemeProvider>
  );
}