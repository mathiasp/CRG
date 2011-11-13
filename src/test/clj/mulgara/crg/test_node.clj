(ns mulgara.crg.test-node
  (use [mulgara.crg.node]
       [clojure.test])
  (import [java.net URI URL]
          [mulgara.crg.node Iri QName Literal BlankNode]))

(def iri-str "http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
(def iri-str-dc "http://purl.org/dc/terms/title")
(def iri-unk "http://example.com/foo")

(deftest test-iri
         (is (= (str (Iri. "foo"))
                "<foo>")))
(deftest test-qname
         (is (= (str (QName. "foo" "bar"))
                "foo:bar")))

(deftest test-qn-iri
         (is (= (to-iri (QName. "rdf" "type"))
                (Iri. iri-str)))
         (is (= (to-iri (QName. "" "type"))
                (Iri. "type"))))

(deftest test-iri-node
         (is (= (iri iri-str)
                (QName. "rdf" "type")))
         (is (= (iri iri-str-dc)
                (QName. "dcterms" "title")))
         (is (= (iri iri-unk)
                (Iri. iri-unk)))
         (is (= (iri "foo:bar")
                (Iri. "foo:bar")))
         (is (= (iri :foo/bar)
                (QName. "foo" "bar")))
         (is (= (iri :rdf/type)
                (QName. "rdf" "type")))
         (is (= (iri :type)
                (QName. "" "type")))
         (is (= (iri (URI. iri-str))
                (QName. "rdf" "type")))
         (is (= (iri (URI. iri-unk))
                (Iri. iri-unk)))
         (is (= (iri (URL. iri-str))
                (QName. "rdf" "type")))
         (is (= (iri (URL. iri-unk))
                (Iri. iri-unk))))

(deftest test-literal
         (is (= (str (Literal. "foo" nil nil))
                "\"foo\""))
         (is (= (str (Literal. "foo" (QName. "xsd" "string") nil))
                "\"foo\"^^xsd:string"))
         (is (= (str (Literal. "foo" (to-iri (QName. "xsd" "string")) nil))
                "\"foo\"^^<http://www.w3.org/2001/XMLSchema#string>"))
         (is (= (str (Literal. "foo" nil "en"))
                "\"foo\"@en")))

(deftest test-lit
         (is (= (lit "foo")
                (Literal. "foo" nil nil)))
         (is (= (lit "foo" (QName. "xsd" "string"))
                (Literal. "foo" (QName. "xsd" "string") nil)))
         (is (= (lit "foo" (to-iri (QName. "xsd" "string")))
                (Literal. "foo" (Iri. "http://www.w3.org/2001/XMLSchema#string") nil)))
         (is (= (lit "foo" (URI. "http://www.w3.org/2001/XMLSchema#string"))
                (Literal. "foo" (QName. "xsd" "string") nil)))
         (is (= (lit "foo" "en")
                (Literal. "foo" nil "en"))))

(deftest test-convert
         (is (= (subs (str (subject nil)) 0 2) "_:"))
         (is (thrown? Exception (predicate nil)))
         (is (= (subs (str (object nil)) 0 2) "_:"))
         (is (= (subject iri-str) (QName. "rdf" "type")))
         (is (= (predicate iri-str) (QName. "rdf" "type")))
         (is (= (object iri-str) (Literal. iri-str nil nil)))
         (is (= (subject (URI. iri-str)) (QName. "rdf" "type")))
         (is (= (predicate (URI. iri-str)) (QName. "rdf" "type")))
         (is (= (object (URI. iri-str)) (QName. "rdf" "type")))
         (is (= (subject (URL. iri-str)) (QName. "rdf" "type")))
         (is (= (predicate (URL. iri-str)) (QName. "rdf" "type")))
         (is (= (object (URL. iri-str)) (QName. "rdf" "type")))
         (is (= (subject :rdf/type) (QName. "rdf" "type")))
         (is (= (predicate :rdf/type) (QName. "rdf" "type")))
         (is (= (object :rdf/type) (QName. "rdf" "type")))
         (is (= (subject (Iri. iri-str)) (Iri. iri-str)))
         (is (= (predicate (Iri. iri-str)) (Iri. iri-str)))
         (is (= (object (Iri. iri-str)) (Iri. iri-str)))
         (is (= (subject (QName. "rdf" "type")) (QName. "rdf" "type")))
         (is (= (predicate (QName. "rdf" "type")) (QName. "rdf" "type")))
         (is (= (object (QName. "rdf" "type")) (QName. "rdf" "type")))
         (is (thrown? Exception (subject 5)))
         (is (thrown? Exception (predicate 5)))
         (is (= (object 5) (Literal. "5" (QName. "xsd" "integer") nil)))
         (is (thrown? Exception (subject 5.1)))
         (is (thrown? Exception (predicate 5.1)))
         (is (= (object 5.1) (Literal. "5.1" (QName. "xsd" "double") nil))))

(deftest test-?convert
         (is (= (subs (str (?subject nil)) 0 2) "_:"))
         (is (thrown? Exception (?predicate nil)))
         (is (= (subs (str (?object nil)) 0 2) "_:"))
         (is (= (?subject iri-str) (QName. "rdf" "type")))
         (is (= (?predicate iri-str) (QName. "rdf" "type")))
         (is (= (?object iri-str) (Literal. iri-str nil nil)))
         (is (= (?subject (URI. iri-str)) (QName. "rdf" "type")))
         (is (= (?predicate (URI. iri-str)) (QName. "rdf" "type")))
         (is (= (?object (URI. iri-str)) (QName. "rdf" "type")))
         (is (= (?subject (URL. iri-str)) (QName. "rdf" "type")))
         (is (= (?predicate (URL. iri-str)) (QName. "rdf" "type")))
         (is (= (?object (URL. iri-str)) (QName. "rdf" "type")))
         (is (= (?subject :rdf/type) (QName. "rdf" "type")))
         (is (= (?predicate :rdf/type) (QName. "rdf" "type")))
         (is (= (?object :rdf/type) (QName. "rdf" "type")))
         (is (= (?subject (Iri. iri-str)) (Iri. iri-str)))
         (is (= (?predicate (Iri. iri-str)) (Iri. iri-str)))
         (is (= (?object (Iri. iri-str)) (Iri. iri-str)))
         (is (= (?subject (QName. "rdf" "type")) (QName. "rdf" "type")))
         (is (= (?predicate (QName. "rdf" "type")) (QName. "rdf" "type")))
         (is (= (?object (QName. "rdf" "type")) (QName. "rdf" "type")))
         (is (thrown? Exception (?subject 5)))
         (is (thrown? Exception (?predicate 5)))
         (is (= (?object 5) (Literal. "5" (QName. "xsd" "integer") nil)))
         (is (thrown? Exception (?subject 5.1)))
         (is (thrown? Exception (?predicate 5.1)))
         (is (= (?object 5.1) (Literal. "5.1" (QName. "xsd" "double") nil)))
         (is (= (?subject ?) ?))
         (is (= (?predicate ?) ?))
         (is (= (?object ?) ?)))

