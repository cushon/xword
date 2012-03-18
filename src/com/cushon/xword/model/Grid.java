package com.cushon.xword.model;

/***
 * Grid is a typed multidimensional array that allows elements to be set
 * and retrieved via position. Grid tracks which elements have been set.
 * 
 * @author cushon
 *
 * @param <T>
 */
public class Grid<T> {
	T[][] backingArray;
	boolean [][] isSet;
	
	int width, height;
	
	/***
	 * Constructs a grid from an existing 2D array, assuming that all
	 * null elements represent unassigned values.
	 * 
	 * @param backingArray the 2D array to use as the backing array.
	 */
	public Grid(T[][] backingArray) {
		this.backingArray = backingArray;
		
		width = backingArray.length;
		height = backingArray[0].length;
		isSet = new boolean[width][height];
		for (int y = 0; y < backingArray.length; ++y) {
			for (int x = 0; x < height; ++x) {
				isSet[x][y] = backingArray[x][y] != null;
			}
		}
	}
	
	/***
	 * @param width the width of the new grid.
	 * @param height the height of the new grid. 
	 */
	@SuppressWarnings("unchecked")	
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		backingArray = (T[][]) new Object[width][height];
		isSet = new boolean[width][height];
	}
	
	/***
	 * Assigns a value to the element at position.
	 * 
	 * @param position of the element to assign to.
	 * @param value to be assigned.
	 */
	public void Set(Position position, T value) {
		isSet[position.getX()][position.getY()] = true;
		backingArray[position.getX()][position.getY()] = value;
	}
	
	/***
	 * Assigns the same value to the elements at the specified positions.
	 * 
	 * @param positions the collection of positions to assign to.
	 * @param value the value to assign.
	 */
	public void Set(Iterable<Position> positions, T value) {
		for (Position position : positions) {
			Set(position, value);
		}
	}
	
	/***
	 * Removes the previously assigned value at position.
	 * 
	 * @param position to unset.
	 */
	public void UnSet(Position position) {
		isSet[position.getX()][position.getY()] = false;
		backingArray[position.getX()][position.getY()] = null;
	}
	
	/***
	 * Removes all previously assigned values with the given positions.
	 * 
	 * @param positions of the elements to unset
	 */
	public void UnSet(Iterable<Position> positions) {
		for (Position position : positions) {
			UnSet(position);
		}
	}
	
	/***
	 * Checks if an element is assigned.
	 * 
	 * @param position of the element to query.
	 * @return true if the element is assigned.
	 */
	public boolean IsSet(Position position) {
		return isSet[position.getX()][position.getY()];
	}
	
	/***
	 * Retrieves the value at position. 
	 * 
	 * @param position of the element to retrieve
	 * @return the element at position
	 */
	public T Get(Position position) {
		return backingArray[position.getX()][position.getY()];
	}

	/***
	 * @return width
	 */
	public int getWidth() {
		return width;
	}
	
	/***
	 * @return height
	 */
	public int getHeight() {
		return height;
	}
}
