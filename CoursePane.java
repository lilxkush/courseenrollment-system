// Assignment #: Arizona State University Spring 2023 CSE205 #6
//         Name: Khushal Dhingra
//    StudentID: 1225420820
//      Lecture: MWF 11:15 am
//  Description: This code displays a window that lets the user choose from a list of classes, and gives the user the ability
//               to enter in the class number and intstructor name. It then adds the class to the class list.
//			     The user has the ability to drop check classes at will. There is also a class counter that displays
//				 the current number of classes the user is enrolled in.
//Note: when you submit on gradescope, you need to comment out the package line
//package yourPackageName;
package application;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import javafx.scene.text.TextAlignment;
import java.util.*;

public class CoursePane extends HBox
{
    //GUI components
    public ArrayList<Course> courseList;
    public VBox checkboxContainer;
    public ComboBox<String> Subject;
    public TextField courseNum;
    public TextField instructor;
    public int courseCount;
    public VBox leftPane = new VBox(50);
    public HBox leftBottom = new HBox(100);
    public Label labelLeftBottom = new Label();
    public Label labelRightBottom = new Label("Total course enrolled: " + courseCount);
    public VBox cinfo = new VBox();
    //Setting up scene and labels
    public CoursePane()
    
    {
        //initialize instance variables
       	courseList = new ArrayList<Course>();
    	checkboxContainer = new VBox();
    	instructor = new TextField();
    	courseNum = new TextField();
    	Subject = new ComboBox<String>();
        
    	//Creating left labels and adding them to left pane
    	Label labelLeft = new Label("Add Course(s)");
        labelLeft.setTextFill(Color.BLUE);
        labelLeft.setFont(Font.font(null, 14));
        
        labelLeftBottom.setTextFill(Color.BLACK);
        labelLeftBottom.setFont(Font.font(null, 12));
        leftBottom.setPadding(new Insets(15, 15, 15, 15));
        leftBottom.setAlignment(Pos.BOTTOM_LEFT);
        leftBottom.getChildren().addAll(labelLeftBottom);
       
        VBox leftCenterPane = new VBox(10);
        leftCenterPane.setAlignment(Pos.TOP_LEFT);
        leftCenterPane.getChildren().addAll(
        		new Label("Subject: "),
        			Subject,
        		new Label("Course Number: "),
        		courseNum,
        		new Label("Instructor: "),
        		instructor
        		);
        
        leftPane.setPadding(new Insets(15, 15, 15, 15));
        leftPane.setAlignment(Pos.TOP_LEFT);
        leftPane.getChildren().addAll(labelLeft, leftCenterPane, leftBottom);
        
        //creating add and drop buttons
        Button addButton = new Button("Add =>");
        addButton.setAlignment(Pos.CENTER_RIGHT);
        Button dropButton = new Button("Drop <=");
        dropButton.setAlignment(Pos.CENTER_LEFT);
        VBox centerPane = new VBox(15);
        centerPane.setAlignment(Pos.CENTER);
        centerPane.getChildren().addAll(addButton, dropButton);
        
        //creates right labels and adds them to right pane
	     Label labelRight = new Label("Course(s) Enrolled");
	     labelRight.setTextFill(Color.BLUE);
	     labelRight.setFont(Font.font(null, 14));
	     labelRightBottom.setTextFill(Color.BLACK);    
	        
        VBox rightPane = new VBox(10);
        rightPane.setPadding(new Insets(15, 15, 15, 15));
        rightPane.setAlignment(Pos.CENTER_RIGHT);
        checkboxContainer.setSpacing(5);
        rightPane.getChildren().addAll(labelRight, checkboxContainer, new Label(), labelRightBottom);
        
      
        this.getChildren().addAll(leftPane, centerPane, rightPane);
        this.setPadding(new Insets(10, 10, 10, 10));
     
        //setting up buttons and combobox
        addButton.setOnAction(new ButtonHandler());
        dropButton.setOnAction(new ButtonHandler());
        Subject.setOnAction(new ComboBoxHandler());
        Subject.getItems().addAll("ACC", "AME", "BME", "CHM", "CSE", "DAT", "EEE");
        Subject.setValue("CSE");
    } 
   
    //Adding Button and Checkbox functionality to code
    public void updateCheckBoxContainer() {
        checkboxContainer.getChildren().clear();

        for (Course course : courseList) {
            CheckBox checkbox = new CheckBox();
            checkbox.setWrapText(true);
            checkbox.setText("Course #: " + course.getSubject() + course.getCourseNum());
            Label instructorLabel = new Label("       Instructor: " + course.getInstructor());
            instructorLabel.setFont(Font.font(null, 12));
            HBox cinfo = new HBox(10);
            cinfo.getChildren().addAll(checkbox, instructorLabel);
            checkboxContainer.getChildren().add(cinfo);
           

            checkbox.setOnAction(new CheckBoxHandler(course, checkbox));
        }
    }

