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
import de.unihd.hra.libs.java.luceneTranscodingAnalyzer.UnicodeBlocksDetection;

/**
 * Implements a method for transcoding a string to SLP1 format.
 * 
 * @author Claudius Teodorescu <claudius.teodorescu@gmail.com>
 */

public class TranscodeFunction extends BasicFunction {

	private static final Logger logger = Logger.getLogger(TranscodeFunction.class);
	private final HashMap<String, TransformMap> transformMaps = TransformMaps.transformMaps;

	public final static FunctionSignature signature = new FunctionSignature(
			new QName("transcode", ExistTransliterationModule.NAMESPACE_URI, ExistTransliterationModule.PREFIX),
			"This function transcodes a string to SLP1 format.",
			new SequenceType[] { new FunctionParameterSequenceType("data", Type.STRING, Cardinality.EXACTLY_ONE,
					"The data to be transcoded") },
			new FunctionReturnSequenceType(Type.STRING, Cardinality.EXACTLY_ONE, "The transcoded string."));

	public TranscodeFunction(XQueryContext context) {
		super(context, signature);
	}

	@Override
	public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {

		Sequence result = Sequence.EMPTY_SEQUENCE;
		String transcodingResult = null;

		String data = args[0].getStringValue();
		logger.debug("data = " + data);

		try {
			if (UnicodeBlocksDetection.detectDevanagariBlocks(data)) {
				transcodingResult = WebServices.transformString(data, transformMaps.get("deva2slp1"));
			}

			if (UnicodeBlocksDetection.detectDevanagariTransliterationBlocks(data)) {
				transcodingResult = WebServices.transformString(data, transformMaps.get("roman2slp1"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("transcodingResult = " + transcodingResult);

		result = new StringValue(transcodingResult);

		return result;
	}
}