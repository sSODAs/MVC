import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.*;

// Controller ZONE (Main logic class that handles the interaction between GUI and database.)

public class Logic {

    private CheckDatabase checkDatabase;
    private CowHandler cowHandler;

    public Logic() {
        checkDatabase = new CheckDatabase();
        cowHandler = new CowHandler();
    }

    // Handles calculations when the button is clicked.

    public void handleCalculate(JTextField inputField, JTextArea outputArea) {
        String input = inputField.getText(); // Get input from the text field
        String result = checkDatabase.checkAnimal(input); // Validate and check animal info
        outputArea.setText(result); // Display result in the text area
    }

    // Inner class responsible for handling database operations.

    public class CheckDatabase {
        private Map<String, Animal> animalMap;

        // Constructor to load animal data from the CSV file
        public CheckDatabase() {
            animalMap = new HashMap<>();
            loadAnimals("data.csv");
        }

        // Reads data from CSV file and creates Animal objects.

        private void loadAnimals(String filePath) {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                br.readLine(); // Skip the header line
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 5) {
                        System.err.println("Invalid line format: " + line);
                        continue; // Ensure there are enough columns
                    }

                    String id = parts[0].trim();
                    String type = parts[1].trim();
                    int ageYears = parseIntSafely(parts[2].trim(), 0);
                    int ageMonths = parseIntSafely(parts[3].trim(), 0);
                    Integer udders = parseIntSafely(parts[4].trim(), null);

                    // Add only valid animals to the map
                    animalMap.put(id, new Animal(id, type, ageYears, ageMonths, udders));
                }
            } catch (IOException e) {
                System.err.println("Error reading the CSV file: " + e.getMessage());
            }
        }

        // Utility method to safely parse integers from a string.

        private Integer parseIntSafely(String value, Integer defaultValue) {
            try {
                return value.isEmpty() ? defaultValue : Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return defaultValue; // Return default value in case of error
            }
        }

        // Checks the animal data based on the provided ID.

        public String checkAnimal(String id) {
            if (!isValidId(id)) {
                return "Invalid ID: must be 8 digits and cannot start with 0.";
            }

            Animal animal = getAnimalById(id);
            if (animal != null) {
                return getAnimalInfo(animal);
            }
            return "Animal with ID " + id + " not found.";
        }

        private boolean isValidId(String id) {
            return id.length() == 8 && id.matches("[1-9][0-9]{7}");
        }

        // Returns the correct message based on the animal's type and attributes.

        private String getAnimalInfo(Animal animal) {
            String type = animal.getType().toLowerCase().trim();

            switch (type) {
                case "cow":
                    if (animal.getUdders() != null) {
                        if (animal.getUdders() == 4) {
                            cowHandler.handleCowWithFourUdders(animal);
                            return "This cow has 4 udders and can be milked!";
                        } else if (animal.getUdders() == 3) {
                            cowHandler.handleCowWithThreeUdders(animal);
                            return "This cow has 3 udders. Check if it needs any handling.";
                        } else {
                            return "This cow has an unusual number of udders: " + animal.getUdders();
                        }
                    } else {
                        return "This cow's udders information is missing.";
                    }

                case "goat":
                    return "This is a goat, send it back to the mountain!";

                default:
                    return "Unknown animal type: " + animal.getType();
            }
        }

        // Finds an animal by ID.

        private Animal getAnimalById(String animalId) {
            return animalMap.get(animalId);
        }
    }

    // Inner class for handling cow-specific logic.

    public class CowHandler {
        private MilkCalculator milkCalculator;

        public CowHandler() {
            this.milkCalculator = new MilkCalculator();
        }

        // Handles cow with 4 udders.

        public void handleCowWithFourUdders(Animal cow) {
            if (cow != null && cow.getUdders() != null && cow.getUdders() == 4) {
                Random rand = new Random();
                if (rand.nextInt(100) < 5) {
                    cow.setUdders(3); // Update udders count
                    System.out.println("The cow's udders decreased to 3!");
                } else {
                    int milkProduction = milkCalculator.calculateMilkProduction(cow);
                    System.out.println("The cow has been milked, producing: " + milkProduction + " liters");
                }
            } else {
                System.out.println("Invalid cow or udders information.");
            }
        }

        // Handles cow with 3 udders.

        public void handleCowWithThreeUdders(Animal cow) {
            if (cow != null && cow.getUdders() != null && cow.getUdders() == 3) {
                Random rand = new Random();
                if (rand.nextInt(100) < 20) {
                    cow.setUdders(4); // Update udders count
                    System.out.println("The cow's udders increased to 4!");
                } else {
                    System.out.println("The cow was not milked and no change occurred.");
                }
            }
        }
    }

    // MODEL ZONE

    public class Animal {
        private String id;
        private String type;
        private int ageYears;
        private int ageMonths;
        private Integer udders;

        // Constructs an Animal with the given attributes.

        public Animal(String id, String type, int ageYears, int ageMonths, Integer udders) {
            this.id = id;
            this.type = type;
            this.ageYears = ageYears;
            this.ageMonths = ageMonths;
            this.udders = udders;
        }

        // Getters and Setters

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public int getAgeYears() {
            return ageYears;
        }

        public int getAgeMonths() {
            return ageMonths;
        }

        public Integer getUdders() {
            return udders;
        }

        public void setUdders(Integer udders) {
            this.udders = udders;
        }

        // Calculates the milk production based on the animal's age.

        public int calculateMilkProduction() {
            return ageYears + ageMonths;
        }
    }

    // Inner class for MilkCalculator.

    public class MilkCalculator {

        // Calculates milk production for a cow.

        public int calculateMilkProduction(Animal cow) {
            if (cow.getType().equalsIgnoreCase("Cow")) {
                return cow.getAgeYears() + cow.getAgeMonths();
            }
            return 0; // No milk production if not a cow
        }
    }
}
