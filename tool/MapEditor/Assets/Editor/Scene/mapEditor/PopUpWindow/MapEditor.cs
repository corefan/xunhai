using UnityEngine;
using System.Collections;
using UnityEditor;
using UnityEngine.UI;
using System.Collections.Generic;

/// <summary>
/// 地图编辑器
/// 粉刷地表，可行走，不可行走区域等等
/// 使用说明：
/// 在粉刷地图时，左键和右键都可以粉刷，，在粉刷时使用右链取消粉刷地图
/// </summary>
[ExecuteInEditMode]
public class MapEditor : EditorWindow {

    /// <summary>
    /// 编辑场景数据
    /// </summary>
	public EditSceneVo editSceneVo = new EditSceneVo();

    /// <summary>
    /// 禁止场景物件给鼠标乱拖动打乱位置
    /// </summary>
    public static bool IsLimitSceneSelectGameObject = false;

	/// <summary>
	/// 地图编辑器界面
	/// </summary>
	private EditorMapView _editorMapView;

	/// <summary>
	/// 把3D模型拖动到场景中
	/// </summary>
	private DragGameObjectHandler _dragGameObjectHandler;

	/// <summary>
	/// 地图数据导入
	/// </summary>
	private ImportGameObjectHandler _importGameObjectHandler;

    void Awake()
    {
        editSceneVo.sceneConfigVo = MapEditorFunctionSelector.sceneConfigVo;
        editSceneVo.resId = MapEditorSceneModel.Instance.mapVo.mapresid.ToString();// MapEditorFunctionSelector.sceneConfigVo.resId;//
        importGameObjectHandler.onHandleImportMap();//导入地图数据
    }
    
    //绘制窗口时调用
    void OnGUI()
    {
        editorMapView.OnGUI();
    }

    /// <summary>
    /// 窗体焦点
    /// </summary>
	void OnFocus() {
		//场景事件监听
		SceneView.onSceneGUIDelegate -= this.OnSceneGUI;
		SceneView.onSceneGUIDelegate += this.OnSceneGUI;
        
        EditorApplication.hierarchyWindowItemOnGUI -= HierarchyWindowItemCallback;
		EditorApplication.hierarchyWindowItemOnGUI += HierarchyWindowItemCallback;
    }

    /// <summary>
    /// 场景回调处理
    /// </summary>
    void OnSceneGUI(SceneView sceneView) {
        Event e = Event.current;

        // 禁止场景物件给鼠标乱拖动打乱位置
        int controlID = GUIUtility.GetControlID(FocusType.Passive);
        if (IsLimitSceneSelectGameObject && e.type == EventType.Layout) { 
            HandleUtility.AddDefaultControl(controlID);
        }

        //处理GameObject拖动到Scene
        dragGameObjectHandler.HandleDragGameObjectToScene();
	}


	/// <summary>
	/// 对象拖到Hierarchy窗口时
	/// </summary>
	private void HierarchyWindowItemCallback(int pID, Rect pRect) {
        //Debug.Log("[MapEditor]"+pID + "|" +pRect);
		dragGameObjectHandler.HandleDragGameObjectToScene();
	}
    
	/// <summary>
	/// (地图选择与编辑障碍)界面显示
	/// </summary>
	private EditorMapView editorMapView {
		get {
			if(_editorMapView == null) {
				_editorMapView = new EditorMapView();
			}
			_editorMapView.editSceneVo = editSceneVo;
			return _editorMapView;
		}
	}

	/// <summary>
	/// 拖动处理
	/// </summary>
	private DragGameObjectHandler dragGameObjectHandler {
		get {
			if (_dragGameObjectHandler == null) {
				_dragGameObjectHandler = new DragGameObjectHandler ();
			}
			return _dragGameObjectHandler;
		}
	}

	/// <summary>
	/// 导入地图3D资源
	/// </summary>
	private ImportGameObjectHandler importGameObjectHandler {
		get {
			if (_importGameObjectHandler == null) {
				_importGameObjectHandler = new ImportGameObjectHandler ();
			}
			_importGameObjectHandler.editSceneVo = editSceneVo;
			return _importGameObjectHandler;
		}
	}
	/// <summary>
	/// 销毁
	/// </summary>
	void OnDestroy() {
        Debug.Log("[MapEditor] 销毁");
		SceneView.onSceneGUIDelegate -= this.OnSceneGUI;
		EditorApplication.hierarchyWindowItemOnGUI -= HierarchyWindowItemCallback;
		MapEditorModel.Instance.rmeoveAllGameObjects ();
        MapEditorModel.objId = 0;
    }
}
