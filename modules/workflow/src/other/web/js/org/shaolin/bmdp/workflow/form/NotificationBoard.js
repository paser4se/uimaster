/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_NotificationBoard(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var sentPartyIdUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "sentPartyIdUI"]
    });

    var countUIId = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "countUIId"]
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
        ,items: [sentPartyIdUI,countUIId,cleanupBtn,fieldPanel,messagePanel]
    });

    Form.sentPartyIdUI=sentPartyIdUI;

    Form.countUIId=countUIId;

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
       this.realCounter = $("<span style='color:blue;font-weight:bold;margin-left:-25px;'></span>");
       $("#"+countUIId).append(this.realCounter);
       var o = this;
       this.chat = establishWebsocket("/wsnotificator", 
         function(ws,e){
            var msg = {action: "register", partyId: partyId};
            ws.send(JSON.stringify(msg));
            var msg = {action: "poll", partyId: partyId};
            ws.send(JSON.stringify(msg));
         },
         function(ws,e){
            if (e.data == "_register_confirmed") {
               return;
            }
            o.realCounter.text("("+(++o.msgCounter)+")");
            $(msgContainer).append(e.data);
         },
         function(ws,e){
             console.log("error occurred while receiving a message: " + e.data);
         });
       var repeat = function() {
          var msg = {action: "poll", partyId: partyId};
          o.chat.send(JSON.stringify(msg));
          
          window.setTimeout(function(){
             repeat();
          }, 120 * 1000);
       };  
       window.setTimeout(function(){
          repeat();
       }, 120 * 1000);
    
    
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

        new UIMaster.ui.dialog({dialogType: UIMaster.ui.dialog.CONFIRM_DIALOG,message:'Continue?',messageType:UIMaster.ui.dialog.Warning,optionType:UIMaster.ui.dialog.YES_NO_OPTION,title:'',height:150,width:300,handler: function() {

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),o.__entityName);

        
        }
        }).open();
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_NotificationBoard_invokeDynamicFunction */



