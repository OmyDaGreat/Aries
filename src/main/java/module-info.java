module parse {
	requires javafx.controls;
	requires javafx.fxml;
	requires static lombok;
	requires org.apache.logging.log4j;
	requires org.seleniumhq.selenium.chrome_driver;
	requires org.seleniumhq.selenium.edge_driver;
	requires org.seleniumhq.selenium.firefox_driver;
	
	opens parse to javafx.fxml;
	exports parse;
}