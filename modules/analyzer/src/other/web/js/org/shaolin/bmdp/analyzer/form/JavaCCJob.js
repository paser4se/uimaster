/* null */
/* auto generated constructor */
function org_shaolin_bmdp_analyzer_form_JavaCCJob(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var idUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "idUI"]
    });

    var scriptUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "scriptUILabel"]
    });

    var scriptUI = new UIMaster.ui.textarea
    ({
        ui: elementList[prefix + "scriptUI"]
    });

    var cronExprUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "cronExprUILabel"]
    });

    var cronExprUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "cronExprUI"]
    });

    var descUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "descUILabel"]
    });

    var descUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "descUI"]
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
        ,subComponents: [prefix + "idUI",prefix + "scriptUILabel",prefix + "scriptUI",prefix + "cronExprUILabel",prefix + "cronExprUI",prefix + "descUILabel",prefix + "descUI"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [idUI,scriptUILabel,scriptUI,cronExprUILabel,cronExprUI,descUILabel,descUI,okbtn,cancelbtn,fieldPanel,actionPanel]
    });

    Form.idUI=idUI;

    Form.scriptUILabel=scriptUILabel;

    Form.scriptUI=scriptUI;

    Form.cronExprUILabel=cronExprUILabel;

    Form.cronExprUI=cronExprUI;

    Form.descUILabel=descUILabel;

    Form.descUI=descUI;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.fieldPanel=fieldPanel;

    Form.idUI=idUI;

    Form.scriptUILabel=scriptUILabel;

    Form.scriptUI=scriptUI;

    Form.cronExprUILabel=cronExprUILabel;

    Form.cronExprUI=cronExprUI;

    Form.descUILabel=descUILabel;

    Form.descUI=descUI;

    Form.actionPanel=actionPanel;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_analyzer_form_JavaCCJob */
        /* Construct_LAST:org_shaolin_bmdp_analyzer_form_JavaCCJob */
    };

    Form.Save = org_shaolin_bmdp_analyzer_form_JavaCCJob_Save;

    Form.Cancel = org_shaolin_bmdp_analyzer_form_JavaCCJob_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_analyzer_form_JavaCCJob_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.analyzer.form.JavaCCJob";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_analyzer_form_JavaCCJob */
/* Other_Func_LAST:org_shaolin_bmdp_analyzer_form_JavaCCJob */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_JavaCCJob_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_JavaCCJob_Save */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"saveDetail-20160304-202546",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_JavaCCJob_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_JavaCCJob_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_JavaCCJob_Cancel */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20160304-202546",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_JavaCCJob_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_JavaCCJob_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_JavaCCJob_invokeDynamicFunction */
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
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_JavaCCJob_invokeDynamicFunction */



