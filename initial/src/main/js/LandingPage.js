import React, { useState } from 'react';
import SignIn from './SignIn'
import Dashboard from './dashboard/Dashboard'
import { createTheme, ThemeProvider } from '@mui/material/styles';

const theme = createTheme();

export default function LandingPage() {
  const [data, setData] = useState({sessionToken: 'asdasd'})

  return (
    <ThemeProvider theme={theme}>
      {
        data != null && data.sessionToken != null
        ? <Dashboard /> 
        : <SignIn setData={setData}/>
      }
    </ThemeProvider>
  );
}