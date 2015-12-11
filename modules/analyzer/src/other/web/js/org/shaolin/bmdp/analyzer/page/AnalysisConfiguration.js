/* null */
/* auto generated constructor */
function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var functionsTab = new UIMaster.ui.tab
    ({
        ui: elementList[prefix + "functionsTab"]
        ,items: []
        ,subComponents: [prefix + "dbInfoPanel",prefix + "jobInfoPanel"]
    });
    var dbInfoTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "dbInfoTable"]
    });

    var dbInfoPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "dbInfoPanel"]
        ,items: []
        ,subComponents: [prefix + "dbInfoTable"]
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

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,uiskin: "org.shaolin.uimaster.page.skin.TitlePanel"
        ,items: [functionsTab]
    });

    Form.functionsTab=functionsTab;

    Form.dbInfoPanel=dbInfoPanel;

    Form.dbInfoTable=dbInfoTable;

    Form.jobInfoPanel=jobInfoPanel;

    Form.jobInfoTable=jobInfoTable;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration */
        /* Construct_LAST:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration */
    };

    Form.createDBInfo = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createDBInfo;

    Form.openDBInfo = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openDBInfo;

    Form.createJobInfo = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createJobInfo;

    Form.openJobInfo = org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openJobInfo;

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
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createDBInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createDBInfo */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"createDBInfo-201512052231",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createDBInfo */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openDBInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openDBInfo */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"openDBInfo-201512052231",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openDBInfo */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createJobInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createJobInfo */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"createJobInfo-201512052231",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_createJobInfo */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openJobInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openJobInfo */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"openJobInfo-201512052231",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_openJobInfo */


    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_initPageJs(){/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_initPageJs */
        var constraint_result = true;
        var UIEntity = this;

    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_initPageJs */


    function org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_finalizePageJs(){/* Gen_First:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_finalizePageJs */

    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_AnalysisConfiguration_finalizePageJs */



