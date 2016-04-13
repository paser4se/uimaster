/* null */
/* auto generated constructor */
function org_shaolin_bmdp_analyzer_form_TableStatistic(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var tableNameUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "tableNameUILabel"]
    });

    var tableNameUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "tableNameUI"]
    });

    var descriptionUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "descriptionUILabel"]
    });

    var descriptionUI = new UIMaster.ui.textarea
    ({
        ui: elementList[prefix + "descriptionUI"]
    });

    var needOrgStatsUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "needOrgStatsUILabel"]
    });

    var needOrgStatsUI = new UIMaster.ui.checkbox
    ({
        ui: elementList[prefix + "needOrgStatsUI"]
    });

    var chartTypeUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "chartTypeUILabel"]
    });

    var chartTypeUI = new UIMaster.ui.combobox
    ({
        ui: elementList[prefix + "chartTypeUI"]
    });

    var statsPeriodUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "statsPeriodUILabel"]
    });

    var statsPeriodUI = new UIMaster.ui.combobox
    ({
        ui: elementList[prefix + "statsPeriodUI"]
    });

    var itemTable = new UIMaster.ui.objectlist
    ({
        ui: elementList[prefix + "itemTable"]
        ,editable: true
    });

    var fieldPanel2 = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "fieldPanel2"]
        ,items: []
        ,subComponents: [prefix + "itemTable"]
    });

    var fieldPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "fieldPanel"]
        ,items: []
        ,subComponents: [prefix + "tableNameUILabel",prefix + "tableNameUI",prefix + "descriptionUILabel",prefix + "descriptionUI",prefix + "needOrgStatsUILabel",prefix + "needOrgStatsUI",prefix + "chartTypeUILabel",prefix + "chartTypeUI",prefix + "statsPeriodUILabel",prefix + "statsPeriodUI"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [tableNameUILabel,tableNameUI,descriptionUILabel,descriptionUI,needOrgStatsUILabel,needOrgStatsUI,chartTypeUILabel,chartTypeUI,statsPeriodUILabel,statsPeriodUI,itemTable,fieldPanel,fieldPanel2]
    });

    Form.tableNameUILabel=tableNameUILabel;

    Form.tableNameUI=tableNameUI;

    Form.descriptionUILabel=descriptionUILabel;

    Form.descriptionUI=descriptionUI;

    Form.needOrgStatsUILabel=needOrgStatsUILabel;

    Form.needOrgStatsUI=needOrgStatsUI;

    Form.chartTypeUILabel=chartTypeUILabel;

    Form.chartTypeUI=chartTypeUI;

    Form.statsPeriodUILabel=statsPeriodUILabel;

    Form.statsPeriodUI=statsPeriodUI;

    Form.itemTable=itemTable;

    Form.fieldPanel=fieldPanel;

    Form.tableNameUILabel=tableNameUILabel;

    Form.tableNameUI=tableNameUI;

    Form.descriptionUILabel=descriptionUILabel;

    Form.descriptionUI=descriptionUI;

    Form.needOrgStatsUILabel=needOrgStatsUILabel;

    Form.needOrgStatsUI=needOrgStatsUI;

    Form.chartTypeUILabel=chartTypeUILabel;

    Form.chartTypeUI=chartTypeUI;

    Form.statsPeriodUILabel=statsPeriodUILabel;

    Form.statsPeriodUI=statsPeriodUI;

    Form.fieldPanel2=fieldPanel2;

    Form.itemTable=itemTable;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_analyzer_form_TableStatistic */
        /* Construct_LAST:org_shaolin_bmdp_analyzer_form_TableStatistic */
    };

    Form.addItem = org_shaolin_bmdp_analyzer_form_TableStatistic_addItem;

    Form.deleteItem = org_shaolin_bmdp_analyzer_form_TableStatistic_deleteItem;

    Form.Save = org_shaolin_bmdp_analyzer_form_TableStatistic_Save;

    Form.Cancel = org_shaolin_bmdp_analyzer_form_TableStatistic_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_analyzer_form_TableStatistic_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.analyzer.form.TableStatistic";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_analyzer_form_TableStatistic */
/* Other_Func_LAST:org_shaolin_bmdp_analyzer_form_TableStatistic */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_TableStatistic_addItem(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_TableStatistic_addItem */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"selectedProduct0_49169030",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_TableStatistic_addItem */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_TableStatistic_deleteItem(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_TableStatistic_deleteItem */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"deleteItem_839670735",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_TableStatistic_deleteItem */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_TableStatistic_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_TableStatistic_Save */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"saveDetail-20160411-234815",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_TableStatistic_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_TableStatistic_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_TableStatistic_Cancel */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20160411-234815",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_TableStatistic_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_TableStatistic_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_TableStatistic_invokeDynamicFunction */
        var o = this;
        var UIEntity = this;

        new UIMaster.ui.dialog({dialogType: UIMaster.ui.dialog.CONFIRM_DIALOG,message:'Continue?',messageType:UIMaster.ui.dialog.Warning,optionType:UIMaster.ui.dialog.YES_NO_OPTION,title:'',height:150,width:300,handler: function() {

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),o.__entityName);

        
        }
        }).open();
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_TableStatistic_invokeDynamicFunction */



