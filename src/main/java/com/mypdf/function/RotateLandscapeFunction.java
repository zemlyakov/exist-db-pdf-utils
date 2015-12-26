package com.mypdf.function;

import com.mypdf.PdfUtilsModule;
import com.mypdf.util.SequenceHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.exist.dom.QName;
import org.exist.xquery.*;
import org.exist.xquery.value.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mypdf.function.message.ErrorMessage.EMPTY_INPUT;
import static com.mypdf.function.message.ErrorMessage.INPUT_OUTPUT_SIZE_ERROR;

/**
 * Created by gzemlyakov.
 * gzemlyakov@gmail.com
 */
public class RotateLandscapeFunction extends BasicFunction {

    public static final int LANDSCAPE_DEGREE = 90;

    private static final Logger LOG = Logger.getLogger(RotateLandscapeFunction.class.getName());

    private List<String> rotatedDocuments;
    private List<String> errors;

    public final static FunctionSignature signature =
            new FunctionSignature(
                    new QName("rotateLandscape", PdfUtilsModule.NAMESPACE_URI, PdfUtilsModule.PREFIX),
                    "Rotate pages of PDF document to landscape.",
                    new SequenceType[] { new FunctionParameterSequenceType("text", Type.STRING, Cardinality.ZERO_OR_MORE,
                            "Full PDF documents names to rotate"),
                            new FunctionParameterSequenceType("text", Type.STRING, Cardinality.ZERO_OR_MORE,
                                    "Full output PDF documents names")},
                    new FunctionReturnSequenceType(Type.STRING, Cardinality.ZERO_OR_MORE,
                            "Successfully rotated document name"));


    public RotateLandscapeFunction(XQueryContext context) {
        super(context, signature);
        FunctionHelper.initLogger(LOG);
    }

    @Override
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        errors = new ArrayList<>();
        if (ArrayUtils.isEmpty(args)) {
            registerError(EMPTY_INPUT.toString());
            return SequenceHelper.listToSequence(errors);
        }
        return rotateLandscape(args[0], args[1]);
    }

    /**
     * Returns array of successfully rotated pdf documents names.
     * @param inputFiles Array of full documents names to rotate
     * @param outputFiles Array of full documents names which they will have after rotation
     * @return Array of successfully rotated documents or array of errors if operation failed.
     */
    public Sequence rotateLandscape(Sequence inputFiles, Sequence outputFiles) throws XPathException {
        rotatedDocuments = new ArrayList<>();
        if (!checkInput(inputFiles, outputFiles)) return SequenceHelper.listToSequence(errors);
        SequenceIterator inputIterator = inputFiles.iterate();
        SequenceIterator outputIterator = outputFiles.iterate();
        while (inputIterator.hasNext()) {
            rotateLandscapeDoc(inputIterator.nextItem().getStringValue(), outputIterator.nextItem().getStringValue());
        }
        return SequenceHelper.listToSequence(rotatedDocuments);
    }

    @SuppressWarnings("unchecked")
    private void rotateLandscapeDoc(String inputFile, String outputFile) {
        try {
            PDDocument document = PDDocument.load(inputFile);
            PDDocument rotatedDocument = new PDDocument();
            List<PDPage> pages = document.getDocumentCatalog().getAllPages();
            for (PDPage page : pages) {
                page.setRotation(LANDSCAPE_DEGREE);
                rotatedDocument.addPage(page);
            }
            rotatedDocument.save(outputFile);
            document.close();
            rotatedDocument.close();
            registerRotatedDoc(outputFile);
        } catch (IOException | COSVisitorException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            registerError(e.getMessage());
        }
    }

    private boolean checkInput(Sequence inputFiles, Sequence outputFiles) {
        if (isInputEmpty(inputFiles) || isInputEmpty(outputFiles)) return false;
        if (inputFiles.getItemCount() != outputFiles.getItemCount()) {
            registerError(INPUT_OUTPUT_SIZE_ERROR.toString());
            return false;
        }
        return true;
    }

    private boolean isInputEmpty(Sequence input) {
        if (input.isEmpty()) {
            registerError(EMPTY_INPUT.toString());
            return true;
        }
        return false;
    }

    private void registerRotatedDoc(String fileName) {
        rotatedDocuments.add(fileName);
    }

    private void registerError(String errorMessage) {
        errors.add(errorMessage);
    }

}
