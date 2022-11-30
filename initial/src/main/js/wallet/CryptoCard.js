import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

export default function CryptoCard({ imageTitle, imageUrl, imageDescription, price }) {
  return (
    <Card sx={{ maxWidth: 345 }}>
      <CardMedia
        component="img"
        height="140"
        image= { imageUrl }
        alt= { imageTitle }
      />
      <CardContent>
        <Typography gutterBottom variant="h5" component="div">
          { imageTitle }
        </Typography>
        <Typography gutterBottom variant="h6" component="div">
          { price }
        </Typography>
        <Typography variant="body2" color="text.secondary">
          { imageDescription }
        </Typography>
      </CardContent>
      <CardActions>
        <Button size="small">View</Button>
        <Button size="small">Info</Button>
      </CardActions>
    </Card>
  );
}
