using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using UnityEngine;
using System.Collections;
using LuaInterface;
using LuaFramework;
using Ionic.Zlib;

[RequireComponent (typeof(AudioSource))]
public class MicroPhoneInput : MonoBehaviour 
{

	private static MicroPhoneInput m_instance;

	public float sensitivity=100;
	public float loudness=0;

    private static string[] micArray=null;

    const int HEADER_SIZE = 44;
    const int RECORD_TIME = 5;

    private bool _isRecording = false;
    private float _recordTime = 0;

	// Use this for initialization
	void Start () {
	}

	public static MicroPhoneInput getInstance()
	{
		if (m_instance == null) 
		{
            micArray = Microphone.devices;
            if (micArray.Length == 0)
            {
                Debug.LogError ("Microphone.devices is null");
            }
            foreach (string deviceStr in Microphone.devices)
            {
                Debug.Log("device name = " + deviceStr);
            }
			if(micArray.Length==0)
			{
				Debug.LogError("no mic device");
			}

			GameObject MicObj=new GameObject("MicObj");
			m_instance= MicObj.AddComponent<MicroPhoneInput>();
		}
		return m_instance;
	}

	public void StartRecord()
	{
        _recordTime = 0;
        GetComponent<AudioSource>().Stop();
        if (micArray.Length == 0)
        {
            Debug.Log("No Record Device!");
            return;
        }
		GetComponent<AudioSource>().loop = false;
        GetComponent<AudioSource>().mute = true;
        GetComponent<AudioSource>().clip = null;
        GetComponent<AudioSource>().clip = Microphone.Start(null, false, RECORD_TIME, 8000); //22050 
        //while (!(Microphone.GetPosition(null)>0)) {
        //}
		GetComponent<AudioSource>().Play ();
        _isRecording = true;
        Debug.Log("StartRecord");
        //倒计时
        StartCoroutine(TimeDown());
	}

	public  void StopRecord()
	{
        _isRecording = false;
        if (micArray.Length == 0)
        {
            Debug.Log("No Record Device!");
            return;
        }
        if (!Microphone.IsRecording(null))
        {
            return;
        }
		Microphone.End (null);
        GetComponent<AudioSource>().Stop();
        Debug.Log("StopRecord");

	}

    public LuaByteBuffer GetClipData()
    {
        if (GetComponent<AudioSource>().clip == null)
        {
            Debug.Log("GetClipData audio.clip is null");
            return null; 
        }

        float[] samples = new float[GetComponent<AudioSource>().clip.samples];
        GetComponent<AudioSource>().clip.GetData(samples, 0);

		Byte[] outData = new byte[samples.Length * 2];
        int rescaleFactor = 32767;
        for (int i = 0; i < samples.Length; i++)
        {
            short temshort = (short)(samples[i] * rescaleFactor);
			Byte[] temdata=System.BitConverter.GetBytes(temshort);
			outData[i*2]=temdata[0];
			outData[i*2+1]=temdata[1];
        }

        Debug.Log("GetClipData compress pre:" + outData.Length);
        byte[] outData1 = Compress(outData);
        Debug.Log("GetClipData compress ed:" + outData1.Length + " compress percent:" + (outData.Length / outData1.Length) + " 长度" + outData1.Length + " 大小（kb）：" + (outData1.Length / 1024));
        LuaByteBuffer luabts = new LuaByteBuffer(outData1);

        int size = 1000 * 40;
        byte[] bs = new byte[size];
        //byte[] bs = new byte[] { 0, 0, 1, 1, 4, 123, 21, 35, 54, 1, 1, 0, 0 };
        for (int i = 0; i < size; i++)
        {
            bs[i] = (byte)LuaFramework.Util.Random(-128, 127);
        }
        Debug.Log(bs[0]+"|"+bs[bs.Length-1]+"|"+bs.Length);
        LuaByteBuffer luabts1 = new LuaByteBuffer(bs);
        return luabts;
    }

