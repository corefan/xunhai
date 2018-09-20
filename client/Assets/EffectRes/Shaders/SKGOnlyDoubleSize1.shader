// 功  能： 双面
// 时  间： 2016-09-11 12:59:18
// 作  者： zwx
// E-mail： zhuang_wx@qq.com
Shader "A_SKG/OnlyDoubleSize1" 
{
	Properties 
	{
		_Color ("Color", Color) = (1.0,1.0,1.0,1)
		_MainTex ("Albedo (RGB)", 2D) = "white" {}
	}
	
	SubShader 
	{
		Tags { "Queue" = "Transparent" }
		Material
		{
			Diffuse[_Color]
		} 
		Pass
		{
			Cull off
			Blend SrcAlpha OneMinusSrcAlpha
			color[_Color]
			SetTexture[_MainTex]
			{
				Combine texture * primary // Combine texture * primary DOUBLE
			}
		}
    }
	FallBack "Diffuse"
}