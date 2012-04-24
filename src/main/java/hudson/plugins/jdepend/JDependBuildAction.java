/**
 * 
 */
package hudson.plugins.jdepend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hudson.model.AbstractBuild; //Base implementation of Runs that build software. For now this is primarily the common part of Build and MavenBuild.
import hudson.model.Action;//an interface :String getIconFileName();String getDisplayName();String getUrlName()

/**
 * A build action to generate JDepend HTML reports
 * @author cflewis
 *
 */

//A job is an runnable entity under the monitoring of Hudson.Every time it "runs", 
//it will be recorded as a Run object.To create a custom job type, extend TopLevelItemDescriptor and put Extension on it.

//A particular execution of Job. Custom Run type is always used in conjunction with a custom Job type, 
//so there's no separate registration mechanism for custom Run types.

public class JDependBuildAction implements Action 
{
	public final AbstractBuild<?, ?> build;
	private final JDependParser jDependParser;
	private String htmlReport;
	
	public JDependBuildAction(AbstractBuild<?, ?> build, JDependParser jDependParser)
	{
		super();
		this.build = build;
		this.jDependParser = jDependParser;
    	JDependReporter r = new JDependReporter(jDependParser);
    	
    	try {
    		htmlReport = r.getReport();
    	}
    	catch (Exception e) {
    		htmlReport = "Report generation failed: " + e;
    	}
	}
	
	/** 
	 * Return the JDepend display name
	 * @see hudson.model.Action#getDisplayName()
	 */
	public String getDisplayName() {
		return "JDepend";
	}

	/** 
	 * Return the JDepend icon path
	 * @see hudson.model.Action#getIconFileName()
	 */
	//C:\Users\eyehwan\.jenkins\war\images\24x24\graph.gif
	public String getIconFileName() {
		return "graph.gif";
	}

	/**
	 * Returns the path to the JDepend page
	 * @see hudson.model.Action#getUrlName()
	 */
	public String getUrlName() {
		return "jdepend";
	}

	/**
	 * Get the HTML string of the JDepend report.
	 * This report is HTML tidied, and had the <html><body> tags
	 * and such cruft removed.
	 * 
	 * @return JDepend HTML report
	 */
	public String getJDependHtml() {
		Pattern trimTop = Pattern.compile("^.*<body>", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Pattern trimBottom = Pattern.compile("</body>.*</html>", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		
		Matcher topMatcher = trimTop.matcher(htmlReport);
		htmlReport = topMatcher.replaceAll("");
		
		Matcher bottomMatcher = trimBottom.matcher(htmlReport);
		htmlReport = bottomMatcher.replaceAll("");

		return htmlReport;
	}

	public JDependParser getJDependParser() {
		return jDependParser;
	}
}
