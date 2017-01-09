/* null */
/* auto generated constructor */
function org_shaolin_bmdp_analyzer_form_PageHelper(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var idUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "idUI"]
    });

    var htmlFileUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "htmlFileUILabel"]
    });

    var htmlFileUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "htmlFileUI"]
    });

    var isMobileViewUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "isMobileViewUILabel"]
    });

    var isMobileViewUI = new UIMaster.ui.checkbox
    ({
        ui: elementList[prefix + "isMobileViewUI"]
    });

    var partyTypeUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "partyTypeUILabel"]
    });

    var partyTypeUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "partyTypeUI"]
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
        ,subComponents: [prefix + "idUI",prefix + "htmlFileUILabel",prefix + "htmlFileUI",prefix + "isMobileViewUILabel",prefix + "isMobileViewUI",prefix + "partyTypeUILabel",prefix + "partyTypeUI"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [idUI,htmlFileUILabel,htmlFileUI,isMobileViewUILabel,isMobileViewUI,partyTypeUILabel,partyTypeUI,okbtn,cancelbtn,fieldPanel,actionPanel]
    });

    Form.idUI=idUI;

    Form.htmlFileUILabel=htmlFileUILabel;

    Form.htmlFileUI=htmlFileUI;

    Form.isMobileViewUILabel=isMobileViewUILabel;

    Form.isMobileViewUI=isMobileViewUI;

    Form.partyTypeUILabel=partyTypeUILabel;

    Form.partyTypeUI=partyTypeUI;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.fieldPanel=fieldPanel;

    Form.idUI=idUI;

    Form.htmlFileUILabel=htmlFileUILabel;

    Form.htmlFileUI=htmlFileUI;

    Form.isMobileViewUILabel=isMobileViewUILabel;

    Form.isMobileViewUI=isMobileViewUI;

    Form.partyTypeUILabel=partyTypeUILabel;

    Form.partyTypeUI=partyTypeUI;

    Form.actionPanel=actionPanel;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_analyzer_form_PageHelper */
        /* Construct_LAST:org_shaolin_bmdp_analyzer_form_PageHelper */
    };

    Form.Save = org_shaolin_bmdp_analyzer_form_PageHelper_Save;

    Form.Cancel = org_shaolin_bmdp_analyzer_form_PageHelper_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_analyzer_form_PageHelper_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.analyzer.form.PageHelper";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_analyzer_form_PageHelper */
/* Other_Func_LAST:org_shaolin_bmdp_analyzer_form_PageHelper */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_PageHelper_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_PageHelper_Save */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"saveDetail-20160402-083636",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_PageHelper_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_PageHelper_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_PageHelper_Cancel */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20160402-083636",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_PageHelper_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_PageHelper_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_PageHelper_invokeDynamicFunction */
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
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_PageHelper_invokeDynamicFunction */



