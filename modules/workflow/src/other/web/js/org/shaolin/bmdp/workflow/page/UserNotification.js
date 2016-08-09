/* null */
/* auto generated constructor */
function org_shaolin_bmdp_workflow_page_UserNotification(json)
{
    var prefix = (typeof(json) == "string") ? json : json.prefix; 
    var notificationRef = new org_shaolin_bmdp_workflow_form_NotificationBoard({"prefix":prefix + "notificationRef."});

    var Form = new UIMaster.ui.panel
    ({
        ui: elementList[prefix + "Form"]
        ,uiskin: "org.shaolin.uimaster.page.skin.TitlePanel"
        ,items: [notificationRef]
    });

    Form.notificationRef=notificationRef;

    Form.user_constructor = function()
    {
        /* Construct_FIRST:org_shaolin_bmdp_workflow_page_UserNotification */
        /* Construct_LAST:org_shaolin_bmdp_workflow_page_UserNotification */
    };

    Form.initPageJs = org_shaolin_bmdp_workflow_page_UserNotification_initPageJs;

    Form.finalizePageJs = org_shaolin_bmdp_workflow_page_UserNotification_finalizePageJs;

    Form.__AJAXSubmit = false;
    
    Form.__entityName="org.shaolin.bmdp.workflow.page.UserNotification";

    Form.init();
    return Form;
};

    /* EventHandler Functions */
/* Other_Func_FIRST:org_shaolin_bmdp_workflow_page_UserNotification */
/* Other_Func_LAST:org_shaolin_bmdp_workflow_page_UserNotification */

    function org_shaolin_bmdp_workflow_page_UserNotification_initPageJs(){/* Gen_First:org_shaolin_bmdp_workflow_page_UserNotification_initPageJs */
        var constraint_result = true;
        var UIEntity = this;

    }/* Gen_Last:org_shaolin_bmdp_workflow_page_UserNotification_initPageJs */


    function org_shaolin_bmdp_workflow_page_UserNotification_finalizePageJs(){/* Gen_First:org_shaolin_bmdp_workflow_page_UserNotification_finalizePageJs */

    }/* Gen_Last:org_shaolin_bmdp_workflow_page_UserNotification_finalizePageJs */



