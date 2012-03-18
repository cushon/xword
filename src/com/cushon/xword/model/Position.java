package com.cushon.xword.model;

/***
 * Position represents a 2D coordinate position.
 * 
 * @author cushon
 */
public class Position {
	private int x, y;
	
	/***
	 * @param x coordinate
	 * @param y coordinate
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/***
	 * @return x coordinate
	 */
	public int getX() {
		return x;
	}
	
	/***
	 * @return y coordinate
	 */
	public int getY() {
		return y;
	}
	
	/***
	 * @return a builder class for a new position.
	 */
	public static PositionBuilder getBuilder() {
		return new PositionBuilder();
	}
}
