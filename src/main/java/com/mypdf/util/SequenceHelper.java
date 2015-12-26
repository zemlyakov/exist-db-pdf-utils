package com.mypdf.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.ValueSequence;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by gzemlyakov.
 * gzemlyakov@gmail.com
 */
public class SequenceHelper {

    public static Sequence arrayToSequence(String ... strings) {
        return ArrayUtils.isNotEmpty(strings) ? listToSequence(Arrays.asList(strings)) : Sequence.EMPTY_SEQUENCE;
    }

    public static Sequence listToSequence(Collection<? extends String> collection) {
        if (CollectionUtils.isEmpty(collection)) return Sequence.EMPTY_SEQUENCE;
        ValueSequence result = new ValueSequence();
        for (String str : collection) result.add(new StringValue(str));
        return result;
    }

    public static Sequence toSequence(String str) {
        ValueSequence result = new ValueSequence();
        result.add(new StringValue(str));
        return result;
    }

}
