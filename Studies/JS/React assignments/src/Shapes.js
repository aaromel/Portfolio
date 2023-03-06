import React from 'react';
import './App.css';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import Menu from '@material-ui/core/Menu';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogTitle from '@material-ui/core/DialogTitle';
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Box from '@material-ui/core/Box';

const useStyles = makeStyles(theme => ({
	menuButton: {
		marginRight: theme.spacing(2),
	},
	title: {
		flexGrow: 1,
	},
	dialogContentArea: {
		marginLeft: theme.spacing(4),
		marginRight: theme.spacing(4),
	},
	shape: {
		margin: props => props.spacing,
		width: 100,
		height: 100,
		borderStyle: "groove",
		borderWidth: "2pt",
		borderRadius: "100%",
	}
	
}));

function App() {
	const [anchorEl, setAnchorEl] = React.useState(null);
	const [circleColor, setCircleColor] = React.useState("red");
	const [spacingInputed, setSpacingInputed] = React.useState("5");
	const [circles, setCircles] = React.useState(["red", "green"]);
	const [selected, setSelected] = React.useState(-1);
	const [spacing, setSpacing] = React.useState(5);
	const [colorDialogOpen, setColorDialogOpen] = React.useState(false);
	const [spacingDialogOpen, setSpacingDialogOpen] = React.useState(false);
	
	const menuOpen = Boolean(anchorEl);
	
	const handleMenu = event => {
		setAnchorEl(event.currentTarget);
	};
	const handleMenuClose = () => {
		setAnchorEl(null);
	};
	
	const add = (event) => {
		setCircles(circles.concat(["red"]));
		setAnchorEl(null);
	}
	const remove = (event) => {
		setCircles(circles.slice(0, selected).concat(circles.slice(selected+1)));
		setSelected(-1);
		setAnchorEl(null);
	}
	const updateColor = (event) => {
		setCircleColor(event.target.value);
	};
	const openColorDialog = (event) => {
		setCircleColor(circles[selected]);
		handleMenuClose(event);
		setColorDialogOpen(true);
	}
	const closeColorDialog = (event) => {
		setColorDialogOpen(false);
	}
	const commitColorDialog = (event) => {
		setCircles(circles.slice(0, selected).concat([circleColor]).concat(circles.slice(selected+1, circles.length)));
		setColorDialogOpen(false);
	}
	const openSpacingDialog = (event) => {
		setSpacingInputed(spacing + "");
		handleMenuClose(event);
		setSpacingDialogOpen(true);
	}
	const closeSpacingDialog = (event) => {
		setSpacingDialogOpen(false);
	}
	const commitSpacingDialog = (event) => {
		setSpacing(parseInt(spacingInputed));
		setSpacingDialogOpen(false);
	}
	const updateSpacing = (event) => {
		setSpacingInputed(event.target.value);
	};
	const select = (index) => {
		setSelected(index);
	}
	
	const styledClasses = useStyles({spacing: spacing});
	
	return (
		<div className="App">
			<AppBar position="static">
				<Toolbar>
					<div>
						<IconButton 
							edge="start" 
							className={styledClasses.menuButton} 
							color="inherit" 
							onClick={handleMenu}
						>
							<MenuIcon />
						</IconButton>
						<Menu
							id="menu-appbar"
							anchorEl={anchorEl}
							anchorOrigin={{
								vertical: 'top',
								horizontal: 'right',
							}}
							keepMounted
							transformOrigin={{
								vertical: 'top',
								horizontal: 'right',
							}}
							open={menuOpen}
							onClose={handleMenuClose}
						>
							<MenuItem onClick={add}>Add</MenuItem>
							<MenuItem onClick={remove} disabled={selected<0}>Remove</MenuItem>
							<MenuItem onClick={openColorDialog} disabled={selected<0} >Set color...</MenuItem>
							<MenuItem onClick={openSpacingDialog}>Set spacing...</MenuItem>
						</Menu>
					</div>
					<Typography variant="h6" className={styledClasses.title}>
						Shapes
					</Typography>
				</Toolbar>
			</AppBar>
			
			<Box display="flex" flexWrap="wrap">
				{
					circles.map((item, index) => 
						<div key={"shape_"+index} className={styledClasses.shape} style={{background: item, borderStyle: index===selected?"solid":"groove"}} onClick={(event) => {select(index);}}></div>
					)
					
				}
			</Box>
			
			<Dialog onClose={closeColorDialog} open={colorDialogOpen} id="colorDialog">
					<DialogTitle id="simple-dialog-title">Select shape color</DialogTitle>
					<Box className={styledClasses.dialogContentArea}>
						<FormControl component="fieldset" className={styledClasses.formControl}>
							<FormLabel component="legend">Colour</FormLabel>
							<RadioGroup name="colors" value={circleColor} onChange={updateColor}>
								<FormControlLabel value="red" control={<Radio />} label="Red" />
								<FormControlLabel value="green" control={<Radio />} label="Green" />
								<FormControlLabel value="#800080" control={<Radio />} label="Purplish" />
							</RadioGroup>
						</FormControl>
					</Box>
					<DialogActions>
						<Button variant="contained" id={"dialogCancle"} onClick={closeColorDialog}>Cancel</Button>
						<Button variant="contained" id={"dialogOK"} onClick={commitColorDialog}>OK</Button>
					</DialogActions>
			</Dialog>
			
			<Dialog onClose={closeSpacingDialog} open={spacingDialogOpen} id="spacingDialog">
				<DialogTitle id="simple-dialog-title">Set shape spacing</DialogTitle>
				<Box className={styledClasses.dialogContentArea}>
					<FormControl component="fieldset" className={styledClasses.formControl}>
						<FormLabel component="legend">Spacing</FormLabel>
						<RadioGroup name="spacing" value={spacingInputed} onChange={updateSpacing}>
							<FormControlLabel value="5" control={<Radio />} label="5" />
							<FormControlLabel value="10" control={<Radio />} label="10" />
							<FormControlLabel value="15" control={<Radio />} label="15" />
						</RadioGroup>
					</FormControl>
				</Box>
				<DialogActions>
					<Button variant="contained" id={"dialogCancle"} onClick={closeSpacingDialog}>Cancel</Button>
					<Button variant="contained" id={"dialogOK"} onClick={commitSpacingDialog}>OK</Button>
				</DialogActions>
			</Dialog>
		</div>
	);
}

export default App;
