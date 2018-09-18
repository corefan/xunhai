using UnityEngine;
using UnityEditor;
using System.Collections.Generic;


/// <summary>
/// 怪物数据绑定
/// </summary>
public class MonsterDataBindEditor : EditorWindow
{
	/// <summary>
	/// 怪物数据绑定回调方法
	/// </summary>
	public delegate void OnBindSuccessHandler(GameObject currentGameObject, int mosterId);

	/// <summary>
	/// 绑定失败回调方法
	/// </summary>
	public delegate void OnBindFailHandler ();

	/// <summary>
	/// 怪物数据绑定
	/// </summary>
	private GameObject currentGameObject;

	/// <summary>
	/// 绑定成功回调方法
	/// </summary>
	private OnBindSuccessHandler onBindSuccessHandler;

	/// <summary>
	/// 绑定失败回调
	/// </summary>
	private OnBindFailHandler onBindFailHandler;

	/// <summary>
	/// 显示绑定界面
	/// </summary>
	/// <param name="currentGameObject">Current game object.</param>
	public static MonsterDataBindEditor bind(GameObject currentGameObject, OnBindSuccessHandler onBindSuccessHandler, OnBindFailHandler onBindFailHandler) {
		if (MapEditorSceneModel.Instance.mapInfos.allmonster == null) {
			return null;
		}
		Rect  wr = new Rect (0,0,310,260);
		MonsterDataBindEditor window = (MonsterDataBindEditor)EditorWindow.GetWindowWithRect (typeof (MonsterDataBindEditor),wr,true,"怪物数据绑定");
		window.currentGameObject = currentGameObject;
		window.onBindFailHandler = onBindFailHandler;
		window.onBindSuccessHandler = onBindSuccessHandler;
		window.Show();

		return window;
	}



	//绘制窗口时调用
	void OnGUI () {
		if (GUILayout.Button ("怪物列表", GUILayout.Width (300), GUILayout.Height (20))) {
			GenericMenu toolsMenu = new GenericMenu();
			createItems (toolsMenu);

			toolsMenu.DropDown(new Rect(Screen.width - 216 - 40, 0, 0, 16));
			EditorGUIUtility.ExitGUI();
		}
	}


	/// <summary>
	/// 创建下拉列表
	/// </summary>
	/// <param name="toolsMenu">Tools menu.</param>
	private void createItems(GenericMenu toolsMenu) {
        SceneObjVo serverMapMonsterVo = null;
		List<SceneObjVo> serverMapMonsterVos = MapEditorSceneModel.Instance.mapInfos.allmonster;

		if (serverMapMonsterVos == null || serverMapMonsterVos.Count <= 0) {
			return;
		}

		for (int index = 0; index < serverMapMonsterVos.Count; index++) {
			serverMapMonsterVo = serverMapMonsterVos[index];
			if (serverMapMonsterVo == null) {
				continue;
			}
			string monsterName = serverMapMonsterVo.name;
            toolsMenu.AddItem(new GUIContent(monsterName + "(id=" + serverMapMonsterVo.model + ")"), false, OnTools_OptimizeSelected, serverMapMonsterVo.model);
		}
	}


	/// <summary>
	/// 选中回调
	/// </summary>
	/// <param name="userData">User data.</param>
	void OnTools_OptimizeSelected(object userData) {
		if (onBindSuccessHandler != null) {
			onBindSuccessHandler.Invoke (currentGameObject, (int)userData);
		}
		onBindFailHandler = null;
		this.Close ();
	}

	void OnDestroy() {
		if (onBindFailHandler != null) {
			onBindFailHandler.Invoke ();
		}
		onBindFailHandler = null;
		currentGameObject = null;
		onBindSuccessHandler = null;
	}
}
