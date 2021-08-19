using System;
using com.tvd12.ezyfoxserver.client;
using UnityEngine;

public class SocketInitializer : MonoBehaviour
{
	private static SocketInitializer instance;
	public string host = "127.0.0.1";
	public int port = 3005;
	private EzyClient client;

	private void Awake()
	{
		// When going back to previous scene, don't make duplication
		if (instance != null)
		{
			Destroy(gameObject);
		} 
		else
		{
			instance = this;
			DontDestroyOnLoad(gameObject);
		}
	}

	private void Start()
	{
		var socketProxy = SocketProxy.GetInstance();
		client = socketProxy.setup(host, port);
	}

	private void Update()
	{
		client.processEvents();
	}
}
