package com.forcewake.ShootGame;
/**
 * @author Fly Cai / Jun 13, 2017 12:00:33 AM / GuiYang Tarena
 * 子弹：是飞行物
 *
 */
public class Bullet extends FlyingObject {
	private int speed=3;
	
	/**
	 * 构造方法，子弹的初始坐标随英英雄机定
	 */
	public Bullet(int x,int y){
		image=ShootGame.bullet;//图片
		width=image.getWidth();//宽
		height=image.getHeight();//高
		this.x=x;//随英雄机
		this.y=y;//随英雄机
		
	}
	
	
	/**
	 * 重写step()
	 */
	public void step(){
		y-=speed;
	}
	
	/**
	 * 重写outOfBounds
	 */
	public boolean outOfBounds(){
		return this.y<=-this.height;//
	}
}







