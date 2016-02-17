/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_form_ServerNodeInfo(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var ipAddressUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "ipAddressUILabel"]
    });

    var ipAddressUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "ipAddressUI"]
    });

    var portUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "portUILabel"]
    });

    var portUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "portUI"]
    });

    var protocolUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "protocolUILabel"]
    });

    var protocolUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "protocolUI"]
    });

    var domainUILabel = new UIMaster.ui.label
    ({
        ui: elementList[prefix + "domainUILabel"]
    });

    var domainUI = new UIMaster.ui.textfield
    ({
        ui: elementList[prefix + "domainUI"]
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
        ,subComponents: [prefix + "ipAddressUILabel",prefix + "ipAddressUI",prefix + "portUILabel",prefix + "portUI",prefix + "protocolUILabel",prefix + "protocolUI",prefix + "domainUILabel",prefix + "domainUI"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [ipAddressUILabel,ipAddressUI,portUILabel,portUI,protocolUILabel,protocolUI,domainUILabel,domainUI,okbtn,cancelbtn,fieldPanel,actionPanel]
    });

    Form.ipAddressUILabel=ipAddressUILabel;

    Form.ipAddressUI=ipAddressUI;

    Form.portUILabel=portUILabel;

    Form.portUI=portUI;

    Form.protocolUILabel=protocolUILabel;

    Form.protocolUI=protocolUI;

    Form.domainUILabel=domainUILabel;

    Form.domainUI=domainUI;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.fieldPanel=fieldPanel;

    Form.ipAddressUILabel=ipAddressUILabel;

    Form.ipAddressUI=ipAddressUI;

    Form.portUILabel=portUILabel;

    Form.portUI=portUI;

    Form.protocolUILabel=protocolUILabel;

    Form.protocolUI=protocolUI;

    Form.domainUILabel=domainUILabel;

    Form.domainUI=domainUI;

    Form.actionPanel=actionPanel;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_form_ServerNodeInfo */
        /* Construct_LAST:org_shaolin_bmdp_workflow_form_ServerNodeInfo */
    };

    Form.Save = org_shaolin_bmdp_workflow_form_ServerNodeInfo_Save;

    Form.Cancel = org_shaolin_bmdp_workflow_form_ServerNodeInfo_Cancel;

    Form.invokeDynamicFunction = org_shaolin_bmdp_workflow_form_ServerNodeInfo_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.workflow.form.ServerNodeInfo";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_form_ServerNodeInfo */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_form_ServerNodeInfo */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ServerNodeInfo_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ServerNodeInfo_Save */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"saveDetail-20160127-123227",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ServerNodeInfo_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ServerNodeInfo_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ServerNodeInfo_Cancel */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail-20160127-123227",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ServerNodeInfo_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_form_ServerNodeInfo_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_form_ServerNodeInfo_invokeDynamicFunction */
        var o = this;
        var UIEntity = this;

        new UIMaster.ui.dialog({dialogType: UIMaster.ui.dialog.CONFIRM_DIALOG,message:'????',messageType:UIMaster.ui.dialog.Warning,optionType:UIMaster.ui.dialog.YES_NO_OPTION,title:'',height:150,width:300,handler: function() {

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),event,UIMaster.getValue(eventsource),o.__entityName);

        
        }
        }).open();
    }/* Gen_Last:org_shaolin_bmdp_workflow_form_ServerNodeInfo_invokeDynamicFunction */



