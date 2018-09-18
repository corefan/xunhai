using UnityEngine;
using UnityEditor;
using UnityEditorInternal;
using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Text;

public class AutoSingleCreateAnimationAssets : Editor
{
    static bool useNewAction = false;
	static string id = "error";

	static string[] actionNames = {
		"idle",		
		"appear",
		"ingame",
		"run",	
		"vertigo",	
		"defend",	
		"retreat",	
		"die",	

		"relax",
		"talk",

		"stand",
		"stand_01",
		"stand_02",
		"stand_03",

		"ride",
		"ride_01",
		"ride_02",
		"ride_03",

		"fly",
		"fly_01",
		"fly_02",
		"fly_03",

		"jump",
		"jump_01",
		"jump_02",
		"jump_03",

		"hit",	
		"hitup",	
		"hitup_01",
		"hitup_02",
		"hitup_03",
		"hitup_04",

		"attack",
		"attack_01",
		"attack_02",
		"attack_03",
		"attack_04",
		"attack_05",
		"attack_06",
		"attack_07",
		"attack_08",

		"skill",
		"skill_01",	
		"skill_02",	
		"skill_03",	
		"skill_04",	
		"skill_05",	
		"skill_06",	
		"skill_07",	
		"skill_08",	
		"skill_09",	
		"skill_10",	
		"skill_11",	
		"skill_12",	
		"skill_13",	
		"skill_14",	
		"skill_15",	
		"skill_16",	
		"skill_17",	

		"wakeup",
		"wakeup_02",		
		"warmup_01",	
		"warmup_02",
		"warmup_03",
		"warmup_04",
		"warmup_05",
		"warmup_06",
		"warmup_07",
		"warmup_08",
		"warmup_09",
		"warmup_10",
		"warmup_11",
		"warmup_12",
		"warmup_13",
		"warmup_14",
		"warmup_15",
		"warmup_16",
		"warmup_17",

		"sing",
		"sing_01",	
		"sing_02",	
		"sing_03",
		"sing_04",	
		"sing_05",	
		"sing_06",	
		"sing_07",	
		"sing_08",	
		"sing_09",	
		"sing_10",	
		"sing_11",	
		"sing_12",	
		"sing_13",	
		"sing_14",	
		"sing_15",	
		"sing_16",	
		"sing_17",	

		"storage",
		"storage_01",
		"storage_02",
		"storage_03",
		"storage_04",
		"storage_05",
		"storage_06",
		"storage_07",
		"storage_08",
		"storage_09",
		"storage_10",
		"storage_11",
		"storage_12",
		"storage_13",
		"storage_14",
		"storage_15",
		"storage_16",
		"storage_17",
	};

	[MenuItem("Assets/单个动作生成 Monster AnimatorCtrl(选中要导出的动作文件夹)*")]
	static void CreateMonster ()
	{
        if (useNewAction)
        AutoGenerAnimationEvent.AutoGenerActionData(true);
        DoCreateAnimationAssets("Monster", actionNames);
	}
	[MenuItem("Assets/单个动作生成 NPC AnimatorCtrl(选中要导出的动作文件夹)*")]
	static void CreateNPC ()
	{
        if (useNewAction)
        AutoGenerAnimationEvent.AutoGenerActionData(true);
        DoCreateAnimationAssets("NPC", actionNames);
	}
	[MenuItem("Assets/单个动作生成 Player AnimatorCtrl(选中要导出的动作文件夹)*")]
	static void CreatePlayer ()
	{
        if (useNewAction)
        AutoGenerAnimationEvent.AutoGenerActionData(true);
        DoCreateAnimationAssets("Player", actionNames);
	}

    [MenuItem("Assets/单个动作生成 Wing AnimatorCtrl(选中要导出的动作文件夹)")]
    static void CreateWing()
    {
        if (useNewAction)
        AutoGenerAnimationEvent.AutoGenerActionData();
        DoCreateAnimationAssets("Wing", actionNames);
    }
    [MenuItem("Assets/单个动作生成 Weapon AnimatorCtrl(选中要导出的动作文件夹)")]
    static void CreateWeapon()
    {
        if (useNewAction)
        AutoGenerAnimationEvent.AutoGenerActionData();
        DoCreateAnimationAssets("Weapon", actionNames);
    }

