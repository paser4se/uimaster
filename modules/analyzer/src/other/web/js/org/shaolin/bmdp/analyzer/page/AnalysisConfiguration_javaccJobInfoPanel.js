
    var javaCCJobInfoTable = new UIMaster.ui.objectlist
    ({
        ui: elementList["javaCCJobInfoTable"]
    });

    var javaccJobInfoPanel = new UIMaster.ui.panel
    ({
        ui: elementList["javaccJobInfoPanel"]
        ,items: [javaCCJobInfoTable]
    });

    defaultname.javaCCJobInfoTable=javaCCJobInfoTable;
defaultname.javaccJobInfoPanel = javaccJobInfoPanel;
defaultname.javaccJobInfoPanel.init();

defaultname.javaccJobInfoPanel.Form.items.push(elementList['javaccJobInfoPanel']);

