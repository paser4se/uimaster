/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_ChatWindow2(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
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
        ,style: "height:220px"
        ,appendRowMode: true
    });

    var enterMessageUI = new UIMaster.ui.textarea
    ({
        ui: elementList[prefix + "enterMessageUI"]
        ,style: "width:100%;height:60px;"
    });

    var okbtn = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "okbtn"]
    });

    var clearbtn = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "clearbtn"]
    });

    var cancelbtn = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "cancelbtn"]
    });

    var actionPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "actionPanel"]
        ,items: []
        ,subComponents: [prefix + "okbtn",prefix + "clearbtn",prefix + "cancelbtn"]
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
        ,items: [taskIdUI,orgIdUI,sentPartyIdUI,receivedPartyIdUI,isAbcUI,sessionIdUI,sentPartyNameUI,receivedPartyNameUI,messageUI,enterMessageUI,okbtn,clearbtn,cancelbtn,fieldPanel,topPanel,actionPanel]
    });

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

    Form.clearbtn=clearbtn;

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

    Form.clearbtn=clearbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_form_ChatWindow2 */

        
       var isAbc = this.isAbcUI.value;
       var sessionId = this.sessionIdUI.value;
       if (sessionId == null || sessionId == "null") {
           sessionId = "";
       }
       var partyId = this.sentPartyIdUI.value;
       var fromPartyId = this.sentPartyIdUI.value;
       var toPartyId = this.receivedPartyIdUI.value;
       $(this.messageUI).focus();
       var msgContainer = this.messageUI;
       this.chat = establishWebsocket("/wschart", 
         function(ws,e){
            var msg = {action: "register", partyId: partyId, isAbc: isAbc, sessionId: sessionId};
            ws.send(JSON.stringify(msg));
            var msg = {action: "history", fromPartyId: fromPartyId, toPartyId: toPartyId, sessionId: sessionId};
            setTimeout(function(){ ws.send(JSON.stringify(msg));}, 1000);
         },
         function(ws,e){
            if (e.data == "_register_confirmed") {
               return;
            } else if (e.data == "_register_failed") {
               $(this.enterMessageUI).attr("disabled", "disabled");
               return;
            }
            msgContainer.appendSlide(e.data);
         },
         function(ws,e){
             console.log("error occurred while receiving a message: " + e.data);
         });
    
    
            /* Construct_LAST:org_shaolin_bmdp_workflow_form_ChatWindow2 */
    };

    Form.Send = org_shaolin_bmdp_workflow_form_ChatWindow2_Send;

    Form.ClearMessage = org_shaolin_bmdp_workflow_form_ChatWindow2_ClearMessage;

    Form.Cancel = org_shaolin_bmdp_workflow_form_ChatWindow2_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_workflow_form_ChatWindow2_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.workflow.form.ChatWindow2";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_form_ChatWindow2 */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_form_ChatWindow2 */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ChatWindow2_Send(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ChatWindow2_Send */
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
            var msg = {action: "chating", taskId: 0, orgId: orgId, sessionId: sessionId, fromPartyId: fromPartyId, 
                       toPartyId: toPartyId, content: this.sentPartyNameUI.value+" : "+message};
            this.chat.send(JSON.stringify(msg));
            this.enterMessageUI.value="";
        }    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ChatWindow2_Send */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ChatWindow2_ClearMessage(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ChatWindow2_ClearMessage */
        var o = this;
        var UIEntity = this;

         if(true){
          new UIMaster.ui.dialog({
              dialogType: UIMaster.ui.dialog.CONFIRM_DIALOG,
              message:'Are you sure remove all messages?',
              messageType:UIMaster.ui.dialog.Warning,
              optionType:UIMaster.ui.dialog.YES_NO_OPTION,
              title:'\u5220\u9664\u8282\u70B9?',
              height:150,
              width:300,
              handler: function() {
                 UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"clearMessage-20160305-063830",UIMaster.getValue(eventsource),o.__entityName);
              }
          }).open();
          return;
         }
         
        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"clearMessage-20160305-063830",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ChatWindow2_ClearMessage */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ChatWindow2_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ChatWindow2_Cancel */
        var o = this;
        var UIEntity = this;

        { 
            //var msg = {action: "close", partyId: fromPartyId};
            //this.chat.send(JSON.stringify(msg));
            this.chat.close();
        }
        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20160305-063830",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ChatWindow2_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ChatWindow2_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ChatWindow2_invokeDynamicFunction */
        var o = this;
        var UIEntity = this;

        new UIMaster.ui.dialog({dialogType: UIMaster.ui.dialog.CONFIRM_DIALOG,message:'Are you sure continuing? ^_^',messageType:UIMaster.ui.dialog.Warning,optionType:UIMaster.ui.dialog.YES_NO_OPTION,title:'',height:150,width:300,handler: function() {

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),o.__entityName);

        
        }
        }).open();
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ChatWindow2_invokeDynamicFunction */



