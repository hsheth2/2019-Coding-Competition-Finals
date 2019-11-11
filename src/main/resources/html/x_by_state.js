async function setupChoroplethMap(target) {
    const response = await axios.get(`/api/${target}`);
    const rawData = response.data;

    // Create map instance
    const chart = am4core.create(target, am4maps.MapChart);

    // Set map definition
    chart.geodata = am4geodata_usaLow;

    // Set projection
    chart.projection = new am4maps.projections.AlbersUsa();

    // Create map polygon series
    var polygonSeries = chart.series.push(new am4maps.MapPolygonSeries());

    // Set min/max fill color for each area
    polygonSeries.heatRules.push({
        property: "fill",
        target: polygonSeries.mapPolygons.template,
        min: chart.colors.getIndex(1).brighten(1),
        max: chart.colors.getIndex(1).brighten(-0.3)
    });

    // Make map load polygon data (state shapes and names) from GeoJSON
    polygonSeries.useGeodata = true;

    // Set heatmap values for each state
    const data = Object.entries(rawData).map(([key, value]) => {
        const roundedValue = Math.round(value * 10) / 10;
        return {
            id: `US-${key}`,
            value: roundedValue,
        }
    });
    console.log(data);
    polygonSeries.data = data;

    // Set up heat legend
    let heatLegend = chart.createChild(am4maps.HeatLegend);
    heatLegend.series = polygonSeries;
    heatLegend.align = "right";
    heatLegend.valign = "bottom";
    heatLegend.width = am4core.percent(20);
    heatLegend.marginRight = am4core.percent(4);

    // Configure series tooltip
    const polygonTemplate = polygonSeries.mapPolygons.template;
    polygonTemplate.tooltipText = "{name}: {value}";
    polygonTemplate.nonScalingStroke = true;
    polygonTemplate.strokeWidth = 0.5;

    // Create hover state and set alternative fill color
    const hs = polygonTemplate.states.create("hover");
    hs.properties.fill = am4core.color("#3c5bdc");
}

am4core.ready(function() {
    am4core.useTheme(am4themes_animated);

    setupChoroplethMap('premiums_by_state');
    setupChoroplethMap('age_by_state');
    setupChoroplethMap('accidents_by_state')
});
