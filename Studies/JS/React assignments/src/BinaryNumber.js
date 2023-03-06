import React from 'react';
import './App.css';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Checkbox from '@material-ui/core/Checkbox';
import Box from '@material-ui/core/Box';

/**
  * This is a rather straighforward solution to Exercise 1.2. It utilizes
  * index parameter in binary value update to avoid having multiple, almost
  * identical method implementations.
  */ 
function App() {
	const [bits, setBits] = React.useState([0, 0, 0, 0, 0, 0, 0, 0]);
	const [decimal, setDecimal] = React.useState(0);
	
	const adjustBit = (box, isChecked) => {
		let bs = bits.slice(0, box).concat([isChecked?1:0]).concat(bits.slice(box+1));
		setBits(bs);
	}
	
	const convert = () => {
		setDecimal("" + parseInt(bits.join(''), 2));
	}
	
	return (
		<div className="App">
			<Box>
				<Checkbox id="x" onChange={(e, c) => {adjustBit(0, c)}} />
				<Checkbox onChange={(e, c) => {adjustBit(1, c)}} />
				<Checkbox onChange={(e, c) => {adjustBit(2, c)}} />
				<Checkbox onChange={(e, c) => {adjustBit(3, c)}} />
				<Checkbox onChange={(e, c) => {adjustBit(4, c)}} />
				<Checkbox onChange={(e, c) => {adjustBit(5, c)}} />
				<Checkbox onChange={(e, c) => {adjustBit(6, c)}} />
				<Checkbox onChange={(e, c) => {adjustBit(7, c)}} />
				
				<Box><TextField id={"binary"} disabled={true} value={bits.join('')} /></Box>
				<Box><Button variant="contained" onClick={convert}>Convert</Button></Box>
				<Box><TextField id={"decimal"} disabled={true} value={decimal} /></Box>
			</Box>
		</div>
	);
}

export default App;
