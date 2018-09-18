
using UnityEngine;

/// <summary>
/// 处理地图数据导入
/// </summary>
public class ImportGameObjectHandler
{
	/// <summary>
	/// 引用
	/// </summary>
	public EditSceneVo editSceneVo;

	/// <summary>
	/// 处理导入地图逻辑
	/// </summary>
	public void onHandleImportMap() {
		if (editSceneVo.isImportMap == true) {
			return;
		}
		MapImpotrEditorModel.ImportEditorModel importEditorModel = null;
       
		//导入服务端记录的场景元素
		int totalGameObjects = MapImpotrEditorModel.Instance.totalCout();
		for (int index = 0; index < totalGameObjects; index++) {
			importEditorModel = MapImpotrEditorModel.Instance.getImportEditorModel (index);
            ClientElementInfo cellVo = importEditorModel.cellVo;
            GameObject importGO = importEditorModel.gameObject;
            GameObject curGO = null;
            if (importGO == null) { //如果当前的GameObject为空，就使用正方体代替
				curGO = GameObject.CreatePrimitive (PrimitiveType.Capsule);
				curGO.name = cellVo.getSourceName();

                MeshRenderer meshRender = curGO.GetComponent<MeshRenderer>();
                Material material = new Material(Shader.Find("Diffuse"));
                if (curGO.name.Contains("Monster"))
                {
                    material.SetColor("_Color", Color.blue);
                }
                else if (curGO.name.Contains("Npc"))
                {
                    material.SetColor("_Color", Color.red);
                }
                else if (curGO.name.Contains("Door"))
                {
                    material.SetColor("_Color", Color.yellow);
                }
                meshRender.sharedMaterial = material;
            }
            else {
				curGO = GameObject.Instantiate (importGO);
				curGO.name = cellVo.getSourceName();
			}

			//计算位置
            curGO.transform.position = new Vector3((float)cellVo.location.x, (float)cellVo.location.y, (float)cellVo.location.z);

			//计算旋转角度
			if (cellVo.rotation != null) {
				Vector3 currentRotation = cellVo.rotation.toRotation();
				if (currentRotation.magnitude >= 0.01f) {
					curGO.transform.rotation = Quaternion.Euler(currentRotation);
				} else {
					if (importGO != null) {
						curGO.transform.rotation = importGO.transform.rotation;
					}
				}
			}
			MapEditorModel.Instance.addGameObject(cellVo, curGO);
		}
	}
}