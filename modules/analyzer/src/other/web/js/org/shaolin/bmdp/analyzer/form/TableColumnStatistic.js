/* null */
/* auto generated constructor */
function org_shaolin_bmdp_analyzer_form_TableColumnStatistic(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var idUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "idUI"]
    });

    var nameUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "nameUILabel"]
    });

    var nameUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "nameUI"]
    });

    var descriptionUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "descriptionUILabel"]
    });

    var descriptionUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "descriptionUI"]
    });

    var statsTypeUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "statsTypeUILabel"]
    });

    var statsTypeUI = new UIMaster.ui.combobox
    ({
        ui: elementList[prefix + "statsTypeUI"]
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
        ,subComponents: [prefix + "idUI",prefix + "nameUILabel",prefix + "nameUI",prefix + "descriptionUILabel",prefix + "descriptionUI",prefix + "statsTypeUILabel",prefix + "statsTypeUI"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [idUI,nameUILabel,nameUI,descriptionUILabel,descriptionUI,statsTypeUILabel,statsTypeUI,okbtn,cancelbtn,fieldPanel,actionPanel]
    });

    Form.idUI=idUI;

    Form.nameUILabel=nameUILabel;

    Form.nameUI=nameUI;

    Form.descriptionUILabel=descriptionUILabel;

    Form.descriptionUI=descriptionUI;

    Form.statsTypeUILabel=statsTypeUILabel;

    Form.statsTypeUI=statsTypeUI;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.fieldPanel=fieldPanel;

    Form.idUI=idUI;

    Form.nameUILabel=nameUILabel;

    Form.nameUI=nameUI;

    Form.descriptionUILabel=descriptionUILabel;

    Form.descriptionUI=descriptionUI;

    Form.statsTypeUILabel=statsTypeUILabel;

    Form.statsTypeUI=statsTypeUI;

    Form.actionPanel=actionPanel;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_analyzer_form_TableColumnStatistic */
        /* Construct_LAST:org_shaolin_bmdp_analyzer_form_TableColumnStatistic */
    };

    Form.Save = org_shaolin_bmdp_analyzer_form_TableColumnStatistic_Save;

    Form.Cancel = org_shaolin_bmdp_analyzer_form_TableColumnStatistic_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_analyzer_form_TableColumnStatistic_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.analyzer.form.TableColumnStatistic";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_analyzer_form_TableColumnStatistic */
/* Other_Func_LAST:org_shaolin_bmdp_analyzer_form_TableColumnStatistic */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_TableColumnStatistic_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_TableColumnStatistic_Save */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"saveDetail-20160411-234815",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_TableColumnStatistic_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_TableColumnStatistic_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_TableColumnStatistic_Cancel */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20160411-234815",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_TableColumnStatistic_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_TableColumnStatistic_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_TableColumnStatistic_invokeDynamicFunction */
        var o = this;
        var UIEntity = this;

        new UIMaster.ui.dialog({dialogType: UIMaster.ui.dialog.CONFIRM_DIALOG,message:'Continue?',messageType:UIMaster.ui.dialog.Warning,optionType:UIMaster.ui.dialog.YES_NO_OPTION,title:'',height:150,width:300,handler: function() {

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),o.__entityName);

        
        }
        }).open();
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_TableColumnStatistic_invokeDynamicFunction */



