import SignIn from './SignIn';

const React = require('react');
const ReactDOM = require('react-dom');

function App() {

	return (
		<div id="landingPage">
			<SignIn />
		</div>
	)
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)