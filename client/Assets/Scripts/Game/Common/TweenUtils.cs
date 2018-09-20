using DG.Tweening;
using LuaInterface;
using System;
using UnityEngine;

/// <summary>
/// Tweener:它是用来控制动画和值的缓动变化 [继承 Tweener : Tween] 一个抽象类
/// Sequence: 特殊补间动画，代替控制动画和值，它是控制连续的补间动画的组合(groups) [ 继承 Sequence : Tween] 它是一个密封类（sealed）
/// Tween : 这里代表 一个Tweener 或者 一个Sequence
/// </summary>

public static class TweenUtils
{
	public static Tweener TweenFloat(float start, float end, float duration, LuaFunction OnUpdate)
	{
        
		return DOTween.To(() => start, x =>
		{
			try
			{
				OnUpdate.Call(x);
			}
			catch (Exception e)
			{
				Debug.LogError(e);
			}
		}
		, end, duration).SetEase(Ease.Linear).OnComplete(() =>
		{
			OnUpdate.Dispose();
			OnUpdate = null;
		});
	}
	public static Tweener TweenVector2(Vector2 start, Vector2 end, float duration, LuaFunction OnUpdate)
	{
		return DOTween.To(() => start, x =>
		{
			try
			{
				OnUpdate.Call(x);
			}
			catch (Exception e)
			{
				Debug.LogError(e);
			}
		}, end, duration).OnComplete(() =>
		{
			OnUpdate.Dispose();
			OnUpdate = null;
		});
	}
	public static Tweener TweenSpecialVector2(Vector2 start, Vector2 end, float duration, Ease ease, LuaFunction OnUpdate)
	{
		return DOTween.To(() => start, x =>
		{
			try
			{
				OnUpdate.Call(x);
			}
			catch (Exception e)
			{
				Debug.LogError(e);
			}
		}, end, duration).SetEase(ease).OnComplete(() =>
		{
			OnUpdate.Dispose();
			OnUpdate = null;
		});
	}
	public static Tweener TweenVector3(Vector3 start, Vector3 end, float duration, LuaFunction OnUpdate)
	{
		return DOTween.To(() => start, x => OnUpdate.Call(x), end, duration).OnComplete(() =>
		{
			OnUpdate.Dispose();
			OnUpdate = null;
		});
	}
	public static void SetEase(Tweener tweener, int ease)
	{
        tweener.SetEase((Ease)ease);
	}
	public static void OnComplete(Tweener tweener, LuaFunction func)
	{
		tweener.OnComplete(() =>
		{
			try
			{
				func.Call();
			}
			catch (Exception e)
			{
				Debug.LogError(e);
			}
			func.Dispose();
			func = null;
		});
	}
	public static void OnComplete(Tweener tweener, LuaFunction func, object self)
	{
		tweener.OnComplete(() =>
		{
			try
			{
				func.Call(self);
			}
			catch (Exception e)
			{
				Debug.LogError(e);
			}
			func.Dispose();
			func = null;
			if (self is LuaTable)
			{
				((LuaTable)self).Dispose();
				self = null;
			}
		});
	}
	public static void SetDelay(Tweener tweener, float delay)
	{
		tweener.SetDelay(delay);
	}
	public static void SetLoops(Tweener tweener, int loops)
	{
		tweener.SetLoops(loops);
	}
	public static void SetLoops(Tweener tweener, int loops, bool yoyo)
	{
		tweener.SetLoops(loops, yoyo ? LoopType.Yoyo : LoopType.Restart);
	}
	public static void SetTarget(Tweener tweener, object target)
	{
		tweener.SetTarget(target);
	}
	public static void SetTweenerId(Tweener tweener, object value){
		tweener.SetId(value);
	}
	public static object GetTweenerId(Tweener tweener){
		return tweener.id;
	}
	public static void SetDefaultAutoKill(bool value){
	 	DOTween.defaultAutoKill = value;
	}
	public static void SetAutoKill(Tweener tweener, bool value){
		tweener.SetAutoKill(value);
	}
	public static void Kill(Tweener tweener, bool complete=false){
		tweener.Kill(complete);
	}
    
