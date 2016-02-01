/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_NotificationBoard(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var itemTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "itemTable"]
    });

    var fieldPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "fieldPanel"]
        ,items: []
        ,subComponents: [prefix + "itemTable"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [itemTable,fieldPanel]
    });

    Form.itemTable=itemTable;

    Form.fieldPanel=fieldPanel;

    Form.itemTable=itemTable;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_form_NotificationBoard */
        /* Construct_LAST:org_shaolin_bmdp_workflow_form_NotificationBoard */
    };

    Form.pull = org_shaolin_bmdp_workflow_form_NotificationBoard_pull;

    Form.invokeDynamicFunction = org_shaolin_bmdp_workflow_form_NotificationBoard_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.workflow.form.NotificationBoard";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_form_NotificationBoard */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_form_NotificationBoard */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_NotificationBoard_pull(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_NotificationBoard_pull */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"pull-20160127-1507",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_NotificationBoard_pull */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_NotificationBoard_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_NotificationBoard_invokeDynamicFunction */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_NotificationBoard_invokeDynamicFunction */



