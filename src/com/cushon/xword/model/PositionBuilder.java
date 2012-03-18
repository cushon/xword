package com.cushon.xword.model;

/***
 * Builder class for position objects.
 * 
 * @author cushon
 *
 */
public class PositionBuilder {
	int x = 0;
	int y = 0;
	
	/***
	 * @param x coordinate
	 * @return this 
	 */
	public PositionBuilder setX(int x) {
		this.x = x;
		return this;
	}
	
	/***
	 * @param y coordinate
	 * @return this 
	 */
	public PositionBuilder setY(int y) {
		this.y = y;
		return this;
	}
	
	/***
	 * @return the built position 
	 */
	public Position Build() {
		return new Position(x, y);
	}
}
