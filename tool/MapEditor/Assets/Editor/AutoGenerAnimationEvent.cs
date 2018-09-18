using UnityEngine;
using System.Collections;
using UnityEditor;
using System.Collections.Generic;
public class AutoGenerAnimationEvent
{

    //[MenuItem("Assets/生成结束动作帧事件")]
    public static void AutoGenerFinishEvent()
    {
        List<AnimationEvent> eventGroup = new List<AnimationEvent>();
        eventGroup.Add(CreateActionEvent("OnActionEnd", 0.9f));
        GenerAnimationEvent(eventGroup.ToArray());
    }
   
    /// <summary>
    /// 添加配置动作运行触发事件
    /// </summary>
    public static void AutoGenerActionData(bool useActionEvent=false)
    {
        ActionConfig.ReadCSV();
        AutoAnimationEvent(useActionEvent);
    }

    // 构造出一个运行动作事件
    private static AnimationEvent CreateActionEvent(string functionName, float time = 0.9f, int evtId = 0, string action=null)
    {
        AnimationEvent evt = new AnimationEvent();
        evt.time = time;
        evt.functionName = functionName;
        if (evtId != 0)
            evt.intParameter = evtId;
        if (action != null)
            evt.stringParameter = action;
        evt.messageOptions = SendMessageOptions.DontRequireReceiver;
        return evt;
    }

    /// <summary>
    /// 对动作进行添加运行事件， 【注意：如果动作上已经有事件，那这个动作必需手动清理掉，不然不会进行处理，避免动作运行事件重复生成！】
    /// </summary>
    private static void AutoAnimationEvent(bool useActionEvent = false)
    {
        UnityEngine.Object[] selObjs = Selection.GetFiltered(typeof(UnityEngine.Object), SelectionMode.DeepAssets);
        if (selObjs == null || selObjs.Length == 0)
        {
            Debug.LogError("请选择需要添加帧事件的动画!");
            return;
        }
        foreach (UnityEngine.Object obj in selObjs)
        {
            if (obj.GetType() != typeof(GameObject)) continue;
            GameObject fbx = (GameObject)obj;
            string fbxPath = AssetDatabase.GetAssetPath(fbx);
            UnityEngine.Object[] assets = AssetDatabase.LoadAllAssetsAtPath(fbxPath);
            foreach (UnityEngine.Object objGo in assets)
            {
                if (objGo.GetType() != typeof(AnimationClip))
                    continue;
                if (objGo.name.Contains("Take 0"))
                    continue;
                Debug.Log(objGo.name);

                string actionName = objGo.name.Trim();
                bool isLoop = false;
                bool isExistActionEvent = false;
                List<AnimationEvent> eventGroup = new List<AnimationEvent>();
                if (ActionConfig.actionCfg.ContainsKey(actionName))
                {
                    ActionData ad = ActionConfig.actionCfg[actionName];
                    isLoop = ad.isLoop;
                    if (useActionEvent)
                    {
                        if (ad.endCallback)
                            eventGroup.Add(CreateActionEvent("OnActionEnd", 0.9f, 0, objGo.name));
                        for (int i = 0; i < ad.triggerIds.Count; i++)
                        {
                            int triggerId = ad.triggerIds[i];
                            float triggerDelay = ad.triggerDelays[i];
                            eventGroup.Add(CreateActionEvent("OnActionTriggerId", triggerDelay, triggerId));
                        }
                    }
                }

                AnimationClip clipGo = (AnimationClip)objGo;
                AnimationEvent[] events = AnimationUtility.GetAnimationEvents(clipGo);
                if (events.Length != 0)
                {
                    Debug.Log("【注意】: " + fbx.name + "/" + clipGo.name + "已有帧事件(将被清除!)");
                    foreach (AnimationEvent eventGo in events)
                        Debug.Log(string.Format("functionName: {0}, time: {1}", eventGo.functionName, eventGo.time));

                    isExistActionEvent = true;
                    //continue;
                }

                ModelImporter modelImporter = AssetImporter.GetAtPath(AssetDatabase.GetAssetPath(clipGo)) as ModelImporter;
                if (modelImporter == null)
                    return;
                modelImporter.clipAnimations = modelImporter.defaultClipAnimations;

                SerializedObject serializedObject = new SerializedObject(modelImporter);
                SerializedProperty clipAnimations = serializedObject.FindProperty("m_ClipAnimations");

                Debug.Log("clipAnimations.arraySize " + clipAnimations.arraySize);

                for (int i = 0; i < clipAnimations.arraySize; i++)
                {
                    AnimationClipInfoProperties clipInfoProperties = new AnimationClipInfoProperties(clipAnimations.GetArrayElementAtIndex(i));
                    clipInfoProperties.loopTime = isLoop;
                    if (!isExistActionEvent)
                    {
                        serializedObject.ApplyModifiedProperties();
                    }
                    clipInfoProperties.SetEvents(eventGroup.ToArray());
                    serializedObject.ApplyModifiedProperties();

                    AssetDatabase.ImportAsset(AssetDatabase.GetAssetPath(clipGo));
                }
            }
        }
        AssetDatabase.Refresh();
    }





