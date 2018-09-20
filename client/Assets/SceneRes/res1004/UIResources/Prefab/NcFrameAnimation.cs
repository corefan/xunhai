using UnityEngine;
using System.Collections;
using System.Collections.Generic;

//[ExecuteInEditMode]
public class NcFrameAnimation : MonoBehaviour {
    public int rols = 1;
    public int cows = 1;
    public float  frameRate = 0.1f;
    public WrapMode wrapMode;
    public Orientation qrientation;
    public bool playInAwake = false;
    protected Renderer mRenderer;
    protected float mTilingX = 1;
    protected float mTilingY = 1;
    protected List<Vector2> mFrames = new List<Vector2>();
    protected bool mIsPlaying = false;
    protected float mLastTime;
    protected int mIndex;
    protected bool mIsPositive = false;

    public enum WrapMode
    {
        Once,
        Loop,
        PingPong
    }

    public enum Orientation
    {
        LeftBottom,
        LeftTop,
    }


	// Use this for initialization
	void OnEnable () {

        if (playInAwake)
        {
            Play();
        }
	}

    /// <summary>
    /// 播放
    /// </summary>
    public void Play()
    {
        if (mRenderer == null)
            mRenderer = GetComponent<Renderer>();

        this.rols = Mathf.Max(this.rols, 1);
        this.cows = Mathf.Max(this.cows, 1);
        mTilingX = 1.0f / rols;
        mTilingY = 1.0f / cows;
        mRenderer.material.mainTextureScale = new Vector2(mTilingX, mTilingY);
        mIsPlaying = true;
        mIsPositive = false;
        mLastTime = Time.time;
        mIndex = 0;

        mFrames.Clear();
        if (this.qrientation == Orientation.LeftBottom)
        {
            for (int y = 0; y < this.cows; ++y)
            {
                for (int x = 0; x < this.rols; ++x)
                {
                    mFrames.Add(new Vector2(x * mTilingX, y * mTilingY));
                }
            }
        }
        else
        {
            for (int y = 0; y < this.cows; ++y)
            {
                for (int x = 0; x < this.rols; ++x)
                {
                    mFrames.Add(new Vector2(x * mTilingX, (this.cows - y - 1) * mTilingY));
                }
            }
        }
    }
	
	// Update is called once per frame
	void Update () {

        if (mIsPlaying)
        {
            if (Time.time - mLastTime > this.frameRate)
            {
                mLastTime += this.frameRate;
                if (mIndex < mFrames.Count)
                {
                    if (mIsPositive)               
                    {
                        mRenderer.material.mainTextureOffset = mFrames[mFrames.Count - mIndex - 1];
                    }
                    else
                    {
                        mRenderer.material.mainTextureOffset = mFrames[mIndex];
                    }
                    mIndex++;
                }

                if (mIndex >= mFrames.Count)
                {
                    switch (wrapMode)
                    {
                        case WrapMode.Once:
                            mIsPlaying = false;
                            break;
                        case WrapMode.Loop:
                            mIndex = 0;
                            break;
                        case WrapMode.PingPong:
                            mIndex = 0;
                            mIsPositive = !mIsPositive;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
	}
}
