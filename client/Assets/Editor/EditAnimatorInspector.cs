using UnityEditor;
using UnityEngine;

[CustomEditor(typeof(EditAnimator))]
public class EditAnimatorInspector : Editor
{
    /// <summary>
    /// 滑动杆的当前时间
    /// </summary>
    private float m_CurTime;

    /// <summary>
    /// 是否已经烘培过
    /// </summary>
    private bool m_HasBake;

    /// <summary>
    /// 当前是否是预览播放状态
    /// </summary>
    private bool m_Playing;

    /// <summary>
    /// 当前运行时间
    /// </summary>
    private float m_RunningTime;

    /// <summary>
    /// 上一次系统时间
    /// </summary>
    private double m_PreviousTime;

    /// <summary>
    /// 总的记录时间
    /// </summary>
    private float m_RecorderStopTime;

    /// <summary>
    /// 滑动杆总长度
    /// </summary>
    private const float kDuration = 1f;

    private Animator m_Animator;

    private EditAnimator editAnimator { get { return target as EditAnimator; } }

    public string actionName = "idle";

    private Animator animator
    {
        get { return m_Animator ?? (m_Animator = editAnimator.GetComponent<Animator>()); }
    }

    void OnEnable()
    {
        m_PreviousTime = EditorApplication.timeSinceStartup;
        EditorApplication.update += inspectorUpdate;
    }

    void OnDisable()
    {
        EditorApplication.update -= inspectorUpdate;
    }

    public override void OnInspectorGUI()
    {
        //EditorGUILayout.BeginHorizontal();
        //actionName = EditorGUILayout.TextField(actionName);
        //EditorGUILayout.EndHorizontal();
        EditorGUILayout.BeginHorizontal();
        if (GUILayout.Button("Bake"))
        {
            m_HasBake = false;
            bake();
        }
        if (GUILayout.Button("Play"))
        {
            play();
        }
        if (GUILayout.Button("Stop"))
        {
            stop();
        }
        EditorGUILayout.EndHorizontal();
        m_CurTime = EditorGUILayout.Slider("Time:", m_CurTime, 0f, kDuration);
        manualUpdate();
    }

    /// <summary>
    /// 烘培记录动画数据
    /// </summary>
    private void bake()
    {
        if (m_HasBake)
        {
            return;
        }

        if (Application.isPlaying || animator == null)
        {
            return;
        }

        const float frameRate = 30f;
        const int frameCount = (int)((kDuration * frameRate) + 2);
        animator.Rebind();
        animator.StopPlayback();
        animator.recorderStartTime = 0;

        // 开始记录指定的帧数
        animator.StartRecording(frameCount);

        for (var i = 0; i < frameCount - 1; i++)
        {
            // 这里可以在指定的时间触发新的动画状态
            if (i == 200)
            {
                animator.SetTrigger(actionName);
            }

            // 记录每一帧
            animator.Update(1.0f / frameRate);
        }
        // 完成记录
        animator.StopRecording();

        // 开启回放模式
        animator.StartPlayback();
        m_HasBake = true;
        m_RecorderStopTime = animator.recorderStopTime;
    }

    /// <summary>
    /// 进行预览播放
    /// </summary>
    private void play()
    {
        if (Application.isPlaying || animator == null)
            return;
        animator.CrossFade(actionName, 0f, 0);
       
        bake();
        m_RunningTime = 0f;
        m_Playing = true;
    }

    /// <summary>
    /// 停止预览播放
    /// </summary>
    private void stop()
    {
        if (Application.isPlaying || animator == null)
        {
            return;
        }

        m_Playing = false;
        m_CurTime = 0f;
    }

    /// <summary>
    /// 预览播放状态下的更新
    /// </summary>
    private void update()
    {
        if (Application.isPlaying || animator == null)
        {
            return;
        }

        if (m_RunningTime > m_RecorderStopTime)
        {
            m_Playing = false;
            return;
        }

        // 设置回放的时间位置
        animator.playbackTime = m_RunningTime;
        animator.Update(0);
        m_CurTime = m_RunningTime;
    }

    /// <summary>
    /// 非预览播放状态下，通过滑杆来播放当前动画帧
    /// </summary>
    private void manualUpdate()
    {
        if (animator && !m_Playing && m_HasBake && m_CurTime < m_RecorderStopTime)
        {
            animator.playbackTime = m_CurTime;
            animator.Update(0);
        }
    }

    private void inspectorUpdate()
    {
        var delta = EditorApplication.timeSinceStartup - m_PreviousTime;
        m_PreviousTime = EditorApplication.timeSinceStartup;

        if (!Application.isPlaying && m_Playing)
        {
            m_RunningTime = Mathf.Clamp(m_RunningTime + (float)delta, 0f, kDuration);
            update();
        }
    }
}