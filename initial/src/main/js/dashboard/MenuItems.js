import * as React from 'react';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import ListSubheader from '@mui/material/ListSubheader';
import DashboardIcon from '@mui/icons-material/Dashboard';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import PeopleIcon from '@mui/icons-material/People';
import BarChartIcon from '@mui/icons-material/BarChart';
import LayersIcon from '@mui/icons-material/Layers';
import AssignmentIcon from '@mui/icons-material/Assignment';

export default function MenuItems({selected, setSelected}) {

  return (
    <React.Fragment>
      <ListItemButton onClick={(e) => setSelected("dashboard")} selected={selected == "dashboard"}>
        <ListItemIcon>
          <DashboardIcon />
        </ListItemIcon>
        <ListItemText primary="Dashboard" />
      </ListItemButton>
      <ListItemButton onClick={(e) => setSelected("wallet")} selected={selected == "wallet"}>
        <ListItemIcon>
          <ShoppingCartIcon />
        </ListItemIcon>
        <ListItemText primary="Wallet" />
      </ListItemButton>
      <ListItemButton onClick={(e) => setSelected("cryptocurrencies")} selected={selected == "cryptocurrencies"}>
        <ListItemIcon>
          <PeopleIcon />
        </ListItemIcon>
        <ListItemText primary="Cryptocurrencies" />
      </ListItemButton>
      <ListItemButton onClick={(e) => setSelected("nfts")} selected={selected == "nfts"}>
        <ListItemIcon>
          <BarChartIcon />
        </ListItemIcon>
        <ListItemText primary="NFTs" />
      </ListItemButton>
      <ListItemButton onClick={(e) => setSelected("balances")} selected={selected == "balances"}>
        <ListItemIcon>
          <LayersIcon />
        </ListItemIcon>
        <ListItemText primary="Balances" />
      </ListItemButton>
    </React.Fragment>
  );
}