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

public class DeployFileLoaderFull {
    public static ArrayList<Watchlist>  convert() {
        String xmlPath = "src/main/resources/xml/20210208-FULL(xsd).xml";
        boolean printToFile = false;
        boolean ndjson = false;

        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlPath);
            Element rootElement = document.getRootElement();
            ArrayList<Watchlist> fullWatchlist = new ArrayList<>();
            FileWriter myWriter = new FileWriter("src/main/resources/json/20210208-FULL(xsd).json");

            String source_document = "EUSecurityCouncil";
            for (Iterator i = rootElement.elementIterator(); i.hasNext(); ) {

                ObjectMapper mapper = new ObjectMapper();

                ObjectNode commonFormat = mapper.createObjectNode();
                Watchlist anIndividual = new Watchlist();

                String entity_id;
                String entity_type;
                String primary_name = "";
                List<String> whole_names;

                Element sanctionEntity = (Element) i.next();
                Attribute logicalIdAttribute = sanctionEntity.attribute("logicalId");
                if (logicalIdAttribute != null) {
                    String logicalId = logicalIdAttribute.getValue();
                    entity_id = source_document + "-" + logicalId;
                    commonFormat.put("entity_id", entity_id);
                    commonFormat.put("source_document", source_document);
                    commonFormat.put("id_in_document", logicalId);

                    anIndividual.setEntity_id(entity_id);
                    anIndividual.setSource_document(source_document);
                    anIndividual.setId_in_document(logicalId);
                }

                HashSet<String> remarks = new HashSet<>();
                Element remarkElement = sanctionEntity.element("remark");
                if (remarkElement != null) {
                    String remark = remarkElement.getText();
                    remarks.add(remark);
                }

                Element subjectTypeElement = sanctionEntity.element("subjectType");
                if (subjectTypeElement != null) {
                    SubjectType subjectInstance = new SubjectType();
                    subjectInstance.code = subjectTypeElement.attributeValue("code");
                    subjectInstance.classificationCode = subjectTypeElement.attributeValue("classificationCode");

                    commonFormat.put("entity_type", subjectTypeElement.attributeValue("classificationCode"));
                    entity_type = subjectTypeElement.attributeValue("classificationCode");
                    anIndividual.setEntity_type(entity_type);


                }

                HashSet<String> wholeNameSet = new HashSet<>();
                List<Element> nameAliasElementsList = sanctionEntity.elements("nameAlias");
                if (nameAliasElementsList != null) {
                    HashSet<Alias> nameAliasSet = new HashSet<>();
                    for (Element nameAlias : nameAliasElementsList) {
                        Alias oneAlias = new Alias();
                        Attribute firstName = nameAlias.attribute("firstName");
                        if (firstName != null) {
                            oneAlias.firstName = firstName.getValue();
                        }

                        Attribute middleName = nameAlias.attribute("middleName");
                        if (middleName != null) {
                            oneAlias.middleName = middleName.getValue();
                        }

                        Attribute lastName = nameAlias.attribute("lastName");
                        if (nameAlias.attribute("lastName") != null) {
                            oneAlias.lastName = lastName.getValue();
                        }

                        Attribute wholeName = nameAlias.attribute("wholeName");
                        if (wholeName != null) {
                            oneAlias.wholeName = wholeName.getValue();

                            wholeNameSet.add(wholeName.getValue());
                            if (primary_name == "" && wholeName.getValue() != "") {
                                primary_name = wholeName.getValue();
                                commonFormat.put("primary_name", primary_name);
                                anIndividual.setPrimary_name(primary_name);
                            }

                        }

                        Attribute function = nameAlias.attribute("function");
                        if (function != null) {
                            oneAlias.function = function.getValue();
                            remarks.add(function.getValue());

                        }

                        Attribute gender = nameAlias.attribute("gender");
                        if (gender != null) {
                            oneAlias.gender = gender.getValue();
                            remarks.add(gender.getValue());

                        }

                        Attribute title = nameAlias.attribute("title");
                        if (title != null) {
                            oneAlias.title = title.getValue();
                            remarks.add(title.getValue());
                        }

                        Attribute nameLanguage = nameAlias.attribute("nameLanguage");
                        if (nameLanguage != null) {
                            oneAlias.nameLanguage = nameLanguage.getValue();
                        }
                        nameAliasSet.add(oneAlias);
                    }
                }

                HashSet<String> countriesSet = new HashSet<>();
                List<Element> citizenshipList = sanctionEntity.elements("citizenship");
                if (citizenshipList != null) {
                    HashSet<Citizenship> citizenshipSet = new HashSet<>();
                    for (Element citizenshipItem : citizenshipList) {
                        Citizenship citizenshipItemInstance = new Citizenship();
                        Attribute countryDescription = citizenshipItem.attribute("countryDescription");
                        if (countryDescription != null) {
                            citizenshipItemInstance.countryDescription = countryDescription.getValue();
                        }

                        Attribute countryIso2Code = citizenshipItem.attribute("countryIso2Code");
                        if (countryIso2Code != null) {
                            citizenshipItemInstance.countryIso2Code = countryIso2Code.getValue();
                            countriesSet.add(countryIso2Code.getValue());
                        }

                        Element remark = citizenshipItem.element("remark");
                        if (remark != null) {
                            citizenshipItemInstance.remark = remark.getText();
                            remarks.add(remark.getText());
                        }
                        citizenshipSet.add(citizenshipItemInstance);
                    }
                }

                List<Element> birthdateList = sanctionEntity.elements("birthdate");
                if (birthdateList != null) {
                    HashSet<Birthdate> birthdateSet = new HashSet<>();
                    for (Element birthdateItem : birthdateList) {
                        Birthdate birthdateInstance = new Birthdate();
                        Attribute birthdate = birthdateItem.attribute("birthdate");
                        if (birthdate != null) {
                            birthdateInstance.birthdate = birthdate.getValue();
                        }

                        Attribute countryIso2Code = birthdateItem.attribute("countryIso2Code");
                        if (countryIso2Code != null) {
                            birthdateInstance.countryIso2Code = countryIso2Code.getValue();
                            countriesSet.add(countryIso2Code.getValue());
                            countriesSet.add(countryIso2Code.getValue());
                        }

                        Element countryDescription = birthdateItem.element("countryDescription");
                        if (countryDescription != null) {
                            birthdateInstance.countryDescription = countryDescription.getText();
                        }
                        birthdateSet.add(birthdateInstance);
                    }
                }

                List<Element> identificationList = sanctionEntity.elements("identification");
                if (identificationList != null) {
                    HashSet<Identification> identificationSet = new HashSet<>();
                    for (Element identificationItem : identificationList) {
                        Identification identificationInstance = new Identification();
                        Attribute countryIso2Code = identificationItem.attribute("countryIso2Code");
                        if (countryIso2Code != null) {
                            identificationInstance.countryIso2Code = countryIso2Code.getValue();
                            countriesSet.add(countryIso2Code.getValue());
                        }

                        Element countryDescription = identificationItem.element("countryDescription");
                        if (countryDescription != null) {
                            identificationInstance.countryDescription = countryDescription.getText();
                        }

                        Attribute diplomatic = identificationItem.attribute("diplomatic");
                        if (identificationItem.attribute("diplomatic") != null) {
                            identificationInstance.diplomatic = Boolean.parseBoolean(diplomatic.getValue());
                        }

                        Attribute issuedBy = identificationItem.attribute("issuedBy");
                        if (issuedBy != null) {
                            identificationInstance.issuedBy = issuedBy.getValue();
                            countriesSet.add(issuedBy.getValue());
                        }

                        Attribute nameOnDocument = identificationItem.attribute("nameOnDocument");
                        if (nameOnDocument != null) {
                            identificationInstance.nameOnDocument = nameOnDocument.getValue();
                            wholeNameSet.add(nameOnDocument.getValue());
                        }

                        Attribute identificationTypeCode = identificationItem.attribute("identificationTypeCode");
                        if (identificationTypeCode != null) {
                            identificationInstance.identificationTypeCode = identificationTypeCode.getValue();
                        }

                        Attribute identificationTypeDescription = identificationItem.attribute("identificationTypeDescription");
                        if (identificationTypeDescription != null) {
                            identificationInstance.identificationTypeDescription = identificationTypeDescription.getValue();
                        }

                        List<Element> remarkList = identificationItem.elements("remark");
                        if (remarkList != null) {
                            for (Element remarkItem : remarkList) {
                                identificationInstance.remark.add(remarkItem.getText());
                                remarks.add(remarkItem.getText());
                            }
                        }
                        identificationSet.add(identificationInstance);
                    }
                }

                List<Element> addressList = sanctionEntity.elements("address");
                if (addressList != null) {
                    HashSet<Address> addressSet = new HashSet<>();
                    for (Element addressItem : addressList) { // This doesn't get us a list of aliases, it just return a single alias (nothing to loop through)
                        //System.out.println("Address List: ");
                        //  if (address != null) { // unnecessary
                        Address addressInstance = new Address();
                        Attribute countryIso2Code = addressItem.attribute("countryIso2Code");
                        if (countryIso2Code != null) {

                            addressInstance.countryIso2Code = countryIso2Code.getValue();
                            countriesSet.add(countryIso2Code.getValue());


                        }

                        Element countryDescription = addressItem.element("countryDescription");
                        if (countryDescription != null) {

                            addressInstance.countryDescription = countryDescription.getText();
                        }


                        List<Element> remarkList = addressItem.elements("remark");
                        if (remarkList != null) {

                            for (Element remarkItem : remarkList) {

                                addressInstance.remark.add(remarkItem.getText());
                                remarks.add(remarkItem.getText());

                            }
                        }


                        List<Element> contactInfoList = addressItem.elements("contactInfo");
                        if (contactInfoList != null) {

                            for (Element contactInfoItem : contactInfoList) {

                                ContactInfo newContactInfo = new ContactInfo();

                                newContactInfo.key = contactInfoItem.attributeValue("key");
                                newContactInfo.value = contactInfoItem.attributeValue("value");
                                addressInstance.contactInfo.add(newContactInfo);

                            }
                        }
                        addressSet.add(addressInstance);



                    }
                }







                commonFormat.put("whole_names", mapper.valueToTree(wholeNameSet));
                anIndividual.setWhole_names(wholeNameSet);

                fullWatchlist.add(anIndividual);




                if (printToFile) {
                    if (ndjson) {
                        myWriter.write("{\"index\":{}}");
                        myWriter.write(System.getProperty("line.separator"));
                    }

                    myWriter.write(commonFormat.toString());
                    myWriter.write(System.getProperty("line.separator"));
                }

            }




            myWriter.close();

            return fullWatchlist;




        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}


class Alias {
    public String firstName = "";
    public String middleName = "";
    public String lastName = "";
    public String wholeName = "";
    public String function = "";
    public String gender = "";
    public String title = "";
    public String nameLanguage = "";
}

class Citizenship {
    public String countryDescription = "";
    public String countryIso2Code = "";
    public String remark = "";

}

class Birthdate {
    public String birthdate = "";
    public String countryIso2Code = "";
    public String countryDescription = "";
}


class Identification {
    public String countryIso2Code = "";
    public String countryDescription = "";
    public boolean diplomatic = false;
    public String issuedBy = "";
    public String nameOnDocument = "";
    public String identificationTypeCode = "";
    public String identificationTypeDescription = "";
    public ArrayList<String> remark = new ArrayList<>();
}

class Address {
    public String countryIso2Code = "";
    public String countryDescription = "";
    public ArrayList<String> remark = new ArrayList<>();
    public ArrayList<ContactInfo> contactInfo = new ArrayList<>();


}

class ContactInfo {
    public String key = "";
    public String value = "";
}

class SubjectType {
    public String code = "";
    public String classificationCode = "";
}