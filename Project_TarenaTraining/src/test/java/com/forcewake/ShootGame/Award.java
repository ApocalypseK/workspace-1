package com.forcewake.ShootGame;
/**
 * @author Fly Cai / Jun 12, 2017 11:38:19 PM / GuiYang Tarena
 * 奖励
 *
 */
public interface Award {
	public int DOUBLE_FIRE=0;//火力值
	public int LIFE=1;//命
	/**
	 * 获取奖励类型，0或1
	 * @return
	 */
	public int getType();
	
}
