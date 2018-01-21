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
	// ��̬�����
	static {
		// �����������
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		standardChartTheme.setExtraLargeFont(new Font("����", Font.BOLD, 20));
		standardChartTheme.setRegularFont(new Font("����", Font.PLAIN, 15));
		standardChartTheme.setLargeFont(new Font("����", Font.PLAIN, 15));
		ChartFactory.setChartTheme(standardChartTheme);
	}

	public static String getBarChart(HttpSession session) throws IOException {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(510, "����", "ƻ��1");
		dataset.addValue(520, "����", "�㽶2");
		dataset.addValue(530, "����", "12");
		dataset.addValue(540, "����", "ƻ��3");
		dataset.addValue(550, "����", "ƻ��23");
		dataset.addValue(560, "����", "ƻ��23");
		JFreeChart chart = ChartFactory.createBarChart3D("ƻ������", "ƻ������", "����", dataset, PlotOrientation.VERTICAL, true,
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

		JFreeChart chart = ChartFactory.createPieChart("������Ŀǰ����JDK�汾�ֲ�", dpd, true, false, false);

		String fileName = "";
		try {
			fileName = ServletUtilities.saveChartAsPNG(chart, 600, 500, session);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileName;
	}
	/**
	 * ��״ͼ
	 * 
	 * @return
	 */
	public static String generateBarChart(HttpSession session) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(42, "����", "JAVA");
		dataset.addValue(17, "����", "C#");
		dataset.addValue(14, "����", "C++");
		dataset.addValue(12, "����", "C");
		dataset.addValue(3, "����", "PHP");
		dataset.addValue(2, "����", "JavsScript");
		dataset.addValue(1, "����", "Objective-C");
		dataset.addValue(3, "����", "Python");
		dataset.addValue(4, "����", "����");
		JFreeChart chart = ChartFactory.createBarChart3D("�����ߵ�һ������Էֲ����", "��������", "�ٷֱ�", dataset,
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
	 * ����ͼ
	 * 
	 * @return
	 */
	public static String generateGraphChart(HttpSession session) {

		TimeSeries timeSeries = new TimeSeries("���30����������", Day.class);
		TimeSeriesCollection lineDataset = new TimeSeriesCollection();
		timeSeries.add(new Day(01, 06, 2014), 10200);
		timeSeries.add(new Day(05, 06, 2014), 11200);
		timeSeries.add(new Day(10, 06, 2014), 12000);
		timeSeries.add(new Day(15, 06, 2014), 13200);
		timeSeries.add(new Day(20, 06, 2014), 11600);
		timeSeries.add(new Day(25, 06, 2014), 13200);
		timeSeries.add(new Day(30, 06, 2014), 12500);
		lineDataset.addSeries(timeSeries);

		JFreeChart chart = ChartFactory.createTimeSeriesChart("������ͳ��ʱ����", "�·�", "������", lineDataset, true, true, true);

		XYPlot plot = chart.getXYPlot();
		// �������񱳾���ɫ
		plot.setBackgroundPaint(Color.white);
		// ��������������ɫ
		plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
		// �������������ɫ
		plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

		// �����ӱ���
		/*
		 * TextTitle subtitle = new TextTitle("2014-06-05��2014-06-30");
		 * chart.addSubtitle(subtitle);
		 */
		// ����������
		chart.setTitle(new TextTitle("���30����������"));
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
