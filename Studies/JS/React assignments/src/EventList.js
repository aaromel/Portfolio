import React from 'react';
import './App.css';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Box from '@material-ui/core/Box';
import Drawer from '@material-ui/core/Drawer';
import Switch from '@material-ui/core/Switch';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TableSortLabel from '@material-ui/core/TableSortLabel';
import Paper from '@material-ui/core/Paper';

function App() {
	const [items, setItems] = React.useState([{"name": "event 1", "startTime": new Date(), "private": false}]);
	const [selected, setSelected] = React.useState(-1);
	const [drawerOpen, setDrawerOpen] = React.useState(false);
	const [edited, setEdited] = React.useState([new Date(), "", false]);
	const [sortCol, setSortCol] = React.useState(0);
	const [sortAsc, setSortAsc] = React.useState(false);
	
	const dtf = new Intl.DateTimeFormat("uk-EN", {year: 'numeric', month: 'numeric', day: 'numeric'}) 
	
	const switchDrawer = (event) => {
		setDrawerOpen(!drawerOpen);
	}
	const openDrawer = (event) => {
		setDrawerOpen(true);
	}
	
	const add = (event) => {
		setDrawerOpen(false);
		setItems(items.concat({"name": edited[1], "startTime": edited[0], "private": edited[2]}));
	}
	const remove = (event) => {
		setItems(items.slice(0, selected).concat(items.slice(selected+1)));
	}
	const updateSorting = (event, column) => {
		if (column === sortCol) {
			setSortAsc(!sortAsc);
		}
		setSortCol(column);
	}
	
	const sortedItems = items.sort((a, b) => {
		let dirMult = (sortAsc?1:-1);
		if (sortCol === 0)
			return (a.startTime.getTime()-b.startTime.getTime())*dirMult;
		if (sortCol === 1)
			return a.name.localeCompare(b.name)*dirMult;
		if (sortCol === 2)
			return ((a.private === b.private)?0:(a.private?1:-1))*dirMult;
		return 0;
	});
	
	return (
		<Box>
			<Drawer open={drawerOpen} onClose={switchDrawer} variant="temporary">
				<TextField
					label="event name"
					value={edited[1]}
					onChange={(event) => {setEdited([edited[0], event.target.value, edited[2]])}}
				/>
				<Switch 
					checked={edited[2]} 
					onChange={(event, checked) => {setEdited( [edited[0], edited[1], checked])}}
				/>
				<TextField
					type="date"
					onChange={(event, checked) => {setEdited( [new Date(event.target.value), edited[1], edited[2]])}}
				></TextField>
				<Button onClick={add}>Add</Button>
			</Drawer>
			<TableContainer component={Paper}>
				<Table>
					<TableHead>
						<TableRow>
							<TableCell>
								<TableSortLabel
									active={sortCol === 0}
									direction={sortAsc?'asc':'desc'}
									onClick={(event) => {updateSorting(event, 0)}}
								></TableSortLabel>
								Date
							</TableCell>
							<TableCell>
								<TableSortLabel
									active={sortCol === 1}
									direction={sortAsc?'asc':'desc'}
									onClick={(event) => {updateSorting(event, 1)}}
								></TableSortLabel>
								Name
							</TableCell>
							<TableCell>
								<TableSortLabel
									active={sortCol === 2}
									direction={sortAsc?'asc':'desc'}
									onClick={(event) => {updateSorting(event, 2)}}
								></TableSortLabel>
								Private
							</TableCell>
						</TableRow>
					</TableHead>
					<TableBody>
						{sortedItems.map((row, index) => (
							<TableRow hover key={row.name + "_" + index} onClick={(event) => {setSelected(index);}}>
								<TableCell component="th" scope="row">
									{dtf.format(row.startTime)} 
								</TableCell>
								<TableCell component="th" scope="row">
									{row.name} 
								</TableCell>
								<TableCell>
									{row.private?"Private":"-"}
								</TableCell>
							</TableRow>
						))}
					</TableBody>
				</Table>
			</TableContainer>
			<Button onClick={openDrawer}>Add</Button>
			<Button onClick={remove} disabled={selected<0}>Remove</Button>
		</Box>
	);
}

export default App;
