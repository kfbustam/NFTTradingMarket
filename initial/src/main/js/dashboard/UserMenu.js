import React, { useState } from 'react';
import Avatar from '@mui/material/Avatar';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import Divider from '@mui/material/Divider';
import PersonAdd from '@mui/icons-material/PersonAdd';
import Settings from '@mui/icons-material/Settings';
import Logout from '@mui/icons-material/Logout';
import { useGoogleLogout } from 'react-google-login';

const CLIENT_ID = "104101427642-9kkv6e3v2hk1rd01k96nqk1pmgu81vpe.apps.googleusercontent.com"

export default function UserMenu({anchorEl, handleClose, isOpen, profileData, setProfileData, setIsSigningUp}) {
  const {name, imageUrl, givenName, familyName, email} = profileData;
  const onLogoutSuccess = (res) => {
    console.log("logout success")
    setProfileData(null)
    setIsSigningUp(false)
  }
  const onLogoutFailure = (err) => {
    console.log("logout failure")
    console.err(err)
  }
  const { signOut, loaded } = useGoogleLogout({clientId: CLIENT_ID, onLogoutSuccess: onLogoutSuccess, onFailure: onLogoutFailure})
  return (
    <Menu
    anchorEl={anchorEl}
    id="account-menu"
    open={isOpen}
    onClose={handleClose}
    onClick={handleClose}
    PaperProps={{
      elevation: 0,
      sx: {
        overflow: 'visible',
        filter: 'drop-shadow(0px 2px 8px rgba(0,0,0,0.32))',
        mt: 1.5,
        '& .MuiAvatar-root': {
          width: 32,
          height: 32,
          ml: -0.5,
          mr: 1,
        },
        '&:before': {
          content: '""',
          display: 'block',
          position: 'absolute',
          top: 0,
          right: 14,
          width: 10,
          height: 10,
          bgcolor: 'background.paper',
          transform: 'translateY(-50%) rotate(45deg)',
          zIndex: 0,
        },
      },
    }}
    transformOrigin={{ horizontal: 'right', vertical: 'top' }}
    anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
  >
    <MenuItem>
      <Avatar /> Profile
    </MenuItem>
    <MenuItem>
      <Avatar /> My account
    </MenuItem>
    <Divider />
    <MenuItem>
      <ListItemIcon>
        <PersonAdd fontSize="small" />
      </ListItemIcon>
      Add another account
    </MenuItem>
    <MenuItem>
      <ListItemIcon>
        <Settings fontSize="small" />
      </ListItemIcon>
      Settings
    </MenuItem>
    <MenuItem onClick={() => signOut()}>
      <ListItemIcon>
        <Logout fontSize="small" />
      </ListItemIcon>
      Logout
    </MenuItem>
  </Menu>
  );
}