
    var sessionHistoryTable = new UIMaster.ui.objectlist
    ({
        ui: elementList["sessionHistoryTable"]
    });

    var sessionHistoryTablePanel = new UIMaster.ui.panel
    ({
        ui: elementList["sessionHistoryTablePanel"]
        ,items: [sessionHistoryTable]
    });

    defaultname.sessionHistoryTable=sessionHistoryTable;
defaultname.sessionHistoryTablePanel = sessionHistoryTablePanel;
defaultname.sessionHistoryTablePanel.init();

defaultname.Form.items.push(elementList['sessionHistoryTablePanel']);

