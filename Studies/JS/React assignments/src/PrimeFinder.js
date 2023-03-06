import React from 'react';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import TextField from '@material-ui/core/TextField';
import Paper from '@material-ui/core/Paper';

const useStyles = makeStyles({
    buttons: {
      marginLeft: '105pt',
    },
    contents: {
      margin: '8pt',
    },
    textfields: {
        margin: '8pt',
      },
  });
  
function App() {
    const [fromValue, setFromValue] = React.useState(null);
    const [toValue, setToValue] = React.useState(null);
    const [primes, setPrimes] = React.useState([]);
    

    const classes = useStyles();
    
    const findPrimes = (event) => {
        let fndr = new Worker('primesFinder.js');
        // send message to the worker
        fndr.postMessage({startFrom: 3});
        // receive results from the worker
        fndr.onmessage = function(message) {
            if (message.data.prime) {
                if (message.data.prime >= toValue) {
					fndr.postMessage({stop: true});
				}
            }
            if (message.data.primes) {
                let array = [];
                let ar = {};
                for (let r in message.data.primes) {
                    let textValue = ""+message.data.primes[r];
                    let prime = message.data.primes[r];
                    if (fromValue <= prime && prime <= toValue) {
                        array.push(message.data.primes[r]);
                    }
                }
                setPrimes(array);
            }
        }
    }

    const updateFromValue = (event) => {
        setFromValue(event.target.value);
    }

    const updateToValue = (event) => {
        setToValue(event.target.value);
    }
    
    return (
        <Box>
            <Typography className={classes.contents}>Prime numbers</Typography>
            <Box>
                <TextField
                    className={classes.textfields}
                    label="From"
                    value={fromValue}
                    onChange={updateFromValue}
			    />
            </Box>
            <Box>
                <TextField
                    className={classes.textfields}
                    label="To"
                    value={toValue}
                    onChange={updateToValue}
			    />
            </Box>
            <Button
                className={classes.buttons}
                variant="outlined" 
                disabled={fromValue === null || toValue === null} 
                color="primary" 
                onClick={findPrimes}>
                    Find
            </Button>
            <Paper style={{maxHeight: 400, width: 200, overflow: 'auto', margin: '8pt'}}>
                <List>
			    	{primes.map((prime) => 
			    		<ListItem>
			    			<ListItemText 
			    				primary={<Typography>{prime}</Typography>} 
			    			/>
			    		</ListItem>
			    	    )
			    	}
			    </List>
            </Paper>
        </Box>
    );
}

export default App;