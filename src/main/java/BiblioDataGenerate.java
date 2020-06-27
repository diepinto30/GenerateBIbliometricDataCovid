import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.io.*;

public class BiblioDataGenerate {
    public static final String SEPARADOR = ",";
    public static String[][] atributos = new String[3000][28];
    public static int limite = 0;

    public static String[] tipos_Fabio = {"Article", "Erratum","Review","ConferencePaper", "Letter", "Editorial"};

    public static void main(String[] args) throws FileNotFoundException {



        leerDatos();

        Model model = ModelFactory.createDefaultModel();

        //definición del fichero donde insertaremos los datos RDF
        File f = new File("src/main/resources/RDF/BiblioDataAuthors.rdf");
        FileOutputStream os = new FileOutputStream(f);

        //Set prefix for the URI base (new data)
        String dataPrefix = "http://utpl.edu.ec/COVIDBiblio/ontology/";
        model.setNsPrefix("myData", dataPrefix);

        Model myOntoModel = ModelFactory.createDefaultModel();

        String dbo = "http://dbpedia.org/ontology/";
        model.setNsPrefix("dbo", dbo);
        Model dboModel = ModelFactory.createDefaultModel();

        String dbr = "http://dbpedia.org/resource/";
        model.setNsPrefix("dbr", dbr);
        Model dbrModel = ModelFactory.createDefaultModel();


        String fabio = "http://purl.org/spar/fabio/";
        model.setNsPrefix("fabio", fabio);
        Model fabioModel = ModelFactory.createDefaultModel();


        String dcat = "https://www.w3.org/TR/vocab-dcat-2/";
        model.setNsPrefix("dcat", dcat);
        Model dcatModel = ModelFactory.createDefaultModel();

        String prov = "https://www.w3.org/ns/prov";
        model.setNsPrefix("prov", prov);
        Model provModel = ModelFactory.createDefaultModel();



        for (int i = 1; i < limite; i++) {

            // AUTORES
            String nombres = atributos[i][1];
            String[] parts_nombres = nombres.split(";");
            String id_autores = atributos[i][2];
            String[] parts_id_autores = id_autores.split(";");

            // DOCUMENTOS

            String titulo = atributos[i][3];
            String eid_documento = atributos[i][4];

            String URI_DOCUMENTO = dataPrefix + eid_documento;




            String anio = atributos[i][5];
            String source_title = atributos[i][6];
            String vol = atributos[i][7];
            String issue = atributos[i][8];
            String num_citas = atributos[i][12];
            String doi = atributos[i][13];
            String url = atributos[i][14];
            String document_type = atributos[i][15];
            String stage = atributos[i][16];
            String access = atributos[i][17];


            String fuente = atributos[i][18];
            //URI DE LA FUENTE
            String URI_FUENTE = dataPrefix + fuente.replace(" ","");

            String language = atributos[i][19];
            String publisher = atributos[i][21];
            String issn = atributos[i][22];
            String affiliation = atributos[i][23];
            String pais = atributos[i][24];



            // CREACION DE SCIENTIFIC DATABASE
            Resource fuenteDocumento = model.createResource(URI_FUENTE)
                    .addProperty(FOAF.name, fuente)
                    .addProperty(RDFS.subClassOf, FOAF.Organization);

            //CREACION DEL DATASET
            String URIDataset = dataPrefix + "BiblioDataCovid/";
            Resource datasetInfo = model.createResource(URIDataset)
                    .addProperty(RDF.type, dcatModel.getResource(dcat + "Dataset/")
                            .addProperty(DCTerms.title, "BiblioDataCovid")
                            .addProperty(dcatModel.getProperty(dcat + "keyword/"),("covid19; sars-cov-2"))
                            .addProperty(DCTerms.modified, "10-06-2020"));

            //CREACION DEL CATALOG
            String URICatalog = dataPrefix+"CatalogScopusCOVID/";
            Resource catalog = model.createResource(URICatalog)
                    .addProperty(DCTerms.title, "CatalogScopusCOVID")
                    .addProperty(RDF.type, dcatModel.getResource(dcat + "Catalog/")
                            .addProperty(DCTerms.publisher, fuenteDocumento)
                            .addProperty(dcatModel.getProperty(dcat +"dataset"), datasetInfo ));

            // CREACION DE DOCUMENTO BIBLIOGRAFICO
            Resource documento = model.createResource(URI_DOCUMENTO)
                    .addProperty(DCTerms.title, titulo)
                    .addProperty(DCTerms.date, anio)
                    .addProperty(myOntoModel.getProperty(dataPrefix +"citationsCount"), num_citas)
                    .addProperty(DCTerms.language, dboModel.getResource(dbr+language))
                    .addProperty(RDFS.subClassOf, fabioModel.getResource(fabio+ "ScholaryWork/")
                            .addProperty(provModel.getProperty(prov + "wasDerivedFrom"), datasetInfo));
            // Se crea el tipo de documento
            // Se compara el tipo con los de fabio y si es igual toma la uri de fabio
            for (String nombre : tipos_Fabio) {
                if(document_type.replace(" ","").equals(nombre)){
                    documento.addProperty(RDF.type, fabioModel.getResource(fabio + document_type.replace(" ","")));
                }else{
                    documento.addProperty(RDF.type, myOntoModel.getResource(dataPrefix + document_type.replace(" ","")));
                }
            }


            for (int j = 0; j < parts_id_autores.length; j++) {
                // CREANDO INSTANCIAS DE LOS AUTORES
                int long_parts_nombres = parts_nombres.length;
                String URI_AUTOR = dataPrefix+ "Author/" + parts_id_autores[j];
                Resource autor = model.createResource(URI_AUTOR)
                        .addProperty(RDF.type, myOntoModel.getResource(dataPrefix+"Author"))
                        .addProperty(RDFS.subClassOf, FOAF.Person);
                if (j<long_parts_nombres) {
                    autor.addProperty(FOAF.name, parts_nombres[j]);
                }

                // Vinculando el autor al documento
                documento.addProperty(DCTerms.creator, autor);

            }
        }

        StmtIterator iter = model.listStatements();
        // Print the triplets
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();  // get next statement
            Resource subject = stmt.getSubject();     // get the subject
            Property predicate = stmt.getPredicate();   // get the predicate
            RDFNode object = stmt.getObject();      // get the object

            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if (object instanceof Resource) {
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.toString() + "\"");
            }

            System.out.println(" .");
        }

        // Save to a file
        RDFWriter writer = model.getWriter("RDF/XML"); //RDF/XML
        writer.write(model, os, dataPrefix);

    }

    public static void leerDatos() {
        limite = 0;
        BufferedReader bufferLectura = null;
        try {
            // Abrir el .csv en buffer de lectura
            bufferLectura = new BufferedReader(new FileReader("src/main/resources/datosFin.csv"));

            // Leer una linea del archivo
            String linea = bufferLectura.readLine();

            while (linea != null) {
                // Sepapar la linea leída con el separador definido previamente
                String[] campos = linea.split(SEPARADOR);

                atributos[limite] = linea.split(SEPARADOR);
                // Volver a leer otra línea del fichero
                linea = bufferLectura.readLine();
                limite = limite + 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Cierro el buffer de lectura
            if (bufferLectura != null) {
                try {
                    bufferLectura.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
