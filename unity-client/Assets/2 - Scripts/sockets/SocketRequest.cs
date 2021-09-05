using com.tvd12.ezyfoxserver.client.request;
using com.tvd12.ezyfoxserver.client.util;

public class SocketRequest : EzyLoggable
{
	private static readonly SocketRequest INSTANCE = new SocketRequest();

	public static SocketRequest GetInstance()
	{
		return INSTANCE;
	}
	public void sendAppAccessRequest()
	{
		var client = SocketProxy.GetInstance().Client;
		var request = new EzyAppAccessRequest(SocketProxy.APP_NAME);
		client.send(request);
	}
	public void sendSpinRequest()
	{
		var client = SocketProxy.GetInstance().Client;
		var app = client.getApp();
		app?.send("spin");
	}
}
