using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using LuaInterface;
using System.Text;
namespace LuaFramework
{

    public enum DisType
    {
        Exception = 0,
        Disconnect,
        ConnectTimeOut,
    }
    public enum ConnectState
    {
        None = 0,
        Connnecting,
        Connected,
        ConnectFail,
        OffLine,
        Disconnect,
    }

    public class NetConst
    { 
        public const int TMP_BUF_SIZE = 4096;
        public const int MSG_DATA_LEN = 4;  // 包头中标识包长的字节数
        public const int MSG_ID_LEN = 4; // 包头中标识id的字节数
        public const int MSG_HEAD_LEN = MSG_DATA_LEN + MSG_ID_LEN;
    }
    public class NetworkManager : MonoSingleton<NetworkManager>
    {
        public bool isConnected
        {
            get{ return ConnectState.Connected == m_connectState && null != m_client && m_client.Connected;}
        }
        private ConnectState m_connectState = ConnectState.None;
        private TcpClient m_client = null;
        private string mIp = "127.0.0.1";
        private int mPort = 0;
        public int connectTimes = 0;//连续失败重连连接次数
        private bool dispatchConnect = false;//开启广播连接成功
        private int lostHeartTimes = 0; //丢失心跳数量 

        private byte[] keyBytes = Encoding.UTF8.GetBytes("Glv8PzQQ4fa8P2017");//协议加密

        private byte[] tmpRecvBuf = new byte[NetConst.TMP_BUF_SIZE];
        private Thread recvThread = null;
        private NetRecvBuffer netRecvBuf = new NetRecvBuffer();
        private Queue<NetRecvData> msgQueue = new Queue<NetRecvData>();

        protected override void OnCreate()
        {
            Debug.Log("初始化网络");
            CallMethod("Start");
        }

        #region 发起连接
        /// 发送连接请求
        public void SendConnect(string host, int port)
        {
            if (Application.internetReachability != NetworkReachability.NotReachable)
            {
                Init(host, port);
                Connect();
            }
            else
            {
                m_connectState = ConnectState.ConnectFail;
                OnDisconnected(DisType.ConnectTimeOut, "连接游戏服务器失败！(404)");
            }
        }
        //初始化ip port
        public void Init(string host, int port)
        {
            mIp = host;
            mPort = port;
        }
        //连接
        public void Connect()
        {
            if (isConnected || m_connectState == ConnectState.Connnecting)
            {
                Debug.Log("已经连接上了，别再连接，会出事的！");
                return;
            }
            string strNewIp = "";
            AddressFamily newAddressFamily = AddressFamily.InterNetwork;
            IPv6SupportMidleware.getIPType(mIp, mPort.ToString(), out strNewIp, out newAddressFamily);
            Debug.Log(" Socket AddressFamily :" + newAddressFamily.ToString() + "原始ip:" + mIp + ", 结果ip:" + strNewIp + ",端口:" + mPort);

            m_client = new TcpClient(newAddressFamily);
            m_client.SendTimeout = 1000;
            m_client.ReceiveTimeout = 5000;
            m_client.NoDelay = true;
            m_client.SendBufferSize = 1024 * 8;
            m_client.ReceiveBufferSize = NetConst.TMP_BUF_SIZE;
            m_client.Client.Blocking = true;
            m_connectState = ConnectState.Connnecting;
            try
            {
                m_client.BeginConnect(strNewIp, mPort, new AsyncCallback(OnConnect), m_client);
            }
            catch (Exception e)
            {
                //*设置失败次数++
                Debug.Log("连接不通：" + e.Message + " ==>" + e.ToString());
                m_connectState = ConnectState.ConnectFail;
                OnDisconnected(DisType.ConnectTimeOut, "连接游戏服务器失败！(-1)");
            }
        }

        /// 连接上服务器
        private void OnConnect(IAsyncResult asr)
        {
            try
            {
                TcpClient client = (TcpClient)asr.AsyncState;
                if (client.Connected)
                {
                    client.EndConnect(asr);
                    //*重置失败次数=0
                    client.Client.SetSocketOption(SocketOptionLevel.Tcp, SocketOptionName.NoDelay, true);
                    m_connectState = ConnectState.Connected;
                    dispatchConnect = true;
                    //*已经连接网络广播
                    Debug.Log(string.Format("@net Connected ip={0},prot={1}", mIp, mPort));
                }
                else
                {
                    //*设置失败次数++
                    m_connectState = ConnectState.ConnectFail;
                    OnDisconnected(DisType.ConnectTimeOut, "连接超时！(401)");
                }
            }
            catch (Exception e)
            {
                //*设置失败次数++
                m_connectState = ConnectState.ConnectFail;
                OnDisconnected(DisType.Exception, "连接错误：" + e.Message);  //异常广播
            }
        }
        #endregion

