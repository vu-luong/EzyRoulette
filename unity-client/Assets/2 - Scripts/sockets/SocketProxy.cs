using System;
using com.tvd12.ezyfoxserver.client;
using com.tvd12.ezyfoxserver.client.config;
using com.tvd12.ezyfoxserver.client.constant;
using com.tvd12.ezyfoxserver.client.entity;
using com.tvd12.ezyfoxserver.client.handler;
using com.tvd12.ezyfoxserver.client.request;
using UnityEngine;

class HandshakeHandler : EzyHandshakeHandler
{
	protected override EzyRequest getLoginRequest()
	{
		return new EzyLoginRequest(
			SocketProxy.ZONE_NAME,
			SocketProxy.GetInstance().UserAuthenInfo.Username,
			SocketProxy.GetInstance().UserAuthenInfo.Password
		);
	}
}

class LoginSuccessHandler : EzyLoginSuccessHandler
{
	protected override void handleLoginSuccess(EzyData responseData)
	{
		logger.debug("Login successfully!");
		// send the appAccessRequest to server
		SocketRequest.GetInstance().sendAppAccessRequest();
	}
}

class AppAccessHandler : EzyAppAccessHandler
{
	public static event Action appAccessSuccessEvent;
	protected override void postHandle(EzyApp app, EzyArray data)
	{
		logger.debug("App access successfully!");
		// Start game logic
		appAccessSuccessEvent?.Invoke();
	}
}

#region App Data Handler

class SpinResponseHandler : EzyAbstractAppDataHandler<EzyObject>
{
	public static event Action<int> spinResponseEvent;
	protected override void process(EzyApp app, EzyObject data)
	{
		logger.debug("Receive spin response: " + data);
		var result = data.get<int>("result");
		spinResponseEvent?.Invoke(result);
	}
}

#endregion

public class SocketProxy
{
	private static readonly SocketProxy INSTANCE = new SocketProxy();

	public const string ZONE_NAME = "EzyRoulette";
	public const string PLUGIN_NAME = "EzyRoulette";
	public const string APP_NAME = "EzyRoulette";

	private string host;
	private int port;
	private EzyClient client;
	private User userAuthenInfo = new User("", "");

	public string Host => host;
	public int Port => port;
	public EzyClient Client => client;
	public User UserAuthenInfo => userAuthenInfo;

	public static SocketProxy GetInstance()
	{
		return INSTANCE;
	}

	public EzyClient setup(string host, int port)
	{
		Debug.Log("Start setting up socket client...");
		this.host = host;
		this.port = port;

		var config = EzyClientConfig.builder()
			.clientName(ZONE_NAME)
			.build();
		var clients = EzyClients.getInstance();
		client = clients.newDefaultClient(config);
		var setup = client.setup();
		
		// Add some data handlers to setup
		setup.addDataHandler(EzyCommand.HANDSHAKE, new HandshakeHandler());
		setup.addDataHandler(EzyCommand.LOGIN, new LoginSuccessHandler());
		setup.addDataHandler(EzyCommand.APP_ACCESS, new AppAccessHandler());

		var setApp = setup.setupApp(APP_NAME);
		setApp.addDataHandler("spin", new SpinResponseHandler());

		Debug.Log("Finish setting up socket client!");
		return client;
	}
	public void login(string username, string password)
	{
		userAuthenInfo.Username = username;
		userAuthenInfo.Password = password;
		client.connect(host, port);
	}
}
