beans {
    xmlns context:"http://www.springframework.org/schema/context"
    xmlns camel: "http://camel.apache.org/schema/spring"

    context."component-scan"("base-package" : "adamatti")

    camel.camelContext(id: "camelContext", autoStartup:true) {
        camel.contextScan()
        camel.jmxAgent(id: "agent")
    }
}