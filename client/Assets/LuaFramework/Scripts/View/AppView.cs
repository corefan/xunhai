using UnityEngine;
using LuaFramework;
using System.Collections.Generic;
using System.Collections;
using FairyGUI;
using System;

public class AppView : View
{
    GComponent loader;
    GComponent companyName;
    GComponent gamePrompt;
    GProgressBar progress;
    GComponent compContainer;
    GTextField loadTxt;
    GTextField loadingTips;

    GProgressBar progressBar;

    int step = 0;
    string[] tips = { 
		"请务必注意，更换武器，都会使斗神印记消失！",
		"如果想取消已放置的注灵石，点击弹出的小背包的空白处即可。",
		"注灵属性，只有在对应部位上有装备时，才能发挥作用。",
		"通过自动匹配，可以快速帮你找到志同道合的队友。",
		"每天可以在长安城贵妃那领取悬赏任务，完成后能获得不菲的收入哦。",
		"在对战地图中和其他天人对战的时候要小心哦，失败了是可能会掉落装备的。",
		"和其他天人战斗吧，在侍魂殿中获得更高的阶位，你会获得超棒的奖励。",
		"如果累了，可以和其他天人聊聊人生，生活也不只是战斗。",
		"恶人的名字是红色，他们在败落后会更容易掉落装备哦。",
		"听说对战地区的大妖们虽然强大，但是也携带着更加丰富的装备和道具呢。",
		"每天登录签到可以领取丰厚奖励！",
		"加入家族，跟家族的朋友一起组队进行副本，会更轻松哦。",
		"失败了不要紧，掌握世界大妖的特点，打造强化装备，再次挑战吧。",
		"分解装备可获得注灵石。",
		"低级物品别随便卖掉，或许可合成成高级物品。",
		"除了支线任务，每日任务和环任务也是获得经验的途径。",
		"只有穿了装备，对应槽位的注灵才会生效。",
		"同家族成员三人组队时，可获得经验加成buff。",
		"连续签到可以获得额外奖励。",
		"累计在线即能领取丰厚奖励。",
		"充值月卡，超十倍返利！是游戏开端的不错选择。",
		"每个排行榜只显示前100名，整点刷新排行榜数据哦。",
		"周活动奖励丰富，可以点击活动面板的查看周历按钮进行查看。",
    };
    
    void Awake()
    {
        compContainer = LayersMgr.Instance.GetTopLayer();
        UIPackage.AddPackage("Loader/Loader");
        if (AppConst.ShowCompanyNameWithTips == true) 
        {
            this.ShowGamePrompt();
        }
        else
        {
            this.ShowLoader();
        }
    }

    private void ShowGamePrompt()
    {
        gamePrompt = UIPackage.CreateObject("Loader", "GamePrompt").asCom;
        if (gamePrompt != null)
        {
            compContainer.AddChild(gamePrompt);
            Transition fadeIn = gamePrompt.GetTransition("fadeIn");
            if (fadeIn != null)
            {
                fadeIn.Play(PlayGamePromptFadeOut);
            }
        }
    }

    private void PlayGamePromptFadeOut()
    {
        if (gamePrompt != null)
        {
            Transition fadeOut = gamePrompt.GetTransition("fadeOut");
            if (fadeOut != null)
            {
                fadeOut.Play(HandleGamePromptFadeOut);
            }
        }
    }

    private void HandleGamePromptFadeOut()
    {
        this.HiddenGamePrompt();
        this.ShowLoader();
    }

    private void HiddenGamePrompt()
    {
        if (gamePrompt != null)
        {
            gamePrompt.visible = false;
        }
    }

    private void ShowLoader()
    {
        loader = UIPackage.CreateObject("Loader", "AssetsLoader").asLabel;
        compContainer.AddChild(loader);
        progress = (GProgressBar)loader.GetChild("loaderBar");
        progress.value = 0;
        progress.max = 100;
        progressBar = (GProgressBar)loader.GetChild("progressBar");

        loadTxt = (GTextField)progress.GetChild("txt_title");
        loadTxt.text = "正在连接网络中...";
        loadingTips = (GTextField)loader.GetChild("loadingTips");
        loader.icon = "Icons/Loader/loader";
        Timers.inst.Add(5f, 0, TimeHandle);
        TimeHandle();
        InitEvent();
        StartCoroutine(StartGameMgr());
        
        
    }
    IEnumerator StartGameMgr()
    {
        yield return 1;
        AppFacade.Instance.AddManager<GameManager>(ManagerName.Game);
        yield break;
    }

    void InitEvent()
    {
        GlobalDispatcher.GetInstance().AddEventListener(NotiConst.LOADER_ALL_COMPLETED, OnAllCompleted);
        GlobalDispatcher.GetInstance().AddEventListener(NotiConst.LOADER_COMPLETED, OnCompleted);
        GlobalDispatcher.GetInstance().AddEventListener(NotiConst.LOADER_PROGRESS, OnProgress);
    }
    // msg_progress
    private void OnProgress(EventContext context)
    {
        string[] msg = context.data.ToString().Split('|');
        if(msg.Length > 1)
        {
            progress.value = int.Parse(msg[1]);
            loadTxt.text = msg[0] +" "+ msg[1] + "%";
        }
        else
        {
            loadTxt.text = context.data.ToString();
        }
    }
    // msg[_progress]
    private void OnCompleted(EventContext context)
    {
        loadTxt.text = "正在进入游戏中...";
        progress.value = 99;
    }
    // msg
    private void OnAllCompleted()
    {
        //loadingTips.text = "完成更新!!!";
        //progress.visible = false;
        //loadingTips.visible = false;
        this.OnDestroy();
    }

    void TimeHandle(object param=null)
    {
        if(step >= tips.Length){
            step = 0;
        }
        loadingTips.text = tips[step];
        step++;
    }

    void OnDestroy()
    {
        Timers.inst.Remove(TimeHandle);
        GlobalDispatcher.GetInstance().RemoveEventListener(NotiConst.LOADER_COMPLETED, OnCompleted);
        GlobalDispatcher.GetInstance().RemoveEventListener(NotiConst.LOADER_PROGRESS, OnProgress);
        GlobalDispatcher.GetInstance().RemoveEventListener(NotiConst.LOADER_ALL_COMPLETED, OnAllCompleted);
        loader.Dispose();
    }


}

