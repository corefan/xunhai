using UnityEngine;
using System.Collections;

public class EffectController {

    public float timeCount = 0;

    private float oldTime = 0;

    private int _checkPlayTimeIndex = 0;

    private AnimationState _animationState;
    public int checkType;

    public EffectObjectDescriptor eod;

    protected float _speed = 1;
    protected float _size = 1;

    private EffectRenderObj _render;


    // 生命时间
    public float lifeTime = 0.0f;
    public EffectController(EffectRenderObj render)
    {

        _render = render;
        eod = _render.GetDes();
        //           eod.RecordState();
    }
    public void Release()
    {
        if (eod != null)
            eod = null;
        _render = null;
    }
    public void play()
    {
        for (int i = 0; i < eod.particleSystemList.Count; i++)
        {
            ParticleSystem ps = eod.particleSystemList[i];
            if (ps != null) ps.Play(true);
        }
        //foreach (SpriteAnimation sa in spriteAnimationList)
        //{
        //    sa.delayIng = true;
        //    sa.loop = false;
        //    sa.enabled = true;
        //    sa.isPaused = false;
        //}
        float time = 5000f - (Time.time - oldTime);
        for (int i = 0; i < eod.ncCurveAnimationList.Count; i++)
        {
            NcCurveAnimation nc = eod.ncCurveAnimationList[i];
            if (nc.isDelay())
                nc.m_fDelayTime -= time;
            else
                nc.ResumeAnimation();
        }
        for (int i = 0; i < eod.ncSpriteAnimationList.Count; i++)
        {
            NcSpriteAnimation nc = eod.ncSpriteAnimationList[i];
            if (nc.isDelay())
                nc.m_fDelayTime -= time;
            else
                nc.ResumeAnimation();
        }
        for (int i = 0; i < eod.ncUvAnimationList.Count; i++)
        {
            NcUvAnimation nc = eod.ncUvAnimationList[i];
            nc.ResumeAnimation();
        }
        for (int i = 0; i < eod.ncRotationList.Count; i++)
        {
            NcRotation nc = eod.ncRotationList[i];
            nc.ResumeAnimation();
        }
        for (int i = 0; i < eod.animatorList.Count; i++)
        {
            Animator animator = eod.animatorList[i];
            animator.speed = 1;
        }
        for (int i = 0; i < eod.animationList.Count; i++)
        {
            Animation animation = eod.animationList[i];
            animation.Play();
        }
    }

    public void stop()
    {
        for (int i = 0; i < eod.particleSystemList.Count; i++)
        {
            ParticleSystem ps = eod.particleSystemList[i];
            if (ps != null) ps.Pause(true);
        }
        //foreach (SpriteAnimation sa in eod.spriteAnimationList)
        //{
        //    sa.delayIng = true;
        //    sa.loop = false;
        //    sa.enabled = true;
        //    sa.isPaused = true;
        //}
        oldTime = Time.time;
        for (int i = 0; i < eod.ncCurveAnimationList.Count; i++)
        {
            NcCurveAnimation nc = eod.ncCurveAnimationList[i];
            if (nc.isDelay())
                nc.m_fDelayTime += 5000f;
            else
                nc.PauseAnimation();
        }
        for (int i = 0; i < eod.ncSpriteAnimationList.Count; i++)
        {
            NcSpriteAnimation nc = eod.ncSpriteAnimationList[i];
            if (nc.isDelay())
                nc.m_fDelayTime += 5000f;
            else
                nc.PauseAnimation();
        }
        for (int i = 0; i < eod.ncUvAnimationList.Count; i++)
        {
            NcUvAnimation nc = eod.ncUvAnimationList[i];
            nc.PauseAnimation();
        }
        for (int i = 0; i < eod.ncRotationList.Count; i++)
        {
            NcRotation nc = eod.ncRotationList[i];
            nc.PauseAnimation();
        }
        for (int i = 0; i < eod.animatorList.Count; i++)
        {
            Animator animator = eod.animatorList[i];
            animator.speed = 0;
        }
        for (int i = 0; i < eod.animationList.Count; i++)
        {
            Animation animation = eod.animationList[i];
            animation.Stop();
        }
    }

