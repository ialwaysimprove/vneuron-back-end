package StAXParser.src.main.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.vneuron.sbelasticback.Elasticsearch.Watchlist;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;


public class DeployFileLoaderSDN {

   
    public static ArrayList<Watchlist> convert() {
        String xmlPath = "src/main/resources/xml/sdn.xml";
        boolean printToFile = false;
        boolean ndjson = false;

        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlPath);
            Element rootElement = document.getRootElement();
           


            ArrayList<Watchlist> sdnWatchlist = new ArrayList<>();

            FileWriter myWriter = new FileWriter("src/main/resources/json/sdn.json");


            String source_document = "USTreasury";
            List<Element> sdnEntryList = rootElement.elements("sdnEntry");
            for (Element sdnEntry : sdnEntryList) {




                ObjectMapper mapper = new ObjectMapper();
                ObjectMapper objectMapper = new ObjectMapper();

                ObjectNode commonFormat = mapper.createObjectNode(); 
                Watchlist anIndividual = new Watchlist();


                String entity_id;
                String entity_type;
                String primary_name = "";

                HashSet<String> wholeNameSet = new HashSet<>();
               
                HashSet<String> countriesSet = new HashSet<>();
                HashSet<String> remarksSet = new HashSet<>();

                Element uid = sdnEntry.element("uid");
                if (uid != null) {

                    String id_in_document = uid.getText();
                    entity_id = source_document + "-" + id_in_document;
                    commonFormat.put("entity_id", entity_id);
                    commonFormat.put("source_document", source_document);
                    commonFormat.put("id_in_document", id_in_document);

                    anIndividual.setEntity_id(entity_id);
                    anIndividual.setSource_document(source_document);
                    anIndividual.setId_in_document(id_in_document);

                }

                String wholeName = "";
                Element lastName = sdnEntry.element("lastName");
                if (lastName != null) {
                   
                    wholeName += lastName.getText();
                }

                Element firstName = sdnEntry.element("firstName");
                if (firstName != null) {
                   
                    wholeName += " " + firstName.getText();



                }

                Element sdnType = sdnEntry.element("sdnType");
                if (sdnType != null) {
                   
                    if (sdnType.getText().equals("Entity")) {
                        entity_type = "E";
                        anIndividual.setEntity_type(entity_type);
                        commonFormat.put("entity_type", entity_type);
                    }
                    else {
                        entity_type = "P";
                        anIndividual.setEntity_type(entity_type);
                        commonFormat.put("entity_type", entity_type);

                    }

                }

               
                Element akaList = sdnEntry.element("akaList");
                if (akaList != null && akaList.hasContent()) {
                    HashSet<AKAName> AKANamesList = new HashSet<AKAName>();
                   
                    for (Iterator j = akaList.elementIterator(); j.hasNext(); ) {
                        Element aka = (Element) j.next();
                        String akaFull = "";
                        String akaFirstName = "";
                        String akaLastName = "";

                        Element lastNameElement = aka.element("lastName");
                        if (lastNameElement != null) {
                           
                            akaLastName = lastNameElement.getText();
                            akaFull += akaLastName;

                        }
                        Element firstNameElement = aka.element("firstName");
                        if (firstNameElement != null) {
                           
                            akaFirstName = firstNameElement.getText();
                            if (akaFirstName.equals("")) {
                                akaFull +=  akaFirstName;
                            }
                            else {
                                akaFull += " " + akaFirstName;
                            }

                        }
                        AKAName akaName = new AKAName(akaFirstName, akaLastName);
                        AKANamesList.add(akaName);
                        wholeNameSet.add(akaFull);
                    }
                }
               

                Element addresslist = sdnEntry.element("addressList");
                if (addresslist != null && addresslist.hasContent()) {
                    HashSet<String> countryAddresses = new HashSet<String>();
                   
                    for (Iterator j = addresslist.elementIterator(); j.hasNext(); ) {
                        Element address = (Element) j.next();

                        Element countryAddress = address.element("country");
                        if (countryAddress != null) {
                           
                            countryAddresses.add(countryAddress.getText());
                            countriesSet.add(countryAddress.getText());
                        }
                    }
                }


                Element title = sdnEntry.element("title");
                if (title != null && title.hasContent()) {
                   
                    remarksSet.add(title.getText());
                }

                HashSet<String> idListCountries = new HashSet<String>();
               
                Element idList = sdnEntry.element("idList");
                if (idList != null && idList.hasContent()) {
                   
                    for (Iterator j = idList.elementIterator(); j.hasNext(); ) {
                        Element id = (Element) j.next();
                        if (id.element("idCountry") != null) {
                           
                            idListCountries.add(id.elementText("idCountry"));
                            countriesSet.add(id.elementText("idCountry"));

                        }

                        if ((id.element("idType") != null) && (id.elementText("idType").equals("Gender"))) {
                           
                            remarksSet.add(id.elementText("idNumber"));
                        }
                    }
                }

                Element placeOfBirthList = sdnEntry.element("placeOfBirthList");
                HashSet<String> placesOfBirthArray = new HashSet<String>();
                if (placeOfBirthList != null && placeOfBirthList.hasContent()) {
                   
                    for (Iterator j = placeOfBirthList.elementIterator(); j.hasNext(); ) {
                        Element placeOfBirthItem = ((Element) j.next()).element("placeOfBirth");

                        if (placeOfBirthItem != null) {
                           
                            placesOfBirthArray.add(placeOfBirthItem.getText());
                            countriesSet.add(placeOfBirthItem.getText());
                        }
                    }
                }

                Element remarks = sdnEntry.element("remarks");
                if (remarks != null && remarks.hasContent()) {
                   
                    remarksSet.add(remarks.getText());
                }

                HashSet<String> nationalities = new HashSet<String>();
                Element nationalityList = sdnEntry.element("nationalityList");
                if (nationalityList != null && nationalityList.hasContent()) {
                   
                    for (Iterator j = nationalityList.elementIterator(); j.hasNext(); ) {
                        Element nationality = (Element) j.next();
                        Element countryNationality = nationality.element("country");
                        if (countryNationality != null) {
                           
                            nationalities.add(countryNationality.getText());
                            countriesSet.add(countryNationality.getText());
                        }
                    }
                   
                }

                Element citizenshipList = sdnEntry.element("citizenshipList");
                HashSet<String> citizenships = new HashSet<String>();
                if (citizenshipList != null && citizenshipList.hasContent()) {
                   
                    for (Iterator j = citizenshipList.elementIterator(); j.hasNext(); ) {
                        Element citizenship = ((Element) j.next()).element("country");

                        if (citizenship != null) {
                           
                            citizenships.add(citizenship.getText());
                            countriesSet.add(citizenship.getText());
                        }
                    }
                }

                wholeName = wholeName.trim();
                wholeNameSet.add(wholeName);
                commonFormat.put("primary_name", wholeName);
                commonFormat.put("whole_names", mapper.valueToTree(wholeNameSet));


               

                if (printToFile) {
                    if (ndjson) {
                        myWriter.write("{\"index\":{}}");
                        myWriter.write(System.getProperty( "line.separator" ));
                    }

                    myWriter.write(commonFormat.toString());
                    myWriter.write(System.getProperty( "line.separator" ));

                }

                sdnWatchlist.add(anIndividual);

                
               
               


               
                            }

            myWriter.close();

            return sdnWatchlist;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class AKAName {
    public String firstName = "";
    public String lastName = "";

    public AKAName(String fN, String lN) {
        if (fN != null)
            firstName = fN;
        if (lN != null)
            lastName = lN;
    }

}