import LandingPage from './LandingPage';
// import React from 'react';
// import ReactDom from 'react-dom';
import * as ReactDOMClient from 'react-dom/client';

const React = require('react');

const container = document.getElementById('react');

// Create a root.
const root = ReactDOMClient.createRoot(container);

// Initial render: Render an element to the root.
root.render(<LandingPage />);