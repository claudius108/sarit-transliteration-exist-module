package de.unihd.hra.libs.java.sarit;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;
import org.sanskritlibrary.webservice.TransformMap;
import org.sanskritlibrary.webservice.WebServices;

import de.unihd.hra.libs.java.luceneTranscodingAnalyzer.TransformMaps;

/**
 * Implements a method for transliterate a string.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */

public class TransliterateFunction extends BasicFunction {

	private static final Logger log = Logger.getLogger(TransliterateFunction.class);
	private final HashMap<String, TransformMap> transformMaps = TransformMaps.transformMaps;

	public final static FunctionSignature signature = new FunctionSignature(
			new QName("transliterate", ExistTransliterationModule.NAMESPACE_URI, ExistTransliterationModule.PREFIX),
			"This function transliterates a string.",
			new SequenceType[] {
					new FunctionParameterSequenceType("data", Type.STRING, Cardinality.EXACTLY_ONE,
							"The data to be transliterated"),
					new FunctionParameterSequenceType("source-script", Type.STRING, Cardinality.EXACTLY_ONE,
							"The source script."),
					new FunctionParameterSequenceType("target-script", Type.STRING, Cardinality.EXACTLY_ONE,
							"The target script.") },
			new FunctionReturnSequenceType(Type.STRING, Cardinality.EXACTLY_ONE, "The transliterated string."));

	public TransliterateFunction(XQueryContext context) {
		super(context, signature);
//		this.transformMaps = TransformMaps.transformMaps;
	}

	@Override
	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {

		Sequence result = Sequence.EMPTY_SEQUENCE;
		String transliterationResult = null;

		String data = args[0].getStringValue();
		log.debug("data = " + data);
		String sourceScript = args[1].getStringValue();
		log.debug("sourceScript = " + sourceScript);
		String targetScript = args[2].getStringValue();
		log.debug("targetScript = " + targetScript);

		try {
			// transcode the data from source script to SLP1
			transliterationResult = WebServices.transformString(data, transformMaps.get(sourceScript + "2slp1"));

			// transcode the data from SLP1 to source script
			transliterationResult = WebServices.transformString(transliterationResult,
					transformMaps.get("slp12" + targetScript));
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("transliterationResult = " + transliterationResult);

		result = new StringValue(transliterationResult);

		return result;
	}
}