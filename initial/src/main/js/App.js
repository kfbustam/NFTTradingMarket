const React = require('react');
const ReactDOM = require('react-dom');

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {};
	}

	componentDidMount() {

	}

	render() {
		return (
      <div>TEST</div>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)