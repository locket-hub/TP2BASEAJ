module FoundationsF25 {
	requires javafx.controls;
	requires java.sql;
	requires javafx.graphics;
	
	opens applicationMain to javafx.graphics, javafx.fxml;
}
