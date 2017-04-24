/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_page_TasksManager(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var CENameUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "CENameUI"]
    });

    var functionsTab = new UIMaster.ui.tab
    ({
        ui: elementList[prefix + "functionsTab"]
        ,items: []
        ,subComponents: [prefix + "sessionTablePanel",prefix + "sessionHistoryTablePanel"]
    });
    var sessionTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "sessionTable"]
    });

    var sessionTablePanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "sessionTablePanel"]
        ,items: []
        ,subComponents: [prefix + "sessionTable"]
    });
    var sessionHistoryTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "sessionHistoryTable"]
    });

    var sessionHistoryTablePanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "sessionHistoryTablePanel"]
        ,items: []
        ,subComponents: [prefix + "sessionHistoryTable"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,uiskin: "org.shaolin.uimaster.page.skin.TitlePanel"
        ,items: [CENameUI,functionsTab]
    });

    Form.CENameUI=CENameUI;

    Form.functionsTab=functionsTab;

    Form.sessionTablePanel=sessionTablePanel;

    Form.sessionTable=sessionTable;

    Form.sessionHistoryTablePanel=sessionHistoryTablePanel;

    Form.sessionHistoryTable=sessionHistoryTable;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_page_TasksManager */
        /* Construct_LAST:org_shaolin_bmdp_workflow_page_TasksManager */
    };

    Form.openTask = org_shaolin_bmdp_workflow_page_TasksManager_openTask;

    Form.openSessionTracker = org_shaolin_bmdp_workflow_page_TasksManager_openSessionTracker;

    Form.openHistorySessionTracker = org_shaolin_bmdp_workflow_page_TasksManager_openHistorySessionTracker;

    Form.completeTask = org_shaolin_bmdp_workflow_page_TasksManager_completeTask;

    Form.cancelTask = org_shaolin_bmdp_workflow_page_TasksManager_cancelTask;

    Form.initPageJs = org_shaolin_bmdp_workflow_page_TasksManager_initPageJs;

    Form.finalizePageJs = org_shaolin_bmdp_workflow_page_TasksManager_finalizePageJs;

    Form.__AJAXSubmit = false;
    
    Form.__entityName="org.shaolin.bmdp.workflow.page.TasksManager";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_page_TasksManager */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_page_TasksManager */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_page_TasksManager_openTask(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_openTask */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"openTask-20150809-2009",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_openTask */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_page_TasksManager_openSessionTracker(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_openSessionTracker */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"openSessionTracker-20160614",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_openSessionTracker */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_page_TasksManager_openHistorySessionTracker(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_openHistorySessionTracker */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"openHistorySessionTracker-20160614",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_openHistorySessionTracker */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_page_TasksManager_completeTask(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_completeTask */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"completeTask-20150827-1152",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_completeTask */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_page_TasksManager_cancelTask(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_cancelTask */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelTask-20150827-1152",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_cancelTask */


    function org_shaolin_bmdp_workflow_page_TasksManager_initPageJs(){/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_initPageJs */
        var constraint_result = true;
        var UIEntity = this;

    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_initPageJs */


    function org_shaolin_bmdp_workflow_page_TasksManager_finalizePageJs(){/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_finalizePageJs */

    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_finalizePageJs */



