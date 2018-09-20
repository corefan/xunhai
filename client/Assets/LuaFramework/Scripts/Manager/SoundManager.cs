using UnityEngine;
using System.Collections;
using System.Collections.Generic;

namespace LuaFramework {
    [RequireComponent(typeof(AudioSource))]
    public class SoundManager : Manager {
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
        private delegate void GetBack (AudioClip clip, string key);
        //获取声音资源
        private void Get (string abName, string assetName, GetBack cb)
        {
            string key = abName + "." + assetName;
            if (sounds [key] == null)
            {
                if (requestLoadMap[key] != null ) return;
                requestLoadMap.Add(key, 1);
                ResManager.LoadAudioClip (abName, assetName, (objs) => {
                    if (objs == null || objs[0] == null)
                    {
                        Debug.Log("获取背景声音资源 失败:" + abName + "|" + assetName);
                        cb (null, key);
                        requestLoadMap.Remove(key);
                        return;
                    }else { 
                        sounds.Add (key, objs[0]);
                        cb (objs[0] as AudioClip , key);
                        return;
                    }
                });
            }
            else
            {
                cb (sounds [key] as AudioClip, key);
                return;
            }
        }

        //播放背景音乐
        public void PlayBackSound (string abName, string assetName)
        {
            string tmp = abName + "." + assetName;
            if (backSoundKey != tmp) StopBackSound();
            backSoundKey = tmp;
            Get (abName, assetName, (clip, key) =>
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

        //播放走动音乐(循环动作声音)
        public void PlayWalkSound(string abName, string assetName)
        {
            string tmp = abName + "." + assetName;
            if (walkSoundKey != tmp) StopBackSound();
            walkSoundKey = tmp;
            Get(abName, assetName, (clip, key) =>
            {
                if (clip == null) return;
                if (key != walkSoundKey) return;
                walkAudio.loop = true;
                walkAudio.clip = clip;
                walkAudio.Play();
            });
        }
        //停止走动音乐
        public void StopWalkSound()
        {
            walkSoundKey = "";
            if (walkAudio != null)
                walkAudio.Stop();
        }

        //播放音效
        public void PlayEffect(string abName, string assetName)
        {
            if (Time.time - refuseTime > 0)
                refuseTime = Time.time + 0.05f;
            else
                return;
            Get(abName, assetName, (clip, key) =>
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
        public void PlaySound (string abName, string assetName)
        {
            Get (abName, assetName, (clip, key) =>
            {
                if (clip == null) return;
                if (Camera.main == null) return;
                AudioSource.PlayClipAtPoint (clip, Camera.main.transform.position);
            });
        }
        /// <summary>
        /// 添加一个声音
        /// </summary>
        public void Add(string key, AudioClip value) {
            if (sounds[key] != null || value == null) return;
            sounds.Add(key, value);
        }

        /// <summary>
        /// 获取一个声音
        /// </summary>
        public AudioClip Get(string key) {
            if (sounds[key] == null) return null;
            return sounds[key] as AudioClip;
        }

        //网络的，以后再加上
        public void PlayWebSound()
        {
            //WWW www = new WWW()
        }

    /*
        /// <summary>
        /// 是否播放背景音乐，默认是1：播放
        /// </summary>
        /// <returns></returns>
        public bool CanPlayBackSound() {
            string key = AppConst.AppPrefix + "BackSound";
            int i = PlayerPrefs.GetInt(key, 1);
            return i == 1;
        }

        /// <summary>
        /// 是否播放音效,默认是1：播放
        /// </summary>
        /// <returns></returns>
        public bool CanPlaySoundEffect() {
            string key = AppConst.AppPrefix + "SoundEffect";
            int i = PlayerPrefs.GetInt(key, 1);
            return i == 1;
        }

        /// <summary>
        /// 播放音频剪辑
        /// </summary>
        /// <param name="clip"></param>
        /// <param name="position"></param>
        public void Play(AudioClip clip, Vector3 position) {
            if (!CanPlaySoundEffect()) return;
            AudioSource.PlayClipAtPoint(clip, position); 
        }

        /// <summary>
        /// 播放背景音乐
        /// </summary>
        /// <param name="canPlay"></param>
        public void PlayBacksound(string name, bool canPlay) {
            if (audio.clip != null) {
                if (name.IndexOf(audio.clip.name) > -1) {
                    if (!canPlay) {
                        audio.Stop();
                        audio.clip = null;
                        Util.ClearMemory();
                    }
                    return;
                }
            }
            if (canPlay) {
                audio.loop = true;
                audio.clip = LoadAudioClip(name);
                audio.Play();
            } else {
                audio.Stop();
                audio.clip = null;
                Util.ClearMemory();
            }
        }

        /// <summary>
        /// 载入一个音频
        /// </summary>
        public AudioClip LoadAudioClip(string path) {
            AudioClip ac = Get(path);
            if (ac == null) {
                ac = (AudioClip)Resources.Load(path, typeof(AudioClip));
                Add(path, ac);
            }
            return ac;
        }
    */


    /*
        private new AudioSource audio;
        private Hashtable sounds = new Hashtable();

        void Start() {
            audio = GetComponent<AudioSource>();
        }

        /// <summary>
        /// 添加一个声音
        /// </summary>
        void Add(string key, AudioClip value) {
            if (sounds[key] != null || value == null) return;
            sounds.Add(key, value);
        }

        /// <summary>
        /// 获取一个声音
        /// </summary>
        AudioClip Get(string key) {
            if (sounds[key] == null) return null;
            return sounds[key] as AudioClip;
        }

        /// <summary>
        /// 载入一个音频
        /// </summary>
        public AudioClip LoadAudioClip(string path) {
            AudioClip ac = Get(path);
            if (ac == null) {
                ac = (AudioClip)Resources.Load(path, typeof(AudioClip));
                Add(path, ac);
            }
            return ac;
        }

        /// <summary>
        /// 是否播放背景音乐，默认是1：播放
        /// </summary>
        /// <returns></returns>
        public bool CanPlayBackSound() {
            string key = AppConst.AppPrefix + "BackSound";
            int i = PlayerPrefs.GetInt(key, 1);
            return i == 1;
        }

        /// <summary>
        /// 播放背景音乐
        /// </summary>
        /// <param name="canPlay"></param>
        public void PlayBacksound(string name, bool canPlay) {
            if (audio.clip != null) {
                if (name.IndexOf(audio.clip.name) > -1) {
                    if (!canPlay) {
                        audio.Stop();
                        audio.clip = null;
                        Util.ClearMemory();
                    }
                    return;
                }
            }
            if (canPlay) {
                audio.loop = true;
                audio.clip = LoadAudioClip(name);
                audio.Play();
            } else {
                audio.Stop();
                audio.clip = null;
                Util.ClearMemory();
            }
        }

        /// <summary>
        /// 是否播放音效,默认是1：播放
        /// </summary>
        /// <returns></returns>
        public bool CanPlaySoundEffect() {
            string key = AppConst.AppPrefix + "SoundEffect";
            int i = PlayerPrefs.GetInt(key, 1);
            return i == 1;
        }

        /// <summary>
        /// 播放音频剪辑
        /// </summary>
        /// <param name="clip"></param>
        /// <param name="position"></param>
        public void Play(AudioClip clip, Vector3 position) {
            if (!CanPlaySoundEffect()) return;
            AudioSource.PlayClipAtPoint(clip, position); 
        }
    */
    }
}