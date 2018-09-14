package com.common;

import com.action.ActivityAction;
import com.action.BagAction;
import com.action.BattleAction;
import com.action.BuffAction;
import com.action.ChatAction;
import com.action.CollectAction;
import com.action.EnemyAction;
import com.action.EpigraphAction;
import com.action.EquipmentAction;
import com.action.FamilyAction;
import com.action.FashionAction;
import com.action.FriendAction;
import com.action.FurnaceAction;
import com.action.FuseAction;
import com.action.GameServerAction;
import com.action.GuildAction;
import com.action.InstanceAction;
import com.action.LoginAction;
import com.action.MailAction;
import com.action.MarketAction;
import com.action.PlayerAction;
import com.action.RankAction;
import com.action.SceneAction;
import com.action.SignAction;
import com.action.SkillAction;
import com.action.SmsAction;
import com.action.TaskAction;
import com.action.TeamAction;
import com.action.TestAction;
import com.action.TiantiAction;
import com.action.TowerAction;
import com.action.TradeAction;
import com.action.VipAction;
import com.action.WakanAction;
import com.action.WeekActivityAction;
import com.action.WingAction;

/**
 * 接口集合器
 * 
 * @author ken
 * @date 2016-12-28
 */
public class ActionCollection {

	private GameServerAction gameServerAction = new GameServerAction();
	private TestAction testAction = new TestAction();
	private LoginAction loginAction = new LoginAction();
	private SceneAction sceneAction = new SceneAction();
	private BagAction bagAction = new BagAction();
	private EquipmentAction equipmentAction = new EquipmentAction();
	private BattleAction battleAction = new BattleAction();
	private SkillAction skillAction = new SkillAction();
	private FashionAction fashionAction = new FashionAction();
	private MailAction mailAction = new MailAction();
	private FriendAction friendAction = new FriendAction();
	private TaskAction taskAction = new TaskAction();
	private ChatAction chatAction = new ChatAction();
	private WakanAction wakanAction = new WakanAction();
	private EpigraphAction epigraphAction = new EpigraphAction();
	private TeamAction teamAction = new TeamAction();
	private InstanceAction instanceAction = new InstanceAction();
	private CollectAction collectAction = new CollectAction();
	private TowerAction towerAction = new TowerAction();
	private TradeAction tradeAction = new TradeAction();
	private FuseAction fuseAction = new FuseAction();
	private TiantiAction tiantiAction = new TiantiAction();
	private FamilyAction familyAction = new FamilyAction();	
	private MarketAction marketAction = new MarketAction();	 
	private SignAction signAction = new SignAction();
	private BuffAction buffAction = new BuffAction();
	private ActivityAction activityAction = new ActivityAction();
	private RankAction rankAction = new RankAction();
	private EnemyAction enemyAction = new EnemyAction();	
	private WingAction wingAction = new WingAction();
	private WeekActivityAction weekActivityAction = new WeekActivityAction();
	private PlayerAction playerAction = new PlayerAction();
	private VipAction vipAction = new VipAction();
	private SmsAction smsAction = new SmsAction();
	private GuildAction guildAction = new GuildAction();
	private FurnaceAction furnaceAction = new FurnaceAction();
	public GameServerAction getGameServerAction() {
		return gameServerAction;
	}
	public TestAction getTestAction() {
		return testAction;
	}
	public LoginAction getLoginAction() {
		return loginAction;
	}
	public SceneAction getSceneAction() {
		return sceneAction;
	}
	public BagAction getBagAction() {
		return bagAction;
	}
	public EquipmentAction getEquipmentAction() {
		return equipmentAction;
	}
	public BattleAction getBattleAction() {
		return battleAction;
	}
	public SkillAction getSkillAction() {
		return skillAction;
	}
	public FashionAction getFashionAction() {
		return fashionAction;
	}
	public MailAction getMailAction() {
		return mailAction;
	}
	public FriendAction getFriendAction() {
		return friendAction;
	}
	public TaskAction getTaskAction() {
		return taskAction;
	}
	public ChatAction getChatAction() {
		return chatAction;
	}
	public WakanAction getWakanAction() {
		return wakanAction;
	}
	public EpigraphAction getEpigraphAction() {
		return epigraphAction;
	}
	public TeamAction getTeamAction() {
		return teamAction;
	}
	public InstanceAction getInstanceAction() {
		return instanceAction;
	}
	public CollectAction getCollectAction() {
		return collectAction;
	}
	public TowerAction getTowerAction() {
		return towerAction;
	}
	public TradeAction getTradeAction() {
		return tradeAction;
	}
	public FuseAction getFuseAction() {
		return fuseAction;
	}
	public TiantiAction getTiantiAction() {
		return tiantiAction;
	}
	public FamilyAction getFamilyAction() {
		return familyAction;
	}
	public MarketAction getMarketAction() {
		return marketAction;
	}
	public SignAction getSignAction() {
		return signAction;
	}
	public BuffAction getBuffAction() {
		return buffAction;
	}
	public ActivityAction getActivityAction() {
		return activityAction;
	}
	public RankAction getRankAction() {
		return rankAction;
	}
	public EnemyAction getEnemyAction() {
		return enemyAction;
	}
	public WingAction getWingAction() {
		return wingAction;
	}
	public WeekActivityAction getWeekActivityAction() {
		return weekActivityAction;
	}
	public PlayerAction getPlayerAction() {
		return playerAction;
	}
	public VipAction getVipAction() {
		return vipAction;
	}
	public SmsAction getSmsAction() {
		return smsAction;
	}
	public GuildAction getGuildAction() {
		return guildAction;
	}
	public FurnaceAction getFurnaceAction() {
		return furnaceAction;
	}

}
