// ----------------------------------------------------------------------------------
//
// FXMaker
// Created by ismoon - 2012 - ismoonto@gmail.com
//
// ----------------------------------------------------------------------------------

// Attribute ------------------------------------------------------------------------
// Property -------------------------------------------------------------------------
// Loop Function --------------------------------------------------------------------
// Control Function -----------------------------------------------------------------
// Event Function -------------------------------------------------------------------

using UnityEngine;
using System.Collections;

public class NcRotation : NcEffectBehaviour
{
    // Attribute ------------------------------------------------------------------------
    public bool m_bWorldSpace = false;
    public Vector3 m_vRotationValue = new Vector3(0, 360, 0);
    protected bool m_isPause = false;

    // Property -------------------------------------------------------------------------
#if UNITY_EDITOR
    public override string CheckProperty()
    {
        if (GetComponent<NcBillboard>() != null)
            return "SCRIPT_CLASH_ROTATEBILL";
        return "";	// no error
    }
#endif

    // --------------------------------------------------------------------------
    void Update()
    {
        if (!m_isPause)
            transform.Rotate(GetEngineDeltaTime() * m_vRotationValue.x, GetEngineDeltaTime() * m_vRotationValue.y, GetEngineDeltaTime() * m_vRotationValue.z, (m_bWorldSpace ? Space.World : Space.Self));
    }

    // Event Function -------------------------------------------------------------------
    public override void OnUpdateEffectSpeed(float fSpeedRate, bool bRuntime)
    {
        m_vRotationValue *= fSpeedRate;
    }

    public virtual void PauseAnimation()
    {
        m_isPause = true;
    }

    public virtual void ResumeAnimation()
    {
        m_isPause = false;
    }

    // freeze 2015/2/9 保存起始旋转以便重用
    protected Quaternion startRotation;
    protected Vector3 startVRotationValue;
    public override void RecordStartState()
    {
        base.RecordStartState();
        startRotation = transform.localRotation;
        startVRotationValue = m_vRotationValue;
    }

    public override void ResetToStartState()
    {
        base.ResetToStartState();
        transform.localRotation = startRotation;
        m_vRotationValue = startVRotationValue;
    }
}
