/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routefare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.MeterGaugeChartModel;
import routefare.sessionbean.DataAnalyticsSessionBeanLocal;

/**
 *
 * @author zhuming
 */
@Named(value = "dataAnalyticsManagedBean")
@SessionScoped
public class DataAnalyticsManagedBean implements Serializable {

    @EJB
    private DataAnalyticsSessionBeanLocal dataAnalyticsSessionBeanLocal;

    private BarChartModel passengerVolumeTime;
    private MeterGaugeChartModel fareboxModel;
    private double expenses;
    private double fareboxRatio=0;
    private LineChartModel passengerVolumeStation;
    private List<String> routes;
    private String theRoute;
    private boolean showChart;
    private BarChartModel passengerVolumeStationTime;

    @PostConstruct
    public void init() {
        createAnimatedModels();
        createMeterGaugeModels();
        this.routes = dataAnalyticsSessionBeanLocal.getRoutes();
        createAnimatedModels_station();
        createBarModelInteractive();
        showChart = false;
    }
    
    ////////////////////passenger volume by hour////////////////////
    private void createAnimatedModels() {
        passengerVolumeTime = initBarModel();
        passengerVolumeTime.setTitle("Hourly Passenger Volume");
        passengerVolumeTime.setAnimate(true);
        //animatedModel2.setLegendPosition("ne");
        passengerVolumeTime.setShadow(false);
        Axis xAxis;
        xAxis = passengerVolumeTime.getAxis(AxisType.X);
        xAxis.setLabel("Passenger Volume");
        Axis yAxis = passengerVolumeTime.getAxis(AxisType.Y);
        yAxis.setLabel("Hours");
        xAxis.setMin(0);
        xAxis.setMax(100);
    }

    public BarChartModel getPassengerVolumeTime() {
        return passengerVolumeTime;
    }

    public void setPassengerVolumeTime(BarChartModel passengerVolumeTime) {
        this.passengerVolumeTime = passengerVolumeTime;
    }

    private BarChartModel initBarModel() {
        HorizontalBarChartModel model = new HorizontalBarChartModel();

        ChartSeries boys = new ChartSeries();
        
        List<String> x = dataAnalyticsSessionBeanLocal.getHourList();
        List<Integer> y = dataAnalyticsSessionBeanLocal.countNumberOfPassengersPerHour(x);
        for (int i = 0; i < x.size(); i++) {
            int yValue = y.get(i);
            boys.set(x.get(i), y.get(i));
        }
        model.addSeries(boys);
        return model;
    }
    
    ////////////////////farebox recovery ratio////////////////////
    public MeterGaugeChartModel getFareboxModel() {
        return fareboxModel;
    }
    
    public double getFareboxRatio() {
        return fareboxRatio;
    }

