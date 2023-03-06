import React from 'react';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Slider from '@material-ui/core/Slider';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import PieChart from './PieChart';

const useStyles = makeStyles({
  buttons: {
    margin: '8pt',
  },
  contents: {
    margin: '8pt',
  },
  slider: {
    margin: '8pt',
  },
  textfield: {
    margin: '8pt',
  },
});

function App() {
    const [stats, setStats] = React.useState({});
    const [name, setName] = React.useState("");
    const [value, setValue] = React.useState(1);

	const classes = useStyles();
	
	const addData = (event) => {
        let objects = {};
        // For some reason the application doesn't work if the key is the name.
        // Therefore the application doesn't add items that have same values
        // as each value is invidual key. 
        let letter = value;
        objects[letter] = {value: value, name: name};
        objects[letter].value = value;
        for (let i in stats) {
            objects[i] = {value: stats[i].value, name: stats[i].name};
        }
        setStats(objects);
	}
	
    const updateName = (event) => {
		setName(event.target.value);
	}

    const valueUpdate = (event, newValue) => {
		setValue(newValue);
	}
	
	return (
		<Box>
            <PieChart data={stats}/>
            <Box style={{width: "14%"}}>
			    <Typography className={classes.contents}>Value</Typography>
                <Slider
                    value={value}
                    className={classes.slider}
			    	valueLabelDisplay="auto"
			    	step={1}
			    	min={1}
			    	max={100}
                    onChange={valueUpdate}
			    />
            </Box>
            <Typography className={classes.contents}>Name</Typography>
            <TextField
                value={name}
                onChange={updateName}
                className={classes.textfield}
			/>
			<Button 
                className={classes.buttons} 
                variant="contained" 
                color="primary"
                onClick={addData}>
				Add
			</Button>
		</Box>
	);
}

export default App;