    /// 动效每帧调用执行回调
    public static void OnUpdate(Tweener tweener, LuaFunction func)
    {
        tweener.OnUpdate(() =>
        {
            try
            {
                func.Call();
            }
            catch (Exception e)
            {
                Debug.LogError(e);
            }
            func.Dispose();
            func = null;
        });
    }
    public static void OnUpdate(Tweener tweener, LuaFunction func, object self)
    {
        tweener.OnUpdate(() =>
        {
            try
            {
                func.Call(self);
            }
            catch (Exception e)
            {
                Debug.LogError(e);
            }
            func.Dispose();
            func = null;
        });
    }

	//动画被销毁时触发。
	public static void OnKill(Tweener tweener, LuaFunction func)
	{
		tweener.OnKill(() =>
		{
			try
			{
				func.Call();
			}
			catch (Exception e)
			{
				Debug.LogError(e);
			}
			func.Dispose();
			func = null;
		});
	} 
	public static void OnKill(Tweener tweener, LuaFunction func, object self)
	{
		tweener.OnKill(() =>
		{
			try
			{
				func.Call(self);
			}
			catch (Exception e)
			{
				Debug.LogError(e);
			}
			func.Dispose();
			func = null;
			if (self is LuaTable)
			{
				((LuaTable)self).Dispose();
				self = null;
			}
		});
	}

    /// 对tween的lua回调处理
    public static void OnTweenCompleted(Tween t, LuaFunction func)
    {
        t.OnComplete(() =>
        {
            try
            {
                func.Call();
            }
            catch (Exception e)
            {
                Debug.LogError(e);
            }
            func.Dispose();
            func = null;
        });
        t.SetAutoKill(true);
    }
    public static void OnTweenCompleted(Tween t, LuaFunction func, object self)
    {
        t.OnComplete(() =>
        {
            try
            {
                func.Call(self);
            }
            catch (Exception e)
            {
                Debug.LogError(e);
            }
            func.Dispose();
            func = null;
        });
        t.SetAutoKill(true);
    }
    // 跳跃动效, "transform":目标, "endValue":目标位置, "duration":时间, "jumpNum":期间跳N次,"ease":动效值
    public static Sequence DoJump(Transform transform, Vector3 endValue, float duration, int jumpNum, int ease)
    {

        Sequence t = ShortcutExtensions.DOJump(transform, endValue, jumpNum, duration);
        t.SetEase((Ease)ease);
        return t;
    }
    //创建一个动效序列
    public static Sequence CreateSequence()
    {
        return DOTween.Sequence();
    }
    //在Sequence的最后添加一个tween。
    public static Sequence AppendTween(Sequence sequence, Tween tween)
    {
        return sequence.Append(tween);
    }
    //在Sequence的最后添加一个回调函数。
    public static Sequence AppendCallback(Sequence sequence, LuaFunction func)
    {
        return sequence.AppendCallback(() =>
        {
            try
            {
                func.Call();
            }
            catch (Exception e)
            {
                Debug.LogError(e);
            }
            func.Dispose();
            func = null;
        });
    }
    public static Sequence AppendCallback(Sequence sequence, LuaFunction func, object self)
    {
        return sequence.AppendCallback(() =>
        {
            try
            {
                func.Call(self);
            }
            catch (Exception e)
            {
                Debug.LogError(e);
            }
            func.Dispose();
            func = null;
        });
    }
    //在Sequence的最后添加一段时间间隔。
    public static Sequence AppendInterval(Sequence sequence, float interval)
    {
        return sequence.AppendInterval(interval);
    }
    //在给定的时间位置上放置一个tween，可以实现同时播放多个tween的效果，而不是一个接着一个播放。
    public static Sequence Insert(Sequence sequence, float time, Tween tween)
    {
        return sequence.Insert(time, tween);
    }
    //在给定的时间位置上放置一个回调函数。
    public static Sequence InsertCallback(Sequence sequence, float time, LuaFunction func)
    {
        return sequence.InsertCallback(time, () =>
        {
            try
            {
                func.Call();
            }
            catch (Exception e)
            {
                Debug.LogError(e);
            }
            func.Dispose();
            func = null;
        });
    }
    public static Sequence InsertCallback(Sequence sequence, float time, LuaFunction func, object self)
    {
        return sequence.InsertCallback(time, () =>
        {
            try
            {
                func.Call(self);
            }
            catch (Exception e)
            {
                Debug.LogError(e);
            }
            func.Dispose();
            func = null;
        });
    }
    //在Sequence的最后一个tween的开始处放置一个tween。
    public static Sequence Join(Sequence sequence, Tween tween)
    {
        return sequence.Join(tween);
    }
    //在Sequence开始处插入一个tween，原先的内容根据时间往后移。
    public static Sequence Prepend(Sequence sequence, Tween tween)
    {
        return sequence.Prepend(tween);
    }
    //在Sequence开始处插入一个回调函数。
    public static Sequence PrependCallback(Sequence sequence, LuaFunction func)
    {
        return sequence.PrependCallback(() =>
        {
            try
            {
                func.Call();
            }
            catch (Exception e)
            {
                Debug.LogError(e);
            }
            func.Dispose();
            func = null;
        });
    }
    public static Sequence PrependCallback(Sequence sequence, LuaFunction func, object self)
    {
        return sequence.PrependCallback(() =>
        {
            try
            {
                func.Call(self);
            }
            catch (Exception e)
            {
                Debug.LogError(e);
            }
            func.Dispose();
            func = null;
        });
    }
    //在Sequence开始处插入一段时间间隔，原先的内容根据时间往后移。
    public static Sequence PrependInterval(Sequence sequence, float interval)
    {
        return sequence.PrependInterval(interval);
    }
    // 设置循环 LoopType:  Restart = 0,  Yoyo = 1, Incremental = 2,
    public static Tween SetLoops(Tween tween, int loops, int loopType) 
    {
        return tween.SetLoops(loops, (LoopType)loopType).SetSpeedBased();;
    }

