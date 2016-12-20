/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_Notification(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var idUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "idUI"]
    });

    var countUIId = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "countUIId"]
    });

    var partyIdUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "partyIdUILabel"]
    });

    var partyIdUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "partyIdUI"]
    });

    var subjectUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "subjectUILabel"]
    });

    var subjectUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "subjectUI"]
    });

    var descriptionUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "descriptionUILabel"]
    });

    var descriptionUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "descriptionUI"]
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
        ,subComponents: [prefix + "idUI",prefix + "countUIId",prefix + "partyIdUILabel",prefix + "partyIdUI",prefix + "subjectUILabel",prefix + "subjectUI",prefix + "descriptionUILabel",prefix + "descriptionUI"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [idUI,countUIId,partyIdUILabel,partyIdUI,subjectUILabel,subjectUI,descriptionUILabel,descriptionUI,okbtn,cancelbtn,fieldPanel,actionPanel]
    });

    Form.idUI=idUI;

    Form.countUIId=countUIId;

    Form.partyIdUILabel=partyIdUILabel;

    Form.partyIdUI=partyIdUI;

    Form.subjectUILabel=subjectUILabel;

    Form.subjectUI=subjectUI;

    Form.descriptionUILabel=descriptionUILabel;

    Form.descriptionUI=descriptionUI;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.fieldPanel=fieldPanel;

    Form.idUI=idUI;

    Form.countUIId=countUIId;

    Form.partyIdUILabel=partyIdUILabel;

    Form.partyIdUI=partyIdUI;

    Form.subjectUILabel=subjectUILabel;

    Form.subjectUI=subjectUI;

    Form.descriptionUILabel=descriptionUILabel;

    Form.descriptionUI=descriptionUI;

    Form.actionPanel=actionPanel;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_form_Notification */
        /* Construct_LAST:org_shaolin_bmdp_workflow_form_Notification */
    };

    Form.Save = org_shaolin_bmdp_workflow_form_Notification_Save;

    Form.Cancel = org_shaolin_bmdp_workflow_form_Notification_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_workflow_form_Notification_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.workflow.form.Notification";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_form_Notification */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_form_Notification */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_Notification_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_Notification_Save */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"saveDetail-20150809-191612",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_Notification_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_Notification_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_Notification_Cancel */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20150809-191612",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_Notification_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_Notification_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_Notification_invokeDynamicFunction */
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
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_Notification_invokeDynamicFunction */



