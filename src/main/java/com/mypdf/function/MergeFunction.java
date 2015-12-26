package com.mypdf.function;

import com.mypdf.PdfUtilsModule;
import com.mypdf.util.SequenceHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.exist.dom.QName;
import org.exist.xquery.*;
import org.exist.xquery.value.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mypdf.function.message.ErrorMessage.EMPTY_INPUT;
import static com.mypdf.function.message.ErrorMessage.INVALID_OUTPUT;

/**
 * Created by gzemlyakov.
 * gzemlyakov@gmail.com
 */
public class MergeFunction extends BasicFunction {

    private static final Logger LOG = Logger.getLogger(MergeFunction.class.getName());

    public final static FunctionSignature signature =
            new FunctionSignature(
                    new QName("merge", PdfUtilsModule.NAMESPACE_URI, PdfUtilsModule.PREFIX),
                    "Merge PDF document.",
                    new SequenceType[] { new FunctionParameterSequenceType("text", Type.STRING, Cardinality.ZERO_OR_MORE,
                            "Full PDF documents names to merge"), new FunctionParameterSequenceType("text", Type.STRING, Cardinality.ZERO_OR_MORE,
                            "Output PDF file name")},
                    new FunctionReturnSequenceType(Type.STRING, Cardinality.ZERO_OR_MORE,
                            "Successfully merged document name"));

    public MergeFunction(XQueryContext context) {
        super(context, signature);
        FunctionHelper.initLogger(LOG);
    }

    @Override
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        if (ArrayUtils.isEmpty(args)) {
            return SequenceHelper.toSequence(EMPTY_INPUT.toString());
        }
        return merge(args[0], args[1]);
    }

    /**
     * Returns name of merged document.
     * @param filesToMerge Array of full documents names to merge
     * @param outputFile Name of merged document
     * @return Name of merged document or error message
     */
    private Sequence merge(Sequence filesToMerge, Sequence outputFile) throws XPathException {
        if (isInputEmpty(filesToMerge) || isInputEmpty(outputFile)) return SequenceHelper.toSequence(EMPTY_INPUT.toString());
        if (outputFile.getItemCount() > 1) return SequenceHelper.toSequence(INVALID_OUTPUT.toString());
        try {
            PDFMergerUtility mergerUtility = new PDFMergerUtility();
            SequenceIterator fileToMergeIterator = filesToMerge.iterate();
            while (fileToMergeIterator.hasNext()) {
                mergerUtility.addSource(fileToMergeIterator.nextItem().getStringValue());
            }
            mergerUtility.setDestinationFileName(outputFile.iterate().nextItem().getStringValue());
            mergerUtility.mergeDocuments();
            return outputFile;
        } catch (COSVisitorException | IOException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return SequenceHelper.toSequence(e.getMessage());
        }
    }

    private boolean isInputEmpty(Sequence input) {
        return input.isEmpty();
    }

}
