/* null */
/* auto generated constructor */
function org_shaolin_bmdp_analyzer_form_TableSelector(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var tableListUI = new UIMaster.ui.list
    ({
        ui: elementList[prefix + "tableListUI"]
    });

    var showSQL = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "showSQL"]
    });

    var okbtn = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "okbtn"]
    });

    var cancelbtn = new UIMaster.ui.button
    ({
        ui: elementList[prefix + "cancelbtn"]
    });

    var tableDetail = new org_shaolin_bmdp_analyzer_form_TableStatistic({"prefix":prefix + "tableDetail."});

    var actionPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "actionPanel"]
        ,items: []
        ,subComponents: [prefix + "showSQL",prefix + "okbtn",prefix + "cancelbtn"]
    });

    var secondPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "secondPanel"]
        ,uiskin: "org.shaolin.uimaster.page.skin.TitlePanel"
        ,items: []
        ,subComponents: [prefix + "tableDetail"]
    });

    var firstPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "firstPanel"]
        ,items: []
        ,subComponents: [prefix + "tableListUI"]
    });

    var fieldPanel = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "fieldPanel"]
        ,items: []
        ,subComponents: [prefix + "firstPanel",prefix + "secondPanel"]
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [tableListUI,showSQL,okbtn,cancelbtn,tableDetail,fieldPanel,firstPanel,secondPanel,actionPanel]
    });

    Form.tableListUI=tableListUI;

    Form.showSQL=showSQL;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.tableDetail=tableDetail;

    Form.fieldPanel=fieldPanel;

    Form.firstPanel=firstPanel;

    Form.tableListUI=tableListUI;

    Form.secondPanel=secondPanel;

    Form.tableDetail=tableDetail;

    Form.firstPanel=firstPanel;

    Form.tableListUI=tableListUI;

    Form.secondPanel=secondPanel;

    Form.tableDetail=tableDetail;

    Form.actionPanel=actionPanel;

    Form.showSQL=showSQL;

    Form.okbtn=okbtn;

    Form.cancelbtn=cancelbtn;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_analyzer_form_TableSelector */

        
        { 
        }
            /* Construct_LAST:org_shaolin_bmdp_analyzer_form_TableSelector */
    };

    Form.selectTable = org_shaolin_bmdp_analyzer_form_TableSelector_selectTable;

    Form.Cancel = org_shaolin_bmdp_analyzer_form_TableSelector_Cancel;

    Form.Save = org_shaolin_bmdp_analyzer_form_TableSelector_Save;

    Form.showSQLFunc = org_shaolin_bmdp_analyzer_form_TableSelector_showSQLFunc;

    Form.invokeDynamicFunction = org_shaolin_bmdp_analyzer_form_TableSelector_invokeDynamicFunction;

    Form.__entityName="org.shaolin.bmdp.analyzer.form.TableSelector";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_analyzer_form_TableSelector */
/* Other_Func_LAST:org_shaolin_bmdp_analyzer_form_TableSelector */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_TableSelector_selectTable(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_TableSelector_selectTable */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"selectTable-20150604847",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_TableSelector_selectTable */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_TableSelector_Cancel(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_TableSelector_Cancel */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"cancelDetail1358686533",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_TableSelector_Cancel */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_TableSelector_Save(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_TableSelector_Save */
        var o = this;
        var UIEntity = this;

        {
            this.tableDetail.itemTable.syncBodyDataToServer();
        }
        
        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"Save-20160412-1804",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_TableSelector_Save */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_TableSelector_showSQLFunc(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_TableSelector_showSQLFunc */
        var o = this;
        var UIEntity = this;

        {
            this.tableDetail.itemTable.syncBodyDataToServer();
        }
        
        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"showSQLFunc-20160412-1804",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_TableSelector_showSQLFunc */


    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_analyzer_form_TableSelector_invokeDynamicFunction(eventsource,event) {/* Gen_First:org_shaolin_bmdp_analyzer_form_TableSelector_invokeDynamicFunction */
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
    }/* Gen_Last:org_shaolin_bmdp_analyzer_form_TableSelector_invokeDynamicFunction */



