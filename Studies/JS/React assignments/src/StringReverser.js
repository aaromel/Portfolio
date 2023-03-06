import React from 'react';
import './App.css';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Box from '@material-ui/core/Box';
import translations from './translations.json';

const useStyles = makeStyles(theme => ({
	Typography: {
		marginLeft: theme.spacing(5),
        marginTop: theme.spacing(2),
	},
    Box: {
		marginLeft: theme.spacing(5),
        marginTop: theme.spacing(2),
	},
}));

function App() {
	const [language, setLanguage] = React.useState("en");
    const [string, setString] = React.useState("");
    const [reversedString, setReversedString] = React.useState("");
	
    const changeLanguage = (lng) => {
        setLanguage(lng);
    }

	const updateString = (event) => {
		setString(event.target.value);
        reverseString(event.target.value);
	};

    const reverseString = (str) => {
        var splitString = str.split(""); 
        var reverseArray = splitString.reverse();
        var joinArray = reverseArray.join("");
        setReversedString(joinArray);
    }
	
	const classes = useStyles();
	
	return (
        <Box>
            <Typography variant="h6" className={classes.Typography}>{translations[language].REVERSER}</Typography>
            <Box display="flex">
                <Box className={classes.Box}>
				    <Typography variant="h6">{translations[language].INPUT}</Typography>
                    <TextField
                        value={string}
		    	        onChange={updateString}
                    />
                </Box>
                <Box className={classes.Box}>
                <Typography variant="h6">{translations[language].REVERSED}</Typography>
                <Typography>
                    {reversedString}
                </Typography>
                </Box>
			</Box>
            <Box className={classes.Box}>
            <Button variant="contained" onClick={(event) => {changeLanguage("en")}} value="en">
                {translations[language].ENGLISH}
            </Button>
            <Button variant="contained" onClick={(event) => {changeLanguage("fi")}} value="fi">
                {translations[language].FINNISH}
            </Button>
            </Box>
        </Box>
	);
}

export default App;