        #region 开启一个线程处理接收数据
        private void StartRecvThread()
        {
            recvThread = new Thread(new ThreadStart(RecvHandle));
            recvThread.IsBackground = true;
            recvThread.Start();
        }

        private void RecvHandle()
        {
            while (true)
            {
                RecvExec();
                Thread.Sleep(5);
            }
        }

        private void RecvExec()
        {
            if (m_client == null)
                return;
            if (ConnectState.Connected != m_connectState) 
            {
                return;
            }
            int len = m_client.Client.Receive(tmpRecvBuf);
            if (len > 0)
            {
                netRecvBuf.AddBuffer(tmpRecvBuf, len);
                NetRecvData tmpRecvData = new NetRecvData();
                // 一次取一条,把receive的buff取完为止
                lock (msgQueue)
                {
                    while (netRecvBuf.GetOneMsg(ref tmpRecvData))
                    {

                        if (tmpRecvData._msgId > 0)
                        {
                            //Debug.Log("Enqueue ++ ==>> " + tmpRecvData._msgId);
                            msgQueue.Enqueue(tmpRecvData);
                        }
                    }
                }
            }
        }

        private void DestroyRecvThread()
        {
            if (recvThread != null) 
            {
                recvThread.Abort();
                recvThread = null;
            }
        }
        #endregion

        #region 发送消息
        public void SendMsg(LuaByteBuffer buffer, int msgId)
        {
            StartCoroutine(SendMessage(buffer, msgId));
        }

        IEnumerator SendMessage(LuaByteBuffer buffer, int msgId)
        {
            if (m_client != null)
            {
                if (m_client.Connected)
                {
                    byte[] sendData = SocketBuff.Encryption(buffer.buffer, msgId, keyBytes);
                    try
                    {
                        if (lostHeartTimes > 3)
                        {
                            DoDisconnect();
                            yield break;
                        }
                        else if (msgId == 1200)
                        {
                            lostHeartTimes++;
                        }
                        m_client.Client.Send(sendData, SocketFlags.None);
                    }
                    catch (Exception exc)
                    {
                        if (m_connectState == ConnectState.Connected)
                            DoDisconnect();
                        else
                        {
                            OnDisconnected(DisType.Exception, "发送协议失败：cmd[" + msgId + "] \n原因-->" + exc.ToString());
                        }
                    }
                    sendData = null;
                }
                else
                {
                    if (m_connectState == ConnectState.Connected)
                        DoDisconnect();
                }
            }
            yield break;
        }

        #endregion
        public void Update()
        {
            if (!Util.NetAvailable && m_client != null)
            {
               Close();
               m_connectState = ConnectState.OffLine;
            }
            if (ConnectState.Connected == m_connectState)
            {
                if (dispatchConnect)
                {
                    dispatchConnect = false;
                    ++connectTimes;
                    if (connectTimes > 1)
                    {
                        CallMethod("OnReConnect"); Debug.Log("重新连接到 网关服务器 ---> 重新请求");
                    }
                    else
                    {
                        CallMethod("OnConnect"); Debug.Log("成功连接到 网关服务器 ---> 开始游戏");
                    }
                }
            }
            else if (ConnectState.None != m_connectState)
            {
                if (ConnectState.ConnectFail == m_connectState)
                {
                    CallMethod("ShowTimeOut", "连接游戏服务器失败, 请稍候再尝试连接！");
                }
                else if (ConnectState.Disconnect == m_connectState)
                {
                    CallMethod("OnDisconnect");
                }
                //多次心跳接收不到掉线
                else if (ConnectState.OffLine == m_connectState) 
                {
                    CallMethod("ShowTimeOut", "您的网络已断开, 请稍候再尝试连接！");
                } 
                m_connectState = ConnectState.None;
            }
            if (recvThread == null)
                StartRecvThread();
                
            if (m_client != null && ConnectState.Connected == m_connectState)
            {
                DealWithMsg();
            }
        }
        const string OnMessage="OnMessage";
        IEnumerator DealWithMessage()
        {
            lock (msgQueue)
            {
                while (msgQueue.Count > 0)
                {

                    NetRecvData recvData = msgQueue.Dequeue();
                    int msgId = recvData._msgId;
                    if (msgId <= 0)
                    {
                        Debug.Log("网络数据不正常，停卡处理此协议");
                        yield break;
                    }
                    try
                    {
                        byte[] bytes = recvData._data;
                        LuaByteBuffer buff = new LuaByteBuffer(bytes);
                        CallMethod(OnMessage, msgId, buff);
                        if (msgId == 1201) lostHeartTimes = 0; //重置心跳网络检查
                        buff = null;
                    }
                    catch (Exception e)
                    {
                        OnDisconnected(DisType.Exception, "消息报错: [msgId]" + msgId + " >> " + e.Message + "|" + e.StackTrace);
                    }
                    yield return null;
                }
              
            }
           yield break;
        }

