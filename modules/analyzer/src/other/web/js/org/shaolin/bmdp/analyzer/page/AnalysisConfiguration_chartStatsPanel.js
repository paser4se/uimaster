
    var chartStatsTable = new UIMaster.ui.objectlist
    ({
        ui: elementList["chartStatsTable"]
    });

    var chartStatsPanel = new UIMaster.ui.panel
    ({
        ui: elementList["chartStatsPanel"]
        ,items: [chartStatsTable]
    });

    defaultname.chartStatsTable=chartStatsTable;
defaultname.chartStatsPanel = chartStatsPanel;
defaultname.chartStatsPanel.init();

defaultname.Form.items.push(elementList['chartStatsPanel']);