    /// <summary>
    /// 对动作进行添加运行事件， 【注意：如果动作上已经有事件，那这个动作必需手动清理掉，不然不会进行处理，避免动作运行事件重复生成！】
    /// </summary>
    /// <param name="eventGroup"></param>
    private static void GenerAnimationEvent(AnimationEvent[] eventGroup)
    {
        UnityEngine.Object[] selObjs = Selection.GetFiltered(typeof(UnityEngine.Object), SelectionMode.DeepAssets);
        if (selObjs == null || selObjs.Length == 0)
        {
            Debug.LogError("请选择需要添加帧事件的动画!");
            return;
        }
        foreach (UnityEngine.Object obj in selObjs)
        {
            if (obj.GetType() != typeof(GameObject))
                continue;
            GameObject fbx = (GameObject)obj;
            string fbxPath = AssetDatabase.GetAssetPath(fbx);
            UnityEngine.Object[] assets = AssetDatabase.LoadAllAssetsAtPath(fbxPath);
            foreach (UnityEngine.Object objGo in assets)
            {
                if (objGo.GetType() != typeof(AnimationClip))
                    continue;
                if (objGo.name.Contains("Take 0"))
                    continue;
                Debug.Log(objGo.name);
                AnimationClip clipGo = (AnimationClip)objGo;
                AnimationEvent[] events = AnimationUtility.GetAnimationEvents(clipGo);
                if (events.Length != 0)
                {
                    Debug.Log("【注意】: "+ fbx.name + "/" + clipGo.name + "已有帧事件(不会再进行添加事件，如果以下事件（名字与时间点）不是你想要的，请清除后再生成!)");
                    foreach (AnimationEvent eventGo in events)
                        Debug.Log(string.Format("functionName: {0}, time: {1}", eventGo.functionName, eventGo.time));
                    continue;
                }

                ModelImporter modelImporter = AssetImporter.GetAtPath(AssetDatabase.GetAssetPath(clipGo)) as ModelImporter;
                if (modelImporter == null)
                    return;
                modelImporter.clipAnimations = modelImporter.defaultClipAnimations;

                SerializedObject serializedObject = new SerializedObject(modelImporter);
                SerializedProperty clipAnimations = serializedObject.FindProperty("m_ClipAnimations");
                
                Debug.Log("clipAnimations.arraySize " + clipAnimations.arraySize);
                
                for (int i = 0; i < clipAnimations.arraySize; i++)
                {
                    AnimationClipInfoProperties clipInfoProperties = new AnimationClipInfoProperties(clipAnimations.GetArrayElementAtIndex(i));
                    
                    clipInfoProperties.SetEvents(eventGroup);
                    serializedObject.ApplyModifiedProperties();

                    AssetDatabase.ImportAsset(AssetDatabase.GetAssetPath(clipGo));
                }
            }
        }
        AssetDatabase.Refresh();
    }
    /*
    static void DoAddEventImportedClip(AnimationClip sourceAnimClip, AnimationClip targetAnimClip)
    {
        ModelImporter modelImporter = AssetImporter.GetAtPath(AssetDatabase.GetAssetPath(targetAnimClip)) as ModelImporter;
        if (modelImporter == null)
            return;

        SerializedObject serializedObject = new SerializedObject(modelImporter);
        SerializedProperty clipAnimations = serializedObject.FindProperty("m_ClipAnimations");

        if (!clipAnimations.isArray)
            return;

        for (int i = 0; i < clipAnimations.arraySize; i++)
        {
            AnimationClipInfoProperties clipInfoProperties = new AnimationClipInfoProperties(clipAnimations.GetArrayElementAtIndex(i));
            if (clipInfoProperties.name == targetAnimClip.name)
            {
                AnimationEvent[] sourceAnimEvents = AnimationUtility.GetAnimationEvents(sourceAnimClip);
                clipInfoProperties.SetEvents(sourceAnimEvents);
                serializedObject.ApplyModifiedProperties();
                AssetDatabase.ImportAsset(AssetDatabase.GetAssetPath(targetAnimClip));
                break;
            }
        }
    }*/
}

