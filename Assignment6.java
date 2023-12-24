// Assignment #: Arizona State University Spring 2023 CSE205 #6
//         Name: Khushal Dhingra
//    StudentID: 1225420820
//      Lecture: MWF 11:15 am
//  Description: This code sets up the window or stage where the program will take place
package application;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class Assignment6 extends Application
{
    public static final int WIDTH = 600, HEIGHT = 400;

    public void start(Stage stage)
    {
        StackPane root = new StackPane();
        CoursePane coursePane = new CoursePane();
        root.getChildren().add(coursePane);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("ASU Course Enrollment System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}