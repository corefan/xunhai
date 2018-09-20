-- 游戏中的常量

GameConst = {}
GameConst.ViewType = 0 --0:主界面 1:战斗界面
GameConst.heartCD = 5 -- 心跳
GameConst.FrameRate = 0.01666666667 -- 1/60

GameConst.GameName = AppConst.GameName
GameConst.Debug = (AppConst.DebugEngine == true or AppConst.DebugEngine == 1)
GameConst.ApplePay = false
GameConst.GId = AppConst.GameId -- 游戏平台返回id
GameConst.SId = AppConst.SubGameId -- 游戏平台返回子id

-- 平台
PhonePlat = AppConst.PlatId -- 手机平台
XH = "XH" == PhonePlat -- 迅海平台
SHENHE = "SHENHE" == PhonePlat -- 审核服
DONGHAI = "DONGHAI" == PhonePlat -- 东海运营平台
YUNYOU = "YUNYOU" == PhonePlat -- 云游运营平台

if XH then-- 迅海平台
	GameConst.IP = "sdk.171game.com"
	GameConst.WebPost = "8000"
	GameConst.Debug = true
elseif SHENHE then -- 审核服
	GameConst.IP = "sdk.171game.com"
	GameConst.WebPost = "8000"

elseif DONGHAI then --东海运营平台
	GameConst.IP = "sdk.171game.com"
	GameConst.WebPost = "8001"
	GameConst.ApplePay = true

elseif YUNYOU then  --云游运营平台
	GameConst.IP = "sdk.171game.com"
	GameConst.WebPost = "8002"
	GameConst.ApplePay = true

else 										-- 本地开发
	GameConst.IP = "192.168.0.200"
	GameConst.WebPost = "8000"
	GameConst.Debug = true -- 开启调试模式(不同于cs，这里只是开些一lua的状态处理)[true开，false关]
end

GameConst.RegistURL = string.format("http://%s:%s%s" , GameConst.IP , GameConst.WebPost , "/register") -- 账户注册
GameConst.LoginURL = string.format("http://%s:%s%s" , GameConst.IP , GameConst.WebPost , "/login")     -- 账户登录
GameConst.VisitorBindURL = string.format("http://%s:%s%s" , GameConst.IP , GameConst.WebPost , "/binding") --游客绑定
GameConst.GetbackPasswordURL = string.format("http://%s:%s%s" , GameConst.IP , GameConst.WebPost , "/retPassword") --找回密码
GameConst.ResetPasswordURL = string.format("http://%s:%s%s" , GameConst.IP , GameConst.WebPost , "/changePassword") --修改密码

GameConst.PRINT_PROTO = GameConst.Debug -- 打印收发协议消息

GameConst.scaleX = UnityEngine.Screen.width/layerMgr.WIDTH
GameConst.scaleY = UnityEngine.Screen.height/layerMgr.HEIGHT

GameConst.PI2 = 2*math.pi

GameConst.defaultFont = "方正粗圆简体"

GameConst.USE_PRELOAD = not GameConst.Debug -- 使用预加载