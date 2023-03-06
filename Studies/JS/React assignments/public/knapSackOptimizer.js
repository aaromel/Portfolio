let sackSize = 0;
let items = [];

onmessage = function(message) {
	items = message.data.items;
	sackSize = message.data.sackSize;
	let r = fillSack([], 0, 0, true);
	let result = r.map((item) => items[item]);
	postMessage({"result": result});
}

function fillSack(include, includedValue, includedWeight, updateProgress) {
	if (updateProgress)
		postMessage({"progress": 0});
	bestValue = includedValue;
	bestResult = include;
	for (let i = 0; i < items.length; i++) {
		let used = false;
		for (let j = 0; j < include.length; j++) {
			if (include[j] == i) {
				used = true;
				break;
			}
		}
		if (used) {
			continue;
		}
		if (includedWeight + items[i].weight <= sackSize) {
			ni = [...include, i];
			let candidate = fillSack(ni, includedValue + items[i].value, includedWeight + items[i].weight, false);
			let candidateValue = 0;
			for (let j = 0; j < candidate.length; j++) {
				candidateValue += items[candidate[j]].value;
			}
			if (candidateValue > bestValue) {
				bestResult = candidate;
				bestValue = candidateValue;
			}
		}
		if (updateProgress) // only on the top level of recursion
			postMessage({"progress": (i+1)/items.length});	
		}
	return bestResult;
}


