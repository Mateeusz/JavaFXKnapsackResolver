package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import plecak.*;

public class Controller implements Initializable{

    @FXML
    Label dateLabel, actualCapacityLabel ;
    @FXML
    TextField bagCapacityLabel, addWeightLabel, addValueLabel;
    @FXML
    ComboBox algorithmComboBox;
    @FXML
    TableView<Item> itemTableView, tableViewSolution;
    @FXML
    TableColumn<Item, Double> weightCoulumne, weightColumneSolution;
    @FXML
    TableColumn<Item, Integer> valueCoulumne, sizeColumneSolution;
    @FXML
    BorderPane borderPane;
    @FXML
    Text mainText, capaLabel, addItemLabel1, valLabel, weightLabel, aviItems, chooseAlgLabel, solutionLabel, itemSol;
    @FXML
    Button addCappacityButton, addItemLabel, clearInstatnionButton, solveButton;
    @FXML
    Menu menuApp, menuLan, menuHelp;
    @FXML
    MenuItem menuUS, menuPl, menuGB, menuExit, menuAbout;


    LocalDate localDate = LocalDate.now();
    private List<Item> tempList = new ArrayList<Item>();
    plecak.Instantion instantion = new Instantion();
    ObservableList<Item> oblist = FXCollections.observableArrayList();
    ObservableList<Item> solutionOblist = FXCollections.observableArrayList();
    ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");
    Locale locale;
//    FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/FXML/sample.fxml"));


    public void loadLang(String lang)
    {
        locale = new Locale(lang);
        resourceBundle = ResourceBundle.getBundle("messages",locale);

    }

    public void changeToPolish() {
        loadLang("pl");
        updateText();
        dateLabel.setText(resourceBundle.getString("dateLabel") + " " + localDate.getDayOfMonth() + "." + localDate.getMonthValue() + "." + localDate.getYear());
    }

    public void changeToEnglish() {
        loadLang("en");
        updateText();
        Locale.setDefault(new Locale("en"));
        dateLabel.setText(resourceBundle.getString("dateLabel") + " " + localDate.getYear() + "." + localDate.getMonthValue() + "." + localDate.getDayOfMonth());
    }

    public void changeToEnglishUS() {
        loadLang("en");
        updateText();
        dateLabel.setText(resourceBundle.getString("dateLabel") + " " + localDate.getMonthValue() + "." + localDate.getDayOfMonth() + "." + localDate.getYear());
    }

