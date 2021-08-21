using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class LoginButton : MonoBehaviour
{
    public InputField usernameInput;
    public InputField passwordInput;

    public void OnClick()
    {
        Debug.Log("Username: " + usernameInput.text);
        Debug.Log("Password: " + passwordInput.text);
        SocketProxy.GetInstance().login(usernameInput.text, passwordInput.text);
    }
}