    #region 振动
    public static Tweener DOShakePosition(Transform target, float duration, float strength = 1f, int vibrato = 10, float randomness = 90f, bool snapping = false)
    {
        return ShortcutExtensions.DOShakePosition(target, duration, strength, vibrato, randomness, snapping);
    }
    public static Tweener DOShakePosition(Transform target, float duration, Vector3 strength, int vibrato = 10, float randomness = 90f, bool snapping = false)
    {
        return ShortcutExtensions.DOShakePosition(target, duration, strength, vibrato, randomness, snapping);
    }
    public static Tweener DOShakeRotation(Transform target, float duration, float strength = 90f, int vibrato = 10, float randomness = 90f)
    {
        return ShortcutExtensions.DOShakeRotation(target, duration, strength, vibrato, randomness);
    }
    public static Tweener DOShakeRotation(Transform target, float duration, Vector3 strength, int vibrato = 10, float randomness = 90f)
    {
        return ShortcutExtensions.DOShakeRotation(target, duration, strength, vibrato, randomness);
    }
    public static Tweener DOShakeScale(Transform target, float duration, float strength = 1f, int vibrato = 10, float randomness = 90f)
    {
        return ShortcutExtensions.DOShakeScale(target, duration, strength, vibrato, randomness);
    }
    public static Tweener DOShakeScale(Transform target, float duration, Vector3 strength, int vibrato = 10, float randomness = 90f)
    {
        return ShortcutExtensions.DOShakeScale(target, duration, strength, vibrato, randomness);
    }
    #endregion

    #region 冲压机
    //冲压机，在 n 秒内在原始坐标和下面坐标之间，来回冲压
    public static Tweener DOPunchPosition(Transform target, Vector3 punch, float duration, int vibrato = 10, float elasticity = 1f)
    {
        return ShortcutExtensions.DOPunchPosition(target, punch, duration, vibrato, elasticity);
    }
    //冲压机，在 n 秒内在原始旋转和下面角度之间，来回冲压变化
    public static Tweener DOPunchRotation(Transform target, Vector3 punch, float duration, int vibrato = 10, float elasticity = 1f)
    {
        return ShortcutExtensions.DOPunchRotation(target, punch, duration, vibrato, elasticity);
    }
    //冲压机，在 n 秒内在原始比例和下面比例之间，来回冲压变化
    public static Tweener DOPunchScale(Transform target, Vector3 punch, float duration, int vibrato = 10, float elasticity = 1f)
    {
        return ShortcutExtensions.DOPunchScale(target, punch, duration, vibrato, elasticity);
    }
    #endregion
}
