/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_NotificationBoard2(json)
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
        /* Construct_FIRST:org_shaolin_bmdp_workflow_form_NotificationBoard2 */

        
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
       this.chat = establishWebsocket("/wsnotificator", 
         function(ws,e){
            var msg = {action: "register", partyId: partyId};
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
    
    
            /* Construct_LAST:org_shaolin_bmdp_workflow_form_NotificationBoard2 */
    };

    Form.cleanup = org_shaolin_bmdp_workflow_form_NotificationBoard2_cleanup;

    Form.invokeDynamicFunction = org_shaolin_bmdp_workflow_form_NotificationBoard2_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.workflow.form.NotificationBoard2";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_form_NotificationBoard2 */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_form_NotificationBoard2 */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_NotificationBoard2_cleanup(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_NotificationBoard2_cleanup */
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
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_NotificationBoard2_cleanup */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_NotificationBoard2_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_NotificationBoard2_invokeDynamicFunction */
        var o = this;
        var UIEntity = this;

        new UIMaster.ui.dialog({dialogType: UIMaster.ui.dialog.CONFIRM_DIALOG,message:'Are you sure continuing? ^_^',messageType:UIMaster.ui.dialog.Warning,optionType:UIMaster.ui.dialog.YES_NO_OPTION,title:'',height:150,width:300,handler: function() {

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),o.__entityName);

        
        }
        }).open();
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_NotificationBoard2_invokeDynamicFunction */



