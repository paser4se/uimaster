/* null */
/* auto generated constructor */
function org_shaolin_bmdp_analyzer_form_Job(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var idUI = new UIMaster.ui.hidden
    ({
        ui: elementList[prefix + "idUI"]
    });

    var jarPathUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "jarPathUILabel"]
    });

    var jarPathUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "jarPathUI"]
    });

    var mainClassUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "mainClassUILabel"]
    });

    var mainClassUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "mainClassUI"]
    });

    var taskIdUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "taskIdUILabel"]
    });

    var taskIdUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "taskIdUI"]
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
        ,subComponents: [prefix + "idUI",prefix + "jarPathUILabel",prefix + "jarPathUI",prefix + "mainClassUILabel",prefix + "mainClassUI",prefix + "taskIdUILabel",prefix + "taskIdUI"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [idUI,jarPathUILabel,jarPathUI,mainClassUILabel,mainClassUI,taskIdUILabel,taskIdUI,okbtn,cancelbtn,fieldPanel,actionPanel]
    });

    Form.idUI=idUI;

    Form.jarPathUILabel=jarPathUILabel;

    Form.jarPathUI=jarPathUI;

    Form.mainClassUILabel=mainClassUILabel;

    Form.mainClassUI=mainClassUI;

    Form.taskIdUILabel=taskIdUILabel;

    Form.taskIdUI=taskIdUI;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.fieldPanel=fieldPanel;

    Form.idUI=idUI;

    Form.jarPathUILabel=jarPathUILabel;

    Form.jarPathUI=jarPathUI;

    Form.mainClassUILabel=mainClassUILabel;

    Form.mainClassUI=mainClassUI;

    Form.taskIdUILabel=taskIdUILabel;

    Form.taskIdUI=taskIdUI;

    Form.actionPanel=actionPanel;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_analyzer_form_Job */
        /* Construct_LAST:org_shaolin_bmdp_analyzer_form_Job */
    };

    Form.Save = org_shaolin_bmdp_analyzer_form_Job_Save;

    Form.Cancel = org_shaolin_bmdp_analyzer_form_Job_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_analyzer_form_Job_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.analyzer.form.Job";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_analyzer_form_Job */
/* Other_Func_LAST:org_shaolin_bmdp_analyzer_form_Job */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_Job_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_Job_Save */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"saveDetail-20151205-151421",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_Job_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_Job_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_Job_Cancel */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20151205-151421",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_Job_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_Job_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_Job_invokeDynamicFunction */
        var o = this;
        var UIEntity = this;

        new UIMaster.ui.dialog({dialogType: UIMaster.ui.dialog.CONFIRM_DIALOG,message:'Continue?',messageType:UIMaster.ui.dialog.Warning,optionType:UIMaster.ui.dialog.YES_NO_OPTION,title:'',height:150,width:300,handler: function() {

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),o.__entityName);

        
        }
        }).open();
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_Job_invokeDynamicFunction */



