/*
 *    This file is part of UnitTH
 *
 *   UnitTH is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   UnitTH is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with UnitTH if not, see <http://www.gnu.org/licenses/>.
 *
 * =======================================================================
 * $Id$
 * =======================================================================
 */

/*
 * Based on various examples from 
 * http://www.terrill.ca/sorting/
 */
function TableSort() {
	
	var table = new Object; // The table to be sorted
	var columnNames = new Array;
	var tableRows = new Array;
	var columnIdx = new Object();
	var columnTypeAbbr = new Object();
	var N = 0;
	
	var previouslySortedColumn = '0'; // Default sorting on Run column.
	
	function get(index){
		var ret = getCell(index).firstChild.nodeValue;
		if (null==ret) {
			ret = getCell(index).firstChild.text;
		}
		return ret; 
	}

	function getCell(index){
		return tableRows[index].cells[columnIdx];
	}
		
	/**
	 * Main entry function. Registers the methods that gets invoked when clicking
	 * the column headers.
	 */
	this.init = function(tableId) {
    	table = document.getElementById(tableId);
    	
    	if (null==table) {
        	return false;
		}
		
        // get all the column names
        columnNames = table.getElementsByTagName("th");
        
        // for each column name we register the sortOnColumn method.
        for(var i=0; i<columnNames.length; i++) {
        	columnNames[i].onclick = function() {
				sortOnColumn(this);
			}
        }
        return true;
	};
	
	/**
	 * Triggers the sorting of the rows based on the column that was clicked.
	 *  
	 * @param column The column object that was clicked on.
	 */
	function sortOnColumn(column) {
		
		columnIdx = column.cellIndex;
    	columnTypeAbbr = column.abbr; // In case there is some special comparison needed.
    	
    	if (column.innerHTML == "SPREAD") {
    		return;
    	}
    	
    	tableRows = table.tBodies[0].getElementsByTagName("tr"); // Get rows from first TBODY in the table
    	
		// Check the sorting
		if (previouslySortedColumn==columnIdx) {
			if ('0'==columnIdx) {
				column.className = (column.className != 'graphHeaderLeftAsc' ? 'graphHeaderLeftAsc' : 'graphHeaderLeftDesc' );
			} else {
				column.className = (column.className != 'graphHeaderAsc' ? 'graphHeaderAsc' : 'graphHeaderDesc' );	
			}
			reverseTable();
		} else {
			if ('0'==columnIdx) {
				column.className = (column.className != 'graphHeaderLeftAsc' ? 'graphHeaderLeftAsc' : 'graphHeaderLeftDesc' );
			} else {
				column.className = (column.className != 'graphHeaderAsc' ? 'graphHeaderAsc' : 'graphHeaderDesc' );
			}
			
			// Resetting the TD class
			if ('0'==previouslySortedColumn) {
				columnNames[previouslySortedColumn].className = 'graphHeaderLeft';
			} else {
				columnNames[previouslySortedColumn].className = 'graphHeader';
			}
			
			// Works, but there is a need to sort the columns based on type.
			N = (tableRows.length)/2;
			if ((k = Math.floor(N/5)) > 7) {
				for (var m=0; m<k; m++) {
					isort(true, m, k);
				}
			}
			if ((k = Math.floor(N/7)) > 7) {
				for (var m=0; m<k; m++) {
					isort(true, m, k);
				}
			}
			for (k=7; k>0; k-=2) {
				for (var m=0; m<k; m++) {
					isort(true, m, k);
				}
			}
		}
		previouslySortedColumn = columnIdx;
    }

	
	/**
	 * Shell sort, check Wikipedia.
	 * 
	 * @param start Starting index
	 * @param step Step size
	 */
	function isort(desc, start, step) {
		
		for(var j=start+step; j<N; j+=step) { 
			
			if (desc==true) {

				for(var i=j; i>=step; i-=step) {
					var first = get(i*2);
					var second = get(((i-step)*2));

					if ('input_text'==columnTypeAbbr) {
						
						var fIdx1 = first.indexOf('>');
						if (fIdx1<0) {
							fStripped = first;
						} else {
							var fIdx2 = first.lastIndexOf('<');
							fStripped = first.substring((fIdx1+1),(fIdx2-1));
						}
						
						var sIdx1 = second.indexOf('>');
						if (sIdx1<0) {
							sStripped = second;
						} else {
							var sIdx2 = second.lastIndexOf('<');
							sStripped = second.substring((sIdx1+1),(sIdx2-1));
						}
						
						if (fStripped>sStripped) {
							exchange(i, (i-step));
						}
					} else if ('number'==columnTypeAbbr) {
						var fa = parseFloat(first);
						var sa = parseFloat(second);
						if (isNaN(fa) ) {
							fa = 0;
						}
						if (isNaN(sa) ) {
							sa = 0;
						}
						if (fa>sa) {
							exchange(i, (i-step));
						}
					} else if ('date'==columnTypeAbbr){
						if (first>second) {
							exchange(i, (i-step));
						}
					} else if ('float'==columnTypeAbbr){
						
						// Cannot find &nbsp; so we take the next char
						var fIdx = first.indexOf('(')-1;
						var sIdx = second.indexOf('(')-1;
						
						var fStripped = ""; 
						var sStripped = ""
						
						if (fIdx<0) {
							fStripped = first;
						} else {
							fStripped = first.substring(0, fIdx);
						}
						
						if (sIdx<0) {
							sStripped = second;
						} else {
							sStripped = second.substring(0, sIdx);
						}
						
						var fa = parseFloat(fStripped);
						var sa = parseFloat(sStripped);
						
						if (isNaN(fa) ) {
							fa = 0;
						}
						if (isNaN(sa) ) {
							sa = 0;
						}
						if (fa>sa) {
							exchange(i, (i-step));
						}
					} else if ('percent'==columnTypeAbbr){
						// Cannot find &nbsp; so we take the next char
						var fIdx = first.indexOf('%');
						var sIdx = second.indexOf('%');
						var fStripped = first.substring(0, fIdx);
						var sStripped = second.substring(0, sIdx);
						
						var fa = parseFloat(fStripped);
						var sa = parseFloat(sStripped);
						
						if (isNaN(fa) ) {
							fa = 0;
						}
						if (isNaN(sa) ) {
							sa = 0;
						}
						if (fa>sa) {
							exchange(i, (i-step));
						}
					}
				}
			}
		} 
	} 

	/**
	 * Swaps two different rows.
	 * 
	 * @param i Index of the row to insert in front of row j.
	 * @param j The row to be swapped with.
	 */
	function exchange(i, j) { 
		i = i*2; // Switching only every second row.
		j = j*2; // Switching only every second row.
		if(i == j+1) { 
			table.tBodies[0].insertBefore(tableRows[i], tableRows[j]); 
		} else if(j == i+1) { 
			table.tBodies[0].insertBefore(tableRows[j], tableRows[i]); 
		} else { 
			var tmpNode = table.tBodies[0].replaceChild(tableRows[i], tableRows[j]);
			if(typeof(tableRows[i]) == "undefined") { 
				table.tBodies[0].appendChild(tmpNode); 
			} else { 
				table.tBodies[0].insertBefore(tmpNode, tableRows[i]); 
			} 
		} 
	}
	
	/**
	 * Reversing the tables rows. Simply walks through them all and replaces the top one.
	 */
	function reverseTable() {
		var topRow = tableRows[0];
		for(var i = 2; i<tableRows.length; i+=2) {
			table.tBodies[0].insertBefore(tableRows[i], topRow);
			topRow = tableRows[0];
		}
	}
}

// EOS
