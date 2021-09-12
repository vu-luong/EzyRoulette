using UnityEngine;

public class GameController : MonoBehaviour
{
	public RotateWheel rotateWheel;
	
	private void Awake()
	{
		SpinResponseHandler.spinResponseEvent += OnSpinResponse;
	}
	private void OnSpinResponse(int result)
	{
		rotateWheel.Activate(result);
	}

	public void Spin()
	{
		SocketRequest.GetInstance().sendSpinRequest();
	}
}
