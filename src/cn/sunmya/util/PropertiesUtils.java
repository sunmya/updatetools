package cn.sunmya.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

/**
 * Properties Util函数.
 * 
 * 
 */
public class PropertiesUtils {

	private static PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();
	private static Properties props = new Properties();
	private static Logger log = Logger.getLogger(PropertiesUtils.class.getName());
	private static String location;
	
	private static PropertiesUtils propertiesUtils = new PropertiesUtils();

	public PropertiesUtils() {
		String location = "config.properties";
		if(location.startsWith("/")||location.startsWith("\\")){
			location=location.substring(1);
		}
		String path = "/HCWA/SSNS";
		try {
			path = resourceLoader.getResource("").getFile().getPath();
		} catch (IOException e) {
			log.equals(e);
		}
		this.location = path+File.separator+location;
		log.info(location);
		loadProperty();
	}

	private static void loadProperty() {

		log.info("Loading properties file from classpath:" + location);

		InputStream is = null;
		try {
		  File file = new File(location);
			is = new FileInputStream(file);
			props.load(is);
		//	propertiesPersister.load(props, new InputStreamReader(is, DEFAULT_ENCODING));
		} catch (IOException ex) {
			log.error("Could not load properties from classpath:" + location + ": " + ex.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info(e.getMessage());
				}
			}

		}
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}

	public static void setProperty(String key, String value) {
		props.setProperty(key, value);
	}

	public static void storeProperty() {
		File file = new File(location);
		FileOutputStream outputFile = null;
		try {
			// System.out.println("absolutePath: " +
			// resource.getFile().getAbsolutePath());
			outputFile = new FileOutputStream(file);
			propertiesPersister.store(props, outputFile, "");
			outputFile.flush();
			outputFile.close();
			loadProperty(); //写进配置文件后，重新读取配置文件中的开始时间
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != outputFile)
				try {
					outputFile.close();
				} catch (IOException e) {
					e.printStackTrace();
					log.info(e.getMessage());
				}
		}

	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String loc) {
		location = loc;
	}

}
