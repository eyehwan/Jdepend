/*
 * The MIT License
 *
 * Copyright (c) 2011, Marco Ambu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package hudson.plugins.jdepend.portlet;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.view.dashboard.DashboardPortlet;
import org.kohsuke.stapler.DataBoundConstructor;

/**
*
* @author Yehui Wang
*/
public class JdependPortlet extends DashboardPortlet {
	
	private int numJobs = 5;
	
	@DataBoundConstructor
	public JdependPortlet(String name,int numJobs) {
		super(name);
		this.numJobs = numJobs;
		
	}
	
	public int getNumJobs() {
	    return numJobs <= 0 ? 5 : numJobs;
	  }
		
	public String getTimestampSortData(Run run) {
	    return String.valueOf(run.getTimeInMillis());
	  }
	
	public String getTimestampString(Run run) {
	    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date(run.getTimeInMillis()));
	  }
		
		/**
		 * Last <code>N_LATEST_BUILDS</code> builds
		 *
		 */
		public List<Run> getFinishedBuilds() {
	    List<Job> jobs = getDashboard().getJobs();
			List<Run> allBuilds = new ArrayList<Run>();
			for (Job job : jobs) {
				Run builds = job.getBuilds().getLastBuild();
				allBuilds.add(builds);
			}
			Collections.sort(allBuilds, Run.ORDER_BY_DATE);
			List<Run> recentBuilds = new ArrayList<Run>();
			if(allBuilds.size() < getNumJobs())
				recentBuilds = allBuilds;
			else
				recentBuilds = allBuilds.subList(0,getNumJobs());
				
			return recentBuilds;
		}

	  public String getBuildColumnSortData(Run<?, ?> build) {
	    return String.valueOf(build.getNumber());
	  }
	  
	

	@Extension
    public static class DescriptorImpl extends Descriptor<DashboardPortlet> {

		@Override
		public String getDisplayName() {
			return "Jdepend statistics";
		}
	}
}
