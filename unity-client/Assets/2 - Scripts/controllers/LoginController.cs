using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class LoginController : MonoBehaviour
{
	private void Awake()
	{
		AppAccessHandler.appAccessSuccessEvent += OnAppAccessSuccess;
	}

	private void OnAppAccessSuccess()
	{
		SceneManager.LoadScene("GameScene");
	}
}
