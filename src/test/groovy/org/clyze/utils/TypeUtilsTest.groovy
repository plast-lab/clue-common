package org.clyze.utils

import spock.lang.Specification

class TypeUtilsTest extends Specification {
    def "RaiseTypeId"(String arg, String res) {
        expect:
        TypeUtils.raiseTypeId(arg) == res
        where:
        arg                                                | res
        "I"                                                | "int"
        "[I"                                               | "int[]"
        'Landroid/annotation/SdkConstant$SdkConstantType;' | 'android.annotation.SdkConstant$SdkConstantType'
        "Ljava/lang/Object;"                               | "java.lang.Object"
        "[Ljava/lang/Object;"                              | "java.lang.Object[]"
        "[[Ljava/lang/Object;"                             | "java.lang.Object[][]"

    }

    def "RaiseSignature"() {
        when:
        String sig1 = '(Landroid/graphics/Bitmap;IIZ)V'
        List<String> types1 = ['void', 'android.graphics.Bitmap', 'int', 'int', 'boolean']
        String sig2 = '(Ljava/nio/ByteBuffer;)Lcom/facebook/animated/gif/GifImage;'
        List<String> types2 = ['com.facebook.animated.gif.GifImage', 'java.nio.ByteBuffer']
        String sig3 = '(J)J'
        List<String> types3 = ['long', 'long']
        String sig4 = '(J)V'
        List<String> types4 = ['void', 'long']
        String sig5 = '()J'
        List<String> types5 = ['long']
        String sig6 = '(J[BII)V'
        List<String> types6 = ['void', 'long', 'byte[]', 'int', 'int']
        String sig7 = '(Landroid/graphics/Bitmap;ILandroid/graphics/Bitmap;II)V'
        List<String> types7 = ['void', 'android.graphics.Bitmap', 'int', 'android.graphics.Bitmap', 'int', 'int']

        then:
        assertRaisedSignature(sig1, types1)
        assertRaisedSignature(sig2, types2)
        assertRaisedSignature(sig3, types3)
        assertRaisedSignature(sig4, types4)
        assertRaisedSignature(sig5, types5)
        assertRaisedSignature(sig6, types6)
        assertRaisedSignature(sig7, types7)
    }

    void assertRaisedSignature(String sig, List<String> checkTypes) {
        List<String> types = TypeUtils.raiseSignature(sig)
        assert types.size() == checkTypes.size()
        for (int i = 0; i < types.size(); i++)
            assert types.get(i) == checkTypes.get(i)
    }

    def "IsPrimitiveType"(String t, boolean b) {
        expect:
        TypeUtils.isPrimitiveType(t) == b
        where:
        t                     | b
        "boolean"             | true
        "float"               | true
        "double"              | true
        "int"                 | true
        "int[]"               | false
        "short"               | true
        "char"                | true
        "java.lang.Character" | false
        "S"                   | false
    }

    def "IsLowLevelType"(char f, String t, boolean b) {
        expect:
        TypeUtils.isLowLevelType(f, t) == b
        where:
        f | t  | b
        '[' | "Ljava/lang/Object"  | true
        'L' | "java/lang/Object;"  | true
        'L' | "java/lang/Object"   | false
        'b' | "oolean"             | false
        'B' | ""                   | false
        'j' | "ava.lang.Character" | false
    }
}
