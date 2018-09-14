package com.cc.app.player {
    import com.cc.app.main.MainModel;
    import com.cc.core.constant.OptTypeConstant;
    import com.cc.core.net.GameCCSocket;
    import com.cc.core.util.MessageUtil;
    
    import flash.utils.ByteArray;
    
    import mx.collections.ArrayCollection;
    import mx.utils.StringUtil;
    
    import org.robotlegs.mvcs.Mediator;

    public class SendEmailMediator extends Mediator {
        [Inject]
        public var sendEmail:SendEmail;

        public function SendEmailMediator() {
            super();
        }

        override public function onRegister():void {
            super.onRegister();
            addContextListener(PlayerEvent.SEND_EMAIL, onSendEmail); // 发送邮件
            addContextListener(PlayerEvent.EMAIL_SEARCH_PLAYER, onSearchPlayer); // 查找玩家
            addContextListener(PlayerEvent.EMAIL_SELECT_PLAYER, onSelectPlayer); //选择玩家
			addCMDListener();
            initView();
        }

        private function initView():void {
        }

        override public function onRemove():void {
            super.onRemove();
            removeContextListener(PlayerEvent.SEND_EMAIL, onSendEmail); // 发送邮件
            removeContextListener(PlayerEvent.EMAIL_SEARCH_PLAYER, onSearchPlayer); // 查找玩家
            removeContextListener(PlayerEvent.EMAIL_SELECT_PLAYER, onSelectPlayer); //选择玩家
			removeCMDListener();
        }
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.ITEM_8, onSendEmailBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.PLAYER_10, onSearchPlayerBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.ITEM_8, onSendEmailBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.PLAYER_10, onSearchPlayerBack);
		}

        /**
         * 查询玩家
         **/
        private function onSearchPlayer(e:PlayerEvent):void {
            var data:Object = e.data;
            data.optType = OptTypeConstant.PLAYER_10;
            data.gameSite = MainModel.instance.currentGameSite;
			if(data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.PLAYER_10, param.toString());
        }

        /**
         * 查询玩家返回
         **/
        private function onSearchPlayerBack(data:ByteArray, mid:int):void {
            if (data) {
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);

                var arr:Array = JSON.parse(result.playerList) as Array;
                var list:ArrayCollection = new ArrayCollection();
                for each (var obj:Object in arr) {
                    obj.isSelect = false;
                    list.addItem(obj);
                }

                sendEmail.playerView.myDataGrid.dataProvider = list;
            }
        }
        
        private function onSelectPlayer(e:PlayerEvent):void {
            var ac:ArrayCollection = e.data as ArrayCollection;
            
            if (sendEmail.playerDg.dataProvider != null) {
                var oldDataArr:Array = sendEmail.playerDg.dataProvider.toArray();
                var newDataArr:Array = ac.source;
                
                for (var i:int = 0; i < newDataArr.length; i++) {
                    var existed:Boolean = false;
                    for (var j:int = 0; j < oldDataArr.length; j++) {
                        if (newDataArr[i].name == oldDataArr[j].name) {
                            existed = true;
                            break;
                        }
                    }
                    if (existed == false) {
                        oldDataArr.push(newDataArr[i]);
                    } 
                }
                ac.source = oldDataArr;
            }
            
            var names:String = "";
            var ids:String = "";
            for each (var obj:Object in ac) {
                names += (obj.name + "，");
                ids += (obj.playerId + ",");
            }
            if (names.length > 0) {
                names = names.substring(0, names.length - 1);
                ids = ids.substring(0, ids.length - 1);
            }
            MainModel.instance.emailReceiveNames = names;
            MainModel.instance.emailReceiveIds = ids;
            sendEmail.playerDg.dataProvider = ac;
        }

        private function onSendEmail(e:PlayerEvent):void {
            var data:Object = new Object();
            data.optType = OptTypeConstant.ITEM_8;
            data.receivers = e.data.receivers;
            data.mailContent = e.data.content;
			if (MainModel.instance.currentModel == 3) {
				data.agent = MainModel.instance.agent;
				data.gameSite = "";
			} else if (MainModel.instance.currentModel == 2) {
				data.agent = MainModel.instance.currentAgent;
				data.gameSite = "";
			} else {
				if(MainModel.instance.currentGameSite == ""){
					MessageUtil.showFaultMessage("请选择服务器");
					return;
				}
                data.agent = MainModel.instance.currentAgent;
                data.gameSite = MainModel.instance.currentGameSite;
            }
			
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.ITEM_8, param.toString());
            
        }

        private function onSendEmailBack(data:ByteArray, mid:int):void {
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				if(result.hasOwnProperty("exceptionStr")){
					MessageUtil.showFaultMessage(result.exceptionStr, true);
				}else{
		            MessageUtil.showFaultMessage("发送邮件成功！");
				}
		    }
		}
	}
}
