package testNG;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import utilities.ConfigDataProvider;
import utilities.ExcelDataProvider;
import utilities.Helper;

public class BaseTest
{
	public static WebDriver driver;
	public ConfigDataProvider config;
	public ExcelDataProvider excel;
	public ExtentReports report;
	public ExtentTest logger;

	
	@BeforeClass
	public void setUp() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}
	
	@AfterClass
	public void tearDown() {
		//Close chromedriver
		driver.close();
	}
	
	@AfterMethod
	public void tearDownMethod(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE)
		{
			Helper.captureScreenshot(driver);
		}
		
		report.flush();
	}

	@BeforeSuite
	public void setUpSuite()
	{
		/// Extract chromedriver to local directory if it does not exist
		try
		{
			extractChromeDriver();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		// set chromedriver property
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/chromedriver.exe");
		
		// Initialise Utilities
		config = new ConfigDataProvider();
		excel = new ExcelDataProvider();
		
		// Extent Reporter setup
		ExtentHtmlReporter extent = new ExtentHtmlReporter(new File(System.getProperty("user.dir") + "/test-output/ExtentReport/Report.html"));
		report = new ExtentReports();
		report.attachReporter(extent);
	}

	@AfterSuite
	public void afterSuite()
	{
//		kill chromedriver on program shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, "Shutdown-thread"));

	}

	public static void extractChromeDriver() throws FileNotFoundException
	{
		// Create temp chromedriver
		File file = new File(System.getProperty("user.dir") + "/chromedriver.exe");
		if (!file.exists())
		{
			InputStream in = BaseTest.class.getClass().getResourceAsStream("/chromedriver.exe");
			FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + "/chromedriver.exe");
			System.out.println(System.getProperty("user.dir"));

			try
			{
				copyStream(in, out);
				out.close();
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static void copyStream(InputStream in, OutputStream out) throws IOException
	{
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1)
		{
			out.write(buffer, 0, read);
		}
	}

}
