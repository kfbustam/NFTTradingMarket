import React, { useState } from 'react';
import SignIn from './SignIn'
import Dashboard from './dashboard/Dashboard'
import { createTheme, ThemeProvider } from '@mui/material/styles';

const theme = createTheme();

export default function LandingPage() {
  const [profileData, setProfileData] = useState(null)

  React.useEffect(() => {
    console.log("profile data", profileData)
  }, [profileData])

  return (
    <ThemeProvider theme={theme}>
      {
        profileData != null && profileData.profileObj != null
        ? <Dashboard profileData={profileData.profileObj}/> 
        : <SignIn setProfileData={setProfileData}/>
      }
    </ThemeProvider>
  );
}