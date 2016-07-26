package ngsMetadataToolPackage;

import java.io.File;
import java.util.*;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.awt.Desktop;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Controller class for FXML file
public class Controller {
    @FXML
    RadioButton rnaSeq;
    @FXML
    RadioButton chipSeq;
    @FXML
    ToggleGroup sequenceType;
    @FXML
    RadioButton singleEnd;
    @FXML
    RadioButton pairedEnd;
    @FXML
    ToggleGroup endType;
    @FXML
    Button chooseFiles;
    @FXML
    Button populate;
    @FXML
    ListView fileList;
    @FXML
    TextField person;
    @FXML
    TextField project;
    @FXML
    TextField cancerType;

    static List<File> listOfFiles = null;
    static List<String> filesStringList = new ArrayList<String>();
    static int sheet = 0;

    private Desktop desktop = Desktop.getDesktop();

    //Controls the "Populate Excel File" button
    public void populateButtonHandler() {
        sheet = getSequenceTypeRadioChoice();

        //call spreadsheet modifier
        try {
            spreadsheetEditor.main(null, filesStringList, sheet, listOfFiles, getPersonName(), getProjectName(), getCancerType(), getEndTypeRadioChoice());
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        //Alert the user that the action has been completed
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert!");
        alert.setHeaderText("NGS Spreadsheet has been updated");
        alert.setContentText("The following columns have been populated: Filename, date, cancer type, file path," +
                " paired ends, platform");

        alert.showAndWait();

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File("/Users/REO/OneDrive/NGS-Metadata.xlsx"));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    //Controls the "Choose files..." button
    public void fileChooserButtonHandler() {
        //listOfFiles = null;
        filesStringList.clear();
        listFiles(null);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open NGS files");

        // Allows for selecting multiple files
        listOfFiles = fileChooser.showOpenMultipleDialog(populate.getScene().getWindow());
        /*
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                //openFile(file);
                //listOfFiles.add(file);
                System.out.println("WORKS");
            }
        }
        */
        //List<String> filesStringList = new ArrayList<String>();
        if(listOfFiles != null) {
            for (File file : listOfFiles){
                System.out.println(file.getName());
                filesStringList.add(file.getName().toString());
            }
        }
        listFiles(filesStringList);



        //System.out.println("==> For Loop Example.");
        //for (int i = 0; i < listOfFiles.size(); i++) {
        //    System.out.println(listOfFiles.get(i));
        //}
        //listFiles();

    }
    //Lists the file names in the ListView
    public void listFiles(List<String> files) {
        //wrap list with ObservableList
        if(files == null){
            fileList.setItems(null);
        }
        else {
            ObservableList<String> observableList = FXCollections.observableList(files);
            fileList.setItems(observableList);
        }
    }

    public int getSequenceTypeRadioChoice(){
        System.out.println(sequenceType.getSelectedToggle());
        if(sequenceType.getSelectedToggle().toString().equals("RadioButton[id=chipSeq, styleClass=radio-button]'ChIP-seq'")){
            return 0;
        }
        else{
            return 1;
        }
    }

    public String getEndTypeRadioChoice(){
        System.out.println("END TYPE: "+ endType.getSelectedToggle());
        if(endType.getSelectedToggle().toString().equals("RadioButton[id=singleEnd, styleClass=radio-button]'Single end'")){
            return "Single";
        }
        else{
            System.out.println(endType.getSelectedToggle().toString());
            return "Paired";
        }
    }

    public String getPersonName(){
        return person.getText();
    }

    public String getProjectName() {
        return project.getText();
    }

    public String getCancerType(){
        return cancerType.getText();
    }

    //Opens the "open files" dialogue
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    Controller.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }
}