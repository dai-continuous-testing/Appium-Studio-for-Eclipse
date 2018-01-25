package com.experitest.auto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.remote.DesiredCapabilities;

public class BaseTest {

	protected DesiredCapabilities dc = new DesiredCapabilities();
	protected Properties cloudProperties = new Properties();

	public void init(String deviceQuery) throws Exception {
		initCloudProperties();
		dc.setCapability("deviceQuery", adhocDevice(deviceQuery));
		dc.setCapability("reportDirectory", "reports");
		dc.setCapability("reportFormat", "xml");
		String accessKey = getProperty("accessKey", cloudProperties);
		if(accessKey != null && !accessKey.isEmpty()){
			dc.setCapability("accessKey", getProperty("accessKey", cloudProperties));
		} else {
			dc.setCapability("user", getProperty("username", cloudProperties));
			dc.setCapability("password", getProperty("password", cloudProperties));		}
		
		// In case your user is assign to a single project leave empty,
		// otherwise please specify the project name
		dc.setCapability("project", getProperty("project", cloudProperties));

	}

	protected String getProperty(String property, Properties props) throws FileNotFoundException, IOException {
		if (System.getProperty(property) != null) {
			return System.getProperty(property);
		} else if (System.getenv().containsKey(property)) {
			return System.getenv(property);
		} else if (props != null) {
			return props.getProperty(property);
		}
		return null;
	}

	private void initCloudProperties() throws FileNotFoundException, IOException {
		FileReader fr = new FileReader("cloud.properties");
		cloudProperties.load(fr);
		fr.close();
	}

	private static synchronized String adhocDevice(String deviceQuery) {
		try {
			File jarLocation = (System.getProperty("os.name").toUpperCase().contains("WIN"))
					? new File(System.getenv("APPDATA"), ".mobiledata")
					: new File(System.getProperty("user.home") + "/Library/Application " + "Support", ".mobiledata");
			File adhocProperties = new File(jarLocation, "adhoc.properties");
			if (adhocProperties.exists()) {
				Properties prop = new Properties();
				FileReader reader = new FileReader(adhocProperties);
				try {
					prop.load(reader);
				} finally {
					reader.close();
				}
				adhocProperties.delete();
				return "@serialnumber='" + prop.getProperty("serial") + "'";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceQuery;
	}

}
