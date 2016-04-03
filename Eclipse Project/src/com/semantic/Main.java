package com.semantic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class Main {
	Model model;

	public Main() throws FileNotFoundException {
		model = ModelFactory.createDefaultModel();
		File dir = new File("resource/");

		File[] fileList = dir.listFiles();

		for (File f : fileList) {
			InputStream in = null;

			in = new FileInputStream(f);

			if (in != null)
				model.read(in, null, "RDF/XML");
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		Main jenaTest = new Main();

		String sparqlQueryString = "PREFIX o: <http://www.owl-ontologies.com/Ontology1372543966.owl#>  "
				+ "SELECT ?isim WHERE { "
				+ " ?Insan o:isim ?isim . "
				+ " ?Kedi o:hayvanidir ?Insan . "
				+ " ?Kedi o:isim \"Pamuk\" . }";

		jenaTest.executeQuery(sparqlQueryString);

		sparqlQueryString = "PREFIX o: <http://www.owl-ontologies.com/Ontology1372543966.owl#>  "
				+ "SELECT ?isim WHERE { "
				+ " ?Inek o:isim ?isim . "
				+ " ?Insan o:isim \"Veli\" . "
				+ " ?Insan o:sahibidir ?Inek . }";

		jenaTest.executeQuery(sparqlQueryString);
	}

	public void executeQuery(String sparqlQueryString)
			throws FileNotFoundException {
		Query query = QueryFactory.create(sparqlQueryString);
		ARQ.getContext().setTrue(ARQ.useSAX);

		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		ResultSet results = qexec.execSelect();

		System.out.println("\nRESULT: ");
		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			System.out.println(soln.toString());
		}
		qexec.close();
	}
}
