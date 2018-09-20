#region 功能说明
// 功  能： ErrorWindows.cs
// 描  述： 
// 时  间： 2016-02-29 23:43:41
// 作  者： chunzi
// 公  司： 存志工作室
// 项目名： xgame
#endregion

using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using FairyGUI;
using DG.Tweening;
public class ErrorWindow:Window {

    public GComponent panel;
    public new GComponent contentArea;
    public GList contentList = new GList();

    private GTextField txt;
	private static ErrorWindow inst;

	private string preMsg;
    private Queue<string> queue;

	public ErrorWindow()
	{
        queue = new Queue<string>(20);
	}

	protected override void OnInit ()
	{
        this.contentPane = UIPackage.CreateObject("UIRoot", "ErrorWindow").asCom;
        panel = this.contentPane.GetChild("frame").asCom;
        contentList = (GList)panel.GetChild("contentList");
        contentArea = contentList.GetChildAt(0).asCom;
        txt = contentArea.GetChild("txtContent").asTextField;
        this.Center();
        this.modal = true;
    }

	private void SetError (string msg)
	{
        txt.text = string.Format("{0}\n ---0--- \n\n{1}", msg, txt.text);
        if (txt.text.Length > 5000)
        {
            txt.text.Substring(0, 5000);
        }
    }
    static bool is_render;
    public static void ShowError (string msg)
	{
        if (inst == null) {
			inst = new ErrorWindow();
        }
        inst.queue.Enqueue(msg);
        if(!is_render)
        {
            LuaFramework.LuaHelper.GetGameManager().StartCoroutine(DoMsgShow());
            is_render = true;
        }
        
        
    }
    static string msg = null;
    static IEnumerator DoMsgShow()
    {
        if (!inst.isShowing)
            inst.Show();
        while (true)
        {
            if (inst.queue.Count != 0)
                msg = inst.queue.Dequeue();
            else
                break;
            if (inst.preMsg == msg) continue;
            inst.preMsg = msg;
            inst.SetError(msg);
            yield return null;
        }
        is_render = false;
        yield return null;
    }
    public static void Open()
    {
        if (inst == null)
            inst = new ErrorWindow();
        inst.Show();
    }

 //    override protected void DoShowAnimation()
	// {
	// 	// this.SetScale(0.1f, 0.1f);
	// 	// this.SetPivot(0.5f, 0.5f);
	// 	// this.TweenScale(new Vector2(1, 1), 0.3f).SetEase(Ease.OutQuad).OnComplete(this.OnShown);
	// }

	// override protected void DoHideAnimation()
	// {
	// 	// this.TweenScale(new Vector2(0f, 0f), 0.3f).SetEase(Ease.OutQuad).OnComplete(this.HideImmediately);
	// }

	// override protected void OnShown()
	// {
	// 	//contentPane.GetTransition("t1").Play();
	// }

	// override protected void OnHide()
	// {
	// 	//contentPane.GetTransition("t1").Stop();
	// }


}

