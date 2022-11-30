import LandingPage from './LandingPage';

const React = require('react');
const ReactDOM = require('react-dom');

function App() {

	return (
		<div id="landingPage">
			<LandingPage />
		</div>
	)
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)