/// <summary>
/// 因为新版的动画系统Unity没有提供直接的API来设置动画的循环状态，所以只能通过写文件的形式来修改动画的天生属性。
/// 需要用到自己写封装的类 AnimationClipInfoProperties
/// </summary>
class AnimationClipInfoProperties
{
    SerializedProperty m_Property;

    private SerializedProperty Get(string property) { return m_Property.FindPropertyRelative(property); }

    public AnimationClipInfoProperties(SerializedProperty prop) { m_Property = prop; }

    public string name { get { return Get("name").stringValue; } set { Get("name").stringValue = value; } }
    public string takeName { get { return Get("takeName").stringValue; } set { Get("takeName").stringValue = value; } }
    public float firstFrame { get { return Get("firstFrame").floatValue; } set { Get("firstFrame").floatValue = value; } }
    public float lastFrame { get { return Get("lastFrame").floatValue; } set { Get("lastFrame").floatValue = value; } }
    public int wrapMode { get { return Get("wrapMode").intValue; } set { Get("wrapMode").intValue = value; } }
    public bool loop { get { return Get("loop").boolValue; } set { Get("loop").boolValue = value; } }

    // Mecanim animation properties
    public float orientationOffsetY { get { return Get("orientationOffsetY").floatValue; } set { Get("orientationOffsetY").floatValue = value; } }
    public float level { get { return Get("level").floatValue; } set { Get("level").floatValue = value; } }
    public float cycleOffset { get { return Get("cycleOffset").floatValue; } set { Get("cycleOffset").floatValue = value; } }
    public bool loopTime { get { return Get("loopTime").boolValue; } set { Get("loopTime").boolValue = value; } }
    public bool loopBlend { get { return Get("loopBlend").boolValue; } set { Get("loopBlend").boolValue = value; } }
    public bool loopBlendOrientation { get { return Get("loopBlendOrientation").boolValue; } set { Get("loopBlendOrientation").boolValue = value; } }
    public bool loopBlendPositionY { get { return Get("loopBlendPositionY").boolValue; } set { Get("loopBlendPositionY").boolValue = value; } }
    public bool loopBlendPositionXZ { get { return Get("loopBlendPositionXZ").boolValue; } set { Get("loopBlendPositionXZ").boolValue = value; } }
    public bool keepOriginalOrientation { get { return Get("keepOriginalOrientation").boolValue; } set { Get("keepOriginalOrientation").boolValue = value; } }
    public bool keepOriginalPositionY { get { return Get("keepOriginalPositionY").boolValue; } set { Get("keepOriginalPositionY").boolValue = value; } }
    public bool keepOriginalPositionXZ { get { return Get("keepOriginalPositionXZ").boolValue; } set { Get("keepOriginalPositionXZ").boolValue = value; } }
    public bool heightFromFeet { get { return Get("heightFromFeet").boolValue; } set { Get("heightFromFeet").boolValue = value; } }
    public bool mirror { get { return Get("mirror").boolValue; } set { Get("mirror").boolValue = value; } }

