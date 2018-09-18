using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using LitJson;
using System;
using System.Reflection;

namespace GEngine{
	/// <summary>
	/// 把对应的json数据转化成相应的对象
	/// </summary>
	public class JsonParser {
		public delegate object HandleBaseType (object result);

		/// <summary>
		/// 基础类型处理器
		/// </summary>
		private static Dictionary<string, HandleBaseType> _handles;


		/// <summary>
		/// 把标准json字符串数据转化成对象
		/// </summary>
		/// <param name="jsonText">Json text.</param>
		/// <param name="type">Type.</param>
		public static object parser(string jsonText, Type type) {
			//json数据为空
			if (jsonText == null || jsonText.Equals("") == true || type == null) {
				return null;
			}

			JsonData jsonData = null;
			try {
				jsonData = JsonMapper.ToObject (jsonText);
			} catch {
				Debug.Log("error JsonParser");
			}

			if (jsonData == null) {
				return null;
			}

			return parser (jsonData, type);
		}


		/// <summary>
		/// 把jsonData转化成对象
		/// </summary>
		/// <param name="inputData">Input data.</param>
		/// <param name="type">Type.</param>
		public static object parser(JsonData inputData, Type type) {
			if (inputData == null || type == null) {
				return null;
			}


			FieldInfo[] propertyInfos = type.GetFields ();
			if (propertyInfos == null || propertyInfos.Length <= 0) {
				return null;
			}

			object resultObject = Activator.CreateInstance(type);

			FieldInfo propertyInfo;
			for (int index = 0; index < propertyInfos.Length; index++) {
				propertyInfo = propertyInfos[index];
				if (propertyInfo == null) {
					continue;
				}

				if (propertyInfo.FieldType == null) {
					continue;
				}

				if (propertyInfo.IsLiteral == true) {
					continue;
				}

				//最顶层的对象不进行解包，这里或者需要对某些FieldInfo进行过滤，
				if (propertyInfo.FieldType.Equals (typeof(object)) == true) {
					continue;
				}

				//基础数据类型
				if (inputData.IsArray == false && handles.ContainsKey (propertyInfo.FieldType.FullName) == true) {
					HandleBaseType handleBaseType = handles [propertyInfo.FieldType.FullName];
					if (handleBaseType == null) {
						continue;
					}

					if (inputData.Keys.Contains (propertyInfo.Name) == false) {
						continue;
					}

					object result = inputData[propertyInfo.Name];
					if (result == null) {
						continue;
					}
					//对具体的数据类类型进行强制转换
					object baseResult = handleBaseType (result);
					//把数据设置到对象中
					propertyInfo.SetValue(resultObject, baseResult);
				} else {
					if (inputData.Keys.Contains (propertyInfo.Name) == false) {
						continue;
					}
					JsonData innerJson = inputData[propertyInfo.Name];
					if (innerJson == null) {
						continue;
					}
					if (innerJson.IsArray == true) {
						//处理数组类型
						propertyInfo.SetValue(resultObject, handleArray (innerJson, propertyInfo.FieldType));

					} else {
						//处理基础对象数据类型
						handleObject (innerJson, propertyInfo, resultObject);
					}
				}
			}
			return resultObject;
		}

		/// <summary>
		/// 普通对象类型处理
		/// </summary>
		/// <param name="innerJson">Inner json.</param>
		/// <param name="propertyInfo">Property info.</param>
		/// <param name="resultObject">Result object.</param>
		private static void handleObject(JsonData innerJson, FieldInfo propertyInfo, object resultObject) {
			//普通对象类型
			object result = parser (innerJson, propertyInfo.FieldType);
			if (result == null) {
				return;
			}
			propertyInfo.SetValue(resultObject, result);
		}


