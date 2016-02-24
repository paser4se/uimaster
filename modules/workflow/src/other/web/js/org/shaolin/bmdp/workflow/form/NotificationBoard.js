/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_NotificationBoard(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var itemTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "itemTable"]
        ,appendRowMode: true
        ,refreshInterval: 300
    });

    var cleanupBtn = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "cleanupBtn"]
    });

    var fieldPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "fieldPanel"]
        ,items: []
        ,subComponents: [prefix + "itemTable",prefix + "cleanupBtn"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [itemTable,cleanupBtn,fieldPanel]
    });

    Form.itemTable=itemTable;

    Form.cleanupBtn=cleanupBtn;

    Form.fieldPanel=fieldPanel;

    Form.itemTable=itemTable;

    Form.cleanupBtn=cleanupBtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_form_NotificationBoard */
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

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cleanup-20160127-1507",UIMaster.getValue(eventsource),o.__entityName);
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



