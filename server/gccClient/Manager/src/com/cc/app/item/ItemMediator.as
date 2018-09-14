package com.cc.app.item {
    import com.cc.app.item.event.ItemEvent;
    import com.cc.app.item.model.ItemModel;
    import com.cc.app.main.Main;
    import com.cc.app.main.MainModel;
    import com.cc.app.player.PlayerEvent;
    import com.cc.core.conf.Config;
    import com.cc.core.conf.data.ItemConf;
    import com.cc.core.constant.OptTypeConstant;
    import com.cc.core.net.GameCCSocket;
    import com.cc.core.util.MessageUtil;
    
    import flash.utils.ByteArray;
    
    
    import mx.collections.ArrayCollection;
    import mx.rpc.AsyncToken;
    import mx.rpc.events.ResultEvent;
    import mx.rpc.http.mxml.HTTPService;
    import mx.utils.StringUtil;
    
    import org.robotlegs.mvcs.Mediator;

    public class ItemMediator extends Mediator {

        [Inject]
        public var item:Item;

        override public function onRegister():void {
            super.onRegister();
            addContextListener(ItemEvent.FANYE, getItemList);
            addContextListener(ItemEvent.SEND_ITEM, onSendItem);
            addContextListener(ItemEvent.SEND_ITEM_TO_ALL, onSendItemToAll);
            addContextListener(ItemEvent.SELECT_PLAYER, onSelectPlayer);

            addContextListener(ItemEvent.SEND_ITEM_LIST, onSendItemListHandler); //待发送物品列表
            addContextListener(ItemEvent.SAVE_SEND_ITEMINFO, submitSendItemApply); //保存发送物品信息
            addContextListener(ItemEvent.CHECK_ITEM_BYNAME, onCheckItemHandler); //查询物品

            addContextListener(PlayerEvent.SEARCH_PLAYER, onSearchPlayer);

			addCMDListener();

            initView();
        }

        override public function onRemove():void {
            super.onRemove();
            removeContextListener(ItemEvent.FANYE, getItemList);
            removeContextListener(ItemEvent.SEND_ITEM, onSendItem);
            removeContextListener(ItemEvent.SEND_ITEM_TO_ALL, onSendItemToAll);
            removeContextListener(ItemEvent.SELECT_PLAYER, onSelectPlayer);

            removeContextListener(ItemEvent.SEND_ITEM_LIST, onSendItemListHandler); //待发送物品列表
            removeContextListener(ItemEvent.SAVE_SEND_ITEMINFO, submitSendItemApply); //保存发送物品信息
            removeContextListener(ItemEvent.CHECK_ITEM_BYNAME, onCheckItemHandler); //查询物品

            removeContextListener(PlayerEvent.SEARCH_PLAYER, onSearchPlayer);
			
			removeCMDListener();
        }
		
		public function addCMDListener():void{
			GameCCSocket.instance.addCmdListener(OptTypeConstant.ITEM_1, onGetItemListBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.ITEM_4, submitSendItemApplyBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.ITEM_7, onCheckItemHandlerBack);
			GameCCSocket.instance.addCmdListener(OptTypeConstant.PLAYER_10, onSearchPlayerBack);
		}
		
		public function removeCMDListener():void {
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.ITEM_1, onGetItemListBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.ITEM_4, submitSendItemApplyBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.ITEM_7, onCheckItemHandlerBack);
			GameCCSocket.instance.removeCmdListener(OptTypeConstant.PLAYER_10, onSearchPlayerBack);
		}

        public function ItemMediator() {
            super();
        }

        /**
         * 初始化
         * */
        private function initView():void {
            getItemList();
            item.initView();
        }

        private function getItemList():void {
            var data:Object = new Object();
            data.optType = OptTypeConstant.ITEM_1;
            data.gameSite = MainModel.instance.currentGameSite;
			if(data.gameSite == ""){
				MessageUtil.showFaultMessage("请选择服务器");
				return;
			}
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.ITEM_1, param.toString());
        }

        private function onGetItemListBack(data:ByteArray, mid:int):void {
            if (data) {
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				
                var itemList:Array = JSON.parse(String(result.pageList)) as Array;
                for each (var o:Object in itemList) {
                    o.num = 0;
                    o.checked = false;
                }
                ItemModel.instance.itemConfList = itemList;
                item.updateItemInfo();
            }
        }

        /**
         * 发送物品
         * */
        private function onSendItem(event:ItemEvent):void {
            var data:Object = new Object();
            data.optType = OptTypeConstant.ITEM_2;
            data.gameSite = event.data.gameSite;
            data.itemList = JSON.stringify(MainModel.instance.sendItemList);
            data.receiveNames = MainModel.instance.receiveNames;
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.ITEM_2, param.toString());
        }

        /**
         * 全服发送
         * */
        private function onSendItemToAll(event:ItemEvent):void {
            var data:Object = new Object();
            data.optType = OptTypeConstant.ITEM_3;
            data.title = event.data.title;
            data.content = event.data.content;
            data.gameSite = event.data.gameSite;
            data.anex = JSON.stringify(MainModel.instance.sendItemList);
			var param:Object = JSON.stringify(data);
			GameCCSocket.instance.send(OptTypeConstant.ITEM_3, param.toString());
        }

        /**
         * 查询玩家
         * */
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
         * */
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

                item.stepOneView.playerItemView.myDataGrid.dataProvider = list;
            }
        }

        private function onSelectPlayer(e:ItemEvent):void {
            var ac:ArrayCollection = e.data as ArrayCollection;

            if (item.stepOneView.playerDg.dataProvider != null) {
                var oldDataArr:Array = item.stepOneView.playerDg.dataProvider.toArray();
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
            for each (var obj:Object in ac) {
                names += (obj.name + ";");
            }
            MainModel.instance.receiveNames = names;

            item.stepOneView.playerDg.dataProvider = ac;
        }

        private function onSendItemListHandler(e:ItemEvent):void {
            var ac:ArrayCollection = e.data as ArrayCollection;

            item.stepOneView.itemList = [];
            for each (var obj:Object in ac) {
                item.stepOneView.itemList.push({itemId: obj.id, type: obj.type, itemNum: obj.num});
            }
            item.updateSendItemInfo(ac);
        }

        private function submitSendItemApply(e:ItemEvent):void {
            var obj:Object = new Object();
            obj.optType = OptTypeConstant.ITEM_4;
            obj.appType = e.data.appType;
            obj.receiveNames = e.data.receiveNames;
            obj.itemList = JSON.stringify(e.data.itemList);
            obj.title = e.data.title;
            obj.content = e.data.content;
            obj.reason = e.data.reason;
            
			if (MainModel.instance.currentModel == 3) {
				obj.agent = MainModel.instance.agent;
				obj.gameSite = "";
			} else if (MainModel.instance.currentModel == 2) {
				obj.agent = MainModel.instance.currentAgent;
				obj.gameSite = "";
			} else {
				if(MainModel.instance.currentGameSite == ""){
					MessageUtil.showFaultMessage("请选择服务器");
					return;
				}
                obj.agent = MainModel.instance.currentAgent;
                obj.gameSite = MainModel.instance.currentGameSite;
            }
			
			var param:Object = JSON.stringify(obj);
			GameCCSocket.instance.send(OptTypeConstant.ITEM_4, param.toString());
        }

        private function submitSendItemApplyBack(data:ByteArray, mid:int):void {
			if(data){
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				if(result.hasOwnProperty("exceptionStr")){
					MessageUtil.showFaultMessage(result.exceptionStr, true);
				}else{
					MessageUtil.showFaultMessage("成功保存发送物品信息，请审核发送");
				}
			}
            item.clearTitleText();
        }

        private function onCheckItemHandler(e:ItemEvent):void {
            var obj:Object = new Object();
            obj.optType = OptTypeConstant.ITEM_7;
            obj.itemContent = e.data;

			var param:Object = JSON.stringify(obj);
			GameCCSocket.instance.send(OptTypeConstant.ITEM_7, param.toString());
        }

        private function onCheckItemHandlerBack(data:ByteArray, mid:int):void {
            if (data) {
				var str:String = data.readUTFBytes(data.length);
				str = StringUtil.trim(str);
				var result:Object = JSON.parse(str);
				
                item.stepOneView.addItemView.itemArr = JSON.parse(result.itemList) as Array;
                var list:ArrayCollection = new ArrayCollection();
                for each (var obj:Object in item.stepOneView.addItemView.itemArr) {
//					obj.num = 0;
//					obj.checked = false;
                    list.addItem(obj);
                }

                if (list.length == 0) {
                    MessageUtil.showFaultMessage("该物品不存在");
                }

                item.stepOneView.addItemView.itemDG.dataProvider = list;
            }
        }

    }
}
