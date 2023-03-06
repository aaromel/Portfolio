import React, {useRef, useEffect} from 'react';
import Box from '@material-ui/core/Box';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import {pink, purple, teal} from '@material-ui/core/colors';
import { makeStyles } from '@material-ui/core/styles';

function PieChart(props) {
	const [mouseX, setMouseX] = React.useState(null);
	const [mouseY, setMouseY] = React.useState(null);
	const [colorOffset, setColorOffset] = React.useState(0);
	const [fontSize, setFontSize] = React.useState(10);
	
	const canvasRef = useRef(null);
	
	const colors = [pink[100], teal[100], purple[100], pink[300], teal[300], purple[300], pink[500], teal[500], purple[500]];
	
	const handleClick = (event) => {
		event.preventDefault();
		setMouseX(event.clientX);
		setMouseY(event.clientY);
	};
	
	const handleClose = () => {
		setMouseX(null);
		setMouseY(null);
	};
	
	const adjustFontSize = (change) => {
		console.log("adjustFontSize", fontSize, change);
		setFontSize(Math.max(5, fontSize + change));
	}
	
	const swapColors = () => {
		console.log("swap colors", colorOffset)
		setColorOffset(colorOffset + Math.floor(colors.length/2));
	}
	
	useEffect(() => {
		let ctx = canvasRef.current.getContext("2d");
		let w = ctx.canvas.width;
		let h = ctx.canvas.height;
		ctx.clearRect(0, 0, w, h);
		let total = 0;
		for (let k in props.data) {
			total += props.data[k].value;
		}
		ctx.save();
		ctx.translate(w/2, h/2);
		let radius = Math.min(w, h) * 0.4;
		ctx.textAlign = "center";
		ctx.textBaseline = "middle";
		ctx.font = fontSize + "pt sans-serif";
		let startAngle = 0;
		for (let k in props.data) {
			let angle = Math.PI*2/total*props.data[k].value;
			ctx.beginPath();
			ctx.moveTo(0, 0);
			ctx.arc(0, 0, radius, startAngle, startAngle+angle);
			ctx.fillStyle = colors[(colorOffset+k)%colors.length];
			ctx.fill();
			ctx.stroke();
			ctx.fillStyle = "black";
			startAngle += angle;
		}
		startAngle = 0;
		for (let k in props.data) {
			let angle = Math.PI*2/total*props.data[k].value;
			ctx.fillText(props.data[k].name, Math.cos(startAngle+angle/2)*radius/4*3, Math.sin(startAngle+angle/2)*radius/4*3);
			startAngle += angle;
		}
		ctx.restore();
	});
	
	return (
		<Box>
			<canvas width={300} height={300} ref={canvasRef} onContextMenu={handleClick} style={{ cursor: 'context-menu' }} />
			<Menu
				keepMounted
				open={mouseY !== null}
				onClose={handleClose}
				anchorReference="anchorPosition"
				anchorPosition={
						(mouseY !== null && mouseX !== null) ? 
						{ top: mouseY, left: mouseX } : undefined
					}
				>
				<MenuItem onClick={(evt) => {adjustFontSize(2); handleClose(evt);}}>Increase text size</MenuItem>
				<MenuItem onClick={(evt) => {adjustFontSize(-2); handleClose(evt);}}>Decrease test size</MenuItem>
				<MenuItem onClick={(evt) => {swapColors(); handleClose(evt);}}>Swap colors</MenuItem>
			</Menu>
		</Box>
	);
}

export default PieChart;