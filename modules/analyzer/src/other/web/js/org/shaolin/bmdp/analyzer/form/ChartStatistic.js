/* null */
/* auto generated constructor */
function org_shaolin_bmdp_analyzer_form_ChartStatistic(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var idUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "idUI"]
    });

    var actionOnUIFromUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "actionOnUIFromUILabel"]
    });

    var actionOnUIFromUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "actionOnUIFromUI"]
    });

    var actionOnWidgetIdUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "actionOnWidgetIdUILabel"]
    });

    var actionOnWidgetIdUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "actionOnWidgetIdUI"]
    });

    var statsTableNameUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "statsTableNameUILabel"]
    });

    var statsTableNameUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "statsTableNameUI"]
    });

    var statsUIFromUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "statsUIFromUILabel"]
    });

    var statsUIFromUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "statsUIFromUI"]
    });

    var chartTypeUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "chartTypeUILabel"]
    });

    var chartTypeUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "chartTypeUI"]
    });

    var okbtn = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "okbtn"]
    });

    var cancelbtn = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "cancelbtn"]
    });

    var actionPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "actionPanel"]
        ,items: []
        ,subComponents: [prefix + "okbtn",prefix + "cancelbtn"]
    });

    var fieldPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "fieldPanel"]
        ,items: []
        ,subComponents: [prefix + "idUI",prefix + "actionOnUIFromUILabel",prefix + "actionOnUIFromUI",prefix + "actionOnWidgetIdUILabel",prefix + "actionOnWidgetIdUI",prefix + "statsTableNameUILabel",prefix + "statsTableNameUI",prefix + "statsUIFromUILabel",prefix + "statsUIFromUI",prefix + "chartTypeUILabel",prefix + "chartTypeUI"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [idUI,actionOnUIFromUILabel,actionOnUIFromUI,actionOnWidgetIdUILabel,actionOnWidgetIdUI,statsTableNameUILabel,statsTableNameUI,statsUIFromUILabel,statsUIFromUI,chartTypeUILabel,chartTypeUI,okbtn,cancelbtn,fieldPanel,actionPanel]
    });

    Form.idUI=idUI;

    Form.actionOnUIFromUILabel=actionOnUIFromUILabel;

    Form.actionOnUIFromUI=actionOnUIFromUI;

    Form.actionOnWidgetIdUILabel=actionOnWidgetIdUILabel;

    Form.actionOnWidgetIdUI=actionOnWidgetIdUI;

    Form.statsTableNameUILabel=statsTableNameUILabel;

    Form.statsTableNameUI=statsTableNameUI;

    Form.statsUIFromUILabel=statsUIFromUILabel;

    Form.statsUIFromUI=statsUIFromUI;

    Form.chartTypeUILabel=chartTypeUILabel;

    Form.chartTypeUI=chartTypeUI;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.fieldPanel=fieldPanel;

    Form.idUI=idUI;

    Form.actionOnUIFromUILabel=actionOnUIFromUILabel;

    Form.actionOnUIFromUI=actionOnUIFromUI;

    Form.actionOnWidgetIdUILabel=actionOnWidgetIdUILabel;

    Form.actionOnWidgetIdUI=actionOnWidgetIdUI;

    Form.statsTableNameUILabel=statsTableNameUILabel;

    Form.statsTableNameUI=statsTableNameUI;

    Form.statsUIFromUILabel=statsUIFromUILabel;

    Form.statsUIFromUI=statsUIFromUI;

    Form.chartTypeUILabel=chartTypeUILabel;

    Form.chartTypeUI=chartTypeUI;

    Form.actionPanel=actionPanel;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_analyzer_form_ChartStatistic */
        /* Construct_LAST:org_shaolin_bmdp_analyzer_form_ChartStatistic */
    };

    Form.Save = org_shaolin_bmdp_analyzer_form_ChartStatistic_Save;

    Form.Cancel = org_shaolin_bmdp_analyzer_form_ChartStatistic_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_analyzer_form_ChartStatistic_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.analyzer.form.ChartStatistic";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_analyzer_form_ChartStatistic */
/* Other_Func_LAST:org_shaolin_bmdp_analyzer_form_ChartStatistic */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_ChartStatistic_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_ChartStatistic_Save */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"saveDetail-20151209-172223",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_ChartStatistic_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_ChartStatistic_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_ChartStatistic_Cancel */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20151209-172223",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_ChartStatistic_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_ChartStatistic_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_ChartStatistic_invokeDynamicFunction */
        var o = this;
        var UIEntity = this;

        var constraint_result = this.Form.validate();
        if (constraint_result != true && constraint_result != null) {
            return false;
        }

        new UIMaster.ui.dialog({dialogType: UIMaster.ui.dialog.CONFIRM_DIALOG,message:WORKFLOW_COMFORMATION_MSG,messageType:UIMaster.ui.dialog.Warning,optionType:UIMaster.ui.dialog.YES_NO_OPTION,title:'',height:150,width:300,handler: function() {

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),o.__entityName);

        
        }
        }).open();
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_ChartStatistic_invokeDynamicFunction */



