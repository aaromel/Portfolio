import React from 'react';
import './App.css';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Box from '@material-ui/core/Box';
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import FormControlLabel from '@material-ui/core/FormControlLabel';

function App() {
	const [locale, setLocale] = React.useState("en-US");
    const [value, setValue] = React.useState("");
    const [rate, setRate] = React.useState(1.39);
    const [currency, setCurrency] = React.useState("USD");
	
    const useStyles = makeStyles(theme => ({
        Box: {
            marginLeft: theme.spacing(3),
            marginTop: theme.spacing(3),
        },
        Typography: {
            marginLeft: theme.spacing(3),
        },
        Textfield: {
            marginLeft: theme.spacing(3),
        },
    }));

	const changeDirection = (event) => {
		setLocale(event.target.value);
        if (locale === "en-US") {
            setRate(0.72);
            setCurrency("GBP");
        }
        else {
            setRate(1.39);
            setCurrency("USD");
        }
	};

    const updateValue = (event) => {
		setValue(event.target.value);
	};
	
	const df = new Intl.DateTimeFormat(locale, {day: 'numeric', month: 'long', year: 'numeric'});
	const tf = new Intl.DateTimeFormat(locale , {hour: 'numeric', minute: 'numeric', second: 'numeric'});
	const cf = new Intl.NumberFormat(locale, { style: 'currency', currency: currency });
				
	
	const classes = useStyles();
	
	return (
        <div>
            <Box className={classes.Box}>
                <Typography>Exchange rate at</Typography>
                <Typography>{df.format(new Date())} at {tf.format(new Date())}</Typography>
            </Box>
            <Box display="flex">
                <Typography className={classes.Typography}>is</Typography>
                <TextField
                    className={classes.Textfield}
                    value={value}
		    		onChange={updateValue}
                />
                <Typography className={classes.Typography}>=</Typography>
                <Typography className={classes.Typography}>{cf.format(value*rate)}</Typography>
            </Box>
            <Box className={classes.Box}>
                <Typography>Direction:</Typography>
                <FormControl>
		        <RadioGroup name="fontColour" value={locale} onChange={changeDirection} position="static">
		        	<FormControlLabel value="en-US" control={<Radio />} label="£ to $" />
		        	<FormControlLabel value="en-GB" control={<Radio />} label="$ to £" />
		        </RadioGroup>
	            </FormControl>
            </Box>
        </div>
	);
}

export default App;