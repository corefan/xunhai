using UnityEngine;
using System.Collections;
using LuaInterface;
using LuaFramework;
/// <summary>
/// 绑定lua中的场景对象，使其有碰撞互动 ,
/// 注意： lua 类必需要有 LuaToCS
/// </summary>
public class LuaBindSceneObj : MonoBehaviour
{
    //Lua表
    public LuaTable table;
    public Transform target;

    //public Animator animator;
    public static void Add(GameObject go, LuaTable tableClass)
    {
         LuaBindSceneObj cmp = go.GetComponent<LuaBindSceneObj>();
        if(cmp == null)
            cmp = go.AddComponent<LuaBindSceneObj>();
        cmp.table = tableClass;
        cmp.target = go.transform;
    }

    //获取lua组件 
    public static LuaTable Get(GameObject go, LuaTable table)
    {
        LuaBindSceneObj[] cmps = go.GetComponents<LuaBindSceneObj>();
        foreach (LuaBindSceneObj cmp in cmps){
            string mat1 = table.ToString();
            string mat2 = cmp.table.GetMetaTable().ToString();
            if (mat1 == mat2)
                return cmp.table;
        }
        return null;
    }
    public static LuaTable Get(GameObject go)
    {
        LuaBindSceneObj cmp = go.GetComponent<LuaBindSceneObj>();
        if (cmp == null) return null;
        return cmp.table;
    }
    void CallAwake ()
    {
        LuaFunction fun = table.GetLuaFunction("Awake");
        if (fun != null)
            fun.Call(table, target);
    }

    /// <summary>
    /// 动作播放完成
    /// </summary>
    public void OnActionEnd(string action)
    {
        //Animator animator = this.gameObject.GetComponent<Animator>();
        //if (animator != null)
        //{
        //    AnimatorStateInfo stateInfo = animator.GetCurrentAnimatorStateInfo(0);
        //    AnimatorClipInfo[] clipInfos = animator.GetCurrentAnimatorClipInfo(0);
        //    AnimatorClipInfo clipInfo = clipInfos[0];
        //    //获得当前层 正在播放 的剪辑数组,并且获取到的时间长度(clip.length)不受Animator速度影响;如果想获取全部原始剪辑，可以用runtimeAnimatorController.animationClip
        //    AnimationClip[] clips = animator.runtimeAnimatorController.animationClips;
        //    AnimationClip clip = clips[0];
        //    Debug.Log("clipInfos.Length:" + clipInfos.Length +
        //        "_clipInfo.clip.name:" + clipInfo.clip.name +
        //        "_clips.Length:" + clips.Length +
        //        "_clip.name:" + clip.name +
        //        "_frameRate:" + clip.frameRate + clipInfo.clip.isLooping);
        //}
        Util.CallMethod("MotionMgr", "onActionEnd", table, target, action);
    }

    /// <summary>
    /// 动作播放中触发动作执行接口
    /// </summary>
    public void OnActionTrigger(string callBack)
    {
        Util.CallMethod("MotionMgr", callBack, table, target);
    }

    // 触发器引发------- (CharacterController | RigidBody) <-> Collider.isTrigger -----------------------------------------------------------
    void OnTriggerEnter(Collider collider)
    {
        LuaFunction fun = table.GetLuaFunction("OnTriggerEnter");
        if (fun != null)
            fun.Call(table, collider, table);
    }
    void OnTriggerExit(Collider collider)
    {
        LuaFunction fun = table.GetLuaFunction("OnTriggerExit");
        if (fun != null)
            fun.Call(table, collider, table);
    }
    // 触发器引发------------------------------------------------------------------

    // -----------------CharacterController.Move 触发------------------------------
    void OnControllerColliderHit(ControllerColliderHit hit)
    {
        LuaFunction fun = table.GetLuaFunction("OnControllerColliderHit");
        if (fun != null)
        {
            if (hit.gameObject.layer != LayerMask.NameToLayer("Default") )
            {
                fun.Call(table, hit, table);
            }
        }
    }
    // -----------------CharacterController.Move 触发------------------------------

}

