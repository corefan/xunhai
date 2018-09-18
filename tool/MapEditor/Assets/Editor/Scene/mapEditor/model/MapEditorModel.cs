using GEngine;
using System.Collections.Generic;
using UnityEngine;

/// 地图编辑器数据处理
public class MapEditorModel : Singleton<MapEditorModel> {

	public static int objId = 0;
	
	private List<GameObjectCellVo> cellList = new List<GameObjectCellVo>();

	/// 添加GameObject到列表
	/// id 主要是用于区分怪物
	public void addGameObject (GameObject gameobject, int id)
	{
		if (gameobject == null)
			return;

		ClientElementInfo cellVo = new ClientElementInfo ();
		GameObjectCellVo gameObjectCellVo = new GameObjectCellVo ();
        
		GameObjectDestroyCallBack gameObjectDestroyCallBack = gameobject.GetComponent<GameObjectDestroyCallBack>();//用于监听销毁
        if(gameObjectDestroyCallBack == null)
            gameObjectDestroyCallBack = gameobject.AddComponent<GameObjectDestroyCallBack> ();
		gameObjectDestroyCallBack.destroyCallBack = removeGameObject;
		gameObjectDestroyCallBack.cellVo = cellVo;

		cellVo.setSourceName (gameobject.name);
		cellVo.model = id;
        //cellVo.id = AutoId.id++;
        gameobject.name = cellVo.getSourceName() + "|" + cellVo.model;// objId;
		//cellVo.name = gameobject.name;
		gameObjectCellVo.cellVo = cellVo;
		gameObjectCellVo.currentGameObject = gameobject;
        cellList.Add (gameObjectCellVo);

		objId += 1;
	}

	/// 添加GameObject到列表
	public void addGameObject(ClientElementInfo cellVo, GameObject gameobject) {
		if (gameobject == null)
			return;
        
        GameObjectDestroyCallBack gameObjectDestroyCallBack = gameobject.GetComponent<GameObjectDestroyCallBack>(); //用于监听销毁
        if(gameObjectDestroyCallBack == null)
            gameObjectDestroyCallBack = gameobject.AddComponent<GameObjectDestroyCallBack> ();
		gameObjectDestroyCallBack.destroyCallBack = removeGameObject;
		gameObjectDestroyCallBack.cellVo = cellVo;

		GameObjectCellVo gameObjectCellVo = new GameObjectCellVo ();
		gameObjectCellVo.cellVo = cellVo;

        gameobject.transform.position = gameobject.transform.position;
		gameobject.name = cellVo.getSourceName() + "|" + cellVo.model;

		gameObjectCellVo.currentGameObject = gameobject;
		cellList.Add (gameObjectCellVo);

		objId += 1;
	}

	/// 清理所有GameObject
	public void rmeoveAllGameObjects() {
		GameObjectCellVo gameObjectCellVo = null;

        int index = 0;
        while (cellList.Count > 0)
        {
            gameObjectCellVo = cellList[index];
            if (gameObjectCellVo != null)
                gameObjectCellVo.dispose();
        }
        
		cellList.Clear ();
	}

	/// 删除GameObject
	public void removeGameObject(ClientElementInfo gameObject) {
		GameObjectCellVo cellVo = existGameObject (gameObject);
		if (cellVo == null) {
			return;
		}
		cellList.Remove (cellVo);
	}

	/// 获得数据列表
	public List<GameObjectCellVo> getCellVos() {
		return cellList;
	}

	/// 返回某个GameObject数据
	public GameObjectCellVo existGameObject(ClientElementInfo gameObject) {
		GameObjectCellVo cell = null;
		for (int index = 0; index < cellList.Count; index++) {
			cell = cellList[index];
			if (cell.currentGameObject == null) {
				continue;
			}

			if (cell.cellVo.Equals (gameObject) == true) {
				return cell;
			}
		}
		return null;
	}
}