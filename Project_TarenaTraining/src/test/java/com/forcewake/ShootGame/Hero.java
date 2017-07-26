package com.forcewake.ShootGame;
import java.awt.image.BufferedImage;
/**
 * @author Fly Cai / Jun 13, 2017 12:03:57 AM / GuiYang Tarena
 * 英雄机
 *
 */
public class Hero extends FlyingObject {
	private int doubleFire;//火力值
	private int life;//命
	private BufferedImage[] images;//两张图片数组(切换)
	private int index;//协助图片切换
	
	/**
	 * 英雄机构造
	 */
	public Hero(){
		image=ShootGame.hero0;//图片
		width=image.getWidth();//宽
		height=image.getHeight();//高
		x=150;//固定值
		y=400;//固定值
		doubleFire=0;//默认为0（即单倍火力）
		life=3;//默认3条命
		images=new BufferedImage[]{ShootGame.hero0,ShootGame.hero1};//两张图片切换
		index=0;//协助切换
	}
	
	/**
	 * 重写step()
	 */
	public void step(){
		/*
		//每100毫秒切一次
		index++;
		int a=index/10;//10毫秒走一次,任何小于10的数整除10等于0
		int b=a%2;//任何模比自身大的数等于自身
		image=images[b];
		*/
		image=images[index++/10%images.length];
	}
	
	public Bullet[] shoot(){
		int xStep=this.width/4;//英雄宽分成4份
		int yStep=20;//固定值 
		if(doubleFire>0){//双倍火力
			Bullet[] bs=new Bullet[2];
			bs[0]=new Bullet(this.x+1*xStep,this.y-yStep);//在英雄机1/4宽的位置画子弹
			bs[1]=new Bullet(this.x+3*xStep,this.y-yStep);//在英雄机3/4宽的位置画子弹
			//x:英雄机的x+1/4英雄机的宽 y:英雄机的y-
			doubleFire-=2;//发射一次双倍火力，则doubleFire-=2
			return bs;
		}else{//单倍
			Bullet[] bs=new Bullet[1];
			bs[0]=new Bullet(this.x+2*xStep,this.y-yStep);//在英雄机2/4宽的位置画子弹
			return bs;
		}
	}
	
	/**
	 * 英雄机随着鼠标动:
	 * x:鼠标的x
	 * y:鼠标的y
	 */
	public void moveTo(int x, int y){
		/**
		 * 因为鼠标在英雄机的中心点
		 */
		this.x=x-this.width/2;//英雄机的x= 鼠标的x-英雄机宽/2
		this.y=y-this.height/2;//英雄机的y = 鼠标的y-英雄机高/2
	}
	
	/**
	 * 重写outOfBounds
	 */
	public boolean outOfBounds(){
		return false;//永不越界
	}
	
	/**
	 * 英雄机加命
	 */
	public void addLife(){
		life++;//命加一条
	}
	
	public int getLife(){
		return life;//返回命数
	}
	
	//英雄机减命
	public void subtractLife(){
		life--;//命数减1
	}
	
	//清空英雄机火力值
	public void clearDoubleFire(){
		doubleFire=0;//火力值归零
	}
	
	/**
	 * 英雄机加火力
	 */
	
	public void addDoubleFire(){
		doubleFire+=40;//火力值加40
	}
	/**
	 * 英雄机与敌人碰撞算法，此处this是英雄机,other是敌人
	 * @param other
	 * @return
	 */
	public boolean hit(FlyingObject other){
		/**
		 * x1是英雄机的中心点，只要中心点在英雄机与敌人的共有大框内，就算相撞
		 */
		int x1=other.x-this.width/2;//x1:敌人的x-英雄机的宽的1/2，
		int x2=other.x+other.width+this.width/2;//敌人的x2:敌人的宽1/2
		int y1=other.y-this.height/2;//敌人的y+英雄机的高1/2
		int y2=other.y+other.height+this.height/2;//敌人的y+敌人的高+英雄机的高的1/2
		int x=this.x+this.width/2;//英雄机的中心点x
		int y=this.y+this.height/2;//英雄机的中心点y
		
		return x>=x1 && x<=x2 && y>=y1 &&y<= y2;
		
		
		
	}
}






















