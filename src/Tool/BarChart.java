package Tool;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class BarChart {
	// 静态代码块
	static {
		// 解决乱码问题
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
		ChartFactory.setChartTheme(standardChartTheme);
	}

	public static String getBarChart(HttpSession session) throws IOException {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(510, "深圳", "苹果1");
		dataset.addValue(520, "深圳", "香蕉2");
		dataset.addValue(530, "深圳", "12");
		dataset.addValue(540, "深圳", "苹果3");
		dataset.addValue(550, "深圳", "苹果23");
		dataset.addValue(560, "深圳", "苹果23");
		JFreeChart chart = ChartFactory.createBarChart3D("苹果销售", "苹果种类", "销量", dataset, PlotOrientation.VERTICAL, true,
				true, true);
		String filename = ServletUtilities.saveChartAsJPEG(chart, 700, 500, session);
		System.out.println(filename);
		return filename;
	}

	public static String generatePieChart(HttpSession session) {

		DefaultPieDataset dpd = new DefaultPieDataset();
		dpd.setValue("JDK1.4", 60);
		dpd.setValue("JDK1.5", 15);
		dpd.setValue("JDK1.6", 25);
		dpd.setValue("JDK1.7", 25);
		dpd.setValue("JDK1.8", 25);

		JFreeChart chart = ChartFactory.createPieChart("开发者目前采用JDK版本分布", dpd, true, false, false);

		String fileName = "";
		try {
			fileName = ServletUtilities.saveChartAsPNG(chart, 600, 500, session);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileName;
	}
	/**
	 * 柱状图
	 * 
	 * @return
	 */
	public static String generateBarChart(HttpSession session) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(42, "语言", "JAVA");
		dataset.addValue(17, "语言", "C#");
		dataset.addValue(14, "语言", "C++");
		dataset.addValue(12, "语言", "C");
		dataset.addValue(3, "语言", "PHP");
		dataset.addValue(2, "语言", "JavsScript");
		dataset.addValue(1, "语言", "Objective-C");
		dataset.addValue(3, "语言", "Python");
		dataset.addValue(4, "语言", "其他");
		JFreeChart chart = ChartFactory.createBarChart3D("开发者第一编程语言分布情况", "开发语言", "百分比", dataset,
				PlotOrientation.VERTICAL, false, false, false);
		String fileName = "";
		try {
			fileName = ServletUtilities.saveChartAsPNG(chart, 700, 500, null, session);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(fileName);
		return fileName;
	}

	/**
	 * 曲线图
	 * 
	 * @return
	 */
	public static String generateGraphChart(HttpSession session) {

		TimeSeries timeSeries = new TimeSeries("最近30天流量趋势", Day.class);
		TimeSeriesCollection lineDataset = new TimeSeriesCollection();
		timeSeries.add(new Day(01, 06, 2014), 10200);
		timeSeries.add(new Day(05, 06, 2014), 11200);
		timeSeries.add(new Day(10, 06, 2014), 12000);
		timeSeries.add(new Day(15, 06, 2014), 13200);
		timeSeries.add(new Day(20, 06, 2014), 11600);
		timeSeries.add(new Day(25, 06, 2014), 13200);
		timeSeries.add(new Day(30, 06, 2014), 12500);
		lineDataset.addSeries(timeSeries);

		JFreeChart chart = ChartFactory.createTimeSeriesChart("访问量统计时间线", "月份", "访问量", lineDataset, true, true, true);

		XYPlot plot = chart.getXYPlot();
		// 设置网格背景颜色
		plot.setBackgroundPaint(Color.white);
		// 设置网格竖线颜色
		plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
		// 设置网格横线颜色
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

		// 设置子标题
		/*
		 * TextTitle subtitle = new TextTitle("2014-06-05至2014-06-30");
		 * chart.addSubtitle(subtitle);
		 */
		// 设置主标题
		chart.setTitle(new TextTitle("最近30天流量趋势"));
		chart.setAntiAlias(true);
		String fileName = "";
		try {
			fileName = ServletUtilities.saveChartAsPNG(chart, 600, 500, null, session);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileName;
	}

}
