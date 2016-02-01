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
        ,subComponents: [prefix + "tasksTablePanel",prefix + "tasksHistoryTablePanel",prefix + "tasksAnalysisPanel",prefix + "serverNodePanel"]
    });
    var tasksTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "tasksTable"]
    });

    var tasksTablePanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "tasksTablePanel"]
        ,items: []
        ,subComponents: [prefix + "tasksTable"]
    });
    var tasksHistoryTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "tasksHistoryTable"]
    });

    var tasksHistoryTablePanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "tasksHistoryTablePanel"]
        ,items: []
        ,subComponents: [prefix + "tasksHistoryTable"]
    });
    var currentTasksPie = new UIMaster.ui.chart
    ({
        ui: elementList[prefix + "currentTasksPie"]
    });

    var historyTasksPie = new UIMaster.ui.chart
    ({
        ui: elementList[prefix + "historyTasksPie"]
    });

    var firstPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "firstPanel"]
        ,items: []
        ,subComponents: [prefix + "currentTasksPie",prefix + "historyTasksPie"]
    });
    var historyTasksRadar = new UIMaster.ui.chart
    ({
        ui: elementList[prefix + "historyTasksRadar"]
    });

    var tasksAnalysisPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "tasksAnalysisPanel"]
        ,items: []
        ,subComponents: [prefix + "firstPanel",prefix + "historyTasksRadar"]
    });
    var serverNodeTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "serverNodeTable"]
    });

    var serverNodePanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "serverNodePanel"]
        ,items: []
        ,subComponents: [prefix + "serverNodeTable"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,uiskin: "org.shaolin.uimaster.page.skin.TitlePanel"
        ,items: [CENameUI,functionsTab]
    });

    Form.CENameUI=CENameUI;

    Form.functionsTab=functionsTab;

    Form.tasksTablePanel=tasksTablePanel;

    Form.tasksTable=tasksTable;

    Form.tasksHistoryTablePanel=tasksHistoryTablePanel;

    Form.tasksHistoryTable=tasksHistoryTable;

    Form.tasksAnalysisPanel=tasksAnalysisPanel;

    Form.firstPanel=firstPanel;

    Form.currentTasksPie=currentTasksPie;

    Form.historyTasksPie=historyTasksPie;

    Form.historyTasksRadar=historyTasksRadar;

    Form.serverNodePanel=serverNodePanel;

    Form.serverNodeTable=serverNodeTable;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_page_TasksManager */
        /* Construct_LAST:org_shaolin_bmdp_workflow_page_TasksManager */
    };

    Form.createTask = org_shaolin_bmdp_workflow_page_TasksManager_createTask;

    Form.openTask = org_shaolin_bmdp_workflow_page_TasksManager_openTask;

    Form.postponeTask = org_shaolin_bmdp_workflow_page_TasksManager_postponeTask;

    Form.completeTask = org_shaolin_bmdp_workflow_page_TasksManager_completeTask;

    Form.cancelTask = org_shaolin_bmdp_workflow_page_TasksManager_cancelTask;

    Form.createServerNode = org_shaolin_bmdp_workflow_page_TasksManager_createServerNode;

    Form.openServerNode = org_shaolin_bmdp_workflow_page_TasksManager_openServerNode;

    Form.deleteUser = org_shaolin_bmdp_workflow_page_TasksManager_deleteUser;

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
    function org_shaolin_bmdp_workflow_page_TasksManager_createTask(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_createTask */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"showBlanktaskInfoPanel",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_createTask */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_page_TasksManager_openTask(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_openTask */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"openTask-20150809-2009",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_openTask */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_page_TasksManager_postponeTask(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_postponeTask */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"postponeTask-20150827-1152",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_postponeTask */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_page_TasksManager_completeTask(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_completeTask */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"completeTask-20150827-1152",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_completeTask */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_page_TasksManager_cancelTask(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_cancelTask */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelTask-20150827-1152",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_cancelTask */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_page_TasksManager_createServerNode(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_createServerNode */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"createServerNode-20150809-2009",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_createServerNode */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_page_TasksManager_openServerNode(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_openServerNode */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"openServerNode-20150809-2009",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_openServerNode */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_page_TasksManager_deleteUser(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_deleteUser */

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_deleteUser */


    function org_shaolin_bmdp_workflow_page_TasksManager_initPageJs(){/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_initPageJs */
        var constraint_result = true;
        var UIEntity = this;

    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_initPageJs */


    function org_shaolin_bmdp_workflow_page_TasksManager_finalizePageJs(){/* Gen_First:org_shaolin_bmdp_workflow_page_TasksManager_finalizePageJs */

    }/* Gen_Last:org_shaolin_bmdp_workflow_page_TasksManager_finalizePageJs */



