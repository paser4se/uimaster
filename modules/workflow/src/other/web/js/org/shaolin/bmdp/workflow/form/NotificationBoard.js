/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_NotificationBoard(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var serverURLUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "serverURLUI"]
    });

    var sentPartyIdUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "sentPartyIdUI"]
    });

    var countUIId = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "countUIId"]
    });

    var skipLoadSIOjsUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "skipLoadSIOjsUI"]
    });

    var cleanupBtn = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "cleanupBtn"]
    });

    var messagePanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "messagePanel"]
        ,items: []
        ,subComponents: []
    });

    var fieldPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "fieldPanel"]
        ,items: []
        ,subComponents: [prefix + "messagePanel",prefix + "cleanupBtn"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [serverURLUI,sentPartyIdUI,countUIId,skipLoadSIOjsUI,cleanupBtn,fieldPanel,messagePanel]
    });

    Form.serverURLUI=serverURLUI;

    Form.sentPartyIdUI=sentPartyIdUI;

    Form.countUIId=countUIId;

    Form.skipLoadSIOjsUI=skipLoadSIOjsUI;

    Form.cleanupBtn=cleanupBtn;

    Form.fieldPanel=fieldPanel;

    Form.messagePanel=messagePanel;

    Form.cleanupBtn=cleanupBtn;

    Form.messagePanel=messagePanel;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_form_NotificationBoard */

        
       var partyId = this.sentPartyIdUI.value;
       var msgContainer = this.messagePanel;
       var countUIId = this.countUIId.value;
       this.msgCounter = 0;
       if (IS_MOBILEVIEW) {
          this.realCounter = $("<span style='color:blue;font-weight:bold;margin-left:-25px;'></span>");
       } else {
          this.realCounter = $("<span style='color:blue;font-weight:bold;margin-left:-55px;'></span>");
       }
       $("#"+countUIId).append(this.realCounter);
       var o = this;
       if (this.skipLoadSIOjsUI.value != "true") {
       	  UIMaster.require("/js/socket.io.js");
       }
       if (o.serverURLUI.value == "") {
          return;
       }
       o.nodesocket = io.connect(o.serverURLUI.value);
       o.nodesocket.on('connect', function(e) {
            var msg = {partyId: partyId};
            o.nodesocket.emit('register', msg);
       });
       o.nodesocket.on('loginSuccess', function(e) {
            var msg = {partyId: partyId};
            o.nodesocket.emit('notifihistory', msg);
       });
       o.nodesocket.on('notifyhistory', function(e) {
            for (var i=0;i<e.length;i++) {
	            var row = "<div class=\"uimaster_noti_item "+((i%2==0)?"uimaster_chat_item_even":"uimaster_chat_item_old")+"\"><div><div class=\"uimaster_chat_time\">"
					 + e[i].CREATEDATE + "</div><div class=\"uimaster_chat_subject\"> " + (e[i].SUBJECT == null ? "": e[i].SUBJECT) + "</div><div class=\"uimaster_chat_message\"> " + e[i].DESCRIPTION + "</div></div></div>"
	            $(row).appendTo(msgContainer);
            }
            o.msgCounter = o.msgCounter + e.length;
            o.realCounter.text("("+o.msgCounter+")");
       });
       o.nodesocket.on('notifySingleItem', function(e) {
            for (var i=0;i<e.length;i++) {
	            var row = "<div class=\"uimaster_noti_item "+((i%2==0)?"uimaster_chat_item_even":"uimaster_chat_item_old")+"\"><div><div class=\"uimaster_chat_time\">"
					 + e[i].CREATEDATE + "</div><div class=\"uimaster_chat_subject\"> " + (e[i].SUBJECT == null ? "": e[i].SUBJECT) + "</div><div class=\"uimaster_chat_message\"> " + e[i].DESCRIPTION + "</div></div></div>"
	            $(row).appendTo(msgContainer);
            }
            o.msgCounter = o.msgCounter + e.length;
            o.realCounter.text("("+o.msgCounter+")");
       });
    
    
            /* Construct_LAST:org_shaolin_bmdp_workflow_form_NotificationBoard */
    };

    Form.cleanup = org_shaolin_bmdp_workflow_form_NotificationBoard_cleanup;

    Form.invokeDynamicFunction = org_shaolin_bmdp_workflow_form_NotificationBoard_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.workflow.form.NotificationBoard";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_form_NotificationBoard */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_form_NotificationBoard */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_NotificationBoard_cleanup(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_NotificationBoard_cleanup */
        var o = this;
        var UIEntity = this;

        { 
          this.msgCounter = 0;
          this.realCounter.text("");
          $(this.messagePanel).children().each(function(){
             $(this).remove();
          });
        
        }
        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cleanup-201506102211",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_NotificationBoard_cleanup */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_NotificationBoard_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_NotificationBoard_invokeDynamicFunction */
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
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_NotificationBoard_invokeDynamicFunction */



