/* null */
/* auto generated constructor */
function org_shaolin_bmdp_analyzer_page_PageHelperConfiguration(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var functionsTab = new UIMaster.ui.tab
    ({
        ui: elementList[prefix + "functionsTab"]
        ,items: []
        ,subComponents: [prefix + "javaCCJobInfoPanel"]
    });
    var helperTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "helperTable"]
    });

    var javaCCJobInfoPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "javaCCJobInfoPanel"]
        ,items: []
        ,subComponents: [prefix + "helperTable"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,uiskin: "org.shaolin.uimaster.page.skin.TitlePanel"
        ,items: [functionsTab]
    });

    Form.functionsTab=functionsTab;

    Form.javaCCJobInfoPanel=javaCCJobInfoPanel;

    Form.helperTable=helperTable;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_analyzer_page_PageHelperConfiguration */
        /* Construct_LAST:org_shaolin_bmdp_analyzer_page_PageHelperConfiguration */
    };

    Form.createPageHelperInfo = org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_createPageHelperInfo;

    Form.openHelperInfo = org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_openHelperInfo;

    Form.initPageJs = org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_initPageJs;

    Form.finalizePageJs = org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_finalizePageJs;

    Form.__AJAXSubmit = false;
    
    Form.__entityName="org.shaolin.bmdp.analyzer.page.PageHelperConfiguration";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_analyzer_page_PageHelperConfiguration */
/* Other_Func_LAST:org_shaolin_bmdp_analyzer_page_PageHelperConfiguration */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_createPageHelperInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_createPageHelperInfo */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"createPageHelperInfo-201512052231",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_createPageHelperInfo */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_openHelperInfo(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_openHelperInfo */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"openHelperInfo-201512052231",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_openHelperInfo */


    function org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_initPageJs(){/* Gen_First:org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_initPageJs */
        var constraint_result = true;
        var UIEntity = this;

    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_initPageJs */


    function org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_finalizePageJs(){/* Gen_First:org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_finalizePageJs */

    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_PageHelperConfiguration_finalizePageJs */



