package application;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EtudiantController implements Initializable{

	@FXML
	private TableColumn<Etudiant, String> fnameColumn;

	@FXML
	private TextField fnameLabel;

	@FXML
	private TableView<Etudiant> studentsTable;

	@FXML
	private Button clearButton;

	@FXML
	private TextField ageLabel;

	@FXML
	private Button eraseButton;

	@FXML
	private TableColumn<Etudiant, String> departmentColumn;

	@FXML
	private TableColumn<Etudiant, Double> ageColumn;

	@FXML
	private ComboBox<String> departmentComboBox;

	@FXML
	private Button modifyButton;

	@FXML
	private TableColumn<Etudiant, String> lnameColumn;

	@FXML
	private Button addButton;

	@FXML
	private TextField lnameLabel;
	
	public static Boolean fnameBlank;
	public static Boolean lnameBlank;
	public static Boolean ageBlank;
	public static Boolean departmentBlank;
	
	public static String fnameError;
	public static String lnameError;
	public static String ageError;
	public static String departmentError;


	// liste pour les départements - cette liste permettra de donner les valeurs du comboBox

	private ObservableList<String> list=(ObservableList<String>) FXCollections.observableArrayList("Sciences","Droit","Médecine"); 

	// Placer les étudiants dans une observable list
	public ObservableList<Etudiant> etudiantData=FXCollections.observableArrayList();

	// Créer une méthode pour accéder à la liste des étudiants

	public ObservableList<Etudiant> getetudiantData() {
		return etudiantData;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		departmentComboBox.setItems(list);
		//attribuer les valeurs aux colonnes du tableView
		fnameColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		lnameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
		departmentColumn.setCellValueFactory(new PropertyValueFactory<>("departement"));
		ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
		studentsTable.setItems(etudiantData);

		modifyButton.setDisable(true);
		eraseButton.setDisable(true);
		clearButton.setDisable(true);

		showEtudiants(null);
		// Mettre à jour l'affichage d'un étudiant sélectionné
		studentsTable.getSelectionModel().selectedItemProperty().addListener((
				observable, oldValue, newValue)-> showEtudiants(newValue));



	}

	// Ajouter un étudiant 
	@FXML
	void ajouter() {
		
		if (fnameLabel.getText().equals("")) {
			
			fnameBlank = true;
			fnameError = "Prenom ";
			
		} else {
			
			fnameBlank = false;
			fnameError = "";
			
		}
		
		if (lnameLabel.getText().equals("")) {
			
			lnameBlank = true;
			lnameError = "Nom ";
			
		} else {
			
			lnameBlank = false;
			lnameError = "";
			
		}
		
		if (ageLabel.getText().equals("")) {
			
			ageBlank = true;
			ageError = "Age ";
			
		} else {
			
			ageBlank = false;
			ageError = "";
			
		}
		
		if (departmentComboBox.getValue() == null) {
			
			departmentBlank = true;
			departmentError = "Département ";
			
		} else {
			
			departmentBlank = false;
			departmentError = "";
			
		}
		
		if (fnameBlank == true || lnameBlank == true || ageBlank == true || departmentBlank == true) {
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Informations manquantes");
			alert.setContentText("Complete les informations suivantes: " + fnameError + lnameError + ageError + departmentError);			
			alert.showAndWait();
			
		} else {
		
			Etudiant tmp=new Etudiant();
			tmp=new Etudiant();
			tmp.setNom(lnameLabel.getText());
			tmp.setPrenom(fnameLabel.getText());
			tmp.setAge(Double.parseDouble(ageLabel.getText()));
			tmp.setDepartement(departmentComboBox.getValue());
			etudiantData.add(tmp);	
			clearFields();
		
		}

	}

	// Effacer le contenu des champs
	@FXML
	void clearFields() {
		departmentComboBox.setValue(null);
		lnameLabel.setText("");
		fnameLabel.setText("");
		ageLabel.setText("");
	}
	
	
	
	//Trouver des inputs non-numériques
	@FXML
	public void verifNum() {
		
		ageLabel.textProperty().addListener((observable, oldValue, newValue) -> {
			
			if (!newValue.matches("^[0-9](\\.[0-9]+)?$")) {
				
				ageLabel.setText(newValue.replaceAll("[^\\d*\\.]","")); //Si l'input est numérique ca le remplace.
				
			}
			
		});
		
	}

	// Afficher les étudiants
	public void showEtudiants(Etudiant etudiant) {
		
		if(etudiant !=null) {

			departmentComboBox.setValue(etudiant.getDepartement());
			lnameLabel.setText(etudiant.getNom());
			fnameLabel.setText(etudiant.getPrenom());
			ageLabel.setText(Double.toString(etudiant.getAge()));
			modifyButton.setDisable(false);
			eraseButton.setDisable(false);
			clearButton.setDisable(false);

		} else {
			clearFields();
		}	   
		
	}

	// Mise à jour d'un étudiant
		@FXML
		public void updateEtudiant() {
			Etudiant etudiant=studentsTable.getSelectionModel().getSelectedItem();
			 
			etudiant.setNom(lnameLabel.getText());
			etudiant.setPrenom(fnameLabel.getText());
			etudiant.setAge(Double.parseDouble(ageLabel.getText()));
			etudiant.setDepartement(departmentComboBox.getValue());
			studentsTable.refresh();
		}  

	    // Effacer un étudiant
		@FXML
		public void deleteEtudiant() {
			int selectedIndex = studentsTable.getSelectionModel().getSelectedIndex();
			
			if (selectedIndex >=0) {
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Effacer");
				alert.setContentText("Voulez vous effacer?");
				Optional<ButtonType> result = alert.showAndWait();
				
				if (result.get() == ButtonType.OK) {
				
					studentsTable.getItems().remove(selectedIndex);
				
				}
			} 
			
		}

		//Sauvegarde des données

		//Récupérer le chemin des fichiers
		public File getEtudiantFilePath() {
			
			Preferences prefs = Preferences.userNodeForPackage(Main.class);
			String filePath = prefs.get("filePath", null);
			
			if (filePath != null) {
				return new File(filePath);
			} else {
				return null;
			}
			
		}
		
		//Attribuer un chemin de fichier
		public void  setEtudiantFilePath(File file) {
			
			Preferences prefs = Preferences.userNodeForPackage(Main.class);
			if (file != null) {
				prefs.put("filePath", file.getPath());
			} else {
				prefs.remove("filePath");
			}
			
		}
		
		//Prendre les données de type  XML et les convertir en données de type javaFX
		public void loadEtudiantDataFromFile(File file) {
		
		try {
			
			JAXBContext context = JAXBContext.newInstance(EtudiantListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			EtudiantListWrapper wrapper = (EtudiantListWrapper) um.unmarshal(file);
			etudiantData.clear();
			etudiantData.addAll(wrapper.getEtudiants());
			setEtudiantFilePath(file);
			
			//Donner le titre du fichier chargé
			Stage primaryStage = (Stage) studentsTable.getScene().getWindow();
			primaryStage.setTitle(file.getName());
			
		} catch (Exception e) {
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Les données n'ont pas été trouvées.");
			alert.setContentText("Les données ne pouvaient pas etre trouvées dans le fichier: \n" + file.getPath());
			alert.showAndWait();
			
		}
		
		}
		
		//Prendre les données de type JavaFX et les convertir en type XML
		
		public void saveEtudiantDataToFile(File file) {
			try {
				
				JAXBContext context = JAXBContext.newInstance(EtudiantListWrapper.class);
				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,  true);
				EtudiantListWrapper wrapper = new EtudiantListWrapper();
				wrapper.setEtudiants(etudiantData);
				
				m.marshal(wrapper, file);
				
				//Savegarde dans le registre
				setEtudiantFilePath(file);
				
				//Donner le titre du fichier sauvegardé
				Stage primaryStage = (Stage) studentsTable.getScene().getWindow();
				primaryStage.setTitle(file.getName());
				
			} catch (Exception e) {
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Données non sauvegardées");
				alert.setContentText("Les données ne pouvaient pas etre sauvegardées dans le fichier:\n" + file.getPath());
				alert.showAndWait();
				
			}
		}
		
		//Commencer un nouveau
		@FXML
		private void handleNew() {
			
			getetudiantData().clear();
			setEtudiantFilePath(null);
			
		}
		
		//FileChooser
		
		@FXML
		private void handleOpen() {
			FileChooser fileChooser = new FileChooser();
			
			//Permettre un filtre sur l'extension du fichier à chercher
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
			fileChooser.getExtensionFilters().add(extFilter)
;			
			//Montrer le dialogue
			File file = fileChooser.showOpenDialog(null);
			
			if (file != null) {
				loadEtudiantDataFromFile(file);
			}
		}
		
		//Sauvegarde le fichier correspondant à l'étudiant actif. S'il n'y a pas de fichier, le menu sauvegarde sous va s'afficher.
		@FXML
		private void handleSave() {
			
			File etudiantFile = getEtudiantFilePath();
			
			if (etudiantFile != null) {
				
				saveEtudiantDataToFile(etudiantFile);
				
			} else {
				handleSaveAs();
			}
			
		}
		
		//Ouvrir le FileChooser pour trouver le chemin.
		@FXML
		private void handleSaveAs() {
			FileChooser fileChooser = new FileChooser();
			
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
			fileChooser.getExtensionFilters().add(extFilter);
			
			//Sauvegarde
			File file = fileChooser.showSaveDialog(null);
			
			if (file != null ) {
				//Vérification de l'extension
				if (!file.getPath().endsWith(".xml")) {
					file = new File(file.getPath() + ".xml");
				}
				saveEtudiantDataToFile(file);
			}
		}
		
}

