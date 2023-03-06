import React from 'react';
import './App.css';
import Typography from '@material-ui/core/Typography';
import Slider from '@material-ui/core/Slider';

function App() {
	const [red, setRed] = React.useState(255);
	const [green, setGreen] = React.useState(255);
	const [blue, setBlue] = React.useState(255);
	
	const updateColor = (component, value) => {
		if (component === 0)
			setRed(value);
		if (component === 1)
			setGreen(value);
		if (component === 2)
			setBlue(value);
	}
	
	const color = "rgba(" + red + "," + green + "," + blue + ",1)";

	return (
		<div style={{backgroundColor: color, padding: "10%", height: "50%"}}>
			<div style={{height: "12em"}}>
				<Typography id="range-slider" gutterBottom>
					RGB Color
				</Typography>
				<Slider
					value={red}
					orientation="vertical"
					valueLabelDisplay="auto"
					step={1}
					min={0}
					max={255}
					onChange={(event, value) => updateColor(0, value)}
				/>
				<Slider
					value={green}
					orientation="vertical"
					valueLabelDisplay="auto"
					step={1}
					min={0}
					max={155}
					onChange={(event, value) => updateColor(1, value)}
				/>
				<Slider
					value={blue}
					orientation="vertical"
					valueLabelDisplay="auto"
					step={1}
					min={0}
					max={155}
					onChange={(event, value) => updateColor(2, value)}
				/>
			</div>
		</div>
	);
}

export default App;
