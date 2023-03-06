import React from 'react';
import './App.css';
import Box from '@material-ui/core/Box';
import Paper from '@material-ui/core/Paper';
import Checkbox from '@material-ui/core/Checkbox';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';

function App() {
	const [moveDistanceOn, setMoveDistanceOn] = React.useState(false);
	const [wheelDistanceOn, setWheelDistanceOn] = React.useState(false);
	const [moveSpeedOn, setMoveSpeedOn] = React.useState(false);
	const [wheelSpeedOn, setWheelSpeedOn] = React.useState(false);
	
	const [moveDistance, setMoveDistance] = React.useState(0);
	const [wheelDistance, setWheelDistance] = React.useState(0);
	
	const [previousMovePoint, setMovePoint] = React.useState(null);
	
	const [movementStart, setMovementStart] = React.useState(null);
	const [wheelStart, setWheelStart] = React.useState(null);
	
	const [updateTimeout, setUpdateTimeout] = React.useState(null);
	
	const setMoveTracking = (on) => {
		setMovementStart(on?Date.now():null);
		setMoveDistance(on?moveDistance:0);
		setMoveDistanceOn(on);
	}
	const setWheelTracking = (on) => {
		setWheelStart(on?Date.now():null);
		setWheelDistance(on?wheelDistance:0);
		setWheelDistanceOn(on);
	}
	const setMoveSpeedTracking = (on) => {
		setMovementStart(on?Date.now():null);
		setMoveSpeedOn(on);
	}
	const setWheelSpeedTracking = (on) => {
		setWheelStart(on?Date.now():null);
		setWheelSpeedOn(on);
	}
	const updateValues = () => {
		if (updateTimeout !== null);
			clearTimeout(updateTimeout);
		if (moveSpeedOn || wheelSpeedOn) {
			setUpdateTimeout(setTimeout(updateValues, 300));
		}
	}
	
	const mouseMove = (event) => {
		if (previousMovePoint !== null)
			setMoveDistance(moveDistance + Math.hypot(event.clientX - previousMovePoint.x, event.clientY - previousMovePoint.y));
		setMovePoint({'x': event.clientX, 'y': event.clientY});
		updateValues();
	}
	
	const mouseWheel = (event) => {
		setWheelDistance(wheelDistance + Math.abs(event.deltaY));
		updateValues();
	}
	
	const moveDistanceListener = moveDistanceOn?mouseMove:null;
	const wheelDistanceListener = wheelDistanceOn?mouseWheel:null;
	const outputMessage = "Move distance: " + Math.round(moveDistance) + " Scroll distance: " + Math.round(wheelDistance);
	const outputMessage2 = (moveSpeedOn?("Move speed: " + Math.round(moveDistance / ((Date.now() - movementStart)/1000)))+"/s ":"") + (wheelSpeedOn?("Scroll speed: " + (Math.round(wheelDistance / ((Date.now() - wheelStart)/1000))) + "/s"):"");
	return (
		<div className="App">
			<Box display="flex">
				<Paper style={{width: 400}}>
					<Box display="flex" width="80">
						<Box display="flex" flexDirection="column" m={2}>
							<Box height={40}>
								<Typography varian="h6"></Typography>
							</Box>
							<Box height={40}>
								<Typography varian="h6">Mouse</Typography>
							</Box>
							<Box height={40}>
								<Typography varian="h6">Wheel</Typography>
							</Box>
						</Box>
						<Box display="flex" flexDirection="column" m={2}>
							<Box height={40}>
								<Typography varian="h6">Movement</Typography>
							</Box>
							<Box height={40}>
								<Checkbox onChange={(e, c) => {setMoveTracking(c)}} />
							</Box>
							<Box height={40}>
								<Checkbox onChange={(e, c) => {setWheelTracking(c)}} />
							</Box>
						</Box>
						<Box display="flex" flexDirection="column" m={2}>
							<Box height={40}>
								<Typography varian="h6">Speed</Typography>
							</Box>
							<Box height={40}>
								<Checkbox onChange={(e, c) => {setMoveSpeedTracking(c)}} />
							</Box>
							<Box height={40}>
								<Checkbox onChange={(e, c) => {setWheelSpeedTracking(c)}} />
							</Box>
						</Box>
					</Box>
					<Grid container >
						<Typography>{outputMessage}</Typography>
						<Typography>{outputMessage2}</Typography>
					</Grid>
				</Paper>
				<Paper style={{width: 400, background: 'gray',}} onMouseMove={moveDistanceListener}  onWheel={wheelDistanceListener}>
					Tracking area
				</Paper>
			</Box>
		</div>
	);
}

export default App;
