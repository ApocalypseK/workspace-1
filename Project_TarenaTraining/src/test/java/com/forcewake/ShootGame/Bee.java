package com.forcewake.ShootGame;

import java.util.Random;

/**
 * @author Fly Cai / Jun 12, 2017 11:51:49 PM / GuiYang Tarena
 * 蜜蜂:是飞行物
 *
 */
public class Bee extends FlyingObject implements Award {
	/**
	 * 蜜蜂斜着动
	 */
	private int xSpeed=1;//x坐标移动速度
	private int ySpeed=2;//y坐标移动速度
	private int awardType;
	
	//初始化成员变量
	public Bee(){
		Random rand=new Random();
		
		image=ShootGame.bee;//图片
		width=image.getWidth();//宽
		height=image.getHeight();//高
		
		x=rand.nextInt(ShootGame.WIDTH-this.width);
		y=-this.height;
		awardType=rand.nextInt(2);//随机得到奖励类型：不包括2，即0，1，奖励随机产生
	}
	
	/**
	 * 重写getType()
	 */
	public int getType(){
		return awardType;//返回奖励类型
	}
	
	
	/**
	 * 重写step()
	 */
	public void step(){
		x+=xSpeed;//向左或向右移动
		y+=ySpeed;//向下移动
		if(x>=ShootGame.WIDTH-this.width){
			//若x>=(窗口宽-蜜蜂宽)，则xSpeed为-1
			xSpeed=-1;
		}
		
		if(x<=0){
			//若x<=0,则xSpeed为1，加1即为向右
			xSpeed=1;
		}
	}
	
	/**
	 * 重写outOfBounds
	 */
	public boolean outOfBounds(){
		return this.y>=ShootGame.HEIGHT;//蜜蜂的y>=窗口高就算越界
	}
	
	
}


























