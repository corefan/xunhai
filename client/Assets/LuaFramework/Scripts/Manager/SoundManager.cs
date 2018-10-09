using UnityEngine;
using System.Collections;
using System.Collections.Generic;

namespace LuaFramework
{
    [RequireComponent(typeof(AudioSource))]
    public class SoundManager : Manager
    {
        public AudioSource audio;
        public float bgAudioVolume = 1.0f;

        public AudioSource effAudio;
        public float effAudioVolume = 1.0f;

        public AudioSource walkAudio;
        public float walkVolume = 1.0f;

        private Hashtable sounds = new Hashtable();
        string backSoundKey = "";
        string walkSoundKey = "";

        private Hashtable requestLoadMap = new Hashtable();

        private float refuseTime = 0.0f;

        private AudioListener loginAudioListener;
        void Start()
        {
            audio = GetComponent<AudioSource>();
            if (audio == null)
            {
                audio = gameObject.AddComponent<AudioSource>();
            }

            loginAudioListener = GetComponent<AudioListener>();
            if (loginAudioListener == null)
            {
                loginAudioListener = gameObject.AddComponent<AudioListener>();
            }
        }

        public void DestroyLoginAudioListener()
        {
            if (loginAudioListener != null)
            {
                Destroy(loginAudioListener);
            }
        }

        /// <summary>
        /// 走路音效
        /// </summary>
        public void RegistWalkAudio()
        {
            walkAudio = GetComponent<AudioSource>();
            if (walkAudio == null)
                walkAudio = gameObject.AddComponent<AudioSource>();
        }

        /// 注册音效源
        public void RegistEffAudio()
        {
            if (effAudio == null)
            {
                GameObject gameObject = GameObject.Find("Stage");
                effAudio = gameObject.GetComponent<AudioSource>();
            }
            UpdateEffVolume();
        }
        /// 音量
        public void SetEffVolume(float value)
        {
            effAudioVolume = value;
            UpdateEffVolume();
        }
        public void UpdateEffVolume()
        {
            if (effAudio)
            {
                if (effAudioVolume == 0f)
                    effAudio.Stop();
                else
                    effAudio.Play();
                effAudio.volume = effAudioVolume;
            }

            if (walkAudio)
            {
                walkAudio.volume = effAudioVolume;
                if (effAudioVolume == 0f)
                    effAudio.Stop();
                else
                    effAudio.Play();
            }
        }
        /// 音量
        public void SetBgVolume(float value)
        {
            bgAudioVolume = value;
            UpdateBgVolume();
        }
        public void UpdateBgVolume()
        {
            if (audio)
            {
                audio.volume = bgAudioVolume;
                if (bgAudioVolume == 0f)
                    audio.Stop();
                else
                    audio.Play();
            }
        }
        //回调函数原型
        private delegate void GetBack(AudioClip clip, string key);
        //获取声音资源
        private void Get(string assetName, GetBack cb)
        {
            string key = assetName;
            if (sounds[key] == null)
            {
                if (requestLoadMap[key] != null) return;
                requestLoadMap.Add(key, 1);
                ResManager.LoadAudioClip("Audio/" + assetName, (objs) =>
                {
                    if (objs == null)
                    {
                        Debug.Log("获取背景声音资源 失败:" + "|" + assetName);
                        cb(null, key);
                        requestLoadMap.Remove(key);
                        return;
                    }
                    else
                    {
                        sounds.Add(key, objs);
                        cb(objs as AudioClip, key);
                        return;
                    }
                });
            }
            else
            {
                cb(sounds[key] as AudioClip, key);
                return;
            }
        }

        //播放背景音乐
        public void PlayBackSound(string assetName)
        {
            if (assetName != backSoundKey) StopBackSound();
            backSoundKey = assetName;
            //AudioClip clip = Resources.Load<AudioClip>("Audio/" + assetName);
            //if (clip == null) return;
            //audio.loop = true;
            //audio.clip = clip;
            //audio.Play();
            Get(assetName, (clip, key) =>
            {
                if (clip == null) return;
                if (key != backSoundKey) return;
                audio.loop = true;
                audio.clip = clip;
                audio.Play();
            });
        }

        //停止背景音乐
        public void StopBackSound()
        {
            backSoundKey = "";
            if (audio != null)
                audio.Stop();
        }
        //播放音效
        public void PlayEffect(string assetName)
        {
            if (Time.time - refuseTime > 0)
                refuseTime = Time.time + 0.05f;
            else
                return;
            //AudioClip clip = Resources.Load<AudioClip>("Audio/" + assetName);
            //if (clip == null) return;
            //effAudio.PlayOneShot(clip);
            Get(assetName, (clip, key) =>
            {
                if (clip == null) return;
                effAudio.PlayOneShot(clip);

            });
        }

        public void StopEffect()
        {
            if (effAudio != null)
            {
                effAudio.Stop();
            }
        }

        //播放音效
        public void PlaySound(string assetName)
        {
            //AudioClip clip = Resources.Load<AudioClip>("Audio/" + assetName);
            //if (clip == null) return;
            //if (Camera.main == null) return;
            //AudioSource.PlayClipAtPoint(clip, Camera.main.transform.position);
            Get(assetName, (clip, key) =>
            {
                if (clip == null) return;
                if (Camera.main == null) return;
                AudioSource.PlayClipAtPoint(clip, Camera.main.transform.position);
            });
        }
        /// <summary>
        /// 添加一个声音
        /// </summary>
        public void Add(string key, AudioClip value)
        {
            if (sounds[key] != null || value == null) return;
            sounds.Add(key, value);
        }

        /// <summary>
        /// 获取一个声音
        /// </summary>
        public AudioClip Get(string key)
        {
            if (sounds[key] == null) return null;
            return sounds[key] as AudioClip;
        }

    }
}