        public void DealWithMsg()
        {
            StartCoroutine(DealWithMessage());
        }

        /// 丢失链接
        private void OnDisconnected(DisType dis, string msg)
        {
            //bool connecting = (null != m_client && m_client.Connected);//记下是否已经连接中的
            Close();//关掉客户端链接
            if (dis == DisType.Disconnect)
            {//*断开连接网络广播
                Debug.Log("@net 正常主动断线");
            }
            else if (dis == DisType.ConnectTimeOut)
            {
                Debug.Log("@net 连接服务器超时");
            }
            else if (dis == DisType.Exception)
            {
                Debug.LogError("@net 异常断开服务器:> " + msg + " Distype:>" + dis);
            }
        }

        /// 主动断开socket
        public void DoDisconnect()
        {
            m_connectState = ConnectState.Disconnect;
            OnDisconnected(DisType.Disconnect, "主动断开");
        }


        /// 正常关掉socket
        public void Close()
        {
            if (m_client != null)
            {
                m_client.Close();
                m_client = null;
            }
            lostHeartTimes = 0;
            CloseReceive();
        }
        void CloseReceive() 
        {
            DestroyRecvThread();
            msgQueue.Clear();
        }
        void OnDestroy()
        {
            StopAllCoroutines();
            DestroyRecvThread();
            if (m_client != null)
            {
                m_client.Close();
                m_client = null;
            }
            Unload();
            Debug.LogWarning("~NetworkManager was destroy");
        }
        public void Unload()
        {
            CallMethod("Unload");
        }
        public void CallMethod(string func, params object[] args)// 执行Lua方法
        {
            Util.CallMethod("Network", func, args);
        }

        #region web 请求
        public float progress = 0;
        private string content;
        private LuaFunction _callback;
        /// msgId 0 表示使用Get, 1 表示使用Post
        /// param 必需规则为:["key", "value", "key", "value", ...]
        public void HttpRequest(int type, string url, object[] param, LuaFunction callback)
        {
            _callback = callback;
            Dictionary<string, string> dic = new Dictionary<string, string>();
            if (param != null)
            {
                if (param.Length % 2 != 0)
                {
                    Debug.LogError("[WebHttpManager]参数规则有误！");
                    return;
                }

                for (int i = 0; i < param.Length; i = i + 2)
                {
                    dic.Add(param[i].ToString(), param[i + 1].ToString());
                }
            }
            if (type == 0)
                StartCoroutine(GET(url, dic));
            else
                StartCoroutine(POST(url, dic));

        }

        //POST请求(Form表单传值、效率低、安全 ，)  
        IEnumerator POST(string url, Dictionary<string, string> post)
        {
            WWWForm form = new WWWForm();//表单   
            //从集合中取出所有参数，设置表单参数（AddField()).  
            foreach (KeyValuePair<string, string> post_arg in post)
            {
                form.AddField(post_arg.Key, post_arg.Value);
            }
            //表单传值，就是post   
            WWW www = new WWW(url, form);
            yield return www;
            progress = www.progress;

            if (www.error != null)
            {
                content = "error :" + www.error;//POST请求失败
                if (_callback != null)
                    _callback.Call(0, content);
            }
            else
            { //POST请求成功
                content = www.text;
                if (_callback != null)
                    _callback.Call(1, content);
            }
        }

        //GET请求（url?传值、效率高、不安全 ）  
        IEnumerator GET(string url, Dictionary<string, string> get)
        {
            string Parameters;
            bool first;
            if (get.Count > 0)
            {
                first = true;
                Parameters = "?";
                //从集合中取出所有参数，设置表单参数（AddField()).  
                foreach (KeyValuePair<string, string> post_arg in get)
                {
                    if (first)
                        first = false;
                    else
                        Parameters += "&";
                    Parameters += post_arg.Key + "=" + post_arg.Value;
                }
            }
            else
            {
                Parameters = "";
            }

            //直接URL传值就是get  
            WWW www = new WWW(url + Parameters);
            yield return www;
            progress = www.progress;

            if (www.error != null)
            {
                //GET请求失败  
                content = "error :" + www.error;
                if (_callback != null)
                    _callback.Call(0, content);
            }
            else
            {
                //GET请求成功  
                content = www.text;
                if (_callback != null)
                    _callback.Call(1, content);
            }
        }
        #endregion
    }
}