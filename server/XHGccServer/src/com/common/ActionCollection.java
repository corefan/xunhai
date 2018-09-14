package com.common;


import com.command.AuthorityAction;
import com.command.ChatAction;
import com.command.DealDataAction;
import com.command.ExceptionAction;
import com.command.IpAction;
import com.command.ItemAction;
import com.command.LoginAction;
import com.command.MailAction;
import com.command.PlayerAction;
import com.command.RoleAction;
import com.command.ServerAction;
import com.command.SystemNoticeAction;
import com.command.UserAction;

/**
 * @author Administrator
 */

public class ActionCollection {

	private LoginAction loginAction = new LoginAction();
	private ExceptionAction exceptionAction = new ExceptionAction();
	private AuthorityAction authorityAction = new AuthorityAction();
	private UserAction userAction = new UserAction();
	private RoleAction roleAction = new RoleAction();
	private PlayerAction playerAction = new PlayerAction();
	private IpAction ipAction = new IpAction();
	private MailAction mailAction = new MailAction();
	private ItemAction itemAction = new ItemAction();
	private DealDataAction dealDataAction = new DealDataAction();
	private SystemNoticeAction systemNoticeAction = new SystemNoticeAction();
	private ChatAction chatAction = new ChatAction();
	private ServerAction serverAction = new ServerAction();

	public LoginAction getLoginAction() {
		return loginAction;
	}

	public ExceptionAction getExceptionAction() {
		return exceptionAction;
	}

	public AuthorityAction getAuthorityAction() {
		return authorityAction;
	}

	public UserAction getUserAction() {
		return userAction;
	}

	public RoleAction getRoleAction() {
		return roleAction;
	}

	public PlayerAction getPlayerAction() {
		return playerAction;
	}

	public IpAction getIpAction() {
		return ipAction;
	}

	public MailAction getMailAction() {
		return mailAction;
	}

	public ItemAction getItemAction() {
		return itemAction;
	}

	public DealDataAction getDealDataAction() {
		return dealDataAction;
	}

	public SystemNoticeAction getSystemNoticeAction() {
		return systemNoticeAction;
	}

	public ChatAction getChatAction() {
		return chatAction;
	}

	public ServerAction getServerAction() {
		return serverAction;
	}

}