    public void aboutApp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("aboutAppBut"));
        alert.setHeaderText(resourceBundle.getString("titleApp"));
        alert.setContentText(resourceBundle.getString("aboutAppStr"));
        alert.showAndWait();
    }

    public void closeApp() {
        System.exit(0);
    }

    public void clearInstantion() {
        actualCapacityLabel.setVisible(false);
        instantion.getItems().clear();
        oblist.clear();
        itemTableView.refresh();
    }

    public void addCapacity() {

        try {
            Integer.parseInt(bagCapacityLabel.getText());

        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("errorMsg"));
            alert.setHeaderText(resourceBundle.getString("errorTitle"));
            alert.setContentText(resourceBundle.getString("capError"));
            alert.showAndWait();
            return;
        }
        finally {
            instantion.setBagSize(Integer.parseInt(bagCapacityLabel.getText()));
            actualCapacityLabel.setText(resourceBundle.getString("bagCapLabel") + bagCapacityLabel.getText());
            actualCapacityLabel.setVisible(true);
        }
    }

    public void addItem() {

        try {
            Double.parseDouble(addWeightLabel.getText());
            Integer.parseInt(addValueLabel.getText());
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("errorMsg"));
            alert.setHeaderText(resourceBundle.getString("errorTitle"));
            alert.setContentText(resourceBundle.getString("addItemErr"));
            alert.showAndWait();
            return;
        }
        finally {
            instantion.addItem(Double.parseDouble(addWeightLabel.getText()), Integer.parseInt(addValueLabel.getText()));
            oblist.add(new Item(Double.parseDouble(addWeightLabel.getText()), Integer.parseInt(addValueLabel.getText())));

            weightCoulumne.setCellValueFactory(new PropertyValueFactory<Item, Double>("value"));
            valueCoulumne.setCellValueFactory(new PropertyValueFactory<Item, Integer>("size"));

            itemTableView.setItems(oblist);
        }
    }

    public void resolve() {

        tableViewSolution.getItems().clear();
        tableViewSolution.refresh();
        solutionOblist.clear();

        if(algorithmComboBox.getValue().toString().equals("Brute Force")) {

            BruteForce bruteForce = new BruteForce();

            for (int i = 0; i<bruteForce.solution(instantion).size(); i++) {
                solutionOblist.add(bruteForce.solution(instantion).get(i));
            }

            weightColumneSolution.setCellValueFactory(new PropertyValueFactory<Item, Double>("value"));
            sizeColumneSolution.setCellValueFactory(new  PropertyValueFactory<Item, Integer>("size"));

            tableViewSolution.setItems(solutionOblist);

            System.out.println(solutionOblist.size());
        }
        else if(algorithmComboBox.getValue().toString().equals("Randomized")) {

            Randomized randomized = new Randomized();

            for (int i = 0; i<randomized.solution(instantion).size(); i++) {
                solutionOblist.add(randomized.solution(instantion).get(i));
            }

            weightColumneSolution.setCellValueFactory(new PropertyValueFactory<Item, Double>("value"));
            sizeColumneSolution.setCellValueFactory(new  PropertyValueFactory<Item, Integer>("size"));

            tableViewSolution.setItems(solutionOblist);

            System.out.println(solutionOblist.size());
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("errorMsg"));
            alert.setHeaderText(resourceBundle.getString("errorTitle"));
            alert.setContentText(resourceBundle.getString("resolveErrorMSG"));
            alert.showAndWait();
            return;
        }
        if(solutionOblist.size() == 0) {
            itemSol.setVisible(false);
        }
        else if(solutionOblist.size() == 1) {
            itemSol.setVisible(true);
            itemSol.setText(resourceBundle.getString("solNumLab") + solutionOblist.size() + resourceBundle.getString("oneItem"));
        }
        else if((solutionOblist.size() > 1) && (solutionOblist.size() < 5)) {
            itemSol.setVisible(true);
            itemSol.setText(resourceBundle.getString("solNumLab") + solutionOblist.size() + resourceBundle.getString("2to5"));
        }
        else if(solutionOblist.size() > 4) {
            itemSol.setVisible(true);
            itemSol.setText(resourceBundle.getString("solNumLab") + solutionOblist.size() + resourceBundle.getString("moreThan5"));
        }
    }


    public void updateText() {
        addCappacityButton.setText(resourceBundle.getString("addBut"));
        mainText.setText(resourceBundle.getString("mainLabel"));
        menuApp.setText(resourceBundle.getString("appBut"));
        menuLan.setText(resourceBundle.getString("langBut"));
        menuHelp.setText(resourceBundle.getString("helpButton"));
        menuUS.setText(resourceBundle.getString("engUSBut"));
        menuPl.setText(resourceBundle.getString("plBut"));
        menuGB.setText(resourceBundle.getString("engBut"));
        menuExit.setText(resourceBundle.getString("exitBut"));
        menuAbout.setText(resourceBundle.getString("aboutAppBut"));
        capaLabel.setText(resourceBundle.getString("capacityLabel"));
        addItemLabel1.setText(resourceBundle.getString("addBut"));
        valLabel.setText(resourceBundle.getString("valueLabel"));
        weightLabel.setText(resourceBundle.getString("weightLabel"));
        aviItems.setText(resourceBundle.getString("aviItems"));
        actualCapacityLabel.setText(resourceBundle.getString("bagCapLabel") + instantion.bagSize);
        weightCoulumne.setText(resourceBundle.getString("weightLabel"));
        valueCoulumne.setText(resourceBundle.getString("valueLabel"));
        weightColumneSolution.setText(resourceBundle.getString("weightLabel"));
        sizeColumneSolution.setText(resourceBundle.getString("valueLabel"));
        addItemLabel.setText(resourceBundle.getString("addBut"));
        dateLabel.setText(resourceBundle.getString("dateLabel"));
        clearInstatnionButton.setText(resourceBundle.getString("clrInstantion"));
        chooseAlgLabel.setText(resourceBundle.getString("chooseAlgorithm"));
        solveButton.setText(resourceBundle.getString("solutionBut"));
        solutionLabel.setText(resourceBundle.getString("solutionLabel"));
        Main.getMain().setTitle(resourceBundle.getString("titleApp"));
        itemSol.setText(resourceBundle.getString("solNumLab"));
        if(solutionOblist.size() == 0) {
            itemSol.setVisible(false);
        }
        else if(solutionOblist.size() == 1) {
            itemSol.setVisible(true);
            itemSol.setText((resourceBundle.getString("solNumLab") + solutionOblist.size() + resourceBundle.getString("oneItem")));
        }
        else if((solutionOblist.size() > 1) && (solutionOblist.size() < 5)) {
            itemSol.setVisible(true);
            itemSol.setText((resourceBundle.getString("solNumLab") + solutionOblist.size() + resourceBundle.getString("2to5")));
        }
        else if(solutionOblist.size() > 4) {
            itemSol.setVisible(true);
            itemSol.setText((resourceBundle.getString("solNumLab") + solutionOblist.size() + resourceBundle.getString("moreThan5")));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String temp = dateLabel.getText();
        dateLabel.setText(temp + " " + LocalDate.now().toString());
    }
}
