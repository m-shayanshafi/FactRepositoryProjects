package com.carolinarollergirls.scoreboard.defaults;

import java.util.*;
import java.lang.reflect.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.event.*;
import com.carolinarollergirls.scoreboard.model.*;

public class DefaultPolicyModel extends PolicyListenerManager implements PolicyModel
{
	public DefaultPolicyModel() {
		id = getClass().getName().replaceFirst(getClass().getPackage().getName()+".", "");
		setName(id);
	}
	public DefaultPolicyModel(String i) {
		id = i;
		setName(getClass().getName().replaceFirst(getClass().getPackage().getName()+".", ""));
	}

	public ScoreBoard getScoreBoard() { return getScoreBoardModel(); }
	public ScoreBoardModel getScoreBoardModel() { return scoreBoardModel; }
	public void setScoreBoardModel(ScoreBoardModel sbm) throws Exception {
		if (scoreBoardModel != null)
			throw new IllegalStateException("This PolicyModel is already associated with a ScoreBoardModel!");

		scoreBoardModel = sbm;
	}

	public Policy getPolicy() { return this; }

	public String getId() { return id; }

	public String getName() { return name; }
	public void setName(String n) {
		name = n;
		policyChange(new PolicyNameEvent(this, name));
	}

	public String getDescription() { return description; }
	public void setDescription(String d) {
		description = d;
		policyChange(new PolicyDescriptionEvent(this, description));
	}

	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean e) {
		enabled = e;
		policyChange(new PolicyEnabledEvent(this, enabled));
	}

	public List<PolicyModel.ParameterModel> getParameterModels() { return Collections.unmodifiableList(new ArrayList<PolicyModel.ParameterModel>(parameters.values())); }
	public List<Policy.Parameter> getParameters() {
		List<Policy.Parameter> list = new ArrayList<Policy.Parameter>(parameters.size());
		Iterator<PolicyModel.ParameterModel> i = parameters.values().iterator();
		while (i.hasNext())
			list.add(i.next().getParameter());
		return Collections.unmodifiableList(list);
	}
	public PolicyModel.ParameterModel getParameterModel(String name) { return parameters.get(name); }
	public Policy.Parameter getParameter(String name) { try { return parameters.get(name).getParameter(); } catch ( NullPointerException npE ) { return null; } }

	protected void addParameterModel(PolicyModel.ParameterModel parameterModel) {
		parameters.put(parameterModel.getName(), parameterModel);
		parameterModel.addPolicyParameterListener(new PolicyParameterListener() {
				public void policyParameterChange(PolicyParameterEvent event) { policyChange(event); }
			});
	}

	protected ScoreBoardModel scoreBoardModel = null;
	protected Map<String,PolicyModel.ParameterModel> parameters = new Hashtable<String,PolicyModel.ParameterModel>();
	protected String id;
	protected String name = "No name";
	protected String description = "No description";
	protected boolean enabled = true;

	public class DefaultParameterModel extends PolicyParameterListenerManager implements PolicyModel.ParameterModel
	{
		public DefaultParameterModel(PolicyModel pM, String n, String t, String v) {
			policyModel = pM;
			name = n;
			type = t;
			value = v;

			try {
				constructor = Class.forName("java.lang."+type).getConstructor(new Class[]{ String.class });
			} catch ( Exception e ) {
				constructor = null;
			}
		}

		public PolicyModel getPolicyModel() { return policyModel; }
		public Policy getPolicy() { return getPolicyModel().getPolicy(); }

		public Policy.Parameter getParameter() { return this; }

		public String getName() { return name; }

		public String getValue() { return value; }

		public String getType() { return type; }

		public void setValue(String v) throws IllegalArgumentException {
			synchronized (valueLock) {
				try {
					if (null != constructor)
						constructor.newInstance(new Object[]{ v });
					value = v;
					policyParameterChange(new PolicyParameterValueEvent(this, value));
				} catch ( Exception e ) {
					throw new IllegalArgumentException("Invalid value ("+v+") : "+e.getMessage());
				}
			}
		}

		protected PolicyModel policyModel;
		protected String name;
		protected String type;
		protected String value;
		protected Object valueLock = new Object();
		protected Constructor<?> constructor;
	}
}
