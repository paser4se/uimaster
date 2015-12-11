/* null */
/* auto generated constructor */
function org_shaolin_bmdp_analyzer_form_StatsDateLinearChart(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var linearChart = new UIMaster.ui.chart
    ({
        ui: elementList[prefix + "linearChart"]
    });

    var okbtn = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "okbtn"]
    });

    var actionPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "actionPanel"]
        ,items: []
        ,subComponents: [prefix + "okbtn"]
    });

    var fieldPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "fieldPanel"]
        ,items: []
        ,subComponents: [prefix + "linearChart"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [linearChart,okbtn,fieldPanel,actionPanel]
    });

    Form.linearChart=linearChart;

    Form.okbtn=okbtn;

    Form.fieldPanel=fieldPanel;

    Form.linearChart=linearChart;

    Form.actionPanel=actionPanel;

    Form.okbtn=okbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_analyzer_form_StatsDateLinearChart */
        /* Construct_LAST:org_shaolin_bmdp_analyzer_form_StatsDateLinearChart */
    };

    Form.Cancel = org_shaolin_bmdp_analyzer_form_StatsDateLinearChart_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_analyzer_form_StatsDateLinearChart_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.analyzer.form.StatsDateLinearChart";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_analyzer_form_StatsDateLinearChart */
/* Other_Func_LAST:org_shaolin_bmdp_analyzer_form_StatsDateLinearChart */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_StatsDateLinearChart_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_StatsDateLinearChart_Cancel */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20151205-183328",UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_StatsDateLinearChart_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_StatsDateLinearChart_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_StatsDateLinearChart_invokeDynamicFunction */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_StatsDateLinearChart_invokeDynamicFunction */



