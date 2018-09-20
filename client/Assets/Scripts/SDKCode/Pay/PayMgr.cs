using UnityEngine;
using UnityEngine.UI;
using System.Collections.Generic;
using System.Runtime.InteropServices;


public class PayMgr : MonoSingleton<PayMgr>
{
#if _LOCAL_GAME_
    
	#if (UNITY_IPHONE || UNITY_IOS) && !UNITY_EDITOR
		[DllImport("__Internal")]
		private static extern void BuyProductIOS(string productId);
		[DllImport("__Internal")]
		private static extern void InitIAPHelperIOS(string callBackObjectName, string gameServerAddress);
		[DllImport("__Internal")]
		private static extern void StartBuyProductIOS(string productId, string payInfo);
		[DllImport("__Internal")]
		private static extern void CompletedSKPaymentTransactionIOS(string identifier);
		[DllImport("__Internal")]
		private static extern void RestoreTransactionsIOS(string productIds, string username);
		[DllImport("__Internal")]
		private static extern void RestoreTransactionIOS(string productId, string username);
	#endif
#endif

	void Start()
	{
#if _LOCAL_GAME_	
	#if (UNITY_IPHONE || UNITY_IOS) && !UNITY_EDITOR
		InitIAPHelperIOS ("PayMgr", LuaFramework.AppConst.payURL);
	#endif
#endif
	}

	// 真实支付接口
	public void Pay(string payItem, string payInfo, int type, string cpOrderId="") {
#if UNITY_ANDROID && !UNITY_EDITOR
		AndroidJavaClass javaClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
		AndroidJavaObject currentActivity = javaClass.GetStatic<AndroidJavaObject>("currentActivity");
		currentActivity.Call("pay", payInfo, type);
#elif (UNITY_IPHONE || UNITY_IOS) && !UNITY_EDITOR
		#if _LOCAL_GAME_
			StartBuyProductIOS(payItem, payInfo);
		#endif
#endif
	}

	//// iap 回调
	void PreBuyProductFailed(string productInfo)
	{
		//TODO 根据自己的需求处理结果
	}
	//// iap 回调
	void GotProductsSuccess()
	{

	}
	//// iap 回调
	void BuyProductSuccess(string productInfo)
	{

	}
	//// iap 回调
	void BuyProudctFailed(string transactionIdentifier)
	{
		//TODO 根据自己的需求处理结果
	}
	//// iap 回调
	void VerifyProductReceiptData(string receiptData)
	{
		// IAPNetWorkDispatcher.Instance.ParseAndVerifyReceiptData(receiptData);
	}
	//// iap 回调
	void VerifyProductReceiptDataRestore(string receiptData)
	{
		// IAPNetWorkDispatcher.Instance.ParseAndVerifyReceiptData(receiptData);
	}

	//// iap 回调 restore product not exist in appid's list
	public void RestoreProductFailed(string productIdentifier)
	{
		//IAPNetWorkDispatcher.Instance.RemoveRestoreTransaction(productIdentifier);
		//TODO 根据自己的需求处理结果
	}
	// iap 回调
	public void CancelRestoreProducts()
	{
		//IAPNetWorkDispatcher.Instance.RemoveRestoreTransactions();
		//TODO 根据自己的需求处理结果
	}

// 以下是之前测试调用
	// 测试用
	public void StartBuyProduct(string productId, string payInfo)
	{
#if _LOCAL_GAME_
	#if (UNITY_IPHONE || UNITY_IOS) && !UNITY_EDITOR
		StartBuyProductIOS(payItem, payInfo);
	#endif
#endif
	}

	/// 结果回调
	public void PayResult(string result)
	{
		Debug.Log(result);
	}


	public void CompletedSKPaymentTransaction(string identifier)
	{

	}

	public void StartRestoreProduct(string productIdentifier)
	{

	}

	public void RestoreTransactions(string productIds, string username)
	{
		Debug.Log("RestoreTransactions");
#if _LOCAL_GAME_
	#if (UNITY_IPHONE || UNITY_IOS) && !UNITY_EDITOR
		RestoreTransactionsIOS(productIds, username);
	#endif
#endif
	}

	public void RestoreTransaction(string productId, string username)
	{
#if _LOCAL_GAME_
	#if (UNITY_IPHONE || UNITY_IOS) && !UNITY_EDITOR
    	RestoreTransactionIOS(productId, username);
		#endif
#endif
	}
}
