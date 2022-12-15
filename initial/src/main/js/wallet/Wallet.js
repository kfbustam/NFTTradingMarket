import * as React from 'react';
import { experimentalStyled as styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Grid from '@mui/material/Grid';
import CryptoCard from './CryptoCard';
import Button from '@mui/material/Button';

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
  ...theme.typography.body2,
  padding: theme.spacing(2),
  textAlign: 'center',
  color: theme.palette.text.secondary,
}));

export default function Wallet() {
    const debugValue = {
        imageTitle: "Beeple",
        imageUrl: "https://static01.nyt.com/images/2021/03/12/arts/11nft-auction1/11nft-auction1-videoSixteenByNineJumbo1600.jpg",
        imageDescription: "Bidding at the two-week Beeple sale, consisting of just one lot, began at $100. With seconds remaining, the work was set to sell for less than $30 million, but a last-moment cascade of bids prompted a two-minute extension of the auction and pushed the final price over $60 million",
        price: "60 Million"
    };
  return (
    <div>

        <div>
            <h2>My NFT Collection</h2>

            <Button variant="contained">Create NFT</Button>
        </div>
        <Box sx={{ flexGrow: 1 }}>
        <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }}>
            {Array.from(Array(6)).map((_, index) => (
            <Grid item xs={2} sm={4} md={4} key={index}>
                <CryptoCard imageTitle = { debugValue.imageTitle } imageUrl= { debugValue.imageUrl } imageDescription = { debugValue.imageDescription } price = { debugValue.price }></CryptoCard>
            </Grid>
            ))}
        </Grid>
        </Box>
    </div>
  );
}
