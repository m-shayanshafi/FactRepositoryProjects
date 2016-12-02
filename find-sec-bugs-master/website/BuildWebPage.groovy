import groovy.text.SimpleTemplateEngine

System.setProperty("file.encoding", "UTF-8");

String metaDataDir = "../plugin/src/main/resources/metadata"

//Contains the bug descriptions
InputStream messagesStreamEn = new FileInputStream(new File(metaDataDir,"messages.xml"))
InputStream messagesStreamJa = new FileInputStream(new File(metaDataDir,"messages_ja.xml"))

//Html Template of the documentation page
def getTemplateReader(String path) {
     return new InputStreamReader(
            new FileInputStream(new File("templates",path)), "UTF-8");
}

//Generated page will be place there
outDir = "out_web/"

//Loading detectors

println "Importing message from messages.xml"
bugsBindingEn = buildMapping(messagesStreamEn)
bugsBindingEn['lang'] = 'en'
bugsBindingJa = buildMapping(messagesStreamJa)
bugsBindingJa['lang'] = 'ja'

def buildMapping(InputStream xmlStream) {
    rootXml = new XmlParser().parse(new InputStreamReader(xmlStream,"UTF-8"))
    println "Mapping the descriptions to the templates"
    def bugsBinding = [:]
    bugsBinding['bugPatterns'] = []

    bugsBinding['nbPatterns'] = 0
    bugsBinding['nbDetectors'] = 0

    rootXml.BugPattern.each { pattern ->
        bugsBinding['bugPatterns'].add(
                ['title': pattern.ShortDescription.text().replaceAll(" in \\{1\\}",""),
                 'description': pattern.Details.text(),
                 'type':pattern.attribute("type")])
        println pattern.ShortDescription.text()
        bugsBinding['nbPatterns']++
    }

    rootXml.Detector.each { detector ->
        bugsBinding['nbDetectors']++
    }
    return bugsBinding;
}


//Version and download links

downloadUrl = "http://search.maven.org/remotecontent?filepath=com/h3xstream/findsecbugs/findsecbugs-plugin/1.4.4/findsecbugs-plugin-1.4.4.jar"
mavenCentralSearch = "http://search.maven.org/#search|gav|1|g:%22com.h3xstream.findsecbugs%22 AND a:%22findsecbugs-plugin%22"
latestVersion = "1.4.4"
latestUpdateDate = "20th November 2015"

//Screenshots

screenshots = []
screenshots.add(['title':'Eclipse',
                 'description':'<a href="http://marketplace.eclipse.org/content/findbugs-eclipse-plugin">Eclipse plugin</a> with FindSecurityBugs detectors.',
                 'path':'images/screens/eclipse.png'])
screenshots.add(['title':'IntelliJ / Android Studio',
                 'description':'<a href="https://plugins.jetbrains.com/plugin/3847?pr=idea">IntelliJ plugin</a> with FindSecurityBugs detectors.',
                 'path':'images/screens/intellij.png'])
screenshots.add(['title':'Sonar Qube',
                 'description':'<a href="http://docs.sonarqube.org/display/SONAR/Findbugs+Plugin">Sonar Qube</a> with FindBugs plugin (version 3.2+).',
                 'path':'images/screens/sonar.png'])

//Generate

def engine = new SimpleTemplateEngine()

println "Writing the template to ${outDir}/index.htm"



new File(outDir,"index.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'Home','section':'home','pageRedirect':''])
        w << engine.createTemplate(getTemplateReader("/home.htm")).make(['latestVersion':latestVersion,
                                                                         'latestUpdateDate':latestUpdateDate,
                                                                         'nbPatterns':bugsBindingEn['nbPatterns'],
                                                                         'screenshots':screenshots])
        w << engine.createTemplate(getTemplateReader("/social.htm")).make()
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

new File(outDir,"download.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(
                ['title':'Download','section':'download','pageRedirect':'download.htm'])
        w << engine.createTemplate(getTemplateReader("/download.htm")).make(
                ['downloadUrl':downloadUrl,'latestVersion':latestVersion,'mavenCentralSearch':mavenCentralSearch])
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

new File(outDir,"tutorials.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'Tutorials','section':'tutorials','pageRedirect':'tutorials.htm'])
        w << engine.createTemplate(getTemplateReader("/tutorials.htm")).make()
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

new File(outDir,"security.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'Getting Started in Security','section':'tutorials','pageRedirect':'security.htm'])
        w << engine.createTemplate(getTemplateReader("/security.htm")).make()
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

new File(outDir,"bugs.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'Bug Patterns','section':'bugs','pageRedirect':'bugs.htm'])
        w << engine.createTemplate(getTemplateReader("/bugs.htm")).make(bugsBindingEn)
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

new File(outDir,"bugs_ja.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'Bug Patterns','section':'bugs','pageRedirect':'bugs_ja.htm'])
        w << engine.createTemplate(getTemplateReader("/bugs.htm")).make(bugsBindingJa)
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}


new File(outDir,"license.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'License','section':'license','pageRedirect':'license.htm'])
        w << engine.createTemplate(getTemplateReader("/license.htm")).make()
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}