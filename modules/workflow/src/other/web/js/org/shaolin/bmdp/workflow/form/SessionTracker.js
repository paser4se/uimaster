/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_SessionTracker(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var sessionId = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "sessionId"]
    });

    var tasksTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "tasksTable"]
    });

    var cancelbtn = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "cancelbtn"]
    });

    var actionPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "actionPanel"]
        ,items: []
        ,subComponents: [prefix + "cancelbtn"]
    });

    var fieldPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "fieldPanel"]
        ,items: []
        ,subComponents: [prefix + "sessionId",prefix + "tasksTable"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [sessionId,tasksTable,cancelbtn,fieldPanel,actionPanel]
    });

    Form.sessionId=sessionId;

    Form.tasksTable=tasksTable;

    Form.cancelbtn=cancelbtn;

    Form.fieldPanel=fieldPanel;

    Form.sessionId=sessionId;

    Form.tasksTable=tasksTable;

    Form.actionPanel=actionPanel;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_form_SessionTracker */
        /* Construct_LAST:org_shaolin_bmdp_workflow_form_SessionTracker */
    };

    Form.Save = org_shaolin_bmdp_workflow_form_SessionTracker_Save;

    Form.Cancel = org_shaolin_bmdp_workflow_form_SessionTracker_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_workflow_form_SessionTracker_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.workflow.form.SessionTracker";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_form_SessionTracker */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_form_SessionTracker */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_SessionTracker_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_SessionTracker_Save */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"saveDetail-20150801-235410",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_SessionTracker_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_SessionTracker_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_SessionTracker_Cancel */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20150801-235410",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_SessionTracker_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_SessionTracker_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_SessionTracker_invokeDynamicFunction */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_SessionTracker_invokeDynamicFunction */



