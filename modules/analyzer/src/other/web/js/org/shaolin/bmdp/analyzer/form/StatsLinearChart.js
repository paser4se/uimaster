/* null */
/* auto generated constructor */
function org_shaolin_bmdp_analyzer_form_StatsLinearChart(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var linearChart = new UIMaster.ui.chart
    ({
        ui: elementList[prefix + "linearChart"]
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
        ,items: [linearChart,fieldPanel]
    });

    Form.linearChart=linearChart;

    Form.fieldPanel=fieldPanel;

    Form.linearChart=linearChart;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_analyzer_form_StatsLinearChart */
        /* Construct_LAST:org_shaolin_bmdp_analyzer_form_StatsLinearChart */
    };

    Form.invokeDynamicFunction = org_shaolin_bmdp_analyzer_form_StatsLinearChart_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.analyzer.form.StatsLinearChart";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_analyzer_form_StatsLinearChart */
/* Other_Func_LAST:org_shaolin_bmdp_analyzer_form_StatsLinearChart */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_StatsLinearChart_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_StatsLinearChart_invokeDynamicFunction */

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),this.__entityName);

        var UIEntity = this;
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_StatsLinearChart_invokeDynamicFunction */



