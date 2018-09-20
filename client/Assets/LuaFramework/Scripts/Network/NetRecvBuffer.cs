using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace LuaFramework
{
    // 消息结构,含id和buff data
    public struct NetRecvData
    {
        public int _msgId;
        public byte[] _data;
    }

    public class NetRecvBuffer
    {
        private byte[] _buff;   // 内部缓冲区
        private int _buffLen = 0;  // 内部缓冲区大小
        private int _curPos = 0; // 标识接收到的字节数
        private int _dataLen = 0; // 当前处理的包中的数据部分长度
        private int _msgId;
        private int _defaultLen;

        public NetRecvBuffer(int len = NetConst.TMP_BUF_SIZE)
        {
            _defaultLen = len;
            _buff = new byte[len];
        }

        public void AddBuffer(byte[] buf, int len)
        {
            if (len > _buff.Length - _curPos) 
            { 
                byte[] newBuf = new byte[_curPos + len]; // 新开个buffer替代之前的
                Array.Copy(_buff, 0, newBuf, 0, _curPos);
                Array.Copy(buf, 0, newBuf, _curPos, len);
                _buff = newBuf;
                newBuf = null;
            }
            else 
            {
                Array.Copy(buf, 0, _buff, _curPos, len);
            }
            _curPos += len;
        }

        // 更新当前处理消息的各种长度数据
        private void UpdateLen() 
        {
            if (_dataLen == 0 && _curPos >= NetConst.MSG_HEAD_LEN)
            { 
                // 解长度
                byte[] lenBuf = new byte[NetConst.MSG_DATA_LEN];
                Array.Copy(_buff, 0, lenBuf, 0, NetConst.MSG_DATA_LEN);
                _buffLen = BitConverter.ToInt32(lenBuf, 0).SwapInt32();

                // 解id
                byte[] idBuf = new byte[NetConst.MSG_ID_LEN];
                Array.Copy(_buff, NetConst.MSG_DATA_LEN, idBuf, 0, NetConst.MSG_ID_LEN);
                _msgId = BitConverter.ToInt32(idBuf, 0).SwapInt32();
                //Debug.Log("id ++ " + _msgId);

                _dataLen = _buffLen - NetConst.MSG_HEAD_LEN;
            }
        }

        public bool GetOneMsg(ref NetRecvData recvData)
        { 
            if ( _buffLen <= 0) 
            {
                UpdateLen();
            }
            if (_buffLen > 0 && _buffLen <= _curPos) 
            {
                recvData._msgId = _msgId;
                recvData._data = new byte[_dataLen];
                Array.Copy(_buff, NetConst.MSG_HEAD_LEN, recvData._data, 0, _dataLen);

                _curPos -= _buffLen;
                byte[] newBuf = new byte[_curPos > NetConst.TMP_BUF_SIZE ? _curPos : NetConst.TMP_BUF_SIZE];
                Array.Copy(_buff, _buffLen, newBuf, 0, _curPos);
                _buff = newBuf;
                newBuf = null;

                _buffLen = 0;
                _dataLen = 0;
                _msgId = 0;
                return true;
            }
            return false;
        }

        public void Reset()
        {
            _buffLen = 0;
            _dataLen = 0;
            _msgId = 0;
            _curPos = 0;
            _defaultLen = NetConst.TMP_BUF_SIZE;
            _buff = new byte[_defaultLen];
        }
    }
}
