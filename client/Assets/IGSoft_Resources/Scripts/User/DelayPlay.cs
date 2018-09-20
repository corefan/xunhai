/*************************
 * Delay play function
 * LiuYu. 153250945@qq.com
 ************************/
using UnityEngine;
using System.Collections;

public class DelayPlay : MonoBehaviour {
	
	public float delayTime = 1.0f;

	void Awake()
	{
		EnableSelfAll(false);
		EnableChildrenAll(false);
	}

	void Start () 
	{	
		EnableSelfAll(false);
		EnableChildrenAll(false);
		InvokePlay();
	}

	public void InvokePlay(bool loop = false, bool includeChildren = true)
    {
        EnableChildrenAll(false);
        if (loop)
		{
			if (includeChildren)
				Invoke("DelayPlayAllLoop", delayTime);
			else
				Invoke("DelayPlaySelfLoop", delayTime);
		}
		else
		{
			if (includeChildren)
				Invoke("DelayPlayAllOnce", delayTime);
			else
				Invoke("DelayPlaySelfOnce", delayTime);
		}
	}

	void DelayPlayAllLoop()
	{
		EnableSelfAll(true);
		EnableChildrenAll(true);
		PlayAll (true);
	}
	
	void DelayPlaySelfLoop()
	{
		EnableSelfAll(true);
		EnableChildrenAll(true);
		PlaySelf (true);
	}

	void DelayPlayAllOnce()
	{
		EnableSelfAll(true);
		EnableChildrenAll(true);
		PlayAll (false);
	}
	
	void DelayPlaySelfOnce()
	{
		EnableSelfAll(true);
		EnableChildrenAll(true);
		PlaySelf (false);
	}

	public void EnableSelfAll (bool enable) 
	{	
		ParticleSystem ps = GetComponent<ParticleSystem>();
		if (ps != null)
		{
			if (enable)
				ps.Play();
			else
				ps.Stop();
		}
		
		Animator amt = GetComponent<Animator>();
		if (amt != null)
		{
			amt.enabled = enable;
		}
		
		Animation an = GetComponent<Animation>();
		if (an != null)
		{
			an.enabled = enable;
		}
	}

	public void EnableChildrenAll(bool enable)
	{		
		for (int i = transform.childCount - 1; i >= 0; i--) 
		{			
			transform.GetChild(i).gameObject.SetActive(enable);			
		}
	}

	public void PlayAll (bool loop) 
	{	
		ParticleSystem[] pss = GetComponentsInChildren<ParticleSystem>(true);
		foreach(ParticleSystem ps in pss)
		{
			ps.loop = loop;
			ps.Clear(true);
			ps.Play();
		}

		Animation[] anis = GetComponentsInChildren<Animation>(true);
		foreach(Animation an in anis)
		{           
			an.wrapMode = loop? WrapMode.Loop : WrapMode.Once;
			an.Play();
		}
		
		Animator[] amts = GetComponentsInChildren<Animator>();
		foreach(Animator amt in amts)
		{           
			if (null != amt)
			{         
			    #if UNITY_5   
				AnimatorClipInfo[] infs = amt.GetCurrentAnimatorClipInfo(0);
				foreach (AnimatorClipInfo info in infs)
				{
					info.clip.wrapMode = loop? WrapMode.Loop : WrapMode.Once;
					amt.Play(info.clip.name, -1, 0);
					break;
				}
				#else
				AnimationInfo[] infs = amt.GetCurrentAnimationClipState(0);
				foreach (AnimationInfo info in infs)
				{
					info.clip.wrapMode = loop? WrapMode.Loop : WrapMode.Once;
					amt.Play(info.clip.name, -1, 0);
					break;
				}
				#endif
			}	
		}
	}

	void PlaySelf(bool loop)
	{		
		ParticleSystem ps = GetComponent<ParticleSystem>();
		if (null != ps)
		{
			ps.loop = false;
			ps.Clear(true);
			ps.time = 0f;
			ps.Play();
		}
		
		Animation anim = GetComponent<Animation>();
		if (null != anim)
		{           
			anim.wrapMode = WrapMode.Once;
			anim.Play();
		}
		
		Animator amt = GetComponent<Animator>();
		if (null != amt)
		{           		
	        #if UNITY_5  
			AnimatorClipInfo[] infs = amt.GetCurrentAnimatorClipInfo(0);
			foreach (AnimatorClipInfo info in infs)
			{
				info.clip.wrapMode = WrapMode.Once;
				amt.Play(info.clip.name, -1, 0);
				break;
			}
			#else			
			AnimationInfo[] infs = amt.GetCurrentAnimationClipState(0);
			foreach (AnimationInfo info in infs)
			{
				info.clip.wrapMode = WrapMode.Once;
				amt.Play(info.clip.name, -1, 0);
				break;
			}
			#endif
		}
	}
}
