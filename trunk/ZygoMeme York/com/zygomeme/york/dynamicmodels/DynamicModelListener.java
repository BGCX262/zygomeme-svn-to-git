package com.zygomeme.york.dynamicmodels;

/**
* *********************************************************************
*   This file forms part of the ZygoMeme York project - an analysis and
*   modelling platform.
*  
*   Copyright (c) 2009 ZygoMeme Ltd., email: coda@zygomeme.com
*
*   This program is free software: you can redistribute it and/or modify
*   it under the terms of the GNU General Public License as published by
*   the Free Software Foundation, either version 3 of the License, or
*   (at your option) any later version.
*
*   This program is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*   GNU General Public License for more details.
*
*   You should have received a copy of the GNU General Public License
*   along with this program.  If not, see <http://www.gnu.org/licenses/>.
* 
* **********************************************************************
* 
*
* Interface for those object that wish to listen to changes in the Dynamic 
* Model.
* 
*/
public interface DynamicModelListener {

	public void setRunTerminatedStatus(boolean terminatedStatus);
	public void setProgress(int count);
	public void setMinimum(int min);
	public void setmMaximum(int max);
		
}
