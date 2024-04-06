module parse {
	requires static lombok;
	requires org.apache.logging.log4j;
	requires org.seleniumhq.selenium.chrome_driver;
	requires org.seleniumhq.selenium.edge_driver;
	requires org.seleniumhq.selenium.firefox_driver;
	requires java.desktop;
	requires io.github.vincenzopalazzo.materialuiswing;
	requires com.miglayout.swing;
	
	exports parse;
}