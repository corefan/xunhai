using UnityEngine;
using System.Collections;

public class Delay : MonoBehaviour {
	
	public float delayTime = 1.0f;
    protected bool _isStart = false;
	// Use this for initialization
    void Start()
    {
        _isStart = true;
        Reset();
    }

    void DelayFunc()
    {
        gameObject.SetActive(true);
        _isStart = false;
    }

    public void Reset()
    {
        if (_isStart)
        {
            _isStart = false;
        }
        else
        {
            gameObject.SetActive(false);
            Invoke("DelayFunc", delayTime);
        }
    }
	
}
