package com.mypdf;

import com.mypdf.function.MergeFunction;
import com.mypdf.function.RotateLandscapeFunction;
import org.exist.xquery.AbstractInternalModule;
import org.exist.xquery.FunctionDef;

import java.util.List;
import java.util.Map;

/**
 * Created by gzemlyakov.
 * gzemlyakov@gmail.com
 */
public class PdfUtilsModule extends AbstractInternalModule {

    public final static String NAMESPACE_URI = "http://mypdf.com/PdfUtilsModule";
    public final static String PREFIX = "mypdf";
    public final static String DESCRIPTION = "Module to rotate and merge PDF documents";
    public final static String RELEASED_IN_VERSION = "eXist-2.2";

    private final static FunctionDef[] functions = {
            new FunctionDef(RotateLandscapeFunction.signature, RotateLandscapeFunction.class),
            new FunctionDef(MergeFunction.signature, MergeFunction.class)
    };

    public PdfUtilsModule(Map<String, List<? extends Object>> parameters) {
        super(functions, parameters);
    }

    @Override
    public String getNamespaceURI() {
        return NAMESPACE_URI;
    }

    @Override
    public String getDefaultPrefix() {
        return PREFIX;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getReleaseVersion() {
        return RELEASED_IN_VERSION;
    }
}
