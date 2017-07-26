package com.forcewake.ShootGame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;

/**
 * @author Fly Cai / Jun 14, 2017 8:49:10 PM / GuiYang Tarena
 * 主窗口
 * 可以直接继承JPanel，效果一样，直接继承JPanel可以直接出窗口效果
 */
public class ShootGame extends JPanel {
	public static final int WIDTH=400;//窗口宽
	public static final int HEIGHT=654;//窗口高
	
	public static BufferedImage background;//背景
	public static BufferedImage start;//开始
	public static BufferedImage pause;//暂停
	public static BufferedImage gameover;//游戏结束
	public static BufferedImage airplane;//敌机
	public static BufferedImage bee;//蜜蜂
	public static BufferedImage bullet;//子弹
	public static BufferedImage hero0;//英雄机0
	public static BufferedImage hero1;//英雄机1
	
	private Hero hero=new Hero();//一个英雄机
	private FlyingObject[] flyings={};//一堆敌人
	private Bullet[] bullets={};//一堆子弹
	
		
	/**
	 * 初始化成员变量
	 */
	/**
	 *  for test...
	public ShootGame(){
		flyings=new FlyingObject[2];
		flyings[0]=new Airplane();
		flyings[1]=new Bee();
		bullets=new Bullet[1];
		bullets[0]=new Bullet(100,200);
				
	}*/
	
	
	static{//初始化静态资源（图片）
		try {
			background=ImageIO.read(ShootGame.class.getResource("background.png"));
			start=ImageIO.read(ShootGame.class.getResource("start.png"));
			pause=ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover=ImageIO.read(ShootGame.class.getResource("gameover.png"));
			airplane=ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee=ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet=ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0=ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1=ImageIO.read(ShootGame.class.getResource("hero1.png"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final int START=0;//游戏启动
	public static final int RUNNING=1;//游戏运行
	public static final int PAUSE=2;//游戏暂停
	public static final int GAME_OVER=3;//游戏结束
	private int state=START;//当前状态（默认为启动状态），不同状态所做处理不一样
	
	/*随机生成敌人*/
	public FlyingObject nextOne(){
		Random rand=new Random();
		int type=rand.nextInt(20);
		if(type<7){//0~6时，生成蜜蜂对象
			return new Bee();
		}else{//7~19时，生成敌机对象
			return new Airplane();
		}
	}
	
	int flyIndex=0;//敌人计数器
	/**
	 * 敌人（敌机+蜜蜂）入场
	 */
	public void enterAction(){
		flyIndex++;//每10毫秒增1
		if(flyIndex%40==0){//400（40*10）毫秒走一次
			FlyingObject obj=nextOne();//获取敌人对象
			flyings=Arrays.copyOf(flyings,flyings.length+1);
			flyings[flyings.length-1]=obj;//敌人数组扩容
		}
	}
	
	/**/
	public void stepAction(){
		hero.step();//英雄机走一步
		for(int i=0;i<flyings.length;i++){//遍历所有敌人
			flyings[i].step();//敌人走一步
		}
		
		for(int i=0;i<bullets.length;i++){//遍历所有子弹
			bullets[i].step();//子弹走一步
		}
	}
	
	/*子弹入场*/
	int shootIndex=0;//射击计数
	public void shootAction(){//10毫秒走一次
		shootIndex++;//每10毫秒增1
		if(shootIndex%30==0){//300毫秒走一次
			Bullet[] bs=hero.shoot();//获取子弹对象
			bullets=Arrays.copyOf(bullets,bullets.length+bs.length);//子弹数组扩容
			System.arraycopy(bs, 0,bullets,bullets.length-bs.length, bs.length);//存入元素
			//以上为数组追加:把一个数组追加到另外一个数组的后面
//			if(bs.length>1){//双
//				bullets[bullets.length-1]=bs[0];
//				bullets[bullets.length-2]=bs[1];
//			}else{//单
//				bullets[bullets.length-1]=bs[0];
//			}
		}
	}
	
	/*删除越界飞行物*/
	/*方法1：
	public void outOfBoundsAction(){//10毫秒走一次
		for(int i=0;i<flyings.length;i++){
			FlyingObject f=flyings[i];
			if(f.outOfBounds()){//如果已经越界
				//将f对象从flyings数组中删掉
				
			}
		}
	}*/
		
	//删除界飞行物
	public void outOfBoundsAction(){
		FlyingObject[] flyingLives=new FlyingObject[flyings.length];
		int index=0;//1.下标 2。不越界敌人的个数
		for(int i=0;i<flyings.length;i++){
			FlyingObject f=flyings[i];
			if(!(f.outOfBounds())){//把不越界装到数组中
				flyingLives[index]=f;
				index++;
			}
		}
		flyings=Arrays.copyOf(flyingLives, index);//仅把不越界的放到flyings中，即删除了越界的飞行物
		index=0;//index归零
		
		//删除越界子弹
		Bullet[] bulletLives=new Bullet[bullets.length];
		for(int i=0;i<bullets.length;i++){//遍历子弹
			Bullet b=bullets[i];
			if(!(b.outOfBounds())){//把不越界装到数组中
				bulletLives[index]=b;
				index++;
			}
		}
		bullets=Arrays.copyOf(bulletLives, index);//仅把不越界的放到bullets中，即删除了越界子弹
	}
	
	//碰撞,所有子弹和所有敌人撞
	/* 方法1
	public void bangAction(){//10毫秒走一次
		for(int i=0;i<bullets.length;i++){//遍历所有子弹
			Bullet b=bullets[i];//把子放入一个对象中
			for(int j=0;j<flyings.length;j++){//遍历所有敌人
				FlyingObject f=flyings[j];//把敌人入一个对象中
				if(f.shootBy(b)){//敌人被撞了
					/**
					 * 获取被撞的敌人,判断是敌机还是蜜蜂，
					 * 若是敌机，则玩家得分，
					 * 若是蜜蜂，则获取奖励类型，
					 * 再判断是命还是火力，
					 * 若是命则加命，若为火力则加火力，
					 * 删除被撞的敌人
					 *//*
				}
			}
		}
	}*/
	
	//所有子弹与所有敌人（敌机+蜜蜂+子弹）的碰撞
	public void bangAction(){//10毫秒走一次
		for(int i=0;i<bullets.length;i++){//遍历所有子弹
			Bullet b= bullets[i];//获取每个子弹传给bang()
			bang(b);//实现一个子弹与所有敌人(敌机+蜜蜂)碰撞
		}
	}
	
	
	int score=0;//玩家得分
	//一个子弹与所有敌人撞
	public void bang(Bullet b){
		int index=-1;//被撞敌人下标
		for(int i=0;i<flyings.length;i++){//遍历所有敌人
			FlyingObject f= flyings[i];
			if(f.shootBy(b)){//撞上了
				index=i;//记录被撞敌人下标
				break;//其余敌人不再参与比较
			}
		}
		if(index!=-1){//撞上了
			FlyingObject one=flyings[index];
			if(one instanceof Enemy){//若是敌人
				Enemy e= (Enemy)one;//将被撞对象转为敌人
				score+=e.getScore();//玩家得分
				System.out.println("分数："+score);//for test...
			}
			
		if(one instanceof Award){//若是奖励
			Award a=(Award)one;//转为奖励
			int type=a.getType();//得到奖励类型
			switch(type){//根据奖励类型获取不同奖励
			case Award.DOUBLE_FIRE://若奖励为火力
				hero.addDoubleFire();//英雄机加火力
				break;
			case Award.LIFE://若为生命
				hero.addLife();//英雄机加生命
				System.out.println("命数："+hero.getLife());//for test...
				break;
				}
			}
		
		//得完东西之后删除被撞对象
		FlyingObject t=flyings[index];
		flyings[index]=flyings[flyings.length-1];
		flyings[flyings.length-1]=t;
		//缩容,通过交换后，去掉最后一个元素，即被撞敌人对象被删除
		flyings=Arrays.copyOf(flyings, flyings.length-1);
		
		}
	}
	/*英雄机与敌人（敌人+蜜蜂）碰撞*/
	public void hitAction(){//10毫秒走一次
		for(int i=0;i<flyings.length;i++){
			FlyingObject f=flyings[i];//获取每个敌人
			if(hero.hit(f)){
				hero.subtractLife();//英雄机减命
				hero.clearDoubleFire();//英雄机清火力
				//把被撞敌人放到flyings最后一个元素
				FlyingObject t=flyings[i];
				flyings[i]=flyings[flyings.length-1];
				flyings[flyings.length-1]=t;
				
				//缩容（去掉最后一个元素，即被撞的敌人对象）
				flyings=Arrays.copyOf(flyings, flyings.length-1);
			}
		}
	}
	
	//检查游戏是否结束
	public void checkGameOverAction(){
		if(hero.getLife()<=0){
//			System.out.println("游戏结束！");//for test...
			state=GAME_OVER;//修改状态为GAME_OVER
		}
	}
	
	/*启动程序的执行,此处放相关业务逻辑，main方法中直接调用*/
	public void action(){
		//创建鼠标侦听器
		MouseAdapter l=new MouseAdapter(){
			//重写mouseMoved()鼠标移动事件
			public void mouseMoved(MouseEvent e){
//				System.out.println(123);//for test...
				if(state==RUNNING){//运行状态
					int x=e.getX();//获取鼠标坐标
					int y=e.getY();
					hero.moveTo(x, y);//英雄机随着鼠标动
				}
				
			}
			//重写mouseClicked()鼠标事件
			public void mouseClicked(MouseEvent e){
//				System.out.println("for test...");
				switch(state){//根据当前状态做不同的操作
				case START://启动状态
					state=RUNNING;//修改为运行状态
					break;
				case GAME_OVER://游戏结束
					//游戏结束清理现玚
					score=0;
					hero=new Hero();//旧Hero变成垃圾等待回收
					flyings=new FlyingObject[0];//旧flyings变成垃圾等待回收
					bullets=new Bullet[0];//旧bullets变成垃圾等待回收
					state=START;//修改为启动状态
					break;
				}
			}
			
			//重写mouseExited()鼠标事件
			public void mouseExited(MouseEvent e){
				if(state==RUNNING){//运行状时
					state=PAUSE;//改为暂停状态
				}
			}
			
			//重写mouseEntered()鼠标事件
			public void mouseEntered(MouseEvent e){
				if(state==PAUSE){//暂停状态
					state=RUNNING;//改为运行状态
				}
			}
		};
		
		this.addMouseListener(l);//处理鼠标操作事件
		this.addMouseMotionListener(l);//处理鼠标滑动事件
		
		Timer timer=new Timer();//创建定时器对象
		int interval=10;//时间间隔（以毫秒为单位）
		timer.schedule(new TimerTask(){
			public void run(){//定时执行的动作
//				System.out.println(111);//for test...
				if(state==RUNNING){
					enterAction();//敌人（敌机+蜜蜂）
					stepAction();//飞行物走一步
					shootAction();//英雄机发射子弹
					outOfBoundsAction();//删除越界飞行物（敌机+蜜蜂+子弹）
					bangAction();//子弹与敌人（敌机+蜜蜂）的碰撞
					hitAction();//英雄机与敌人（敌人+蜜蜂）的碰撞
					checkGameOverAction();//检查游戏是否结束
				}
				
				
				repaint();//重画（会自动调用paint())，系统方法
			}
		},interval,interval);//
	}
	
	/**
	 * 重写paint()
	 */
	public void paint(Graphics g){
//		System.out.println(111);//测试，在main方法没有显示调用，但系统会自动调，不是点调的
		g.drawImage(background,0,0,null);//画背景
		paintHero(g);//画英雄机
		paintFlyingObjects(g);//画敌人（敌机+蜜蜂）
		paintBullets(g);//画子弹
		paintScoreAndLife(g);//画分和画命
		paintState(g);//画状态
		
	}
	
	
	//画英雄机
	public void paintHero(Graphics g){
		g.drawImage(hero.image,hero.x,hero.y,null);
	}
	
	//画敌人（敌机+小蜜蜂）对象
	public void paintFlyingObjects(Graphics g){
		for(int i=0;i<flyings.length;i++){//遍历敌人
			FlyingObject f=flyings[i];//先把敌人存到f里面，简化代码
			g.drawImage(f.image,f.x,f.y,null);//画敌人（敌机或蜜蜂）
		}
	}
	
	//画子弹对象
	public void paintBullets(Graphics g){
		for(int i=0;i<bullets.length;i++){
			Bullet b=bullets[i];
			g.drawImage(b.image,b.x,b.y,null);
		}
	}
	
	//画分和命
	public void paintScoreAndLife(Graphics g){
		g.setColor(new Color(0xFF0000));
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
		g.drawString("SCORE:"+score,10,25);
		g.drawString("Life:"+hero.getLife(),10,45);
	}
	
	/**
	 * 画状态
	 * @param g
	 */
	public void paintState(Graphics g){
		switch(state){//根据不同状态画不同的图
		case START://启动游戏时的状态图
			g.drawImage(start,0,0,null);
			break;
		case PAUSE://暂停时状态图
			g.drawImage(pause,0,0,null);
			break;
		case GAME_OVER://游戏结束图
			g.drawImage(gameover,0,0,null);
			break;
		}
		
	}
	
	public static void main(String[] args) {
		JFrame frame=new JFrame("Fly");//创建画相框对象
		ShootGame game=new ShootGame();//new了一个ShootGame对象，即创建面板对象
		frame.add(game);//将面板添加到相框中
		
		frame.setSize(WIDTH, HEIGHT);//设置相框宽高，即窗口大小
		frame.setAlwaysOnTop(false);//设置窗口总在最前面
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭窗口时退出程序
		frame.setLocationRelativeTo(null);//窗口相对布局，设为null值为剧中显示
		frame.setVisible(true);//1.设置窗口可见，2）尽快调用paint()方法
		
		game.action();//启动程序的执行
	}
}




























