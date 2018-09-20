using UnityEngine;
using System.Collections;
using FairyGUI;
using DG.Tweening;

public class BaseWindow : Window {

	public GComponent panel;// 基本ui， UI 为 “frame”名的标签组件
    public new GComponent contentArea;

    private string _uiPkg;
    private string _uiPanelName;
	private bool _useEffect;

	public BaseWindow(string pkg, string panelName, bool useEffect=true)
	{
		_uiPkg = pkg;
		_uiPanelName = panelName;
		_useEffect = useEffect;
	}

	protected override void OnInit ()
	{
		//Debug.Log(_uiPkg+" "+_uiPanelName);
		this.contentPane = UIPackage.CreateObject(_uiPkg, _uiPanelName).asCom;
		panel = this.contentPane.GetChild("frame").asCom;
		contentArea = panel.GetChild("contentArea").asCom;

        this.Center();
        this.modal = true;
    }

    override protected void DoShowAnimation ()
	{
		if (_useEffect) {
			this.SetScale (0.1f, 0.1f);
			this.SetPivot (0.5f, 0.5f);
			this.TweenScale (new Vector2 (1, 1), 0.3f).SetEase (Ease.OutQuad).OnComplete (this.OnShown);
		} else {
			this.OnShown();
		}
	}

	override protected void DoHideAnimation ()
	{
		if (_useEffect) {
			this.TweenScale (new Vector2 (0f, 0f), 0.3f).SetEase (Ease.OutQuad).OnComplete (this.HideImmediately);
		} else {
			this.HideImmediately();
		}
	}

	override protected void OnShown()
	{
		
	}

	override protected void OnHide()
	{
		
	}

}
