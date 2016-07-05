/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_ChatHistory(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var idUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "idUI"]
    });

    var taskIdUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "taskIdUILabel"]
    });

    var taskIdUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "taskIdUI"]
    });

    var sentPartyIdUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "sentPartyIdUILabel"]
    });

    var sentPartyIdUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "sentPartyIdUI"]
    });

    var receivedPartyIdUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "receivedPartyIdUILabel"]
    });

    var receivedPartyIdUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "receivedPartyIdUI"]
    });

    var messageUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "messageUILabel"]
    });

    var messageUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "messageUI"]
    });

    var readUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "readUILabel"]
    });

    var readUI = new UIMaster.ui.checkbox
    ({
        ui: elementList[prefix + "readUI"]
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

    var fieldPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "fieldPanel"]
        ,items: []
        ,subComponents: [prefix + "idUI",prefix + "taskIdUILabel",prefix + "taskIdUI",prefix + "sentPartyIdUILabel",prefix + "sentPartyIdUI",prefix + "receivedPartyIdUILabel",prefix + "receivedPartyIdUI",prefix + "messageUILabel",prefix + "messageUI",prefix + "readUILabel",prefix + "readUI"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [idUI,taskIdUILabel,taskIdUI,sentPartyIdUILabel,sentPartyIdUI,receivedPartyIdUILabel,receivedPartyIdUI,messageUILabel,messageUI,readUILabel,readUI,okbtn,cancelbtn,fieldPanel,actionPanel]
    });

    Form.idUI=idUI;

    Form.taskIdUILabel=taskIdUILabel;

    Form.taskIdUI=taskIdUI;

    Form.sentPartyIdUILabel=sentPartyIdUILabel;

    Form.sentPartyIdUI=sentPartyIdUI;

    Form.receivedPartyIdUILabel=receivedPartyIdUILabel;

    Form.receivedPartyIdUI=receivedPartyIdUI;

    Form.messageUILabel=messageUILabel;

    Form.messageUI=messageUI;

    Form.readUILabel=readUILabel;

    Form.readUI=readUI;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.fieldPanel=fieldPanel;

    Form.idUI=idUI;

    Form.taskIdUILabel=taskIdUILabel;

    Form.taskIdUI=taskIdUI;

    Form.sentPartyIdUILabel=sentPartyIdUILabel;

    Form.sentPartyIdUI=sentPartyIdUI;

    Form.receivedPartyIdUILabel=receivedPartyIdUILabel;

    Form.receivedPartyIdUI=receivedPartyIdUI;

    Form.messageUILabel=messageUILabel;

    Form.messageUI=messageUI;

    Form.readUILabel=readUILabel;

    Form.readUI=readUI;

    Form.actionPanel=actionPanel;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_form_ChatHistory */
        /* Construct_LAST:org_shaolin_bmdp_workflow_form_ChatHistory */
    };

    Form.Save = org_shaolin_bmdp_workflow_form_ChatHistory_Save;

    Form.Cancel = org_shaolin_bmdp_workflow_form_ChatHistory_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_workflow_form_ChatHistory_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.workflow.form.ChatHistory";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_form_ChatHistory */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_form_ChatHistory */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ChatHistory_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ChatHistory_Save */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"saveDetail-20160308-232542",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ChatHistory_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ChatHistory_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ChatHistory_Cancel */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20160308-232542",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ChatHistory_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ChatHistory_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ChatHistory_invokeDynamicFunction */
        var o = this;
        var UIEntity = this;

        new UIMaster.ui.dialog({dialogType: UIMaster.ui.dialog.CONFIRM_DIALOG,message:'Are you sure continuing? ^_^',messageType:UIMaster.ui.dialog.Warning,optionType:UIMaster.ui.dialog.YES_NO_OPTION,title:'',height:150,width:300,handler: function() {

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),o.__entityName);

        
        }
        }).open();
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ChatHistory_invokeDynamicFunction */