    public AnimationEvent GetEvent(int index)
    {
        AnimationEvent evt = new AnimationEvent();
        SerializedProperty events = Get("events");
        if (events != null && events.isArray)
        {
            if (index < events.arraySize)
            {
                evt.floatParameter = events.GetArrayElementAtIndex(index).FindPropertyRelative("floatParameter").floatValue;
                evt.functionName = events.GetArrayElementAtIndex(index).FindPropertyRelative("functionName").stringValue;
                evt.intParameter = events.GetArrayElementAtIndex(index).FindPropertyRelative("intParameter").intValue;
                evt.objectReferenceParameter = events.GetArrayElementAtIndex(index).FindPropertyRelative("objectReferenceParameter").objectReferenceValue;
                evt.stringParameter = events.GetArrayElementAtIndex(index).FindPropertyRelative("data").stringValue;
                evt.time = events.GetArrayElementAtIndex(index).FindPropertyRelative("time").floatValue;
            }else{
                Debug.LogWarning("Invalid Event Index");
            }
        }
        return evt;
    }

    public void SetEvent(int index, AnimationEvent animationEvent)
    {
        SerializedProperty events = Get("events");

        if (events != null && events.isArray)
        {
            if (index < events.arraySize)
            {
                events.GetArrayElementAtIndex(index).FindPropertyRelative("floatParameter").floatValue = animationEvent.floatParameter;
                events.GetArrayElementAtIndex(index).FindPropertyRelative("functionName").stringValue = animationEvent.functionName;
                events.GetArrayElementAtIndex(index).FindPropertyRelative("intParameter").intValue = animationEvent.intParameter;
                events.GetArrayElementAtIndex(index).FindPropertyRelative("objectReferenceParameter").objectReferenceValue = animationEvent.objectReferenceParameter;
                events.GetArrayElementAtIndex(index).FindPropertyRelative("data").stringValue = animationEvent.stringParameter;
                events.GetArrayElementAtIndex(index).FindPropertyRelative("time").floatValue = animationEvent.time;
            }else{
                Debug.LogWarning("Invalid Event Index");
            }
        }
    }

    public void ClearEvents()
    {
        SerializedProperty events = Get("events");

        if (events != null && events.isArray)
        {
            events.ClearArray();
        }
    }

    public int GetEventCount()
    {
        int ret = 0;
        SerializedProperty curves = Get("events");
        if (curves != null && curves.isArray)
        {
            ret = curves.arraySize;
        }
        return ret;
    }

    public void SetEvents(AnimationEvent[] newEvents)
    {
        SerializedProperty events = Get("events");
        if (events != null && events.isArray)
        {
            events.ClearArray();
            foreach (AnimationEvent evt in newEvents)
            {
                events.InsertArrayElementAtIndex(events.arraySize);
                SetEvent(events.arraySize - 1, evt);
            }
        }
    }

    public AnimationEvent[] GetEvents()
    {
        AnimationEvent[] ret = new AnimationEvent[GetEventCount()];
        SerializedProperty events = Get("events");
        if (events != null && events.isArray)
        {
            for (int i = 0; i < GetEventCount(); ++i)
            {
                ret[i] = GetEvent(i);
            }
        }
        return ret;
    }
}