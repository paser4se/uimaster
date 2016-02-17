/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_CreateNode(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var nameUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "nameUILabel"]
    });

    var nameUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "nameUI"]
    });

    var expressionUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "expressionUILabel"]
    });

    var expressionUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "expressionUI"]
    });

    var partyTypeUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "partyTypeUILabel"]
    });

    var partyTypeUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "partyTypeUI"]
    });

    var typeUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "typeUILabel"]
    });

    var typeUI = new UIMaster.ui.combobox
    ({
        ui: elementList[prefix + "typeUI"]
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
        ,subComponents: [prefix + "nameUILabel",prefix + "nameUI",prefix + "expressionUILabel",prefix + "expressionUI",prefix + "partyTypeUILabel",prefix + "partyTypeUI",prefix + "typeUILabel",prefix + "typeUI"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [nameUILabel,nameUI,expressionUILabel,expressionUI,partyTypeUILabel,partyTypeUI,typeUILabel,typeUI,okbtn,cancelbtn,fieldPanel,actionPanel]
    });

    Form.nameUILabel=nameUILabel;

    Form.nameUI=nameUI;

    Form.expressionUILabel=expressionUILabel;

    Form.expressionUI=expressionUI;

    Form.partyTypeUILabel=partyTypeUILabel;

    Form.partyTypeUI=partyTypeUI;

    Form.typeUILabel=typeUILabel;

    Form.typeUI=typeUI;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.fieldPanel=fieldPanel;

    Form.nameUILabel=nameUILabel;

    Form.nameUI=nameUI;

    Form.expressionUILabel=expressionUILabel;

    Form.expressionUI=expressionUI;

    Form.partyTypeUILabel=partyTypeUILabel;

    Form.partyTypeUI=partyTypeUI;

    Form.typeUILabel=typeUILabel;

    Form.typeUI=typeUI;

    Form.actionPanel=actionPanel;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_form_CreateNode */
        /* Construct_LAST:org_shaolin_bmdp_workflow_form_CreateNode */
    };

    Form.Save = org_shaolin_bmdp_workflow_form_CreateNode_Save;

    Form.Cancel = org_shaolin_bmdp_workflow_form_CreateNode_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_workflow_form_CreateNode_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.workflow.form.CreateNode";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_form_CreateNode */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_form_CreateNode */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_CreateNode_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_CreateNode_Save */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"saveDetail-20150807-230249",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_CreateNode_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_CreateNode_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_CreateNode_Cancel */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20150807-230249",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_CreateNode_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_CreateNode_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_CreateNode_invokeDynamicFunction */
        var o = this;
        var UIEntity = this;

        new UIMaster.ui.dialog({dialogType: UIMaster.ui.dialog.CONFIRM_DIALOG,message:'????',messageType:UIMaster.ui.dialog.Warning,optionType:UIMaster.ui.dialog.YES_NO_OPTION,title:'',height:150,width:300,handler: function() {

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),o.__entityName);

        
        }
        }).open();
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_CreateNode_invokeDynamicFunction */



