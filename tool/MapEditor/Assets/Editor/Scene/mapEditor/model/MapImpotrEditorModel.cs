
using GEngine;
using UnityEngine;
using System.Collections.Generic;


/// <summary>
/// 编辑其它地图时的逻辑处理
/// </summary>
public class MapImpotrEditorModel  : Singleton<MapImpotrEditorModel>
{
    /// <summary>
    /// 模型列表
    /// </summary>
	private List<ImportEditorModel> listModels = new List<ImportEditorModel>();
    
	/// <summary>
	/// 保存数据
	/// </summary>
	public void addLoadedInfo(ClientElementInfo cellVo, GameObject gameObject) {
		ImportEditorModel importEditor = new ImportEditorModel ();
		importEditor.cellVo = cellVo;
		importEditor.gameObject = gameObject;

		listModels.Add (importEditor);
	}
    /// <summary>
    /// 获得指定索引的数据对象
    /// </summary>
	public ImportEditorModel getImportEditorModel(int index) {
		if (index >= listModels.Count) {
			return null;
		}
		return listModels[index];
	}
    /// <summary>
    /// 清除缓存
    /// </summary>
	public void clearImportModel() {
		listModels.Clear ();
	}
    /// <summary>
    /// 总数
    /// </summary>
	public int totalCout() {
		return listModels.Count;
	}
    /// <summary>
    /// 导入的编辑单元
    /// </summary>
	public class ImportEditorModel {
		public ClientElementInfo cellVo;
		public GameObject gameObject;
	}
}