    public static LuaByteBuffer GetVoiceData()
    {
        byte[] bs = new byte[40000];
        for (int i = 0; i < bs.Length; i++)
        {
            bs[i] = (byte)Util.Random(-128, 127);
        }
        Debug.Log("发送起始字节：" + bs[0] + "| 最后个字节：" + bs[bs.Length - 1] + "|长度 " + bs.Length);
        LuaByteBuffer bytes = new LuaByteBuffer(bs);
        return bytes;
    }

    public void PlayClipData(byte[] bs)
    {
        Debug.Log(bs[0] + " | " + bs[bs.Length - 1] + " | " + bs.Length);
        byte[] data = DeCompress(bs);
        PlayClipData(ByteToHexStr(data));
    }

    public void PlayClipData(Int16[] intArr)
    {

        string aaastr = intArr.ToString();
        long  aaalength=aaastr.Length;

		string aaastr1 = Convert.ToString (intArr);
		aaalength = aaastr1.Length;

        if (intArr.Length == 0)
        {
            Debug.Log("get intarr clipdata is null");
            return;
        }
        //从Int16[]到float[]
        float[] samples = new float[intArr.Length];
        int rescaleFactor = 32767;
        for (int i = 0; i < intArr.Length; i++)
        {
            samples[i] = (float)intArr[i] / rescaleFactor;
        }
        
        //从float[]到Clip
        AudioSource audioSource = this.GetComponent<AudioSource>();
        if (audioSource.clip == null)
        {
            audioSource.clip = AudioClip.Create("playRecordClip", intArr.Length, 1, 44100, false, false);
        }
        audioSource.clip.SetData(samples, 0);
        audioSource.mute = false;
        audioSource.Play();
    }

    public byte[] Compress(byte[] bytes)
    {
        return DeflateStream.CompressBuffer(bytes);
    }

    public byte[] DeCompress(byte[] bytes)
    {
        return DeflateStream.UncompressBuffer(bytes);
    }


    public Int16[] ByteToHexStr(byte[] bytes)
    {
        byte[] data = bytes;
        int i = 0;
        List<short> result = new List<short>();
        while (data.Length - i > 2)
        {
            result.Add(BitConverter.ToInt16(data, i));
            i += 2;
        }
        Int16[] arr = result.ToArray();//这就是你要的
        return arr;
    }

    public void PlayRecord()
	{
        if (GetComponent<AudioSource>().clip == null)
        {
            Debug.Log("audio.clip=null");
            return;
        }
		GetComponent<AudioSource>().mute = false;
		GetComponent<AudioSource>().loop = false;
		GetComponent<AudioSource>().Play ();
        Debug.Log("PlayRecord");

	}

	public  float GetAveragedVolume()
	{
		float[] data=new float[256];
		float a=0;
		GetComponent<AudioSource>().GetOutputData(data,0);
		foreach(float s in data)
		{
			a+=Mathf.Abs(s);
		}
		return a/256;
	}
	
	// Update is called once per frame
	void Update ()
    {
		loudness = GetAveragedVolume () * sensitivity;
		if (loudness > 1) 
		{
			Debug.Log("loudness = "+loudness);
		}
        if (_isRecording)
        {
            _recordTime = _recordTime + Time.deltaTime;
        }
	}

    public float GetRecordTime()
    {
        return _recordTime;
    }

    public bool IsRecording()
    {
        return _isRecording;
    }

    private IEnumerator TimeDown()
    {
        Debug.Log(" IEnumerator TimeDown()");

        int time = 0;
        while (time < RECORD_TIME)
        {
			if (!Microphone.IsRecording (null)) 
			{ //如果没有录制
				Debug.Log ("IsRecording false");
				yield break;
			}
            Debug.Log("yield return new WaitForSeconds "+time);
            yield return new WaitForSeconds(1);
            time++;
        }
        if (time >= 10)
        {
            Debug.Log("RECORD_TIME is out! stop record!");
            StopRecord();
        }
        yield return 0;
    }
}