    public void setFareboxRatio(double fareboxRatio) {
        this.fareboxRatio = fareboxRatio;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public MeterGaugeChartModel initMeterGaugeModel() {
        List<Number> intervals = new ArrayList<Number>() {
            {
                add(30);
                add(60);
                add(100);
                add(200);
            }
        };

        return new MeterGaugeChartModel((Number)fareboxRatio, intervals);
    }

    public void createMeterGaugeModels() {
        fareboxModel = initMeterGaugeModel();
        fareboxModel.setTitle("Farebox Recovery Ratio");
        fareboxModel.setSeriesColors("cc6666,E7E658,93b75f,66cc66");
        //fareboxModel.setGaugeLabel("km/h");
        //fareboxModel.setGaugeLabelPosition("bottom");
        //fareboxModel.setShowTickLabels(false);
        fareboxModel.setLabelHeightAdjust(110);
        fareboxModel.setIntervalOuterRadius(100);
    }

    public void goCalculate() {
        double fbRatio = dataAnalyticsSessionBeanLocal.calculateFareboxRatio(expenses);
        if(fbRatio>200){
            this.fareboxRatio = 199;
        }
        this.fareboxRatio = fbRatio;
    }
    
    public void update(){
         List<Number> intervals = new ArrayList<Number>(){{
            add(30);
            add(60);
            add(100);
            add(200);
        }};
        
        //return new MeterGaugeChartModel(110, intervals);
        goCalculate();
        MeterGaugeChartModel mg = new MeterGaugeChartModel((Number)fareboxRatio, intervals);
        fareboxModel = mg;
        fareboxModel.setTitle("Farebox Recovery Ratio");
        fareboxModel.setSeriesColors("cc6666,E7E658,93b75f,66cc66");
        //fareboxModel.setGaugeLabel("km/h");
        //fareboxModel.setGaugeLabelPosition("bottom");
        //fareboxModel.setShowTickLabels(false);
        fareboxModel.setLabelHeightAdjust(110);
        fareboxModel.setIntervalOuterRadius(100);
    }

    /////////////////////passenger volumn by station////////////////////
    public LineChartModel getPassengerVolumeStation() {
        return passengerVolumeStation;
    }

    public List<String> getRoutes() {
        return routes;
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
    }

    public String getTheRoute() {
        return theRoute;
    }

    public void setTheRoute(String theRoute) {
        this.theRoute = theRoute;
    }

    public boolean isShowChart() {
        return showChart;
    }

    public void setShowChart(boolean showChart) {
        this.showChart = showChart;
    }

    public BarChartModel getPassengerVolumeStationTime() {
        return passengerVolumeStationTime;
    }

    public void setPassengerVolumeStationTime(BarChartModel passengerVolumeStationTime) {
        this.passengerVolumeStationTime = passengerVolumeStationTime;
    }

    private void createAnimatedModels_station() {
        passengerVolumeStation = initLinearModel();
        passengerVolumeStation.setTitle("Passenger Volume by Station for "+theRoute);
        passengerVolumeStation.setAnimate(true);
        passengerVolumeStation.setLegendPosition("ne");
        Axis yAxis = passengerVolumeStation.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(100);
    }
    
    private LineChartModel initLinearModel() {
        LineChartModel model = new LineChartModel();
 
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Tap In");
 
        series1.set(0, 0);
        series1.set(0, 0);
        series1.set(0, 0);
        series1.set(0, 0);
        series1.set(0, 0);
 
        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Tap Out");
 
        series2.set(0, 0);
        series2.set(0, 0);
        series2.set(0, 0);
        series2.set(0, 0);
        series2.set(0, 0);
 
        model.addSeries(series1);
        model.addSeries(series2);
        model.getAxes().put(AxisType.X, new CategoryAxis("Station Number"));
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel("Passenger Volume");
        return model;
    }
    
    public void updateVolumeStationLine(){
        LineChartModel model = new LineChartModel();
 
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Tap in");
 
        List<Integer> x1 = dataAnalyticsSessionBeanLocal.getStationXaxis(theRoute);
        List<Integer> y1 = dataAnalyticsSessionBeanLocal.countNumberOfPassengersPerStation_tapin(dataAnalyticsSessionBeanLocal.getSortedStationCode(theRoute));
        for (int i = 0; i < x1.size(); i++) {
            series1.set(x1.get(i), y1.get(i));
        }
        
        
        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Tap out");
 
        List<Integer> x2 = dataAnalyticsSessionBeanLocal.getStationXaxis(theRoute);
        List<Integer> y2 = dataAnalyticsSessionBeanLocal.countNumberOfPassengersPerStation_tapout(dataAnalyticsSessionBeanLocal.getSortedStationCode(theRoute));
        for (int i = 0; i < x2.size(); i++) {
            series2.set(x2.get(i), y2.get(i));
        }
 
        model.addSeries(series1);
        model.addSeries(series2);
        passengerVolumeStation = model;
        passengerVolumeStation.setTitle("Passenger Volume by Station for "+theRoute);
        passengerVolumeStation.setAnimate(true);
        passengerVolumeStation.setLegendPosition("se");
        passengerVolumeStation.getAxes().put(AxisType.X, new CategoryAxis("Station Number"));
        Axis yAxis = passengerVolumeStation.getAxis(AxisType.Y);
        yAxis.setLabel("Passenger Volume");
        yAxis.setMin(0);
        yAxis.setMax(100);
    }
    
    public void createBarModelInteractive() {
        passengerVolumeStationTime = initBarModel1();
         
        passengerVolumeStationTime.setTitle("Passenger Volume by Period");
        passengerVolumeStationTime.setLegendPosition("ne");
        passengerVolumeStationTime.setStacked(true);
        
        Axis xAxis = passengerVolumeStationTime.getAxis(AxisType.X);
        xAxis.setLabel("Passenger Volume");
         
        Axis yAxis = passengerVolumeStationTime.getAxis(AxisType.Y);
        yAxis.setLabel("Hours");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }
    private BarChartModel initBarModel1() {
        HorizontalBarChartModel model = new HorizontalBarChartModel();
 
        ChartSeries tapIn = new ChartSeries();
        tapIn.setLabel("Tap In");
        tapIn.set("0", 0);
        tapIn.set("0", 0);
        tapIn.set("0", 0);
        tapIn.set("0", 0);
        tapIn.set("0", 0);
 
        ChartSeries tapOut = new ChartSeries();
        tapOut.setLabel("Tap Out");
        tapOut.set("0", 0);
        tapOut.set("0", 0);
        tapOut.set("0", 0);
        tapOut.set("0", 0);
        tapOut.set("0", 0);
        
        model.addSeries(tapIn);
        model.addSeries(tapOut);
         
        return model;
    }
    public void itemSelect(ItemSelectEvent event) {
        showChart = true;
        
        HorizontalBarChartModel model = new HorizontalBarChartModel();
        int index = event.getItemIndex();
        System.out.println("lalalalal index = "+index);
        List<Integer> x = dataAnalyticsSessionBeanLocal.getStationXaxis(theRoute);
        
        List<String> x1 = dataAnalyticsSessionBeanLocal.getHourList();
        List<Integer> y1 = dataAnalyticsSessionBeanLocal.countNumberOfPassengersPerPeriod_tapin(theRoute, index, x1, x);
        List<Integer> y2 = dataAnalyticsSessionBeanLocal.countNumberOfPassengersPerPeriod_tapout(theRoute, index, x1, x);
        
        ChartSeries tapIn = new ChartSeries();
        tapIn.setLabel("Tap In");
        
        for (int i = 0; i < x1.size(); i++) {
            tapIn.set(x1.get(i), y1.get(i));
            System.out.println("tapin volume = "+y1.get(i));
        }
        
        ChartSeries tapOut = new ChartSeries();
        tapOut.setLabel("Tap Out");
        for (int i = 0; i < x1.size(); i++) {
            tapOut.set(x1.get(i), y2.get(i));
            System.out.println("tapin volume = "+y2.get(i));
        }
 
        model.addSeries(tapIn);
        model.addSeries(tapOut);
        
        passengerVolumeStationTime = model;
         
        passengerVolumeStationTime.setTitle("Hourly Passenger Volume");
        passengerVolumeStationTime.setLegendPosition("ne");
        passengerVolumeStationTime.setShadow(false);
        passengerVolumeStationTime.setStacked(true);
        
        Axis xAxis = passengerVolumeStationTime.getAxis(AxisType.X);
        xAxis.setLabel("Passenger Volume");
         
        Axis yAxis = passengerVolumeStationTime.getAxis(AxisType.Y);
        yAxis.setLabel("Hours");
        yAxis.setMin(0);
        yAxis.setMax(10);
    }
}
