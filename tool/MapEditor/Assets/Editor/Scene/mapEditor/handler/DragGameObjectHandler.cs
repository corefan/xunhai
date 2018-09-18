using UnityEngine;
using System.Collections;
using UnityEditor;
using UnityEngine.UI;
using System.Collections.Generic;

public class DragGameObjectHandler
{
	/// 怪物数据绑定
	private MonsterDataBindEditor monsterDataBindEditor;

	/// 处理拖放GameObject放到Scene场景中
	public void HandleDragGameObjectToScene() {
		if (Event.current.type != EventType.DragPerform) 
			return;
		
		//拖动Prefab到Scene里面
		object[] dragRefrences = DragAndDrop.objectReferences;
		if (dragRefrences == null || dragRefrences.Length <= 0) {
			return;
		}
		GameObject currentGameObject = null;
		for (int index = 0; index < dragRefrences.Length; index++) {
			currentGameObject = dragRefrences[index] as GameObject;
			if (currentGameObject == null) {
				continue;
			}
			AddGameObjectToScene (currentGameObject);
		}
		DragAndDrop.AcceptDrag ();
		Event.current.Use();
	}

	/// 添加游戏对像到达
	public void AddGameObjectToScene(GameObject currentGameObject) {
		if (monsterDataBindEditor != null) {
			GameObject.DestroyImmediate (currentGameObject);
			return;
		}
		if (currentGameObject == null) {
			return;
		}

        ////角色由服务端配好，无需客户端拖到场景上
        //if (currentGameObject.name.ToLower().IndexOf(ElementVo.ELEMENT_TYPE_PLAYERSPAWN.ToLower()) >= 0)
        //{
        //    return;
        //}
        ////传送门由服务端配好，无需客户端拖到场景上
        //if (currentGameObject.name.ToLower().IndexOf(ElementVo.ELEMENT_TYPE_DOOR.ToLower()) >= 0)
        //{
        //    return;
        //}
        ////掉落由服务端配好，无需客户端拖到场景上
        //if (currentGameObject.name.ToLower().IndexOf(ElementVo.ELEMENT_TYPE_DROP.ToLower()) >= 0)
        //{
        //    return;
        //}
        ////npc由服务端配好，无需客户端拖到场景上
        //if (currentGameObject.name.ToLower().IndexOf(ElementVo.ELEMENT_TYPE_NPC.ToLower()) >= 0)
        //{
        //    return;
        //}

		//需要绑定怪物数据，需要让编辑者去选择
        if (currentGameObject.name.ToLower ().IndexOf (ElementVo.ELEMENT_TYPE_MONSTER.ToLower ()) >= 0) {
            MonsterDataBindEditor.bind(currentGameObject, onBindSuccessHandler, onBindFailHandler);
        }
	}

	/// 导入成功回调
	private void onBindSuccessHandler(GameObject currentGameObject, int id) {
		GameObject copyGameObject = GameObject.Instantiate (currentGameObject);
		copyGameObject.transform.position = Vector3.zero;
		MapEditorModel.Instance.addGameObject (copyGameObject, id);
	}

	private void onBindFailHandler() {
        MonoBehaviour.print("添加怪物失败！！！");
	}
}
