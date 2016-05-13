/* null */
/* auto generated constructor */
function org_shaolin_bmdp_analyzer_page_OrgAnalysisReport(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var orgCharts = new UIMaster.ui.field
    ({
        ui: elementList[prefix + "orgCharts"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,uiskin: "org.shaolin.uimaster.page.skin.TitlePanel"
        ,items: [orgCharts]
    });

    Form.orgCharts=orgCharts;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_analyzer_page_OrgAnalysisReport */
        /* Construct_LAST:org_shaolin_bmdp_analyzer_page_OrgAnalysisReport */
    };

    Form.initPageJs = org_shaolin_bmdp_analyzer_page_OrgAnalysisReport_initPageJs;

    Form.finalizePageJs = org_shaolin_bmdp_analyzer_page_OrgAnalysisReport_finalizePageJs;

    Form.__AJAXSubmit = false;
    
    Form.__entityName="org.shaolin.bmdp.analyzer.page.OrgAnalysisReport";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_analyzer_page_OrgAnalysisReport */
/* Other_Func_LAST:org_shaolin_bmdp_analyzer_page_OrgAnalysisReport */

    function org_shaolin_bmdp_analyzer_page_OrgAnalysisReport_initPageJs(){/* Gen_First:org_shaolin_bmdp_analyzer_page_OrgAnalysisReport_initPageJs */
        var constraint_result = true;
        var UIEntity = this;

    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_OrgAnalysisReport_initPageJs */


    function org_shaolin_bmdp_analyzer_page_OrgAnalysisReport_finalizePageJs(){/* Gen_First:org_shaolin_bmdp_analyzer_page_OrgAnalysisReport_finalizePageJs */

    }/* Gen_Last:org_shaolin_bmdp_analyzer_page_OrgAnalysisReport_finalizePageJs */



