/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_FlowAVPConfig(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var idUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "idUI"]
    });

    var moduleNameUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "moduleNameUILabel"]
    });

    var moduleNameUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "moduleNameUI"]
    });

    var nameUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "nameUILabel"]
    });

    var nameUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "nameUI"]
    });

    var valueUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "valueUILabel"]
    });

    var valueUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "valueUI"]
    });

    var filterUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "filterUILabel"]
    });

    var filterUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "filterUI"]
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
        ,subComponents: [prefix + "idUI",prefix + "moduleNameUILabel",prefix + "moduleNameUI",prefix + "nameUILabel",prefix + "nameUI",prefix + "valueUILabel",prefix + "valueUI",prefix + "filterUILabel",prefix + "filterUI"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [idUI,moduleNameUILabel,moduleNameUI,nameUILabel,nameUI,valueUILabel,valueUI,filterUILabel,filterUI,okbtn,cancelbtn,fieldPanel,actionPanel]
    });

    Form.idUI=idUI;

    Form.moduleNameUILabel=moduleNameUILabel;

    Form.moduleNameUI=moduleNameUI;

    Form.nameUILabel=nameUILabel;

    Form.nameUI=nameUI;

    Form.valueUILabel=valueUILabel;

    Form.valueUI=valueUI;

    Form.filterUILabel=filterUILabel;

    Form.filterUI=filterUI;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.fieldPanel=fieldPanel;

    Form.idUI=idUI;

    Form.moduleNameUILabel=moduleNameUILabel;

    Form.moduleNameUI=moduleNameUI;

    Form.nameUILabel=nameUILabel;

    Form.nameUI=nameUI;

    Form.valueUILabel=valueUILabel;

    Form.valueUI=valueUI;

    Form.filterUILabel=filterUILabel;

    Form.filterUI=filterUI;

    Form.actionPanel=actionPanel;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_form_FlowAVPConfig */
        /* Construct_LAST:org_shaolin_bmdp_workflow_form_FlowAVPConfig */
    };

    Form.Save = org_shaolin_bmdp_workflow_form_FlowAVPConfig_Save;

    Form.Cancel = org_shaolin_bmdp_workflow_form_FlowAVPConfig_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_workflow_form_FlowAVPConfig_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.workflow.form.FlowAVPConfig";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_form_FlowAVPConfig */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_form_FlowAVPConfig */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_FlowAVPConfig_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_FlowAVPConfig_Save */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"saveDetail-20161030-230323",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_FlowAVPConfig_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_FlowAVPConfig_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_FlowAVPConfig_Cancel */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20161030-230323",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_FlowAVPConfig_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_FlowAVPConfig_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_FlowAVPConfig_invokeDynamicFunction */
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
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_FlowAVPConfig_invokeDynamicFunction */



