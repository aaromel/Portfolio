import React from 'react';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';

function App() {
	const useStyles = makeStyles(theme => ({
		Typography: {
			textAlign: 'center'
		},
	}));

    const classes = useStyles();

    const weekdays = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
    const months = ["January", "February", "March", "April", "May", "June", "July", "August", 
    "September", "October", "November", "December"];
    const day = new Intl.DateTimeFormat("uk-EN", {day: 'numeric'});
    const year = new Intl.DateTimeFormat("uk-EN", {year: 'numeric'});
    const date = new Date();

	return (
		<Box style={{zIndex: 1, position: "fixed", right: 50, bottom: 50}}>
            <Typography variant='h5' className={classes.Typography}>{weekdays[date.getDay()-1]}</Typography>
            <Typography variant='h4' className={classes.Typography}>{day.format(date)}</Typography>
            <Typography variant='h5' className={classes.Typography}>{months[date.getMonth()]}</Typography>
            <Typography className={classes.Typography}>{year.format(date)}</Typography>
		</Box>
	);
}

export default App;