    //ButtonHandler class
    private class ButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            try {
                if (e.getSource() instanceof Button) {
                    Button source = (Button) e.getSource();
                    
                    //if add button is pressed...
                    if (source.getText().equals("Add =>")) {
                    	
                            
                        String sub = Subject.getValue();
                        int num = Integer.parseInt(courseNum.getText());
                        String inst = instructor.getText().trim();
                        Course newcourse = new Course(sub, num, inst);
  
                        if (inst.isEmpty() || (inst.isEmpty() && !courseNum.getText().matches("\\d+"))) {
                        	labelLeftBottom.setTextFill(Color.BLACK);
                        	labelLeftBottom.setText("At least one field is empty. Fill all fields");
                        } else {
                            
                        	CheckBox checkBox = new CheckBox(newcourse.toString());
                            checkboxContainer.getChildren().add(checkBox);

                            courseList.add(newcourse);
                            courseCount++;
                            labelLeftBottom.setTextFill(Color.BLACK);
                            labelLeftBottom.setText("Course added successfully");
                            labelRightBottom.setText("Total course(s) enrolled: " + courseCount);

                            courseNum.clear();
                            instructor.clear();
                            
                        }
                    	
                    //if drop button is pressed
                    } else if (source.getText().equals("Drop <=")) {
                        ArrayList<Node> checkboxesToRemove = new ArrayList<>();
                        ArrayList<Course> coursesToRemove = new ArrayList<>();
                        ArrayList<String> itemsToDrop = new ArrayList<>();
                        for (Node node : checkboxContainer.getChildren()) {
                            if (node instanceof CheckBox) {
                                CheckBox checkbox = (CheckBox) node;

                                if (checkbox.isSelected()) {
                                    checkboxesToRemove.add(checkbox);
                                    itemsToDrop.add(checkbox.getText());
                                    cinfo.getChildren().clear();
                                }
                            }
                        }
                        for (Course course : courseList) {
                            for (String item : itemsToDrop) {

                                if (item.contains(course.getSubject()) && item.contains(String.valueOf(course.getCourseNum()))
                                        && item.contains(course.getInstructor())) {
                                    coursesToRemove.add(course);
                                    courseCount--;
                                    labelRightBottom.setText("Total course(s) enrolled: " + courseCount);
                                    break;
                                }

                            }
                        }
                        courseList.removeAll(coursesToRemove);
                        checkboxContainer.getChildren().removeAll(checkboxesToRemove);
                        coursesToRemove.clear();
                        checkboxesToRemove.clear();
                        itemsToDrop.clear();
                    }
                }
                
            //handling exceptions
            } catch (NumberFormatException ex) {
            	if(courseNum.getText().isEmpty()) {
            		labelLeftBottom.setTextFill(Color.BLACK);
                	labelLeftBottom.setText("At least one field is empty. Fill all fields");
            		
            	}
            	else if (!courseNum.getText().matches("\\d+")){
            	labelLeftBottom.setTextFill(Color.RED);
            	labelLeftBottom.setText("Error! Course number must be an integer");
                courseNum.clear();
            	}
            } catch (Exception exception) {
            	labelLeftBottom.setTextFill(Color.RED);
                labelLeftBottom.setText("At least one field is empty. Fill all fields");
            }
        }
    }

    //creates handle method
    private class ComboBoxHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            if (e.getSource() instanceof ComboBox) {
                ComboBox<String> source = (ComboBox<String>) e.getSource();
                String selectedSubject = source.getValue();
                ArrayList<Course> filteredCourses = new ArrayList<Course>();
                for (Course course : courseList) {
                    if (selectedSubject.equals(course.getSubject())) {
                        filteredCourses.add(course);
                    }
                }
            }
        }
    }
    
    //creates checkboxhandler method
    private class CheckBoxHandler implements EventHandler<ActionEvent> {
        private Course oneCourse;
        private CheckBox checkbox;

        public CheckBoxHandler(Course course, CheckBox checkbox) {
            oneCourse = course;
            this.checkbox = checkbox;
        }

        @Override
        public void handle(ActionEvent e) {
            if (e.getSource() instanceof CheckBox) {
                CheckBox source = (CheckBox) e.getSource();
                boolean selected = source.isSelected();
                if (selected && checkbox.getText().equals(oneCourse.toString())) {
                    courseList.remove(oneCourse);
                    checkboxContainer.getChildren().remove(checkbox);
                    courseCount--;
                    labelLeftBottom.setText("Course dropped successfully");
                    labelRightBottom.setText("Total course(s) enrolled: ");
                }
            }
        }
    }
}
   