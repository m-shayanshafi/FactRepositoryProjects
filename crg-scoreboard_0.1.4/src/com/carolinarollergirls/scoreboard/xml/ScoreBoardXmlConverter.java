package com.carolinarollergirls.scoreboard.xml;

import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.defaults.*;

public class ScoreBoardXmlConverter
{
	/*****************************/
	/* ScoreBoard to XML methods */

	public String toString(ScoreBoard scoreBoard) throws TransformerException {
		return editor.toString(toDocument(scoreBoard));
	}

	public Document toDocument(ScoreBoard scoreBoard) {
		Document d = editor.createDocument("ScoreBoard");
		Element sb = editor.getElement(d.getDocumentElement(), "ScoreBoard");
		editor.setElement(sb, "TimeoutOwner", null, scoreBoard.getTimeoutOwner());

		Iterator<Clock> clocks = scoreBoard.getClocks().iterator();
		while (clocks.hasNext())
			toElement(sb, clocks.next());

		Iterator<Team> teams = scoreBoard.getTeams().iterator();
		while (teams.hasNext())
			toElement(sb, teams.next());

		Iterator<ScoreBoardImage> images = scoreBoard.getScoreBoardImages().iterator();
		while (images.hasNext())
			toElement(sb, images.next());

		Iterator<Policy> policies = scoreBoard.getPolicies().iterator();
		while (policies.hasNext())
			toElement(sb, policies.next());

		return d;
	}

	public Element toElement(Element sb, Clock c) {
		Element e = editor.setElement(sb, "Clock", c.getId());
		editor.setElement(e, "Name", null, c.getName());
		editor.setElement(e, "Number", null, String.valueOf(c.getNumber()));
		editor.setElement(e, "MinimumNumber", null, String.valueOf(c.getMinimumNumber()));
		editor.setElement(e, "MaximumNumber", null, String.valueOf(c.getMaximumNumber()));
		editor.setElement(e, "Time", null, String.valueOf(c.getTime()));
		editor.setElement(e, "MinimumTime", null, String.valueOf(c.getMinimumTime()));
		editor.setElement(e, "MaximumTime", null, String.valueOf(c.getMaximumTime()));
		editor.setElement(e, "Running", null, String.valueOf(c.isRunning()));
		editor.setElement(e, "Direction", null, String.valueOf(c.isCountDirectionDown()));
		return e;
	}

	public Element toElement(Element sb, Team t) {
		Element e = editor.setElement(sb, "Team", t.getId());
		editor.setElement(e, "Name", null, t.getName());
		editor.setElement(e, "Score", null, String.valueOf(t.getScore()));
		editor.setElement(e, "Timeouts", null, String.valueOf(t.getTimeouts()));
		editor.setElement(e, "LeadJammer", null, String.valueOf(t.isLeadJammer()));

		toElement(e, t.getTeamLogo());

		Iterator<String> positions = t.getPositions().iterator();
		while (positions.hasNext()) {
			String position = positions.next();
			Skater skater = t.getPosition(position);
			editor.setElement(e, "Position", position, (null==skater?"":skater.getId()));
		}

		Iterator<Skater> skaters = t.getSkaters().iterator();
		while (skaters.hasNext())
			toElement(e, skaters.next());

		return e;
	}

	public Element toElement(Element sb, Policy p) {
		Element e = editor.setElement(sb, "Policy", p.getId());
		editor.setElement(e, "Name", null, p.getName());
		editor.setElement(e, "Description", null, p.getDescription());
		editor.setElement(e, "Enabled", null, String.valueOf(p.isEnabled()));

		Iterator<Policy.Parameter> parameters = p.getParameters().iterator();
		while (parameters.hasNext())
			toElement(e, parameters.next());

		return e;
	}

	public Element toElement(Element p, Policy.Parameter pp) {
		Element e = editor.setElement(p, "Parameter", pp.getName());
		editor.setElement(e, "Name", null, pp.getName());
		editor.setElement(e, "Type", null, pp.getType());
		editor.setElement(e, "Value", null, pp.getValue());
		return e;
	}

	public Element toElement(Element parent, ScoreBoardImage sbi) {
		Element e = editor.setElement(parent, "Image", sbi.getId());
		editor.setElement(e, "Name", null, sbi.getName());
		editor.setElement(e, "Type", null, sbi.getType());
		editor.setElement(e, "Directory", null, sbi.getDirectory());
		editor.setElement(e, "Filename", null, sbi.getFilename());
		return e;
	}