    ///  这里要注意游戏帧数变成30帧 因为是底层所以不能引用globalData;
    public bool checkPlayComplete()
    {
        timeCount += _speed * Time.deltaTime;
        //Debug.Log("                 timeCount        " + timeCount + "                  lifeTime   " + lifeTime);
        return timeCount >= lifeTime;
    }

    public void setSpeed(float speed)
    {
        _speed = speed;
        for (int i = 0; i < eod.particleSystemList.Count; i++)
        {
            ParticleSystem ps = eod.particleSystemList[i];
            ps.playbackSpeed *= speed;
        }
        for (int k = 0; k < eod.ncEffectAniBehaviourList.Count; k++)
        {
            NcEffectAniBehaviour nc = eod.ncEffectAniBehaviourList[k];
            nc.OnUpdateEffectSpeed(speed, true);
        }
        for (int i = 0; i < eod.ncRotationList.Count; i++)
        {
            NcRotation nc = eod.ncRotationList[i];
            nc.OnUpdateEffectSpeed(speed, true);
        }
    }
    public void setSize(float size)
    {
        reSize();
        _size = size;
        Vector3 scale = _render.GetScale() * size;
        scale.x = Mathf.Abs(scale.x);
        _render.GetTransForm().localScale = scale;
        for (int i = 0; i < eod.particleSystemList.Count; i++)
        {
            ParticleSystem ps = eod.particleSystemList[i];
            ps.startSize *= size;
            ps.startSpeed *= size;
        }
    }
    public void reSize()
    {
        if (eod != null)
        {
            for (int i = 0; i < eod.particleSystemList.Count; i++)
            {
                ParticleSystem ps = eod.particleSystemList[i];
                if (ps != null)
                {
                    ps.startSize /= _size;
                    ps.startSpeed /= _size;
                }
            }
        }
    }

    public void compareLife()
    {
        float life = 0;
        for (int i = 0; i < eod.particleSystemList.Count; i++)
        {
            ParticleSystem ps = eod.particleSystemList[i];
            float maxTime = ps.duration + ps.startLifetime;
            life = ps.startDelay + maxTime;
            if (life > lifeTime)
            {
                lifeTime = life;
                _checkPlayTimeIndex = i;
                checkType = 1;
                //Log.info(ps.name+" startDelay:" + ps.startDelay + " startLifetime:" + ps.startLifetime);
            }
            //                 ParticleSystemRenderer psr = ps.renderer as ParticleSystemRenderer;
            //                 if (psr.renderMode == ParticleSystemRenderMode.Mesh)
            //                 {
            //                 }
        }

        for (int k = 0; k < eod.ncCurveAnimationList.Count; k++)
        {
            NcCurveAnimation ncca = eod.ncCurveAnimationList[k];
            life = ncca.m_fDelayTime + ncca.m_fDurationTime;
            life *= _speed;
            if (life > lifeTime)
            {
                lifeTime = life;
                _checkPlayTimeIndex = k;
                checkType = 2;
            }
        }
        for (int k = 0; k < eod.ncSpriteAnimationList.Count; k++)
        {
            NcSpriteAnimation nc = eod.ncSpriteAnimationList[k];
            life = nc.m_fDelayTime + nc.GetDurationTime();
            life *= _speed;
            if (life > lifeTime)
            {
                lifeTime = life;
                _checkPlayTimeIndex = k;
                checkType = 3;
            }
        }
        for (int k = 0; k < eod.animationList.Count; k++)
        {
            Animation ani = eod.animationList[k];
            foreach (AnimationState aniS in ani)
            {
                life = aniS.length;
                _animationState = aniS;
                _animationState.wrapMode = WrapMode.ClampForever;
                break;
            }

            if (life > lifeTime)
            {
                lifeTime = life;
                _checkPlayTimeIndex = k;
                checkType = 4;
            }
        }
        //Log.info("lifeTime:" + _controller.lifeTime);
    }
}