		/// <summary>
		/// 处理数组类型数据
		/// </summary>
		/// <param name="innerJson">Inner json.</param>
		/// <param name="propertyInfo">Property info.</param>
		/// <param name="resultObject">Result object.</param>
		private static object handleArray(JsonData innerJson, Type propertyInfo) {
			//对象数组
			Type type = propertyInfo.GetGenericArguments()[0];
			var listType = typeof(List<>).MakeGenericType(type);
			IList resultList = (IList)Activator.CreateInstance(listType);

			//基础数组类型
			if (handles.ContainsKey (type.FullName) == true) {
				HandleBaseType handleBaseType = handles [type.FullName];
				if (handleBaseType == null) {
					return resultList;
				}
				object result = null;
				for (int index = 0; index < innerJson.Count; index++) {
					result = handleBaseType (innerJson[index]);
					resultList.Add (result);
				}
			} else {
				object result = null;
				JsonData resultValue;
				for (int index = 0; index < innerJson.Count; index++) {
					resultValue = innerJson [index];
					if (resultValue == null) {
						continue;
					}

					if (resultValue.IsArray) {
						result = handleArray (resultValue, type);
					} else {
						result = parser (resultValue, type);
					}
					resultList.Add (result);
				}
			}

			return resultList;
		}



		/// <summary>
		/// float类型数据
		/// </summary>
		/// <param name="propertyInfo">Property info.</param>
		/// <param name="innerJson">Inner json.</param>
		/// <param name="resultObject">Result object.</param>
		private static object floatHandler(object result) {
			if (string.IsNullOrEmpty(result.ToString ()) == true) {
				return 0.0f;
			}
			return float.Parse (result.ToString());
		}


		/// <summary>
		/// double类型烽据
		/// </summary>
		/// <param name="propertyInfo">Property info.</param>
		/// <param name="innerJson">Inner json.</param>
		/// <param name="resultObject">Result object.</param>
		private static object doubleHandler(object result) {
			if (string.IsNullOrEmpty(result.ToString ()) == true) {
				return 0.0;
			}
			return double.Parse (result.ToString());
		}

		/// <summary>
		/// 字符串数据
		/// </summary>
		/// <param name="propertyInfo">Property info.</param>
		/// <param name="innerJson">Inner json.</param>
		/// <param name="resultObject">Result object.</param>
		private static object stringHandler(object result) {
			return result.ToString ();
		}

		/// <summary>
		/// 整数数据
		/// </summary>
		/// <param name="propertyInfo">Property info.</param>
		/// <param name="innerJson">Inner json.</param>
		/// <param name="resultObject">Result object.</param>
		private static object intHandler(object result) {
			if (string.IsNullOrEmpty(result.ToString ()) == true) {
				return 0;
			}
			return int.Parse (result.ToString());
		}

		private static object shortHandler(object result) {
			if (string.IsNullOrEmpty(result.ToString ()) == true) {
				return 0;
			}
			return short.Parse (result.ToString());
		}


		private static object bytetHandler(object result) {
			if (string.IsNullOrEmpty(result.ToString ()) == true) {
				return 0;
			}
			return byte.Parse (result.ToString());
		}

		/// <summary>
		/// 整数数据
		/// </summary>
		/// <param name="propertyInfo">Property info.</param>
		/// <param name="innerJson">Inner json.</param>
		/// <param name="resultObject">Result object.</param>
		private static object longHandler(object result) {
			if (string.IsNullOrEmpty(result.ToString ()) == true) {
				return 0;
			}
			return long.Parse (result.ToString());
		}


		public static Dictionary<string, HandleBaseType> handles {
			get {
				if (_handles == null) {
					_handles = new Dictionary<string, HandleBaseType> ();
					_handles.Add (typeof(float).FullName, floatHandler);
					_handles.Add (typeof(double).FullName, doubleHandler);
					_handles.Add (typeof(string).FullName, stringHandler);
					_handles.Add (typeof(int).FullName, intHandler);
					_handles.Add (typeof(long).FullName, longHandler);
					_handles.Add (typeof(short).FullName, shortHandler);
					_handles.Add (typeof(byte).FullName, bytetHandler);
				}
				return _handles;
			}
		}
	}

}