	public Element toElement(Element parent, TeamLogo tl) {
		Element e = editor.setElement(parent, "TeamLogo");
		editor.setElement(e, "Id", null, tl.getId());
		editor.setElement(e, "Name", null, tl.getName());
		editor.setElement(e, "Type", null, tl.getType());
		editor.setElement(e, "Directory", null, tl.getDirectory());
		editor.setElement(e, "Filename", null, tl.getFilename());
		return e;
	}

	public Element toElement(Element t, Skater s) {
		Element e = editor.setElement(t, "Skater", s.getId());
		editor.setElement(e, "Name", null, s.getName());
		editor.setElement(e, "Number", null, s.getNumber());
		editor.setElement(e, "Position", null, s.getPosition());
		editor.setElement(e, "LeadJammer", null, String.valueOf(s.isLeadJammer()));
		editor.setElement(e, "PenaltyBox", null, String.valueOf(s.isPenaltyBox()));
		editor.setElement(e, "CurrentPass", null, String.valueOf(s.getCurrentPass()));

		Iterator<Integer> passes = s.getPasses().iterator();
		while (passes.hasNext()) {
			int pass = passes.next().intValue();
			toElement(e, s.getPass(pass));
		}
		return e;
	}

	public Element toElement(Element s, Pass p) {
		Element e = editor.setElement(s, "Pass", p.getId());
		editor.setElement(e, "Number", null, String.valueOf(p.getNumber()));
		editor.setElement(e, "Score", null, String.valueOf(p.getScore()));
		return e;
	}

	/*****************************/
	/* XML to ScoreBoard methods */

