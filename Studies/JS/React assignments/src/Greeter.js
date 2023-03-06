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
import TextField from '@material-ui/core/TextField';

const useStyles = makeStyles(theme => ({
	menuButton: {
		marginRight: theme.spacing(2),
	},
	title: {
		flexGrow: 1,
	},
	editDialog: {
		marginLeft: theme.spacing(4),
		marginRight: theme.spacing(4),
	}
}));

function App() {
	const [anchorEl, setAnchorEl] = React.useState(null);
	const [name, setName] = React.useState("N.N.");
	const [dialogOpen, setDialogOpen] = React.useState(false);
	
	const menuOpen = Boolean(anchorEl);
	
	const handleMenu = event => {
		setAnchorEl(event.currentTarget);
	};
	const handleMenuClose = () => {
		setAnchorEl(null);
	};
	
	const updateName = (event) => {
		setName(event.target.value);
	};
	const openDialog = (event) => {
		handleMenuClose(event);
		setDialogOpen(true);
	}
	const closeDialog = (event) => {
		setDialogOpen(false);
	}
	
	const styledClasses = useStyles();
	
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
							<MenuItem onClick={openDialog}>Edit name...</MenuItem>
						</Menu>
					</div>
					<Typography variant="h6" className={styledClasses.title}>
						Greeter
					</Typography>
				</Toolbar>
			</AppBar>
			
			<Typography variant="h1">
				Greetings {name}!
			</Typography>
			
			<Dialog onClose={closeDialog} open={dialogOpen}>
				<DialogTitle id="simple-dialog-title">Update text</DialogTitle>
				<TextField id={"name"} className={styledClasses.editDialog} value={name} onChange={updateName} />
				<DialogActions>
					<Button variant="contained" id={"dialogOK"} onClick={closeDialog}>OK</Button>
				</DialogActions>
			</Dialog>
		</div>
	);
}

export default App;
