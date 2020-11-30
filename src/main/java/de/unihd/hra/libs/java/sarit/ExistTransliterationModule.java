package de.unihd.hra.libs.java.sarit;

import java.util.List;
import java.util.Map;

import org.exist.xquery.AbstractInternalModule;
import org.exist.xquery.FunctionDef;
import org.sanskritlibrary.ResourceInputStream;
import org.sanskritlibrary.webservice.TransformMap;

import de.unihd.hra.libs.java.luceneTranscodingAnalyzer.TransformMaps;

/**
 * Implements the module definition.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */
public class ExistTransliterationModule extends AbstractInternalModule {

	static {
		ResourceInputStream ris = new TransformMaps.Transcoders();

		try {
			TransformMaps.transformMaps.put("deva2slp1", new TransformMap("*:deva->slp1", ris, TransformMaps.fsmh));
			TransformMaps.transformMaps.put("roman2slp1", new TransformMap("*:roman->slp1", ris, TransformMaps.fsmh));
			TransformMaps.transformMaps.put("slp12deva", new TransformMap("*:slp1->deva", ris, TransformMaps.fsmh));
			TransformMaps.transformMaps.put("slp12roman", new TransformMap("*:slp1->roman", ris, TransformMaps.fsmh));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String NAMESPACE_URI = "http://hra.uni-heidelberg.de/ns/sarit-transliteration";
	public static String PREFIX = "sarit-slp1";
	public final static String INCLUSION_DATE = "2016-04-23";
	public final static String RELEASED_IN_VERSION = "eXist-1.5";

	private final static FunctionDef[] functions = {
			new FunctionDef(TransliterateFunction.signature, TransliterateFunction.class),
			new FunctionDef(TranscodeFunction.signature, TranscodeFunction.class) };

	public ExistTransliterationModule(Map<String, List<? extends Object>> parameters) {
		super(functions, parameters);
	}

	@Override
	public String getDefaultPrefix() {
		return PREFIX;
	}

	@Override
	public String getDescription() {
		return "A module for performing transliteration between various scripts, by using SLP1 as pivotal format.";
	}

	@Override
	public String getNamespaceURI() {
		return NAMESPACE_URI;
	}

	@Override
	public String getReleaseVersion() {
		return RELEASED_IN_VERSION;
	}

}
