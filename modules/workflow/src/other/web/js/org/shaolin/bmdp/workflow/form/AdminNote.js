/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_AdminNote(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var descriptionUI = new UIMaster.ui.textarea
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
        ,subComponents: [prefix + "descriptionUI"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [descriptionUI,okbtn,cancelbtn,fieldPanel,actionPanel]
    });

    Form.descriptionUI=descriptionUI;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.fieldPanel=fieldPanel;

    Form.descriptionUI=descriptionUI;

    Form.actionPanel=actionPanel;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_form_AdminNote */
        /* Construct_LAST:org_shaolin_bmdp_workflow_form_AdminNote */
    };

    Form.Save = org_shaolin_bmdp_workflow_form_AdminNote_Save;

    Form.Cancel = org_shaolin_bmdp_workflow_form_AdminNote_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_workflow_form_AdminNote_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.workflow.form.AdminNote";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_form_AdminNote */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_form_AdminNote */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_AdminNote_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_AdminNote_Save */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"saveDetail-20160801-135410",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_AdminNote_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_AdminNote_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_AdminNote_Cancel */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20160801-135410",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_AdminNote_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_AdminNote_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_AdminNote_invokeDynamicFunction */
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
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_AdminNote_invokeDynamicFunction */



