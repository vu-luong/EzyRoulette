using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameController : MonoBehaviour
{
	public void Spin()
	{
		SocketRequest.GetInstance().sendSpinRequest();
	}
}
