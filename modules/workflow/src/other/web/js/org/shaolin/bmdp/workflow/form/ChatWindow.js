/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_ChatWindow(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var serverURLUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "serverURLUI"]
    });

    var taskIdUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "taskIdUI"]
    });

    var orgIdUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "orgIdUI"]
    });

    var sentPartyIdUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "sentPartyIdUI"]
    });

    var receivedPartyIdUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "receivedPartyIdUI"]
    });

    var isAbcUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "isAbcUI"]
    });

    var sessionIdUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "sessionIdUI"]
    });

    var sentPartyNameUI = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "sentPartyNameUI"]
    });

    var receivedPartyNameUI = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "receivedPartyNameUI"]
    });

    var messageUI = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "messageUI"]
        ,utype: "swiper"
        ,skipEmptyRawMessage: true
        ,style: "overflow-y: scroll;overflow-x: hidden;"
        ,appendRowMode: true
    });

    var enterMessageUI = new UIMaster.ui.textarea
    ({
        ui: elementList[prefix + "enterMessageUI"]
        ,emojiSupport: true
        ,style: "width:100%;height:60px;"
    });

    var okbtn = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "okbtn"]
    });

    var cancelbtn = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "cancelbtn"]
    });

    var actionPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "actionPanel"]
        ,items: []
        ,subComponents: [prefix + "okbtn",prefix + "cancelbtn"]
    });

    var topPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "topPanel"]
        ,items: []
        ,subComponents: [prefix + "sentPartyNameUI",prefix + "receivedPartyNameUI"]
    });

    var fieldPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "fieldPanel"]
        ,items: []
        ,subComponents: [prefix + "topPanel",prefix + "messageUI",prefix + "enterMessageUI"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [serverURLUI,taskIdUI,orgIdUI,sentPartyIdUI,receivedPartyIdUI,isAbcUI,sessionIdUI,sentPartyNameUI,receivedPartyNameUI,messageUI,enterMessageUI,okbtn,cancelbtn,fieldPanel,topPanel,actionPanel]
    });

    Form.serverURLUI=serverURLUI;

    Form.taskIdUI=taskIdUI;

    Form.orgIdUI=orgIdUI;

    Form.sentPartyIdUI=sentPartyIdUI;

    Form.receivedPartyIdUI=receivedPartyIdUI;

    Form.isAbcUI=isAbcUI;

    Form.sessionIdUI=sessionIdUI;

    Form.sentPartyNameUI=sentPartyNameUI;

    Form.receivedPartyNameUI=receivedPartyNameUI;

    Form.messageUI=messageUI;

    Form.enterMessageUI=enterMessageUI;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.fieldPanel=fieldPanel;

    Form.topPanel=topPanel;

    Form.sentPartyNameUI=sentPartyNameUI;

    Form.receivedPartyNameUI=receivedPartyNameUI;

    Form.messageUI=messageUI;

    Form.enterMessageUI=enterMessageUI;

    Form.topPanel=topPanel;

    Form.sentPartyNameUI=sentPartyNameUI;

    Form.receivedPartyNameUI=receivedPartyNameUI;

    Form.actionPanel=actionPanel;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_form_ChatWindow */

        
       var isAbc = this.isAbcUI.value;
       var sessionId = this.sessionIdUI.value;
       if (sessionId == null || sessionId == "null") {
           sessionId = "";
       }
       if (this.serverURLUI.value == "" || this.serverURLUI.value == "null") {
          alert("Error: server url does not specify.");
          return;
       }
       var partyId = this.sentPartyIdUI.value;
       var fromPartyId = this.sentPartyIdUI.value;
       var toPartyId = this.receivedPartyIdUI.value;
       $(this.messageUI).focus();
       var msgContainer = this.messageUI;
       var o = this;
       UIMaster.require("/js/socket.io.js");
       this.nodesocket = io.connect(this.serverURLUI.value);
       this.nodesocket.on('connect', function(e) {
            var msg = {partyId: partyId, isAbc: isAbc, sessionId: sessionId};
            o.nodesocket.emit('register', msg)
       });
       this.nodesocket.on('loginSuccess', function(e) {
            var msg = {fromPartyId: fromPartyId, toPartyId: toPartyId, sessionId: sessionId};
            o.nodesocket.emit('history', msg);
       });
       this.nodesocket.on('alreadyLogined', function(e) {
            var msg = {fromPartyId: fromPartyId, toPartyId: toPartyId, sessionId: sessionId};
            o.nodesocket.emit('history', msg);
       });
       this.nodesocket.on('loginFail', function(e) {
            $(this.enterMessageUI).attr("disabled", "disabled");
       });
       this.nodesocket.on('history', function(e) {
            for (var i=0;i<e.length;i++) {
                var color = ((i%2==0)?"uimaster_chat_item_even":"uimaster_chat_item_old");
	            var row = "<div class=\"swiper-slide uimaster_chat_item_to "+color+"\"><div><div class=\"uimaster_chat_time\">"
					 + e[i].CREATEDATE + "</div><div class=\"uimaster_chat_message\"> " + e[i].MESSAGE + "</div></div></div>"
	            msgContainer.appendSlide($(row));
            }
       });
       this.nodesocket.on('chatTo', function(e) {
            var color = ((msgContainer.children[0].childElementCount%2==0)?"uimaster_chat_item_even":"uimaster_chat_item_old");
            var row = "<div class=\"swiper-slide uimaster_chat_item_to "+color+"\"><div><div class=\"uimaster_chat_time\">"
				 + new Date() + "</div><div class=\"uimaster_chat_message\"> " + e.content + "</div></div></div>"
            msgContainer.appendSlide($(row));
       });
    
    
            /* Construct_LAST:org_shaolin_bmdp_workflow_form_ChatWindow */
    };

    Form.Send = org_shaolin_bmdp_workflow_form_ChatWindow_Send;

    Form.Cancel = org_shaolin_bmdp_workflow_form_ChatWindow_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_workflow_form_ChatWindow_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.workflow.form.ChatWindow";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_form_ChatWindow */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_form_ChatWindow */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ChatWindow_Send(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ChatWindow_Send */
        var o = this;
        var UIEntity = this;

        { 
            var message = this.enterMessageUI.value;
            if (message.trim() == "") {
                return;
            }
            var sessionId = this.sessionIdUI.value;
            if (sessionId == null || sessionId == "null") {
	           sessionId = "";
	        }
            var partyId = this.sentPartyIdUI.value;
	        var fromPartyId = this.sentPartyIdUI.value;
	        var toPartyId = this.receivedPartyIdUI.value;
	        var orgId = this.orgIdUI.value;
            var msg = {taskId: 0, orgId: orgId, sessionId: sessionId, fromPartyId: fromPartyId, 
                       toPartyId: toPartyId, content: this.sentPartyNameUI.value+" : "+message};
            this.nodesocket.emit('chatTo', msg);
            this.enterMessageUI.value="";
        }    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ChatWindow_Send */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ChatWindow_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ChatWindow_Cancel */
        var o = this;
        var UIEntity = this;

        { 
            //var partyId = this.sentPartyIdUI.value;
            //this.nodesocket.emit('unregister', {partyId: partyId});
            //only for user logout.
            this.nodesocket.disconnect();
        }
        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20160305-063830",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ChatWindow_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ChatWindow_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ChatWindow_invokeDynamicFunction */
        var o = this;
        var UIEntity = this;

        var constraint_result = this.Form.validate();
        if (constraint_result != true && constraint_result != null) {
            return false;
        }

        new UIMaster.ui.dialog({dialogType: UIMaster.ui.dialog.CONFIRM_DIALOG,message:WORKFLOW_COMFORMATION_MSG,messageType:UIMaster.ui.dialog.Warning,optionType:UIMaster.ui.dialog.YES_NO_OPTION,title:'',height:150,width:300,handler: function() {

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),o.__entityName);

        
        }
        }).open();
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ChatWindow_invokeDynamicFunction */



