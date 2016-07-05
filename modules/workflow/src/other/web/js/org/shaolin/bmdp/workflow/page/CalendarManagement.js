/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_page_CalendarManagement(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var container = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "container"]
        ,items: []
        ,subComponents: []
    });

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,items: [container]
    });

    Form.container=container;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_page_CalendarManagement */
        /* Construct_LAST:org_shaolin_bmdp_workflow_page_CalendarManagement */
    };

    Form.openPersonalAccount = org_shaolin_bmdp_workflow_page_CalendarManagement_openPersonalAccount;

    Form.initPageJs = org_shaolin_bmdp_workflow_page_CalendarManagement_initPageJs;

    Form.finalizePageJs = org_shaolin_bmdp_workflow_page_CalendarManagement_finalizePageJs;

    Form.__AJAXSubmit = false;
    
    Form.__entityName="org.shaolin.bmdp.workflow.page.CalendarManagement";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_page_CalendarManagement */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_page_CalendarManagement */

    /* auto generated eventlistener function declaration */
    function org_shaolin_bmdp_workflow_page_CalendarManagement_openPersonalAccount(eventsource,event) {/* Gen_First:org_shaolin_bmdp_workflow_page_CalendarManagement_openPersonalAccount */
        var o = this;
        var UIEntity = this;

        // cal ajax function. 

        UIMaster.triggerServerEvent(UIMaster.getUIID(eventsource),"openPersonalAccount234324",UIMaster.getValue(eventsource),o.__entityName);
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_CalendarManagement_openPersonalAccount */


    function org_shaolin_bmdp_workflow_page_CalendarManagement_initPageJs(){/* Gen_First:org_shaolin_bmdp_workflow_page_CalendarManagement_initPageJs */
        var constraint_result = true;
        var UIEntity = this;
{
            $(Form.container).fullCalendar({
                lang: "zh-cn",
                events: [],
                editable: true,eventLimit: true, 
                eventClick: function(event) {
                   alert(event);
                }
            });
            }
    }/* Gen_Last:org_shaolin_bmdp_workflow_page_CalendarManagement_initPageJs */


    function org_shaolin_bmdp_workflow_page_CalendarManagement_finalizePageJs(){/* Gen_First:org_shaolin_bmdp_workflow_page_CalendarManagement_finalizePageJs */

    }/* Gen_Last:org_shaolin_bmdp_workflow_page_CalendarManagement_finalizePageJs */