    static void DoCreateAnimationAssets(string assetRoot, string[] actionNames)
	{
		id = Selection.activeObject.name;
		//创建animationController文件，保存在Assets路径下
		string controllerPath = string.Format ("Assets/_Animator/{0}.controller", id);
		//Debugger.Log("完成："+controllerPath);
		UnityEditor.Animations.AnimatorController animatorController = UnityEditor.Animations.AnimatorController.CreateAnimatorControllerAtPath (controllerPath);
		UnityEditor.Animations.AnimatorControllerLayer layer = animatorController.layers [0];
		//把动画文件保存在创建的AnimationController中

		Vector3 entryPosition = layer.stateMachine.entryPosition;
		layer.stateMachine.entryPosition = new Vector3(entryPosition.x-226f, entryPosition.y-106f, 0);
		layer.stateMachine.exitPosition = new Vector3(entryPosition.x+216f, entryPosition.y-106f, 0);
		Vector3 anyStatePosition = layer.stateMachine.anyStatePosition;
		float OFFSET_X = 210;
		float OFFSET_Y = 50;
		float ITEM_PER_LINE = 3;
		float originX = anyStatePosition.x - OFFSET_X * (ITEM_PER_LINE / 2.5f);
		float originY = anyStatePosition.y + OFFSET_Y;
		float x = originX;
		float y = originY;

		string root = "Assets";
		for (int i = 0; i < actionNames.Length; i++) {
			string act = actionNames [i];
			string path = string.Format ("/_FBX/" + assetRoot + "/{0}/{0}@{1}.FBX", id, act);
			if (File.Exists (Application.dataPath + path)) {
				string assetPath = root + path;
				AddStateTransition (assetPath, layer, act, new Vector3 (x, y, 0));
				x += OFFSET_X;
				if (x >= originX + OFFSET_X * ITEM_PER_LINE) {
					x = originX;
					y += OFFSET_Y;
				}
			}
		}

		string fbxRoot =  string.Format ("/_FBX/{0}/{1}/{1}", assetRoot, id);
		string sourcePath = root + fbxRoot + "@mesh.FBX";
		if (!File.Exists (Application.dataPath + fbxRoot + "@mesh.FBX")) {
			sourcePath = root + fbxRoot + "@idle.FBX";
		}
		string prefabPath = root+"/Res/Prefabs/" + assetRoot + "/" + id + ".prefab";
		//Debugger.Log("完成：生成在"+prefabPath);
		GameObject go = AssetDatabase.LoadAssetAtPath(sourcePath, typeof(GameObject)) as GameObject;
		go.name = id + ".prefab";
        Animator animator = go.GetComponent<Animator>();
        if (assetRoot == "NPC" || assetRoot == "Monster")
        {
            go.layer = LayerMask.NameToLayer("Element");
        }
        else
        {
            go.layer = LayerMask.NameToLayer("Character");
        }
        if (animator == null)
            animator = go.AddComponent<Animator>();
        animator.applyRootMotion = false;
        animator.runtimeAnimatorController = animatorController;
        PrefabUtility.CreatePrefab(prefabPath, go);
        //GameObject.DestroyImmediate(go, true);
		AssetDatabase.Refresh();
    }
	
    private static void AddStateTransition(string path, UnityEditor.Animations.AnimatorControllerLayer layer, string motionName, Vector3 pos)
    {
        UnityEditor.Animations.AnimatorStateMachine sm = layer.stateMachine;
        Motion motion = AssetDatabase.LoadAssetAtPath(path, typeof(Motion)) as Motion;//根据动画文件读取它的Motion对象
		UnityEditor.Animations.AnimatorState state = sm.AddState(motionName, pos);//motion.name
        
		state.motion = motion;//取出动画名子 添加到state里面
        UnityEditor.Animations.AnimatorStateTransition trans = sm.AddAnyStateTransition(state);//把state添加在layer里面

        trans.hasExitTime = false;//把默认的时间条件删除 //false
        //trans.exitTime = 1f; //0.9f

        trans.duration = 0f; //默认过渡时间 // 0.1f
        trans.canTransitionToSelf = true;//默认true
    }
}


public class ActionConfig
{
    public static Dictionary<string, ActionData> actionCfg;
    public static void ReadCSV()
    {
        actionCfg = new Dictionary<string, ActionData>();
        FileStream fs = new FileStream(Application.dataPath + "/_FBX/ModelActionCfg.csv", FileMode.Open, FileAccess.Read, FileShare.None);
        StreamReader sr = new StreamReader(fs, System.Text.Encoding.GetEncoding(936));
        string s = Console.ReadLine();
        string line = sr.ReadLine();//去掉首行
        while (line != null)
        {
            line = sr.ReadLine();
            if (string.IsNullOrEmpty(line) || line.Trim() == "") break;
            string[] cells = line.Split(',');
            if (cells.Length < 3)
            {
                sr.Close();
                throw (new Exception(line + "   没有配置正确！"));
            }
            ActionData ad = new ActionData();
            ad.action = cells[0].Trim();
            ad.isLoop = cells[1].Trim() == "1";
            ad.endCallback = cells[2].Trim() == "1";

            if (cells.Length > 3 && !string.IsNullOrEmpty(cells[3]) && !string.IsNullOrEmpty(cells[3].Trim()))
            {
                string[] triggers = cells[3].Trim().Split('_');
                for (int i = 0; i < triggers.Length; i++)
                {
                    string trigger = triggers[i];
                    if (!string.IsNullOrEmpty(trigger))
                    {
                        string[] c = trigger.Split(':');
                        if (c.Length == 2)
                        {
                            ad.triggerIds.Add(int.Parse(c[0]));
                            ad.triggerDelays.Add(float.Parse(c[1]));
                        }
                    }
                }
            }
            actionCfg.Add(ad.action, ad);
        }
        sr.Close();
    }
}

public class ActionData
{
    public string action;
    public bool isLoop;
    public bool endCallback;

    public List<int> triggerIds = new List<int>();
    public List<float> triggerDelays = new List<float>();
}