	public void processDocument(ScoreBoardModel scoreBoardModel, Document document) {
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			Element element;
			try { element = (Element)list.item(i); } catch ( ClassCastException ccE ) { continue; }
			if (element.getNodeName().equals("ScoreBoard"))
				processScoreBoard(scoreBoardModel, element);
		}
	}

	public void processScoreBoard(ScoreBoardModel scoreBoardModel, Element scoreBoard) {
		String call = scoreBoard.getAttribute("call");
		scoreBoard.removeAttribute("call");
		scoreBoard.removeAttribute("add");
		scoreBoard.removeAttribute("remove");

		if (call.equals("startJam"))
			scoreBoardModel.startJam();
		else if (call.equals("stopJam"))
			scoreBoardModel.stopJam();
		else if (call.equals("timeout"))
			scoreBoardModel.timeout();
		else if (call.equals("unStartJam"))
			scoreBoardModel.unStartJam();
		else if (call.equals("unStopJam"))
			scoreBoardModel.unStopJam();
		else if (call.equals("unTimeout"))
			scoreBoardModel.unTimeout();
		else if (call.equals("updateImages"))
			scoreBoardModel.updateScoreBoardImageModels();

		NodeList list = scoreBoard.getChildNodes();
		List<Element> removeList = new LinkedList<Element>();
		for (int i=0; i<list.getLength(); i++) {
			Element element;
			try { element = (Element)list.item(i); } catch ( ClassCastException ccE ) { continue; }
			try {
				String name = element.getNodeName();
				String value = editor.getContent(element);

				if (name.equals("TimeoutOwner"))
					scoreBoardModel.setTimeoutOwner(value);
				else if (name.equals("Clock"))
					processClock(scoreBoardModel, element);
				else if (name.equals("Team"))
					processTeam(scoreBoardModel, element);
				else if (name.equals("ScoreBoardImage"))
					processScoreBoardImage(scoreBoardModel, element);
				else if (name.equals("Policy"))
					processPolicy(scoreBoardModel, element);
				else
					continue;
			} catch ( Exception e ) {
			}
			scoreBoard.removeChild(element);
		}
	}

	public void processClock(ScoreBoardModel scoreBoardModel, Element clock) {
		String id = clock.getAttribute("Id");
		ClockModel clockModel = scoreBoardModel.getClockModel(id);

		NodeList list = clock.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			Element element;
			try { element = (Element)list.item(i); } catch ( ClassCastException ccE ) { continue; }
			try {
				String name = element.getNodeName();
				String value = editor.getContent(element);

				boolean isChange = Boolean.parseBoolean(element.getAttribute("change"));
				boolean isReset = Boolean.parseBoolean(element.getAttribute("reset"));

//FIXME - might be better way to handle changes/resets than an attribute...
				if ((null == value) && !isReset)
					continue;
				else if (name.equals("Name"))
					clockModel.setName(value);
				else if (name.equals("Number") && isChange)
					clockModel.changeNumber(Integer.parseInt(value));
				else if (name.equals("Number") && !isChange)
					clockModel.setNumber(Integer.parseInt(value));
				else if (name.equals("MinimumNumber"))
					clockModel.setMinimumNumber(Integer.parseInt(value));
				else if (name.equals("MaximumNumber"))
					clockModel.setMaximumNumber(Integer.parseInt(value));
				else if (name.equals("Time") && isChange)
					clockModel.changeTime(Long.parseLong(value));
				else if (name.equals("Time") && isReset)
					clockModel.resetTime();
				else if (name.equals("Time") && !isChange && !isReset)
					clockModel.setTime(Long.parseLong(value));
				else if (name.equals("MinimumTime"))
					clockModel.setMinimumTime(Long.parseLong(value));
				else if (name.equals("MaximumTime"))
					clockModel.setMaximumTime(Long.parseLong(value));
				else if (name.equals("Running") && Boolean.parseBoolean(value))
					clockModel.start();
				else if (name.equals("Running") && !Boolean.parseBoolean(value))
					clockModel.stop();
				else if (name.equals("Direction"))
					clockModel.setCountDirectionDown(Boolean.parseBoolean(value));
			} catch ( Exception e ) {
			}
		}
	}

	public void processTeam(ScoreBoardModel scoreBoardModel, Element team) {
		String id = team.getAttribute("Id");
		TeamModel teamModel = scoreBoardModel.getTeamModel(id);

		String call = team.getAttribute("call");
		team.removeAttribute("call");

		if (call.equals("timeout"))
			teamModel.timeout();

		NodeList list = team.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			Element element;
			try { element = (Element)list.item(i); } catch ( ClassCastException ccE ) { continue; }
			try {
				String name = element.getNodeName();
				String eId = element.getAttribute("Id");
				String value = editor.getContent(element);

				boolean isChange = Boolean.parseBoolean(element.getAttribute("change"));

				if (name.equals("Skater"))
					processSkater(teamModel, element);
				else if (name.equals("TeamLogo"))
					processTeamLogo(teamModel, element);
				else if (null == value)
					continue;
				else if (name.equals("Name"))
					teamModel.setName(value);
				else if (name.equals("Score") && isChange)
					teamModel.changeScore(Integer.parseInt(value));
				else if (name.equals("Score") && !isChange)
					teamModel.setScore(Integer.parseInt(value));
				else if (name.equals("Timeouts") && isChange)
					teamModel.changeTimeouts(Integer.parseInt(value));
				else if (name.equals("Timeouts") && !isChange)
					teamModel.setTimeouts(Integer.parseInt(value));
				else if (name.equals("Position") && null == value)
					teamModel.clearPosition(eId);
				else if (name.equals("Position"))
					teamModel.setPosition(eId, value);
				else if (name.equals("LeadJammer"))
					teamModel.setLeadJammer(Boolean.parseBoolean(value));
			} catch ( Exception e ) {
			}
		}
	}

	public void processTeamLogo(TeamModel teamModel, Element teamLogo) {
		TeamLogoModel teamLogoModel = teamModel.getTeamLogoModel();

		NodeList list = teamLogo.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			Element element;
			try { element = (Element)list.item(i); } catch ( ClassCastException ccE ) { continue; }
			try {
				String name = element.getNodeName();
				String eId = element.getAttribute("Id");
				String value = editor.getContent(element);

				if (null == value)
					continue;
				else if (name.equals("Id"))
					teamLogoModel.setId(value);
			} catch ( Exception e ) {
			}
		}
	}

	public void processScoreBoardImage(ScoreBoardModel scoreBoardModel, Element scoreBoardImage) {
		String id = scoreBoardImage.getAttribute("Id");
		ScoreBoardImageModel scoreBoardImageModel = scoreBoardModel.getScoreBoardImageModel(id);

		if (Boolean.parseBoolean(scoreBoardImage.getAttribute("remove"))) {
			scoreBoardImage.getParentNode().removeChild(scoreBoardImage);
			scoreBoardModel.removeScoreBoardImageModel(id);
			return;
		}

		if (Boolean.parseBoolean(scoreBoardImage.getAttribute("add")) && (scoreBoardImageModel == null)) {
			String type = editor.getContent(editor.getElement(scoreBoardImage, "Type"));
			String directory = editor.getContent(editor.getElement(scoreBoardImage, "Directory"));
			String filename = editor.getContent(editor.getElement(scoreBoardImage, "Filename"));
			String name = editor.getContent(editor.getElement(scoreBoardImage, "Name"));
			scoreBoardModel.addScoreBoardImageModel(new DefaultScoreBoardImageModel(scoreBoardModel, id, type, directory, filename, name));
			scoreBoardImageModel = scoreBoardModel.getScoreBoardImageModel(id);
		}

		if (scoreBoardImageModel == null)
			return;

		NodeList list = scoreBoardImage.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			Element element;
			try { element = (Element)list.item(i); } catch ( ClassCastException ccE ) { continue; }
			try {
				String name = element.getNodeName();
				String eId = element.getAttribute("Id");
				String value = editor.getContent(element);

				if (null == value)
					continue;
				else if (name.equals("Name"))
					scoreBoardImageModel.setName(value);
			} catch ( Exception e ) {
			}
		}
	}

	public void processPolicy(ScoreBoardModel scoreBoardModel, Element policy) throws NoSuchElementException {
		String id = policy.getAttribute("Id");
		PolicyModel policyModel = scoreBoardModel.getPolicyModel(id);

		NodeList list = policy.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			Element element;
			try { element = (Element)list.item(i); } catch ( ClassCastException ccE ) { continue; }
			try {
				String name = element.getNodeName();
				String eId = element.getAttribute("Id");
				String value = editor.getContent(element);

				if (name.equals("Parameter"))
					processPolicyParameter(policyModel, element);
				else if (null == value)
					continue;
				else if (name.equals("Name"))
					policyModel.setName(value);
				else if (name.equals("Description"))
					policyModel.setDescription(value);
				else if (name.equals("Enabled"))
					policyModel.setEnabled(Boolean.parseBoolean(value));
			} catch ( Exception e ) {
			}
		}
	}

	public void processPolicyParameter(PolicyModel policyModel, Element parameter) throws NoSuchElementException {
		String id = parameter.getAttribute("Id");
		PolicyModel.ParameterModel parameterModel = policyModel.getParameterModel(id);

		NodeList list = parameter.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			Element element;
			try { element = (Element)list.item(i); } catch ( ClassCastException ccE ) { continue; }
			try {
				String name = element.getNodeName();
				String eId = element.getAttribute("Id");
				String value = editor.getContent(element);

				if (null == value)
					continue;
				else if (name.equals("Value"))
					parameterModel.setValue(value);
			} catch ( Exception e ) {
			}
		}
	}

	public void processSkater(TeamModel teamModel, Element skater) throws SkaterNotFoundException {
		String id = skater.getAttribute("Id");
		SkaterModel skaterModel;

		if (Boolean.parseBoolean(skater.getAttribute("remove"))) {
			skater.getParentNode().removeChild(skater);
			teamModel.removeSkaterModel(id);
			return;
		}

		try {
			skaterModel = teamModel.getSkaterModel(id);
		} catch ( SkaterNotFoundException snfE ) {
			//FIXME - always add if skater doesn't exist?  Or how to handle loading from XML where the skaters do not have add attributes?
			skater.setAttribute("add", "true");
			skaterModel = null;
		}

		if (Boolean.parseBoolean(skater.getAttribute("add")) && (skaterModel == null)) {
			skater.getParentNode().removeChild(skater);
			teamModel.addSkaterModel(new DefaultSkaterModel(teamModel, id, id, "", Team.POSITION_BENCH));
			skaterModel = teamModel.getSkaterModel(id);
		}

		NodeList list = skater.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			Element element;
			try { element = (Element)list.item(i); } catch ( ClassCastException ccE ) { continue; }
			try {
				String name = element.getNodeName();
				String value = editor.getContent(element);

				if (name.equals("Name"))
					skaterModel.setName(value);
				else if (name.equals("Number"))
					skaterModel.setNumber(value);
				else if (name.equals("Position"))
					skaterModel.setPosition(value);
				else if (name.equals("LeadJammer"))
					skaterModel.setLeadJammer(Boolean.parseBoolean(value));
				else if (name.equals("PenaltyBox"))
					skaterModel.setPenaltyBox(Boolean.parseBoolean(value));
				else if (name.equals("CurrentPass"))
					skaterModel.setCurrentPass(Integer.parseInt(value));
			} catch ( Exception e ) {
			}
		}
	}

	public void processPass(SkaterModel skaterModel, Element pass) {
		String id = pass.getAttribute("Id");
		Integer number;
		try {
			number = Integer.parseInt(id);
		} catch ( NumberFormatException nfE ) {
			return;
		}

		PassModel passModel = skaterModel.getPassModel(number.intValue());

		NodeList list = pass.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			Element element;
			try { element = (Element)list.item(i); } catch ( ClassCastException ccE ) { continue; }
			try {
				String name = element.getNodeName();
				String value = editor.getContent(element);

				if (name.equals("Score"))
					passModel.setScore(Integer.parseInt(value));
			} catch ( Exception e ) {
			}
		}
	}

	public static ScoreBoardXmlConverter getInstance() { return scoreBoardXmlConverter; }

	protected XmlDocumentEditor editor = new XmlDocumentEditor();

	private static ScoreBoardXmlConverter scoreBoardXmlConverter = new ScoreBoardXmlConverter();
}
