#region 脚本说明
/*----------------------------------------------------------------
// 脚本作用：实现编辑器模式下播放预设的动画，便于特效对位
// 创建者：黑仔
//----------------------------------------------------------------*/
#endregion

using UnityEngine;
using UnityEditor;
using UnityEditor.Animations;
using System.Collections;
using System.Collections.Generic;

public class EditorAnimatorPlay : EditorWindow
{

	private List<AnimatorState> stateNames = new List<AnimatorState>();

	private int aniLieBiaoID = 0;

	private GameObject go;
	private Animator animator;
	private AnimatorController aniConS;
	private AnimatorState aniState;

	protected float clipTime = 0.0f;
	private string[] aniLieBiaoString;
	private bool playtime = false;
	protected bool lockSelection = false;
	private bool hongPei = false;
	private string aniZhuangTai = "";

	/// <summary>
	/// 时间增量
	/// </summary>
	private double delta;

	/// <summary>
	/// 当前运行时间
	/// </summary>
	private float m_RunningTime;

	/// <summary>
	/// 上一次系统时间
	/// </summary>
	private double m_PreviousTime;

	/// <summary>
	/// 最大时间长度	
	/// </summary>
	private float aniTime = 0.0f;

	private float RecordEndTime = 0;

	private bool isInspectorUpdate = false;

	private void ChuShiHua()
	{
		stateNames.Clear();
		aniLieBiaoID = 0;
		clipTime = 0.0f;
		aniLieBiaoString = null;
		playtime = false;
		lockSelection = false;
	}

	[MenuItem("HZTools/编辑模式动画播放")]
	public static void DoWindow()
	{
		GetWindow<EditorAnimatorPlay>("编辑模式动画播放");
	}

	public void OnSelectionChange()
	{
		if (!lockSelection)
		{
			go = Selection.activeGameObject;
			animator = null;
			if (go != null && go.GetComponent<Animator>() != null) 
			{
				ChuShiHua();
				animator = go.GetComponent<Animator>();
				GetAllStateName();
				LieBiaoShuaXin();
				AnimaterHongPei(aniState);

			}
			Repaint();
		}
	}

	private void GetAllStateName()
	{
		if (animator != null) 
		{
			var runAnimator = animator.runtimeAnimatorController;
			aniConS = runAnimator as AnimatorController;

			foreach (var layer in aniConS.layers)
			{
				GetAnimState(layer.stateMachine);
			}

		}
	}

	private void GetAnimState(AnimatorStateMachine aSM)
	{
		foreach (var s in aSM.states)
		{
			if (s.state.motion == null)
				continue;
			var clip = GetClip(s.state.motion.name);
			if (clip != null) 
			{
				stateNames.Add(s.state);
			}
		}

		foreach (var sms in aSM.stateMachines)
		{
			GetAnimState(sms.stateMachine);
		}
	}

	private AnimationClip GetClip(string name)
	{
		foreach (var clip in aniConS.animationClips)
		{
			if (clip.name.Equals(name))
				return clip;
		}

		return null;
	}

	public void OnGUI()
	{

		if (go == null || animator == null)
		{
			EditorGUILayout.HelpBox("请选择一个带Animator的物体！", MessageType.Info);
			return;
		}


		var oldAniID = aniLieBiaoID;
		aniLieBiaoID = EditorGUILayout.Popup("动画列表", aniLieBiaoID, aniLieBiaoString);

		aniState = stateNames[aniLieBiaoID];

		if (oldAniID != aniLieBiaoID) 
		{
			hongPei = false;
			AnimaterHongPei(aniState);
		}

		EditorGUILayout.BeginHorizontal();

		EditorGUI.BeginChangeCheck();
		if (!lockSelection) {
			aniZhuangTai = "开启动画";
		} else {
			aniZhuangTai = "关闭动画";
		}
		GUILayout.Toggle(AnimationMode.InAnimationMode(), aniZhuangTai, EditorStyles.toolbarButton);
		if (EditorGUI.EndChangeCheck())
		{
			lockSelection = !lockSelection;
			if (!lockSelection) 
			{
				GuanBi();
			}
			AnimaterHongPei(aniState);
		}
		GUILayout.FlexibleSpace();

		if (GUILayout.Button("Play"))
		{
			playtime = true;
		}
		if (GUILayout.Button("Stop"))
		{
			playtime = false;
		}
		EditorGUILayout.EndHorizontal();

		if (aniState != null)
		{
			if (!playtime) 
			{
				float startTime = 0.0f;
				float stopTime  = RecordEndTime;
				clipTime = EditorGUILayout.Slider(clipTime, startTime, stopTime);
				var zhen = clipTime * 30;
				EditorGUILayout.LabelField("帧数：" + (int)zhen);
			}
		}
		Repaint();
	}

	private void LieBiaoShuaXin()
	{
		aniLieBiaoString = new string[stateNames.Count];
		for (int i = 0; i < stateNames.Count; i++) 
		{
			aniLieBiaoString[i] = stateNames[i].name;
		}
		
	}


	/// <summary>
	/// 预览播放状态下的更新
	/// </summary>
	void update()
	{
		if (Application.isPlaying || !lockSelection)
		{
			return;
		}

		if (go == null)
			return;

		if (aniState == null)
			return;

		if (animator != null && animator.runtimeAnimatorController == null)
			return;

		if (!hongPei) 
		{
			return;
		}

		if (playtime) 
		{
			if (m_RunningTime <= aniTime) 
			{
				animator.playbackTime = m_RunningTime;
				animator.Update(0);
			}
			if (m_RunningTime >= aniTime) 
			{
				m_RunningTime = 0.0f;
			}
		} else {
			m_RunningTime = clipTime;
			animator.playbackTime = m_RunningTime;
			animator.Update(0);
		}
	}

	void GuanBi()
	{
		if (animator != null) 
		{
			m_RunningTime = 0;
			animator.playbackTime = m_RunningTime;
			animator.Update(0);
		}
	}

	void AnimaterHongPei(AnimatorState state)
	{
		if (Application.isPlaying || state == null || !lockSelection)
		{
			return;
		}

		aniTime = 0.0f;

		aniTime = GetClip(state.motion.name).length;
		
		float frameRate = 30f;
		int frameCount = (int)((aniTime * frameRate) + 2);
		animator.StopPlayback();
		animator.Play(state.name);
		animator.recorderStartTime = 0;
		
		animator.StartRecording(frameCount);
		
		for (var j = 0; j < frameCount - 1; j++)
		{
			animator.Update(1.0f / frameRate);
		}
		
		animator.StopRecording(); 
		RecordEndTime = animator.recorderStopTime;
		animator.StartPlayback();
		hongPei = true;
	}

	void OnEnable()
	{
		m_PreviousTime = EditorApplication.timeSinceStartup;
		EditorApplication.update += inspectorUpdate;
		isInspectorUpdate = true;
	}

	void OnDestroy()
	{
		EditorApplication.update -= inspectorUpdate;
		isInspectorUpdate = false;
		GuanBi();
	}

	private void inspectorUpdate()
	{
		delta = EditorApplication.timeSinceStartup - m_PreviousTime;
		m_PreviousTime = EditorApplication.timeSinceStartup;

		if (!Application.isPlaying)
		{
			m_RunningTime = m_RunningTime + (float)delta;
			update();
		}
	}
}
