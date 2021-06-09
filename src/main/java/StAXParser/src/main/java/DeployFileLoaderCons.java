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

public class DeployFileLoaderCons {
    
    

    public static ArrayList<Watchlist>  convert() {
        String xmlPath = "src/main/resources/xml/cons_advanced.xml";
        boolean printToFile = false;
        boolean ndjson = false;


        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlPath);
            Element rootElement = document.getRootElement();
            FileWriter myWriter = new FileWriter("src/main/resources/json/cons_advanced.json");

            ArrayList<Watchlist> consWatchlist = new ArrayList<>();

            String source_document = "UNSecurityCouncil";
            Element Individuals = rootElement.element("INDIVIDUALS");
            for (Iterator i = Individuals.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                Watchlist anIndividual = new Watchlist();

                String entity_id;

                String entity_type;
                String primary_name = ""; // just trim leading and trailing spaces

                ObjectMapper mapper = new ObjectMapper();
                ObjectMapper objectMapper = new ObjectMapper();

                ObjectNode commonFormat = mapper.createObjectNode();

                HashSet<String> wholeNameSet = new HashSet<>();

                HashSet<String> countriesSet = new HashSet<>();
                HashSet<String> remarksSet = new HashSet<>();

                commonFormat.put("entity_type", "P");
                entity_type = "P";
                anIndividual.setEntity_type(entity_type);

                Element DATAID = element.element("DATAID");
                if (DATAID != null) {
                    String id_in_document = DATAID.getText();
                    entity_id = source_document + "-" + id_in_document;
                    commonFormat.put("entity_id", entity_id);
                    commonFormat.put("source_document", source_document);
                    commonFormat.put("id_in_document", id_in_document);
                    anIndividual.setEntity_id(entity_id);
                    anIndividual.setSource_document(source_document);
                    anIndividual.setId_in_document(id_in_document);
                }

                Element FIRST_NAME = element.element("FIRST_NAME");
                if (FIRST_NAME != null) {
                    primary_name += " " + FIRST_NAME.getText();
                }

                Element SECOND_NAME = element.element("SECOND_NAME");
                if (SECOND_NAME != null) {
                    primary_name += " " + SECOND_NAME.getText();
                }

                Element THIRD_NAME = element.element("THIRD_NAME");
                if (THIRD_NAME != null) {
                    primary_name += " " + THIRD_NAME.getText();
                }

                Element UN_LIST_TYPE = element.element("UN_LIST_TYPE");
                if (UN_LIST_TYPE != null) {
                    remarksSet.add(UN_LIST_TYPE.getText());
                }

                Element COMMENTS1 = element.element("COMMENTS1");
                if (COMMENTS1 != null) {
                    remarksSet.add(COMMENTS1.getText());

                }

                Element designationsList = element.element("DESIGNATION");
                if (designationsList != null && designationsList.hasContent()) {
                    HashSet<String> designationsSet = new HashSet<String>();
                    for (Iterator j = designationsList.elementIterator(); j.hasNext(); ) {
                        Element designationValue = (Element) j.next();
                        designationsSet.add(designationValue.getText());
                        remarksSet.add(designationValue.getText());
                    }
                }

                Element nationalityList = element.element("NATIONALITY");
                if (nationalityList != null && nationalityList.hasContent()) {
                    HashSet<String> nationalitySet = new HashSet<String>();
                    for (Iterator j = nationalityList.elementIterator(); j.hasNext(); ) {
                        Element nationalityValue = (Element) j.next();
                        nationalitySet.add(nationalityValue.getText());
                        countriesSet.add(nationalityValue.getText());
                    }
                }

                List<Element> INDIVIDUAL_ALIAS = element.elements("INDIVIDUAL_ALIAS");
                if (INDIVIDUAL_ALIAS != null) {
                    HashSet<String> individualAliasSet = new HashSet<String>();
                    for (Element individualAlias : INDIVIDUAL_ALIAS) {
                        Element ALIAS_NAME = individualAlias.element("ALIAS_NAME");
                        Element COUNTRY_OF_BIRTH = individualAlias.element("COUNTRY_OF_BIRTH");
                        if (ALIAS_NAME != null) {
                            individualAliasSet.add(ALIAS_NAME.getText());
                            wholeNameSet.add(ALIAS_NAME.getText());
                        }
                        if (COUNTRY_OF_BIRTH != null) {
                            countriesSet.add(COUNTRY_OF_BIRTH.getText());
                        }
                    }
                }

                List<Element> INDIVIDUAL_ADDRESS = element.elements("INDIVIDUAL_ADDRESS");
                if (INDIVIDUAL_ADDRESS != null) {
                    HashSet<String> individualAddressSet = new HashSet<String>();
                    for (Element individualAddress : INDIVIDUAL_ADDRESS) {
                        if (individualAddress.hasContent()) {
                            Element individualAddressCountry = individualAddress.element("COUNTRY");
                            if (individualAddressCountry != null) {
                                individualAddressSet.add(individualAddressCountry.getText());
                                countriesSet.add(individualAddressCountry.getText());
                            }
                        }
                    }
                }

                List<Element> INDIVIDUAL_PLACE_OF_BIRTH = element.elements("INDIVIDUAL_PLACE_OF_BIRTH");
                if (INDIVIDUAL_PLACE_OF_BIRTH != null) {
                    HashSet<String> individualPlaceOfBirthSet = new HashSet<String>();
                    for (Element individualPlaceOfBirth : INDIVIDUAL_PLACE_OF_BIRTH) {
                        if (individualPlaceOfBirth.hasContent()) {
                            Element COUNTRY = individualPlaceOfBirth.element("COUNTRY");
                            if (COUNTRY != null) {
                                individualPlaceOfBirthSet.add(COUNTRY.getText());
                                countriesSet.add(COUNTRY.getText());
                            }
                        }
                    }
                }

                List<Element> INDIVIDUAL_DOCUMENT = element.elements("INDIVIDUAL_DOCUMENT");
                if (INDIVIDUAL_DOCUMENT != null) {
                    HashSet<String> individualDocumentSet = new HashSet<String>();
                    for (Element individualDocument : INDIVIDUAL_DOCUMENT) {
                        if (individualDocument != null && individualDocument.hasContent()) {
                            Element COUNTRY_OF_ISSUE = individualDocument.element("COUNTRY_OF_ISSUE");
                            Element ISSUING_COUNTRY = individualDocument.element("ISSUING_COUNTRY");
                            if (COUNTRY_OF_ISSUE != null) {
                                individualDocumentSet.add(COUNTRY_OF_ISSUE.getText());
                                countriesSet.add(COUNTRY_OF_ISSUE.getText());
                            }
                            if (ISSUING_COUNTRY != null) {
                                individualDocumentSet.add(ISSUING_COUNTRY.getText());
                                countriesSet.add(ISSUING_COUNTRY.getText());
                            }
                        }
                    }
                }


                Element NAME_ORIGINAL_SCRIPT = element.element("NAME_ORIGINAL_SCRIPT");
                if (NAME_ORIGINAL_SCRIPT != null) {
                    wholeNameSet.add(NAME_ORIGINAL_SCRIPT.getText());
                }

                Element FOURTH_NAME = element.element("FOURTH_NAME");
                if (FOURTH_NAME != null) {
                    primary_name += " " + FOURTH_NAME.getText();
                }

                Element GENDER = element.element("GENDER");
                if (GENDER != null) {
                    remarksSet.add(GENDER.getText());
                }

                Element title = element.element("TITLE");
                if (title != null && title.hasContent()) {
                    HashSet<String> titlesSet = new HashSet<String>();
                    for (Iterator j = title.elementIterator(); j.hasNext(); ) {
                        Element titleValue = (Element) j.next();
                        titlesSet.add(titleValue.getText());
                        remarksSet.add(titleValue.getText());
                    }
                }



                primary_name = primary_name.trim();
                wholeNameSet.add(primary_name);
                anIndividual.setPrimary_name(primary_name);
                anIndividual.setWhole_names(wholeNameSet);

                commonFormat.put("whole_names", mapper.valueToTree(wholeNameSet));
                commonFormat.put("primary_name", primary_name);

                if (printToFile) {
                    if (ndjson) {
                        myWriter.write("{\"index\":{}}");
                        myWriter.write(System.getProperty("line.separator"));
                    }
                    myWriter.write(commonFormat.toString());
                    myWriter.write(System.getProperty("line.separator"));
                }
                consWatchlist.add(anIndividual);

            }
            Element Entities = rootElement.element("ENTITIES");
            for (Iterator i = Entities.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                ObjectMapper mapper = new ObjectMapper();
                ObjectMapper objectMapper = new ObjectMapper();

                ObjectNode commonFormat = mapper.createObjectNode();

                HashSet<String> wholeNameSet = new HashSet<>();

                Watchlist anEntity = new Watchlist();

                String entity_id;
                String entity_type;
                String primary_name = "";
                List<String> whole_names;


                HashSet<String> countriesSet = new HashSet<>();
                HashSet<String> remarksSet = new HashSet<>();

                commonFormat.put("entity_type", "E");
                entity_type = "E";
                anEntity.setEntity_type(entity_type);



                Element DATAID = element.element("DATAID");
                if (DATAID != null) {

                    String id_in_document = DATAID.getText();
                    commonFormat.put("entity_id", source_document + "-" + id_in_document);
                    commonFormat.put("source_document", source_document);
                    commonFormat.put("id_in_document", id_in_document);


                    entity_id = "UNSecurityCouncil-" + DATAID.getText();
                    anEntity.setEntity_id(entity_id);
                    anEntity.setSource_document(source_document);
                    anEntity.setId_in_document(id_in_document);
                }

                Element FIRST_NAME = element.element("FIRST_NAME");
                if (FIRST_NAME != null) {
                    wholeNameSet.add(FIRST_NAME.getText());
                    primary_name = FIRST_NAME.getText();
                }

                Element UN_LIST_TYPE = element.element("UN_LIST_TYPE");
                if (UN_LIST_TYPE != null) {
                    remarksSet.add(UN_LIST_TYPE.getText());
                }

                Element COMMENTS1 = element.element("COMMENTS1");
                if (COMMENTS1 != null) { // one
                    remarksSet.add(COMMENTS1.getText());
                }

                List<Element> ENTITY_ALIAS = element.elements("ENTITY_ALIAS");
                if (ENTITY_ALIAS != null) {
                    HashSet<String> entityAliasSet = new HashSet<String>();
                    for (Element entityAliasList : ENTITY_ALIAS) {
                        Element ALIAS_NAME = entityAliasList.element("ALIAS_NAME");
                        if (ALIAS_NAME != null) {
                            entityAliasSet.add(ALIAS_NAME.getText());
                            wholeNameSet.add(ALIAS_NAME.getText());
                        }
                    }
                }

                List<Element> ENTITY_ADDRESS = element.elements("ENTITY_ADDRESS");
                if (ENTITY_ADDRESS != null) {
                    HashSet<String> entityAddressSet = new HashSet<String>();
                    for (Element entityAddressList : ENTITY_ADDRESS) {
                        Element COUNTRY = entityAddressList.element("COUNTRY");
                        if (COUNTRY != null) {
                            entityAddressSet.add(COUNTRY.getText());
                            countriesSet.add(COUNTRY.getText());
                        }
                    }
                }

                primary_name.trim();
                anEntity.setWhole_names(wholeNameSet);

                commonFormat.put("whole_names", mapper.valueToTree(wholeNameSet));
                commonFormat.put("primary_name", primary_name);

                if (printToFile) {
                    if (ndjson) {
                        myWriter.write("{\"index\":{}}");
                        myWriter.write(System.getProperty("line.separator"));
                    }
                    myWriter.write(commonFormat.toString());
                    myWriter.write(System.getProperty("line.separator"));

                }
                consWatchlist.add(anEntity);




            }

            myWriter.close();

            return consWatchlist;



        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}