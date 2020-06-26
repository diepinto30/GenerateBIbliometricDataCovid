import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;

        import java.io.*;

public class BiblioDataGenerate {
    public static final String SEPARADOR = ",";
    public static String[][] atributos = new String[3000][28];
    public static int limite = 0;

    public static void main(String[] args) throws FileNotFoundException {

        leerDatos();

        Model model = ModelFactory.createDefaultModel();
        File f = new File("src/main/resources/RDF/BiblioDataAuthors.rdf"); //definición del fichero donde insertaremos los datos RDF
        FileOutputStream os = new FileOutputStream(f);

        //Set prefix for the URI base (new data)
        String dataPrefix = "http://example.org/data/";
        model.setNsPrefix("myData", dataPrefix);

        String dbo = "http://dbpedia.org/ontology/";
        model.setNsPrefix("dbo", dbo);
        Model dboModel = ModelFactory.createDefaultModel();


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
            String language = atributos[i][19];
            String publisher = atributos[i][21];
            String issn = atributos[i][22];
            String affiliation = atributos[i][23];
            String pais = atributos[i][24];

            //System.out.println(atributos[i][0]);


            Resource documento = model.createResource(URI_DOCUMENTO)
                    .addProperty(DCTerms.title, titulo);

            for (int j = 0; j < parts_id_autores.length; j++) {
                // CREANDO INSTANCIAS DE LOS AUTORES
                int long_parts_nombres = parts_nombres.length;
                String URI_AUTOR = dataPrefix + parts_id_autores[j];
                Resource autor = model.createResource(URI_AUTOR)
                        .addProperty(RDF.type, FOAF.Person);
                if (j<long_parts_nombres) {
                    autor.addProperty(FOAF.name, parts_nombres[j]);
                }



                // Vinculando el autor al documento
                documento.addProperty(DCTerms.creator, autor);

            }

            /*
            // create people 2 and add the properties cascading style
            String personURI = dataPrefix + atributos[i][0];
            String name = atributos[i][1];
            String lastname = atributos[i][2];
            String country = atributos[i][3];
            String country_cod = atributos[i][4];
            String email = atributos[i][5];
            String id_father = atributos[i][6];
            String id_mother = atributos[i][7];
            String Ocupation = atributos[i][8];

            //Resource country_resource = model.createResource(country);
            Resource personFunction = model.createResource(dataPrefix + Ocupation)
                    .addProperty(RDF.type, dboModel.getResource(dbo + "PersonFunction"))
                    .addProperty(RDFS.label, Ocupation);

            Resource person = model.createResource(personURI)
                    .addProperty(RDF.type, FOAF.Person)
                    .addProperty(FOAF.name, name)
                    .addProperty(FOAF.lastName, lastname)
                    .addProperty(dboModel.getProperty(dbo + "occupation"), personFunction);

            if(!(country.equals(""))){
                person.addProperty(dboModel.getProperty(dbo + "country"), dboModel.getResource(dbo + country));
            }
            if(!(id_father.equals(""))){
                person.addProperty(dboModel.getProperty(dbo + "father"), id_father);
            }
            if(!(id_mother.equals(""))){
                person.addProperty(dboModel.getProperty(dbo + "mother"), id_mother);
            }
             */
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
