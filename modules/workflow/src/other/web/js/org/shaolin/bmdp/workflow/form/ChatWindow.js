/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_ChatWindow(json)
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

    var sentPartyNameUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "sentPartyNameUI"]
    });

    var receivedPartyNameUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "receivedPartyNameUI"]
    });

    var messageUI = new UIMaster.ui.textarea
    ({
        ui: elementList[prefix + "messageUI"]
        ,hiddenToolbar: true
        ,persistable: false
    });

    var enterMessageUI = new UIMaster.ui.textarea
    ({
        ui: elementList[prefix + "enterMessageUI"]
        ,persistable: false
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
        ,items: [taskIdUI,orgIdUI,sentPartyIdUI,receivedPartyIdUI,sentPartyNameUI,receivedPartyNameUI,messageUI,enterMessageUI,okbtn,cancelbtn,fieldPanel,topPanel,actionPanel]
    });

    Form.taskIdUI=taskIdUI;

    Form.orgIdUI=orgIdUI;

    Form.sentPartyIdUI=sentPartyIdUI;

    Form.receivedPartyIdUI=receivedPartyIdUI;

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

        
       var partyId = this.sentPartyIdUI.value;
       var fromPartyId = this.sentPartyIdUI.value;
       var toPartyId = this.receivedPartyIdUI.value;
       var msgContainer = this.messageUI;
       this.chat = establishWebsocket("/wschart", 
         function(ws,e){
            var msg = {action: "register", partyId: partyId};
            ws.send(JSON.stringify(msg));
            var msg = {action: "history", fromPartyId: fromPartyId, toPartyId: toPartyId};
            ws.send(JSON.stringify(msg));
         },
         function(ws,e){
            if (e.data == "_register_confirmed") {
               return;
            }
            msgContainer.appendHTMLText(e.data);
         },
         function(ws,e){
             console.log("error occurred while receiving a message: " + e.data);
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
            var partyId = this.sentPartyIdUI.value;
	        var fromPartyId = this.sentPartyIdUI.value;
	        var toPartyId = this.receivedPartyIdUI.value;
	        var orgId = this.orgIdUI.value;
            var msg = {action: "chating", taskId: 0, orgId: orgId, fromPartyId: fromPartyId, 
                       toPartyId: toPartyId, content: this.enterMessageUI.getHTMLText()};
            this.chat.send(JSON.stringify(msg));
            this.enterMessageUI.clearHTMLText();
        }    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ChatWindow_Send */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ChatWindow_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ChatWindow_Cancel */
        var o = this;
        var UIEntity = this;

        { 
            //var msg = {action: "close", partyId: fromPartyId};
            //this.chat.send(JSON.stringify(msg));
            this.chat.close();
        }
        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20160305-063830",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ChatWindow_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ChatWindow_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ChatWindow_invokeDynamicFunction */
        var o = this;
        var UIEntity = this;

        new UIMaster.ui.dialog({dialogType: UIMaster.ui.dialog.CONFIRM_DIALOG,message:'Continue?',messageType:UIMaster.ui.dialog.Warning,optionType:UIMaster.ui.dialog.YES_NO_OPTION,title:'',height:150,width:300,handler: function() {

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),o.__entityName);

        
        }
        }).open();
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ChatWindow_invokeDynamicFunction */



