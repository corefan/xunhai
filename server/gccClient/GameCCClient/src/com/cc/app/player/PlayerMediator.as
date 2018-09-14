package com.cc.app.player {
    import com.cc.app.main.MainModel;
    import com.cc.app.player.model.PlayerModel;
    import com.cc.core.constant.OptTypeConstant;
    import com.cc.core.net.GameCCSocket;
    import com.cc.core.util.MessageUtil;
    
    import flash.utils.ByteArray;
    
    import mx.utils.StringUtil;
    
    import org.robotlegs.mvcs.Mediator;

    public class PlayerMediator extends Mediator {
        [Inject]
        public var player:Player;

        public function PlayerMediator() {
            super();
        }

        override public function onRegister():void {
            super.onRegister();
            addViewListener(PlayerEvent.SEARCH_PLAYER, onSearchPlayer);

            addViewListener(PlayerEvent.SEARCH_PLAYER_DATA_LOG, onPlayerDataLogCheck);

            addContextListener(PlayerEvent.FENG_HAO, onfenghao);
            addContextListener(PlayerEvent.JIE_FENG, onjiefeng);
            addContextListener(PlayerEvent.DELETE_CACHE, onDeleteCache);
            addContextListener(PlayerEvent.UPDATE_PLYAER_ROLE, onPlayerRole);

            addContextListener(PlayerEvent.JIN_YAN, onJinYan); // 禁言
            addContextListener(PlayerEvent.JIE_JIN, onJieJin); // 解禁
			
			addCMDListener();
			
        }

        override public function onRemove():void {
            super.onRemove();
            removeViewListener(PlayerEvent.SEARCH_PLAYER, onSearchPlayer);

            removeViewListener(PlayerEvent.SEARCH_PLAYER_DATA_LOG, onPlayerDataLogCheck);

            removeContextListener(PlayerEvent.FENG_HAO, onfenghao);
            removeContextListener(PlayerEvent.JIE_FENG, onjiefeng);
            removeContextListener(PlayerEvent.DELETE_CACHE, onDeleteCache);
            removeContextListener(PlayerEvent.UPDATE_PLYAER_ROLE, onPlayerRole);
            
            removeContextListener(PlayerEvent.JIN_YAN, onJinYan); // 禁言
            removeContextListener(PlayerEvent.JIE_JIN, onJieJin); // 解禁
			
			removeCMDListener();
        }
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.PLAYER_2, onSearchPlayerBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.PLAYER_3, onfenghaoBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.PLAYER_4, onjiefengBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.PLAYER_5, onDeleteCacheBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.PLAYER_6, onPlayerBagItemCheckBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.PLAYER_7, onPlayerLoginCheckBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.PLAYER_15, onPlayerRoleBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.PLAYER_17, onJinYanBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.PLAYER_18, onJieJinBack);
			
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.PLAYER_2, onSearchPlayerBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.PLAYER_3, onfenghaoBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.PLAYER_4, onjiefengBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.PLAYER_5, onDeleteCacheBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.PLAYER_6, onPlayerBagItemCheckBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.PLAYER_7, onPlayerLoginCheckBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.PLAYER_15, onPlayerRoleBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.PLAYER_17, onJinYanBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.PLAYER_18, onJieJinBack);
			
		}

        /**
         * 封号
         * */
        private function onfenghao(e:PlayerEvent):void {
			var data:Object = new Object();
			data.gameSite = MainModel.instance.currentGameSite;
			if(data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			data.playerID = e.data;
			data.optType = OptTypeConstant.PLAYER_3;
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.PLAYER_3, param.toString());
        }
		
		/**
		 * 封号返回
		 * */
		private function onfenghaoBack(data:ByteArray, mid:int):void {
			if (data) {
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				var resultFlag:int = result.resultFlag;
				if (resultFlag == 1) {
					MessageUtil.showFaultMessage(result.resultMsg);
					player.searchPlayer();
				} else if (resultFlag == 0) {
					MessageUtil.showFaultMessage(result.resultMsg);
				}
			} 
		}

        /**
         * 解封
         * */
        private function onjiefeng(e:PlayerEvent):void {
			var data:Object = new Object();
			data.gameSite = MainModel.instance.currentGameSite;
			if(data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			data.playerID = e.data;
			data.optType = OptTypeConstant.PLAYER_4;
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.PLAYER_4, param.toString());
        }
		
		/**
		 * 解封返回
		 * */
		private function onjiefengBack(data:ByteArray, mid:int):void {
			if (data) {
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				var resultFlag:int = result.resultFlag;
				if (resultFlag == 1) {
					MessageUtil.showFaultMessage(result.resultMsg);
					player.searchPlayer();
				} else if (resultFlag == 0) {
					MessageUtil.showFaultMessage(result.resultMsg);
				}
			} 
		}

        /**
         * 删除玩家缓存
         * */
        private function onDeleteCache(e:PlayerEvent):void {
			var data:Object = new Object();
			data.gameSite = MainModel.instance.currentGameSite;
			if(data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			data.optType = OptTypeConstant.PLAYER_5;
			data.playerID = e.data;
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.PLAYER_5, param.toString());
        }
		
		/**
		 * 删除玩家缓存返回
		 * */
		private function onDeleteCacheBack(data:ByteArray, mid:int):void {
			if (data) {
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				var resultFlag:int = result.resultFlag;
				if (resultFlag == 1) {
					MessageUtil.showFaultMessage(result.resultMsg);
					player.searchPlayer();
				} else if (resultFlag == 0) {
					MessageUtil.showFaultMessage(result.resultMsg);
				}
			} 
		}

        /**
         * 查询玩家
         * */
        private function onSearchPlayer(e:PlayerEvent):void {
			
			e.data.gameSite = MainModel.instance.currentGameSite;
			if(e.data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			var param:Object = JSON.stringify(e.data);
			GameCCSocket.instance.send(OptTypeConstant.PLAYER_2, param.toString());
			
        }

        /**
         * 查询玩家返回
         * */
        private function onSearchPlayerBack(data:ByteArray, mid:int):void {
            if (data) {
//				var str:String = data.readMultiByte(data.length, "utf8");
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
                var result:Object = JSON.parse(str);
                PlayerModel.instance.port = result.gamePort;
                PlayerModel.instance.host = result.host;
//                PlayerModel.instance.site = result.gameSite;
                PlayerModel.instance.assets = result.assert;
                if (result.size <= 0) {
                    MessageUtil.showFaultMessage("没有查到玩家数据");
                }
                player.onSearchPlayer(result.playerList);
            } else {
                MessageUtil.showFaultMessage("没有查到玩家数据");
            }
        }

		/**
		 * 查询玩家日常数据日志记录
		 * */
		private function onPlayerDataLogCheck(e:PlayerEvent):void {
			var data:Object = new Object();
			data.gameSite = MainModel.instance.currentGameSite;
			if(data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			data.optType = e.searchType;
			data.playerID = e.data.playerID;
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(e.searchType, param.toString());
		}
		
        /**
         * 查询玩家背包物品返回
         * */
        private function onPlayerBagItemCheckBack(data:ByteArray, mid:int):void {
            if (data) {
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
                if (str == "{}") {
                    MessageUtil.showFaultMessage("玩家背包数据异常");
                } else {
                    var result:Object = JSON.parse(str);
                    player.setPlayerBagItemInfo(result);
                }
            }
        }
		
		/**
		 * 查询玩家登陆日志返回
		 * */
		private function onPlayerLoginCheckBack(data:ByteArray, mid:int):void {
			if (data) {
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				player.setPlayerLoginInfo(result);
			} else {
				MessageUtil.showFaultMessage("玩家无登陆日志");
			}
			
		}


        private function onPlayerRole(e:PlayerEvent):void {
			var data:Object = new Object();
			data.gameSite = MainModel.instance.currentGameSite;
			if(data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			data.optType = OptTypeConstant.PLAYER_15;
			data.playerID = e.data.playerID;
			data.name = e.data.name;
			data.playerType = e.data.playerType;
			data.typeName = e.data.typeName;
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.PLAYER_15, param.toString());
        }

        private function onPlayerRoleBack(data:ByteArray, mid:int):void {
            if (data) {
                player.searchPlayer();
            }
        }

        private function onJinYan(e:PlayerEvent):void {
			var data:Object = new Object();
			data.gameSite = MainModel.instance.currentGameSite;
			if(data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			data.optType = OptTypeConstant.PLAYER_17;
			data.playerID = e.data.playerID;
			data.banTime = e.data.banTime;
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.PLAYER_17, param.toString());
        }
        
        private function onJinYanBack(data:ByteArray, mid:int):void {
            if (data) {
                player.searchPlayer();
            }
        }
        
        private function onJieJin(e:PlayerEvent):void {
			var data:Object = new Object();
			data.gameSite = MainModel.instance.currentGameSite;
			if(data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			data.optType = OptTypeConstant.PLAYER_18;
			data.playerID = e.data;
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.PLAYER_18, param.toString());
        }
        
        private function onJieJinBack(data:ByteArray, mid:int):void {
            if (data) {
                player.searchPlayer();
            }
        }
    }
}
