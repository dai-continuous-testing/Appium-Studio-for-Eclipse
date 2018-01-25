package com.experitest.auto;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class IOSDemoTest extends BaseTest {
	protected IOSDriver<IOSElement> driver = null;

	@BeforeMethod
	@Parameters("deviceQuery")
	public void setUp(@Optional("@os='ios'") String deviceQuery) throws Exception {
		init(deviceQuery);
		// Init application / device capabilities
		//dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.ExperiBank");
		//dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank");
		dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.ExperiBank");
		dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank");
		dc.setCapability("instrumentApp", true);
		
		dc.setCapability("dontGoHomeOnQuit", true);
		
		dc.setCapability("testName", "IOSDemoTest");
		driver = new IOSDriver<>(new URL(getProperty("url",cloudProperties) + "/wd/hub"), dc);
	}

	@Test
	public void test() {
		driver.findElement(in.Repo.obj("Login_page.Username")).sendKeys("company");
		driver.findElement(in.Repo.obj("Login_page.Password")).sendKeys("company");
		driver.findElement(in.Repo.obj("Login_page.Login_button")).click();
		driver.context("WEBVIEW_1");
		new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(in.Repo.obj("main_page.Your_balance")));
		driver.context("NATIVE_APP_INSTRUMENTED");
		driver.findElement(in.Repo.obj("main_page.Logout")).click();
	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}

}
