/* null */
/* auto generated constructor */
function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var functionsTab = new UIMaster.ui.tab
    ({
        ui: elementList[prefix + "functionsTab"]
        ,items: []
        ,subComponents: [prefix + "tableStatsPanel",prefix + "javaccJobInfoPanel",prefix + "jobInfoPanel",prefix + "chartStatsPanel"]
    });
    var tableStatsTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "tableStatsTable"]
    });

    var tableStatsPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "tableStatsPanel"]
        ,items: []
        ,subComponents: [prefix + "tableStatsTable"]
    });
    var javaCCJobInfoTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "javaCCJobInfoTable"]
    });

    var javaccJobInfoPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "javaccJobInfoPanel"]
        ,items: []
        ,subComponents: [prefix + "javaCCJobInfoTable"]
    });
    var jobInfoTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "jobInfoTable"]
    });

    var jobInfoPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "jobInfoPanel"]
        ,items: []
        ,subComponents: [prefix + "jobInfoTable"]
    });
    var chartStatsTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "chartStatsTable"]
    });

    var chartStatsPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "chartStatsPanel"]
        ,items: []
        ,subComponents: [prefix + "chartStatsTable"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,uiskin: "org.shaolin.uimaster.page.skin.TitlePanel"
        ,items: [functionsTab]
    });

    Form.functionsTab=functionsTab;

    Form.tableStatsPanel=tableStatsPanel;

    Form.tableStatsTable=tableStatsTable;

    Form.javaccJobInfoPanel=javaccJobInfoPanel;

    Form.javaCCJobInfoTable=javaCCJobInfoTable;

    Form.jobInfoPanel=jobInfoPanel;

    Form.jobInfoTable=jobInfoTable;

    Form.chartStatsPanel=chartStatsPanel;

    Form.chartStatsTable=chartStatsTable;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration */
        /* Construct_LAST:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration */
    };

    Form.createStatsTableInfo = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createStatsTableInfo;

    Form.openStatsTableInfo = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openStatsTableInfo;

    Form.createJobInfo = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createJobInfo;

    Form.openJobInfo = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openJobInfo;

    Form.createJavaCCJobInfo = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createJavaCCJobInfo;

    Form.openJavaCCJobInfo = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openJavaCCJobInfo;

    Form.startJavaCCJob = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_startJavaCCJob;

    Form.stopJavaCCJob = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_stopJavaCCJob;

    Form.createStatsInfo = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createStatsInfo;

    Form.openStatsInfo = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openStatsInfo;

    Form.initPageJs = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_initPageJs;

    Form.finalizePageJs = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_finalizePageJs;

    Form.__AJAXSubmit = false;
    
    Form.__entityName="org.shaolin.bmdp.analyzer.page.AnalysisConfiguration";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration */
/* Other_Func_LAST:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createStatsTableInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createStatsTableInfo */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"createStatsTableInfo-201512052231",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createStatsTableInfo */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openStatsTableInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openStatsTableInfo */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"openStatsTableInfo-201512052231",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openStatsTableInfo */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createJobInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createJobInfo */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"createJobInfo-201512052231",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createJobInfo */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openJobInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openJobInfo */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"openJobInfo-201512052231",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openJobInfo */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createJavaCCJobInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createJavaCCJobInfo */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"createJavaCCJobInfo-201512052231",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createJavaCCJobInfo */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openJavaCCJobInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openJavaCCJobInfo */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"openJavaCCJobInfo-201512052231",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openJavaCCJobInfo */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_startJavaCCJob(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_startJavaCCJob */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"startJavaCCJob-201512052231",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_startJavaCCJob */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_stopJavaCCJob(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_stopJavaCCJob */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"stopJavaCCJob-201512052231",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_stopJavaCCJob */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createStatsInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createStatsInfo */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"createStatsInfo-201512052231",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createStatsInfo */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openStatsInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openStatsInfo */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"openStatsInfo-201512052231",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openStatsInfo */


    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_initPageJs(){/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_initPageJs */
        var constraint_result = true;
        var UIEntity = this;

    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_initPageJs */


    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_finalizePageJs(){/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_finalizePageJs */

    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_finalizePageJs */



