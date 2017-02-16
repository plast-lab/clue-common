package org.clyze.persistent

import groovy.json.JsonSlurper
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by saiko on 9/11/2015.
 */
class JsonSpec extends Specification {

    @Shared TestItem item = createTestItem()
    @Shared ComplexTestItem citem = createComplexTestItem()
    @Shared NestedTestItem nitem = createNestedTestItem()

    def "json serialization is correct"() {
        setup:
        println obj.toJSON()

        expect:
        verify(obj, new JsonSlurper().parseText(json) as Map)

        where:
        obj   | json
        item  | '{"aBool":false,"aStr":"str","anInt":3,"id":"42"}'
        citem | '{"aBool":false,"aStr":"str","anInt":3,"id":"42","list":["one","two"],"map":{"one":1,"two":2}}'
        nitem | '{"aBool":false,"aStr":"str","anInt":3,"id":"42","list":["one","two"],"map":{"one":1,"two":2},"nested":{"memberStr":"member","memberInt":7}}'
    }

    def "json deserialization is correct"() {
        expect:
        obj.fromJSON(otherObj.toJSON())
        obj == otherObj

        where:
        obj                   | otherObj
        new TestItem()        | item
        new ComplexTestItem() | citem
        new NestedTestItem()  | nitem

    }

    private static TestItem createTestItem() {
        TestItem item = new TestItem()
        initTestItem(item)
        return item
    }

    private static ComplexTestItem createComplexTestItem() {
        ComplexTestItem item = new ComplexTestItem()
        initComplexTestItem(item)
        return item
    }

    private static NestedTestItem createNestedTestItem() {
        NestedTestItem item = new NestedTestItem()
        initComplexTestItem(item)
        item.nested = new TestClass(memberStr: "member", memberInt: 7)
        return item
    }

    private static void initTestItem(TestItem item) {
        item.id = "42"
        item.aStr = "str"
        item.anInt = 3
        item.aBool = false
    }

    private static void initComplexTestItem(ComplexTestItem item) {
        initTestItem(item)
        item.list = ["one", "two"]
        item.map = ["one":1, "two":2]
    }

    private static void verify(TestItem item, Map map) {
        if (item instanceof NestedTestItem)
            verifyNestedTestItem(item, map)
        else if (item instanceof ComplexTestItem)
            verifyComplexTestItem(item, map)
        else
            verifyTestItem(item, map)
    }

    private static void verifyTestItem(TestItem item, Map map) {
        assert item.id == map.id
        assert item.aBool == map.aBool
        assert item.aStr == map.aStr
        assert item.anInt == map.anInt
    }

    private static void verifyComplexTestItem(ComplexTestItem item, Map map) {
        verifyTestItem(item, map)
        item.list == map.list
        item.map == map.map
    }

    private static void verifyNestedTestItem(NestedTestItem item, Map map) {
        verifyComplexTestItem(item, map)
        assert item.nested.memberStr == map.nested.memberStr
        assert item.nested.memberInt == map.nested.memberInt
    }

}
